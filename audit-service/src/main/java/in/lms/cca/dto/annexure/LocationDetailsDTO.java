package in.lms.cca.dto.annexure;

import org.springframework.web.multipart.MultipartFile;

public class LocationDetailsDTO {

    private String locationDetailsId;
    private String description;
    private String location;
    private MultipartFile caAdministratorCount;
    private MultipartFile sysAdministratorCount;
    private MultipartFile caOperatorsCount;
    private MultipartFile verificationOfficersCount;
    private MultipartFile caManpowerCount;
    private String caAdministratorFileName;
    private String sysAdministratorFileName;
    private String caOperatorsFileName;
    private String verificationOfficersFileName;
    private String caManpowerFileName;
    
    
	public String getLocationDetailsId() {
		return locationDetailsId;
	}
	public void setLocationDetailsId(String locationDetailsId) {
		this.locationDetailsId = locationDetailsId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public MultipartFile getCaAdministratorCount() {
		return caAdministratorCount;
	}
	public void setCaAdministratorCount(MultipartFile caAdministratorCount) {
		this.caAdministratorCount = caAdministratorCount;
	}
	public MultipartFile getSysAdministratorCount() {
		return sysAdministratorCount;
	}
	public void setSysAdministratorCount(MultipartFile sysAdministratorCount) {
		this.sysAdministratorCount = sysAdministratorCount;
	}
	public MultipartFile getCaOperatorsCount() {
		return caOperatorsCount;
	}
	public void setCaOperatorsCount(MultipartFile caOperatorsCount) {
		this.caOperatorsCount = caOperatorsCount;
	}
	public MultipartFile getVerificationOfficersCount() {
		return verificationOfficersCount;
	}
	public void setVerificationOfficersCount(MultipartFile verificationOfficersCount) {
		this.verificationOfficersCount = verificationOfficersCount;
	}
	public MultipartFile getCaManpowerCount() {
		return caManpowerCount;
	}
	public void setCaManpowerCount(MultipartFile caManpowerCount) {
		this.caManpowerCount = caManpowerCount;
	}
	public String getCaAdministratorFileName() {
		return caAdministratorFileName;
	}
	public void setCaAdministratorFileName(String caAdministratorFileName) {
		this.caAdministratorFileName = caAdministratorFileName;
	}
	public String getSysAdministratorFileName() {
		return sysAdministratorFileName;
	}
	public void setSysAdministratorFileName(String sysAdministratorFileName) {
		this.sysAdministratorFileName = sysAdministratorFileName;
	}
	public String getCaOperatorsFileName() {
		return caOperatorsFileName;
	}
	public void setCaOperatorsFileName(String caOperatorsFileName) {
		this.caOperatorsFileName = caOperatorsFileName;
	}
	public String getVerificationOfficersFileName() {
		return verificationOfficersFileName;
	}
	public void setVerificationOfficersFileName(String verificationOfficersFileName) {
		this.verificationOfficersFileName = verificationOfficersFileName;
	}
	public String getCaManpowerFileName() {
		return caManpowerFileName;
	}
	public void setCaManpowerFileName(String caManpowerFileName) {
		this.caManpowerFileName = caManpowerFileName;
	}
    
    
	
    
	
    
    
    
    
}
