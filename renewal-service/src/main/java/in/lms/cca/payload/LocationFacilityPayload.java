package in.lms.cca.payload;

import java.util.List;

public class LocationFacilityPayload {
	
	
	private List<LocationDetailsPayload>LocationDetails;
	private String Status;
    private String created;           
    private String updated;         
    private String createdBy;           
    private String updatedBy;
    
	
	public List<LocationDetailsPayload> getLocationDetails() {
		return LocationDetails;
	}
	public void setLocationDetails(List<LocationDetailsPayload> locationDetails) {
		LocationDetails = locationDetails;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
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
