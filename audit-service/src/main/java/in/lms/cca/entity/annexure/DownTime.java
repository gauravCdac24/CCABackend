package in.lms.cca.entity.annexure;

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
@Table(name = "down_time", schema="audit_management")
public class DownTime extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "down_time_id", length = 11)
    private Long downTimeId;
    
    @ManyToOne
    @JoinColumn(name = "annexure_main_id", referencedColumnName = "annexure_main_id")
	private AnnexureMain annexureMainId;
 
    @Column(name = "auditor_verification", length = 250)
    private String auditorVerification;
    
    @Column(name = "reason_and_measures_taken", columnDefinition = "TEXT")
    private String reasonAndMeasuresTaken;
    
    @Column(name = "down_time_hour", length = 4)
    private Integer downTimeHour;
    
    @Column(name = "down_time_minute", length = 2)
    private Integer downTimeMinute;
    
    @Column(name = "down_time_second", length = 2)
    private Integer downTimeSecond;

	public Long getDownTimeId() {
		return downTimeId;
	}

	public void setDownTimeId(Long downTimeId) {
		this.downTimeId = downTimeId;
	}

	public AnnexureMain getAnnexureMainId() {
		return annexureMainId;
	}

	public void setAnnexureMainId(AnnexureMain annexureMainId) {
		this.annexureMainId = annexureMainId;
	}

	public String getAuditorVerification() {
		return auditorVerification;
	}

	public void setAuditorVerification(String auditorVerification) {
		this.auditorVerification = auditorVerification;
	}

	public String getReasonAndMeasuresTaken() {
		return reasonAndMeasuresTaken;
	}

	public void setReasonAndMeasuresTaken(String reasonAndMeasuresTaken) {
		this.reasonAndMeasuresTaken = reasonAndMeasuresTaken;
	}

	public Integer getDownTimeHour() {
		return downTimeHour;
	}

	public void setDownTimeHour(Integer downTimeHour) {
		this.downTimeHour = downTimeHour;
	}

	public Integer getDownTimeMinute() {
		return downTimeMinute;
	}

	public void setDownTimeMinute(Integer downTimeMinute) {
		this.downTimeMinute = downTimeMinute;
	}

	public Integer getDownTimeSecond() {
		return downTimeSecond;
	}

	public void setDownTimeSecond(Integer downTimeSecond) {
		this.downTimeSecond = downTimeSecond;
	}


    
        
    
}
