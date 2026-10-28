package in.lms.cca.service.Impl.annexure;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.annexure.AnnualAuditPeriodMain;
import in.lms.cca.repository.annexure.AnnualAuditPeriodMainRepository;
import in.lms.cca.service.annexure.IAnnualAuditPeriodMainService;

@Service
@Transactional
public class AnnualAuditPeriodMainServiceImpl implements IAnnualAuditPeriodMainService{

	@Autowired
	private AnnualAuditPeriodMainRepository repo;
	
	@Override
	public AnnualAuditPeriodMain addAnnualAuditPeriodMain(AnnualAuditPeriodMain obj) throws IllegalArgumentException, RuntimeException {
	    if (obj == null) {
	        throw new IllegalArgumentException("Input object cannot be null");
	    }

	    try {
	        return repo.save(obj);
	    } catch (Exception e) {
	        throw new RuntimeException("Failed to save AnnualAuditPeriodMain object", e);
	    }
	}

	@Override
	public AnnualAuditPeriodMain getByAnnexureId(Long annexureMainId) {
		
		return repo.findByAnnexureId(annexureMainId);
	}



	
}







