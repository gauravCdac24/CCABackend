package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.AuditControlEntity;
import jakarta.transaction.Transactional;

public interface AuditControlRepository extends JpaRepository<AuditControlEntity, Long> {

	@Query("SELECT d FROM AuditControlEntity d WHERE d.userName = :userName AND d.auditScheduleId = :auditScheduleId AND d.status = 'Active'")
	List<AuditControlEntity> findActiveByUserAndSchedule(@Param("userName") String userName,
			@Param("auditScheduleId") Long auditScheduleId);

	@Query("SELECT d FROM AuditControlEntity d WHERE d.userName = :userName AND d.status = 'Active'")
	List<AuditControlEntity> findActiveByUserName(@Param("userName") String userName);

	@Modifying
	@Transactional
	@Query("DELETE FROM AuditControlEntity d WHERE d.controlId = :controlId AND d.userName = :userName AND d.auditScheduleId = :auditScheduleId")
	void deleteByControlUserAndSchedule(@Param("controlId") Integer controlId, @Param("userName") String userName,
			@Param("auditScheduleId") Long auditScheduleId);
}
