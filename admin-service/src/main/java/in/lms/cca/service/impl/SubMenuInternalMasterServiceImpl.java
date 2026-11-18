package in.lms.cca.service.impl;

import java.net.InetAddress;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.SubMenuInternalMaster;
import in.lms.cca.entity.SubMenuMaster;
import in.lms.cca.entity.logs.SubMenuLogMaster;
import in.lms.cca.repository.SubMenuInternalMasterRepository;
import in.lms.cca.service.ISubMenuInternalMasterService;
import in.lms.cca.util.global.Constant;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class SubMenuInternalMasterServiceImpl implements ISubMenuInternalMasterService{

	@Autowired
	private SubMenuInternalMasterRepository subMenuInternalRepo;
	
	@Override
	public Optional<SubMenuInternalMaster> addSubMenuInternal(SubMenuInternalMaster subMenuInternalObj) {
		if(subMenuInternalObj == null)
			return Optional.empty();
		
		try { 
	       
			SubMenuInternalMaster obj = subMenuInternalRepo.save(subMenuInternalObj);
	        return Optional.of(obj);
	    
	}catch(Exception e) {
			return Optional.empty();
		}
	}

	@Override
	public Optional<SubMenuInternalMaster> updateSubMenuInternal(SubMenuInternalMaster subMenuInternalObj) {
		if(subMenuInternalObj == null)
			return Optional.empty();
		
		if(subMenuInternalObj.getSubMenuInternalId() == null)
			return Optional.empty();
		
		try { 
	       
			SubMenuInternalMaster obj = subMenuInternalRepo.save(subMenuInternalObj);
	        return Optional.of(obj);
	    
	}catch(Exception e) {
			return Optional.empty();
		}
	}

	@Override
	public SubMenuInternalMaster getSubMenuInternalById(Long id) {
		
		return subMenuInternalRepo.findBySubMenuInternalId(id);
	}

	@Override
	public List<SubMenuInternalMaster> getAllSubMenuInternal() {
		
		return subMenuInternalRepo.findAll(Sort.by(Sort.Direction.DESC, "created"));
	}

	@Override
	public List<SubMenuInternalMaster> getAllActiveSubMenuInternal() {
		
		return subMenuInternalRepo.findAllActiveSubMenuInternal();
	}

	@Override
	public List<SubMenuInternalMaster> getAllInactiveSubMenuInternal() {
		
		return subMenuInternalRepo.findAllInActiveSubMenuInternal();
	}


	@Override
	public List<SubMenuInternalMaster> getAllSubMenusInternalBySubMenuId(Long submenuId) {
		
		return subMenuInternalRepo.findAllSubMenuInternalBySubMenuId(submenuId);
	}

	@Override
	public List<SubMenuInternalMaster> getAllActiveSubMenusInternalBySubMenuId(Long submenuId) {
		
		return subMenuInternalRepo.findAllActiveSubMenuInternalBySubMenuId(submenuId);
	}

	@Override
	public List<SubMenuInternalMaster> getAllInactiveSubMenusInternalBySubMenuId(Long submenuId) {
		
		return subMenuInternalRepo.findAllInActiveSubMenuInternalBySubMenuId(submenuId);
	}

	@Override
	public SubMenuInternalMaster getSubMenuInternalByPath(String path) {
		
		return subMenuInternalRepo.findSubMenuInternalByPath(path);
	}

	@Override
	public SubMenuInternalMaster getSubMenuInternalByNameAndSubMenuId(String name, Long submenuid) {
		
		return subMenuInternalRepo.findSubMenuInternalByNameAndSubMenuId(name, submenuid);
	}

	@Override
	public SubMenuInternalMaster getSubMenuInternalByPathAndSubMenuId(String path, Long submenuid) {
		
		return subMenuInternalRepo.findSubMenuInternalByPathAndSubMenuId(path, submenuid);
	}

	@Override
	public SubMenuInternalMaster getSubMenuInternalDetailsByPath(String pathName) {
		
		return subMenuInternalRepo.findSubMenuInternalDetailsByPath(pathName);
	}

}
