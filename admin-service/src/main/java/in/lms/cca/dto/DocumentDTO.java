package in.lms.cca.dto;

public class DocumentDTO {
	
	private String documentId;
	private String documentName;
	private String createdBy;
	private String updatedBy;
	public String getDocumentId() {
		return documentId;
	}
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
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
		return "DocumentDTO [documentId=" + documentId + ", documentName=" + documentName + ", createdBy=" + createdBy
				+ ", updatedBy=" + updatedBy + "]";
	}
	
	
	

}
