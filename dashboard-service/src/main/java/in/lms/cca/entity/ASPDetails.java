package in.lms.cca.entity;

import java.util.Date;

import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "asp_details", schema = "dashboard_management")
public class ASPDetails extends AbstractTimestampEntity{

	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "aspid",length = 11)
	    private Long aspId;

	    @Column(name = "causername", length = 75)
	    private String caUsername;
	    
	    @Column(name = "aspname", length = 100)
	    private String aspName;
	        
	    @Column(name = "emailid", length = 50)
	    private String emailId;
	    
	    @Column(name = "countryid", length = 75)
	    private String countryId;
	        
	    @Column(name = "stateid", length = 75)
	    private String stateId;
	    
	    @Column(name = "onboarding_date")
	    private Date onBoardingDate;
	    
	    @Column(name = "exit_date", length = 10)
	    private Date exitDate;
		
	    @Column(name = "created_by", length = 75)
	    private String createdBy; 

	    @Column(name = "Updated_by", length = 75)
	    private String updatedBy;
	    
	    

		public Long getAspId() {
			return aspId;
		}

		public void setAspId(Long aspId) {
			this.aspId = aspId;
		}

		public String getCaUsername() {
			return caUsername;
		}

		public void setCaUsername(String caUsername) {
			this.caUsername = caUsername;
		}

		public String getAspName() {
			return aspName;
		}

		public void setAspName(String aspName) {
			this.aspName = aspName;
		}

		public String getEmailId() {
			return emailId;
		}

		public void setEmailId(String emailId) {
			this.emailId = emailId;
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

		public Date getOnBoardingDate() {
			return onBoardingDate;
		}

		public void setOnBoardingDate(Date onBoardingDate) {
			this.onBoardingDate = onBoardingDate;
		}

		public Date getExitDate() {
			return exitDate;
		}

		public void setExitDate(Date exitDate) {
			this.exitDate = exitDate;
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
