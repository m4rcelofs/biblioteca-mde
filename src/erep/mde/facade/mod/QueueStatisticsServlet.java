package erep.mde.facade.mod;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.google.appengine.api.taskqueue.QueueStatistics;

import erep.mde.QueueController;

public class QueueStatisticsServlet extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		JSONObject jsStats = new JSONObject();

		QueueStatistics stats = new QueueController()
				.getArticlesQueueStatistics();

		jsStats.put("queue", stats.getQueueName());
		jsStats.put("waiting", stats.getNumTasks());
		jsStats.put("executedLastMinute", stats.getExecutedLastMinute());

		resp.getWriter().write(jsStats.toString());
	}

}
