package erep.mde.facade;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;

import erep.mde.IndexController;
import erep.mde.dao.ArticlesDAO;

public class SearchServlet extends HttpServlet {
	private static final long	serialVersionUID	= 1L;

	private static final Logger	logger				= Logger.getLogger(SearchServlet.class
															.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		ArticlesDAO artDAO = new ArticlesDAO();

		try {

			String text = null;
			if (req.getParameter("text") != null)
				text = req.getParameter("text").trim();

			String authorId = null;
			if (req.getParameter("authorId") != null)
				authorId = req.getParameter("authorId").trim();

			String[] cats = req.getParameterValues("categoryId");

			if ((text == null || text.isEmpty())
					&& (cats == null || cats.length == 0) && authorId != null
					&& !authorId.trim().isEmpty()) {
				resp.sendRedirect("browse/author?id=" + authorId);
				return;
			}

			if ((text == null || text.isEmpty())
					&& (authorId == null || authorId.isEmpty()) && cats != null
					&& cats.length == 1) {
				resp.sendRedirect("browse/category?id=" + cats[0]);
				return;
			}

			String query = buildQuery(text, authorId, cats);

			if (query.trim().isEmpty()) {
				resp.getWriter().write(new JSONArray().toJSONString());
				return;
			}

			JSONArray array = convertResults(new IndexController()
					.search(query));

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
		}
		// TODO 503 Service Unavailable

	}

	@SuppressWarnings("unchecked")
	private JSONArray convertResults(Results<ScoredDocument> search) {
		JSONArray array = new JSONArray();

		for (ScoredDocument result : search) {
			JSONObject obj = new JSONObject();

			obj.put("eRepId", result.getId());
			obj.put("title", result.getOnlyField("title").getText());
			obj.put("category", result.getOnlyField("category").getAtom());

			JSONObject author = new JSONObject();
			author.put("id", result.getOnlyField("authorId").getAtom());
			author.put("name", result.getOnlyField("authorName").getAtom());
			obj.put("author", author);

			obj.put("eRepDay", result.getOnlyField("eRepDay").getNumber());

			array.add(obj);
		}

		return array;
	}

	private String buildQuery(String text, String authorId, String[] cats) {
		StringBuilder builder = new StringBuilder();

		if (text != null && !text.isEmpty()) {
			String[] searchTerms = text.split(" ");
			for (String term : searchTerms) {
				if (builder.length() > 0)
					builder.append(" AND ");
				builder.append("(title:\"" + term + "\"")
						.append(" OR description:\"" + term + "\"")
						.append(" OR content:\"" + term + "\")");
			}
		}

		if (authorId != null && !authorId.isEmpty()) {
			if (builder.length() > 0)
				builder.append(" AND ");
			builder.append("(authorId:" + authorId + ")");
		}

		if (cats != null && cats.length > 0 && cats[0] != null
				&& !cats[0].trim().isEmpty()) {
			if (builder.length() > 0)
				builder.append(" AND ");
			builder.append("(");

			for (int i = 0; i < cats.length; i++) {
				if (i > 0)
					builder.append(" OR ");
				builder.append("category:" + cats[i]);
			}

			builder.append(")");
		}

		logger.log(Level.INFO, "Query: " + builder.toString());
		return builder.toString();
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
