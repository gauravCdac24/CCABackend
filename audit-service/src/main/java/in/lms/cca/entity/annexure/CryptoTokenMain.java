package in.lms.cca.entity.annexure;

import com.fasterxml.jackson.annotation.JsonIgnore;

import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "crypto_token_main", schema = "audit_management")
public class CryptoTokenMain extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crypto_tok_main_id", length = 11)
    private Long cryptoTokMainId;

    @ManyToOne
    @JoinColumn(name = "annexure_main_id", referencedColumnName = "annexure_main_id")
	private AnnexureMain annexureMainId;
 	

    @Column(name = "auditor_verification", length = 200)
    private String auditorVerification;


	public Long getCryptoTokMainId() {
		return cryptoTokMainId;
	}


	public void setCryptoTokMainId(Long cryptoTokMainId) {
		this.cryptoTokMainId = cryptoTokMainId;
	}

	@JsonIgnore
	public AnnexureMain getAnnexureMainId() {
		return annexureMainId;
	}


	public void setAnnexureMainId(AnnexureMain annexureMainId) {
		this.annexureMainId = annexureMainId;
	}


	public String getAuditorVerification() {
		return auditorVerification;
	}


	public void setAuditorVerification(String auditorVerification) {
		this.auditorVerification = auditorVerification;
	}


    
	    
    
}
