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
@Table(name = "location_details", schema="audit_management")
public class LocationDetails extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_details_id", length = 11)
    private Long locationDetailsId;
    
    @ManyToOne
    @JoinColumn(name = "location_main_id", referencedColumnName = "location_main_id")
    private LocationMain locationMainId;
    
    @Column(name = "description", length = 50)
    private String description;
    
    @Column(name = "location", length = 100)
    private String location;
    
    @Column(name = "ca_administrator_count", length = 100)
    private String caAdministratorCount;
    
    @Column(name = "sys_administrator_count", length = 100)
    private String sysAdministratorCount;
    
    @Column(name = "ca_operators_count", length = 100)
    private String caOperatorsCount;
    
    @Column(name = "verification_officers_count", length = 100)
    private String verificationOfficersCount;
    
    @Column(name = "ca_manpower_count", length = 100)
    private String caManpowerCount;
    
	public Long getLocationDetailsId() {
		return locationDetailsId;
	}

	public void setLocationDetailsId(Long locationDetailsId) {
		this.locationDetailsId = locationDetailsId;
	}

	public LocationMain getLocationMainId() {
		return locationMainId;
	}

	public void setLocationMainId(LocationMain locationMainId) {
		this.locationMainId = locationMainId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCaAdministratorCount() {
		return caAdministratorCount;
	}

	public void setCaAdministratorCount(String caAdministratorCount) {
		this.caAdministratorCount = caAdministratorCount;
	}

	public String getSysAdministratorCount() {
		return sysAdministratorCount;
	}

	public void setSysAdministratorCount(String sysAdministratorCount) {
		this.sysAdministratorCount = sysAdministratorCount;
	}

	public String getCaOperatorsCount() {
		return caOperatorsCount;
	}

	public void setCaOperatorsCount(String caOperatorsCount) {
		this.caOperatorsCount = caOperatorsCount;
	}

	public String getVerificationOfficersCount() {
		return verificationOfficersCount;
	}

	public void setVerificationOfficersCount(String verificationOfficersCount) {
		this.verificationOfficersCount = verificationOfficersCount;
	}

	public String getCaManpowerCount() {
		return caManpowerCount;
	}

	public void setCaManpowerCount(String caManpowerCount) {
		this.caManpowerCount = caManpowerCount;
	}

    
    
    
    
}
