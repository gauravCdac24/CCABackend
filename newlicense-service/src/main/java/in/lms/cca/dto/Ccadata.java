package in.lms.cca.dto;

public class Ccadata {
	
	private String userName;
	private String reviewerUserName;
	private String remarks;
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
	@Override
	public String toString() {
		return "Ccadata [userName=" + userName + ", reviewerUserName=" + reviewerUserName + ", remarks=" + remarks
				+ "]";
	}
	
	

}
