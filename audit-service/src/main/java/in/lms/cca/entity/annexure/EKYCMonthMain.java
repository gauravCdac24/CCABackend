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
@Table(name = "ekyc_month_main", schema = "audit_management")
public class EKYCMonthMain extends AbstractTimestampEntity {

	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 @Column(name = "ekyc_month_main_id", length = 11)
	 private Long eKYCMonthMainId;
	
	 @ManyToOne
	 @JoinColumn(name = "annexure_main_id", referencedColumnName = "annexure_main_id")
	 private AnnexureMain annexureMainId;
	
	 @Column(name = "auditor_verification", length = 200)
	 private String auditorVerification;

	public Long geteKYCMonthMainId() {
		return eKYCMonthMainId;
	}

	public void seteKYCMonthMainId(Long eKYCMonthMainId) {
		this.eKYCMonthMainId = eKYCMonthMainId;
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
	

 
 
 
}

