package in.lms.cca.dto;

import java.util.Date;

public class AuditRequestDTO {

	private Long id;
	private Integer controlId;
	private String userName;
	private String status;
	private Long auditScheduleId;
	private Date created;
	private Date updated;

	public AuditRequestDTO() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getControlId() {
		return controlId;
	}

	public void setControlId(Integer controlId) {
		this.controlId = controlId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getAuditScheduleId() {
		return auditScheduleId;
	}

	public void setAuditScheduleId(Long auditScheduleId) {
		this.auditScheduleId = auditScheduleId;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}
}
