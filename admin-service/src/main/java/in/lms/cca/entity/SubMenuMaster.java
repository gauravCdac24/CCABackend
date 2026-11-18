package in.lms.cca.entity;

import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "sub_menu_master", schema="admin_user_management")
public class SubMenuMaster extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sub_menu_id", length=11)
    private Long subMenuId;

    @ManyToOne
    @JoinColumn(name = "menu_id", referencedColumnName = "menu_id")
    private MenuMaster menuId;

    @Column(name = "submenu_name", length = 50)
    private String subMenuName;

    @Column(name = "submenu_path", length = 100)
    private String subMenuPath;
    
    @Column(name = "sub_menu_order", length=3)
    private Integer subMenuOrder;
    
    @Column(name = "tracker_heading", length = 50)
    private String trackerHeading;

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

	public MenuMaster getMenuId() {
		return menuId;
	}

	public void setMenuId(MenuMaster menuId) {
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

	public String getTrackerHeading() {
		return trackerHeading;
	}

	public void setTrackerHeading(String trackerHeading) {
		this.trackerHeading = trackerHeading;
	}

	
   
    
}
