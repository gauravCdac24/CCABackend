package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import in.lms.cca.entity.AuditShortComingsEntity;
import in.lms.cca.entity.LicenseeAuditEntity;

public interface AuditShortComingsRepository extends JpaRepository<AuditShortComingsEntity, Long> {

	@Query("SELECT d FROM AuditShortComingsEntity d WHERE d.licenseeAuditId=:applicationAuditEntity AND d.status='Active'")
	AuditShortComingsEntity getByAuditId(@Param("applicationAuditEntity")LicenseeAuditEntity applicationAuditEntity);

	@Query("SELECT d FROM AuditShortComingsEntity d WHERE d.licenseeAuditId=:appAuditId")
	List<AuditShortComingsEntity> findByAuditsId(@Param("appAuditId")Long appAuditId);

}
