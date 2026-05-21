package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.AuditControlEntity;
import jakarta.transaction.Transactional;


public interface AuditRepository extends JpaRepository<AuditControlEntity, Long>{

	@Modifying
	@Transactional
	@Query("DELETE  FROM AuditControlEntity d WHERE d.controlId = :idToRemove")
	void deleteByControlId(@Param("idToRemove")int idToRemove);

	@Query("SELECT d FROM AuditControlEntity d WHERE d.userName = :userName AND d.status='Active'")
	List<AuditControlEntity> getAuditControlsByUser(@Param("userName")String userName);

}
