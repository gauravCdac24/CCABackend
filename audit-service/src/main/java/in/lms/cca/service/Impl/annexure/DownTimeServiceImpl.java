
package in.lms.cca.service.Impl.annexure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.annexure.DownTime;
import in.lms.cca.repository.annexure.DownTimeRepository;
import in.lms.cca.service.annexure.IDownTimeService;

@Service
@Transactional
public class DownTimeServiceImpl implements IDownTimeService {

	@Autowired
	private DownTimeRepository repo;
	
	@Override
	public DownTime getByDownTimeId(Long id) {
		
		return repo.findByDownTimeId(id);
	}

	@Override
	public DownTime addDownTime(DownTime obj) {
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
	public DownTime getByAnnexureId(Long id) {
		
		return repo.findByAnnexureId(id);
	}


	
}