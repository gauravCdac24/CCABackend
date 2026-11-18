package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.AuditShortComingsEntity;
import in.lms.cca.entity.LicenseeAuditEntity;

public interface AuditShortComingsService {

	Optional<AuditShortComingsEntity> saveData(AuditShortComingsEntity auditShortComingsEntity);

	AuditShortComingsEntity getByAuditId(LicenseeAuditEntity applicationAuditEntity);

	Optional<AuditShortComingsEntity> updateData(AuditShortComingsEntity documentEntity);

	List<AuditShortComingsEntity> findByAuditsId(Long appAuditId);

}
