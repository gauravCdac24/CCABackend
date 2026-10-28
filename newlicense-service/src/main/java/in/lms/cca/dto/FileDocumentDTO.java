package in.lms.cca.dto;

import org.springframework.web.multipart.MultipartFile;

public class FileDocumentDTO {

    private String fileName;          // Name of the file
    private String fileType;          // Type of the file (e.g., pdf, jpg)
    private long fileSize;            // Size of the file in bytes
    private MultipartFile file;       // File content (if uploaded as MultipartFile)

    // Constructor with no arguments (required for deserialization)
    public FileDocumentDTO() {}

    // Constructor with arguments
    public FileDocumentDTO(String fileName, String fileType, long fileSize, MultipartFile file) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.file = file;
    }

    // Getters and Setters
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "FileDocumentDTO{" +
                "fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileSize=" + fileSize +
                '}';
    }
}
