package in.lms.cca.dto;

public class RolesDTO {
	private String userRoleId;
	private String roleName;
	public String getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(String userRoleId) {
		this.userRoleId = userRoleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	@Override
	public String toString() {
		return "RolesDTO [userRoleId=" + userRoleId + ", roleName=" + roleName + "]";
	}
	
	  public RolesDTO(String roleId, String roleName) {
	        this.userRoleId = roleId;
	        this.roleName = roleName;
	    }

}