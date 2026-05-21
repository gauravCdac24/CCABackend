package in.lms.cca.dto;

public class OrganisationDetailsDTO {

    private String DR;
    private String PR;
    private String administrativeMinistry;
    private String bankAccountNo;
    private String bankAccountType;
    private String bankName;
    private String blockNo;
    private String branchName;
    private String designation;
    private String emailId;
    private String fax;
    private String firmCPSDocument;
    private String nameOfHead;
    private String orgName;
    private String orgType;
    private String remarks;
    private String reviewerUserName;
    private String telephoneNo;
    private String userName;
    private String village;
    private String webPageURL;

    // Getters and Setters
    public String getDR() {
        return DR;
    }

    public void setDR(String DR) {
        this.DR = DR;
    }

    public String getPR() {
        return PR;
    }

    public void setPR(String PR) {
        this.PR = PR;
    }

    public String getAdministrativeMinistry() {
        return administrativeMinistry;
    }

    public void setAdministrativeMinistry(String administrativeMinistry) {
        this.administrativeMinistry = administrativeMinistry;
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

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBlockNo() {
        return blockNo;
    }

    public void setBlockNo(String blockNo) {
        this.blockNo = blockNo;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
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

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

   
    public String getFirmCPSDocument() {
		return firmCPSDocument;
	}

	public void setFirmCPSDocument(String firmCPSDocument) {
		this.firmCPSDocument = firmCPSDocument;
	}

	public String getNameOfHead() {
        return nameOfHead;
    }

    public void setNameOfHead(String nameOfHead) {
        this.nameOfHead = nameOfHead;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getReviewerUserName() {
        return reviewerUserName;
    }

    public void setReviewerUserName(String reviewerUserName) {
        this.reviewerUserName = reviewerUserName;
    }

    public String getTelephoneNo() {
        return telephoneNo;
    }

    public void setTelephoneNo(String telephoneNo) {
        this.telephoneNo = telephoneNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getWebPageURL() {
        return webPageURL;
    }

    public void setWebPageURL(String webPageURL) {
        this.webPageURL = webPageURL;
    }

	@Override
	public String toString() {
		return "OrganisationDetailsDTO [DR=" + DR + ", PR=" + PR + ", administrativeMinistry=" + administrativeMinistry
				+ ", bankAccountNo=" + bankAccountNo + ", bankAccountType=" + bankAccountType + ", bankName=" + bankName
				+ ", blockNo=" + blockNo + ", branchName=" + branchName + ", designation=" + designation + ", emailId="
				+ emailId + ", fax=" + fax + ", firmCPSDocument=" + firmCPSDocument + ", nameOfHead=" + nameOfHead
				+ ", orgName=" + orgName + ", orgType=" + orgType + ", remarks=" + remarks + ", reviewerUserName="
				+ reviewerUserName + ", telephoneNo=" + telephoneNo + ", userName=" + userName + ", village=" + village
				+ ", webPageURL=" + webPageURL + "]";
	}
    
    
}

