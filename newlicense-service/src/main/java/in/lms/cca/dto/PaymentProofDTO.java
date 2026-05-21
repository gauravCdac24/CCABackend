package in.lms.cca.dto;

import org.springframework.web.multipart.MultipartFile;

public class PaymentProofDTO {

    private Long intentAppId;
    private MultipartFile file;
    private String amount;
    private String selectedDate;
    private String transactionNumber;

    // Constructors
    public PaymentProofDTO() {}

    public PaymentProofDTO(Long intentAppId, MultipartFile file, String amount, String selectedDate, String transactionNumber) {
        this.intentAppId = intentAppId;
        this.file = file;
        this.amount = amount;
        this.selectedDate = selectedDate;
        this.transactionNumber = transactionNumber;
    }

    // Getters and Setters
    public Long getIntentAppId() {
        return intentAppId;
    }

    public void setIntentAppId(Long intentAppId) {
        this.intentAppId = intentAppId;
    }


    public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

	@Override
	public String toString() {
		return "PaymentProofDTO [intentAppId=" + intentAppId + ", amount=" + amount + ", selectedDate=" + selectedDate
				+ ", transactionNumber=" + transactionNumber + "]";
	}

    
}