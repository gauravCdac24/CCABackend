package in.lms.cca.dto;

public class GetRoles {
	private String roleId;
	private String roleName;
	private String Status;
	

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	
	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	@Override
	public String toString() {
		return "GetRoles [roleId=" + roleId + ", roleName=" + roleName + ", Status=" + Status + "]";
	}

	
	
	

}
