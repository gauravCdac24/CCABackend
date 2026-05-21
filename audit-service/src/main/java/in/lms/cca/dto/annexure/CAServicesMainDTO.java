package in.lms.cca.dto.annexure;

import java.util.List;

public class CAServicesMainDTO {

	private List<CAServicesDetailsDTO> caServicesDetails;
	private String userName;
	
	
	public List<CAServicesDetailsDTO> getCaServicesDetails() {
		return caServicesDetails;
	}
	public void setCaServicesDetails(List<CAServicesDetailsDTO> caServicesDetails) {
		this.caServicesDetails = caServicesDetails;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
    
}
