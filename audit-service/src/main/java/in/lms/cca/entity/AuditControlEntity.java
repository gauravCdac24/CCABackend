package in.lms.cca.entity;

import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "audit_control",schema="audit_management")
public class AuditControlEntity extends AbstractTimestampEntity{
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(name = "control_id", length = 20)
	    private Integer controlId;

	    @Column(name = "user_name", length = 25)
	    private String userName;
	    
	    @Column(name="status",length = 20)
	    private String status;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Integer getControlId() {
			return controlId;
		}

		public void setControlId(Integer controlId) {
			this.controlId = controlId;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		@Override
		public String toString() {
			return "AuditControlEntity [id=" + id + ", controlId=" + controlId + ", userName=" + userName + ", status="
					+ status + "]";
		}

		
	    
	    

}
