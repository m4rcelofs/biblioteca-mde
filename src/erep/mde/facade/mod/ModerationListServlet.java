package erep.mde.facade.mod;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.google.appengine.labs.repackaged.org.json.JSONArray;

import erep.mde.dao.ArticlesDAO;
import erep.mde.dao.ErepUserDAO;
import erep.mde.data.Article;

public class ModerationListServlet extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	private static final Logger	logger				= Logger.getLogger(ModerationListServlet.class
															.getName());

	/**
	 * gets unaproved articles
	 **/
	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		ArticlesDAO dao = new ArticlesDAO();
		ErepUserDAO userDao = new ErepUserDAO();
		DateFormat formatter = new SimpleDateFormat(
				"yyyy.MM.dd, HH:mm:ss 'UTC'Z");
		formatter.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));

		try {
			JSONArray array = new JSONArray();
			for (Article article : dao.getNewArticles()) {
				JSONObject jsArticle = new JSONObject();
				jsArticle.put("id", article.getId());
				jsArticle.put("erepId", article.geteRepId());
				jsArticle.put("description", article.getDescription());
				jsArticle.put(
						"sentOn",
						article.getSentOn() != null ? formatter.format(article
								.getSentOn()) : null);
				jsArticle.put("category", article.getCategoryId());
				jsArticle.put("sender", article.getSenderId() != null ? userDao
						.getUser(article.getSenderId()).toJSON() : null);

				array.put(jsArticle);
			}

			resp.getWriter().write(array.toString());
		} catch (Exception e) {
			writeError(resp, e);
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} finally {
			dao.close();
			userDao.close();
		}
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
