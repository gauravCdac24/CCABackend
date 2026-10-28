
package in.lms.cca.service.Impl.annexure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.annexure.LocationDetails;
import in.lms.cca.repository.annexure.LocationDetailsRepository;
import in.lms.cca.service.annexure.ILocationDetailsService;

@Service
@Transactional
public class LocationDetailsServiceImpl implements ILocationDetailsService{

	@Autowired
	private LocationDetailsRepository repo;
	
	@Override
	public LocationDetails getByLocationDetailsId(Long id) {
		
		return repo.findByLocationDetailsId(id);
	}

	@Override
	public LocationDetails addLocationDetails(LocationDetails obj) {
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
	public List<LocationDetails> getByLocationMainId(Long id) {
		
		return repo.findByLocationMainId(id);
	}


	
}






