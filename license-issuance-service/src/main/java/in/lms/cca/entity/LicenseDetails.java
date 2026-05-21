package in.lms.cca.entity;

import java.util.Date;

import in.lms.cca.util.golbal.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "license_details", schema="license_issuance_management") 
public class LicenseDetails extends AbstractTimestampEntity{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "license_id", length = 11)
    private Long licenseId;

	@Column(name = "intent_app_id", length = 255, nullable = false)
    private String intentAppId;
	
    @Column(name = "licenseCertificate", length = 255, nullable = false)
    private String licenseCertificate;
    
    @Column(name = "username", length = 50, nullable=false)
	private String userName;
    
    @Column(name = "licensename", length = 200, nullable=false)
   	private String licenseName;
    
    @Column(name = "serialNo", length = 25, nullable = false)
    private String serialNo;

    @Column(name = "issue_date", nullable = false)
    private Date issueDate;

    @Column(name = "expiry_date", nullable = false)
    private Date expiryDate;
    
    @Column(name = "status", length = 9, nullable = false)
    private String status;

    @Column(name = "created_by", length = 74, nullable = false)
    private String createdBy;

    @Column(name = "updated_by", length = 74, nullable = false)
    private String updatedBy;
    
    

	public Long getLicenseId() {
		return licenseId;
	}
	
	

	public String getLicenseName() {
		return licenseName;
	}



	public void setLicenseName(String licenseName) {
		this.licenseName = licenseName;
	}



	public void setLicenseId(Long licenseId) {
		this.licenseId = licenseId;
	}

	public String getIntentAppId() {
		return intentAppId;
	}

	public void setIntentAppId(String intentAppId) {
		this.intentAppId = intentAppId;
	}

	public String getLicenseCertificate() {
		return licenseCertificate;
	}

	public void setLicenseCertificate(String licenseCertificate) {
		this.licenseCertificate = licenseCertificate;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
    
    
	
	
}
