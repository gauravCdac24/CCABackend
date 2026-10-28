package in.lms.cca.entity.logs;

import in.lms.cca.entity.MenuMaster;
import in.lms.cca.util.global.LogAbstractTimestampOperationEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "menu_master", schema = "cca_logs_management")
public class MenuMasterLogs extends LogAbstractTimestampOperationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_menu_id", length=11)
    private Long logMenuId;
    
    @Column(name = "menu_id", length=11)
    private Long menuId;

    @Column(name = "menu_name", length = 50)
    private String menuName;

    @Column(name = "menu_icon", length = 50)
    private String menuIcon;

    @Column(name = "menu_order", length=3)
    private Integer menuOrder;
    
    @Column(name = "role_id", length=11)
    private Long roleId;

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

	public Long getLogMenuId() {
		return logMenuId;
	}

	public void setLogMenuId(Long logMenuId) {
		this.logMenuId = logMenuId;
	}

	public MenuMasterLogs(Long logMenuId, Long menuId, Long roleId, String menuName, String menuIcon, Integer menuOrder,
			String createdBy, String updatedBy, String status) {
		super();
		this.logMenuId = logMenuId;
		this.menuId = menuId;
		this.menuName = menuName;
		this.menuIcon = menuIcon;
		this.menuOrder = menuOrder;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
		this.status = status;
		this.roleId = roleId;
	}
    
	public MenuMasterLogs(MenuMaster e, String IPAddress,String operation,String userName) {
		
		this.menuId = e.getMenuId();
		this.menuName = e.getMenuName();
		this.menuIcon = e.getMenuIcon();
		this.menuOrder = e.getMenuOrder();
		this.status = e.getStatus();
		this.setIPAddress(IPAddress);
		this.setOperation(operation);
		this.setUserName(userName);
	}
    

}
