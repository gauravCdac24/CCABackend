package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.ReviewedApplication;

public interface ReviewedApplicationRepository extends JpaRepository<ReviewedApplication, Long>{

	@Query("FROM ReviewedApplication a WHERE a.espReviewId.espReviewId = :id ORDER BY created DESC")
	List<ReviewedApplication> findReviewedApplicationByReviewId(Long id);

}
