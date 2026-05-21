package in.lms.cca.dto;

import java.util.Date;

import in.lms.cca.entity.AuditControl;

public class AuditControlDTO {

	
	private String auditControlId;
	private String auditParameterId;
	private String controlDesc;
	private String auditCheckId;
	private String auditControlTypeId;
	private String selfAuditControlId;
	private String validTill;
	private String effectiveDate;
	private String references;
	private String status;
	private String createdBy;
	private String updatedBy;
	private String created;
	
	
	public String getAuditControlId() {
		return auditControlId;
	}
	public void setAuditControlId(String auditControlId) {
		this.auditControlId = auditControlId;
	}
	public String getAuditParameterId() {
		return auditParameterId;
	}
	public void setAuditParameterId(String auditParameterId) {
		this.auditParameterId = auditParameterId;
	}
	public String getControlDesc() {
		return controlDesc;
	}
	public void setControlDesc(String controlDesc) {
		this.controlDesc = controlDesc;
	}
	public String getAuditCheckId() {
		return auditCheckId;
	}
	public void setAuditCheckId(String auditCheckId) {
		this.auditCheckId = auditCheckId;
	}
	public String getAuditControlTypeId() {
		return auditControlTypeId;
	}
	public void setAuditControlTypeId(String auditControlTypeId) {
		this.auditControlTypeId = auditControlTypeId;
	}
	public String getReferences() {
		return references;
	}
	public void setReferences(String references) {
		this.references = references;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getSelfAuditControlId() {
		return selfAuditControlId;
	}
	public void setSelfAuditControlId(String selfAuditControlId) {
		this.selfAuditControlId = selfAuditControlId;
	}
	public String getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public String getValidTill() {
		return validTill;
	}
	public void setValidTill(String validTill) {
		this.validTill = validTill;
	}
	
	
	@Override
	public String toString() {
		return "AuditControlDTO [auditControlId=" + auditControlId + ", auditParameterId=" + auditParameterId
				+ ", controlDesc=" + controlDesc + ", auditCheckId=" + auditCheckId + ", auditControlTypeId="
				+ auditControlTypeId + ", selfAuditControlId=" + selfAuditControlId + ", validTill=" + validTill
				+ ", effectiveDate=" + effectiveDate + ", references=" + references + ", status=" + status
				+ ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + ", created=" + created + "]";
	}
	
}
