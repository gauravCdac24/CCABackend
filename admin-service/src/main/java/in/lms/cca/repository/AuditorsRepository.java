package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.Auditors;

public interface AuditorsRepository extends JpaRepository<Auditors, Long>{

	@Query("FROM Auditors a WHERE a.auditorsId=:auditorsId")
	Auditors findByAuditorsId (@Param("auditorsId")Long auditorsId);
	
	@Query("FROM Auditors a WHERE a.auditAgencyId.auditAgencyId=:auditAgencyId")
	List<Auditors> findAuditorsByAuditAgencyId (Long auditAgencyId);
	
	@Query("FROM Auditors WHERE status = 'Active' ORDER BY created DESC")
	List<Auditors> findAllActiveAuditors();
	
	@Query("FROM Auditors WHERE status = 'Inactive' ORDER BY created DESC")
	List<Auditors> findAllInActiveAuditors();

	@Query("FROM Auditors a WHERE a.auditAgencyId.auditAgencyId=:auditAgencyId")
	List<Auditors> findByAuditAgencyId(@Param("auditAgencyId")Long auditAgencyId);
}
