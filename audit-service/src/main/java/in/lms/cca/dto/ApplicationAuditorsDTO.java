package in.lms.cca.dto;

public class ApplicationAuditorsDTO {

    private String appAuditorId;
    private String appAuditId;
    private String auditorId;
    private String certificationType;
    private String reviewBy;
    private String remarks;
    private String certificateExpiry;
    private String certificateDocument;
    private String status;
    private String created;
    private String updated;
    private String createdBy;
    private String updatedBy;
	public String getAppAuditorId() {
		return appAuditorId;
	}
	public void setAppAuditorId(String appAuditorId) {
		this.appAuditorId = appAuditorId;
	}
	public String getAppAuditId() {
		return appAuditId;
	}
	public void setAppAuditId(String appAuditId) {
		this.appAuditId = appAuditId;
	}
	public String getAuditorId() {
		return auditorId;
	}
	public void setAuditorId(String auditorId) {
		this.auditorId = auditorId;
	}
	public String getCertificationType() {
		return certificationType;
	}
	public void setCertificationType(String certificationType) {
		this.certificationType = certificationType;
	}
	public String getReviewBy() {
		return reviewBy;
	}
	public void setReviewBy(String reviewBy) {
		this.reviewBy = reviewBy;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getCertificateExpiry() {
		return certificateExpiry;
	}
	public void setCertificateExpiry(String certificateExpiry) {
		this.certificateExpiry = certificateExpiry;
	}
	public String getCertificateDocument() {
		return certificateDocument;
	}
	public void setCertificateDocument(String certificateDocument) {
		this.certificateDocument = certificateDocument;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getUpdated() {
		return updated;
	}
	public void setUpdated(String updated) {
		this.updated = updated;
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
	@Override
	public String toString() {
		return "ApplicationAuditorsDTO [appAuditorId=" + appAuditorId + ", appAuditId=" + appAuditId + ", auditorId="
				+ auditorId + ", certificationType=" + certificationType + ", reviewBy=" + reviewBy + ", remarks="
				+ remarks + ", certificateExpiry=" + certificateExpiry + ", certificateDocument=" + certificateDocument
				+ ", status=" + status + ", created=" + created + ", updated=" + updated + ", createdBy=" + createdBy
				+ ", updatedBy=" + updatedBy + "]";
	}

    
}