package in.lms.cca.dto;


public class AddressDTO {

	private Long addressId;
	private AddressTypeDTO addressTypeId;
	private String blockNo;
	private String village;
	private String postOffice;
	private String pincode;
	private String subDivision;
	private CountryDTO countryId;
	private StateDTO stateId;
	private CityDTO cityId;
	private String fax;
	private String telephoneNo;
	private String mobile;
	private String status;
    private String commicationAddress;
    
    
	public Long getAddressId() {
		return addressId;
	}
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	public AddressTypeDTO getAddressTypeId() {
		return addressTypeId;
	}
	public void setAddressTypeId(AddressTypeDTO addressTypeId) {
		this.addressTypeId = addressTypeId;
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
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getSubDivision() {
		return subDivision;
	}
	public void setSubDivision(String subDivision) {
		this.subDivision = subDivision;
	}
	public CountryDTO getCountryId() {
		return countryId;
	}
	public void setCountryId(CountryDTO countryId) {
		this.countryId = countryId;
	}
	public StateDTO getStateId() {
		return stateId;
	}
	public void setStateId(StateDTO stateId) {
		this.stateId = stateId;
	}
	public CityDTO getCityId() {
		return cityId;
	}
	public void setCityId(CityDTO cityId) {
		this.cityId = cityId;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCommicationAddress() {
		return commicationAddress;
	}
	public void setCommicationAddress(String commicationAddress) {
		this.commicationAddress = commicationAddress;
	}
	

    
    
	
}
