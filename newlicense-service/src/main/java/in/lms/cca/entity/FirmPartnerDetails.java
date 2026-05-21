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
@Table(name = "firm_partner_details", schema = "new_license_management")
public class FirmPartnerDetails extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "partner_detail_id", length = 11)
    private Long partnerDetailId;

    @Column(name = "salutation", length = 6)
    private String salutation;

    @Column(name = "first_name", length = 30)
    private String firstName;

    @Column(name = "middle_name", length = 30)
    private String middleName;

    @Column(name = "last_name", length = 45)
    private String lastName;

    @Column(name = "address_id", length = 75)
    private String addressId;

    @Column(name = "telephone_no", length = 12)
    private String telephoneNo;

    @Column(name = "mobile_no", length = 10)
    private String mobileNo;

    @Column(name = "fax", length = 12)
    private String fax;

    @Column(name = "nationality", length = 56)
    private String nationality;

    @Column(name = "visa_type", length = 15)
    private String visaType;

    @Column(name = "visa_number", length = 8)
    private String visaNumber;

    @Column(name = "visa_issue_date")
    private Date visaIssueDate;

    @Column(name = "visa_expiry_date")
    private Date visaExpiryDate;

    @Column(name = "passport_no", length = 9)
    private String passportNo;

    @Column(name = "passport_issuing_authority", length = 75)
    private String passportIssuingAuthority;

    @Column(name = "passport_expiry_date")
    private Date passportExpiryDate;

    @Column(name = "passport_document", length = 70)
    private String passportDocument;

    @Column(name = "voter_id_card", length = 14)
    private String voterIdCard;

    @Column(name = "voter_id_card_document", length = 70)
    private String voterIdCardDocument;

    @Column(name = "pan", length = 10)
    private String pan;

    @Column(name = "pan_document", length = 70)
    private String panDocument;

    @Column(name = "email_id", length = 50)
    private String emailId;

    @Column(name = "personal_web_page", length = 100)
    private String personalWebPage;

    @ManyToOne
    @JoinColumn(name = "intent_app_id", referencedColumnName = "intent_app_id")
    private IntentApplication intentAppId;
    
    @Column(name = "status", length = 8)
    private String status;

    @Column(name = "created_by", length = 74)
    private String createdBy;

    @Column(name = "updated_by", length = 74)
    private String updatedBy;

	public Long getPartnerDetailId() {
		return partnerDetailId;
	}

	public void setPartnerDetailId(Long partnerDetailId) {
		this.partnerDetailId = partnerDetailId;
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

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getVisaType() {
		return visaType;
	}

	public void setVisaType(String visaType) {
		this.visaType = visaType;
	}

	public String getVisaNumber() {
		return visaNumber;
	}

	public void setVisaNumber(String visaNumber) {
		this.visaNumber = visaNumber;
	}

	public Date getVisaIssueDate() {
		return visaIssueDate;
	}

	public void setVisaIssueDate(Date visaIssueDate) {
		this.visaIssueDate = visaIssueDate;
	}

	public Date getVisaExpiryDate() {
		return visaExpiryDate;
	}

	public void setVisaExpiryDate(Date visaExpiryDate) {
		this.visaExpiryDate = visaExpiryDate;
	}

	public String getPassportNo() {
		return passportNo;
	}

	public void setPassportNo(String passportNo) {
		this.passportNo = passportNo;
	}

	public String getPassportIssuingAuthority() {
		return passportIssuingAuthority;
	}

	public void setPassportIssuingAuthority(String passportIssuingAuthority) {
		this.passportIssuingAuthority = passportIssuingAuthority;
	}

	public Date getPassportExpiryDate() {
		return passportExpiryDate;
	}

	public void setPassportExpiryDate(Date passportExpiryDate) {
		this.passportExpiryDate = passportExpiryDate;
	}

	public String getPassportDocument() {
		return passportDocument;
	}

	public void setPassportDocument(String passportDocument) {
		this.passportDocument = passportDocument;
	}

	public String getVoterIdCard() {
		return voterIdCard;
	}

	public void setVoterIdCard(String voterIdCard) {
		this.voterIdCard = voterIdCard;
	}

	public String getVoterIdCardDocument() {
		return voterIdCardDocument;
	}

	public void setVoterIdCardDocument(String voterIdCardDocument) {
		this.voterIdCardDocument = voterIdCardDocument;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getPanDocument() {
		return panDocument;
	}

	public void setPanDocument(String panDocument) {
		this.panDocument = panDocument;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPersonalWebPage() {
		return personalWebPage;
	}

	public void setPersonalWebPage(String personalWebPage) {
		this.personalWebPage = personalWebPage;
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

