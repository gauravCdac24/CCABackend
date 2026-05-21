
package in.lms.cca.service.Impl.annexure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.annexure.EKYCAcMonthDetails;
import in.lms.cca.repository.annexure.EKYCAcMonthDetailsRepository;
import in.lms.cca.service.annexure.IEKYCAcMonthDetailsService;

@Service
@Transactional
public class EKYCAcMonthDetailsServiceImpl implements IEKYCAcMonthDetailsService {

	@Autowired
	private EKYCAcMonthDetailsRepository repo;
	
	@Override
	public EKYCAcMonthDetails getByeKYCAcMonthId(Long id) {
		
		return repo.findByeKYCAcMonthId(id);
	}

	@Override
	public EKYCAcMonthDetails addEKYCAcMonthDetails(EKYCAcMonthDetails obj) {
		if (obj == null) {
	        throw new IllegalArgumentException("Input object cannot be null");
	    }

	    try {
	        return repo.save(obj);
	    } catch (Exception e) {
	        throw new RuntimeException("Failed to save  object", e);
	    }
	}

	@Override
	public List<EKYCAcMonthDetails> getByEKYCAcMonthMainId(Long id) {
		
		return repo.findByEKYCAcMonthMainId(id);
	}


	
}