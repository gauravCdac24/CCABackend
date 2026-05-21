package in.lms.cca.dto;

public class ReviewDocumentDTO {
	
	private Long reviewDocId;
    private Long reviewId;
    private String status;
    private String created;
    private String updated;
    private String createdBy;
    private String updatedBy;
	public Long getReviewDocId() {
		return reviewDocId;
	}
	public void setReviewDocId(Long reviewDocId) {
		this.reviewDocId = reviewDocId;
	}
	public Long getReviewId() {
		return reviewId;
	}
	public void setReviewId(Long reviewId) {
		this.reviewId = reviewId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getUpdated() {
		return updated;
	}
	public void setUpdated(String updated) {
		this.updated = updated;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	@Override
	public String toString() {
		return "ReviewDocumentDTO [reviewDocId=" + reviewDocId + ", reviewId=" + reviewId + ", status=" + status
				+ ", created=" + created + ", updated=" + updated + ", createdBy=" + createdBy + ", updatedBy="
				+ updatedBy + "]";
	}
    
    

}
