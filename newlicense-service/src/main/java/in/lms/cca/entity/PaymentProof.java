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
import java.util.Date;

@Entity
@Table(name = "payment_proof", schema = "new_license_management")
public class PaymentProof extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_proof_id", length = 11)
    private Long paymentProofId;

    @OneToOne
   	@JoinColumn(name = "intent_app_id", referencedColumnName = "intent_app_id")
    private IntentApplication indivApplicationId;

    @Column(name = "transaction_id", length = 30)
    private String transactionID;

    @Column(name = "date_of_payment")
    private Date dateOfPayment;

    @Column(name = "amount", length = 6)
    private Integer amount;

    @Column(name = "payment_proof_doc", length = 100)
    private String paymentProofDoc;

    @Column(name = "status", length = 8)
    private String status;

    @Column(name = "created_by", length = 74)
    private String createdBy;

    @Column(name = "updated_by", length = 74)
    private String updatedBy;

	public Long getPaymentProofId() {
		return paymentProofId;
	}

	public void setPaymentProofId(Long paymentProofId) {
		this.paymentProofId = paymentProofId;
	}

	

	public IntentApplication getIndivApplicationId() {
		return indivApplicationId;
	}

	public void setIndivApplicationId(IntentApplication indivApplicationId) {
		this.indivApplicationId = indivApplicationId;
	}

	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public Date getDateOfPayment() {
		return dateOfPayment;
	}

	public void setDateOfPayment(Date dateOfPayment) {
		this.dateOfPayment = dateOfPayment;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getPaymentProofDoc() {
		return paymentProofDoc;
	}

	public void setPaymentProofDoc(String paymentProofDoc) {
		this.paymentProofDoc = paymentProofDoc;
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
