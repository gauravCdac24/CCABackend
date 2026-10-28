package in.lms.cca.dto;

import java.util.List;

import in.lms.cca.entity.EkycMode;
import in.lms.cca.entity.EsignAPIVersion;

public class PreviewApplicationDTO {

	private List<EkycMode> ekycModes;
	private String coverLetterId;
	private String auditReportId;
	private String cpsDocumentId;
	private EsignAPIVersion esignAPIVersion;
	private String purpose;
	
	
	public List<EkycMode> getEkycModes() {
		return ekycModes;
	}
	public void setEkycModes(List<EkycMode> ekycModes) {
		this.ekycModes = ekycModes;
	}
	public String getCoverLetterId() {
		return coverLetterId;
	}
	public void setCoverLetterId(String coverLetterId) {
		this.coverLetterId = coverLetterId;
	}
	public String getAuditReportId() {
		return auditReportId;
	}
	public void setAuditReportId(String auditReportId) {
		this.auditReportId = auditReportId;
	}
	public String getCpsDocumentId() {
		return cpsDocumentId;
	}
	public void setCpsDocumentId(String cpsDocumentId) {
		this.cpsDocumentId = cpsDocumentId;
	}
	public EsignAPIVersion getEsignAPIVersion() {
		return esignAPIVersion;
	}
	public void setEsignAPIVersion(EsignAPIVersion esignAPIVersion) {
		this.esignAPIVersion = esignAPIVersion;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	
	
	
}
