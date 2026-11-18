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
@Table(name = "self_assessment_main", schema="audit_management")
public class SelfAssessmentMain extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "self_ass_main_id", length = 11)
    private Long selfAssMainId;
    
    @ManyToOne
    @JoinColumn(name = "annexure_main_id", referencedColumnName = "annexure_main_id")
	private AnnexureMain annexureMainId;
    
    @Column(name = "auditor_verification", length = 200)
    private String auditorVerification;
    
    @Column(name = "dsc_issued_count_w_app_form", length = 9)
    private Long dscIssuedCountWAppForm;
    
    @Column(name = "nc_reason", length = 200)
    private String ncReason;
    
    @Column(name = "dsc_issued_w_fee", length = 9)
    private Long dscIssuedWFee;
    
    @Column(name = "details_dsc_issued_w_fee", length = 200)
    private String detailsDSCIssuedWFee;
    
    @Column(name = "dsc_issued_w_physical_ver", length = 9)
    private Long dscIssuedWPhysicalVer;
    
    @Column(name = "dsc_issued_w_match", length = 9)
    private Long dscIssuedWMatch;
    
    @Column(name = "nc_reason_with_match", length = 200)
    private String ncReasonWithMatch;
    
    @Column(name = "is_otp_sent", length = 3)
    private String isOTPSent;
    
    @Column(name = "otp_reason_nc", length = 200)
    private String otpReasonNC;
    
    @Column(name = "ca_system_access_details", length = 250)
    private String caSystemAccessDetails;
    
    @Column(name = "own_nc_details", length = 200)
    private String ownNCDetails;
    
	public Long getSelfAssMainId() {
		return selfAssMainId;
	}

	public void setSelfAssMainId(Long selfAssMainId) {
		this.selfAssMainId = selfAssMainId;
	}

	public String getAuditorVerification() {
		return auditorVerification;
	}

	public void setAuditorVerification(String auditorVerification) {
		this.auditorVerification = auditorVerification;
	}

	public Long getDscIssuedCountWAppForm() {
		return dscIssuedCountWAppForm;
	}

	public void setDscIssuedCountWAppForm(Long dscIssuedCountWAppForm) {
		this.dscIssuedCountWAppForm = dscIssuedCountWAppForm;
	}

	public String getNcReason() {
		return ncReason;
	}

	public void setNcReason(String ncReason) {
		this.ncReason = ncReason;
	}

	public Long getDscIssuedWFee() {
		return dscIssuedWFee;
	}

	public void setDscIssuedWFee(Long dscIssuedWFee) {
		this.dscIssuedWFee = dscIssuedWFee;
	}

	public String getDetailsDSCIssuedWFee() {
		return detailsDSCIssuedWFee;
	}

	public void setDetailsDSCIssuedWFee(String detailsDSCIssuedWFee) {
		this.detailsDSCIssuedWFee = detailsDSCIssuedWFee;
	}

	public Long getDscIssuedWPhysicalVer() {
		return dscIssuedWPhysicalVer;
	}

	public void setDscIssuedWPhysicalVer(Long dscIssuedWPhysicalVer) {
		this.dscIssuedWPhysicalVer = dscIssuedWPhysicalVer;
	}

	public Long getDscIssuedWMatch() {
		return dscIssuedWMatch;
	}

	public void setDscIssuedWMatch(Long dscIssuedWMatch) {
		this.dscIssuedWMatch = dscIssuedWMatch;
	}

	public String getNcReasonWithMatch() {
		return ncReasonWithMatch;
	}

	public void setNcReasonWithMatch(String ncReasonWithMatch) {
		this.ncReasonWithMatch = ncReasonWithMatch;
	}

	public String getIsOTPSent() {
		return isOTPSent;
	}

	public void setIsOTPSent(String isOTPSent) {
		this.isOTPSent = isOTPSent;
	}

	public String getOtpReasonNC() {
		return otpReasonNC;
	}

	public void setOtpReasonNC(String otpReasonNC) {
		this.otpReasonNC = otpReasonNC;
	}

	public String getCaSystemAccessDetails() {
		return caSystemAccessDetails;
	}

	public void setCaSystemAccessDetails(String caSystemAccessDetails) {
		this.caSystemAccessDetails = caSystemAccessDetails;
	}

	public String getOwnNCDetails() {
		return ownNCDetails;
	}

	public void setOwnNCDetails(String ownNCDetails) {
		this.ownNCDetails = ownNCDetails;
	}

	public AnnexureMain getAnnexureMainId() {
		return annexureMainId;
	}

	public void setAnnexureMainId(AnnexureMain annexureMainId) {
		this.annexureMainId = annexureMainId;
	}


	
    
    
    
}
