package in.lms.cca.dto;

public class FileDTO {

    private String fileName;
    private Object fileObject; 
   

    // Getters and Setters
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
	public Object getFileObject() {
		return fileObject;
	}
	public void setFileObject(Object fileObject) {
		this.fileObject = fileObject;
	}
	@Override
	public String toString() {
		return "FileDTO [fileName=" + fileName + ", fileObject=" + fileObject + "]";
	}
	
	    

   }
