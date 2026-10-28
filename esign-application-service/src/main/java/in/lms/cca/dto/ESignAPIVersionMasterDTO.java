package in.lms.cca.dto;

import java.sql.Date;

public class ESignAPIVersionMasterDTO{

    private Long esignApiVerId;
    private EsignAPISpecificationMasterDTO esignApiSpecId;
    private String apiVersion;
    private Date releaseDate;
    private Date espReadinessBy;
    private Date deprecationDate;
    private String fileName;
    private String status;  
    private String createdBy;
    private String updatedBy;

	public Long getEsignApiVerId() {
		return esignApiVerId;
	}

	public void setEsignApiVerId(Long esignApiVerId) {
		this.esignApiVerId = esignApiVerId;
	}

	public EsignAPISpecificationMasterDTO getEsignApiSpecId() {
		return esignApiSpecId;
	}

	public void setEsignApiSpecId(EsignAPISpecificationMasterDTO esignApiSpecId) {
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
