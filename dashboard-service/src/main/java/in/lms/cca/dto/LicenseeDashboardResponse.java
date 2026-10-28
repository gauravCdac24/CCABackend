package in.lms.cca.dto;

public class LicenseeDashboardResponse {

	private Long totalASP;
	private Long totaleSignIssued;
	private Long totalDSCIssued;
	private Boolean providesDsc;
	private Boolean providesEsign;
	
	
	
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
	public Boolean getProvidesDsc() {
		return providesDsc;
	}
	public void setProvidesDsc(Boolean providesDsc) {
		this.providesDsc = providesDsc;
	}
	public Boolean getProvidesEsign() {
		return providesEsign;
	}
	public void setProvidesEsign(Boolean providesEsign) {
		this.providesEsign = providesEsign;
	}
	
}
