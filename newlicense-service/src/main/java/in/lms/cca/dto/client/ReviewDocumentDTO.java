package in.lms.cca.dto.client;

public class ReviewDocumentDTO {
    private String documentId;
    private String documentName;
    private String documentType;

  
    public ReviewDocumentDTO() {}

    public ReviewDocumentDTO(String documentId, String documentName, String documentType) {
        this.documentId = documentId;
        this.documentName = documentName;
        this.documentType = documentType;
    }

   
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }
}
