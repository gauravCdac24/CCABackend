package in.lms.cca.service.Impl.annexure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.annexure.AnnualAuditPeriodDetails;
import in.lms.cca.repository.annexure.AnnualAuditPeriodDetailsRepository;
import in.lms.cca.service.annexure.IAnnualAuditPeriodDetailsService;

@Service
@Transactional
public class AnnualAuditPeriodDetailsServiceImpl implements IAnnualAuditPeriodDetailsService{

	@Autowired
	private AnnualAuditPeriodDetailsRepository repo;
	
	@Override
	public AnnualAuditPeriodDetails addAnnualAuditPeriodDetails(AnnualAuditPeriodDetails obj) {
		
	    if (obj == null) {
	        throw new IllegalArgumentException("Input object cannot be null");
	    }

	    try {
	        return repo.save(obj);
	    } catch (Exception e) {
	        throw new RuntimeException("Failed to save AnnualAuditPeriodDetails object", e);
	    }
	}

	@Override
	public AnnualAuditPeriodDetails getByAnnualAuditPeriodId(Long id) {
		
		return repo.getByAnnualAuditPeriodId(id);
	}

	@Override
	public List<AnnualAuditPeriodDetails> getByAnnualAuditPeriodMainId(Long periodMainId) {
		
		return repo.getByAnnualAuditPeriodMainId(periodMainId);
	}


	
}
