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
import java.util.Date;

@Entity
@Table(name = "indiv_application", schema = "new_license_management")
public class IndivApplication extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "indiv_application_id", length = 11)
    private Long indivApplicationId;

    @Column(name = "salutation1", length = 6)
    private String salutation1;

    @Column(name = "first_name1", length = 30)
    private String firstName1;

    @Column(name = "middle_name1", length = 30)
    private String middleName1;

    @Column(name = "last_name1", length = 45)
    private String lastName1;

    @Column(name = "salutation2", length = 6)
    private String salutation2;

    @Column(name = "first_name2", length = 30)
    private String firstName2;

    @Column(name = "middle_name2", length = 30)
    private String middleName2;

    @Column(name = "last_name2", length = 45)
    private String lastName2;

    @Column(name = "fsalutation", length = 6)
    private String fsalutation;

    @Column(name = "ffirst_name", length = 30)
    private String ffirstName;

    @Column(name = "fmiddle_name", length = 30)
    private String fmiddleName;

    @Column(name = "flast_name", length = 45)
    private String flastName;

    @Column(name = "gender", length = 6)
    private String gender;

    @Column(name = "dob")
    private Date dob;

    @Column(name = "nationality", length = 56)
    private String nationality;

    @ManyToOne
	@JoinColumn(name = "location_id", referencedColumnName = "location_id")
    private AppLocation communicationAddress;

    @ManyToOne
    @JoinColumn(name = "intent_app_id", referencedColumnName = "intent_app_id")
    private IntentApplication intentAppId;

    @Column(name = "status", length = 8)
    private String status;

    @Column(name = "created_by", length = 74)
    private String createdBy;

    @Column(name = "updated_by", length = 74)
    private String updatedBy;
    
    
    

	public Long getIndivApplicationId() {
		return indivApplicationId;
	}

	public void setIndivApplicationId(Long indivApplicationId) {
		this.indivApplicationId = indivApplicationId;
	}

	public String getSalutation1() {
		return salutation1;
	}

	public void setSalutation1(String salutation1) {
		this.salutation1 = salutation1;
	}

	public String getFirstName1() {
		return firstName1;
	}

	public void setFirstName1(String firstName1) {
		this.firstName1 = firstName1;
	}

	public String getMiddleName1() {
		return middleName1;
	}

	public void setMiddleName1(String middleName1) {
		this.middleName1 = middleName1;
	}

	public String getLastName1() {
		return lastName1;
	}

	public void setLastName1(String lastName1) {
		this.lastName1 = lastName1;
	}

	public String getSalutation2() {
		return salutation2;
	}

	public void setSalutation2(String salutation2) {
		this.salutation2 = salutation2;
	}

	public String getFirstName2() {
		return firstName2;
	}

	public void setFirstName2(String firstName2) {
		this.firstName2 = firstName2;
	}

	public String getMiddleName2() {
		return middleName2;
	}

	public void setMiddleName2(String middleName2) {
		this.middleName2 = middleName2;
	}

	public String getLastName2() {
		return lastName2;
	}

	public void setLastName2(String lastName2) {
		this.lastName2 = lastName2;
	}

	public String getFsalutation() {
		return fsalutation;
	}

	public void setFsalutation(String fsalutation) {
		this.fsalutation = fsalutation;
	}

	public String getFfirstName() {
		return ffirstName;
	}

	public void setFfirstName(String ffirstName) {
		this.ffirstName = ffirstName;
	}

	public String getFmiddleName() {
		return fmiddleName;
	}

	public void setFmiddleName(String fmiddleName) {
		this.fmiddleName = fmiddleName;
	}

	public String getFlastName() {
		return flastName;
	}

	public void setFlastName(String flastName) {
		this.flastName = flastName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	

	public AppLocation getCommunicationAddress() {
		return communicationAddress;
	}

	public void setCommunicationAddress(AppLocation communicationAddress) {
		this.communicationAddress = communicationAddress;
	}

	public IntentApplication getIntentAppId() {
		return intentAppId;
	}

	public void setIntentAppId(IntentApplication intentAppId) {
		this.intentAppId = intentAppId;
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

