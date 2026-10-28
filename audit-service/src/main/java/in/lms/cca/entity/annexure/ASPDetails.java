package in.lms.cca.entity.annexure;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "asp_details", schema="audit_management")
public class ASPDetails extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asp_details_id", length = 11)
    private Long aspDetailsId;
    
    @ManyToOne
    @JoinColumn(name = "annexure_main_id", referencedColumnName = "annexure_main_id")
	private AnnexureMain annexureMainId;
    
    @Column(name = "auditor_verification", length = 200)
    private String auditorVerification;
    
    @Column(name = "asp_count", length = 100)
    private String aspCount;
    
    @Column(name = "asps_audit_overdue_count", length = 100)
    private String aspsAuditOverdueCount;

	public Long getAspDetailsId() {
		return aspDetailsId;
	}

	public void setAspDetailsId(Long aspDetailsId) {
		this.aspDetailsId = aspDetailsId;
	}

	@JsonIgnore
	public AnnexureMain getAnnexureMainId() {
		return annexureMainId;
	}

	public void setAnnexureMainId(AnnexureMain annexureMainId) {
		this.annexureMainId = annexureMainId;
	}

	public String getAuditorVerification() {
		return auditorVerification;
	}

	public void setAuditorVerification(String auditorVerification) {
		this.auditorVerification = auditorVerification;
	}

	public String getAspCount() {
		return aspCount;
	}

	public void setAspCount(String aspCount) {
		this.aspCount = aspCount;
	}

	public String getAspsAuditOverdueCount() {
		return aspsAuditOverdueCount;
	}

	public void setAspsAuditOverdueCount(String aspsAuditOverdueCount) {
		this.aspsAuditOverdueCount = aspsAuditOverdueCount;
	}
    

    
    
}
