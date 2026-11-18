package in.lms.cca.entity.annexure;

import java.util.Date;

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
@Table(name = "internal_audit_details", schema = "audit_management")
public class InternalAuditDetails extends AbstractTimestampEntity {

	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 @Column(name = "in_audit_details_id", length = 11)
	 private Long inAuditDetailsId;
	
	 @ManyToOne
	 @JoinColumn(name = "in_audit_main_id", referencedColumnName = "in_audit_main_id")
	 private InternalAuditMain inAuditMainId;
	
	 @Column(name = "description", length = 50)
	 private String description;
	
	 @Column(name = "from_date")
	 private Date fromDate;
	
	 @Column(name = "to_date")
	 private Date toDate;
	
	 @Column(name = "observations", length = 50)
	 private String observations;
	
	 @Column(name = "auditor_details", length = 64)
	 private String auditorDetails;
	
	
	public Long getInAuditDetailsId() {
		return inAuditDetailsId;
	}
	
	public void setInAuditDetailsId(Long inAuditDetailsId) {
		this.inAuditDetailsId = inAuditDetailsId;
	}
	
	public InternalAuditMain getInAuditMainId() {
		return inAuditMainId;
	}
	
	public void setInAuditMainId(InternalAuditMain inAuditMainId) {
		this.inAuditMainId = inAuditMainId;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Date getFromDate() {
		return fromDate;
	}
	
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	
	public Date getToDate() {
		return toDate;
	}
	
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	
	public String getObservations() {
		return observations;
	}
	
	public void setObservations(String observations) {
		this.observations = observations;
	}
	
	public String getAuditorDetails() {
		return auditorDetails;
	}
	
	public void setAuditorDetails(String auditorDetails) {
		this.auditorDetails = auditorDetails;
	}
	


}
