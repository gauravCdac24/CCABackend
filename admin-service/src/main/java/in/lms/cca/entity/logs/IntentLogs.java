package in.lms.cca.entity.logs;

import in.lms.cca.entity.Address;
import in.lms.cca.entity.Intent;
import in.lms.cca.util.global.LogAbstractTimestampOperationEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "intent_logs", schema = "cca_logs_management")
public class IntentLogs extends LogAbstractTimestampOperationEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "log_intent_id", length = 11)
	private Long logIntentId;
	
	@Column(name = "intent_id", length = 11)
	private Long intentId;
	
	@OneToOne
	@JoinColumn(name = "address_id", referencedColumnName = "address_id")
	private Address addressId;
	
	@Column(name = "salutation", length = 6)
	private String salutation;
	
	@Column(name = "first_name", length = 30)
	private String firstName;
	
	@Column(name = "middle_name", length = 30)
	private String middleName;
	
	@Column(name = "last_name", length = 45)
	private String lastName;
	
	@Column(name = "status", length = 11)
	private String status;

	public Long getLogIntentId() {
		return logIntentId;
	}

	public void setLogIntentId(Long logIntentId) {
		this.logIntentId = logIntentId;
	}

	public Long getIntentId() {
		return intentId;
	}

	public void setIntentId(Long intentId) {
		this.intentId = intentId;
	}

	public Address getAddressId() {
		return addressId;
	}

	public void setAddressId(Address addressId) {
		this.addressId = addressId;
	}

	public String getSalutation() {
		return salutation;
	}

	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public IntentLogs(Long logIntentId, Long intentId, Address addressId, String salutation, String firstName,
			String middleName, String lastName, String emailId, String mobileNo, String status) {
		super();
		this.logIntentId = logIntentId;
		this.intentId = intentId;
		this.addressId = addressId;
		this.salutation = salutation;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.status = status;
	}
	
	public IntentLogs(Intent e,String IPAddress,String operation,String userName) {
		
		this.intentId = e.getIntentId();
		this.addressId = e.getAddressId();
		this.salutation = e.getSalutation();
		this.firstName = e.getFirstName();
		this.middleName = e.getMiddleName();
		this.lastName = e.getLastName();
		this.status = e.getStatus();
		this.setIPAddress(IPAddress);
		this.setOperation(operation);
		this.setUserName(userName);
	}

}
