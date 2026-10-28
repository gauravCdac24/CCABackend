
package in.lms.cca.service.Impl.annexure;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import in.lms.cca.entity.annexure.CATrustedPersonMain;
import in.lms.cca.repository.annexure.CATrustedPersonMainRepository;
import in.lms.cca.service.annexure.ICATrustedPersonMainService;


@Service
@Transactional
public class CATrustedPersonMainServiceImpl implements ICATrustedPersonMainService {

	@Autowired
	private CATrustedPersonMainRepository repo;

	

	@Override
	public CATrustedPersonMain addCATrustedPersonMain(CATrustedPersonMain obj) {
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
	public CATrustedPersonMain getByAnnexureId(Long id) {
		
		return repo.findByAnnexureId(id);
	}

	
	
	
	

	
}












