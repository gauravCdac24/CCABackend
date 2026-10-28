package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.AppUndertakingEntity;

public interface AppUndertakingRepository extends JpaRepository<AppUndertakingEntity, Long> {

	@Query("SELECT d FROM AppUndertakingEntity d WHERE d.intentAppId.intentAppId = :intentAppId")
	List<AppUndertakingEntity> findByIntentAppId(@Param("intentAppId")Long intentAppId);


}
