package in.lms.cca.dto;

import in.lms.cca.entity.Intent;

public class IntentLoginDTO {

	private Intent intent;
	private String accountStatus=null;
	
	public Intent getIntent() {
		return intent;
	}
	public void setIntent(Intent intent) {
		this.intent = intent;
	}
	public String getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}
	
	
}
