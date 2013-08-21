package erep.mde.tasks;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.google.appengine.api.datastore.Text;

import erep.mde.IndexController;
import erep.mde.dao.ArticlesDAO;
import erep.mde.dao.ErepUserDAO;
import erep.mde.data.Article;
import erep.mde.data.ErepUser;
import erep.mde.erpk.Fetcher;

public class FetchArticleTask extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		ArticlesDAO dao = new ArticlesDAO();
		try {

			Long id = Long.valueOf(req.getParameter("id"));

			Article article = dao.getArticle(id);

			fetchArticle(article);

			dao.save(article);

			dao.removeCategoryFromCache(article.getCategoryId());
			dao.removeAuthorFromCache(article.getAuthorId());

		} finally {
			dao.close();
		}

	}

	private void fetchArticle(Article article) throws IOException {

		ErepUserDAO userDAO = new ErepUserDAO();
		try {
			JSONObject jsArticle = new Fetcher().fetchArticle(article
					.geteRepId());

			if (jsArticle == null
					|| jsArticle.containsKey("status")
					&& "deleted".equalsIgnoreCase(jsArticle.get("status")
							.toString())) {
				article.setTitle("Deleted Article");
				article.setIsApproved(false);
				article.setPaymentId(null);
				return;
			}

			Integer day = Integer.valueOf(jsArticle.get("eRepDay").toString());
			article.seteRepDay(day);

			String title = jsArticle.get("title").toString();
			article.setTitle(title);

			if (article.getAuthorId() == null) {
				ErepUser author = userDAO.findUserByName(jsArticle
						.get("author").toString());
				article.setAuthorId(author.getId());
			}

			article.setContent(new Text(jsArticle.get("content").toString()));

			IndexController index = new IndexController();
			boolean added = index.index(article);
			if (!added)
				throw new IllegalStateException("Document could not be added");

		} catch (ParseException e) {
			throw new IOException(e);
		} finally {
			userDAO.close();
		}
	}
}
