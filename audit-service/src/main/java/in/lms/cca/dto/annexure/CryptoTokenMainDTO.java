package in.lms.cca.dto.annexure;

import java.util.List;

public class CryptoTokenMainDTO {

    private List<CryptoTokenDetailsDTO> cryptoTokenDetails;
	private String userName;
	
	
	public List<CryptoTokenDetailsDTO> getCryptoTokenDetails() {
		return cryptoTokenDetails;
	}
	public void setCryptoTokenDetails(List<CryptoTokenDetailsDTO> cryptoTokenDetails) {
		this.cryptoTokenDetails = cryptoTokenDetails;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
    
	
	    
    
}
