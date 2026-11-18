package in.lms.cca.payload;

public class EmployeeDetailsDTO {
	
	private String name;
    private String designation;
    private String locationOfPosting;
    private String dcDr;
    private String roleInCA;
    private String idCardNo;
    private String mobileNo;
    private String caPayrollIdentification;
    private String employedSince;
    private String trainingDetails;
    private String backgroundVerificationDate;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getLocationOfPosting() {
		return locationOfPosting;
	}
	public void setLocationOfPosting(String locationOfPosting) {
		this.locationOfPosting = locationOfPosting;
	}
	public String getDcDr() {
		return dcDr;
	}
	public void setDcDr(String dcDr) {
		this.dcDr = dcDr;
	}
	public String getRoleInCA() {
		return roleInCA;
	}
	public void setRoleInCA(String roleInCA) {
		this.roleInCA = roleInCA;
	}
	public String getIdCardNo() {
		return idCardNo;
	}
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getCaPayrollIdentification() {
		return caPayrollIdentification;
	}
	public void setCaPayrollIdentification(String caPayrollIdentification) {
		this.caPayrollIdentification = caPayrollIdentification;
	}
	public String getEmployedSince() {
		return employedSince;
	}
	public void setEmployedSince(String employedSince) {
		this.employedSince = employedSince;
	}
	public String getTrainingDetails() {
		return trainingDetails;
	}
	public void setTrainingDetails(String trainingDetails) {
		this.trainingDetails = trainingDetails;
	}
	public String getBackgroundVerificationDate() {
		return backgroundVerificationDate;
	}
	public void setBackgroundVerificationDate(String backgroundVerificationDate) {
		this.backgroundVerificationDate = backgroundVerificationDate;
	}
	@Override
	public String toString() {
		return "EmployeeDetailsDTO [name=" + name + ", designation=" + designation + ", locationOfPosting="
				+ locationOfPosting + ", dcDr=" + dcDr + ", roleInCA=" + roleInCA + ", idCardNo=" + idCardNo
				+ ", mobileNo=" + mobileNo + ", caPayrollIdentification=" + caPayrollIdentification + ", employedSince="
				+ employedSince + ", trainingDetails=" + trainingDetails + ", backgroundVerificationDate="
				+ backgroundVerificationDate + "]";
	}
    
    
    

}
