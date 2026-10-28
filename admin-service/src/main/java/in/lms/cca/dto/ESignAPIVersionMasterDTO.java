package in.lms.cca.dto;

import java.util.Date;


public class ESignAPIVersionMasterDTO {

    private String esignApiVerId;
    private String esignApiSpecId;
    private String apiVersion;
    private Date releaseDate;
    private Date espReadinessBy;
    private Date deprecationDate;
    private String createdBy;
    private String updatedBy;
    
	public String getEsignApiVerId() {
		return esignApiVerId;
	}
	public void setEsignApiVerId(String esignApiVerId) {
		this.esignApiVerId = esignApiVerId;
	}
	public String getEsignApiSpecId() {
		return esignApiSpecId;
	}
	public void setEsignApiSpecId(String esignApiSpecId) {
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
