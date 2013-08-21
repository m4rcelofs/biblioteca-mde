package erep.mde.dao;

import javax.jdo.JDOObjectNotFoundException;

import com.google.appengine.api.users.User;

import erep.mde.cache.CacheFacade;
import erep.mde.cache.Cacheable;
import erep.mde.data.Moderator;

public class ModerationDAO extends AbstractDAO<Moderator> {

	public Moderator getModerator(User gUser) {
		return getModerator(gUser.getUserId());
	}

	@Cacheable
	public Moderator getModerator(String id) {
		return getModeratorUncached(id);
	}

	public Moderator getModeratorUncached(User gUser) {
		return getModeratorUncached(gUser.getUserId());
	}

	public Moderator getModeratorUncached(String id) {
		try {
			return pm.detachCopy(pm.getObjectById(Moderator.class, id));
		} catch (JDOObjectNotFoundException e) {
			return null;
		}
	}

	public void removeFromCache(User gUser) {
		CacheFacade.remove("erep.mde.dao.ModerationDAO.getModerator(User="
				+ gUser.getEmail() + ";)");
	}

	public void removeFromCache(Moderator mod) {
		CacheFacade.remove("erep.mde.dao.ModerationDAO.getModerator(User="
				+ mod.getgEmail() + ";)");
	}
}
