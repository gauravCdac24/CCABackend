package in.lms.cca.entity;

import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "menu_master", schema="admin_user_management")
public class MenuMaster extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id", length=11)
    private Long menuId;

    @Column(name = "menu_name", length = 50)
    private String menuName;

    @Column(name = "menu_icon", length = 50)
    private String menuIcon;

    @Column(name = "menu_order", length=3)
    private Integer menuOrder;
    
    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    private RoleMaster roleId;

    @Column(name = "createdby", length = 74)
    private String createdBy;

    @Column(name = "updatedby", length = 74)
    private String updatedBy;

    @Column(name = "status", length = 8)
    private String status;

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuIcon() {
		return menuIcon;
	}

	public void setMenuIcon(String menuIcon) {
		this.menuIcon = menuIcon;
	}

	public Integer getMenuOrder() {
		return menuOrder;
	}

	public void setMenuOrder(Integer menuOrder) {
		this.menuOrder = menuOrder;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public RoleMaster getRoleId() {
		return roleId;
	}

	public void setRoleId(RoleMaster roleId) {
		this.roleId = roleId;
	}
    
    
    

}
