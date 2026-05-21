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
@Table(name = "application_timeline", schema="new_license_management")
public class ApplicationTimeLine extends AbstractTimestampEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "app_timeline_id", length = 11)
	private Long appTimelineId;
	
	@ManyToOne
	@JoinColumn(name = "intent_app_id", referencedColumnName = "intent_app_id")
	private IntentApplication intentAppId;
	
	@Column(name = "application_status", length = 100)
	private String applicationStatus;
	
	@Column(name = "initiated_by", length = 74)
	private String initiatedBy;

	public Long getAppTimelineId() {
		return appTimelineId;
	}

	public void setAppTimelineId(Long appTimelineId) {
		this.appTimelineId = appTimelineId;
	}

	public IntentApplication getIntentAppId() {
		return intentAppId;
	}

	public void setIntentAppId(IntentApplication intentAppId) {
		this.intentAppId = intentAppId;
	}

	public String getApplicationStatus() {
		return applicationStatus;
	}

	public void setApplicationStatus(String applicationStatus) {
		this.applicationStatus = applicationStatus;
	}

	public String getInitiatedBy() {
		return initiatedBy;
	}

	public void setInitiatedBy(String initiatedBy) {
		this.initiatedBy = initiatedBy;
	}
	
	

}

	
	