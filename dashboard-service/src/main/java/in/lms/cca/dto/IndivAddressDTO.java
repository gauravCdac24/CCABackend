package in.lms.cca.dto;

public class IndivAddressDTO {
	
	private Long IndivAddressId;
	private AddressDTO residential;
	private AddressDTO official;
	private String communicationAddress;
	private String status;
	private String createdBy;
	private String updaedBy;
	private String created;
	private String updated;
	private String userName; 
	
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public AddressDTO getResidential() {
		return residential;
	}
	public void setResidential(AddressDTO residential) {
		this.residential = residential;
	}
	public AddressDTO getOfficial() {
		return official;
	}
	public void setOfficial(AddressDTO official) {
		this.official = official;
	}
	public String getCommunicationAddress() {
		return communicationAddress;
	}
	public void setCommunicationAddress(String communicationAddress) {
		this.communicationAddress = communicationAddress;
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
	public Long getIndivAddressId() {
		return IndivAddressId;
	}
	public void setIndivAddressId(Long indivAddressId) {
		IndivAddressId = indivAddressId;
	}
		
}
