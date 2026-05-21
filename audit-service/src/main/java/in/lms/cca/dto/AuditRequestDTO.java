package in.lms.cca.dto;

import java.util.Date;

public class AuditRequestDTO {
	  private Long id;
	    private Integer controlId;
	    private String userName;
	    private String status;
        private Date created;
        private Date updated;
	    // Getters and Setters
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

	   
	    public Date getCreated() {
			return created;
		}

		public void setCreated(Date created) {
			this.created = created;
		}

		public Date getUpdated() {
			return updated;
		}

		public void setUpdated(Date updated) {
			this.updated = updated;
		}

		
	    @Override
		public String toString() {
			return "AuditRequestDTO [id=" + id + ", controlId=" + controlId + ", userName=" + userName + ", status="
					+ status + ", created=" + created + ", updated=" + updated + "]";
		}

		public AuditRequestDTO(Long id, Integer controlId, String userName, String status, Date created, Date updated) {
			super();
			this.id = id;
			this.controlId = controlId;
			this.userName = userName;
			this.status = status;
			this.created = created;
			this.updated = updated;
		}

		public AuditRequestDTO() {
	        
	    }
    
}
