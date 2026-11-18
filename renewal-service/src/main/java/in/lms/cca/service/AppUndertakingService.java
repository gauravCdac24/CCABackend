package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.AppUndertakingEntity;

public interface AppUndertakingService {

	Optional<AppUndertakingEntity> addData(AppUndertakingEntity appUndertakingEntity);

	List<AppUndertakingEntity> findByIntentAppId(Long intentAppId);

	Optional<AppUndertakingEntity> updateData(AppUndertakingEntity existingUndertaking);

}
