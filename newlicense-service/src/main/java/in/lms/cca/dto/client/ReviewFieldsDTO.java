package in.lms.cca.dto.client;

public class ReviewFieldsDTO {
    private String fieldId;
    private String fieldName;
    private String fieldValue;

    // Constructors
    public ReviewFieldsDTO() {}

    public ReviewFieldsDTO(String fieldId, String fieldName, String fieldValue) {
        this.fieldId = fieldId;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

   
    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }
}
