package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.CCAStaff;

public interface CCAStaffRepository extends JpaRepository<CCAStaff, Long>{

	@Query("FROM CCAStaff a WHERE a.staffId=:staffId")
	CCAStaff findByCCAStaffId (@Param("staffId")Long staffId);

	@Query("FROM CCAStaff WHERE status = 'Active' ORDER BY created DESC")
	List<CCAStaff> findAllActiveCCAStaff();
	
	@Query("FROM CCAStaff WHERE status = 'Inactive' ORDER BY created DESC")
	List<CCAStaff> findAllInActiveCCAStaff();

	@Query("FROM CCAStaff a WHERE a.emailId=:emailId")
	CCAStaff findByCCAStaffEmailId(String emailId);
	
	@Query("FROM CCAStaff a WHERE a.mobileNo=:mobileNo")
	CCAStaff findByCCAStaffMobileNo(String mobileNo);
}
