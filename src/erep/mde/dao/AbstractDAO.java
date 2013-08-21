package erep.mde.dao;

import java.util.Collection;

import javax.jdo.PersistenceManager;

import erep.mde.PMF;

public abstract class AbstractDAO<T> {

	protected PersistenceManager	pm	= PMF.getNewPersistenceManager();

	public void close() {
		pm.close();
	}

	public void save(T obj) {
		pm.makePersistent(obj);
	}

	public void save(Collection<T> objs) {
		pm.makePersistentAll(objs);
	}
}
