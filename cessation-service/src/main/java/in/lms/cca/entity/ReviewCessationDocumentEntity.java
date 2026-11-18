package in.lms.cca.entity;

import in.lms.cca.util.golbal.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "review_cessation_document", schema="cessation_management")
public class ReviewCessationDocumentEntity extends AbstractTimestampEntity{
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	  @Column(name="review_doc_id", length = 11)
    private Long reviewDocId;

	 @ManyToOne
	    @JoinColumn(name = "review_id", referencedColumnName = "review_id")
    private ReviewDocumentEntity reviewId;
	 
	 @Column(name="undertaking_id",length = 11)
	 private Long undertakingId;

    @Column(name="status", length = 15)
    private String status; 
    
    @Column(name="created_by", length = 74)
    private String createdBy; 
    
    @Column(name="updated_by",length = 74)
    private String updatedBy;

	public Long getReviewDocId() {
		return reviewDocId;
	}

	public void setReviewDocId(Long reviewDocId) {
		this.reviewDocId = reviewDocId;
	}

	public ReviewDocumentEntity getReviewId() {
		return reviewId;
	}

	public void setReviewId(ReviewDocumentEntity reviewId) {
		this.reviewId = reviewId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
	

	public Long getUndertakingId() {
		return undertakingId;
	}

	public void setUndertakingId(Long undertakingId) {
		this.undertakingId = undertakingId;
	}

	@Override
	public String toString() {
		return "ReviewCessationDocumentEntity [reviewDocId=" + reviewDocId + ", reviewId=" + reviewId
				+ ", undertakingId=" + undertakingId + ", status=" + status + ", createdBy=" + createdBy
				+ ", updatedBy=" + updatedBy + "]";
	}

	
    

}
