package in.lms.cca.entity.logs;

import in.lms.cca.entity.AddressType;
import in.lms.cca.util.global.LogAbstractTimestampOperationEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "address_type_logs", schema = "cca_logs_management")
public class AddressTypeLogs extends LogAbstractTimestampOperationEntity{
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "log_address_type_id", length = 2)
	private Integer logAddressTypeId;
	
	@Column(name = "address_type_id", length = 2)
	private Integer addressTypeId;
	
	@Column(name = "addressTypeName", length = 15)
	private String addressTypeName;

	public Integer getLogAddressTypeId() {
		return logAddressTypeId;
	}

	public void setLogAddressTypeId(Integer logAddressTypeId) {
		this.logAddressTypeId = logAddressTypeId;
	}

	public Integer getAddressTypeId() {
		return addressTypeId;
	}

	public void setAddressTypeId(Integer addressTypeId) {
		this.addressTypeId = addressTypeId;
	}

	public String getAddressTypeName() {
		return addressTypeName;
	}

	public void setAddressTypeName(String addressTypeName) {
		this.addressTypeName = addressTypeName;
	}

	public AddressTypeLogs(Integer logAddressTypeId, Integer addressTypeId, String addressTypeName) {
		super();
		this.logAddressTypeId = logAddressTypeId;
		this.addressTypeId = addressTypeId;
		this.addressTypeName = addressTypeName;
	}
	
	public AddressTypeLogs(AddressType e,String IPAddress,String operation,String userName) {

		
		this.addressTypeId = e.getAddressTypeId();
		this.addressTypeName = e.getAddressTypeName();
		this.setIPAddress(IPAddress);
		this.setOperation(operation);
		this.setUserName(userName);
	}

	public AddressTypeLogs() {
		super();
		// TODO Auto-generated constructor stub
	}
	

}
