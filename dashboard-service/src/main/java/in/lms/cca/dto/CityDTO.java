package in.lms.cca.dto;


public class CityDTO {

	private Long cityId;
	private StateDTO stateId;
	private String cityName;
	private String status;
	
	public Long getCityId() {
		return cityId;
	}
	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}
	public StateDTO getStateId() {
		return stateId;
	}
	public void setStateId(StateDTO stateId) {
		this.stateId = stateId;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	
	
}
