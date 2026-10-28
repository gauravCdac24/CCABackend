package in.lms.cca.payload;

import org.springframework.web.multipart.MultipartFile;

public class AuditorControlsNC {
	
	private String auditorControlsNCId;
	private MultipartFile file;
	private String selectedMultipartFile;
	private String controlId;
	private String compliance;
	private String applicantUserName;
	public String getAuditorControlsNCId() {
		return auditorControlsNCId;
	}
	public void setAuditorControlsNCId(String auditorControlsNCId) {
		this.auditorControlsNCId = auditorControlsNCId;
	}
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public String getControlId() {
		return controlId;
	}
	public void setControlId(String controlId) {
		this.controlId = controlId;
	}
	public String getCompliance() {
		return compliance;
	}
	public void setCompliance(String compliance) {
		this.compliance = compliance;
	}
	public String getApplicantUserName() {
		return applicantUserName;
	}
	public void setApplicantUserName(String applicantUserName) {
		this.applicantUserName = applicantUserName;
	}
	
	public String getSelectedMultipartFile() {
		return selectedMultipartFile;
	}
	public void setSelectedMultipartFile(String selectedMultipartFile) {
		this.selectedMultipartFile = selectedMultipartFile;
	}
	@Override
	public String toString() {
		return "AuditorControlsNC [auditorControlsNCId=" + auditorControlsNCId + ", file=" + file
				+ ", selectedMultipartFile=" + selectedMultipartFile + ", controlId=" + controlId + ", compliance="
				+ compliance + ", applicantUserName=" + applicantUserName + "]";
	}
	
	
	
	

}
