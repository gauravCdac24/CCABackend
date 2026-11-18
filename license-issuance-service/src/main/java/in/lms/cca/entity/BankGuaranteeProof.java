package in.lms.cca.entity;

import java.util.Date;

import in.lms.cca.util.golbal.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bank_guarantee_proof", schema="license_issuance_management")
public class BankGuaranteeProof extends AbstractTimestampEntity{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proof_id", length = 11)
    private Long proofId;

	@Column(name = "intent_app_id", length = 74, nullable = false)
    private String intentAppId;
	
    @Column(name = "confirmdoc", length = 100, nullable = false)
    private String confirmDoc;
    
    @Column(name = "issue_date", nullable = false)
    private Date issueDate;
    
    @Column(name = "expiry_date", nullable = false)
    private Date expiryDate;
    
    @Column(name = "transaction_number", length=22, nullable = false)
    private String transactionNumber;

    
    @Column(name = "status", length = 8, nullable = false)
    private String status;

    @Column(name = "created_by", length = 74, nullable = false)
    private String createdBy;

    @Column(name = "updated_by", length = 74, nullable = false)
    private String updatedBy;

	public Long getProofId() {
		return proofId;
	}

	public void setProofId(Long proofId) {
		this.proofId = proofId;
	}

	public String getIntentAppId() {
		return intentAppId;
	}

	public void setIntentAppId(String intentAppId) {
		this.intentAppId = intentAppId;
	}

	public String getConfirmDoc() {
		return confirmDoc;
	}

	public void setConfirmDoc(String confirmDoc) {
		this.confirmDoc = confirmDoc;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
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
