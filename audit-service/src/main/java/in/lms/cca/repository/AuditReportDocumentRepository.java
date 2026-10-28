package in.lms.cca.repository;

import java.util.List;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.AuditReportDocumentEntity;

public interface AuditReportDocumentRepository extends JpaRepository<AuditReportDocumentEntity, Long> {

	@Query("SELECT d FROM AuditReportDocumentEntity d WHERE d.appAuditId.appAuditId = :appAuditId AND d.status = 'Active'")
	AuditReportDocumentEntity getActiveByAppAuditId(@Param("appAuditId") Long appAuditId);

	@Query("SELECT d FROM AuditReportDocumentEntity d WHERE d.appAuditId IS NULL ORDER BY d.criteriaDocId DESC")
	List<AuditReportDocumentEntity> findAllWithNullAppAuditId();

	@Query("SELECT d FROM AuditReportDocumentEntity d WHERE d.appAuditId IS NULL AND d.criteriaDocId IN :criteriaDocIds")
	List<AuditReportDocumentEntity> findOrphansByCriteriaDocIds(@Param("criteriaDocIds") List<Long> criteriaDocIds);

	@Query("SELECT d FROM AuditReportDocumentEntity d WHERE d.appAuditId.appAuditId=:appAuditId")
	List<AuditReportDocumentEntity> findByAuditId(@Param("appAuditId")Long appAuditId);

	@Query("SELECT d FROM AuditReportDocumentEntity d WHERE d.appAuditId.appAuditId=:appAuditId AND d.status='Active'")
	AuditReportDocumentEntity findByAuditsId(@Param("appAuditId")Long appAuditId);

}
