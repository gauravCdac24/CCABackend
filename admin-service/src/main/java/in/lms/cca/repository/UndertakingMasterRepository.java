package in.lms.cca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.UndertakingMasterEntity;

public interface UndertakingMasterRepository extends JpaRepository<UndertakingMasterEntity, Long> {

	
	@Query("From UndertakingMasterEntity a where a.undertakingId=:id")
	UndertakingMasterEntity getUndertakingMasterById(@Param("id")Long id);

	
}
