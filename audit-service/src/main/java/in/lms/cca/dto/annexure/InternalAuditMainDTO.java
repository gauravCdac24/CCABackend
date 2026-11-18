package in.lms.cca.dto.annexure;

import java.util.List;

public class InternalAuditMainDTO {

	private List<InternalAuditDetailsDTO> internalAudits;
	private String userName;
	
	public List<InternalAuditDetailsDTO> getInternalAudits() {
		return internalAudits;
	}
	public void setInternalAudits(List<InternalAuditDetailsDTO> internalAudits) {
		this.internalAudits = internalAudits;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	

	
	
	
}
