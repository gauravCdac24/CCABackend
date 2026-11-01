package in.lms.cca.repository.annexure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.annexure.AnnexureMain;

public interface AnnexureMainRepository extends JpaRepository<AnnexureMain, Long>{

	@Query("FROM AnnexureMain a WHERE a.status = :status AND a.userName = :username AND a.auditScheduleId IS NULL AND a.intentAppId IS NULL")
	AnnexureMain findAnnexureByStatusAndUsername(String status, String username);

	@Query("FROM AnnexureMain a WHERE a.status = :status AND a.userName = :username AND a.auditScheduleId = :auditScheduleId")
	AnnexureMain findAnnexureByStatusUsernameAndAuditScheduleId(String status, String username, Long auditScheduleId);

	@Query("FROM AnnexureMain a WHERE a.status = :status AND a.userName = :username AND a.intentAppId = :intentAppId AND a.auditScheduleId IS NULL")
	AnnexureMain findAnnexureByStatusUsernameAndIntentAppId(String status, String username, Long intentAppId);

	@Query("FROM AnnexureMain a WHERE a.status = 'Active' AND a.userName = :username AND a.auditScheduleId IS NULL AND a.intentAppId IS NOT NULL ORDER BY a.annexureMainId DESC")
	List<AnnexureMain> findActiveRenewalByUsername(String username);

	@Query("FROM AnnexureMain a WHERE a.userName = :username AND a.auditScheduleId IS NULL AND a.intentAppId IS NULL ORDER BY a.annexureMainId DESC")
	List<AnnexureMain> findFreshByUserNameOrderByIdDesc(String username);

	
	@Query("FROM AnnexureMain a WHERE a.status ='Active'")
	List<AnnexureMain> getAllAnnexureMain();

	@Query("FROM AnnexureMain a WHERE a.userName = :username ORDER BY a.annexureMainId DESC")
	List<AnnexureMain> findByUserNameOrderByIdDesc(String username);

}
