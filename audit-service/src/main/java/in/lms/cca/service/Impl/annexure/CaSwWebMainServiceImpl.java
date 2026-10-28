
package in.lms.cca.service.Impl.annexure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.annexure.CaSwWebMain;
import in.lms.cca.repository.annexure.CaSwWebMainRepository;
import in.lms.cca.service.annexure.ICaSwWebMainService;

@Service
@Transactional
public class CaSwWebMainServiceImpl implements ICaSwWebMainService{

	@Autowired
	private CaSwWebMainRepository repo;
	
	@Override
	public CaSwWebMain addCaSwWebMain(CaSwWebMain obj) {
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
	public CaSwWebMain getByAnnexureId(Long annexureMainId) {
		
		return repo.findByAnnexureId(annexureMainId);
	}


	
}



