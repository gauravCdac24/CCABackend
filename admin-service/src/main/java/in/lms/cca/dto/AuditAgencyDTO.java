package in.lms.cca.dto;

import java.util.List;

public class AuditAgencyDTO {
private String auditAgencyId;
private String addressId;
public String getAddressId() {
	return addressId;
}
public void setAddressId(String addressId) {
	this.addressId = addressId;
}
private String auditAgencyName;
private List<AuditAgencyEmailDTO> emailId;
//private String emailId;
private List<AuditAgencyMobileDTO> phoneRecord;
private List<AuditorsDTO> auditors;
private String effectiveFrom;
private String effectiveTo;
private String blockNo;
private String village;
private String postOffice;
private String subDivision;
private String country;
private String city;
private String state;
private String pin;
private String status;
private String createdBy;
private String updaedBy;
private String created;
private String updated;


public String getAuditAgencyName() {
	return auditAgencyName;
}
public void setAuditAgencyName(String auditAgencyName) {
	this.auditAgencyName = auditAgencyName;
}
public String getAuditAgencyId() {
	return auditAgencyId;
}
public void setAuditAgencyId(String auditAgencyId) {
	this.auditAgencyId = auditAgencyId;
}

public List<AuditAgencyEmailDTO> getEmailId() {
	return emailId;
}
public void setEmailId(List<AuditAgencyEmailDTO> emailId) {
	this.emailId = emailId;
}
public List<AuditAgencyMobileDTO> getPhoneRecord() {
	return phoneRecord;
}
public void setPhoneRecord(List<AuditAgencyMobileDTO> phoneRecord) {
	this.phoneRecord = phoneRecord;
}
public List<AuditorsDTO> getAuditors() {
	return auditors;
}
public void setAuditors(List<AuditorsDTO> auditors) {
	this.auditors = auditors;
}
public String getEffectiveFrom() {
	return effectiveFrom;
}
public void setEffectiveFrom(String effectiveFrom) {
	this.effectiveFrom = effectiveFrom;
}
public String getEffectiveTo() {
	return effectiveTo;
}
public void setEffectiveTo(String effectiveTo) {
	this.effectiveTo = effectiveTo;
}
public String getBlockNo() {
	return blockNo;
}
public void setBlockNo(String blockNo) {
	this.blockNo = blockNo;
}
public String getVillage() {
	return village;
}
public void setVillage(String village) {
	this.village = village;
}
public String getPostOffice() {
	return postOffice;
}
public void setPostOffice(String postOffice) {
	this.postOffice = postOffice;
}
public String getSubDivision() {
	return subDivision;
}
public void setSubDivision(String subDivision) {
	this.subDivision = subDivision;
}
public String getCountry() {
	return country;
}
public void setCountry(String country) {
	this.country = country;
}
public String getCity() {
	return city;
}
public void setCity(String city) {
	this.city = city;
}
public String getState() {
	return state;
}
public void setState(String state) {
	this.state = state;
}
public String getPin() {
	return pin;
}
public void setPin(String pin) {
	this.pin = pin;
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
public String getUpdaedBy() {
	return updaedBy;
}
public void setUpdaedBy(String updaedBy) {
	this.updaedBy = updaedBy;
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
public AuditAgencyDTO(String auditAgencyId, String addressId, String auditAgencyName, List<AuditAgencyEmailDTO> emailId,
		List<AuditAgencyMobileDTO> phoneRecord, List<AuditorsDTO> auditors, String effectiveFrom, String effectiveTo,
		String blockNo, String village, String postOffice, String subDivision, String country, String city,
		String state, String pin, String status, String createdBy, String updaedBy, String created, String updated) {
	super();
	this.auditAgencyId = auditAgencyId;
	this.addressId = addressId;
	this.auditAgencyName = auditAgencyName;
	this.emailId = emailId;
	this.phoneRecord = phoneRecord;
	this.auditors = auditors;
	this.effectiveFrom = effectiveFrom;
	this.effectiveTo = effectiveTo;
	this.blockNo = blockNo;
	this.village = village;
	this.postOffice = postOffice;
	this.subDivision = subDivision;
	this.country = country;
	this.city = city;
	this.state = state;
	this.pin = pin;
	this.status = status;
	this.createdBy = createdBy;
	this.updaedBy = updaedBy;
	this.created = created;
	this.updated = updated;
}
public AuditAgencyDTO() {
	super();
	// TODO Auto-generated constructor stub
}
@Override
public String toString() {
	return "AuditAgencyDTO [auditAgencyId=" + auditAgencyId + ", addressId=" + addressId + ", auditAgencyName="
			+ auditAgencyName + ", emailId=" + emailId + ", phoneRecord=" + phoneRecord + ", auditors=" + auditors
			+ ", effectiveFrom=" + effectiveFrom + ", effectiveTo=" + effectiveTo + ", blockNo=" + blockNo
			+ ", village=" + village + ", postOffice=" + postOffice + ", subDivision=" + subDivision + ", country="
			+ country + ", city=" + city + ", state=" + state + ", pin=" + pin + ", status=" + status + ", createdBy="
			+ createdBy + ", updaedBy=" + updaedBy + ", created=" + created + ", updated=" + updated + "]";
}


}
