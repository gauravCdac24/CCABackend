package in.lms.cca.dto.annexure;

import java.util.List;

public class EKYCMonthMainDTO {
	
	private List<EKYCAcMonthDetailsDTO> ekycMonthDetails;
	private String userName;
	 
	public List<EKYCAcMonthDetailsDTO> getEkycMonthDetails() {
		return ekycMonthDetails;
	}
	public void setEkycMonthDetails(List<EKYCAcMonthDetailsDTO> ekycMonthDetails) {
		this.ekycMonthDetails = ekycMonthDetails;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	 
	
}

