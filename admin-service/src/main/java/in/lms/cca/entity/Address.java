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

@Entity
@Table(name = "address", schema="admin_user_management")
public class Address extends AbstractTimestampEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "address_id", length = 11)
	private Long addressId;
	
	@ManyToOne
	@JoinColumn(name = "address_type_id", referencedColumnName = "address_type_id")
	private AddressType addressTypeId;
	
	@Column(name = "blockno", length = 15)
	private String blockNo;
	
	@Column(name = "village", length = 30)
	private String village;
	
	@Column(name = "post_office", length = 30)
	private String postOffice;
	
	@Column(name = "pin_code", length = 6)
	private String pincode;
	
	@Column(name = "sub_division", length = 30)
	private String subDivision;
	
	@ManyToOne
	@JoinColumn(name = "country_id", referencedColumnName = "country_id")
	private Country countryId;
	
	@ManyToOne
	@JoinColumn(name = "state_id", referencedColumnName = "state_id")
	private State stateId;
	
	@ManyToOne
	@JoinColumn(name = "city_id", referencedColumnName = "city_id")
	private City cityId;
	
	@Column(name = "fax", length = 12)
	private String fax;
	@Column(name = "telephone_no", length = 12)
	private String telephoneNo;
	@Column(name = "mobile", length = 10)
	private String mobile;
	@Column(name = "status", length = 8)
	private String status;
	
	@Column(name = "created_by", length = 74)
	private String createdBy;
	
	@Column(name = "updated_by", length = 74)
	private String updatedBy;
	@Column(name = "communication_address", length = 20)
    private String commicationAddress;
	
	public String getCommicationAddress() {
		return commicationAddress;
	}
	public void setCommicationAddress(String commicationAddress) {
		this.commicationAddress = commicationAddress;
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

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public AddressType getAddressTypeId() {
		return addressTypeId;
	}

	public void setAddressTypeId(AddressType addressTypeId) {
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

	public String getSubDivision() {
		return subDivision;
	}

	public void setSubDivision(String subDivision) {
		this.subDivision = subDivision;
	}

	public Country getCountryId() {
		return countryId;
	}

	public void setCountryId(Country countryId) {
		this.countryId = countryId;
	}

	public State getStateId() {
		return stateId;
	}

	public void setStateId(State stateId) {
		this.stateId = stateId;
	}

	public City getCityId() {
		return cityId;
	}

	public void setCityId(City cityId) {
		this.cityId = cityId;
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

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	
	
	
}
