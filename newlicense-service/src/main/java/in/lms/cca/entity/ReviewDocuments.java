package in.lms.cca.entity;

import jakarta.persistence.*;

import in.lms.cca.util.global.AbstractTimestampEntity;

@Entity
@Table(name = "review_documents", schema = "new_license_management")
public class ReviewDocuments extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_doc_id", length = 11)
    private Long reviewDocId;

    @ManyToOne
    @JoinColumn(name = "review_id", referencedColumnName = "review_id")
    private ReviewApplication reviewId;

    @ManyToOne
    @JoinColumn(name = "app_doc_id", referencedColumnName = "app_doc_id")
    private ApplicationDocument applicationDocument;

    @Column(name = "status", length = 8)
    private String status;

    @Column(name = "created_by", length = 74)
    private String createdBy;

    @Column(name = "updated_by", length = 74)
    private String updatedBy;

	public Long getReviewDocId() {
		return reviewDocId;
	}

	public void setReviewDocId(Long reviewDocId) {
		this.reviewDocId = reviewDocId;
	}

	public ReviewApplication getReviewId() {
		return reviewId;
	}

	public void setReviewId(ReviewApplication reviewId) {
		this.reviewId = reviewId;
	}


	public ApplicationDocument getApplicationDocument() {
		return applicationDocument;
	}

	public void setApplicationDocument(ApplicationDocument applicationDocument) {
		this.applicationDocument = applicationDocument;
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

	@Override
	public String toString() {
		return "ReviewDocuments [reviewDocId=" + reviewDocId + ", reviewId=" + reviewId + ", applicationDocument="
				+ applicationDocument + ", status=" + status + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy
				+ "]";
	}

    
    
    
}
