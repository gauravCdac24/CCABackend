package in.lms.cca.service;

import java.util.List;
import java.util.Optional;
import in.lms.cca.entity.SubMenuInternalMaster;

public interface ISubMenuInternalMasterService {

	Optional<SubMenuInternalMaster> addSubMenuInternal(SubMenuInternalMaster subMenuInternalObj);
	Optional<SubMenuInternalMaster> updateSubMenuInternal(SubMenuInternalMaster subMenuInternalObj);
	SubMenuInternalMaster getSubMenuInternalById(Long id);
	List<SubMenuInternalMaster> getAllSubMenuInternal();
	List<SubMenuInternalMaster> getAllActiveSubMenuInternal();
	List<SubMenuInternalMaster> getAllInactiveSubMenuInternal();
	List<SubMenuInternalMaster> getAllSubMenusInternalBySubMenuId (Long submenuId);
	List<SubMenuInternalMaster> getAllActiveSubMenusInternalBySubMenuId (Long submenuId);
	List<SubMenuInternalMaster> getAllInactiveSubMenusInternalBySubMenuId (Long submenuId);
	SubMenuInternalMaster getSubMenuInternalByPath(String path);
	SubMenuInternalMaster getSubMenuInternalByNameAndSubMenuId(String name, Long submenuid);
	SubMenuInternalMaster getSubMenuInternalByPathAndSubMenuId(String path, Long submenuid);
	SubMenuInternalMaster getSubMenuInternalDetailsByPath(String pathName);
}
