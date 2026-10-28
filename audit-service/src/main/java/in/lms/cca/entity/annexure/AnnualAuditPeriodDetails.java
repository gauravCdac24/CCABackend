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
@Table(name = "annual_audit_period_details", schema="audit_management")
public class AnnualAuditPeriodDetails extends AbstractTimestampEntity{


	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "period_details_id",length = 11)
	private Long periodDetailsId;
	
	@ManyToOne
    @JoinColumn(name = "period_main_id", referencedColumnName = "period_main_id")
	private AnnualAuditPeriodMain periodMainId;
	
	@Column(name = "description",length = 50)
    private String description;
	
	@Column(name = "fromDate")
    private Date fromDate;
	
	@Column(name = "toDate")
    private Date toDate;
	
	@Column(name = "observations",columnDefinition="TEXT")
    private String observations;

	public Long getPeriodDetailsId() {
		return periodDetailsId;
	}

	public void setPeriodDetailsId(Long periodDetailsId) {
		this.periodDetailsId = periodDetailsId;
	}

	public AnnualAuditPeriodMain getPeriodMainId() {
		return periodMainId;
	}

	public void setPeriodMainId(AnnualAuditPeriodMain periodMainId) {
		this.periodMainId = periodMainId;
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
