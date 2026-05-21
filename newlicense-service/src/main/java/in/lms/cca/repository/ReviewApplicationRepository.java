package in.lms.cca.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.IntentApplication;
import in.lms.cca.entity.ReviewApplication;

public interface ReviewApplicationRepository extends JpaRepository<ReviewApplication, Long> {

	@Query("SELECT d FROM ReviewApplication d WHERE d.intentAppId = :intentApp AND d.status='Active'")
	List<ReviewApplication> findByIntentId(@Param("intentApp")IntentApplication intentApp);

	@Query("SELECT d FROM ReviewApplication d")
	List<ReviewApplication> findAllActiveData();

	@Query("SELECT d FROM ReviewApplication d WHERE d.intentAppId = :intentApp AND d.status='Active'")
	ReviewApplication findByIntentsId(@Param("intentApp")IntentApplication intentApp);

	@Query("SELECT d FROM ReviewApplication d WHERE d.intentAppId = :intentApp")
	List<ReviewApplication> findByIntentesId(@Param("intentApp")IntentApplication intentApp);

	@Query("SELECT d FROM ReviewApplication d WHERE d.momFile = :fileName")
	Optional<ReviewApplication> downloadfile(@Param("fileName")String fileName);


}
