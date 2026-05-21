package in.lms.cca.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.AuditNCSEntity;

public interface AuditNCSRepository extends JpaRepository<AuditNCSEntity, Long> {

	@Query("SELECT d FROM AuditNCSEntity d WHERE d.licenseeAuditId=:appAuditId AND d.status='Active'")
	AuditNCSEntity findByAuditId(@Param("appAuditId")Long appAuditId);

	@Query("SELECT d FROM AuditNCSEntity d WHERE d.ncs=:decrypt AND d.status='Active'")
	Optional<AuditNCSEntity> downloadfileBydDocumentName(@Param("decrypt")String decrypt);

}
