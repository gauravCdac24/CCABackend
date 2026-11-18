package in.lms.cca.dto;

public class TotalDSCeSignIssuedResponse {

    private String licenseeName;
    private Long dscIssued;
    private Long eSignIssued;
    
    
	public String getLicenseeName() {
		return licenseeName;
	}
	public void setLicenseeName(String licenseeName) {
		this.licenseeName = licenseeName;
	}
	public Long getDscIssued() {
		return dscIssued;
	}
	public void setDscIssued(Long dscIssued) {
		this.dscIssued = dscIssued;
	}
	public Long geteSignIssued() {
		return eSignIssued;
	}
	public void seteSignIssued(Long eSignIssued) {
		this.eSignIssued = eSignIssued;
	}
	
    
    
    
	
}
