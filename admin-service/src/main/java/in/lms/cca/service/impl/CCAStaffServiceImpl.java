package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.CCAStaff;
import in.lms.cca.repository.CCAStaffRepository;
import in.lms.cca.service.ICCAStaffService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class CCAStaffServiceImpl implements ICCAStaffService{

	@Autowired
	private CCAStaffRepository repo;
	
	@Override
	public Optional<CCAStaff> addCCAStaff(CCAStaff obj) {
		if (obj == null)
	        return Optional.empty();
		
	    try {
	    	
	    	CCAStaff iobj = repo.save(obj);	
	    		return Optional.of(iobj);
	        
	    } catch (Exception e) {
	        return Optional.empty();
	    }
	}

	@Override
	public Optional<CCAStaff> updateCCAStaff(CCAStaff obj) {
		if (obj == null)
	        return Optional.empty();
		
		if (obj.getStaffId() == null)
			return Optional.empty();
		
	    try {
	    	
	    	CCAStaff iobj = repo.save(obj);	
	    		return Optional.of(iobj);
	        
	    } catch (Exception e) {
	        return Optional.empty();
	    }
	}

	@Override
	public List<CCAStaff> getAllCCAStaff() {
		
		return repo.findAll(Sort.by(Sort.Direction.DESC, "created"));
	}

	@Override
	public List<CCAStaff> getAllActiveCCAStaff() {
		
		return repo.findAllActiveCCAStaff();
	}

	@Override
	public List<CCAStaff> getAllInactiveCCAStaff() {

		return repo.findAllActiveCCAStaff();
	}

	@Override
	public CCAStaff getCCAStaffById(Long id) {

		return repo.findByCCAStaffId(id);
	}

	@Override
	public CCAStaff getCCAStaffByEmailId(String emailId) {
		
		return repo.findByCCAStaffEmailId(emailId);
	}

	@Override
	public CCAStaff getCCAStaffByMobileNo(String mobileNo) {
		
		return repo.findByCCAStaffMobileNo(mobileNo);
	}

}
