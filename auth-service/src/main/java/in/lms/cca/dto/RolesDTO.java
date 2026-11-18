package in.lms.cca.dto;

public class RolesDTO {
    private final String userRoleId;
    private final String roleName;

    // Constructor
    public RolesDTO(String userRoleId, String roleName) {
        this.userRoleId = userRoleId;
        this.roleName = roleName;
    }

    // Getters
  

    public String getRoleName() {
        return roleName;
    }

    public String getUserRoleId() {
		return userRoleId;
	}

	@Override
	public String toString() {
		return "RolesDTO [userRoleId=" + userRoleId + ", roleName=" + roleName + "]";
	}

	
}