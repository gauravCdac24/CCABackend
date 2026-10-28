package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.ReviewFields;

public interface ReviewFieldsRepository extends JpaRepository<ReviewFields, Long> {

	
	@Query("SELECT d FROM ReviewFields d WHERE d.reviewId.reviewId = :reviewId AND status='Active'")
	List<ReviewFields> getAllDetailsByReviewId(@Param("reviewId")Long reviewId);

}
