package in.lms.cca.dto;

import java.util.List;

public class CCAGraphDataResponse {

	private List<Integer> years;
	private List<DSCeSignIssuedPayload> yearwise;
	
	private List<String> months;
	private List<DSCeSignIssuedPayload> monthwise;
	
	private List<String> states;
	private List<DSCeSignIssuedPayload> statewise;
	
	private List<String> ca;
	private List<DSCeSignIssuedPayload> cawise;
	
	private List<CAWithYearOfLicensing> cayoflicensing;
	
	private List<ESPWithEkycModeDTO> espEkycModeApproved;
	
	public List<Integer> getYears() {
		return years;
	}
	public void setYears(List<Integer> years) {
		this.years = years;
	}
	public List<DSCeSignIssuedPayload> getYearwise() {
		return yearwise;
	}
	public void setYearwise(List<DSCeSignIssuedPayload> yearwise) {
		this.yearwise = yearwise;
	}
	public List<String> getMonths() {
		return months;
	}
	public void setMonths(List<String> months) {
		this.months = months;
	}
	public List<DSCeSignIssuedPayload> getMonthwise() {
		return monthwise;
	}
	public void setMonthwise(List<DSCeSignIssuedPayload> monthwise) {
		this.monthwise = monthwise;
	}
	public List<String> getStates() {
		return states;
	}
	public void setStates(List<String> states) {
		this.states = states;
	}
	public List<DSCeSignIssuedPayload> getStatewise() {
		return statewise;
	}
	public void setStatewise(List<DSCeSignIssuedPayload> statewise) {
		this.statewise = statewise;
	}
	public List<String> getCa() {
		return ca;
	}
	public void setCa(List<String> ca) {
		this.ca = ca;
	}
	public List<DSCeSignIssuedPayload> getCawise() {
		return cawise;
	}
	public void setCawise(List<DSCeSignIssuedPayload> cawise) {
		this.cawise = cawise;
	}
	public List<CAWithYearOfLicensing> getCayoflicensing() {
		return cayoflicensing;
	}
	public void setCayoflicensing(List<CAWithYearOfLicensing> cayoflicensing) {
		this.cayoflicensing = cayoflicensing;
	}
	public List<ESPWithEkycModeDTO> getEspEkycModeApproved() {
		return espEkycModeApproved;
	}
	public void setEspEkycModeApproved(List<ESPWithEkycModeDTO> espEkycModeApproved) {
		this.espEkycModeApproved = espEkycModeApproved;
	}
	
	
}
