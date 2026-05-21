package in.lms.cca.entity.logs;

import in.lms.cca.entity.City;
import in.lms.cca.entity.State;
import in.lms.cca.util.global.LogAbstractTimestampOperationEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "city_logs", schema = "cca_logs_management")
public class CityLogs extends LogAbstractTimestampOperationEntity{
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "log_city_id", length = 11)
	private Long logCityId;
	
	@Column(name = "city_id", length = 11)
	private Long cityId;
	
	@ManyToOne
	@JoinColumn(name = "state_id", referencedColumnName = "state_id")
	private State stateId;
	
	@Column(name = "city_name", length = 60)
	private String cityName;
	
	@Column(name = "status", length = 8)
	private String status;
	
	@Column(name = "client_host_name", length = 100, nullable = false)
	private String clientHostName;



	public Long getLogCityId() {
		return logCityId;
	}

	public void setLogCityId(Long logCityId) {
		this.logCityId = logCityId;
	}

	public State getStateId() {
		return stateId;
	}

	public void setStateId(State stateId) {
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

	public String getClientHostName() {
		return clientHostName;
	}

	public void setClientHostName(String clientHostName) {
		this.clientHostName = clientHostName;
	}

	public CityLogs(Long logCityId, State stateId, String cityName, String status) {
		super();
		this.logCityId = logCityId;
		this.stateId = stateId;
		this.cityName = cityName;
		this.status = status;
	}
	
	public CityLogs(City e, String IPAddress, String operation, String userName, String clientHostName) {
		super();
		this.cityId = e.getCityId();
		this.stateId = e.getStateId();
		this.cityName = e.getCityName();
		this.status = e.getStatus();
		this.setIPAddress(IPAddress);
		this.setOperation(operation);
		this.setUserName(userName);
		this.clientHostName = clientHostName;
	}

	// Default constructor
	public CityLogs() {
		super();
	}

}
