package in.lms.cca.entity;

import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "audit_short_coming",schema="audit_management")

public class AuditShortComingsEntity extends AbstractTimestampEntity{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "short_coming_id",length = 11)
	
	    private Long shortcomingId;
	    
	    
		 @ManyToOne
	    @JoinColumn(name = "app_audit_id", referencedColumnName = "app_audit_id")
	    private ApplicationAuditEntity appAuditId;
	    @Column(name = "short_coming",length = 250)
	    private String shortcomings;
	    @Column(name = "remarks", columnDefinition = "TEXT")
	    private String remarks;
	    @Column(name = "created_by", length = 74)
	    private String createdBy;

	    @Column(name = "updated_by",length = 74)
	    private String updatedBy;



	@Column(name="status",length = 15)
	       private String status;
		public Long getShortcomingId() {
			return shortcomingId;
		}
		public void setShortcomingId(Long shortcomingId) {
			this.shortcomingId = shortcomingId;
		}
		public ApplicationAuditEntity getAppAuditId() {
			return appAuditId;
		}
		public void setAppAuditId(ApplicationAuditEntity appAuditId) {
			this.appAuditId = appAuditId;
		}
		public String getShortcomings() {
			return shortcomings;
		}
		public void setShortcomings(String shortcomings) {
			this.shortcomings = shortcomings;
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
		@Override
		public String toString() {
			return "AuditShortComingsEntity [shortcomingId=" + shortcomingId + ", appAuditId=" + appAuditId
					+ ", shortcomings=" + shortcomings + ", remarks=" + remarks + ", status=" + status + ",  createdBy=" + createdBy + ", updatedBy=" + updatedBy + "]";
		}
	    
	    

}
