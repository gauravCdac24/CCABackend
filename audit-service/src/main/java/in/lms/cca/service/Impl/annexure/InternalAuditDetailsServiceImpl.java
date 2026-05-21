
package in.lms.cca.service.Impl.annexure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.annexure.InternalAuditDetails;
import in.lms.cca.repository.annexure.InternalAuditDetailsRepository;
import in.lms.cca.service.annexure.IInternalAuditDetailsService;

@Service
@Transactional
public class InternalAuditDetailsServiceImpl  implements IInternalAuditDetailsService{

	@Autowired
	private InternalAuditDetailsRepository repo;
	
	@Override
	public InternalAuditDetails getByInternalAuditDetailsId(Long id) {
		
		return repo.findByInternalAuditDetailsId(id);
	}

	@Override
	public InternalAuditDetails addInternalAuditDetails(InternalAuditDetails obj) {
		
		if (obj == null) {
	        throw new IllegalArgumentException("Input object cannot be null");
	    }

	    try {
	        return repo.save(obj);
	    } catch (Exception e) {
	        throw new RuntimeException("Failed to save  object", e);
	    }
	}

	@Override
	public List<InternalAuditDetails> getByInternalAuditMainId(Long id) {
		
		return repo.findByInternalAuditMainId(id);
	}

	


	
}