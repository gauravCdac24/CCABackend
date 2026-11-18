package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.ReviewCessationDocumentEntity;
import in.lms.cca.entity.ReviewDocumentEntity;

public interface ReviewCessationDocumentRepository extends JpaRepository<ReviewCessationDocumentEntity, Long> {

	@Query("FROM ReviewCessationDocumentEntity a WHERE a.reviewId.reviewId = :reviewId AND a.status='Active'")
	List<ReviewCessationDocumentEntity> getByRewievId(@Param("reviewId")Long reviewId);

	@Query("FROM ReviewCessationDocumentEntity a WHERE a.reviewId = :documentEntity AND a.status='Active'")
	List<ReviewCessationDocumentEntity> getAllDatas(@Param("documentEntity")ReviewDocumentEntity documentEntity);

	@Query("FROM ReviewCessationDocumentEntity a WHERE a.reviewId = :documentEntity")
	List<ReviewCessationDocumentEntity> getAllData(@Param("documentEntity")ReviewDocumentEntity documentEntity);

}
