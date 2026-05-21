package in.lms.cca.util.global;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;



@MappedSuperclass
public abstract class LogAbstractTimestampOperationEntity {

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created", nullable = false)
	private Date logCreated;

	@Column(name = "operation", nullable = false, length = 8)
	private String operation;

	@Column(name = "ip_address", nullable = false, length = 45)
	private String IPAddress;

	@Column(name = "user_name", nullable = false, length = 28)
	private String userName;

	public Date getLogCreated() {
		return logCreated;
	}

	@PrePersist
	protected void onCreate() {
		logCreated = new Date();
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getIPAddress() {
		return IPAddress;
	}

	public void setIPAddress(String iPAddress) {
		IPAddress = iPAddress;
	}

	public void setLogCreated(Date logCreated) {
		this.logCreated = logCreated;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
