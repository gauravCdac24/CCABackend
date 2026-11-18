
package in.lms.cca.service.Impl.annexure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.annexure.CertificateCost;
import in.lms.cca.repository.annexure.CertificateCostRepository;
import in.lms.cca.service.annexure.ICertificateCostService;

@Service
@Transactional
public class CertificateCostServiceImpl implements ICertificateCostService{

	@Autowired
	private CertificateCostRepository repo;
	
	@Override
	public CertificateCost getByCertificateCostId(Long id) {
		
		return repo.findByCertificateCostId(id);
	}

	@Override
	public CertificateCost addCertificateCost(CertificateCost obj) {
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
	public CertificateCost getByAnnexureId(Long id) {
		
		return repo.findByAnnexureId(id);
	}


	
}















