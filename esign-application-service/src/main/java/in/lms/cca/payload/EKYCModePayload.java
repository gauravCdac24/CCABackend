package in.lms.cca.payload;

import java.util.List;

public class EKYCModePayload{

    private List<Long> ekycMode;
    private List<String> ekycModeTitles;
    private String purpose;
    
    
	public List<Long> getEkycMode() {
		return ekycMode;
	}
	public void setEkycMode(List<Long> ekycMode) {
		this.ekycMode = ekycMode;
	}
	public List<String> getEkycModeTitles() {
		return ekycModeTitles;
	}
	public void setEkycModeTitles(List<String> ekycModeTitles) {
		this.ekycModeTitles = ekycModeTitles;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
    
    

   
    
    
       
}

