package in.lms.cca.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import in.lms.cca.util.global.AbstractTimestampEntity;


@Entity
@Table(name = "role_master", schema="admin_user_management")
public class RoleMaster extends AbstractTimestampEntity {

    @Override
	public String toString() {
		return "RoleMaster [roleId=" + roleId + ", roleName=" + roleName + ", description=" + description + ", path="
				+ path + ", homePage=" + homePage + ", status=" + status + "]";
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", length=2)
    private Integer roleId;

    @Column(name = "role_name", length = 50)
    private String roleName;

    @Column(name = "description", length = 50)
    private String description;

    @Column(name = "path", length = 100)
    private String path;

    @Column(name = "home_page", length = 100)
    private String homePage;

    @Column(name = "status", length = 8)
    private String status;

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getHomePage() {
		return homePage;
	}

	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

    
    
    
    
}


