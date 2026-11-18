package in.lms.cca.entity;


import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "cps_document", schema="new_license_management")
public class CPSDocument extends AbstractTimestampEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cps_doc_id", length = 11)
    private Long cpsDocId;

    @Column(name = "cps_file_name", length = 30)
    private String cpsFileName;

    @Column(name = "status", length = 8)
    private String status;

    @Column(name = "created_by", length = 74)
    private String createdBy;

    @Column(name = "updated_by", length = 74)
    private String updatedBy;

	public Long getCpsDocId() {
		return cpsDocId;
	}

	public void setCpsDocId(Long cpsDocId) {
		this.cpsDocId = cpsDocId;
	}

	public String getCpsFileName() {
		return cpsFileName;
	}

	public void setCpsFileName(String cpsFileName) {
		this.cpsFileName = cpsFileName;
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
