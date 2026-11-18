package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.UndertakingMasterEntity;

public interface UndertakingMasterService {

	Optional<UndertakingMasterEntity> addUnderTakingMaster(UndertakingMasterEntity undertakingMasterEntity);

	List<UndertakingMasterEntity> getAllUndertakingMasterEntity();

	UndertakingMasterEntity getUndertakingMasterById(Long id);

	Optional<UndertakingMasterEntity> updateUnderTakingMaster(UndertakingMasterEntity undertakingMasterEntity);

}
