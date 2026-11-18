package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.EsignAPISpecificationMaster;
import in.lms.cca.repository.ESignAPISpecificationMasterRepository;
import in.lms.cca.service.IEsignAPISpecificationMasterService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class EsignAPISpecificationMasterServiceImpl implements IEsignAPISpecificationMasterService{

	@Autowired
	private ESignAPISpecificationMasterRepository apiSpecRepo;
	
	@Override
	public Optional<EsignAPISpecificationMaster> addEsignAPISpecificationMaster(EsignAPISpecificationMaster Obj) {
		if (Obj == null)
	        return Optional.empty();
		
	    try {
	    	
	    	EsignAPISpecificationMaster iobj = apiSpecRepo.save(Obj);	
	    		return Optional.of(iobj);
	        
	    } catch (Exception e) {
	        return Optional.empty();
	    }
	}

	@Override
	public Optional<EsignAPISpecificationMaster> updateEsignAPISpecificationMaster(EsignAPISpecificationMaster Obj) {
		if (Obj == null)
	        return Optional.empty();
		
		if (Obj.getEsignApiSpecId() == null)
			return Optional.empty();
		
	    try {
	    	
	    	EsignAPISpecificationMaster iobj = apiSpecRepo.save(Obj);	
	    		return Optional.of(iobj);
	        
	    } catch (Exception e) {
	        return Optional.empty();
	    }
	}

	@Override
	public List<EsignAPISpecificationMaster> getAllEsignAPISpecificationMaster() {
		return apiSpecRepo.findAll(Sort.by(Sort.Direction.DESC, "created"));
	}

	@Override
	public List<EsignAPISpecificationMaster> getAllActiveEsignAPISpecificationMaster() {
		
		return apiSpecRepo.findAllActiveEsignAPISpecificationMaster();
	}

	@Override
	public List<EsignAPISpecificationMaster> getAllInactiveEsignAPISpecificationMaster() {
		return apiSpecRepo.findAllInActiveEsignAPISpecificationMaster();
	}

	@Override
	public EsignAPISpecificationMaster getEsignAPISpecificationMasterById(Long id) {
		
		return apiSpecRepo.findByEsignAPISpecificationMasterId(id);
		
	}

	@Override
	public EsignAPISpecificationMaster getByEsignAPISpecification(String apiSpecification) {
		
		return apiSpecRepo.findByEsignAPISpecification(apiSpecification);
	}
	

}
