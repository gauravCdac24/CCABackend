package in.lms.cca.dto;

public class EsignResponseDTO {

	private String eSignResponse;
	private String espTxnID;

	public String geteSignResponse() {
		return eSignResponse;
	}

	public void seteSignResponse(String eSignResponse) {
		this.eSignResponse = eSignResponse;
	}

	public String getEspTxnID() {
		return espTxnID;
	}

	public void setEspTxnID(String espTxnID) {
		this.espTxnID = espTxnID;
	}
	
	
	
}
