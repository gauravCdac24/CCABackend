package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


import in.lms.cca.entity.AuditCriteria;
import jakarta.transaction.Transactional;

public interface AuditCriteriaRepository extends JpaRepository<AuditCriteria, Long>{

			//Delete
			@Modifying
			@Transactional
			@Query("DELETE FROM AuditCriteria a WHERE a.auditCriteriaId=:auditCriteriaId")
			void deleteByAuditCriteriaId(Long auditCriteriaId);
			
			//Find By Audit Criteria Id
			@Query("FROM AuditCriteria a WHERE a.auditCriteriaId=:auditCriteriaId")
			AuditCriteria findByAuditCriteriaId(Long auditCriteriaId);
			
			//Find all active
			@Query("FROM AuditCriteria WHERE status='Active' ORDER BY created DESC")
			List<AuditCriteria> findAllActiveAuditCriteria();
			
			//Find all inactive
			@Query("FROM AuditCriteria WHERE status='Inactive' ORDER BY created DESC")
			List<AuditCriteria> findAllInActiveAuditCriteria();

			@Query("FROM AuditCriteria a WHERE a.auditCriteriaTitle=:auditCriteriaTitle")
			AuditCriteria findAuditCriteriaByTitle(String auditCriteriaTitle);
	
}
