package in.lms.cca.entity.logs;

import in.lms.cca.entity.Country;
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
@Table(name = "state_logs", schema = "cca_logs_management")
public class stateLogs extends LogAbstractTimestampOperationEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "log_state_id", length = 11)
	private Long logStateId;
	
	@Column(name = "client_host_name", length = 100, nullable = false)
	private String clientHostName;
	
	@Column(name = "state_id", length = 11)
	private Long stateId;
	
	@ManyToOne
	@JoinColumn(name = "country_id", referencedColumnName = "country_id")
	private Country countryId;
	
	@Column(name = "state_name", length = 60)
	private String stateName;
	
	@Column(name = "status", length = 8)
	private String status;

	public stateLogs(Long logStateId, Long stateId, Country countryId, String stateName, String status) {
		super();
		this.logStateId = logStateId;
		this.stateId = stateId;
		this.countryId = countryId;
		this.stateName = stateName;
		this.status = status;
	}

	public stateLogs(State e,String IPAddress,String operation,String userName , String clientHostName) {
		
		this.stateId = e.getStateId();
		this.countryId = e.getCountryId();
		this.stateName = e.getStateName();
		this.status = e.getStatus();
		this.setIPAddress(IPAddress);
		this.setOperation(operation);
		this.setUserName(userName);
		this.clientHostName = clientHostName;
	}

	
	public Long getLogStateId() {
		return logStateId;
	}

	public void setLogStateId(Long logStateId) {
		this.logStateId = logStateId;
	}

	public Long getStateId() {
		return stateId;
	}

	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}

	public Country getCountryId() {
		return countryId;
	}

	public void setCountryId(Country countryId) {
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

	public stateLogs() {
		super();
		// TODO Auto-generated constructor stub
	}


}
