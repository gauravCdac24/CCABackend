
package in.lms.cca.service.Impl.annexure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.annexure.RevocationMain;
import in.lms.cca.repository.annexure.RevocationMainRepository;
import in.lms.cca.service.annexure.IRevocationMainService;

@Service
@Transactional
public class RevocationMainService implements IRevocationMainService {

	@Autowired
	private RevocationMainRepository repo;
	
	@Override
	public RevocationMain getByRevocationMainId(Long id) {
		
		return repo.findByRevocationMainId(id);
	}

	@Override
	public RevocationMain addRevocationMain(RevocationMain obj) {
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
	public RevocationMain getByAnnexureId(Long id) {
		
		return repo.findByAnnexureId(id);
	}


	
}






