package in.lms.cca.dto.annexure;

import java.util.Date;


public class CaSwWebDetailsDTO {

    private String caWebDetailsId;
    private String description;
    private String developedBy;
    private String databaseUsed;
    private String certification;
    private Date lastSecurityAudit;
    
    
	public String getCaWebDetailsId() {
		return caWebDetailsId;
	}
	public void setCaWebDetailsId(String caWebDetailsId) {
		this.caWebDetailsId = caWebDetailsId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDevelopedBy() {
		return developedBy;
	}
	public void setDevelopedBy(String developedBy) {
		this.developedBy = developedBy;
	}
	public String getDatabaseUsed() {
		return databaseUsed;
	}
	public void setDatabaseUsed(String databaseUsed) {
		this.databaseUsed = databaseUsed;
	}
	public String getCertification() {
		return certification;
	}
	public void setCertification(String certification) {
		this.certification = certification;
	}
	public Date getLastSecurityAudit() {
		return lastSecurityAudit;
	}
	public void setLastSecurityAudit(Date lastSecurityAudit) {
		this.lastSecurityAudit = lastSecurityAudit;
	}
 
    
    
}
