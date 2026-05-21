package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.AuditSubCriteria;
import jakarta.transaction.Transactional;

public interface AuditSubCriteriaRepository extends JpaRepository<AuditSubCriteria, Long>{

	    //Delete
		@Modifying
		@Transactional
		@Query("DELETE FROM AuditSubCriteria a WHERE a.auditSubCriteriaId=:auditSubCriteriaId")
		void deleteByAuditSubCriteriaId(Long auditSubCriteriaId);
		
		//Delete By Audit Criteria
		@Modifying
		@Transactional
		@Query("DELETE FROM AuditSubCriteria a WHERE a.auditCriteriaId.auditCriteriaId=:auditCriteriaId")
		void deleteByAuditCriteriaId(Long auditCriteriaId);
		
		//Find By Audit Sub Criteria Id
		@Query("FROM AuditSubCriteria a WHERE a.auditSubCriteriaId=:auditSubCriteriaId")
		AuditSubCriteria findByAuditSubCriteriaId(Long auditSubCriteriaId);
		
		//Find By Audit Criteria Id
		@Query("FROM AuditSubCriteria a WHERE a.auditCriteriaId.auditCriteriaId=:auditCriteriaId ORDER BY created DESC")
		List<AuditSubCriteria> findAllByAuditCriteriaId(Long auditCriteriaId);

		@Query("FROM AuditSubCriteria a WHERE a.auditCriteriaId.auditCriteriaId=:auditCriteriaId AND status='Active' ORDER BY created DESC")
		List<AuditSubCriteria> findAllActiveByAuditCriteriaId(Long auditCriteriaId);

		@Query("FROM AuditSubCriteria a WHERE a.auditCriteriaId.auditCriteriaId = :auditCriteriaId "
				+ "AND LOWER(TRIM(a.status)) = 'active' AND a.isVisible = true ORDER BY created DESC")
		List<AuditSubCriteria> findAllEnabledByAuditCriteriaId(Long auditCriteriaId);

		@Query("FROM AuditSubCriteria a WHERE LOWER(TRIM(a.status)) = 'active' AND a.isVisible = true ORDER BY created DESC")
		List<AuditSubCriteria> findAllEnabledForAuditorView();

		@Query("FROM AuditSubCriteria a WHERE a.auditCriteriaId.auditCriteriaId=:auditCriteriaId AND  status='Inactive' ORDER BY created DESC")
		List<AuditSubCriteria> findAllInactiveByAuditCriteriaId(Long auditCriteriaId);

		
		//Find all active
		@Query("FROM AuditSubCriteria WHERE status='Active' ORDER BY created DESC")
		List<AuditSubCriteria> findAllActiveAuditSubCriteria();
		
		//Find all inactive
		@Query("FROM AuditSubCriteria WHERE status='Inactive' ORDER BY created DESC")
		List<AuditSubCriteria> findAllInActiveAuditSubCriteria();

		@Query("FROM AuditSubCriteria a WHERE a.auditSubCriteriaTitle=:auditSubCriteriaTitle")
		AuditSubCriteria findAuditSubCriteriaByTitle(String auditSubCriteriaTitle);

	
}
