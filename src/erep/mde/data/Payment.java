package erep.mde.data;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Payment implements Serializable {

	private static final long	serialVersionUID	= 1L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long				id;

	@Persistent
	private Double				value;

	@Persistent
	// Moderator
	private String				moderatorId;

	@Persistent
	// ErepUser
	private Long				userId;

	@Persistent
	private Date				paidOn;

	public Payment(String valueToParse) {
		if (valueToParse == null || valueToParse.trim().isEmpty())
			value = 0d;
		else
			value = ArticleValue.getValue(valueToParse).doubleValue();
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public String getModeratorId() {
		return moderatorId;
	}

	public void setModeratorId(String moderatorId) {
		this.moderatorId = moderatorId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getPaidOn() {
		return paidOn;
	}

	public void setPaidOn(Date paidOn) {
		this.paidOn = paidOn;
	}

	public Long getId() {
		return id;
	}

}
