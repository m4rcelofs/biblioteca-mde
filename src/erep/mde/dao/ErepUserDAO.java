package erep.mde.dao;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.Query;

import erep.mde.QueueController;
import erep.mde.cache.CacheFacade;
import erep.mde.cache.Cacheable;
import erep.mde.data.ErepUser;

public class ErepUserDAO extends AbstractDAO<ErepUser> {

	@Cacheable
	public ErepUser getUser(Long id) {
		return getUserUncached(id);
	}

	public ErepUser getUserUncached(Long id) {
		try {
			return pm.detachCopy(pm.getObjectById(ErepUser.class, id));
		} catch (JDOObjectNotFoundException e) {
			return null;
		}
	}

	@Cacheable
	public ErepUser findUser(String eRepId) {
		Query q = pm.newQuery(ErepUser.class);
		q.setFilter("eRepId == '" + eRepId + "'");
		q.setUnique(true);
		ErepUser user = (ErepUser) q.execute();
		if (user == null) {
			user = new ErepUser();
			user.seteRepId(eRepId);
			pm.makePersistent(user);
			populateUser(user);
		}
		return pm.detachCopy(user);
	}

	@Cacheable
	public ErepUser findUserByName(String eRepName) {
		Query q = pm.newQuery(ErepUser.class);
		q.setFilter("eRepName == '" + eRepName + "'");
		q.setUnique(true);
		ErepUser user = (ErepUser) q.execute();
		if (user == null) {
			user = new ErepUser();
			user.seteRepName(eRepName);
			pm.makePersistent(user);
			populateUser(user);
		}
		return pm.detachCopy(user);
	}

	private void populateUser(ErepUser user) {
		//FIXME this might run only after article has been indexed
		new QueueController().fetchUser(user);
	}

	public void removeFromCache(ErepUser user) {
		CacheFacade.remove("erep.mde.dao.ErepUserDAO.getUser(Long="
				+ user.getId() + ";)");
		CacheFacade.remove("erep.mde.dao.ErepUserDAO.findUser(String="
				+ user.geteRepId() + ";)");
		CacheFacade.remove("erep.mde.dao.ErepUserDAO.findUserByName(String="
				+ user.geteRepName() + ";)");
	}

}
