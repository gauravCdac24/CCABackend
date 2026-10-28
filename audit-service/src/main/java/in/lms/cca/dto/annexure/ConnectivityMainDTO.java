package in.lms.cca.dto.annexure;

import java.util.List;

public class ConnectivityMainDTO {

	private List<ConnectivityDetailsDTO> connectivityDetails;
	private String userName;
	
	
	public List<ConnectivityDetailsDTO> getConnectivityDetails() {
		return connectivityDetails;
	}
	public void setConnectivityDetails(List<ConnectivityDetailsDTO> connectivityDetails) {
		this.connectivityDetails = connectivityDetails;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

    
	    
    
}
