package in.lms.cca.dto.annexure;

import org.springframework.web.multipart.MultipartFile;

public class CAServicesDetailsDTO {

    private String caServicesDetailsId;
    private String description;
    private String internalOnly;
    private String externalOnly;
    private String aspOrgCount;
    private MultipartFile aspOrgCountFile;
    private String fileName;
    
    
	public String getCaServicesDetailsId() {
		return caServicesDetailsId;
	}
	public void setCaServicesDetailsId(String caServicesDetailsId) {
		this.caServicesDetailsId = caServicesDetailsId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getInternalOnly() {
		return internalOnly;
	}
	public void setInternalOnly(String internalOnly) {
		this.internalOnly = internalOnly;
	}
	public String getExternalOnly() {
		return externalOnly;
	}
	public void setExternalOnly(String externalOnly) {
		this.externalOnly = externalOnly;
	}
	public String getAspOrgCount() {
		return aspOrgCount;
	}
	public void setAspOrgCount(String aspOrgCount) {
		this.aspOrgCount = aspOrgCount;
	}
	public MultipartFile getAspOrgCountFile() {
		return aspOrgCountFile;
	}
	public void setAspOrgCountFile(MultipartFile aspOrgCountFile) {
		this.aspOrgCountFile = aspOrgCountFile;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
    
    
	
    
	
    
    
}
