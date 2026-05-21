package in.lms.cca.dto.annexure;

import java.util.Date;


public class AnnualAuditPeriodDetailsDTO {

	private String periodDetailsId;
    private String description;
    private Date fromDate;
    private Date toDate;
	private String observations;
	
	public String getPeriodDetailsId() {
		return periodDetailsId;
	}
	public void setPeriodDetailsId(String periodDetailsId) {
		this.periodDetailsId = periodDetailsId;
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
	
	
	
	
}
