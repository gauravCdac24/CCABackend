package in.lms.cca.payload;

public class AdditionalDetailsDTO {

    // Inner class for Bank Details
    public static class BankDetails {
    	private String bankDetailsId;
        private String bankName;
        private String branch;
        private String accountType;
        private String accountNo;
        private String status;
  	  private String createdBy;
  	  private String updatedBy;
  	  private String created;
  	  private String updated;
  	  
  	  

        public String getBankDetailsId() {
		return bankDetailsId;
	}

	public void setBankDetailsId(String bankDetailsId) {
		this.bankDetailsId = bankDetailsId;
	}

		// Getters and Setters
        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getBranch() {
            return branch;
        }

        public void setBranch(String branch) {
            this.branch = branch;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public String getAccountNo() {
            return accountNo;
        }

        public void setAccountNo(String accountNo) {
            this.accountNo = accountNo;
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

    	public String getCreated() {
    		return created;
    	}

    	public void setCreated(String created) {
    		this.created = created;
    	}

    	public String getUpdated() {
    		return updated;
    	}

    	public void setUpdated(String updated) {
    		this.updated = updated;
    	}
    }

    // Inner class for Bank Draft details
    public static class BankDraft {
    	private String bankDraftId;
    	private String bankName;
        private String draftNo;
        private String issueDate;
        private String amount;
        private String status;
  	  private String createdBy;
  	  private String updatedBy;
  	  private String created;
  	  private String updated;

        public String getBankDraftId() {
		return bankDraftId;
	}

	public void setBankDraftId(String bankDraftId) {
		this.bankDraftId = bankDraftId;
	}

		// Getters and Setters
        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getDraftNo() {
            return draftNo;
        }

        public void setDraftNo(String draftNo) {
            this.draftNo = draftNo;
        }

        public String getIssueDate() {
            return issueDate;
        }

        public void setIssueDate(String issueDate) {
            this.issueDate = issueDate;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
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

    	public String getCreated() {
    		return created;
    	}

    	public void setCreated(String created) {
    		this.created = created;
    	}

    	public String getUpdated() {
    		return updated;
    	}

    	public void setUpdated(String updated) {
    		this.updated = updated;
    	}
    }

    private BankDetails bankDetails;
    private BankDraft bankDraft;
    private boolean knownByOtherName;
    private boolean DSC;
    private boolean eSign;
    private boolean timeStamp;
    private boolean SSL;
    private String status;
    private String userName;
	  private String createdBy;
	  private String updatedBy;
	  private String created;
	  private String updated;
	  
	  

    public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	// Getters and Setters for the main class
    public BankDetails getBankDetails() {
        return bankDetails;
    }

    public void setBankDetails(BankDetails bankDetails) {
        this.bankDetails = bankDetails;
    }

    public BankDraft getBankDraft() {
        return bankDraft;
    }

    public void setBankDraft(BankDraft bankDraft) {
        this.bankDraft = bankDraft;
    }

    public boolean isKnownByOtherName() {
        return knownByOtherName;
    }

    public void setKnownByOtherName(boolean knownByOtherName) {
        this.knownByOtherName = knownByOtherName;
    }

    public boolean isDSC() {
        return DSC;
    }

    public void setDSC(boolean DSC) {
        this.DSC = DSC;
    }

    public boolean iseSign() {
        return eSign;
    }

    public void seteSign(boolean eSign) {
        this.eSign = eSign;
    }

    public boolean isTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(boolean timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isSSL() {
        return SSL;
    }

    public void setSSL(boolean SSL) {
        this.SSL = SSL;
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

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
		this.updated = updated;
	}
    
    
}
