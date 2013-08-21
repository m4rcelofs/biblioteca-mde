package erep.mde.facade.mod;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.google.appengine.api.users.UserServiceFactory;

import erep.mde.QueueController;
import erep.mde.dao.ArticlesDAO;
import erep.mde.dao.ModerationDAO;
import erep.mde.dao.PaymentDAO;
import erep.mde.data.Article;
import erep.mde.data.Payment;

public class ApprovalServlet extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	private static final Logger	logger				= Logger.getLogger(ApprovalServlet.class
															.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		doPost(req, resp);
	}

	/**
	 * approves an article
	 **/
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		ArticlesDAO artDAO = new ArticlesDAO();
		ModerationDAO modDAO = new ModerationDAO();
		PaymentDAO payDao = new PaymentDAO();

		try {
			Long articleId = Long.valueOf(req.getParameter("articleId"));
			String articleValue = req.getParameter("value");

			Article article = artDAO.getArticle(articleId);

			if (article.getIsApproved() != null) {
				writeError(resp, new IllegalArgumentException(
						"Este artigo já foi moderado"));
				return;
			}

			Payment payment = new Payment(articleValue);
			payDao.save(payment);

			article.setPaymentId(payment.getId());
			article.setIsApproved(true);
			article.setLastModified(new Date());

			article.setModeratorId(UserServiceFactory.getUserService()
					.getCurrentUser().getUserId());

			artDAO.save(article);

			new QueueController().fetchArticle(article);

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
			artDAO.close();
			modDAO.close();
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
