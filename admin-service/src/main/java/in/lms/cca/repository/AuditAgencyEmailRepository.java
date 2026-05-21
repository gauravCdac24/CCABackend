package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.AuditAgencyEmail;
import jakarta.transaction.Transactional;

public interface AuditAgencyEmailRepository extends JpaRepository<AuditAgencyEmail, Long>{

			//Delete
			@Modifying
			@Transactional
			@Query("DELETE FROM AuditAgencyEmail a WHERE a.auditAgencyId.auditAgencyId=:auditAgencyId")
			void deleteEmailByAuditAgencyId(Long auditAgencyId);
			
			@Modifying
			@Transactional
			@Query("DELETE FROM AuditAgencyEmail a WHERE a.agencyEmailId=:agencyEmailId")
			void deleteByAuditAgencyEmailId(Long agencyEmailId);
					
			//Find all active 
			@Query("FROM AuditAgencyEmail WHERE status='Active' order by created DESC")
			List<AuditAgencyEmail> findAllActiveAuditAgencyEmail();
			
			//Find all inactive 
			@Query("FROM AuditAgencyEmail WHERE status='Inactive' order by created DESC")
			List<AuditAgencyEmail> findAllInActiveAuditAgencyEmail();
			
			//Get Audit Agency Email by Id
			@Query("FROM AuditAgencyEmail a WHERE a.agencyEmailId=:agencyEmailId")
			AuditAgencyEmail findAuditAgencyEmailById(@Param("agencyEmailId")Long agencyEmailId);
			
			//Get All Email By Audit Agency Id
			@Query("FROM AuditAgencyEmail a WHERE a.auditAgencyId.auditAgencyId=:auditAgencyId order by created DESC")
			List<AuditAgencyEmail> findAllAuditAgencyEmailByAuditAgencyId(Long auditAgencyId);
			
			//Get All Active Email By Audit Agency Id
			@Query("FROM AuditAgencyEmail a WHERE a.auditAgencyId.auditAgencyId=:auditAgencyId AND status='Active' order by created DESC")
			List<AuditAgencyEmail> findAllActiveAuditAgencyEmailByAuditAgencyId(Long auditAgencyId);
			
			//Get All Inactive Email By Audit Agency Id
			@Query("FROM AuditAgencyEmail a WHERE a.auditAgencyId.auditAgencyId=:auditAgencyId AND status='Inactive' order by created DESC")
			List<AuditAgencyEmail> findAllInactiveAuditAgencyEmailByAuditAgencyId(Long auditAgencyId);

			@Query("FROM AuditAgencyEmail a WHERE a.auditAgencyId.auditAgencyId=:auditAgencyId")
			List<AuditAgencyEmail> findByAuditAgencyId(@Param("auditAgencyId")Long auditAgencyId);
	
}			
