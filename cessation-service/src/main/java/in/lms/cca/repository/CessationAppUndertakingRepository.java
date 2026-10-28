package in.lms.cca.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.CessationAppUndertakingEntity;

public interface CessationAppUndertakingRepository extends JpaRepository<CessationAppUndertakingEntity, Long> {

	@Query("FROM CessationAppUndertakingEntity a WHERE a.cessationAppId = :cessationAppId AND a.status='Active'")
	List<CessationAppUndertakingEntity> getAllByCessationId(@Param("cessationAppId")String cessationAppId);

	@Query("FROM CessationAppUndertakingEntity a WHERE a.status='Active'")
	List<CessationAppUndertakingEntity> getAllByUndertaking();

	@Query("FROM CessationAppUndertakingEntity a WHERE a.cessationAppId = :cessationAppId AND a.status='Active'")
	List<CessationAppUndertakingEntity> getAllByUndertakingByCessationId(@Param("cessationAppId")String cessationAppId);

	@Query("FROM CessationAppUndertakingEntity a WHERE a.appUndertakingId = :id AND a.status='Active'")
	Optional<CessationAppUndertakingEntity> downloadfile(@Param("id")Long id);

	@Query("FROM CessationAppUndertakingEntity a WHERE a.undertakingId = :undertakingId AND a.status='Active'")
	CessationAppUndertakingEntity getAllDataByUndertakingId(@Param("undertakingId")String undertakingId);

}
