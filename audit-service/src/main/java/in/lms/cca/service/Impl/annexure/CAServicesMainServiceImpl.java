
package in.lms.cca.service.Impl.annexure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.annexure.CAServicesMain;
import in.lms.cca.repository.annexure.CAServicesMainRepository;
import in.lms.cca.service.annexure.ICAServicesMainService;

@Service
@Transactional
public class CAServicesMainServiceImpl implements ICAServicesMainService {

	@Autowired
	private CAServicesMainRepository repo;
	
	@Override
	public CAServicesMain addCAServicesMain(CAServicesMain obj) {
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
	public CAServicesMain getByAnnexureId(Long annexureMainId) {
		
		return repo.findByAnnexureId(annexureMainId);
	}


	
}


