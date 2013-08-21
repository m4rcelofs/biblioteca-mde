package erep.mde.dao;

import java.util.Collection;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.Query;

import erep.mde.cache.Cacheable;
import erep.mde.data.Category;

public class CategoryDAO extends AbstractDAO<Category> {

	@Cacheable
	public Category getCategory(Long id) {
		try {
			return pm.detachCopy(pm.getObjectById(Category.class, id));
		} catch (JDOObjectNotFoundException e) {
			return null;
		}
	}

	@Cacheable
	@SuppressWarnings("unchecked")
	public Collection<Category> getCategories() {
		Query q = pm.newQuery(Category.class);
		return pm.detachCopyAll((List<Category>) q.execute());
	}

}
