package erep.mde.facade.mod;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import erep.mde.dao.ArticlesDAO;
import erep.mde.dao.ErepUserDAO;
import erep.mde.dao.ModerationDAO;
import erep.mde.data.Article;

public class RejectedServlet extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	private static final Logger	logger				= Logger.getLogger(RejectedServlet.class
															.getName());

	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		ArticlesDAO artDao = new ArticlesDAO();
		ErepUserDAO userDao = new ErepUserDAO();
		ModerationDAO modDao = new ModerationDAO();

		try {

			JSONArray array = new JSONArray();
			SimpleDateFormat date = new SimpleDateFormat(
					"yyyy.MM.dd, HH:mm:ss Z");

			for (Article a : artDao.getRejectedArticles()) {
				JSONObject obj = new JSONObject();
				obj.put("id", a.getId());
				obj.put("eRepId", a.geteRepId());
				obj.put("category", a.getCategoryId());
				obj.put("description", a.getDescription());
				obj.put("sender",
						a.getSenderId() != null ? userDao.getUser(
								a.getSenderId()).toJSON() : null);
				obj.put("sendOn", date.format(a.getSentOn()));
				obj.put("moderator", a.getModeratorId() != null ? modDao
						.getModerator(a.getModeratorId()).toJSON() : null);
				obj.put("lastModified", date.format(a.getLastModified()));
				array.add(obj);
			}

			resp.getWriter().write(array.toJSONString());

		} finally {
			artDao.close();
			userDao.close();
			modDao.close();
		}

	}

}
