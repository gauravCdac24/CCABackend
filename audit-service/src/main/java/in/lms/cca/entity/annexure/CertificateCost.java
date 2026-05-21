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
@Table(name = "certificate_cost", schema="audit_management")
public class CertificateCost extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cert_cost_id", length = 11)
    private Long certCostId;
    
    @ManyToOne
    @JoinColumn(name = "annexure_main_id", referencedColumnName = "annexure_main_id")
	private AnnexureMain annexureMainId;
    
    @Column(name = "auditor_verification", length = 200)
    private String auditorVerification;
    
    @Column(name = "avg_dsc_issu_maintenance_cost", length = 5)
    private Integer avgDscIssuMaintenanceCost;
    
    @Column(name = "avg_fee_charged_by_ca", length = 5)
    private Integer avgFeeChargedByCA;
    
    @Column(name = "explanation_for_cost_mismatch", length = 250)
    private String explanationForCostMismatch;

	public Long getCertCostId() {
		return certCostId;
	}

	public void setCertCostId(Long certCostId) {
		this.certCostId = certCostId;
	}

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

	public Integer getAvgDscIssuMaintenanceCost() {
		return avgDscIssuMaintenanceCost;
	}

	public void setAvgDscIssuMaintenanceCost(Integer avgDscIssuMaintenanceCost) {
		this.avgDscIssuMaintenanceCost = avgDscIssuMaintenanceCost;
	}

	public Integer getAvgFeeChargedByCA() {
		return avgFeeChargedByCA;
	}

	public void setAvgFeeChargedByCA(Integer avgFeeChargedByCA) {
		this.avgFeeChargedByCA = avgFeeChargedByCA;
	}

	public String getExplanationForCostMismatch() {
		return explanationForCostMismatch;
	}

	public void setExplanationForCostMismatch(String explanationForCostMismatch) {
		this.explanationForCostMismatch = explanationForCostMismatch;
	}
    

    
    
    
    
}
