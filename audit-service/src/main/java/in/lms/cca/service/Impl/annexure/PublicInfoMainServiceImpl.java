
package in.lms.cca.service.Impl.annexure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.annexure.PublicInfoMain;
import in.lms.cca.repository.annexure.PublicInfoMainRepository;
import in.lms.cca.service.annexure.IPublicInfoMainService;

@Service
@Transactional
public class PublicInfoMainServiceImpl implements IPublicInfoMainService {

	@Autowired
	private PublicInfoMainRepository repo;
	
	
	@Override
	public PublicInfoMain addPublicInfoMain(PublicInfoMain obj) {
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
	public PublicInfoMain getByAnnexureId(Long annexureMainId) {
		return repo.findByAnnexureId(annexureMainId);
	}


	
}







