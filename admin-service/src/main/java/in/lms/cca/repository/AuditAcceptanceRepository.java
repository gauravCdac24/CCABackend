package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.lms.cca.entity.AuditAcceptance;

public interface AuditAcceptanceRepository extends JpaRepository<AuditAcceptance, Long> {

	List<AuditAcceptance> findByStatusOrderByAuditAcceptanceIdDesc(String status);

	List<AuditAcceptance> findAllByOrderByAuditAcceptanceIdDesc();

	boolean existsByVersionIgnoreCase(String version);

	boolean existsByVersionIgnoreCaseAndAuditAcceptanceIdNot(String version, Long auditAcceptanceId);

	boolean existsByStatusAndAuditAcceptanceIdNot(String status, Long auditAcceptanceId);
}
