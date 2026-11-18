
package in.lms.cca.service.Impl.annexure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.annexure.InternalAuditMain;
import in.lms.cca.repository.annexure.InternalAuditMainRepository;
import in.lms.cca.service.annexure.IInternalAuditMainService;

@Service
@Transactional
public class InternalAuditMainServiceImpl implements IInternalAuditMainService{

	@Autowired
	private InternalAuditMainRepository repo;
	
	@Override
	public InternalAuditMain getByAnnexureId(Long annexureMainId) {
		
		return repo.getByAnnexureId(annexureMainId);
	}

	@Override
	public InternalAuditMain addInternalAuditMain(InternalAuditMain obj) throws IllegalArgumentException, RuntimeException{
		if (obj == null) {
	        throw new IllegalArgumentException("Input object cannot be null");
	    }

	    try {
	        return repo.save(obj);
	    } catch (Exception e) {
	        throw new RuntimeException("Failed to save  object", e);
	    }
	}


	
}








