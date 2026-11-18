
package in.lms.cca.service.Impl.annexure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.annexure.CryptoTokenMain;
import in.lms.cca.repository.annexure.CryptoTokenMainRepository;
import in.lms.cca.service.annexure.ICryptoTokenMainService;

@Service
@Transactional
public class CryptoTokenMainServiceImpl implements ICryptoTokenMainService{

	@Autowired
	private CryptoTokenMainRepository repo;
	
	@Override
	public CryptoTokenMain addCryptoTokenMain(CryptoTokenMain obj) {
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
	public CryptoTokenMain getByAnnexureId(Long annexureMainId) {
		
		return repo.findByAnnexureId(annexureMainId);
	}


	
}


