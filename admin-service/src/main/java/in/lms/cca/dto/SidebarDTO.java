package in.lms.cca.dto;

import java.util.List;

import in.lms.cca.entity.SubMenuMaster;

public class SidebarDTO {

	private  Long menuId;
	private String menuName;
    private String menuIcon;
    private Integer menuOrder;
    private List<SubMenuMaster> children;
    
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
	public List<SubMenuMaster> getChildren() {
		return children;
	}
	public void setChildren(List<SubMenuMaster> children) {
		this.children = children;
	}
	public SidebarDTO(Long menuId, String menuName, String menuIcon, Integer menuOrder,
			List<SubMenuMaster> children) {
		super();
		this.menuId = menuId;
		this.menuName = menuName;
		this.menuIcon = menuIcon;
		this.menuOrder = menuOrder;
		this.children = children;
	}
	
	
	
}
