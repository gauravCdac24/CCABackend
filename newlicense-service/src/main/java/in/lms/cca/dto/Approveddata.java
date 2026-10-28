package in.lms.cca.dto;

import org.springframework.web.multipart.MultipartFile;

public class Approveddata {
	
	
	private String userName;
	private String reviewerUserName;
	private String remarks;
	private Boolean isRejected;
	private String reviewDate;
	private MultipartFile file1;
	
	
	
	public String getReviewDate() {
		return reviewDate;
	}
	public void setReviewDate(String reviewDate) {
		this.reviewDate = reviewDate;
	}
	public MultipartFile getFile1() {
		return file1;
	}
	public void setFile1(MultipartFile file1) {
		this.file1 = file1;
	}
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
	@Override
	public String toString() {
		return "Approveddata [userName=" + userName + ", reviewerUserName=" + reviewerUserName + ", remarks=" + remarks
				+ ", isRejected=" + isRejected + ", reviewDate=" + reviewDate + ", file1=" + file1 + "]";
	}
	
	

}
