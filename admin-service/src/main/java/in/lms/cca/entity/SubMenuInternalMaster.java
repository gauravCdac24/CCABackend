package in.lms.cca.entity;

import in.lms.cca.util.global.AbstractTimestampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "sub_menu_internal_master", schema="admin_user_management")
public class SubMenuInternalMaster extends AbstractTimestampEntity{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sub_menu_internal_id", length=11)
    private Long subMenuInternalId;

    @ManyToOne
    @JoinColumn(name = "sub_menu_id", referencedColumnName = "sub_menu_id")
    private SubMenuMaster subMenuId;

    @Column(name = "submenu_internal_name", length = 50)
    private String subMenuInternalName;

    @Column(name = "submenu_internal_path", length = 100)
    private String subMenuInternalPath;
       
    @Column(name = "tracker_heading", length = 50)
    private String trackerHeading;

    @Column(name = "created_by", length = 74)
    private String createdBy;

    @Column(name = "updated_by", length = 74)
    private String updatedBy;

    @Column(name = "status", length = 8)
    private String status;

	public Long getSubMenuInternalId() {
		return subMenuInternalId;
	}

	public void setSubMenuInternalId(Long subMenuInternalId) {
		this.subMenuInternalId = subMenuInternalId;
	}

	public SubMenuMaster getSubMenuId() {
		return subMenuId;
	}

	public void setSubMenuId(SubMenuMaster subMenuId) {
		this.subMenuId = subMenuId;
	}

	public String getSubMenuInternalName() {
		return subMenuInternalName;
	}

	public void setSubMenuInternalName(String subMenuInternalName) {
		this.subMenuInternalName = subMenuInternalName;
	}

	public String getSubMenuInternalPath() {
		return subMenuInternalPath;
	}

	public void setSubMenuInternalPath(String subMenuInternalPath) {
		this.subMenuInternalPath = subMenuInternalPath;
	}

	public String getTrackerHeading() {
		return trackerHeading;
	}

	public void setTrackerHeading(String trackerHeading) {
		this.trackerHeading = trackerHeading;
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

    
	
    
    
	
}
