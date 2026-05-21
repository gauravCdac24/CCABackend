package in.lms.cca.entity;


import in.lms.cca.util.golbal.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "application_timeline", schema="esign_application_management")
public class ApplicationTimeLine extends AbstractTimestampEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "app_timeline_id", length = 11)
	private Long appTimelineId;
	
	@ManyToOne
	@JoinColumn(name = "esign_licensee_appid", referencedColumnName = "esign_licensee_appid", nullable = false)
	private EsignLicenseeApplication esignLicenseeAppId;
	
	@Column(name = "application_status", length = 100, nullable = false)
	private String applicationStatus;
	
	@Column(name = "initiated_by", length = 74, nullable = false)
	private String initiatedBy;
	
	

	public Long getAppTimelineId() {
		return appTimelineId;
	}

	public void setAppTimelineId(Long appTimelineId) {
		this.appTimelineId = appTimelineId;
	}

	public EsignLicenseeApplication getEsignLicenseeAppId() {
		return esignLicenseeAppId;
	}

	public void setEsignLicenseeAppId(EsignLicenseeApplication esignLicenseeAppId) {
		this.esignLicenseeAppId = esignLicenseeAppId;
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

	
	