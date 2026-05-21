package in.lms.cca.dto.annexure;

import java.util.List;

public class LocationMainDTO {

	private List<LocationDetailsDTO> locationDetails;
	private String userName;
	
	
	public List<LocationDetailsDTO> getLocationDetails() {
		return locationDetails;
	}
	public void setLocationDetails(List<LocationDetailsDTO> locationDetails) {
		this.locationDetails = locationDetails;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	

    
    
    
}
