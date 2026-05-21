package in.lms.cca.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Date;

import in.lms.cca.util.global.AbstractTimestampEntity;

@Entity
@Table(name = "esign_api_version_master", schema = "admin_user_management")
public class ESignAPIVersionMaster extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "esign_api_verid", length = 11)
    private Long esignApiVerId;

    @ManyToOne
    @JoinColumn(name = "esign_api_specid", referencedColumnName = "esign_api_specid")
    private EsignAPISpecificationMaster esignApiSpecId;

    @Column(name = "api_version", length = 100, nullable = false)
    private String apiVersion;

    @Column(name = "release_date")
    private Date releaseDate;

    @Column(name = "esp_readiness_by")
    private Date espReadinessBy;

    @Column(name = "deprecation_date")
    private Date deprecationDate;

    @Column(name = "file_name", length = 100, nullable = false)
    private String fileName;

    @Column(name = "status", length = 8, nullable = false)
    private String status;  

    @Column(name = "created_by", length = 74, nullable = false)
    private String createdBy;

    @Column(name = "updated_by", length = 74, nullable = false)
    private String updatedBy;

	public Long getEsignApiVerId() {
		return esignApiVerId;
	}

	public void setEsignApiVerId(Long esignApiVerId) {
		this.esignApiVerId = esignApiVerId;
	}

	public EsignAPISpecificationMaster getEsignApiSpecId() {
		return esignApiSpecId;
	}

	public void setEsignApiSpecId(EsignAPISpecificationMaster esignApiSpecId) {
		this.esignApiSpecId = esignApiSpecId;
	}

	public String getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public Date getEspReadinessBy() {
		return espReadinessBy;
	}

	public void setEspReadinessBy(Date espReadinessBy) {
		this.espReadinessBy = espReadinessBy;
	}

	public Date getDeprecationDate() {
		return deprecationDate;
	}

	public void setDeprecationDate(Date deprecationDate) {
		this.deprecationDate = deprecationDate;
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

    
    
    

}
