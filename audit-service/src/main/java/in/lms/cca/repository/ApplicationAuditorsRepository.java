package in.lms.cca.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.ApplicationAuditorsEntity;

public interface ApplicationAuditorsRepository extends JpaRepository<ApplicationAuditorsEntity, Long> {

	@Query("SELECT d FROM ApplicationAuditorsEntity d WHERE d.appAuditId.appAuditId = :appAuditId AND d.status='Active'")
	List<ApplicationAuditorsEntity> getAllDataByAuditId(@Param("appAuditId")Long appAuditId);

	@Query("SELECT d FROM ApplicationAuditorsEntity d WHERE d.appAuditorId = :id AND d.status='Active'")
	Optional<ApplicationAuditorsEntity> downloadfile(@Param("id")Long id);

	
	@Query("SELECT d FROM ApplicationAuditorsEntity d WHERE d.status='Active'")
	List<ApplicationAuditorsEntity> getAllActiveActiveAuditor();

	@Query("SELECT d FROM ApplicationAuditorsEntity d WHERE d.undertakingDocument = :id AND d.status='Active'")
	Optional<ApplicationAuditorsEntity> downloadfiles(@Param("id")String id);

}
