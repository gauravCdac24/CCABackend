package in.lms.cca.payload;

import java.util.List;

public class RoleRequest {

	private String userId;
	private List<String> userRoles;
	
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<String> getUserRoles() {
		return userRoles;
	}
	public void setUserRoles(List<String> userRoles) {
		this.userRoles = userRoles;
	}
	
	
	
	
	
}
