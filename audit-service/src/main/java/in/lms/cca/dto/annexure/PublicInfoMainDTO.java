package in.lms.cca.dto.annexure;

import java.util.List;

public class PublicInfoMainDTO  {

	private List<PublicInfoDetailsDTO> publicInfoDetails;
	private String userName;
	
	
	public List<PublicInfoDetailsDTO> getPublicInfoDetails() {
		return publicInfoDetails;
	}
	public void setPublicInfoDetails(List<PublicInfoDetailsDTO> publicInfoDetails) {
		this.publicInfoDetails = publicInfoDetails;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
     
	
    
    
}
