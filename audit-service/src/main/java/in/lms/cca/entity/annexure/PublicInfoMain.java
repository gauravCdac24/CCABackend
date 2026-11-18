package in.lms.cca.entity.annexure;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "public_info_main", schema="audit_management")
public class PublicInfoMain extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "public_info_main_id", length = 11)
    private Long publicInfoMainId;
    
    @Column(name = "auditor_verification", length = 250)
    private String auditorVerification;
    
    @ManyToOne
    @JoinColumn(name = "annexure_main_id", referencedColumnName = "annexure_main_id")
	private AnnexureMain annexureMainId;

	public Long getPublicInfoMainId() {
		return publicInfoMainId;
	}

	public void setPublicInfoMainId(Long publicInfoMainId) {
		this.publicInfoMainId = publicInfoMainId;
	}

	public String getAuditorVerification() {
		return auditorVerification;
	}

	public void setAuditorVerification(String auditorVerification) {
		this.auditorVerification = auditorVerification;
	}

	@JsonIgnore
	public AnnexureMain getAnnexureMainId() {
		return annexureMainId;
	}

	public void setAnnexureMainId(AnnexureMain annexureMainId) {
		this.annexureMainId = annexureMainId;
	}
     

    
    
}
