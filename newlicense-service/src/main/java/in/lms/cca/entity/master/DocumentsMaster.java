package in.lms.cca.entity.master;

import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "documents_master", schema = "new_license_management")
public class DocumentsMaster extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "documents_id", length = 11)
    private Long documentsId;

    @Column(name = "documents_title", length = 255)
    private String documentsTitle;

    @Column(name = "ismandatory")
    private boolean isMandatory;
    
    @Column(name = "documents_for", length = 16)
    private String documentsFor;

    @Column(name = "status", length = 8)
    private String status;

    @Column(name = "created_by", length = 74)
    private String createdBy;

    @Column(name = "updated_by", length = 74)
    private String updatedBy;

	public Long getDocumentsId() {
		return documentsId;
	}

	public void setDocumentsId(Long documentsId) {
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

	public String getDocumentsFor() {
		return documentsFor;
	}

	public void setDocumentsFor(String documentsFor) {
		this.documentsFor = documentsFor;
	}
    
	
    
    
}

