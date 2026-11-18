package in.lms.cca.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import in.lms.cca.util.global.AbstractTimestampEntity;

@Entity
@Table(name = "indiv_emails", schema = "new_license_management")
public class IndivEmails extends AbstractTimestampEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "indiv_email_id",length=11)
    private Long indivEmailId;

    @Column(name = "email_id", length = 50)
    private String emailId;

    @ManyToOne
    @JoinColumn(name = "intent_app_id", referencedColumnName = "intent_app_id")
    private IntentApplication intentAppId;

    @Column(name = "created_by", length = 74)
    private String createdBy;  

    @Column(name = "updated_by", length = 74)
    private String updatedBy; 

    @Column(name="status", length = 8)
    private String status;

	public Long getIndivEmailId() {
		return indivEmailId;
	}

	public void setIndivEmailId(Long indivEmailId) {
		this.indivEmailId = indivEmailId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public IntentApplication getIntentAppId() {
		return intentAppId;
	}

	public void setIntentAppId(IntentApplication intentAppId) {
		this.intentAppId = intentAppId;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


    
    
}

