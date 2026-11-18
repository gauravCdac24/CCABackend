
package in.lms.cca.service.Impl.annexure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.annexure.CAServicesDetails;
import in.lms.cca.repository.annexure.CAServicesDetailsRepository;
import in.lms.cca.service.annexure.ICAServicesDetailsService;

@Service
@Transactional
public class CAServicesDetailsServiceImpl implements ICAServicesDetailsService{

	@Autowired
	private CAServicesDetailsRepository repo;
	
	@Override
	public CAServicesDetails addCAServicesDetails(CAServicesDetails obj) {
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
	public CAServicesDetails getByCAServicesDetailsId(Long id) {
		
		return repo.getByCAServicesDetailsId(id);
	}

	@Override
	public List<CAServicesDetails> getByCAServicesMainId(Long id) {
		
		return repo.getByCAServicesMainId(id);
	}


	
}



