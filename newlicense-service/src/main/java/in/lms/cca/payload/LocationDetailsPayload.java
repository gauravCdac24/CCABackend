package in.lms.cca.payload;

import org.springframework.web.multipart.MultipartFile;

public class LocationDetailsPayload {

    private String addressId;
    private String blockNo;
    private String city;
    private String country;
    private String locationType;
    private String pin;
    private String postOffice;
    private String state;
    private String subDivision;
    private String village;
    private String status;
    private String locationId;
    private String intentAppId;
    private String created;
    private String updated;
    private String createdBy;
    private String updatedBy;
    private String userName;
    private MultipartFile certificationDocument;
    private String certificationLevel;
    private String companyName;
    private String certificateName;

    // Default constructor
    public LocationDetailsPayload() {}

    // Getter and setter methods
    
    
    public String getAddressId() {
        return addressId;
    }
    public String getCertificateName() {
		return certificateName;
	}

	public void setCertificateName(String certificateName) {
		this.certificateName = certificateName;
	}

	public void setAddressId(String addressId) {
        this.addressId = addressId;
    }
    public String getBlockNo() {
        return blockNo;
    }
    public void setBlockNo(String blockNo) {
        this.blockNo = blockNo;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getLocationType() {
        return locationType;
    }
    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }
    public String getPin() {
        return pin;
    }
    public void setPin(String pin) {
        this.pin = pin;
    }
    public String getPostOffice() {
        return postOffice;
    }
    public void setPostOffice(String postOffice) {
        this.postOffice = postOffice;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getSubDivision() {
        return subDivision;
    }
    public void setSubDivision(String subDivision) {
        this.subDivision = subDivision;
    }
    public String getVillage() {
        return village;
    }
    public void setVillage(String village) {
        this.village = village;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getLocationId() {
        return locationId;
    }
    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }
    public String getIntentAppId() {
        return intentAppId;
    }
    public void setIntentAppId(String intentAppId) {
        this.intentAppId = intentAppId;
    }
    public String getCreated() {
        return created;
    }
    public void setCreated(String created) {
        this.created = created;
    }
    public String getUpdated() {
        return updated;
    }
    public void setUpdated(String updated) {
        this.updated = updated;
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
    public MultipartFile getCertificationDocument() {
        return certificationDocument;
    }
    public void setCertificationDocument(MultipartFile certificationDocument) {
        this.certificationDocument = certificationDocument;
    }
    public String getCertificationLevel() {
        return certificationLevel;
    }
    public void setCertificationLevel(String certificationLevel) {
        this.certificationLevel = certificationLevel;
    }
    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    // Additional constructor if needed for easier creation
    public LocationDetailsPayload(String addressId, String blockNo, String city, String country, 
                                  String locationType, String pin, String postOffice, String state, 
                                  String subDivision, String village, String status, String locationId, 
                                  String intentAppId, String created, String updated, String createdBy, 
                                  String updatedBy, String userName, MultipartFile certificationDocument, 
                                  String certificationLevel, String companyName) {
        this.addressId = addressId;
        this.blockNo = blockNo;
        this.city = city;
        this.country = country;
        this.locationType = locationType;
        this.pin = pin;
        this.postOffice = postOffice;
        this.state = state;
        this.subDivision = subDivision;
        this.village = village;
        this.status = status;
        this.locationId = locationId;
        this.intentAppId = intentAppId;
        this.created = created;
        this.updated = updated;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.userName = userName;
        this.certificationDocument = certificationDocument;
        this.certificationLevel = certificationLevel;
        this.companyName = companyName;
    }

	@Override
	public String toString() {
		return "LocationDetailsPayload [addressId=" + addressId + ", blockNo=" + blockNo + ", city=" + city
				+ ", country=" + country + ", locationType=" + locationType + ", pin=" + pin + ", postOffice="
				+ postOffice + ", state=" + state + ", subDivision=" + subDivision + ", village=" + village
				+ ", status=" + status + ", locationId=" + locationId + ", intentAppId=" + intentAppId + ", created="
				+ created + ", updated=" + updated + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy
				+ ", userName=" + userName + ", certificationDocument=" + certificationDocument
				+ ", certificationLevel=" + certificationLevel + ", companyName=" + companyName + "]";
	}
    
}
