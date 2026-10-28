
package in.lms.cca.service.Impl.annexure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.annexure.SelfAssessmentMain;
import in.lms.cca.repository.annexure.SelfAssessmentMainRepository;
import in.lms.cca.service.annexure.ISelfAssessmentMainService;

@Service
@Transactional
public class SelfAssessmentMainServiceImpl implements ISelfAssessmentMainService{

	@Autowired
	private SelfAssessmentMainRepository repo;
	
	@Override
	public SelfAssessmentMain getBySelfAssessmentMainId(Long id) {
		
		return repo.findBySelfAssessmentMainId(id);
	}

	@Override
	public SelfAssessmentMain addSelfAssessmentMain(SelfAssessmentMain obj) {
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
	public SelfAssessmentMain getByAnnexureId(Long id) {
		
		return repo.findByAnnexureId(id);
	}


	
}







