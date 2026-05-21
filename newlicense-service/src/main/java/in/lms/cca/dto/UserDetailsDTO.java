package in.lms.cca.dto;

import org.springframework.web.multipart.MultipartFile;

public class UserDetailsDTO {


	    // Personal Information
	    private String salutation;
	    private String firstName;
	    private String middleName;
	    private String lastName;
	    private String otherFirstName;
	    private String otherMiddleName;
	    private String otherLastName;
	    private String emailId;
	    private String dob;  // Consider using LocalDate for dates
	    private String gender;
	    private String fathersSalutation;
	    private String fatherFirstName;
	    private String fatherMiddleName;
	    private String fatherLastName;

	    // Bank Details
	    private String bankName;
	    private String branchName;
	    private String bankAccountType;
	    private String bankAccountNo;

	    // Bank Draft Details
	    private String draftBankName;
	    private String draftNo;
	    private Double draftAmount;
	    private String draftIssueDate;

	    // Residential Address
	    private String residentialBlockNo;
	    private String residentialVillage;
	    private String residentialPostOffice;
	    private String residentialSubDivision;
	    private String residentialCountry;
	    private String residentialState;
	    private String residentialCity;
	    private String residentialPin;
	    private String residentialTelephoneNo;
	    private String residentialFax;
	    private String residentialMobile;

	    // Official Address
	    private String officialBlockNo;
	    private String officialVillage;
	    private String officialPostOffice;
	    private String officialSubDivision;
	    private String officialCountry;
	    private String officialState;
	    private String officialCity;
	    private String officialPin;
	    private String officialTelephoneNo;
	    private String officialFax;

	    // Credit Card Details
	    private String creditCardType;
	    private String creditCardNo;
	    private String issuedBy;

	    // ISP Details
	    private String ispName;
	    private String ispWebsiteAddress;
	    private String ispUserName;

	    // Web Information
	    private String webPageURL;
	    private String passportNo;
	    private String passportIssuingAuthority;
	    private String passportExpiryDate;

	    // Files
	    private String individualCPSDocument;
	    private String capitalDocument;
	    private String file3;
	    private String file4;

	    // Additional Identification
	    private String voterIdNo;
	    private String incomeTaxPanNo;
	    private String capital;
	    
	    private String remarks;
	    private String userName;
	    private String reviewerUserName;
	    private Boolean isRejected;
	    
	    private String reviewDate;
		private MultipartFile file1;
	    
