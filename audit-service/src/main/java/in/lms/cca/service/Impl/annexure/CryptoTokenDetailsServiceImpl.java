
package in.lms.cca.service.Impl.annexure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.annexure.CryptoTokenDetails;
import in.lms.cca.repository.annexure.CryptoTokenDetailsRepository;
import in.lms.cca.service.annexure.ICryptoTokenDetailsService;

@Service
@Transactional
public class CryptoTokenDetailsServiceImpl implements ICryptoTokenDetailsService {

	@Autowired
	private CryptoTokenDetailsRepository repo;
	
	@Override
	public CryptoTokenDetails addCryptoTokenDetails(CryptoTokenDetails obj) {
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
	public CryptoTokenDetails getByCryptoTokenDetailsId(Long id) {
		
		return repo.getByCryptoTokenDetailsId(id);
	}

	@Override
	public List<CryptoTokenDetails> getByCryptoTokenMainId(Long id) {

		return repo.getByCryptoTokenMainId(id);
	}


	
}