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
@Table(name = "indiv_additional_details", schema = "new_license_management")
public class IndivAdditionalDetails extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "indiv_additional_details_id", length = 11)
    private Long indivAdditionalDetailsId;

    @Column(name = "credit_card_type", length = 45)
    private String creditCardType;

    @Column(name = "credit_card_no", length = 19)
    private String creditCardNo;

    @Column(name = "credit_card_issued_by", length = 40)
    private String creditCardIssuedBy;

    @Column(name = "web_url", length = 100)
    private String webURL;

    @Column(name = "passport_no", length = 20)
    private String passportNo;

    @Column(name = "passport_issuing_authority", length = 75)
    private String passportIssuingAuthority;

    @Column(name = "passport_expiry_date")
    private Date passportExpiryDate;

    @Column(name = "voter_id", length = 24)
    private String voterId;

    @Column(name = "pan", length = 20)
    private String pan;

    @Column(name = "isp_name", length = 75)
    private String ispName;

    @Column(name = "isp_website", length = 100)
    private String ispWebsite;

    @Column(name = "isp_username", length = 25)
    private String ispUsername;

    @Column(name = "personal_web_page", length = 100)
    private String personalWebPage;

    @Column(name = "indiv_capital", length = 10)
    private String indivCapital;

    @ManyToOne
    @JoinColumn(name = "intent_app_id", referencedColumnName = "intent_app_id")
    private IntentApplication intentAppId;

    @Column(name = "status", length = 8)
    private String status;

    @Column(name = "created_by", length = 74)
    private String createdBy;

    @Column(name = "updated_by", length = 74)
    private String updatedBy;

    
    
	public Long getIndivAdditionalDetailsId() {
		return indivAdditionalDetailsId;
	}

	public void setIndivAdditionalDetailsId(Long indivAdditionalDetailsId) {
		this.indivAdditionalDetailsId = indivAdditionalDetailsId;
	}

	public String getCreditCardType() {
		return creditCardType;
	}

	public void setCreditCardType(String creditCardType) {
		this.creditCardType = creditCardType;
	}

	public String getCreditCardNo() {
		return creditCardNo;
	}

	public void setCreditCardNo(String creditCardNo) {
		this.creditCardNo = creditCardNo;
	}

	public String getCreditCardIssuedBy() {
		return creditCardIssuedBy;
	}

	public void setCreditCardIssuedBy(String creditCardIssuedBy) {
		this.creditCardIssuedBy = creditCardIssuedBy;
	}

	public String getWebURL() {
		return webURL;
	}

	public void setWebURL(String webURL) {
		this.webURL = webURL;
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

	public String getVoterId() {
		return voterId;
	}

	public void setVoterId(String voterId) {
		this.voterId = voterId;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getIspName() {
		return ispName;
	}

	public void setIspName(String ispName) {
		this.ispName = ispName;
	}

	public String getIspWebsite() {
		return ispWebsite;
	}

	public void setIspWebsite(String ispWebsite) {
		this.ispWebsite = ispWebsite;
	}

	public String getIspUsername() {
		return ispUsername;
	}

	public void setIspUsername(String ispUsername) {
		this.ispUsername = ispUsername;
	}

	public String getPersonalWebPage() {
		return personalWebPage;
	}

	public void setPersonalWebPage(String personalWebPage) {
		this.personalWebPage = personalWebPage;
	}

	public String getIndivCapital() {
		return indivCapital;
	}

	public void setIndivCapital(String indivCapital) {
		this.indivCapital = indivCapital;
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

