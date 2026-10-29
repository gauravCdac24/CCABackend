package in.lms.cca.dto;

import org.springframework.web.multipart.MultipartFile;

public class EKYCApprovalDocDTO {

	private String id;
	private MultipartFile file;
	private String fileName;
	private String serviceProviderName;
	private String integrationDetails;
	private String integrationDate;
	private String entityName;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getServiceProviderName() {
		return serviceProviderName;
	}
	public void setServiceProviderName(String serviceProviderName) {
		this.serviceProviderName = serviceProviderName;
	}
	public String getIntegrationDetails() {
		return integrationDetails;
	}
	public void setIntegrationDetails(String integrationDetails) {
		this.integrationDetails = integrationDetails;
	}
	public String getIntegrationDate() {
		return integrationDate;
	}
	public void setIntegrationDate(String integrationDate) {
		this.integrationDate = integrationDate;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	
	
}
