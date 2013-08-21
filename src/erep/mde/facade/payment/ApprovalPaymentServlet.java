package erep.mde.facade.payment;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import erep.mde.dao.PaymentDAO;

public class ApprovalPaymentServlet extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	private static final Logger	logger				= Logger.getLogger(ListPaymentsServlet.class
															.getName());

	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		PaymentDAO payDao = new PaymentDAO();

		try {

			// TODO

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
		}
		// TODO 503 Service Unavailable
	}

	@SuppressWarnings("unchecked")
	private void writeOK(HttpServletResponse resp) throws IOException {
		JSONObject json = new JSONObject();
		json.put("result", "ok");
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
