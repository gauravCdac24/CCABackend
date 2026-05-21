package in.lms.cca.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.ApplicationAuditorsEntity;
import in.lms.cca.entity.AuditReportCriteriaEntity;
import in.lms.cca.entity.LicenseeAuditEntity;

public interface AuditReportCriteriaRepository extends JpaRepository<AuditReportCriteriaEntity, Long> {

	@Query("SELECT d FROM AuditReportCriteriaEntity d WHERE d.status='Active'")
	List<AuditReportCriteriaEntity> findAllActiveData();

	@Query("SELECT d FROM AuditReportCriteriaEntity d WHERE d.document=:documentName AND d.status='Active'")
	Optional<AuditReportCriteriaEntity> downloadfileBydDocumentName(@Param("documentName")String documentName);

	@Query("SELECT d FROM AuditReportCriteriaEntity d WHERE d.licenseeAuditId=:appAuditId AND d.status='Active'")
	List<AuditReportCriteriaEntity> findByAuditId(@Param("appAuditId")Long appAuditId);

	@Query("SELECT d FROM AuditReportCriteriaEntity d WHERE d.auditControlId=:controlId AND d.licenseeAuditId=:applicationAuditEntity AND d.status='Active'")
	AuditReportCriteriaEntity getAllDataByControlId(@Param("controlId")String controlId, @Param("applicationAuditEntity")LicenseeAuditEntity applicationAuditEntity);

}
