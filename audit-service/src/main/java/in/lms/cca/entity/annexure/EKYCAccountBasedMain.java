package in.lms.cca.entity.annexure;

import java.util.Date;

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
@Table(name = "ekyc_account_based_main", schema = "audit_management")
public class EKYCAccountBasedMain extends AbstractTimestampEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ekyc_ac_main_id", length = 11)
    private Long ekycAcMainId;

    @ManyToOne
    @JoinColumn(name = "annexure_main_id", referencedColumnName = "annexure_main_id")
	private AnnexureMain annexureMainId;
 
	
    @Column(name = "auditor_verification", length = 200)
    private String auditorVerification;

    @Column(name = "approval_date_offline_aadhaar")
    private Date approvalDateOfflineAadhaar;

    @Column(name = "approval_date_online_aadhar")
    private Date approvalDateOnlineAadhar;

    @Column(name = "approval_date_pan")
    private Date approvalDatePAN;

    @Column(name = "approval_date_ca")
    private Date approvalDateCA;

    @Column(name = "approval_date_organisational")
    private Date approvalDateOrganisational;

    @Column(name = "approval_date_banking")
    private Date approvalDateBanking;

    @Column(name = "offline_aadhaar_dsc_count", length = 9)
    private Long offlineAadhaarDSCCount;

    @Column(name = "online_aadhar_dsc_count", length = 9)
    private Long onlineAadharDSCCount;

    @Column(name = "pan_dsc_count", length = 9)
    private Long panDSCCount;

    @Column(name = "ca_dsc_count", length = 9)
    private Long caDSCCount;

    @Column(name = "organisational_dsc_count", length = 9)
    private Long organisationalDSCCount;

    @Column(name = "banking_dsc_count", length = 9)
    private Long bankingDSCCount;

    @Column(name = "kua_license_date")
    private Date kuaLicenseDate;

    @Column(name = "pan_serv_details")
    private Date panServDetails;

    @Column(name = "gst_service_details")
    private Date gstServiceDetails;

    @Column(name = "name_of_banks", length = 200)
    private String nameOfBanks;

    @Column(name = "ext_serv_off_aadhar", length = 200)
    private String extServOffAadhar;

    @Column(name = "ext_serv_on_aadhar", length = 200)
    private String extServOnAadhar;

    @Column(name = "ext_serv_pan", length = 200)
    private String extServPAN;

    @Column(name = "ext_serv_ca", length = 200)
    private String extServCA;

    @Column(name = "ext_serv_org", length = 200)
    private String extServOrg;

    @Column(name = "ext_serv_banking", length = 200)
    private String extServBanking;

	public Long getEkycAcMainId() {
		return ekycAcMainId;
	}

	public void setEkycAcMainId(Long ekycAcMainId) {
		this.ekycAcMainId = ekycAcMainId;
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

	public Date getApprovalDateOfflineAadhaar() {
		return approvalDateOfflineAadhaar;
	}

	public void setApprovalDateOfflineAadhaar(Date approvalDateOfflineAadhaar) {
		this.approvalDateOfflineAadhaar = approvalDateOfflineAadhaar;
	}

	public Date getApprovalDateOnlineAadhar() {
		return approvalDateOnlineAadhar;
	}

	public void setApprovalDateOnlineAadhar(Date approvalDateOnlineAadhar) {
		this.approvalDateOnlineAadhar = approvalDateOnlineAadhar;
	}

	public Date getApprovalDatePAN() {
		return approvalDatePAN;
	}

	public void setApprovalDatePAN(Date approvalDatePAN) {
		this.approvalDatePAN = approvalDatePAN;
	}

	public Date getApprovalDateCA() {
		return approvalDateCA;
	}

	public void setApprovalDateCA(Date approvalDateCA) {
		this.approvalDateCA = approvalDateCA;
	}

	public Date getApprovalDateOrganisational() {
		return approvalDateOrganisational;
	}

	public void setApprovalDateOrganisational(Date approvalDateOrganisational) {
		this.approvalDateOrganisational = approvalDateOrganisational;
	}

	public Date getApprovalDateBanking() {
		return approvalDateBanking;
	}

	public void setApprovalDateBanking(Date approvalDateBanking) {
		this.approvalDateBanking = approvalDateBanking;
	}

	public Long getOfflineAadhaarDSCCount() {
		return offlineAadhaarDSCCount;
	}

	public void setOfflineAadhaarDSCCount(Long offlineAadhaarDSCCount) {
		this.offlineAadhaarDSCCount = offlineAadhaarDSCCount;
	}

	public Long getOnlineAadharDSCCount() {
		return onlineAadharDSCCount;
	}

	public void setOnlineAadharDSCCount(Long onlineAadharDSCCount) {
		this.onlineAadharDSCCount = onlineAadharDSCCount;
	}

	public Long getPanDSCCount() {
		return panDSCCount;
	}

	public void setPanDSCCount(Long panDSCCount) {
		this.panDSCCount = panDSCCount;
	}

	public Long getCaDSCCount() {
		return caDSCCount;
	}

	public void setCaDSCCount(Long caDSCCount) {
		this.caDSCCount = caDSCCount;
	}

	public Long getOrganisationalDSCCount() {
		return organisationalDSCCount;
	}

	public void setOrganisationalDSCCount(Long organisationalDSCCount) {
		this.organisationalDSCCount = organisationalDSCCount;
	}

	public Long getBankingDSCCount() {
		return bankingDSCCount;
	}

	public void setBankingDSCCount(Long bankingDSCCount) {
		this.bankingDSCCount = bankingDSCCount;
	}

	public Date getKuaLicenseDate() {
		return kuaLicenseDate;
	}

	public void setKuaLicenseDate(Date kuaLicenseDate) {
		this.kuaLicenseDate = kuaLicenseDate;
	}

	public Date getPanServDetails() {
		return panServDetails;
	}

	public void setPanServDetails(Date panServDetails) {
		this.panServDetails = panServDetails;
	}

	public Date getGstServiceDetails() {
		return gstServiceDetails;
	}

	public void setGstServiceDetails(Date gstServiceDetails) {
		this.gstServiceDetails = gstServiceDetails;
	}

	public String getNameOfBanks() {
		return nameOfBanks;
	}

	public void setNameOfBanks(String nameOfBanks) {
		this.nameOfBanks = nameOfBanks;
	}

	public String getExtServOffAadhar() {
		return extServOffAadhar;
	}

	public void setExtServOffAadhar(String extServOffAadhar) {
		this.extServOffAadhar = extServOffAadhar;
	}

	public String getExtServOnAadhar() {
		return extServOnAadhar;
	}

	public void setExtServOnAadhar(String extServOnAadhar) {
		this.extServOnAadhar = extServOnAadhar;
	}

	public String getExtServPAN() {
		return extServPAN;
	}

	public void setExtServPAN(String extServPAN) {
		this.extServPAN = extServPAN;
	}

	public String getExtServCA() {
		return extServCA;
	}

	public void setExtServCA(String extServCA) {
		this.extServCA = extServCA;
	}

	public String getExtServOrg() {
		return extServOrg;
	}

	public void setExtServOrg(String extServOrg) {
		this.extServOrg = extServOrg;
	}

	public String getExtServBanking() {
		return extServBanking;
	}

	public void setExtServBanking(String extServBanking) {
		this.extServBanking = extServBanking;
	}

    
    
    
    
    
    
}