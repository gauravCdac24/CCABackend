package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.AnnualAuditScheduleEntity;

public interface LicenseeAuditservice {

	Optional<AnnualAuditScheduleEntity> addAnnualAudit(AnnualAuditScheduleEntity newAnnualAuditScheduleEntity);

	List<AnnualAuditScheduleEntity> getAllAnnualAuditSchedule();

	List<AnnualAuditScheduleEntity> getAnnualAuditDetailsByUsername(String id);

}
