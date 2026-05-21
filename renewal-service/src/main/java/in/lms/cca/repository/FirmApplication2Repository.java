package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.FirmApplication2;

public interface FirmApplication2Repository extends JpaRepository<FirmApplication2, Long> {
	
	
	@Query("SELECT d FROM FirmApplication2 d WHERE d.intentAppId.intentAppId = :intentAppId")
	FirmApplication2 findIntentAppById(@Param("intentAppId")Long intentAppId);

	@Query("SELECT d FROM FirmApplication2 d WHERE d.intentAppId.intentAppId = :intentAppId")
	List<FirmApplication2> findIntentWithoutStatusAppById(@Param("intentAppId")Long intentAppId);
	

}