		public String getRemarks() {
			return remarks;
		}
		public void setRemarks(String remarks) {
			this.remarks = remarks;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getReviewerUserName() {
			return reviewerUserName;
		}
		public void setReviewerUserName(String reviewerUserName) {
			this.reviewerUserName = reviewerUserName;
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
		public String getOtherFirstName() {
			return otherFirstName;
		}
		public void setOtherFirstName(String otherFirstName) {
			this.otherFirstName = otherFirstName;
		}
		public String getOtherMiddleName() {
			return otherMiddleName;
		}
		public void setOtherMiddleName(String otherMiddleName) {
			this.otherMiddleName = otherMiddleName;
		}
		public String getOtherLastName() {
			return otherLastName;
		}
		public void setOtherLastName(String otherLastName) {
			this.otherLastName = otherLastName;
		}
		public String getEmailId() {
			return emailId;
		}
		public void setEmailId(String emailId) {
			this.emailId = emailId;
		}
		public String getDob() {
			return dob;
		}
		public void setDob(String dob) {
			this.dob = dob;
		}
		public String getGender() {
			return gender;
		}
		public void setGender(String gender) {
			this.gender = gender;
		}
		public String getFathersSalutation() {
			return fathersSalutation;
		}
		public void setFathersSalutation(String fathersSalutation) {
			this.fathersSalutation = fathersSalutation;
		}
		public String getFatherFirstName() {
			return fatherFirstName;
		}
		public void setFatherFirstName(String fatherFirstName) {
			this.fatherFirstName = fatherFirstName;
		}
		public String getFatherMiddleName() {
			return fatherMiddleName;
		}
		public void setFatherMiddleName(String fatherMiddleName) {
			this.fatherMiddleName = fatherMiddleName;
		}
		public String getFatherLastName() {
			return fatherLastName;
		}
		public void setFatherLastName(String fatherLastName) {
			this.fatherLastName = fatherLastName;
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
		public String getBankAccountType() {
			return bankAccountType;
		}
		public void setBankAccountType(String bankAccountType) {
			this.bankAccountType = bankAccountType;
		}
		public String getBankAccountNo() {
			return bankAccountNo;
		}
		public void setBankAccountNo(String bankAccountNo) {
			this.bankAccountNo = bankAccountNo;
		}
		public String getIndividualCPSDocument() {
			return individualCPSDocument;
		}
		public void setIndividualCPSDocument(String individualCPSDocument) {
			this.individualCPSDocument = individualCPSDocument;
		}
		public String getCapitalDocument() {
			return capitalDocument;
		}
		public void setCapitalDocument(String capitalDocument) {
			this.capitalDocument = capitalDocument;
		}
		public String getDraftBankName() {
			return draftBankName;
		}
		public void setDraftBankName(String draftBankName) {
			this.draftBankName = draftBankName;
		}
		public String getDraftNo() {
			return draftNo;
		}
		public void setDraftNo(String draftNo) {
			this.draftNo = draftNo;
		}
		public Double getDraftAmount() {
			return draftAmount;
		}
		public void setDraftAmount(Double draftAmount) {
			this.draftAmount = draftAmount;
		}
		public String getDraftIssueDate() {
			return draftIssueDate;
		}
		public void setDraftIssueDate(String draftIssueDate) {
			this.draftIssueDate = draftIssueDate;
		}
		public String getResidentialBlockNo() {
			return residentialBlockNo;
		}
		public void setResidentialBlockNo(String residentialBlockNo) {
			this.residentialBlockNo = residentialBlockNo;
		}
		public String getResidentialVillage() {
			return residentialVillage;
		}
		public void setResidentialVillage(String residentialVillage) {
			this.residentialVillage = residentialVillage;
		}
		public String getResidentialPostOffice() {
			return residentialPostOffice;
		}
		public void setResidentialPostOffice(String residentialPostOffice) {
			this.residentialPostOffice = residentialPostOffice;
		}
		public String getResidentialSubDivision() {
			return residentialSubDivision;
		}
		public void setResidentialSubDivision(String residentialSubDivision) {
			this.residentialSubDivision = residentialSubDivision;
		}
		public String getResidentialCountry() {
			return residentialCountry;
		}
		public void setResidentialCountry(String residentialCountry) {
			this.residentialCountry = residentialCountry;
		}
		public String getResidentialState() {
			return residentialState;
		}
		public void setResidentialState(String residentialState) {
			this.residentialState = residentialState;
		}
		public String getResidentialCity() {
			return residentialCity;
		}
		public void setResidentialCity(String residentialCity) {
			this.residentialCity = residentialCity;
		}
		public String getResidentialPin() {
			return residentialPin;
		}
		public void setResidentialPin(String residentialPin) {
			this.residentialPin = residentialPin;
		}
		public String getResidentialTelephoneNo() {
			return residentialTelephoneNo;
		}
		public void setResidentialTelephoneNo(String residentialTelephoneNo) {
			this.residentialTelephoneNo = residentialTelephoneNo;
		}
		public String getResidentialFax() {
			return residentialFax;
		}
		public void setResidentialFax(String residentialFax) {
			this.residentialFax = residentialFax;
		}
		public String getResidentialMobile() {
			return residentialMobile;
		}
		public void setResidentialMobile(String residentialMobile) {
			this.residentialMobile = residentialMobile;
		}
		public String getOfficialBlockNo() {
			return officialBlockNo;
		}
		public void setOfficialBlockNo(String officialBlockNo) {
			this.officialBlockNo = officialBlockNo;
		}
		public String getOfficialVillage() {
			return officialVillage;
		}
		public void setOfficialVillage(String officialVillage) {
			this.officialVillage = officialVillage;
		}
		public String getOfficialPostOffice() {
			return officialPostOffice;
		}
		public void setOfficialPostOffice(String officialPostOffice) {
			this.officialPostOffice = officialPostOffice;
		}
		public String getOfficialSubDivision() {
			return officialSubDivision;
		}
		public void setOfficialSubDivision(String officialSubDivision) {
			this.officialSubDivision = officialSubDivision;
		}
		public String getOfficialCountry() {
			return officialCountry;
		}
		public void setOfficialCountry(String officialCountry) {
			this.officialCountry = officialCountry;
		}
		public String getOfficialState() {
			return officialState;
		}
		public void setOfficialState(String officialState) {
			this.officialState = officialState;
		}
		public String getOfficialCity() {
			return officialCity;
		}
		public void setOfficialCity(String officialCity) {
			this.officialCity = officialCity;
		}
		public String getOfficialPin() {
			return officialPin;
		}
		public void setOfficialPin(String officialPin) {
			this.officialPin = officialPin;
		}
		public String getOfficialTelephoneNo() {
			return officialTelephoneNo;
		}
		public void setOfficialTelephoneNo(String officialTelephoneNo) {
			this.officialTelephoneNo = officialTelephoneNo;
		}
		public String getOfficialFax() {
			return officialFax;
		}
		public void setOfficialFax(String officialFax) {
			this.officialFax = officialFax;
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
		public String getIssuedBy() {
			return issuedBy;
		}
		public void setIssuedBy(String issuedBy) {
			this.issuedBy = issuedBy;
		}
		public String getIspName() {
			return ispName;
		}
		public void setIspName(String ispName) {
			this.ispName = ispName;
		}
		public String getIspWebsiteAddress() {
			return ispWebsiteAddress;
		}
		public void setIspWebsiteAddress(String ispWebsiteAddress) {
			this.ispWebsiteAddress = ispWebsiteAddress;
		}
		public String getIspUserName() {
			return ispUserName;
		}
		public void setIspUserName(String ispUserName) {
			this.ispUserName = ispUserName;
		}
		public String getWebPageURL() {
			return webPageURL;
		}
		public void setWebPageURL(String webPageURL) {
			this.webPageURL = webPageURL;
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
		public String getPassportExpiryDate() {
			return passportExpiryDate;
		}
		public void setPassportExpiryDate(String passportExpiryDate) {
			this.passportExpiryDate = passportExpiryDate;
		}
		
		public String getFile3() {
			return file3;
		}
		public void setFile3(String file3) {
			this.file3 = file3;
		}
		public String getFile4() {
			return file4;
		}
		public void setFile4(String file4) {
			this.file4 = file4;
		}
		public String getVoterIdNo() {
			return voterIdNo;
		}
		public void setVoterIdNo(String voterIdNo) {
			this.voterIdNo = voterIdNo;
		}
		public String getIncomeTaxPanNo() {
			return incomeTaxPanNo;
		}
		public void setIncomeTaxPanNo(String incomeTaxPanNo) {
			this.incomeTaxPanNo = incomeTaxPanNo;
		}
		public String getCapital() {
			return capital;
		}
		public void setCapital(String capital) {
			this.capital = capital;
		}
		public Boolean getIsRejected() {
			return isRejected;
		}
		public void setIsRejected(Boolean isRejected) {
			this.isRejected = isRejected;
		}
		
		
		
		public String getReviewDate() {
			return reviewDate;
		}
		public void setReviewDate(String reviewDate) {
			this.reviewDate = reviewDate;
		}
		public MultipartFile getFile1() {
			return file1;
		}
		public void setFile1(MultipartFile file1) {
			this.file1 = file1;
		}
		@Override
		public String toString() {
			return "UserDetailsDTO [salutation=" + salutation + ", firstName=" + firstName + ", middleName="
					+ middleName + ", lastName=" + lastName + ", otherFirstName=" + otherFirstName
					+ ", otherMiddleName=" + otherMiddleName + ", otherLastName=" + otherLastName + ", emailId="
					+ emailId + ", dob=" + dob + ", gender=" + gender + ", fathersSalutation=" + fathersSalutation
					+ ", fatherFirstName=" + fatherFirstName + ", fatherMiddleName=" + fatherMiddleName
					+ ", fatherLastName=" + fatherLastName + ", bankName=" + bankName + ", branchName=" + branchName
					+ ", bankAccountType=" + bankAccountType + ", bankAccountNo=" + bankAccountNo + ", draftBankName="
					+ draftBankName + ", draftNo=" + draftNo + ", draftAmount=" + draftAmount + ", draftIssueDate="
					+ draftIssueDate + ", residentialBlockNo=" + residentialBlockNo + ", residentialVillage="
					+ residentialVillage + ", residentialPostOffice=" + residentialPostOffice
					+ ", residentialSubDivision=" + residentialSubDivision + ", residentialCountry="
					+ residentialCountry + ", residentialState=" + residentialState + ", residentialCity="
					+ residentialCity + ", residentialPin=" + residentialPin + ", residentialTelephoneNo="
					+ residentialTelephoneNo + ", residentialFax=" + residentialFax + ", residentialMobile="
					+ residentialMobile + ", officialBlockNo=" + officialBlockNo + ", officialVillage="
					+ officialVillage + ", officialPostOffice=" + officialPostOffice + ", officialSubDivision="
					+ officialSubDivision + ", officialCountry=" + officialCountry + ", officialState=" + officialState
					+ ", officialCity=" + officialCity + ", officialPin=" + officialPin + ", officialTelephoneNo="
					+ officialTelephoneNo + ", officialFax=" + officialFax + ", creditCardType=" + creditCardType
					+ ", creditCardNo=" + creditCardNo + ", issuedBy=" + issuedBy + ", ispName=" + ispName
					+ ", ispWebsiteAddress=" + ispWebsiteAddress + ", ispUserName=" + ispUserName + ", webPageURL="
					+ webPageURL + ", passportNo=" + passportNo + ", passportIssuingAuthority="
					+ passportIssuingAuthority + ", passportExpiryDate=" + passportExpiryDate
					+ ", individualCPSDocument=" + individualCPSDocument + ", capitalDocument=" + capitalDocument
					+ ", file3=" + file3 + ", file4=" + file4 + ", voterIdNo=" + voterIdNo + ", incomeTaxPanNo="
					+ incomeTaxPanNo + ", capital=" + capital + ", remarks=" + remarks + ", userName=" + userName
					+ ", reviewerUserName=" + reviewerUserName + ", isRejected=" + isRejected + "]";
		}
		
		
		
		
		
}
