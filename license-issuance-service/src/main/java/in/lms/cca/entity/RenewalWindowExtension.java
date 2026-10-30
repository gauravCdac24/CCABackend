package in.lms.cca.entity;

import java.time.LocalDate;

import in.lms.cca.util.golbal.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "renewal_window_extension", schema = "license_issuance_management")
public class RenewalWindowExtension extends AbstractTimestampEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "extension_id")
	private Long extensionId;

	@Column(name = "license_id")
	private Long licenseId;

	@Column(name = "username", length = 74, nullable = false)
	private String userName;

	@Column(name = "licensename", length = 200)
	private String licenseName;

	@Column(name = "serial_no", length = 50)
	private String serialNo;

	@Column(name = "granted_on", nullable = false)
	private LocalDate grantedOn;

	@Column(name = "extension_until", nullable = false)
	private LocalDate extensionUntil;

	@Column(name = "granted_by", length = 74, nullable = false)
	private String grantedBy;

	@Column(name = "remarks", columnDefinition = "TEXT")
	private String remarks;

	@Column(name = "status", length = 10, nullable = false)
	private String status;

	public Long getExtensionId() {
		return extensionId;
	}

	public void setExtensionId(Long extensionId) {
		this.extensionId = extensionId;
	}

	public Long getLicenseId() {
		return licenseId;
	}

	public void setLicenseId(Long licenseId) {
		this.licenseId = licenseId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLicenseName() {
		return licenseName;
	}

	public void setLicenseName(String licenseName) {
		this.licenseName = licenseName;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public LocalDate getGrantedOn() {
		return grantedOn;
	}

	public void setGrantedOn(LocalDate grantedOn) {
		this.grantedOn = grantedOn;
	}

	public LocalDate getExtensionUntil() {
		return extensionUntil;
	}

	public void setExtensionUntil(LocalDate extensionUntil) {
		this.extensionUntil = extensionUntil;
	}

	public String getGrantedBy() {
		return grantedBy;
	}

	public void setGrantedBy(String grantedBy) {
		this.grantedBy = grantedBy;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
