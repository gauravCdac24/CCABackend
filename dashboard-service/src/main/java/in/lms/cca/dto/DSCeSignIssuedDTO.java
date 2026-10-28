package in.lms.cca.dto;

import java.util.List;

public class DSCeSignIssuedDTO {

    private String caUsername;
    private List<DataIssuedDTO> dscesignList;
    private String month;
    private String year;
    private List<LdifDocumentDTO> ldifFile;
	
	public String getCaUsername() {
		return caUsername;
	}
	public void setCaUsername(String caUsername) {
		this.caUsername = caUsername;
	}
	public List<DataIssuedDTO> getDscesignList() {
		return dscesignList;
	}
	public void setDscesignList(List<DataIssuedDTO> dscesignList) {
		this.dscesignList = dscesignList;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public List<LdifDocumentDTO> getLdifFile() {
		return ldifFile;
	}
	public void setLdifFile(List<LdifDocumentDTO> ldifFile) {
		this.ldifFile = ldifFile;
	}
    
    
    

    
    
}
