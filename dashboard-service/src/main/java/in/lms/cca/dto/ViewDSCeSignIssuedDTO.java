package in.lms.cca.dto;


public class ViewDSCeSignIssuedDTO {

	private String caUsername;
    private String totalDSCIssued;
    private String totalEsignIssued;
    private String month;
    private String year;
    
    
	public String getCaUsername() {
		return caUsername;
	}
	public void setCaUsername(String caUsername) {
		this.caUsername = caUsername;
	}
	public String getTotalDSCIssued() {
		return totalDSCIssued;
	}
	public void setTotalDSCIssued(String totalDSCIssued) {
		this.totalDSCIssued = totalDSCIssued;
	}
	public String getTotalEsignIssued() {
		return totalEsignIssued;
	}
	public void setTotalEsignIssued(String totalEsignIssued) {
		this.totalEsignIssued = totalEsignIssued;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
    
	
	
	
}
