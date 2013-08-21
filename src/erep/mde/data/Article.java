package erep.mde.data;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Article implements Serializable {

	private static final long	serialVersionUID	= 1L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long				id;

	@Persistent
	private String				eRepId;

	@Persistent
	private String				title;

	@Persistent
	// Category
	private Long				categoryId;

	@Persistent
	// ErepUser
	private Long				authorId;

	@Persistent
	// ErepUser
	private Long				senderId;

	@Persistent
	// Moderator
	private String				moderatorId;

	@Persistent
	private Date				sentOn;

	@Persistent
	private Date				lastModified;

	@Persistent
	private Boolean				isApproved;

	@Persistent
	private String				description;

	@Persistent
	private Integer				eRepDay;

	@Persistent
	private Text				content;

	@Persistent
	// Payment
	private Long				paymentId;

	public String geteRepId() {
		return eRepId;
	}

	public void seteRepId(String eRepId) {
		this.eRepId = eRepId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getSentOn() {
		return sentOn;
	}

	public void setSentOn(Date sentOn) {
		this.sentOn = sentOn;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public Boolean getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(Boolean isApproved) {
		this.isApproved = isApproved;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer geteRepDay() {
		return eRepDay;
	}

	public void seteRepDay(Integer eRepDay) {
		this.eRepDay = eRepDay;
	}

	public Long getId() {
		return id;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public Long getSenderId() {
		return senderId;
	}

	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}

	public String getModeratorId() {
		return moderatorId;
	}

	public void setModeratorId(String moderatorId) {
		this.moderatorId = moderatorId;
	}

	public Text getContent() {
		return content;
	}

	public void setContent(Text content) {
		this.content = content;
	}

	public Long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}

}
