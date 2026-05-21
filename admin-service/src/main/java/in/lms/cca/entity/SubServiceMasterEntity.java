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
@Table(name = "sub_service_master", schema = "admin_user_management")
public class SubServiceMasterEntity extends AbstractTimestampEntity {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "sub_service_id", length = 11)
	    private Long subServiceId;
	    @ManyToOne
		@JoinColumn(name = "service_id", referencedColumnName = "service_id")
		private ServiceMaster serviceId;
	    @Column(name = "sub_service_name", length = 17)
	    private String subServiceName;
	    @Column(name="is_mandatory")
	    private boolean IsMandatory;
	    @Column(name = "status", length = 11)
	    private String status;
	    @Column(name = "created_by", length = 11)
	    private String createdBy;
	    @Column(name = "update_by", length = 11)
	    private String updatedBy;
	  
		public Long getSubServiceId() {
			return subServiceId;
		}
		public void setSubServiceId(Long subServiceId) {
			this.subServiceId = subServiceId;
		}
		
		public ServiceMaster getServiceId() {
			return serviceId;
		}
		public void setServiceId(ServiceMaster serviceId) {
			this.serviceId = serviceId;
		}
		public String getSubServiceName() {
			return subServiceName;
		}
		public void setSubServiceName(String subServiceName) {
			this.subServiceName = subServiceName;
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
		public boolean isIsMandatory() {
			return IsMandatory;
		}
		public void setIsMandatory(boolean isMandatory) {
			IsMandatory = isMandatory;
		}
		
	    
	    

}
