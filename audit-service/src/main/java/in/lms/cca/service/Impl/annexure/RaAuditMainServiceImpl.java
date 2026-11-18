
package in.lms.cca.service.Impl.annexure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.annexure.RaAuditMain;
import in.lms.cca.repository.annexure.RaAuditMainRepository;
import in.lms.cca.service.annexure.IRaAuditMainService;

@Service
@Transactional
public class RaAuditMainServiceImpl implements IRaAuditMainService {

	@Autowired
	private RaAuditMainRepository repo;
	
	@Override
	public RaAuditMain getByRaAuditMainId(Long id) {
		
		return repo.findByRaAuditMainId(id);
	}

	@Override
	public RaAuditMain addRaAuditMain(RaAuditMain obj) {
		if (obj == null) {
	        throw new IllegalArgumentException("Input object cannot be null");
	    }

	    try {
	        return repo.save(obj);
	    } catch (Exception e) {
	        throw new RuntimeException("Failed to save object", e);
	    }
	}

	@Override
	public RaAuditMain getByAnnexureId(Long id) {
		
		return repo.findByAnnexureId(id);
		
	}


	
}








