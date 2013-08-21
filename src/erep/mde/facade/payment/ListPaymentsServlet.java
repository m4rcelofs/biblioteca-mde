package erep.mde.facade.payment;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import erep.mde.dao.ErepUserDAO;
import erep.mde.dao.PaymentDAO;
import erep.mde.data.ErepUser;
import erep.mde.data.Payment;

public class ListPaymentsServlet extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	private static final Logger	logger				= Logger.getLogger(ListPaymentsServlet.class
															.getName());

	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		PaymentDAO payDao = new PaymentDAO();
		ErepUserDAO userDao = new ErepUserDAO();

		try {

			Multimap<ErepUser, Payment> payments = ArrayListMultimap.create();

			for (Payment p : payDao.getUnpaid()) {
				ErepUser user = userDao.getUser(p.getUserId());
				payments.put(user, p);
			}

			JSONArray array = new JSONArray();

			for (ErepUser user : payments.keys()) {
				JSONObject jsItem = new JSONObject();

				JSONObject jsUser = new JSONObject();
				jsUser.put("id", user.geteRepId());
				jsUser.put("name", user.geteRepName());
				jsItem.put("user", jsUser);

				JSONArray jsPayments = new JSONArray();
				for (Payment p : payments.get(user)) {
					JSONObject jsP = new JSONObject();
					jsP.put("id", p.getId());
					jsP.put("value", p.getValue());

					jsPayments.add(jsP);
				}
				array.add(jsPayments);
			}

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
			payDao.close();
			userDao.close();
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
