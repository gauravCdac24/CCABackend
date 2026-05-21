package in.lms.cca.entity.annexure;

import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "ca_trusted_person", schema="audit_management")
public class CATrustedPerson extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id", length = 11)
    private Long personId;
    
    @ManyToOne
    @JoinColumn(name = "person_main_id", referencedColumnName = "person_main_id")
	private CATrustedPersonMain personMainId;
    
    @Column(name = "full_name", length = 100)
    private String fullName;
    
    @Column(name = "designation", length = 50)
    private String designation;
    
    @Column(name = "location_of_posting", length = 100)
    private String locationOfPosting;
    
    @Column(name = "role", length = 50)
    private String role;
    
    @Column(name = "id_card_no", length = 20)
    private String idCardNo;
    
    @Column(name = "mobile_no", length = 10)
    private String mobileNo;
    
    @Column(name = "identification_details", length = 250)
    private String identificationDetails;
    
    @Column(name = "employed_since")
    private Date employedSince;
    
    @Column(name = "training_details", length = 250)
    private String trainingDetails;
    
    @Column(name = "last_back_ver_date")
    private Date lastBackVerDate;

	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	public CATrustedPersonMain getPersonMainId() {
		return personMainId;
	}

	public void setPersonMainId(CATrustedPersonMain personMainId) {
		this.personMainId = personMainId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getLocationOfPosting() {
		return locationOfPosting;
	}

	public void setLocationOfPosting(String locationOfPosting) {
		this.locationOfPosting = locationOfPosting;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getIdentificationDetails() {
		return identificationDetails;
	}

	public void setIdentificationDetails(String identificationDetails) {
		this.identificationDetails = identificationDetails;
	}

	public Date getEmployedSince() {
		return employedSince;
	}

	public void setEmployedSince(Date employedSince) {
		this.employedSince = employedSince;
	}

	public String getTrainingDetails() {
		return trainingDetails;
	}

	public void setTrainingDetails(String trainingDetails) {
		this.trainingDetails = trainingDetails;
	}

	public Date getLastBackVerDate() {
		return lastBackVerDate;
	}

	public void setLastBackVerDate(Date lastBackVerDate) {
		this.lastBackVerDate = lastBackVerDate;
	}



    
}
