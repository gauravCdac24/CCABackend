package in.lms.cca.entity;

import java.util.Date;

import in.lms.cca.util.golbal.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "esign_request", schema="esign_dsc_management")
public class ESignRequest extends AbstractTimestampEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "esign_request_id", length = 11)
	private Long eSignRequestId;
	
	@Column(name = "user_name", length = 30)
	private String userName;
	
	@Column(name = "fullname", length = 200)
	private String fullname;
	
	@Column(name = "service_name", length = 50)
	private String serviceName;
	
	@Column(name = "service_Url", columnDefinition = "TEXT")
	private String serviceUrl;
	
	@Column(name = "orginal_file_id", length = 75)
	private String orgFileId;
	
	@Column(name = "document_path", columnDefinition = "TEXT")
	private String documentPath;
	
	@Column(name = "document_hash", length = 100)
	private String documentHash;
	
	@Column(name = "eSignVersion", length = 3)
	private String eSignVersion;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "request_timestamp" )
	private Date requestTimeStamp;
	
	@Column(name = "transaction_id", length = 50)
	private String transactionId;
	
	@Column(name = "ekyc_id_type", length = 2)
	private String eKYCIdType;
	
	@Column(name = "auth_mode", length = 11)
	private String authMode;
	
	@Column(name = "response_sig_type", length = 8)
	private String responseSigType;
	
	@Column(name = "doc_info", length = 50)
	private String docInfo;
	
	@Column(name = "redirect_url", columnDefinition = "TEXT")
	private String redirectUrl;

	@Column(name = "file_name", length = 50)
	private String fileName;

	@Column(name = "auth_token", columnDefinition = "TEXT")
	private String authToken;

	public Long geteSignRequestId() {
		return eSignRequestId;
	}

	
	
	public String getFullname() {
		return fullname;
	}



	public void setFullname(String fullname) {
		this.fullname = fullname;
	}



	public void seteSignRequestId(Long eSignRequestId) {
		this.eSignRequestId = eSignRequestId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public String getOrgFileId() {
		return orgFileId;
	}

	public void setOrgFileId(String orgFileId) {
		this.orgFileId = orgFileId;
	}

	public String getDocumentPath() {
		return documentPath;
	}

	public void setDocumentPath(String documentPath) {
		this.documentPath = documentPath;
	}

	public String getDocumentHash() {
		return documentHash;
	}

	public void setDocumentHash(String documentHash) {
		this.documentHash = documentHash;
	}

	public String geteSignVersion() {
		return eSignVersion;
	}

	public void seteSignVersion(String eSignVersion) {
		this.eSignVersion = eSignVersion;
	}

	public Date getRequestTimeStamp() {
		return requestTimeStamp;
	}

	public void setRequestTimeStamp(Date requestTimeStamp) {
		this.requestTimeStamp = requestTimeStamp;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String geteKYCIdType() {
		return eKYCIdType;
	}

	public void seteKYCIdType(String eKYCIdType) {
		this.eKYCIdType = eKYCIdType;
	}

	public String getAuthMode() {
		return authMode;
	}

	public void setAuthMode(String authMode) {
		this.authMode = authMode;
	}

	public String getResponseSigType() {
		return responseSigType;
	}

	public void setResponseSigType(String responseSigType) {
		this.responseSigType = responseSigType;
	}

	public String getDocInfo() {
		return docInfo;
	}

	public void setDocInfo(String docInfo) {
		this.docInfo = docInfo;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	

	
	

}
