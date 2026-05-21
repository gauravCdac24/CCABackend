package in.lms.cca.entity;

import in.lms.cca.util.golbal.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cessation_app_undertaking", schema="cessation_management")
public class CessationAppUndertakingEntity extends AbstractTimestampEntity{
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name="app_undertaking_id", length = 74)
	    private Long appUndertakingId;

	    @Column(name="undertaking_id", length = 74)
	    private String undertakingId;

	    @Column(name="file_name", length = 100)
	    private String filename;

	    @Column(name="status", length = 15)
	    private String status; 
	    
	    @Column(name="undertaking_key", length = 30)
	    private String undertakingKey; 
	    
	    
	    @Column(name="cessation_app_id",length = 11)
	    private Long cessationAppId;

	    @Column(name="created_by", length = 74)
	    private String createdBy; 

	    @Column(name="updated_by",length = 74)
	    private String updatedBy;

		public Long getAppUndertakingId() {
			return appUndertakingId;
		}

		public void setAppUndertakingId(Long appUndertakingId) {
			this.appUndertakingId = appUndertakingId;
		}

		public String getUndertakingId() {
			return undertakingId;
		}

		public void setUndertakingId(String undertakingId) {
			this.undertakingId = undertakingId;
		}

		public String getFilename() {
			return filename;
		}

		public void setFilename(String filename) {
			this.filename = filename;
		}

		public Long getCessationAppId() {
			return cessationAppId;
		}

		public void setCessationAppId(Long cessationAppId) {
			this.cessationAppId = cessationAppId;
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
		
		

		public String getUndertakingKey() {
			return undertakingKey;
		}

		public void setUndertakingKey(String undertakingKey) {
			this.undertakingKey = undertakingKey;
		}

		@Override
		public String toString() {
			return "CessationAppUndertakingEntity [appUndertakingId=" + appUndertakingId + ", undertakingId="
					+ undertakingId + ", filename=" + filename + ", status=" + status + ", undertakingKey="
					+ undertakingKey + ", cessationAppId=" + cessationAppId + ", createdBy=" + createdBy
					+ ", updatedBy=" + updatedBy + "]";
		}

		
		
	    
	    

}
