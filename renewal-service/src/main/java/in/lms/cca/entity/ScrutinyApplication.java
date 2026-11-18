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
@Table(name = "scrutiny_application", schema = "new_license_management")
public class ScrutinyApplication extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scrutiny_id", length = 11)
    private Long scrutinyId;

    @ManyToOne
    @JoinColumn(name = "intent_app_id", referencedColumnName = "intent_app_id")
    private IntentApplication intentAppId;

    @Column(name = "remarks", length = 255)
    private String remarks;

    @Column(name = "scrutiny_by", length = 74)
    private String scrutinyBy;

    @Column(name = "status", length = 8)
    private String status;

    @Column(name = "created_by", length = 74)
    private String createdBy;

    @Column(name = "updated_by", length = 74)
    private String updatedBy;

	public Long getScrutinyId() {
		return scrutinyId;
	}

	public void setScrutinyId(Long scrutinyId) {
		this.scrutinyId = scrutinyId;
	}

	public IntentApplication getIntentAppId() {
		return intentAppId;
	}

	public void setIntentAppId(IntentApplication intentAppId) {
		this.intentAppId = intentAppId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getScrutinyBy() {
		return scrutinyBy;
	}

	public void setScrutinyBy(String scrutinyBy) {
		this.scrutinyBy = scrutinyBy;
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
