package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.EkycModeReview;

public interface EkycModeReviewRepository extends JpaRepository<EkycModeReview, Long>{

	@Query("FROM EkycModeReview a WHERE a.espReviewId.espReviewId = :id ORDER BY created DESC")
	List<EkycModeReview> findEkycModeReviewByReviewId(Long id);

}
