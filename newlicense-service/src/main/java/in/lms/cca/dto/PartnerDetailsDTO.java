package in.lms.cca.dto;

import java.util.HashMap;

public class PartnerDetailsDTO {
    private String partnerDetailId;
    private String blockNo;
    private String village;
    private String postOffice;
    private String subDivision;
    private String country;
    private String city;
    private String state;
    private String pin;
    private String fax;
    private String telephoneNo;
    private String mobile;
    
    // Validate email format
    private String email;
    private String firstName;
    private String lastName;
    private String middleName;
    private String salutation;
    private String passposrtNo;
    private String passportIssuingAthority;
    
    // Consider changing to Date for better handling of date types
    private String passportExpirydate; // Change to Date if preferred
    private String passportDocument;
    private String voterCardNo;
    private String voterCardDocument;
    private String pancardNo;
    private String pancardDocument;
    private String webURL;
    private String nationality;
    private String userName;
    private String addressId;
    private HashMap<String, FileDTO> files;

  
	@Override
	public String toString() {
		return "PartnerDetailsDTO [partnerDetailId=" + partnerDetailId + ", blockNo=" + blockNo + ", village=" + village
				+ ", postOffice=" + postOffice + ", subDivision=" + subDivision + ", country=" + country + ", city="
				+ city + ", state=" + state + ", pin=" + pin + ", fax=" + fax + ", telephoneNo=" + telephoneNo
				+ ", mobile=" + mobile + ", email=" + email + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", middleName=" + middleName + ", salutation=" + salutation + ", passposrtNo=" + passposrtNo
				+ ", passportIssuingAthority=" + passportIssuingAthority + ", passportExpirydate=" + passportExpirydate
				+ ", passportDocument=" + passportDocument + ", voterCardNo=" + voterCardNo + ", voterCardDocument="
				+ voterCardDocument + ", pancardNo=" + pancardNo + ", pancardDocument=" + pancardDocument + ", webURL="
				+ webURL + ", nationality=" + nationality + ", userName=" + userName + ", addressId=" + addressId + "]";
	}

	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	public String getPartnerDetailId() {
		return partnerDetailId;
	}

	public void setPartnerDetailId(String partnerDetailId) {
		this.partnerDetailId = partnerDetailId;
	}

	public String getBlockNo() {
		return blockNo;
	}

	public void setBlockNo(String blockNo) {
		this.blockNo = blockNo;
	}

	public String getVillage() {
		return village;
	}

	public void setVillage(String village) {
		this.village = village;
	}

	public String getPostOffice() {
		return postOffice;
	}

	public void setPostOffice(String postOffice) {
		this.postOffice = postOffice;
	}

	public String getSubDivision() {
		return subDivision;
	}

	public void setSubDivision(String subDivision) {
		this.subDivision = subDivision;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getTelephoneNo() {
		return telephoneNo;
	}

	public void setTelephoneNo(String telephoneNo) {
		this.telephoneNo = telephoneNo;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getSalutation() {
		return salutation;
	}

	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}


	public String getPassposrtNo() {
		return passposrtNo;
	}

	public void setPassposrtNo(String passposrtNo) {
		this.passposrtNo = passposrtNo;
	}

	public String getPassportIssuingAthority() {
		return passportIssuingAthority;
	}

	public void setPassportIssuingAthority(String passportIssuingAthority) {
		this.passportIssuingAthority = passportIssuingAthority;
	}

	public String getPassportExpirydate() {
		return passportExpirydate;
	}

	public void setPassportExpirydate(String passportExpirydate) {
		this.passportExpirydate = passportExpirydate;
	}

	public String getPassportDocument() {
		return passportDocument;
	}

	public void setPassportDocument(String passportDocument) {
		this.passportDocument = passportDocument;
	}

	public String getVoterCardNo() {
		return voterCardNo;
	}

	public void setVoterCardNo(String voterCardNo) {
		this.voterCardNo = voterCardNo;
	}

	public String getVoterCardDocument() {
		return voterCardDocument;
	}

	public void setVoterCardDocument(String voterCardDocument) {
		this.voterCardDocument = voterCardDocument;
	}

	public String getPancardNo() {
		return pancardNo;
	}

	public void setPancardNo(String pancardNo) {
		this.pancardNo = pancardNo;
	}

	public String getPancardDocument() {
		return pancardDocument;
	}

	public void setPancardDocument(String pancardDocument) {
		this.pancardDocument = pancardDocument;
	}

	public String getWebURL() {
		return webURL;
	}

	public void setWebURL(String webURL) {
		this.webURL = webURL;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public HashMap<String, FileDTO> getFiles() {
		return files;
	}

	public void setFiles(HashMap<String, FileDTO> files) {
		this.files = files;
	}
	
	
}
