package in.lms.cca.entity;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.sql.Timestamp;

import in.lms.cca.util.global.AbstractTimestampEntity;

@Entity
@Table(name = "app_undertaking", schema="new_license_management")
public class AppUndertakingEntity extends AbstractTimestampEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "app_undertaking_id",length = 11)
    private Long appUndertakingId;

    @Column(name = "undertaking_id", length = 11)
    private Long undertakingId;

    @ManyToOne
    @JoinColumn(name = "intent_app_id", referencedColumnName = "intent_app_id")
    private IntentApplication intentAppId;


    @Column(name = "created_by", length = 74)
    private String createdBy;

    @Column(name = "updated_by",length = 74)
    private String updatedBy;

   
       @Column(name="status",length = 15)
       private String status;
    // Getters and Setters
    public Long getAppUndertakingId() {
        return appUndertakingId;
    }

    public void setAppUndertakingId(Long appUndertakingId) {
        this.appUndertakingId = appUndertakingId;
    }

    public Long getUndertakingId() {
        return undertakingId;
    }

    public void setUndertakingId(Long undertakingId) {
        this.undertakingId = undertakingId;
    }

  

    public IntentApplication getIntentAppId() {
		return intentAppId;
	}

	public void setIntentAppId(IntentApplication intentAppId) {
		this.intentAppId = intentAppId;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    
}
