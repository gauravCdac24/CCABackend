package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.AuditControlType;
import jakarta.transaction.Transactional;

public interface AuditControlTypeRepository extends JpaRepository<AuditControlType, Integer>{

		//Delete
		@Modifying
		@Transactional
		@Query("DELETE FROM AuditControlType a WHERE a.auditControlTypeId=:auditControlTypeId")
		void deleteByAuditControlTypeId(Integer auditControlTypeId);
		
		@Query("FROM AuditControlType a WHERE a.auditControlTypeId=:auditControlTypeId")
		AuditControlType findByAuditControlTypeId(Integer auditControlTypeId);
		
		@Query("FROM AuditControlType WHERE status='Active' ORDER BY created DESC")
		List<AuditControlType> findAllActiveAuditControlType();
		
		@Query("FROM AuditControlType WHERE status='Inactive' ORDER BY created DESC")
		List<AuditControlType> findAllInactiveAuditControlType();

		@Query("FROM AuditControlType a WHERE a.auditControlDesc=:auditControlDesc")
		AuditControlType findAuditControlTypeByDesc(String auditControlDesc);
		
	
}
