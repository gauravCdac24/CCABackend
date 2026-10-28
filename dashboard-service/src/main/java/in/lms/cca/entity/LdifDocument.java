package in.lms.cca.entity;


import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ldif_document",schema = "dashboard_management")
public class LdifDocument  extends AbstractTimestampEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ldif_document_id",length = 11)
    private Long ldifDocumentId;

    @Column(name = "causername", length = 75)
    private String caUsername;
    
    @Column(name = "month", length = 10)
    private String month;
    
    @Column(name = "year", length = 4)
    private String year;
	
    @Column(name = "created_by", length = 75)
    private String createdBy; 

    @Column(name = "Updated_by", length = 75)
    private String updatedBy;
    
    @Column(name = "filename", length = 100)
    private String fileName;

	public Long getLdifDocumentId() {
		return ldifDocumentId;
	}

	public void setLdifDocumentId(Long ldifDocumentId) {
		this.ldifDocumentId = ldifDocumentId;
	}

	public String getCaUsername() {
		return caUsername;
	}

	public void setCaUsername(String caUsername) {
		this.caUsername = caUsername;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


    
    
    
    

}
