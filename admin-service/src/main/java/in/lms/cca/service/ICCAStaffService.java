package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.CCAStaff;

public interface ICCAStaffService {

	Optional<CCAStaff> addCCAStaff(CCAStaff obj);
	Optional<CCAStaff> updateCCAStaff(CCAStaff obj);
	List<CCAStaff> getAllCCAStaff();
	List<CCAStaff> getAllActiveCCAStaff();
	List<CCAStaff> getAllInactiveCCAStaff();
	CCAStaff getCCAStaffById(Long id);
	CCAStaff getCCAStaffByEmailId(String emailId);
	CCAStaff getCCAStaffByMobileNo(String mobileNo);
	
}
