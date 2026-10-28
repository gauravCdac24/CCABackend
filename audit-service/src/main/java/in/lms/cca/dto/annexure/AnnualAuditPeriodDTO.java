package in.lms.cca.dto.annexure;

import java.util.List;

public class AnnualAuditPeriodDTO {

	private List<AnnualAuditPeriodDetailsDTO> auditPeriods;
	private String userName;
	
	
	public List<AnnualAuditPeriodDetailsDTO> getAuditPeriods() {
		return auditPeriods;
	}
	public void setAuditPeriods(List<AnnualAuditPeriodDetailsDTO> auditPeriods) {
		this.auditPeriods = auditPeriods;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	
}
