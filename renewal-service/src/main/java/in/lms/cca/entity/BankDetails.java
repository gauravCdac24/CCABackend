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
@Table(name = "bank_details", schema="new_license_management")
public class BankDetails extends AbstractTimestampEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bank_details_id", length = 11)
    private Long bankDetailsId;

    @Column(name = "bank_name", length = 75)
    private String bankName;

    @Column(name = "branch_name", length = 50)
    private String branchName;

    @Column(name = "bank_account_no", length = 18)
    private String bankAccountNo;

    @Column(name = "bank_account_type", length = 20)
    private String bankAccountType;

    @Column(name = "ifsc", length = 11)
    private String ifsc;

    @Column(name = "status", length = 8)
    private String status;

    @Column(name = "created_by", length = 74)
    private String createdBy;

    @Column(name = "updated_by", length = 74)
    private String updatedBy;
    
    @ManyToOne
    @JoinColumn(name = "intent_app_id", referencedColumnName = "intent_app_id")
    private IntentApplication intentAppId;


	public IntentApplication getIntentAppId() {
		return intentAppId;
	}

	public void setIntentAppId(IntentApplication intentAppId) {
		this.intentAppId = intentAppId;
	}

	public Long getBankDetailsId() {
		return bankDetailsId;
	}

	public void setBankDetailsId(Long bankDetailsId) {
		this.bankDetailsId = bankDetailsId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getBankAccountNo() {
		return bankAccountNo;
	}

	public void setBankAccountNo(String bankAccountNo) {
		this.bankAccountNo = bankAccountNo;
	}

	public String getBankAccountType() {
		return bankAccountType;
	}

	public void setBankAccountType(String bankAccountType) {
		this.bankAccountType = bankAccountType;
	}

	public String getIfsc() {
		return ifsc;
	}

	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
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
