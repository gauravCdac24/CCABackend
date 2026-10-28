
package in.lms.cca.repository.annexure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.annexure.InternalAuditDetails;

public interface InternalAuditDetailsRepository extends JpaRepository<InternalAuditDetails, Long>{

	@Query("FROM InternalAuditDetails a WHERE a.inAuditDetailsId = :id")
	InternalAuditDetails findByInternalAuditDetailsId(Long id);


	@Query("FROM InternalAuditDetails a WHERE a.inAuditMainId.inAuditMainId = :id ORDER BY inAuditDetailsId")
	List<InternalAuditDetails> findByInternalAuditMainId(Long id);

	
}