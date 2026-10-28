package in.lms.cca.payload;

public class AuditReportDocumentPayload {
private Long criteriaDocId;
private Long appAuditId; 
private String document;
private String createdBy;
private String updatedBy;
private String remarks;
private String status;
private String applicantUserName;
private String intentId;
// Getters and Setters
public Long getCriteriaDocId() {
    return criteriaDocId;
}

public void setCriteriaDocId(Long criteriaDocId) {
    this.criteriaDocId = criteriaDocId;
}

public Long getAppAuditId() {
    return appAuditId;
}

public void setAppAuditId(Long appAuditId) {
    this.appAuditId = appAuditId;
}

public String getDocument() {
    return document;
}

public void setDocument(String document) {
    this.document = document;
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

public String getRemarks() {
    return remarks;
}

public void setRemarks(String remarks) {
    this.remarks = remarks;
}

public String getStatus() {
    return status;
}

public void setStatus(String status) {
    this.status = status;
}

public String getApplicantUserName() {
	return applicantUserName;
}

public void setApplicantUserName(String applicantUserName) {
	this.applicantUserName = applicantUserName;
}

public String getIntentId() {
	return intentId;
}

public void setIntentId(String intentId) {
	this.intentId = intentId;
}

@Override
public String toString() {
	return "AuditReportDocumentPayload [criteriaDocId=" + criteriaDocId + ", appAuditId=" + appAuditId + ", document="
			+ document + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + ", remarks=" + remarks + ", status="
			+ status + ", applicantUserName=" + applicantUserName + ", intentId=" + intentId + "]";
}

}