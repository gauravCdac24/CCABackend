
package in.lms.cca.service.Impl.annexure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.annexure.CATrustedPerson;
import in.lms.cca.repository.annexure.CATrustedPersonRepository;
import in.lms.cca.service.annexure.ICATrustedPersonService;

@Service
@Transactional
public class CATrustedPersonServiceImpl implements ICATrustedPersonService {

	@Autowired
	private CATrustedPersonRepository repo;
	
	@Override
	public CATrustedPerson getByCATrustedPersonId(Long id) {
		
		return repo.findByCATrustedPersonId(id);
	}

	@Override
	public CATrustedPerson addCATrustedPerson(CATrustedPerson obj) {
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
	public List<CATrustedPerson> getByCATrustedPersonMainId(Long id) {
		
		return repo.findByCATrustedPersonMainId(id);
	}


	
}












