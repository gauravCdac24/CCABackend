package in.lms.cca.entity.annexure;

import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "revocation_main", schema = "audit_management")
public class RevocationMain extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "revocation_main_id", length = 11)
    private Long revocationMainId;

    @ManyToOne
    @JoinColumn(name = "annexure_main_id", referencedColumnName = "annexure_main_id")
	private AnnexureMain annexureMainId;

    @Column(name = "auditor_verification", length = 200)
    private String auditorVerification;

    @Column(name = "dsc_revoked_count", length = 9)
    private Long dscRevokedCount;

    @Column(name = "revoc_req_reason", length = 200)
    private String revocReqReason;

    @Column(name = "dsc_revoked_reason", length = 200)
    private String dscRevokedReason;


	public Long getRevocationMainId() {
		return revocationMainId;
	}

	public void setRevocationMainId(Long revocationMainId) {
		this.revocationMainId = revocationMainId;
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

	public Long getDscRevokedCount() {
		return dscRevokedCount;
	}

	public void setDscRevokedCount(Long dscRevokedCount) {
		this.dscRevokedCount = dscRevokedCount;
	}

	public String getRevocReqReason() {
		return revocReqReason;
	}

	public void setRevocReqReason(String revocReqReason) {
		this.revocReqReason = revocReqReason;
	}

	public String getDscRevokedReason() {
		return dscRevokedReason;
	}

	public void setDscRevokedReason(String dscRevokedReason) {
		this.dscRevokedReason = dscRevokedReason;
	}

}
