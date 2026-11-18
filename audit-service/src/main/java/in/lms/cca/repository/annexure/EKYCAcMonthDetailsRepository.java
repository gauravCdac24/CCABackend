
package in.lms.cca.repository.annexure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.annexure.EKYCAcMonthDetails;

public interface EKYCAcMonthDetailsRepository extends JpaRepository<EKYCAcMonthDetails, Long>{

	@Query("FROM EKYCAcMonthDetails a WHERE a.eKYCAcMonthId = :id")
	EKYCAcMonthDetails findByeKYCAcMonthId(Long id);

	@Query("FROM EKYCAcMonthDetails a WHERE a.eKYCMonthMainId.eKYCMonthMainId = :id ORDER BY eKYCAcMonthId")
	List<EKYCAcMonthDetails> findByEKYCAcMonthMainId(Long id);


	
}