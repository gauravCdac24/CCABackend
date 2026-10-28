package in.lms.cca.dto;


public class EsignAPISpecificationMasterDTO{

    private Long esignApiSpecId;
    private String apiSpecification;
    private String status;
    private String createdBy;
    private String updatedBy;

	public Long getEsignApiSpecId() {
		return esignApiSpecId;
	}

	public void setEsignApiSpecId(Long esignApiSpecId) {
		this.esignApiSpecId = esignApiSpecId;
	}

	public String getApiSpecification() {
		return apiSpecification;
	}

	public void setApiSpecification(String apiSpecification) {
		this.apiSpecification = apiSpecification;
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

