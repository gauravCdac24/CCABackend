package in.lms.cca.dto;

public class StepThreeResponseDTO {

	private String esignApiSpecId;
	private String apiVersionId;
	private EsignDocumentResponseDTO auditReport;
	private EsignDocumentResponseDTO cps;
	
	public String getEsignApiSpecId() {
		return esignApiSpecId;
	}
	public void setEsignApiSpecId(String esignApiSpecId) {
		this.esignApiSpecId = esignApiSpecId;
	}
	public String getApiVersionId() {
		return apiVersionId;
	}
	public void setApiVersionId(String apiVersionId) {
		this.apiVersionId = apiVersionId;
	}
	public EsignDocumentResponseDTO getAuditReport() {
		return auditReport;
	}
	public void setAuditReport(EsignDocumentResponseDTO auditReport) {
		this.auditReport = auditReport;
	}
	public EsignDocumentResponseDTO getCps() {
		return cps;
	}
	public void setCps(EsignDocumentResponseDTO cps) {
		this.cps = cps;
	}
	
	
	
}
