package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.AuditCheck;
import jakarta.transaction.Transactional;

public interface AuditCheckRepository extends JpaRepository<AuditCheck, Long>{

		//Delete
		@Modifying
		@Transactional
		@Query("DELETE FROM AuditCheck a WHERE a.auditCheckId=:auditCheckId")
		void deleteByAuditCheckId(Long auditCheckId);
		
		//Find By Audit Check Id
		@Query("FROM AuditCheck a WHERE a.auditCheckId=:auditCheckId")
		AuditCheck findByAuditCheckId(Long auditCheckId);
		
		//Find all active
		@Query("FROM AuditCheck WHERE status='Active' ORDER BY created DESC")
		List<AuditCheck> findAllActiveAuditCheck();
		
		//Find all inactive
		@Query("FROM AuditCheck WHERE status='Inactive' ORDER BY created DESC")
		List<AuditCheck> findAllInActiveAuditCheck();

		@Query("FROM AuditCheck a WHERE a.auditCheckDesc=:auditCheckDesc")
		AuditCheck findAuditCheckByDesc(String auditCheckDesc);
		
}
