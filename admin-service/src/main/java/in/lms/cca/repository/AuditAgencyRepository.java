package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.AuditAgency;
import jakarta.transaction.Transactional;

public interface AuditAgencyRepository extends JpaRepository<AuditAgency, Long>{

	//Delete
		@Modifying
		@Transactional
		@Query("DELETE FROM AuditAgency a WHERE a.auditAgencyId=:auditAgencyId")
		void deleteByAuditAgencyId(Long auditAgencyId);
				
		//Find all active Audit Agency
		@Query("FROM AuditAgency WHERE status='Active' order by created DESC")
		List<AuditAgency> findAllActiveAuditAgency();
		
		//Find all inactive Audit Agency
		@Query("FROM AuditAgency WHERE status='Inactive' order by created DESC")
		List<AuditAgency> findAllInActiveAuditAgency();
		
		//Get Audit Agency by Id
		@Query("FROM AuditAgency a WHERE a.auditAgencyId=:auditAgencyId")
		AuditAgency findAuditAgencyById(@Param("auditAgencyId")Long auditAgencyId);
		
		//Find Audit Agency by createdBy (user's encrypted username)
		@Query("FROM AuditAgency a WHERE a.createdBy=:createdBy AND a.status='Active'")
		AuditAgency findByCreatedBy(@Param("createdBy")String createdBy);
		
		//Find Audit Agency by agency name (matches user's first_name)
		@Query("FROM AuditAgency a WHERE a.agencyName=:agencyName AND a.status='Active'")
		AuditAgency findByAgencyName(@Param("agencyName")String agencyName);
	
}
