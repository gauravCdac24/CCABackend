package in.lms.cca.dto;

public class DataIssuedDTO {

	private String dscesignIssuedId;
    private String countryId;
    private String dscIssued;
    private String eSignIssued;
    private String stateId;
    
    
	public String getDscesignIssuedId() {
		return dscesignIssuedId;
	}
	public void setDscesignIssuedId(String dscesignIssuedId) {
		this.dscesignIssuedId = dscesignIssuedId;
	}
	public String getCountryId() {
		return countryId;
	}
	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}
	public String getDscIssued() {
		return dscIssued;
	}
	public void setDscIssued(String dscIssued) {
		this.dscIssued = dscIssued;
	}
	public String geteSignIssued() {
		return eSignIssued;
	}
	public void seteSignIssued(String eSignIssued) {
		this.eSignIssued = eSignIssued;
	}
	public String getStateId() {
		return stateId;
	}
	public void setStateId(String stateId) {
		this.stateId = stateId;
	}
    
    
	
}
