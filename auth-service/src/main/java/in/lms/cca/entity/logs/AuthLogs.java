package in.lms.cca.entity.logs;

import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "auth_logs", schema = "cca_logs_management")
public class AuthLogs extends AbstractTimestampEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "log_id", length = 11)
	private Long logId;
	
	@Column(name = "user_id", length = 8)
	private Integer userId;
	
	@Column(name = "action", length = 50)
	private String action;
	
	@Column(name = "ip_address", length = 40)
	private Integer ipAddress;

	public Long getLogId() {
		return logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Integer getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(Integer ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	
	
}
