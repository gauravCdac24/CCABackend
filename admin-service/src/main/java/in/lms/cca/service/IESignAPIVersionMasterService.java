package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.ESignAPIVersionMaster;

public interface IESignAPIVersionMasterService {

	Optional<ESignAPIVersionMaster> addESignAPIVersionMaster(ESignAPIVersionMaster Obj);
	Optional<ESignAPIVersionMaster> updateESignAPIVersionMaster(ESignAPIVersionMaster Obj);
	
	List<ESignAPIVersionMaster> getAllESignAPIVersionMaster();
	List<ESignAPIVersionMaster> getAllActiveESignAPIVersionMaster();
	List<ESignAPIVersionMaster> getAllInactiveESignAPIVersionMaster();
	
	ESignAPIVersionMaster getESignAPIVersionMasterById(Long id);
	
	List<ESignAPIVersionMaster> getAllESignAPIVersionMasterByApiSpecId(Long id);
	List<ESignAPIVersionMaster> getAllActiveESignAPIVersionMasterByApiSpecId(Long id);
	List<ESignAPIVersionMaster> getAllInactiveESignAPIVersionMasterByApiSpecId(Long id);
	
	ESignAPIVersionMaster getEsignAPIVersionByAPIVersionName(String apivername);
	
	
}
