package in.lms.cca.dto.annexure;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

public class CryptoTokenDetailsDTO  {

    private String cryptoTokDetailsId;
    private String brandName;
    private String oemDetails;
    private String makInPercentage;
    private String fipCertUpTo;
    private MultipartFile secAuditDetails;
    private String filename;
    
	public String getCryptoTokDetailsId() {
		return cryptoTokDetailsId;
	}
	public void setCryptoTokDetailsId(String cryptoTokDetailsId) {
		this.cryptoTokDetailsId = cryptoTokDetailsId;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getOemDetails() {
		return oemDetails;
	}
	public void setOemDetails(String oemDetails) {
		this.oemDetails = oemDetails;
	}
	public String getMakInPercentage() {
		return makInPercentage;
	}
	public void setMakInPercentage(String makInPercentage) {
		this.makInPercentage = makInPercentage;
	}
	public String getFipCertUpTo() {
		return fipCertUpTo;
	}
	public void setFipCertUpTo(String fipCertUpTo) {
		this.fipCertUpTo = fipCertUpTo;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public MultipartFile getSecAuditDetails() {
		return secAuditDetails;
	}
	public void setSecAuditDetails(MultipartFile secAuditDetails) {
		this.secAuditDetails = secAuditDetails;
	}
	
    

    
   
}

