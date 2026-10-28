package in.lms.cca.dto;

import java.util.List;

public class CAWithYearOfLicensing {

	private String caName;
	private List<CALicenseDetailsDTO> caLicense;
	
	
	public String getCaName() {
		return caName;
	}
	public void setCaName(String caName) {
		this.caName = caName;
	}
	public List<CALicenseDetailsDTO> getCaLicense() {
		return caLicense;
	}
	public void setCaLicense(List<CALicenseDetailsDTO> caLicense) {
		this.caLicense = caLicense;
	}
	
	

	
}


