package in.lms.cca.dto;


public class DocumentsMasterDTO {

	private String documentsId;
    private String documentsTitle;
    private boolean isMandatory;
    private String documentsFor;
    private String status;
    private String createdBy;
    private String updatedBy;
    private String created;
    private String updated;
    
    
	public String getDocumentsId() {
		return documentsId;
	}
	public void setDocumentsId(String documentsId) {
		this.documentsId = documentsId;
	}
	public String getDocumentsTitle() {
		return documentsTitle;
	}
	public void setDocumentsTitle(String documentsTitle) {
		this.documentsTitle = documentsTitle;
	}
	public boolean isMandatory() {
		return isMandatory;
	}
	public void setMandatory(boolean isMandatory) {
		this.isMandatory = isMandatory;
	}
	public String getDocumentsFor() {
		return documentsFor;
	}
	public void setDocumentsFor(String documentsFor) {
		this.documentsFor = documentsFor;
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
	public String getUpdated() {
		return updated;
	}
	public void setUpdated(String updated) {
		this.updated = updated;
	}
    
    
	
}
