package in.lms.cca.entity.annexure;

import java.util.Date;
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
@Table(name = "ca_sw_web_details", schema="audit_management")
public class CaSwWebDetails extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ca_web_details_id", length = 11)
    private Long caWebDetailsId;
    
    @ManyToOne
    @JoinColumn(name = "ca_web_main_id", referencedColumnName = "ca_web_main_id")
    private CaSwWebMain caWebMainId;
    
    @Column(name = "description", length = 50)
    private String description;
    
    @Column(name = "developed_by", length = 64)
    private String developedBy;
    
    @Column(name = "database_used", length = 30)
    private String databaseUsed;
    
    @Column(name = "certification", length = 64)
    private String certification;
    
    @Column(name = "last_security_audit")
    private Date lastSecurityAudit;
    

	public Long getCaWebDetailsId() {
		return caWebDetailsId;
	}

	public void setCaWebDetailsId(Long caWebDetailsId) {
		this.caWebDetailsId = caWebDetailsId;
	}

	public CaSwWebMain getCaWebMainId() {
		return caWebMainId;
	}

	public void setCaWebMainId(CaSwWebMain caWebMainId) {
		this.caWebMainId = caWebMainId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDevelopedBy() {
		return developedBy;
	}

	public void setDevelopedBy(String developedBy) {
		this.developedBy = developedBy;
	}

	public String getDatabaseUsed() {
		return databaseUsed;
	}

	public void setDatabaseUsed(String databaseUsed) {
		this.databaseUsed = databaseUsed;
	}

	public String getCertification() {
		return certification;
	}

	public void setCertification(String certification) {
		this.certification = certification;
	}

	public Date getLastSecurityAudit() {
		return lastSecurityAudit;
	}

	public void setLastSecurityAudit(Date lastSecurityAudit) {
		this.lastSecurityAudit = lastSecurityAudit;
	}


    
    
    
}
