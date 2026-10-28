package in.lms.cca.entity.annexure;

import com.fasterxml.jackson.annotation.JsonIgnore;

import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "crypto_token_details", schema = "audit_management")
public class CryptoTokenDetails extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crypto_tok_details_id", length = 11)
    private Long cryptoTokDetailsId;

    @ManyToOne
    @JoinColumn(name = "crypto_token_id", referencedColumnName = "crypto_tok_main_id")
    private CryptoTokenMain cryptoTokMainId;

    @Column(name = "brand_name", length = 50)
    private String brandName;

    @Column(name = "oem_details", length = 100)
    private String oemDetails;

    @Column(name = "mak_in_percentage", length = 3)
    private Integer makInPercentage;

    @Column(name = "fip_cert_up_to")
    private Date fipCertUpTo;

    @Column(name = "sec_audit_details", length = 100)
    private String secAuditDetails; 
    
	public Long getCryptoTokDetailsId() {
		return cryptoTokDetailsId;
	}

	public void setCryptoTokDetailsId(Long cryptoTokDetailsId) {
		this.cryptoTokDetailsId = cryptoTokDetailsId;
	}

	@JsonIgnore
	public CryptoTokenMain getCryptoTokMainId() {
		return cryptoTokMainId;
	}

	public void setCryptoTokMainId(CryptoTokenMain cryptoTokMainId) {
		this.cryptoTokMainId = cryptoTokMainId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getOemDetails() {
		return oemDetails;
	}

	public void setOemDetails(String oemDetails) {
		this.oemDetails = oemDetails;
	}

	public Integer getMakInPercentage() {
		return makInPercentage;
	}

	public void setMakInPercentage(Integer makInPercentage) {
		this.makInPercentage = makInPercentage;
	}

	public Date getFipCertUpTo() {
		return fipCertUpTo;
	}

	public void setFipCertUpTo(Date fipCertUpTo) {
		this.fipCertUpTo = fipCertUpTo;
	}

	public String getSecAuditDetails() {
		return secAuditDetails;
	}

	public void setSecAuditDetails(String secAuditDetails) {
		this.secAuditDetails = secAuditDetails;
	}

    
   
}

