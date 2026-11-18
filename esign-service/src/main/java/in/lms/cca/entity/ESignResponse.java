package in.lms.cca.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "esign_response", schema="esign_dsc_management")
public class ESignResponse {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "esign_response_id", length = 11)
	private Long eSignResponseId;
	
	@OneToOne
	@JoinColumn(name = "esign_request_id", referencedColumnName = "esign_request_id")
	private ESignRequest eSignRequestId;
	
	@Column(name = "esign_status", length = 1)
	private Integer eSignStatus;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "response_timestamp" )
	private Date responseTimeStamp;
	
	@Column(name = "rescode", length = 50)
	private String rescode;

	@Column(name = "digest_value", length = 100)
	private String digestValue;
	
	@Column(name = "signature_value", columnDefinition="TEXT")
	private String signatureValue;

	@Column(name = "err_code", length = 10)
	private String errCode;
	
	@Column(name = "err_msg", length = 250)
	private String errMsg;
	
	@Column(name = "file_name", length = 50)
	private String fileName;
	

	public Long geteSignResponseId() {
		return eSignResponseId;
	}

	public void seteSignResponseId(Long eSignResponseId) {
		this.eSignResponseId = eSignResponseId;
	}

	public ESignRequest geteSignRequestId() {
		return eSignRequestId;
	}

	public void seteSignRequestId(ESignRequest eSignRequestId) {
		this.eSignRequestId = eSignRequestId;
	}

	public Integer geteSignStatus() {
		return eSignStatus;
	}

	public void seteSignStatus(Integer eSignStatus) {
		this.eSignStatus = eSignStatus;
	}

	public Date getResponseTimeStamp() {
		return responseTimeStamp;
	}

	public void setResponseTimeStamp(Date responseTimeStamp) {
		this.responseTimeStamp = responseTimeStamp;
	}

	public String getRescode() {
		return rescode;
	}

	public void setRescode(String rescode) {
		this.rescode = rescode;
	}

	public String getDigestValue() {
		return digestValue;
	}

	public void setDigestValue(String digestValue) {
		this.digestValue = digestValue;
	}

	public String getSignatureValue() {
		return signatureValue;
	}

	public void setSignatureValue(String signatureValue) {
		this.signatureValue = signatureValue;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
	
	
}
