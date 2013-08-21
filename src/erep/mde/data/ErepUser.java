package erep.mde.data;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.json.simple.JSONObject;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ErepUser implements Serializable {

	private static final long	serialVersionUID	= 1L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long				id;

	@Persistent
	private String				eRepId;

	@Persistent
	private String				eRepName;

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

	public Long getId() {
		return id;
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		obj.put("eRepId", eRepId);
		obj.put("name", eRepName);
		return obj;
	}

}
