package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.ApplicationAuditEntity;
import in.lms.cca.entity.AuditShortComingsEntity;

public interface AuditShortComingsService {

	Optional<AuditShortComingsEntity> saveData(AuditShortComingsEntity auditShortComingsEntity);

	AuditShortComingsEntity getByAuditId(ApplicationAuditEntity applicationAuditEntity);

	AuditShortComingsEntity getActiveByAppAuditId(Long appAuditId);

	Optional<AuditShortComingsEntity> updateData(AuditShortComingsEntity documentEntity);

	List<AuditShortComingsEntity> findByAuditsId(Long appAuditId);

}
