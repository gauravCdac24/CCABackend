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
@Table(name = "indiv_address", schema = "new_license_management")
public class IndivAddress extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "indiv_address_id", length = 11)
    private Long indivAddressId;

    @Column(name = "address_id", length = 74)
    private String addressId;

    @Column(name = "telephone_no", length = 12)
    private String telephoneNo;

    @Column(name = "mobile_no", length = 10)
    private String mobileNo;

    @Column(name = "fax", length = 12)
    private String fax;

    @ManyToOne
	@JoinColumn(name = "indiv_application_id", referencedColumnName = "indiv_application_id")
    private IndivApplication indivApplicationId;

    @Column(name = "status", length = 8)
    private String status;

    @Column(name = "created_by", length = 74)
    private String createdBy;

    @Column(name = "updated_by", length = 74)
    private String updatedBy;

	public Long getIndivAddressId() {
		return indivAddressId;
	}

	public void setIndivAddressId(Long indivAddressId) {
		this.indivAddressId = indivAddressId;
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

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public IndivApplication getIndivApplicationId() {
		return indivApplicationId;
	}

	public void setIndivApplicationId(IndivApplication indivApplicationId) {
		this.indivApplicationId = indivApplicationId;
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

