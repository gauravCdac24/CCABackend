package in.lms.cca.dto.annexure;

import org.springframework.web.multipart.MultipartFile;

public class ASPDetailsDTO{

    private String aspDetailsId;
    private MultipartFile aspCount;
    private MultipartFile aspsAuditOverdueCount;
    private String aspCountFileName;
    private String aspsAuditOverdueCountFileName;
    private String userName;
    
    
	public String getAspDetailsId() {
		return aspDetailsId;
	}
	public void setAspDetailsId(String aspDetailsId) {
		this.aspDetailsId = aspDetailsId;
	}
	public MultipartFile getAspCount() {
		return aspCount;
	}
	public void setAspCount(MultipartFile aspCount) {
		this.aspCount = aspCount;
	}
	public MultipartFile getAspsAuditOverdueCount() {
		return aspsAuditOverdueCount;
	}
	public void setAspsAuditOverdueCount(MultipartFile aspsAuditOverdueCount) {
		this.aspsAuditOverdueCount = aspsAuditOverdueCount;
	}
	public String getAspCountFileName() {
		return aspCountFileName;
	}
	public void setAspCountFileName(String aspCountFileName) {
		this.aspCountFileName = aspCountFileName;
	}
	public String getAspsAuditOverdueCountFileName() {
		return aspsAuditOverdueCountFileName;
	}
	public void setAspsAuditOverdueCountFileName(String aspsAuditOverdueCountFileName) {
		this.aspsAuditOverdueCountFileName = aspsAuditOverdueCountFileName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
    
    
	
    
    
    
}
