package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.AuditControl;
import jakarta.transaction.Transactional;

public interface AuditControlRepository extends JpaRepository<AuditControl, Long>{

			//Delete
			@Modifying
			@Transactional
			@Query("DELETE FROM AuditControl a WHERE a.auditControlId=:auditControlId")
			void deleteByAuditControlId(Long auditControlId);
			
			//Find By Audit Control Id
			@Query("FROM AuditControl a WHERE a.auditControlId=:auditControlId")
			AuditControl findByAuditControlId(Long auditControlId);
			
			//Find all active
			@Query("FROM AuditControl WHERE status='Active' ORDER BY created DESC")
			List<AuditControl> findAllActiveAuditControl();
			
			//Find all inactive
			@Query("FROM AuditControl WHERE status='Inactive' ORDER BY created DESC")
			List<AuditControl> findAllInActiveAuditControl();
			
			//Find By Parameter Id
			@Query("FROM AuditControl WHERE auditParameterId.auditParameterId=:auditParameterId ORDER BY created DESC")
			List<AuditControl> findAllAuditControlByParameterId(Long auditParameterId);
			@Query("FROM AuditControl WHERE auditParameterId.auditParameterId=:auditParameterId AND status='Active' ORDER BY created DESC")
			List<AuditControl> findAllActiveAuditControlByParameterId(Long auditParameterId);
			@Query("FROM AuditControl WHERE auditParameterId.auditParameterId=:auditParameterId AND status='Inactive' ORDER BY created DESC")
			List<AuditControl> findAllInActiveAuditControlByParameterId(Long auditParameterId);
			
			//Find By Audit Check Id
			@Query("FROM AuditControl WHERE auditCheckId.auditCheckId=:auditCheckId ORDER BY created DESC")
			List<AuditControl> findAllAuditControlByCheckId(Long auditCheckId);
			@Query("FROM AuditControl WHERE auditCheckId.auditCheckId=:auditCheckId AND status='Active' ORDER BY created DESC")
			List<AuditControl> findAllActiveAuditControlByCheckId(Long auditCheckId);
			@Query("FROM AuditControl WHERE auditCheckId.auditCheckId=:auditCheckId AND status='Inactive' ORDER BY created DESC")
			List<AuditControl> findAllInActiveAuditControlByCheckId(Long auditCheckId);

			//Find By Control Type
			@Query("FROM AuditControl WHERE auditControlTypeId.auditControlTypeId=:auditControlTypeId ORDER BY created DESC")
			List<AuditControl> findAllAuditControlByControlType(Long auditControlTypeId);
			@Query("FROM AuditControl WHERE auditControlTypeId.auditControlTypeId=:auditControlTypeId AND status='Active' ORDER BY created DESC")
			List<AuditControl> findAllActiveAuditControlByControlType(Long auditControlTypeId);
			@Query("FROM AuditControl WHERE auditControlTypeId.auditControlTypeId=:auditControlTypeId AND status='Inactive' ORDER BY created DESC")
			List<AuditControl> findAllInActiveAuditControlByControlType(Long auditControlTypeId);

			@Query("FROM AuditControl a WHERE a.controlDesc=:controlDesc")
			AuditControl findByAuditControlDesc(String controlDesc);

			
}
