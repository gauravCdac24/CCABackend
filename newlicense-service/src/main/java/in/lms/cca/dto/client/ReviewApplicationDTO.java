package in.lms.cca.dto.client;

import java.util.List;

public class ReviewApplicationDTO {
    private String reviewId;
    private List<ReviewDocumentDTO> reviewDocuments;
    private List<ReviewFieldsDTO> reviewFields;

    public ReviewApplicationDTO() {}

    public ReviewApplicationDTO(String reviewId, List<ReviewDocumentDTO> reviewDocuments, List<ReviewFieldsDTO> reviewFields) {
        this.reviewId = reviewId;
        this.reviewDocuments = reviewDocuments;
        this.reviewFields = reviewFields;
    }

    
    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public List<ReviewDocumentDTO> getReviewDocuments() {
        return reviewDocuments;
    }

    public void setReviewDocuments(List<ReviewDocumentDTO> reviewDocuments) {
        this.reviewDocuments = reviewDocuments;
    }

    public List<ReviewFieldsDTO> getReviewFields() {
        return reviewFields;
    }

    public void setReviewFields(List<ReviewFieldsDTO> reviewFields) {
        this.reviewFields = reviewFields;
    }
}
