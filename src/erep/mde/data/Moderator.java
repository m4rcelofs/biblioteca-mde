package erep.mde.data;

import java.io.Serializable;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.json.simple.JSONObject;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Moderator implements Serializable {

	private static final long	serialVersionUID	= 1L;

	@PrimaryKey
	@Persistent
	private String				gId;

	@Persistent
	private String				gEmail;

	@Persistent
	private String				eRepId;

	@Persistent
	private String				eRepName;

	@Persistent
	private Boolean				isAuthorized;

	public Moderator(String gId) {
		this.gId = gId;
	}

	public String getgEmail() {
		return gEmail;
	}

	public void setgEmail(String gEmail) {
		this.gEmail = gEmail;
	}

	public String geteRepId() {
		return eRepId;
	}

	public void seteRepId(String eRepId) {
		this.eRepId = eRepId;
	}

	public String geteRepName() {
		return eRepName;
	}

	public void seteRepName(String eRepName) {
		this.eRepName = eRepName;
	}

	public Boolean getIsAuthorized() {
		return isAuthorized;
	}

	public void setIsAuthorized(Boolean isAuthorized) {
		this.isAuthorized = isAuthorized;
	}

	public String getgId() {
		return gId;
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		obj.put("eRepId", eRepId);
		obj.put("eRepName", eRepName);
		return obj;
	}

}
