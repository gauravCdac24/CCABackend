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
@Table(name = "govt_organization_application", schema = "new_license_management")
public class GovtOrganizationApplication extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "org_application_id", length = 11)
    private Long orgApplicationId;

    @Column(name = "org_name", length = 75)
    private String orgName;

    @Column(name = "administrative_ministry", length = 75)
    private String administrativeMinistry;

    @Column(name = "org_type", length = 7)
    private String orgType;

    @Column(name = "address_id", length = 75)
    private String addressId;

    @Column(name = "telephone_no", length = 12)
    private String telephoneNo;

    @Column(name = "fax", length = 12)
    private String fax;

    @Column(name = "web_page_url", length = 100)
    private String webPageURL;

    @Column(name = "salutation", length = 6)
    private String salutation;

    @Column(name = "first_name", length = 30)
    private String firstName;

    @Column(name = "middle_name", length = 30)
    private String middleName;

    @Column(name = "last_name", length = 45)
    private String lastName;

    @Column(name = "designation", length = 35)
    private String designation;

    @Column(name = "email_id", length = 50)
    private String emailId;

    @Column(name = "public_key", columnDefinition = "TEXT")
    private String publicKey;

    @Column(name = "third_party_testing_report", length = 30)
    private String thirdPartyTestingReport;

    @ManyToOne
    @JoinColumn(name = "intent_app_id", referencedColumnName = "intent_app_id")
    private IntentApplication intentAppId;
    
    @Column(name = "status", length = 8)
    private String status;

    @Column(name = "created_by", length = 74)
    private String createdBy;

    @Column(name = "updated_by", length = 74)
    private String updatedBy;

	public Long getOrgApplicationId() {
		return orgApplicationId;
	}

	public void setOrgApplicationId(Long orgApplicationId) {
		this.orgApplicationId = orgApplicationId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getAdministrativeMinistry() {
		return administrativeMinistry;
	}

	public void setAdministrativeMinistry(String administrativeMinistry) {
		this.administrativeMinistry = administrativeMinistry;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
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

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getWebPageURL() {
		return webPageURL;
	}

	public void setWebPageURL(String webPageURL) {
		this.webPageURL = webPageURL;
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

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getThirdPartyTestingReport() {
		return thirdPartyTestingReport;
	}

	public void setThirdPartyTestingReport(String thirdPartyTestingReport) {
		this.thirdPartyTestingReport = thirdPartyTestingReport;
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
