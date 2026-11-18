package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.ReviewESPApplication;

public interface ReviewESPApplicationRepository extends JpaRepository<ReviewESPApplication, Long>{

	@Query("FROM ReviewESPApplication a WHERE a.status = :status ORDER BY created DESC")
	List<ReviewESPApplication> findReviewESPApplicationByStatus(String status);
	
	@Query("FROM ReviewESPApplication a WHERE a.esignLicenseeAppId.esignLicenseeAppId = :id AND a.status = :status ORDER BY created DESC")
	List<ReviewESPApplication> findReviewESPApplicationByStatusAndId(Long id, String status);

}
