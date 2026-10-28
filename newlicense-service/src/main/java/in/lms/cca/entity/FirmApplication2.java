package in.lms.cca.entity;

import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "firm_application2", schema = "new_license_management")
public class FirmApplication2 extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "firm_application_id", length = 11)
    private Long firmApplicationId;

    @Column(name = "pan", length = 10)
    private String pan;

    @Column(name = "firm_turnover", length = 16)
    private String firmTurnover;

    @Column(name = "firm_net_worth", length = 16)
    private String firmNetWorth;

    @Column(name = "paid_up_capital", length = 16)
    private String paidUpCapital;

    @Column(name = "insurance_policy_no", length = 16)
    private String insurancePolicyNo;

    @Column(name = "insurance_company", length = 75)
    private String insuranceCompany;

   @ManyToOne
   @JoinColumn(name = "intent_app_id", referencedColumnName = "intent_app_id")
   private IntentApplication intentAppId;

    @Column(name = "status", length = 8)
    private String status;

    @Column(name = "created_by", length = 74)
    private String createdBy;

    @Column(name = "updated_by", length = 74)
    private String updatedBy;

	public Long getFirmApplicationId() {
		return firmApplicationId;
	}

	public void setFirmApplicationId(Long firmApplicationId) {
		this.firmApplicationId = firmApplicationId;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getFirmTurnover() {
		return firmTurnover;
	}

	public void setFirmTurnover(String firmTurnover) {
		this.firmTurnover = firmTurnover;
	}

	public String getFirmNetWorth() {
		return firmNetWorth;
	}

	public void setFirmNetWorth(String firmNetWorth) {
		this.firmNetWorth = firmNetWorth;
	}

	

	public String getPaidUpCapital() {
		return paidUpCapital;
	}

	public void setPaidUpCapital(String paidUpCapital) {
		this.paidUpCapital = paidUpCapital;
	}


	public String getInsurancePolicyNo() {
		return insurancePolicyNo;
	}

	public void setInsurancePolicyNo(String insurancePolicyNo) {
		this.insurancePolicyNo = insurancePolicyNo;
	}

	public String getInsuranceCompany() {
		return insuranceCompany;
	}

	public void setInsuranceCompany(String insuranceCompany) {
		this.insuranceCompany = insuranceCompany;
	}

	public IntentApplication getIntentAppId() {
		return intentAppId;
	}

	public void setIntentAppId(IntentApplication intentAppId) {
		this.intentAppId = intentAppId;
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
