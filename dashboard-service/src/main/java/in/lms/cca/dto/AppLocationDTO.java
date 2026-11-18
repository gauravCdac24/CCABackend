package in.lms.cca.dto;

public class AppLocationDTO{

    private Long locationId;
    private String locationName;
    private String addressId;
    private IntentApplicationDTO intentAppId;
    private String status;
    
    
	public Long getLocationId() {
		return locationId;
	}
	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getAddressId() {
		return addressId;
	}
	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}
	public IntentApplicationDTO getIntentAppId() {
		return intentAppId;
	}
	public void setIntentAppId(IntentApplicationDTO intentAppId) {
		this.intentAppId = intentAppId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

    
    
}
