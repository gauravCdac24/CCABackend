package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.CessationAppUndertakingEntity;

public interface CessationAppUndertakingService {

	Optional<CessationAppUndertakingEntity> save(CessationAppUndertakingEntity entity);

	List<CessationAppUndertakingEntity> getAllByCessationId(String cessationAppId);
	
	Optional<CessationAppUndertakingEntity> updateData(CessationAppUndertakingEntity entity);

	List<CessationAppUndertakingEntity> getAllByUndertaking();

	List<CessationAppUndertakingEntity> getAllByUndertakingByCessationId(String cessationAppId);

	Optional<CessationAppUndertakingEntity> downloadfile(Long id);

	CessationAppUndertakingEntity getAllDataByUndertakingId(String undertakingId);

}
