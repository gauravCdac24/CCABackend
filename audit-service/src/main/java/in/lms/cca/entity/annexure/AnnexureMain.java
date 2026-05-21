package in.lms.cca.entity.annexure;

import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "annexure_main", schema="audit_management")
public class AnnexureMain extends AbstractTimestampEntity{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "annexure_main_id",length = 11)
	private Long annexureMainId;
	
	@Column(name="username", length = 50)
	private String userName;
	
	@Column(name="completed", length = 50)
	private Integer completed = 0;
	
	@Column(name="auditor_tracker", length = 50)
	private Integer auditorTracker = 0;

	@Column(name="status", length = 8)
	private String status;
	
	@Column(name = "created_by", length = 74)
    private String createdBy;

    @Column(name = "updated_by", length = 74)
    private String updatedBy;

    
	

	public Long getAnnexureMainId() {
		return annexureMainId;
	}

	public void setAnnexureMainId(Long annexureMainId) {
		this.annexureMainId = annexureMainId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getCompleted() {
		return completed;
	}

	public void setCompleted(Integer completed) {
		this.completed = completed;
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

	public Integer getAuditorTracker() {
		return auditorTracker;
	}

	public void setAuditorTracker(Integer auditorTracker) {
		this.auditorTracker = auditorTracker;
	}
    
    

    
	
}
