package in.lms.cca.service;

import java.util.Optional;

import in.lms.cca.entity.AuditNCSEntity;

public interface AuditNCSService {

	Optional<AuditNCSEntity> saveData(AuditNCSEntity auditNCSEntity);

	AuditNCSEntity findByAuditId(Long appAuditId);

	Optional<AuditNCSEntity> downloadfileBydDocumentName(String decrypt);

}
