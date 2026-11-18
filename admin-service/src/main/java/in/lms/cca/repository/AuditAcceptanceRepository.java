package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.AuditAcceptance;
import jakarta.transaction.Transactional;

public interface AuditAcceptanceRepository extends JpaRepository<AuditAcceptance, Long> {

	@Query("FROM AuditAcceptance a WHERE a.auditAcceptanceId=:auditAcceptanceId")
	AuditAcceptance findByAuditAcceptanceId(@Param("auditAcceptanceId") Long auditAcceptanceId);

	@Query("FROM AuditAcceptance WHERE status = 'Active' ORDER BY created DESC")
	List<AuditAcceptance> findAllActive();

	@Query("FROM AuditAcceptance WHERE status = 'Inactive' ORDER BY created DESC")
	List<AuditAcceptance> findAllInactive();

	@Modifying
	@Transactional
	@Query("DELETE FROM AuditAcceptance c WHERE c.auditAcceptanceId=:auditAcceptanceId")
	void deleteByAuditAcceptanceId(Long auditAcceptanceId);
}
