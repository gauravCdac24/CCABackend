package in.lms.cca.entity;

import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "facility_location", schema = "new_license_management")
public class FacilityLocation extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "facility_location_id", length = 11)
    private Long facilityLocationId;

    @Column(name = "primary_location", length = 74)
    private String primaryLocation;

    @Column(name = "dr_site_location", length = 74)
    private String drSiteLocation;

    @Column(name = "other_location", length = 74)
    private String otherLocation;

    @Column(name = "status", length = 8)
    private String status;

    @Column(name = "created_by", length = 74)
    private String createdBy;

    @Column(name = "updated_by", length = 74)
    private String updatedBy;

	public Long getFacilityLocationId() {
		return facilityLocationId;
	}

	public void setFacilityLocationId(Long facilityLocationId) {
		this.facilityLocationId = facilityLocationId;
	}

	public String getPrimaryLocation() {
		return primaryLocation;
	}

	public void setPrimaryLocation(String primaryLocation) {
		this.primaryLocation = primaryLocation;
	}

	public String getDrSiteLocation() {
		return drSiteLocation;
	}

	public void setDrSiteLocation(String drSiteLocation) {
		this.drSiteLocation = drSiteLocation;
	}

	public String getOtherLocation() {
		return otherLocation;
	}

	public void setOtherLocation(String otherLocation) {
		this.otherLocation = otherLocation;
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
