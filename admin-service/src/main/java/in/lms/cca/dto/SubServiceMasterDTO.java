package in.lms.cca.dto;

public class SubServiceMasterDTO {
	
    private String subServiceId;
	private String serviceId;
    private String subServiceName;
    private boolean IsMandatory;
    private String status;
    private String createdBy;
    private String updatedBy;
    private String created;
    private String updated;
	public String getSubServiceId() {
		return subServiceId;
	}
	public void setSubServiceId(String subServiceId) {
		this.subServiceId = subServiceId;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getSubServiceName() {
		return subServiceName;
	}
	public void setSubServiceName(String subServiceName) {
		this.subServiceName = subServiceName;
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
    
	public boolean isIsMandatory() {
		return IsMandatory;
	}
	public void setIsMandatory(boolean isMandatory) {
		IsMandatory = isMandatory;
	}
	
    

}
