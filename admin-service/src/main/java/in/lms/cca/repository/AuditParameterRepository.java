package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.AuditParameter;
import jakarta.transaction.Transactional;

public interface AuditParameterRepository extends JpaRepository<AuditParameter, Long>{

	//Delete
	@Modifying
	@Transactional
	@Query("DELETE FROM AuditParameter a WHERE a.auditParameterId=:auditParameterId")
	void deleteByAuditParameterId(Long auditParameterId);
	
	//Delete By Sub Criteria
	@Modifying
	@Transactional
	@Query("DELETE FROM AuditParameter a WHERE a.auditSubCriteriaId.auditSubCriteriaId=:auditSubCriteriaId")
	void deleteByAuditSubCriteriaId(Long auditSubCriteriaId);
	
	//Find By Audit Parameter Id
	@Query("FROM AuditParameter a WHERE a.auditParameterId=:auditParameterId")
	AuditParameter findByAuditParameterId(Long auditParameterId);
	
	//Find By Audit Sub Criteria Id
	@Query("FROM AuditParameter a WHERE a.auditSubCriteriaId.auditSubCriteriaId=:auditSubCriteriaId ORDER BY created DESC")
	List<AuditParameter> findAllByAuditSubCriteriaId(Long auditSubCriteriaId);

	@Query("FROM AuditParameter a WHERE a.auditSubCriteriaId.auditSubCriteriaId=:auditSubCriteriaId AND status='Active' ORDER BY created DESC")
	List<AuditParameter> findAllActiveByAuditSubCriteriaId(Long auditSubCriteriaId);

	@Query("FROM AuditParameter a WHERE a.auditSubCriteriaId.auditSubCriteriaId=:auditSubCriteriaId AND status='Inactive' ORDER BY created DESC")
	List<AuditParameter> findAllInactiveByAuditSubCriteriaId(Long auditSubCriteriaId);

	
	//Find all active
	@Query("FROM AuditParameter WHERE status='Active' ORDER BY created DESC")
	List<AuditParameter> findAllActiveAuditParameter();
	
	//Find all inactive
	@Query("FROM AuditParameter WHERE status='Inactive' ORDER BY created DESC")
	List<AuditParameter> findAllInActiveAuditParameter();

	@Query("FROM AuditParameter a WHERE a.auditParameterTitle=:auditParameterTitle")
	AuditParameter findByAuditParameterTitle(String auditParameterTitle);

	
}
