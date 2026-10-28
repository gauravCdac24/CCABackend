package in.lms.cca.entity.annexure;

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
@Table(name = "ra_audit_main", schema = "audit_management")
public class RaAuditMain extends AbstractTimestampEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ra_audit_main_id", length = 11)
    private Long raAuditMainId;

    @ManyToOne
    @JoinColumn(name = "annexure_main_id", referencedColumnName = "annexure_main_id")
	private AnnexureMain annexureMainId;

    @Column(name = "auditor_verification", length = 200)
    private String auditorVerification;

    @Column(name = "total_ra", length = 9)
    private Long totalRA;

    @Column(name = "active_ra", length = 9)
    private Long activeRA;

    @Column(name = "dates_of_ra_audit", length = 200)
    private String datesOfRAAudit;

    @Column(name = "nc_reported_by_ra", length = 200)
    private String ncReportedByRA;

    @Column(name = "ca_action_taken", length = 200)
    private String caActionTaken;

    
	public Long getRaAuditMainId() {
		return raAuditMainId;
	}

	public void setRaAuditMainId(Long raAuditMainId) {
		this.raAuditMainId = raAuditMainId;
	}


	public String getAuditorVerification() {
		return auditorVerification;
	}

	public void setAuditorVerification(String auditorVerification) {
		this.auditorVerification = auditorVerification;
	}

	public Long getTotalRA() {
		return totalRA;
	}

	public void setTotalRA(Long totalRA) {
		this.totalRA = totalRA;
	}

	public Long getActiveRA() {
		return activeRA;
	}

	public void setActiveRA(Long activeRA) {
		this.activeRA = activeRA;
	}

	public String getDatesOfRAAudit() {
		return datesOfRAAudit;
	}

	public void setDatesOfRAAudit(String datesOfRAAudit) {
		this.datesOfRAAudit = datesOfRAAudit;
	}

	public String getNcReportedByRA() {
		return ncReportedByRA;
	}

	public void setNcReportedByRA(String ncReportedByRA) {
		this.ncReportedByRA = ncReportedByRA;
	}

	public String getCaActionTaken() {
		return caActionTaken;
	}

	public void setCaActionTaken(String caActionTaken) {
		this.caActionTaken = caActionTaken;
	}

	public AnnexureMain getAnnexureMainId() {
		return annexureMainId;
	}

	public void setAnnexureMainId(AnnexureMain annexureMainId) {
		this.annexureMainId = annexureMainId;
	}


    
}