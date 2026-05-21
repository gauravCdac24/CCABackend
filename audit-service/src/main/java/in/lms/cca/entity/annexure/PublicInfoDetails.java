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
@Table(name = "public_info_details", schema="audit_management")
public class PublicInfoDetails extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "public_info_details_id", length = 11)
    private Long publicInfoDetailsId;
    
    @ManyToOne
    @JoinColumn(name = "public_info_main_id", referencedColumnName = "public_info_main_id")
    private PublicInfoMain publicInfoMainId;
    
    @Column(name = "description", length = 50)
    private String description;
    
    @Column(name = "web_link", columnDefinition = "TEXT")
    private String webLink;
    

	public Long getPublicInfoDetailsId() {
		return publicInfoDetailsId;
	}

	public void setPublicInfoDetailsId(Long publicInfoDetailsId) {
		this.publicInfoDetailsId = publicInfoDetailsId;
	}

	@JsonIgnore
	public PublicInfoMain getPublicInfoMainId() {
		return publicInfoMainId;
	}

	public void setPublicInfoMainId(PublicInfoMain publicInfoMainId) {
		this.publicInfoMainId = publicInfoMainId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWebLink() {
		return webLink;
	}

	public void setWebLink(String webLink) {
		this.webLink = webLink;
	}

    
    
    
}
