
package in.lms.cca.repository.annexure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.annexure.EKYCAccountBasedMain;

public interface EKYCAccountBasedMainRepository extends JpaRepository<EKYCAccountBasedMain, Long>{

	@Query("FROM EKYCAccountBasedMain a WHERE a.ekycAcMainId = :id")
	EKYCAccountBasedMain findByeKYCAcMainId(Long id);
	
	@Query("FROM EKYCAccountBasedMain a WHERE a.annexureMainId.annexureMainId = :id")
	EKYCAccountBasedMain findByAnnexureId(Long id);


	
}





