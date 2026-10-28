package in.lms.cca.dto;

public class IndivEmailsDTO {

	    private Long indivEmailId;
	    private String emailId;
	    private IntentApplicationDTO intentAppId;
	    
	    
		public Long getIndivEmailId() {
			return indivEmailId;
		}
		public void setIndivEmailId(Long indivEmailId) {
			this.indivEmailId = indivEmailId;
		}
		public String getEmailId() {
			return emailId;
		}
		public void setEmailId(String emailId) {
			this.emailId = emailId;
		}
		public IntentApplicationDTO getIntentAppId() {
			return intentAppId;
		}
		public void setIntentAppId(IntentApplicationDTO intentAppId) {
			this.intentAppId = intentAppId;
		}
	    
	    
}
