package in.lms.cca.dto;

import java.util.List;

public class ESPWithEkycModeDTO {

	private String espUserName;
	private List<String> ekycModeApproved;
	
	
	public String getEspUserName() {
		return espUserName;
	}
	public void setEspUserName(String espUserName) {
		this.espUserName = espUserName;
	}
	public List<String> getEkycModeApproved() {
		return ekycModeApproved;
	}
	public void setEkycModeApproved(List<String> ekycModeApproved) {
		this.ekycModeApproved = ekycModeApproved;
	}
	
	
	
	
}
