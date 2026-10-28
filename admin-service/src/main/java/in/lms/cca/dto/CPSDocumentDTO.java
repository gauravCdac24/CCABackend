package in.lms.cca.dto;

import org.springframework.web.multipart.MultipartFile;

public class CPSDocumentDTO {
	
	private String cpsDocumentId;
	 private String version;
	    private String publishDate;
	    private MultipartFile  fileName;
	    private String documentName;
	private String status;
	private String createdBy;
	private String updaedBy;
	private String created;
	private String updated;
	public String getCpsDocumentId() {
		return cpsDocumentId;
	}
	public void setCpsDocumentId(String cpsDocumentId) {
		this.cpsDocumentId = cpsDocumentId;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}
	public MultipartFile  getFileName() {
		return fileName;
	}
	public void setFileName(MultipartFile  fileName) {
		this.fileName = fileName;
	}
	public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
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
	public String getUpdaedBy() {
		return updaedBy;
	}
	public void setUpdaedBy(String updaedBy) {
		this.updaedBy = updaedBy;
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
	
	

}
