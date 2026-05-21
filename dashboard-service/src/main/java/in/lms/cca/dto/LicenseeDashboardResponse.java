package in.lms.cca.dto;

public class LicenseeDashboardResponse {

	private Long totalASP;
	private Long totaleSignIssued;
	private Long totalDSCIssued;
	
	
	
	public Long getTotalASP() {
		return totalASP;
	}
	public void setTotalASP(Long totalASP) {
		this.totalASP = totalASP;
	}
	public Long getTotaleSignIssued() {
		return totaleSignIssued;
	}
	public void setTotaleSignIssued(Long totaleSignIssued) {
		this.totaleSignIssued = totaleSignIssued;
	}
	public Long getTotalDSCIssued() {
		return totalDSCIssued;
	}
	public void setTotalDSCIssued(Long totalDSCIssued) {
		this.totalDSCIssued = totalDSCIssued;
	}
	
}
