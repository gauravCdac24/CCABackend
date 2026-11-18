package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.AuditAgencyMobile;
import jakarta.transaction.Transactional;

public interface AuditAgencyMobileRepository extends JpaRepository<AuditAgencyMobile, Long>{

	//Delete
	@Modifying
	@Transactional
	@Query("DELETE FROM AuditAgencyMobile a WHERE a.auditAgencyId.auditAgencyId=:auditAgencyId")
	void deleteMobileByAuditAgencyId(Long auditAgencyId);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM AuditAgencyMobile a WHERE a.agencyMobileId=:agencyMobileId")
	void deleteByAuditAgencyMobileId(Long agencyMobileId);
			
	//Find all active 
	@Query("FROM AuditAgencyMobile WHERE status='Active' order by created DESC")
	List<AuditAgencyMobile> findAllActiveAuditAgencyMobile();
	
	//Find all inactive 
	@Query("FROM AuditAgencyMobile WHERE status='Inactive' order by created DESC")
	List<AuditAgencyMobile> findAllInActiveAuditAgencyMobile();
	
	//Get Audit Agency Mobile by Id
	@Query("FROM AuditAgencyMobile a WHERE a.agencyMobileId=:agencyMobileId")
	AuditAgencyMobile findAuditAgencyMobileById(Long agencyMobileId);
	
	//Get All Mobile By Audit Agency Id
	@Query("FROM AuditAgencyMobile a WHERE a.auditAgencyId.auditAgencyId=:auditAgencyId order by created DESC")
	List<AuditAgencyMobile> findAllAuditAgencyMobileByAuditAgencyId(Long auditAgencyId);
	
	//Get All Active Mobile By Audit Agency Id
	@Query("FROM AuditAgencyMobile a WHERE a.auditAgencyId.auditAgencyId=:auditAgencyId AND status='Active' order by created DESC")
	List<AuditAgencyMobile> findAllActiveAuditAgencyMobileByAuditAgencyId(Long auditAgencyId);
	
	//Get All Inactive Mobile By Audit Agency Id
	@Query("FROM AuditAgencyMobile a WHERE a.auditAgencyId.auditAgencyId=:auditAgencyId AND status='Inactive' order by created DESC")
	List<AuditAgencyMobile> findAllInactiveAuditAgencyMobileByAuditAgencyId(Long auditAgencyId);

	@Query("FROM AuditAgencyMobile a WHERE a.auditAgencyId.auditAgencyId=:auditAgencyId")
	List<AuditAgencyMobile> findByAuditAgencyId(@Param("auditAgencyId")Long auditAgencyId);
	
}
