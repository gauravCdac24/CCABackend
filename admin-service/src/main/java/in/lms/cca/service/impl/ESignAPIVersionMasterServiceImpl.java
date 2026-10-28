package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.ESignAPIVersionMaster;
import in.lms.cca.repository.ESignAPIVersionMasterRepository;
import in.lms.cca.service.IESignAPIVersionMasterService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ESignAPIVersionMasterServiceImpl implements IESignAPIVersionMasterService{

	@Autowired
	private ESignAPIVersionMasterRepository verRepo;
	
	
	@Override
	public Optional<ESignAPIVersionMaster> addESignAPIVersionMaster(ESignAPIVersionMaster Obj) {
		
		if (Obj == null)
	        return Optional.empty();
		
	    try {
	    	
	    	ESignAPIVersionMaster iobj = verRepo.save(Obj);	
	    		return Optional.of(iobj);
	        
	    } catch (Exception e) {
	        return Optional.empty();
	    }
	    
	}

	@Override
	public Optional<ESignAPIVersionMaster> updateESignAPIVersionMaster(ESignAPIVersionMaster Obj) {
	
		if (Obj == null)
	        return Optional.empty();
		
		if (Obj.getEsignApiVerId() == null)
			return Optional.empty();
		
	    try {
	    	
	    	ESignAPIVersionMaster iobj = verRepo.save(Obj);	
	    		return Optional.of(iobj);
	        
	    } catch (Exception e) {
	        return Optional.empty();
	    }
	}

	@Override
	public List<ESignAPIVersionMaster> getAllESignAPIVersionMaster() {
	
		return verRepo.findAll(Sort.by(Sort.Direction.DESC, "created"));
	}

	@Override
	public List<ESignAPIVersionMaster> getAllActiveESignAPIVersionMaster() {
	
		return verRepo.findAllActiveESignAPIVersionMaster();
	}

	@Override
	public List<ESignAPIVersionMaster> getAllInactiveESignAPIVersionMaster() {
	
		return verRepo.findAllInactiveESignAPIVersionMaster();
	}

	@Override
	public ESignAPIVersionMaster getESignAPIVersionMasterById(Long id) {
	
		return verRepo.findESignAPIVersionMasterById(id);
	}

	@Override
	public List<ESignAPIVersionMaster> getAllESignAPIVersionMasterByApiSpecId(Long id) {
	
		return verRepo.findAllESignAPIVersionMasterByApiSpecId(id);
	}

	@Override
	public List<ESignAPIVersionMaster> getAllActiveESignAPIVersionMasterByApiSpecId(Long id) {
	
		return verRepo.findAllActiveESignAPIVersionMasterByApiSpecId(id);
	}

	@Override
	public List<ESignAPIVersionMaster> getAllInactiveESignAPIVersionMasterByApiSpecId(Long id) {
	
		return verRepo.findAllInactiveESignAPIVersionMasterByApiSpecId(id);
	}

	@Override
	public ESignAPIVersionMaster getEsignAPIVersionByAPIVersionName(String apivername) {
	
		return verRepo.findEsignAPIVersionByAPIVersionName(apivername);
	}

}
