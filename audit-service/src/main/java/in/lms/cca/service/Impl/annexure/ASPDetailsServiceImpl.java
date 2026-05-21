package in.lms.cca.service.Impl.annexure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.annexure.ASPDetails;
import in.lms.cca.repository.annexure.ASPDetailsRepository;
import in.lms.cca.service.annexure.IASPDetailsService;


@Service
@Transactional
public class ASPDetailsServiceImpl implements IASPDetailsService{

	@Autowired
	private ASPDetailsRepository repo;
	
	@Override
	public ASPDetails getByASPDetailsId(Long id) {
		
		return repo.findByASPDetailsId(id);
	}

	@Override
	public ASPDetails addASPDetails(ASPDetails obj) {
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
	public ASPDetails getByAnnexureId(Long id) {
		
		return repo.findByAnnexureId(id);
	}


	
}












