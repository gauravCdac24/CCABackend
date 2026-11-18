package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.ApplicationAuditEntity;
import in.lms.cca.entity.AuditScheduleEntity;

public interface AuditScheduleService {

	Optional<AuditScheduleEntity> saveData(AuditScheduleEntity auditScheduleEntity);

	List<AuditScheduleEntity> getAllDataByAuditId(Long appAuditId);

	Optional<AuditScheduleEntity> getAllData(Long id);

	Optional<AuditScheduleEntity> aprovedData(AuditScheduleEntity auditScheduleEntity);

	Optional<AuditScheduleEntity> updateData(AuditScheduleEntity scheduleEntity);

}
