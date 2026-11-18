package in.lms.cca.dto;

import java.util.List;

public class StepTwoDTO {

	private String userName;
	private CoverLetterDocDTO coverLetterDoc;
	private List<EKYCApprovalDocDTO> eKYCApprovalDoc;
	
	
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public CoverLetterDocDTO getCoverLetterDoc() {
		return coverLetterDoc;
	}
	public void setCoverLetterDoc(CoverLetterDocDTO coverLetterDoc) {
		this.coverLetterDoc = coverLetterDoc;
	}
	public List<EKYCApprovalDocDTO> geteKYCApprovalDoc() {
		return eKYCApprovalDoc;
	}
	public void seteKYCApprovalDoc(List<EKYCApprovalDocDTO> eKYCApprovalDoc) {
		this.eKYCApprovalDoc = eKYCApprovalDoc;
	}
	
	
	
	
	
}
