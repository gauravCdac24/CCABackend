package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.EsignDocTypeMaster;
import in.lms.cca.repository.EsignDocTypeMasterRepository;
import in.lms.cca.service.IEsignDocTypeMasterService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class EsignDocTypeMasterServiceImpl implements IEsignDocTypeMasterService{

	@Autowired
	private EsignDocTypeMasterRepository esignDocRepo;
	
	@Override
	public Optional<EsignDocTypeMaster> addEsignDocTypeMaster(EsignDocTypeMaster Obj) {
		
		if (Obj == null)
	        return Optional.empty();
		
	    try {
	    	
	    	EsignDocTypeMaster iobj = esignDocRepo.save(Obj);	
	    		return Optional.of(iobj);
	        
	    } catch (Exception e) {
	        return Optional.empty();
	    }
	}

	@Override
	public Optional<EsignDocTypeMaster> updateEsignDocTypeMaster(EsignDocTypeMaster Obj) {
		if (Obj == null)
	        return Optional.empty();
		
		if (Obj.getEsignDocTypeId() == null)
			return Optional.empty();
		
	    try {
	    	
	    	EsignDocTypeMaster iobj = esignDocRepo.save(Obj);	
	    		return Optional.of(iobj);
	        
	    } catch (Exception e) {
	        return Optional.empty();
	    }
	}

	@Override
	public List<EsignDocTypeMaster> getAllEsignDocTypeMaster() {
		return esignDocRepo.findAll(Sort.by(Sort.Direction.DESC, "created"));
	}

	@Override
	public List<EsignDocTypeMaster> getAllActiveEsignDocTypeMaster() {
		return esignDocRepo.findAllActiveEsignDocTypeMaster();
	}

	@Override
	public List<EsignDocTypeMaster> getAllInactiveEsignDocTypeMaster() {
		return esignDocRepo.findAllInactiveEsignDocTypeMaster();
	}

	@Override
	public EsignDocTypeMaster getEsignDocTypeMasterById(Long id) {
		
		return esignDocRepo.findEsignDocTypeMasterById(id);
	}

	@Override
	public EsignDocTypeMaster getByEsignDocType(String doctype) {
		
		return esignDocRepo.findByEsignDocType(doctype);
	}

}
