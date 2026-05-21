package in.lms.cca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AppUndertakingDTO {

    private String appUndertakingId;
    @JsonProperty("undertakingId")
    private String undertakingId;
    private String intentAppId;
    private String createdBy;
    private String updatedBy;
    private String userName;
    @JsonProperty("appTypeMasterId")
    private String appTypeMasterId;
    private String created;
    private String updated;
    private String status;



  


	public String getAppUndertakingId() {
		return appUndertakingId;
	}

	public void setAppUndertakingId(String appUndertakingId) {
		this.appUndertakingId = appUndertakingId;
	}

	


	@Override
	public String toString() {
		return "AppUndertakingDTO [appUndertakingId=" + appUndertakingId + ", undertakingId=" + undertakingId
				+ ", intentAppId=" + intentAppId + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy
				+ ", userName=" + userName + ", appTypeMasterId=" + appTypeMasterId + ", created=" + created
				+ ", updated=" + updated + ", status=" + status + "]";
	}

	public String getUndertakingId() {
		return undertakingId;
	}

	public void setUndertakingId(String undertakingId) {
		this.undertakingId = undertakingId;
	}

	public String getIntentAppId() {
		return intentAppId;
	}

	public void setIntentAppId(String intentAppId) {
		this.intentAppId = intentAppId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAppTypeMasterId() {
		return appTypeMasterId;
	}

	public void setAppTypeMasterId(String appTypeMasterId) {
		this.appTypeMasterId = appTypeMasterId;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
		this.updated = updated;
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
