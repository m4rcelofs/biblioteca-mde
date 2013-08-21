package erep.mde;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.QueueStatistics;

import erep.mde.data.Article;
import erep.mde.data.ErepUser;

public class QueueController {

	public void fetchUser(ErepUser user) {
		Queue queue = QueueFactory.getQueue("users");
		queue.add(withUrl("/admin/fetch/user").param("id",
				user.getId().toString()));
	}

	public void fetchArticle(Article article) {
		Queue queue = QueueFactory.getQueue("articles");
		queue.add(withUrl("/admin/fetch/article").param("id",
				article.getId().toString()));
	}

	public QueueStatistics getArticlesQueueStatistics() {
		Queue queue = QueueFactory.getQueue("articles");
		return queue.fetchStatistics();
	}
}
