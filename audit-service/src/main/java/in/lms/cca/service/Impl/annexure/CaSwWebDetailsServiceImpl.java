
package in.lms.cca.service.Impl.annexure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.annexure.CaSwWebDetails;
import in.lms.cca.repository.annexure.CaSwWebDetailsRepository;
import in.lms.cca.service.annexure.ICaSwWebDetailsService;

@Service
@Transactional
public class CaSwWebDetailsServiceImpl implements ICaSwWebDetailsService{

	@Autowired
	private CaSwWebDetailsRepository repo;
	
	@Override
	public CaSwWebDetails addCaSwWebDetails(CaSwWebDetails obj) {
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
	public CaSwWebDetails getByCaSwWebDetailsId(Long id) {
		
		return repo.getByCaSwWebDetailsId(id);
	}

	@Override
	public List<CaSwWebDetails> getByCaSwWebMainId(Long id) {
		
		return repo.getByCaSwWebMainId(id);
	}


	
}
