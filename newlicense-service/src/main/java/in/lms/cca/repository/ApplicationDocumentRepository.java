package in.lms.cca.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.ApplicationDocument;

public interface ApplicationDocumentRepository extends JpaRepository<ApplicationDocument, Long> {

	@Query("SELECT d FROM ApplicationDocument d WHERE d.intentAppId.intentAppId = :intentAppId")
	List<ApplicationDocument> findByindivApplicationId(@Param("intentAppId")Long intentAppId);

	@Query("SELECT d FROM ApplicationDocument d WHERE d.intentAppId.intentAppId = :intentAppId")
	List<ApplicationDocument> findIntentAppById(@Param("intentAppId")Long intentAppId);

	@Query("SELECT d FROM ApplicationDocument d WHERE d.appDocId = :id")
	Optional<ApplicationDocument> downloadfile(@Param("id")Long id);

	@Query("SELECT d FROM ApplicationDocument d WHERE d.intentAppId.intentAppId = :intentAppId")
	List<ApplicationDocument> findIntentWithoutStatusAppById(@Param("intentAppId")Long intentAppId);

	@Query("SELECT d FROM ApplicationDocument d WHERE d.appDocId = :appDocId")
	Optional<ApplicationDocument> findByAppDocId(@Param("appDocId")String appDocId);

	@Query("SELECT d FROM ApplicationDocument d WHERE d.intentAppId.intentAppId = :intentAppId AND d.status ='Active'")
	List<ApplicationDocument> findIntentWithStatusAppById(@Param("intentAppId")Long intentAppId);

}
