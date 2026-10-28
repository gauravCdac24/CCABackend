package in.lms.cca.dto.annexure;

import java.util.Date;

public class EKYCAcMonthDetailsDTO {

	private String eKYCAcMonthId;
	private String month;
	private Date fromDate;
	private Date toDate;
	private String observations;
	private String auditorDetails;
	 
	public String geteKYCAcMonthId() {
		return eKYCAcMonthId;
	}
	public void seteKYCAcMonthId(String eKYCAcMonthId) {
		this.eKYCAcMonthId = eKYCAcMonthId;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
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

