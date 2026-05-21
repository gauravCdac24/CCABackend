package in.lms.cca.entity;

import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "document_master", schema = "admin_user_management")
public class DocumentMaster extends AbstractTimestampEntity {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "document_id", length = 2)
	    private Long documentId;

	    @Column(name = "document_name", length = 150)
	    private String documentName;

	    @Column(name = "status", length = 8)
	    private String status;  

	    @Column(name = "created_by", length = 74)
	    private String createdBy;

	    @Column(name = "updated_by", length = 74)
	    private String updatedBy;

		public Long getDocumentId() {
			return documentId;
		}

		public void setDocumentId(Long documentId) {
			this.documentId = documentId;
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

		public String getUpdatedBy() {
			return updatedBy;
		}

		public void setUpdatedBy(String updatedBy) {
			this.updatedBy = updatedBy;
		}

		@Override
		public String toString() {
			return "DocumentMaster [documentId=" + documentId + ", documentName=" + documentName + ", status=" + status
					+ ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + "]";
		}
	    
	    

}
