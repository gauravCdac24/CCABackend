package in.lms.cca.dto.annexure;

import java.util.Date;


public class InternalAuditDetailsDTO {

	private String inAuditDetailsId;
    private String description;
    private Date fromDate;
    private Date toDate;
	private String observations;
	private String auditorDetails;
	
	
	public String getInAuditDetailsId() {
		return inAuditDetailsId;
	}
	public void setInAuditDetailsId(String inAuditDetailsId) {
		this.inAuditDetailsId = inAuditDetailsId;
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
