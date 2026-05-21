package in.lms.cca.dto;


public class EsignRequestDTO {

	private String eSignRequest;
	private String aspTxnID;
	private String contentType;
	
	
	public String geteSignRequest() {
		return eSignRequest;
	}
	public void seteSignRequest(String eSignRequest) {
		this.eSignRequest = eSignRequest;
	}
	public String getAspTxnID() {
		return aspTxnID;
	}
	public void setAspTxnID(String aspTxnID) {
		this.aspTxnID = aspTxnID;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	
	
	
	
	
}
