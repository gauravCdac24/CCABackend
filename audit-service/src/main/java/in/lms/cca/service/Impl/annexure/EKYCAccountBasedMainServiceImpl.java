
package in.lms.cca.service.Impl.annexure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.annexure.EKYCAccountBasedMain;
import in.lms.cca.repository.annexure.EKYCAccountBasedMainRepository;
import in.lms.cca.service.annexure.IEKYCAccountBasedMainService;

@Service
@Transactional
public class EKYCAccountBasedMainServiceImpl implements IEKYCAccountBasedMainService {

	@Autowired
	private EKYCAccountBasedMainRepository repo;
	
	@Override
	public EKYCAccountBasedMain getByeKYCAcMainId(Long id) {
		
		return repo.findByeKYCAcMainId(id);
	}

	@Override
	public EKYCAccountBasedMain addeKYCAcMain(EKYCAccountBasedMain obj) {
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
	public EKYCAccountBasedMain getByAnnexureId(Long id) {
		
		return repo.findByAnnexureId(id);
	}


	
}





