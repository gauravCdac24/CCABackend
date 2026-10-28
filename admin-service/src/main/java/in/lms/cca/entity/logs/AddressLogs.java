package in.lms.cca.entity.logs;

import in.lms.cca.entity.Address;
import in.lms.cca.entity.AddressType;
import in.lms.cca.entity.City;
import in.lms.cca.entity.Country;
import in.lms.cca.entity.State;
import in.lms.cca.util.global.LogAbstractTimestampOperationEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "address_logs", schema = "cca_logs_management")
public class AddressLogs extends LogAbstractTimestampOperationEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "log_address_id", length = 11)
	private Long logAddressId;
	
	
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
	
	@Column(name = "status", length = 8)
	private String status;

	public Long getLogAddressId() {
		return logAddressId;
	}

	public void setLogAddressId(Long logAddressId) {
		this.logAddressId = logAddressId;
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

	public AddressLogs(Long logAddressId, Long addressId, AddressType addressTypeId, String blockNo, String village,
			String postOffice, String pincode, String subDivision, Country countryId, State stateId, City cityId,
			String status) {
		super();
		this.logAddressId = logAddressId;
		this.addressId = addressId;
		this.addressTypeId = addressTypeId;
		this.blockNo = blockNo;
		this.village = village;
		this.postOffice = postOffice;
		this.pincode = pincode;
		this.subDivision = subDivision;
		this.countryId = countryId;
		this.stateId = stateId;
		this.cityId = cityId;
		this.status = status;
	}
	
	public AddressLogs(Address e,String IPAddress,String operation,String userName) {

		this.addressId = e.getAddressId();
		this.addressTypeId = e.getAddressTypeId();
		this.blockNo = e.getBlockNo();
		this.village = e.getVillage();
		this.postOffice = e.getPostOffice();
		this.pincode = e.getPincode();
		this.subDivision = e.getSubDivision();
		this.countryId = e.getCountryId();
		this.stateId = e.getStateId();
		this.cityId = e.getCityId();
		this.status = e.getStatus();
		this.setIPAddress(IPAddress);
		this.setOperation(operation);
		this.setUserName(userName);
	}

	public AddressLogs() {
		super();
		// TODO Auto-generated constructor stub
	}


	
	

}
