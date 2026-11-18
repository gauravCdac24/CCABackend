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
@Table(name = "review_document", schema="cessation_management")
public class ReviewDocumentEntity extends AbstractTimestampEntity{
	
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	 @Column(name="review_id", length = 12)
	    private Long reviewId;

	   
	    @Column(name = "cessation_app_id",length = 11)
	    private Long cessationAppId;

	    @Column(name="remarks",length = 500)
	    private String remarks;
	    
	    @Column(name="cca_remarks",length = 500)
	    private String ccaRemarks;

	    @Column(name="status", length = 15)
	    private String status; 
	    
	    @Column(name="created_by", length = 74)
	    private String createdBy; 
	    
	    @Column(name="updated_by",length = 74)
	    private String updatedBy;

		public Long getReviewId() {
			return reviewId;
		}

		public void setReviewId(Long reviewId) {
			this.reviewId = reviewId;
		}

		public Long getCessationAppId() {
			return cessationAppId;
		}

		public void setCessationAppId(Long cessationAppId) {
			this.cessationAppId = cessationAppId;
		}

		public String getRemarks() {
			return remarks;
		}

		public void setRemarks(String remarks) {
			this.remarks = remarks;
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
		
		

		public String getCcaRemarks() {
			return ccaRemarks;
		}

		public void setCcaRemarks(String ccaRemarks) {
			this.ccaRemarks = ccaRemarks;
		}

		@Override
		public String toString() {
			return "ReviewDocumentEntity [reviewId=" + reviewId + ", cessationAppId=" + cessationAppId + ", remarks="
					+ remarks + ", ccaRemarks=" + ccaRemarks + ", status=" + status + ", createdBy=" + createdBy
					+ ", updatedBy=" + updatedBy + "]";
		}

		
	    

}
