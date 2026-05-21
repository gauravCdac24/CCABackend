package in.lms.cca.entity;

import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "esigned_documents",schema="audit_management")
public class ESignedDocumentsEntity extends AbstractTimestampEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "esigned_id",length = 11)
	private Long eSignedId;
	
	 @Column(name = "signed_doc_id",length = 100)
	private String signedDocId;
	
	 @Column(name = "username_id",length = 74)
	private String userName;
	
	 @Column(name = "status",length = 11)
	private String status;

	public Long geteSignedId() {
		return eSignedId;
	}

	public void seteSignedId(Long eSignedId) {
		this.eSignedId = eSignedId;
	}

	

	public String getSignedDocId() {
		return signedDocId;
	}

	public void setSignedDocId(String signedDocId) {
		this.signedDocId = signedDocId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ESignedDocumentsEntity [eSignedId=" + eSignedId + ", signedDocId=" + signedDocId + ", userName="
				+ userName + ", status=" + status + "]";
	}
	
	
	
	

}
