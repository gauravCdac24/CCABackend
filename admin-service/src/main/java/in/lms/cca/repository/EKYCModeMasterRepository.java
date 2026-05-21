package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.EKYCModeMaster;


public interface EKYCModeMasterRepository  extends JpaRepository<EKYCModeMaster, Long>{

	@Query("FROM EKYCModeMaster WHERE status = 'Active' ORDER BY created DESC")
	List<EKYCModeMaster> findAllActiveEKYCModeMaster();

	@Query("FROM EKYCModeMaster WHERE status = 'Inactive' ORDER BY created DESC")
	List<EKYCModeMaster> findAllInactiveEKYCModeMaster();

	@Query("FROM EKYCModeMaster a WHERE a.ekycModeId=:ekycModeId")
	EKYCModeMaster findEKYCModeMasterById(Long ekycModeId);

	@Query("FROM EKYCModeMaster a WHERE a.ekycModeTitle=:ekycModeTitle")
	EKYCModeMaster findByEKYCModeTitle(String ekycModeTitle);

}
