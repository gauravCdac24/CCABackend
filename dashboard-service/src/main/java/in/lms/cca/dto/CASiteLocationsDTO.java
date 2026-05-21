package in.lms.cca.dto;

public class CASiteLocationsDTO {

	private String caName;
	private String primarySite;
	private String drSite;
	private String registeredOffice;
	private String otherSite;
	
	
	public CASiteLocationsDTO(String caName, String primarySite, String drSite, String registeredOffice,
			String otherSite) {
		super();
		this.caName = caName;
		this.primarySite = primarySite;
		this.drSite = drSite;
		this.registeredOffice = registeredOffice;
		this.otherSite = otherSite;
	}
	
	public String getCaName() {
		return caName;
	}
	public void setCaName(String caName) {
		this.caName = caName;
	}
	public String getPrimarySite() {
		return primarySite;
	}
	public void setPrimarySite(String primarySite) {
		this.primarySite = primarySite;
	}
	public String getDrSite() {
		return drSite;
	}
	public void setDrSite(String drSite) {
		this.drSite = drSite;
	}
	public String getRegisteredOffice() {
		return registeredOffice;
	}
	public void setRegisteredOffice(String registeredOffice) {
		this.registeredOffice = registeredOffice;
	}
	public String getOtherSite() {
		return otherSite;
	}
	public void setOtherSite(String otherSite) {
		this.otherSite = otherSite;
	}
	
	
	
}
