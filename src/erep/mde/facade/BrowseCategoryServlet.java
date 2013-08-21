package erep.mde.facade;

import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import erep.mde.dao.ArticlesDAO;
import erep.mde.dao.ErepUserDAO;
import erep.mde.data.Article;

public class BrowseCategoryServlet extends HttpServlet {
	private static final long	serialVersionUID	= 1L;

	private static final Logger	logger				= Logger.getLogger(BrowseCategoryServlet.class
															.getName());

	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		ArticlesDAO artDAO = new ArticlesDAO();
		ErepUserDAO userDAO = new ErepUserDAO();

		try {

			Long categoryId = Long.valueOf(req.getParameter("id"));

			Collection<Article> articles = artDAO
					.getArticlesByCategory(categoryId);

			JSONArray array = new JSONArray();
			for (Article article : articles) {
				JSONObject jsArticle = new JSONObject();
				jsArticle.put("eRepId", article.geteRepId());
				jsArticle.put("title", article.getTitle());
				jsArticle.put("author", article.getAuthorId() != null ? userDAO
						.getUser(article.getAuthorId()).toJSON() : null);
				jsArticle.put("eRepDay", article.geteRepDay());
				jsArticle.put("category", categoryId);
				array.add(jsArticle);
			}

			resp.getWriter().write(array.toJSONString());

		} catch (NullPointerException e) {
			writeError(resp, e);
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
		} catch (NumberFormatException e) {
			writeError(resp, e);
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
		} catch (Exception e) {
			writeError(resp, e);
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} finally {
			artDAO.close();
			userDAO.close();
		}
		// TODO 503 Service Unavailable
	}

	@SuppressWarnings("unchecked")
	private void writeError(HttpServletResponse resp, Exception e)
			throws IOException {
		logger.log(Level.WARNING, e.getMessage(), e);
		JSONObject json = new JSONObject();
		json.put("result", "error");
		json.put("message", e.getMessage());
		resp.getWriter().write(json.toString());
	}
}
