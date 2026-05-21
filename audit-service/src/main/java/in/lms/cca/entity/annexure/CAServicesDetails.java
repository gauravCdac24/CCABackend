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
@Table(name = "ca_services_details", schema="audit_management")
public class CAServicesDetails extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ca_services_details_id", length = 11)
    private Long caServicesDetailsId;
    
    @ManyToOne
    @JoinColumn(name = "ca_services_main_id", referencedColumnName = "ca_services_main_id")
    private CAServicesMain caServicesMainId;
    
    @Column(name = "description", length = 50)
    private String description;
    
    @Column(name = "internal_only", length = 3)
    private String internalOnly;
    
    @Column(name = "external_only", length = 3)
    private String externalOnly;
    
    @Column(name = "asp_org_count", length = 9)
    private Long aspOrgCount;
    
    @Column(name = "asp_org_count_file", length = 100)
    private String aspOrgCountFile;
    

	public Long getCaServicesDetailsId() {
		return caServicesDetailsId;
	}

	public void setCaServicesDetailsId(Long caServicesDetailsId) {
		this.caServicesDetailsId = caServicesDetailsId;
	}

	public CAServicesMain getCaServicesMainId() {
		return caServicesMainId;
	}

	public void setCaServicesMainId(CAServicesMain caServicesMainId) {
		this.caServicesMainId = caServicesMainId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getInternalOnly() {
		return internalOnly;
	}

	public void setInternalOnly(String internalOnly) {
		this.internalOnly = internalOnly;
	}

	public String getExternalOnly() {
		return externalOnly;
	}

	public void setExternalOnly(String externalOnly) {
		this.externalOnly = externalOnly;
	}

	public Long getAspOrgCount() {
		return aspOrgCount;
	}

	public void setAspOrgCount(Long aspOrgCount) {
		this.aspOrgCount = aspOrgCount;
	}

	public String getAspOrgCountFile() {
		return aspOrgCountFile;
	}

	public void setAspOrgCountFile(String aspOrgCountFile) {
		this.aspOrgCountFile = aspOrgCountFile;
	}

    
    
    
    
}
