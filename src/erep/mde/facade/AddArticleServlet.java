package erep.mde.facade;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import erep.mde.dao.ArticlesDAO;
import erep.mde.dao.CategoryDAO;
import erep.mde.dao.ErepUserDAO;
import erep.mde.data.Article;

public class AddArticleServlet extends HttpServlet {
	private static final long		serialVersionUID	= 1L;

	private static final Logger		logger				= Logger.getLogger(AddArticleServlet.class
																.getName());

	private static final Pattern	tagsPattern			= Pattern
																.compile("(<(.|\n)*?>)|(\n|\t)");

	/**
	 * adds an article on the moderation list
	 **/
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		CategoryDAO catDAO = new CategoryDAO();
		ArticlesDAO artDAO = new ArticlesDAO();
		ErepUserDAO userDAO = new ErepUserDAO();

		try {

			String articleId = Long.valueOf(
					escapeHtml4(req.getParameter("articleId"))).toString();
			Long categoryId = Long.valueOf(req.getParameter("categoryId"));

			String descParameter = req.getParameter("description");
			String description = descParameter == null ? "" : tagsPattern
					.matcher(descParameter).replaceAll(" ");
			String userId = Long.valueOf(
					escapeHtml4(req.getParameter("userId"))).toString();

			checkNull(articleId, categoryId, description, userId);

			if (artDAO.exists(articleId)) {
				writeError(resp, new IllegalStateException(
						"Artigo já cadastrado"));
				return;
			}

			Article article = new Article();
			article.seteRepId(articleId);
			article.setCategoryId(categoryId);
			article.setDescription(description);

			if (userId.trim().length() > 5)
				article.setSenderId(userDAO.findUser(userId).getId());

			Date now = new Date();
			article.setSentOn(now);
			article.setLastModified(now);

			artDAO.save(article);

			writeOK(resp);

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
			catDAO.close();
			artDAO.close();
			userDAO.close();
		}
		// TODO 503 Service Unavailable

	}

	private void checkNull(Object... objs) throws NullPointerException {
		for (int i = 0; i < objs.length; i++)
			if (objs[i] == null)
				throw new NullPointerException("Missing parameter");
	}

	@SuppressWarnings("unchecked")
	private void writeOK(HttpServletResponse resp) throws IOException {
		JSONObject json = new JSONObject();
		json.put("result", "ok");
		json.put("message", "Artigo salvo com sucesso");
		resp.getWriter().write(json.toString());
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
