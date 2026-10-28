package in.lms.cca.dto;

import java.util.Date;

public class FirmApplicationDTO {

    private Long firmApplicationId;
    private String registrationNo;
    private Date incorporationDate;
    private String officeName;
    private String addressId;
    private String telephoneNo;
    private String fax;
    private String webPageURL;
    private Integer noOfBranches;
    private String natureOfBusiness;
    private IntentApplicationDTO intentAppId;
    private String status;
    private String createdBy;
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
	public IntentApplicationDTO getIntentAppId() {
		return intentAppId;
	}
	public void setIntentAppId(IntentApplicationDTO intentAppId) {
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
