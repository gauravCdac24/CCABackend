package in.lms.cca.entity;

import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "esign_doc_type_master", schema = "admin_user_management")
public class EsignDocTypeMaster extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "esign_doc_typeid", length = 2)
    private Integer esignDocTypeId;

    @Column(name = "doc_type", length = 16, nullable = false)
    private String docType;

    @Column(name = "status", length = 8, nullable = false)
    private String status;  

    @Column(name = "created_by", length = 74, nullable = false)
    private String createdBy;

    @Column(name = "updated_by", length = 74, nullable = false)
    private String updatedBy;

	public Integer getEsignDocTypeId() {
		return esignDocTypeId;
	}

	public void setEsignDocTypeId(Integer esignDocTypeId) {
		this.esignDocTypeId = esignDocTypeId;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
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

    
    

}
