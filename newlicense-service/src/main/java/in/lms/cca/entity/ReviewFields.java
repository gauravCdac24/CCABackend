package in.lms.cca.entity;

import jakarta.persistence.*;

import in.lms.cca.util.global.AbstractTimestampEntity;

@Entity
@Table(name = "review_fields", schema = "new_license_management")
public class ReviewFields extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_field_id", length = 11)
    private Long reviewFieldId;

    @ManyToOne
    @JoinColumn(name = "review_id", referencedColumnName = "review_id")
    private ReviewApplication reviewId;

    @Column(name = "field_name", length = 50)
    private String fieldName;

    @Column(name = "status", length = 15)
    private String status;

    @Column(name = "created_by", length = 74)
    private String createdBy;

    @Column(name = "updated_by", length = 74)
    private String updatedBy;

	public Long getReviewFieldId() {
		return reviewFieldId;
	}

	public void setReviewFieldId(Long reviewFieldId) {
		this.reviewFieldId = reviewFieldId;
	}

	public ReviewApplication getReviewId() {
		return reviewId;
	}

	public void setReviewId(ReviewApplication reviewId) {
		this.reviewId = reviewId;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
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
		return "ReviewFields [reviewFieldId=" + reviewFieldId + ", reviewId=" + reviewId + ", fieldName=" + fieldName
				+ ", status=" + status + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + "]";
	}
    
    

}
