package in.lms.cca.dto;

public class CcaApprovalRequest {

	private String userName;
	private String reviewerUserName;
	private String remarks;
	private Boolean isRejected;
	private String reviewDate;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getReviewerUserName() {
		return reviewerUserName;
	}

	public void setReviewerUserName(String reviewerUserName) {
		this.reviewerUserName = reviewerUserName;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Boolean getIsRejected() {
		return isRejected;
	}

	public void setIsRejected(Boolean isRejected) {
		this.isRejected = isRejected;
	}

	public String getReviewDate() {
		return reviewDate;
	}

	public void setReviewDate(String reviewDate) {
		this.reviewDate = reviewDate;
	}
}
