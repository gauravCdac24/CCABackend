package in.lms.cca.entity;

import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "firm_application", schema = "new_license_management")
public class FirmApplication extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "firm_application_id", length = 11)
    private Long firmApplicationId;

    @Column(name = "registration_no", length = 21)
    private String registrationNo;

    @Column(name = "incorporation_date")
    private Date incorporationDate;

    @Column(name = "office_name", length = 75)
    private String officeName;

    @Column(name = "address_id", length = 75)
    private String addressId;

    @Column(name = "telephone_no", length = 12)
    private String telephoneNo;

    @Column(name = "fax", length = 12)
    private String fax;

    @Column(name = "web_page_url", length = 100)
    private String webPageURL;

    @Column(name = "no_of_branches", length = 6)
    private Integer noOfBranches;

    @Column(name = "nature_of_business", length = 35)
    private String natureOfBusiness;

    @ManyToOne
    @JoinColumn(name = "intent_app_id", referencedColumnName = "intent_app_id")
    private IntentApplication intentAppId;
    
    @Column(name = "status", length = 8)
    private String status;

    @Column(name = "created_by", length = 74)
    private String createdBy;

    @Column(name = "updated_by", length = 74)
    private String updatedBy;

	public Long getFirmApplicationId() {
		return firmApplicationId;
	}

	public void setFirmApplicationId(Long firmApplicationId) {
		this.firmApplicationId = firmApplicationId;
	}

	public String getRegistrationNo() {
		return registrationNo;
	}

	public void setRegistrationNo(String registrationNo) {
		this.registrationNo = registrationNo;
	}

	public Date getIncorporationDate() {
		return incorporationDate;
	}

	public void setIncorporationDate(Date incorporationDate) {
		this.incorporationDate = incorporationDate;
	}

	
	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	public String getTelephoneNo() {
		return telephoneNo;
	}

	public void setTelephoneNo(String telephoneNo) {
		this.telephoneNo = telephoneNo;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getWebPageURL() {
		return webPageURL;
	}

	public void setWebPageURL(String webPageURL) {
		this.webPageURL = webPageURL;
	}

	public Integer getNoOfBranches() {
		return noOfBranches;
	}

	public void setNoOfBranches(Integer noOfBranches) {
		this.noOfBranches = noOfBranches;
	}

	public String getNatureOfBusiness() {
		return natureOfBusiness;
	}

	public void setNatureOfBusiness(String natureOfBusiness) {
		this.natureOfBusiness = natureOfBusiness;
	}

	

	public IntentApplication getIntentAppId() {
		return intentAppId;
	}

	public void setIntentAppId(IntentApplication intentAppId) {
		this.intentAppId = intentAppId;
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
    
    
    
    
}
