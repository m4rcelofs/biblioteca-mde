package erep.mde.tasks;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import erep.mde.dao.ErepUserDAO;
import erep.mde.data.ErepUser;
import erep.mde.erpk.Fetcher;

public class FetchUserTask extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		ErepUserDAO dao = new ErepUserDAO();
		try {

			Long userId = Long.valueOf(req.getParameter("id"));

			ErepUser user = dao.getUserUncached(userId);

			if (user.geteRepName() == null)
				fetchUserById(user);
			else
				if (user.geteRepId() == null)
					fetchUserByName(user);

			dao.save(user);
			dao.removeFromCache(user);
		} finally {
			dao.close();
		}

	}

	private void fetchUserById(ErepUser user) throws IOException {
		try {
			if (user.geteRepId().trim().length() < 5)
				return;
			JSONObject jsUser = new Fetcher().fetchUser(user.geteRepId());
			user.seteRepName(jsUser.get("name").toString());
		} catch (ParseException e) {
			throw new IOException(e);
		}
	}

	private void fetchUserByName(ErepUser user) throws IOException {
		try {
			JSONArray results = new Fetcher().searchUser(user.geteRepName());
			if (results.size() > 0) {
				JSONObject jsUser = (JSONObject) results.iterator().next();
				if (user.geteRepName().equalsIgnoreCase(
						jsUser.get("name").toString()))
					user.seteRepId(jsUser.get("id").toString());
			}
		} catch (ParseException e) {
			throw new IOException(e);
		}
	}
}
