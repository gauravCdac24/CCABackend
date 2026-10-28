
package in.lms.cca.service.Impl.annexure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.annexure.CourtCasesMain;
import in.lms.cca.repository.annexure.CourtCasesMainRepository;
import in.lms.cca.service.annexure.ICourtCasesMainService;

@Service
@Transactional
public class CourtCasesMainServiceImpl implements ICourtCasesMainService{

	@Autowired
	private CourtCasesMainRepository repo;
	
	@Override
	public CourtCasesMain getByCourtCasesMainId(Long id) {
		
		return repo.findByCourtCasesMainId(id);
	}

	@Override
	public CourtCasesMain addCourtCasesMain(CourtCasesMain obj) {
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
	public CourtCasesMain getByAnnexureId(Long id) {
		
		return repo.findByAnnexureId(id);
	}


	
}

