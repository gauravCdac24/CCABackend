package in.lms.cca.dto;

public class FilesDTO {

    private FileDTO passportDocument;
    private FileDTO idCardDocument;
    private FileDTO panCardDocument;

    // Getters and Setters
    public FileDTO getPassportDocument() { return passportDocument; }
    public void setPassportDocument(FileDTO passportDocument) { this.passportDocument = passportDocument; }

    public FileDTO getIdCardDocument() { return idCardDocument; }
    public void setIdCardDocument(FileDTO idCardDocument) { this.idCardDocument = idCardDocument; }

    public FileDTO getPanCardDocument() { return panCardDocument; }
    public void setPanCardDocument(FileDTO panCardDocument) { this.panCardDocument = panCardDocument; }
}
