package in.lms.cca.entity;


import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "dsc_esign_issued",schema = "dashboard_management")
public class DSCeSignIssued  extends AbstractTimestampEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dscesign_issued_id",length = 11)
    private Long dscesignIssuedId;

    @Column(name = "causername", length = 75)
    private String caUsername;
    
    @Column(name = "countryid", length = 75)
    private String countryId;
        
    @Column(name = "stateid", length = 75)
    private String stateId;

    @Column(name = "month", length = 10)
    private String month;
    
    @Column(name = "year", length = 4)
    private String year;
    
    @Column(name = "dsc_issued", length = 10)
    private String dscIssued;
    
    @Column(name = "esign_issued", length = 10)
    private String eSignIssued;
	
    @Column(name = "created_by", length = 75)
    private String createdBy; 

    @Column(name = "Updated_by", length = 75)
    private String updatedBy;

	public Long getDscesignIssuedId() {
		return dscesignIssuedId;
	}

	public void setDscesignIssuedId(Long dscesignIssuedId) {
		this.dscesignIssuedId = dscesignIssuedId;
	}

	public String getCaUsername() {
		return caUsername;
	}

	public void setCaUsername(String caUsername) {
		this.caUsername = caUsername;
	}

	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public String getStateId() {
		return stateId;
	}

	public void setStateId(String stateId) {
		this.stateId = stateId;
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

	public String getDscIssued() {
		return dscIssued;
	}

	public void setDscIssued(String dscIssued) {
		this.dscIssued = dscIssued;
	}

	public String geteSignIssued() {
		return eSignIssued;
	}

	public void seteSignIssued(String eSignIssued) {
		this.eSignIssued = eSignIssued;
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
    
    
    
    

}
