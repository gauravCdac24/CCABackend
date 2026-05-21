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
@Table(name = "esign_licensee_application", schema="esign_application_management")
public class EsignLicenseeApplication extends AbstractTimestampEntity{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "esign_licensee_appid", length = 11)
    private Long esignLicenseeAppId;

	@Column(name = "licenseid", length = 74, nullable = false)
    private String licenseId;
	
    @Column(name = "applicationStatus", length = 100, nullable = false)
    private String applicationStatus;
    
    @Column(name = "application_number", length = 100, unique=true)
    private String applicationNumber;
    
    @Column(name = "username", nullable = false)
    private String userName;
    
    @Column(name = "created_by", length = 74, nullable = false)
    private String createdBy;

    @Column(name = "updated_by", length = 74, nullable = false)
    private String updatedBy;
    
    @Column(name = "empanelmentDate")
    private Date empanelmentDate;
    

	public Long getEsignLicenseeAppId() {
		return esignLicenseeAppId;
	}

	public void setEsignLicenseeAppId(Long esignLicenseeAppId) {
		this.esignLicenseeAppId = esignLicenseeAppId;
	}

	public String getLicenseId() {
		return licenseId;
	}

	public void setLicenseId(String licenseId) {
		this.licenseId = licenseId;
	}

	public String getApplicationStatus() {
		return applicationStatus;
	}

	public void setApplicationStatus(String applicationStatus) {
		this.applicationStatus = applicationStatus;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getApplicationNumber() {
		return applicationNumber;
	}

	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
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

	public Date getEmpanelmentDate() {
		return empanelmentDate;
	}

	public void setEmpanelmentDate(Date empanelmentDate) {
		this.empanelmentDate = empanelmentDate;
	}
    
    
    

}
