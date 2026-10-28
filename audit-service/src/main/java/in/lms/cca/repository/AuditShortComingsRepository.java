package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.ApplicationAuditEntity;
import in.lms.cca.entity.AuditShortComingsEntity;

public interface AuditShortComingsRepository extends JpaRepository<AuditShortComingsEntity, Long> {

	@Query("SELECT d FROM AuditShortComingsEntity d WHERE d.appAuditId.appAuditId=:appAuditId AND d.status='Active'")
	AuditShortComingsEntity getActiveByAppAuditId(@Param("appAuditId") Long appAuditId);

	@Query("SELECT d FROM AuditShortComingsEntity d WHERE d.appAuditId.appAuditId=:appAuditId")
	List<AuditShortComingsEntity> findByAuditsId(@Param("appAuditId")Long appAuditId);

}
