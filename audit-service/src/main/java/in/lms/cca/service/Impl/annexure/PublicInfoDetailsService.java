
package in.lms.cca.service.Impl.annexure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.annexure.PublicInfoDetails;
import in.lms.cca.repository.annexure.PublicInfoDetailsRepository;
import in.lms.cca.service.annexure.IPublicInfoDetailsService;

@Service
@Transactional
public class PublicInfoDetailsService implements IPublicInfoDetailsService{

	@Autowired
	private PublicInfoDetailsRepository repo;
	
	@Override
	public PublicInfoDetails addPublicInfoDetails(PublicInfoDetails obj) {
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
	public PublicInfoDetails getByPublicInfoDetailsId(Long id) {
		
		return repo.findByPublicInfoDetailsId(id);
	}

	@Override
	public List<PublicInfoDetails> getByPublicInfoMainId(Long id) {
		
		return repo.findByPublicInfoMainId(id);
	}


	
}







