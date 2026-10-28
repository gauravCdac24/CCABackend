package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.EKYCModeMaster;
import in.lms.cca.repository.EKYCModeMasterRepository;
import in.lms.cca.service.IEKYCModeMasterService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class EKYCModeMasterServiceImpl implements IEKYCModeMasterService{

	@Autowired
	private EKYCModeMasterRepository ekycRepo;
	
	@Override
	public Optional<EKYCModeMaster> addEKYCModeMaster(EKYCModeMaster Obj) {
		if (Obj == null)
	        return Optional.empty();
		
	    try {
	    	
	    	EKYCModeMaster iobj = ekycRepo.save(Obj);	
	    		return Optional.of(iobj);
	        
	    } catch (Exception e) {
	        return Optional.empty();
	    }
	}

	@Override
	public Optional<EKYCModeMaster> updateEKYCModeMaster(EKYCModeMaster Obj) {
		if (Obj == null)
	        return Optional.empty();
		
		if (Obj.getEkycModeId() == null)
			return Optional.empty();
		
	    try {
	    	
	    	EKYCModeMaster iobj = ekycRepo.save(Obj);	
	    		return Optional.of(iobj);
	        
	    } catch (Exception e) {
	        return Optional.empty();
	    }
	}

	@Override
	public List<EKYCModeMaster> getAllEKYCModeMaster() {
		return ekycRepo.findAll(Sort.by(Sort.Direction.DESC, "created"));
	}

	@Override
	public List<EKYCModeMaster> getAllActiveEKYCModeMaster() {
		
		return ekycRepo.findAllActiveEKYCModeMaster();
	}

	@Override
	public List<EKYCModeMaster> getAllInactiveEKYCModeMaster() {
		return ekycRepo.findAllInactiveEKYCModeMaster();
	}

	@Override
	public EKYCModeMaster getEKYCModeMasterById(Long id) {
		return ekycRepo.findEKYCModeMasterById(id);
	}

	@Override
	public EKYCModeMaster getByEKYCModeTitle(String title) {
		return ekycRepo.findByEKYCModeTitle(title);
	}

}
