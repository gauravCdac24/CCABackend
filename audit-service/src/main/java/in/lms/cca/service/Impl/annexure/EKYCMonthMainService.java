
package in.lms.cca.service.Impl.annexure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.annexure.EKYCMonthMain;
import in.lms.cca.repository.annexure.EKYCMonthMainRepository;
import in.lms.cca.service.annexure.IEKYCMonthMainService;

@Service
@Transactional
public class EKYCMonthMainService implements IEKYCMonthMainService{

	@Autowired
	private EKYCMonthMainRepository repo;
	
	@Override
	public EKYCMonthMain addEKYCMonthMainn(EKYCMonthMain obj) {
		
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
	public EKYCMonthMain getByAnnexureId(Long annexureMainId) {
		
		return repo.findByAnnexureId(annexureMainId);
	}


	
}




