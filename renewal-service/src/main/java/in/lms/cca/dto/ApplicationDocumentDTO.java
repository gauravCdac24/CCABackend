package in.lms.cca.dto;

public class ApplicationDocumentDTO {
	


	    private Long appDocId;
	    private String documentTitle;
	    private Long intentAppId;
	    private String fileName;
	    private String createdBy;
	    private String updatedBy;
	    private String created;
	    private String updated;
	    private String status;
	    // Constructors
	    public ApplicationDocumentDTO() {}

	  

	    @Override
		public String toString() {
			return "ApplicationDocumentDTO [appDocId=" + appDocId + ", documentTitle=" + documentTitle
					+ ", intentAppId=" + intentAppId + ", fileName=" + fileName + ", status=" + status + ", createdBy="
					+ createdBy + ", updatedBy=" + updatedBy + ", created=" + created + ", updated=" + updated + "]";
		}



		public ApplicationDocumentDTO(Long appDocId, String documentTitle, Long intentAppId, String fileName,
				String status, String createdBy, String updatedBy, String created, String updated) {
			super();
			this.appDocId = appDocId;
			this.documentTitle = documentTitle;
			this.intentAppId = intentAppId;
			this.fileName = fileName;
			this.status = status;
			this.createdBy = createdBy;
			this.updatedBy = updatedBy;
			this.created = created;
			this.updated = updated;
		}



		// Getters and Setters
	    public Long getAppDocId() {
	        return appDocId;
	    }

	    public void setAppDocId(Long appDocId) {
	        this.appDocId = appDocId;
	    }

	    public String getDocumentTitle() {
	        return documentTitle;
	    }

	    public void setDocumentTitle(String documentTitle) {
	        this.documentTitle = documentTitle;
	    }

	    public Long getIntentAppId() {
	        return intentAppId;
	    }

	    public void setIntentAppId(Long intentAppId) {
	        this.intentAppId = intentAppId;
	    }

	    public String getFileName() {
	        return fileName;
	    }

	    public void setFileName(String fileName) {
	        this.fileName = fileName;
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
