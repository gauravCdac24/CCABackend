package in.lms.cca.entity;

import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "intent", schema="admin_user_management")
public class Intent extends AbstractTimestampEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "intent_id", length = 11)
	private Long intentId;
	
	@OneToOne
	@JoinColumn(name = "address_id", referencedColumnName = "address_id")
	private Address addressId;
	
	@OneToOne
	@JoinColumn(name = "unique_code_id", referencedColumnName = "unique_code_id")
	private IntentUniqueCode uniqueCodeId;
	
	
	@Column(name = "salutation", length = 6)
	private String salutation;
	
	@Column(name = "first_name", length = 30)
	private String firstName;
	
	@Column(name = "middle_name", length = 30)
	private String middleName;
	
	@Column(name = "last_name", length = 45)
	private String lastName;
	
	
	@Column(name = "status", length = 8)
	private String status;
	
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

	public IntentUniqueCode getUniqueCodeId() {
		return uniqueCodeId;
	}

	public void setUniqueCodeId(IntentUniqueCode uniqueCodeId) {
		this.uniqueCodeId = uniqueCodeId;
	}

	
	
}
