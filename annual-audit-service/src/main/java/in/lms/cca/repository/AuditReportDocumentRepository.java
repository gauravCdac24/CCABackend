package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import in.lms.cca.entity.AuditReportDocumentEntity;
import in.lms.cca.entity.LicenseeAuditEntity;

public interface AuditReportDocumentRepository extends JpaRepository<AuditReportDocumentEntity, Long> {

	@Query("SELECT d FROM AuditReportDocumentEntity d WHERE d.licenseeAuditId=:applicationAuditEntity AND d.status='Active'")
	AuditReportDocumentEntity getByAuditId(@Param("applicationAuditEntity")LicenseeAuditEntity applicationAuditEntity);

	@Query("SELECT d FROM AuditReportDocumentEntity d WHERE d.licenseeAuditId=:appAuditId")
	List<AuditReportDocumentEntity> findByAuditId(@Param("appAuditId")Long appAuditId);

	@Query("SELECT d FROM AuditReportDocumentEntity d WHERE d.licenseeAuditId=:appAuditId AND d.status='Active'")
	AuditReportDocumentEntity findByAuditsId(@Param("appAuditId")Long appAuditId);

}
