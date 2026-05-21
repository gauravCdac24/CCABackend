package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.ReviewDocuments;

public interface ReviewDocumentsRepository extends JpaRepository<ReviewDocuments, Long>{

	@Query("SELECT d FROM ReviewDocuments d WHERE d.reviewId.reviewId = :reviewId AND status='Active'")
	List<ReviewDocuments> getAllDetailsByReviewId(@Param("reviewId")Long reviewId);

}
