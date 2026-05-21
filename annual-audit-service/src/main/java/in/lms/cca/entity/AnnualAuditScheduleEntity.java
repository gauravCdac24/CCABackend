package in.lms.cca.entity;



import java.util.Date;

import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "annual_audit_schedule",schema="annual_audit_management")
public class AnnualAuditScheduleEntity extends AbstractTimestampEntity{
	
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "audit_schedule_id")
	    private Long auditScheduleId;

	    @Column(name = "license_id")
	    private String licenseId;

	    @Column(name="user_name",length = 30)
	    private String userName;
	    
	    @Column(name = "schedule_start_date")
	    private Date scheduleStartDate;
	    
	    @Column(name = "schedule_submission_date")
	    private Date scheduleSubmissionDate;

	    @Column(name = "actual_start_date")
	    private Date actualStartDate;
	    
	    @Column(name = "audit_agency_id",length = 74)
	    private String auditAgencyId;
	    
	    @Column(name = "date_of_submission")
	    private Date dateOfSubmission;
	    
	    @Column(name = "date_of_completion")
	    private Date dateOfCompletion;
	    
	    @Column(name = "status", length =20)
	    private String status;

	    @Column(name = "created_by",length = 74)
	    private String createdBy;

	    @Column(name = "updated_by",length = 74)
	    private String updatedBy;
	    
	    
	    

		public Long getAuditScheduleId() {
			return auditScheduleId;
		}

		public void setAuditScheduleId(Long auditScheduleId) {
			this.auditScheduleId = auditScheduleId;
		}

		public String getLicenseId() {
			return licenseId;
		}

		public void setLicenseId(String licenseId) {
			this.licenseId = licenseId;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public Date getScheduleStartDate() {
			return scheduleStartDate;
		}

		public void setScheduleStartDate(Date scheduleStartDate) {
			this.scheduleStartDate = scheduleStartDate;
		}

		public Date getScheduleSubmissionDate() {
			return scheduleSubmissionDate;
		}

		public void setScheduleSubmissionDate(Date scheduleSubmissionDate) {
			this.scheduleSubmissionDate = scheduleSubmissionDate;
		}

		public Date getActualStartDate() {
			return actualStartDate;
		}

		public void setActualStartDate(Date actualStartDate) {
			this.actualStartDate = actualStartDate;
		}

		public String getAuditAgencyId() {
			return auditAgencyId;
		}

		public void setAuditAgencyId(String auditAgencyId) {
			this.auditAgencyId = auditAgencyId;
		}

		public Date getDateOfSubmission() {
			return dateOfSubmission;
		}

		public void setDateOfSubmission(Date dateOfSubmission) {
			this.dateOfSubmission = dateOfSubmission;
		}

		public Date getDateOfCompletion() {
			return dateOfCompletion;
		}

		public void setDateOfCompletion(Date dateOfCompletion) {
			this.dateOfCompletion = dateOfCompletion;
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
	    
	    

		
}
