package in.lms.cca.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.AnnualAuditScheduleEntity;


public interface LicenseeAuditRepository  extends JpaRepository<AnnualAuditScheduleEntity, Long>{

	@Query("SELECT d FROM AnnualAuditScheduleEntity d WHERE d.userName = :id ORDER BY scheduleStartDate DESC")
	List<AnnualAuditScheduleEntity> findByUsername(@Param("id") String id);

	@Query("SELECT d FROM AnnualAuditScheduleEntity d WHERE d.userName = :id AND d.scheduleStartDate <= :currentDate ORDER BY d.scheduleStartDate DESC")
	List<AnnualAuditScheduleEntity> findStartedByUsername(@Param("id") String id, @Param("currentDate") Date currentDate);

	@Query("SELECT d FROM AnnualAuditScheduleEntity d WHERE d.scheduleStartDate <= :currentDate ORDER BY d.scheduleStartDate DESC")
	List<AnnualAuditScheduleEntity> findAllStarted(@Param("currentDate") Date currentDate);

	@Query("SELECT DISTINCT la.auditScheduleId FROM LicenseeAuditEntity la, AuditAgencySelectionEntity aas "
			+ "WHERE aas.licenseeAuditId = la AND aas.auditAgencyId = :agencyId AND aas.status = 'Active' "
			+ "AND la.status = 'Active' AND la.auditScheduleId.scheduleStartDate <= :currentDate "
			+ "ORDER BY la.auditScheduleId.scheduleStartDate DESC")
	List<AnnualAuditScheduleEntity> findStartedByAuditAgencyId(
			@Param("agencyId") String agencyId,
			@Param("currentDate") Date currentDate);

}
