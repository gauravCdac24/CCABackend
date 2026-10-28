package in.lms.cca.dto;

import java.util.Date;

public class AnnualAuditScheduleDTO {
	
	    private Long auditScheduleId;
	    private String licenseId;
	    private String userName;
	    private Date scheduleStartDate;
	    private Date scheduleSubmissionDate;
	    private Date actualStartDate;
	    private String auditAgencyId;
	    private Date dateOfSubmission;
	    private Date dateOfCompletion;
	    private String status;
	    
	    
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
 
	    
	    
	    
	    
}
