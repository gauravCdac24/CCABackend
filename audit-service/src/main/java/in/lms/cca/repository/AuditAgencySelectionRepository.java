package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.ApplicationAuditEntity;
import in.lms.cca.entity.AuditAgencySelectionEntity;
import in.lms.cca.payload.AuditAgencySelectionDTO;

public interface AuditAgencySelectionRepository extends JpaRepository<AuditAgencySelectionEntity, Long> {

	@Query("SELECT d FROM AuditAgencySelectionEntity d WHERE d.appAuditId=:appAuditId AND d.status='Active'")
	AuditAgencySelectionEntity findbyAuditId(@Param("appAuditId")ApplicationAuditEntity appAuditId);

	@Query("SELECT d FROM AuditAgencySelectionEntity d WHERE d.appAuditId.appAuditId=:appAuditId")
	List<AuditAgencySelectionEntity> getAllAuditId(@Param("appAuditId")Long appAuditId);

	@Query("SELECT d FROM AuditAgencySelectionEntity d WHERE d.auditAgencyId=:userId AND d.status='Active'")
	List<AuditAgencySelectionEntity> getAllDetailsByAuditId(@Param("userId")String userId);

}
