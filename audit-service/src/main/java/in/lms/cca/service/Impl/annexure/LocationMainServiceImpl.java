
package in.lms.cca.service.Impl.annexure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.annexure.LocationMain;
import in.lms.cca.repository.annexure.LocationMainRepository;
import in.lms.cca.service.annexure.ILocationMainService;

@Service
@Transactional
public class LocationMainServiceImpl implements ILocationMainService {

	@Autowired
	private LocationMainRepository repo;
	
	@Override
	public LocationMain getByAnnexureId(Long annexureMainId) {
		
		return repo.findByAnnexureId(annexureMainId);
	}

	@Override
	public LocationMain addLocationMain(LocationMain obj) {
		
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








