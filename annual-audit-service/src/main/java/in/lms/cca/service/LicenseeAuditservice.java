package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.AnnualAuditScheduleEntity;

public interface LicenseeAuditservice {

	Optional<AnnualAuditScheduleEntity> addAnnualAudit(AnnualAuditScheduleEntity newAnnualAuditScheduleEntity);

	List<AnnualAuditScheduleEntity> getAllAnnualAuditSchedule();

	List<AnnualAuditScheduleEntity> getAnnualAuditDetailsByUsername(String id);

	List<AnnualAuditScheduleEntity> getStartedAnnualAuditDetailsByUsername(String id);

	List<AnnualAuditScheduleEntity> getAllStartedAnnualAuditSchedules();

	List<AnnualAuditScheduleEntity> getStartedAnnualAuditSchedulesByAuditAgencyId(String auditAgencyId);

	Optional<AnnualAuditScheduleEntity> getAnnualAuditScheduleById(Long auditScheduleId);

	Optional<AnnualAuditScheduleEntity> updateScheduleStatus(Long auditScheduleId, String status);

	Optional<AnnualAuditScheduleEntity> updateScheduleStatusByUserName(String userName, String status);

}
