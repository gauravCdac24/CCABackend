package in.lms.cca.dto;

public class AdditionaldocumentDTO {
	

	private String additionalDocumentId;
	private String appTypeMasterId;
	private String additionalDocument;
	private String status;
	private String created;
	private String updated;
	public String getAdditionalDocumentId() {
		return additionalDocumentId;
	}
	public void setAdditionalDocumentId(String additionalDocumentId) {
		this.additionalDocumentId = additionalDocumentId;
	}
	public String getAppTypeMasterId() {
		return appTypeMasterId;
	}
	public void setAppTypeMasterId(String appTypeMasterId) {
		this.appTypeMasterId = appTypeMasterId;
	}
	public String getAdditionalDocument() {
		return additionalDocument;
	}
	public void setAdditionalDocument(String additionalDocument) {
		this.additionalDocument = additionalDocument;
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
	@Override
	public String toString() {
		return "AdditionaldocumentDTO [additionalDocumentId=" + additionalDocumentId + ", appTypeMasterId="
				+ appTypeMasterId + ", additionalDocument=" + additionalDocument + ", status=" + status + ", created="
				+ created + ", updated=" + updated + "]";
	}
	
	
	

}
