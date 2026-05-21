package in.lms.cca.dto;

public class StateDTO{
	
	private Long stateId;
	private CountryDTO countryId;
	private String stateName;
	private String status;
	
	
	public Long getStateId() {
		return stateId;
	}
	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}
	public CountryDTO getCountryId() {
		return countryId;
	}
	public void setCountryId(CountryDTO countryId) {
		this.countryId = countryId;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	
	
}
