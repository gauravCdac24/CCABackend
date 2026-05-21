package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.AnnualAuditScheduleEntity;


public interface LicenseeAuditRepository  extends JpaRepository<AnnualAuditScheduleEntity, Long>{

	@Query("SELECT d FROM AnnualAuditScheduleEntity d WHERE d.userName = :id ORDER BY scheduleStartDate DESC")
	List<AnnualAuditScheduleEntity> findByUsername(String id);

}
