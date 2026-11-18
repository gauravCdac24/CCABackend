package in.lms.cca.entity;

import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "scrutiny_fields", schema = "new_license_management")
public class ScrutinyFields extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scrutiny_field_id", length = 11)
    private Long scrutinyFieldId;

    @ManyToOne
    @JoinColumn(name = "scrutiny_id", referencedColumnName = "scrutiny_id")
    private ScrutinyApplication scrutinyId;

    @Column(name = "field_name", length = 30)
    private String fieldName;

    @Column(name = "status", length = 8)
    private String status;

    @Column(name = "created_by", length = 74)
    private String createdBy;

    @Column(name = "updated_by", length = 74)
    private String updatedBy;

	public Long getScrutinyFieldId() {
		return scrutinyFieldId;
	}

	public void setScrutinyFieldId(Long scrutinyFieldId) {
		this.scrutinyFieldId = scrutinyFieldId;
	}

	public ScrutinyApplication getScrutinyId() {
		return scrutinyId;
	}

	public void setScrutinyId(ScrutinyApplication scrutinyId) {
		this.scrutinyId = scrutinyId;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
    
    
    
    
}
