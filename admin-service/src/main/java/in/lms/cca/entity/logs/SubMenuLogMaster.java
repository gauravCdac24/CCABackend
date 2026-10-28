package in.lms.cca.entity.logs;

import in.lms.cca.entity.SubMenuMaster;
import in.lms.cca.util.global.LogAbstractTimestampOperationEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "sub_menu_master_log",schema = "cca_logs_management")
public class SubMenuLogMaster extends LogAbstractTimestampOperationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_sub_menu_id", length=11)
    private Long logSubMenuId;
    
    @Column(name = "sub_menu_id", length=11)
    private Long subMenuId;

    @ManyToOne
    @JoinColumn(name = "menu_id", referencedColumnName = "menu_id")
    private MenuMasterLogs menuId;

    @Column(name = "submenu_name", length = 50)
    private String subMenuName;

    @Column(name = "submenu_path", length = 100)
    private String subMenuPath;
    
    @Column(name = "sub_menu_order", length=3)
    private Integer subMenuOrder;

    @Column(name = "created_by", length = 74)
    private String createdBy;

    @Column(name = "updated_by", length = 74)
    private String updatedBy;

    @Column(name = "status", length = 8)
    private String status;
    
    

	public Long getSubMenuId() {
		return subMenuId;
	}

	public void setSubMenuId(Long subMenuId) {
		this.subMenuId = subMenuId;
	}

	public MenuMasterLogs getMenuId() {
		return menuId;
	}

	public void setMenuId(MenuMasterLogs menuId) {
		this.menuId = menuId;
	}

	public String getSubMenuName() {
		return subMenuName;
	}

	public void setSubMenuName(String subMenuName) {
		this.subMenuName = subMenuName;
	}

	public String getSubMenuPath() {
		return subMenuPath;
	}

	public void setSubMenuPath(String subMenuPath) {
		this.subMenuPath = subMenuPath;
	}

	public Integer getSubMenuOrder() {
		return subMenuOrder;
	}

	public void setSubMenuOrder(Integer subMenuOrder) {
		this.subMenuOrder = subMenuOrder;
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

	public Long getLogSubMenuId() {
		return logSubMenuId;
	}

	public void setLogSubMenuId(Long logSubMenuId) {
		this.logSubMenuId = logSubMenuId;
	}

	public SubMenuLogMaster(Long logSubMenuId, Long subMenuId, MenuMasterLogs menuId, String subMenuName,
			String subMenuPath, Integer subMenuOrder, String createdBy, String updatedBy, String status) {
		super();
		this.logSubMenuId = logSubMenuId;
		this.subMenuId = subMenuId;
		this.menuId = menuId;
		this.subMenuName = subMenuName;
		this.subMenuPath = subMenuPath;
		this.subMenuOrder = subMenuOrder;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
		this.status = status;
	}

	public SubMenuLogMaster(SubMenuMaster e,String IPAddress,String operation,String userName) {
		
		this.subMenuId = e.getSubMenuId();
		this.subMenuName = e.getSubMenuName();
		this.subMenuPath = e.getSubMenuPath();
		this.subMenuOrder = e.getSubMenuOrder();
		this.createdBy = e.getCreatedBy();
		this.updatedBy = e.getUpdatedBy();
		this.status = e.getStatus();
		this.setIPAddress(IPAddress);
		this.setOperation(operation);
		this.setUserName(userName);
	}

   
    
}
