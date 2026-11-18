package in.lms.cca.dto;

import org.springframework.web.multipart.MultipartFile;

public class CoverLetterDocDTO {

	private MultipartFile file;
	private String fileName;
	
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
		
}
