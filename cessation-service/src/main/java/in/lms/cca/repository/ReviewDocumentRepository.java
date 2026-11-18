package in.lms.cca.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.ReviewDocumentEntity;

public interface ReviewDocumentRepository extends JpaRepository<ReviewDocumentEntity, Long> {

	@Query("FROM ReviewDocumentEntity a WHERE a.cessationAppId = :cessationAppId AND a.status='Active'")
	Optional<ReviewDocumentEntity> getByCessationId(@Param("cessationAppId") Long cessationAppId);

	@Query("FROM ReviewDocumentEntity a WHERE a.cessationAppId = :cessationAppId AND a.status='Active'")
	List<ReviewDocumentEntity> getAllDatas(@Param("cessationAppId") Long cessationAppId);

	@Query("FROM ReviewDocumentEntity a WHERE a.cessationAppId = :cessationAppId ORDER BY a.created DESC")
	List<ReviewDocumentEntity> getAllData(@Param("cessationAppId") Long cessationAppId);

	@Query("FROM ReviewDocumentEntity a WHERE a.cessationAppId = :cessationAppId AND a.status='Active'")
	ReviewDocumentEntity getDataByCessationId(@Param("cessationAppId") Long cessationAppId);

}
