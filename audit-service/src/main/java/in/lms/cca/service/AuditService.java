package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.dto.AuditRequestDTO;
import in.lms.cca.entity.AuditControlEntity;

public interface AuditService {

	Optional<AuditControlEntity> save(AuditControlEntity control);

	List<AuditControlEntity> getAuditControlsByUser(String userName);

	void deleteByControlId(int idToRemove);

	

}
