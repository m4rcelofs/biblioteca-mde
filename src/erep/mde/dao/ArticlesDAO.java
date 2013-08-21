package erep.mde.dao;

import java.util.Collection;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.Query;

import erep.mde.cache.CacheFacade;
import erep.mde.cache.Cacheable;
import erep.mde.data.Article;

public class ArticlesDAO extends AbstractDAO<Article> {

	@SuppressWarnings("unchecked")
	public Collection<Article> getNewArticles() {
		Query q = pm.newQuery(Article.class);
		q.setFilter("isApproved == null");
		// TODO order by date
		return pm.detachCopyAll((List<Article>) q.execute());
	}

	public Article getArticle(Long id) {
		try {
			return pm.detachCopy(pm.getObjectById(Article.class, id));
		} catch (JDOObjectNotFoundException e) {
			return null;
		}
	}

	public boolean exists(String articleId) {
		Query q = pm.newQuery(Article.class);
		q.setFilter("eRepId == '" + articleId + "'");
		q.setUnique(true);
		return q.execute() != null;
	}

	@SuppressWarnings("unchecked")
	public Collection<Article> getRejectedArticles() {
		Query q = pm.newQuery(Article.class);
		q.setFilter("isApproved == false");
		return pm.detachCopyAll((List<Article>) q.execute());
	}

	@Cacheable
	@SuppressWarnings("unchecked")
	public Collection<Article> getArticlesByCategory(Long categoryId) {
		Query q = pm.newQuery(Article.class);
		q.setFilter("categoryId == " + categoryId + " && isApproved == true");
		return pm.detachCopyAll((List<Article>) q.execute());
	}

	@Cacheable
	@SuppressWarnings("unchecked")
	public Collection<Article> getArticlesByAuthor(Long authorId) {
		Query q = pm.newQuery(Article.class);
		q.setFilter("authorId == " + authorId + " && isApproved == true");
		return pm.detachCopyAll((List<Article>) q.execute());
	}

	public void removeCategoryFromCache(Long id) {
		CacheFacade
				.remove("erep.mde.dao.ArticlesDAO.getArticlesByCategory(Long="
						+ id + ";)");
	}

	public void removeAuthorFromCache(Long author) {
		CacheFacade
				.remove("erep.mde.dao.ArticlesDAO.getArticlesByAuthor(Long ="
						+ author + ";)");
	}
}
