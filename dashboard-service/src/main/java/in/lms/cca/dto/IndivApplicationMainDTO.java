package in.lms.cca.dto;

import java.util.List;

public class IndivApplicationMainDTO {

	private IndivApplicationDTO  application;
	private List<IndivEmailsDTO> emails;

	public IndivApplicationDTO getApplication() {
		return application;
	}

	public void setApplication(IndivApplicationDTO application) {
		this.application = application;
	}

	public List<IndivEmailsDTO> getEmails() {
		return emails;
	}

	public void setEmails(List<IndivEmailsDTO> emails) {
		this.emails = emails;
	}

	
	
	
	
}
