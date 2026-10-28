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
@Table(name = "ekyc_ac_month_details", schema = "audit_management")
public class EKYCAcMonthDetails extends AbstractTimestampEntity {

	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 @Column(name = "ekyc_ac_month_id", length = 11)
	 private Long eKYCAcMonthId;
	
	 @ManyToOne
	 @JoinColumn(name = "ekyc_month_main_id", referencedColumnName = "ekyc_month_main_id")
	 private EKYCMonthMain eKYCMonthMainId;
	
	 @Column(name = "month", length = 9)
	 private String month;
	
	 @Column(name = "from_date")
	 private Date fromDate;
	
	 @Column(name = "to_date")
	 private Date toDate;
	
	 @Column(name = "observations", length = 200)
	 private String observations;
	
	 @Column(name = "auditor_details", length = 64)
	 private String auditorDetails;
	
	 
	public Long geteKYCAcMonthId() {
		return eKYCAcMonthId;
	}

	public void seteKYCAcMonthId(Long eKYCAcMonthId) {
		this.eKYCAcMonthId = eKYCAcMonthId;
	}

	public EKYCMonthMain geteKYCMonthMainId() {
		return eKYCMonthMainId;
	}

	public void seteKYCMonthMainId(EKYCMonthMain eKYCMonthMainId) {
		this.eKYCMonthMainId = eKYCMonthMainId;
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

