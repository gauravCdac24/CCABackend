package in.lms.cca.dto.annexure;


public class CertificateCostDTO {
	
    private String certCostId;
    private String avgDscIssuMaintenanceCost;
    private String avgFeeChargedByCA;
    private String explanationForCostMismatch;
    private String userName;
    
    
	public String getCertCostId() {
		return certCostId;
	}
	public void setCertCostId(String certCostId) {
		this.certCostId = certCostId;
	}
	public String getAvgDscIssuMaintenanceCost() {
		return avgDscIssuMaintenanceCost;
	}
	public void setAvgDscIssuMaintenanceCost(String avgDscIssuMaintenanceCost) {
		this.avgDscIssuMaintenanceCost = avgDscIssuMaintenanceCost;
	}
	public String getAvgFeeChargedByCA() {
		return avgFeeChargedByCA;
	}
	public void setAvgFeeChargedByCA(String avgFeeChargedByCA) {
		this.avgFeeChargedByCA = avgFeeChargedByCA;
	}
	public String getExplanationForCostMismatch() {
		return explanationForCostMismatch;
	}
	public void setExplanationForCostMismatch(String explanationForCostMismatch) {
		this.explanationForCostMismatch = explanationForCostMismatch;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
    
    
    
}

