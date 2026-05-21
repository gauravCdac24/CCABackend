package in.lms.cca.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.AuditScheduleEntity;

public interface AuditScheduleRepository extends JpaRepository<AuditScheduleEntity, Long> {

    @Query("SELECT d FROM AuditScheduleEntity d WHERE d.licenseeAuditId.LicenseeAuditId = :appAuditId AND d.status = 'Active'")
    List<AuditScheduleEntity> getAllDataByAuditId(@Param("appAuditId") Long appAuditId);


	@Query("SELECT d FROM AuditScheduleEntity d WHERE d.auditScheduleId = :id AND d.status='Active'")
	Optional<AuditScheduleEntity> downloadfile(@Param("id")Long id);

}
