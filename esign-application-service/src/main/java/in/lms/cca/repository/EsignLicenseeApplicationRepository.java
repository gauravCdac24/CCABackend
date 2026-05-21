package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.EsignLicenseeApplication;

public interface EsignLicenseeApplicationRepository extends JpaRepository<EsignLicenseeApplication, Long>{

	@Query("FROM EsignLicenseeApplication a WHERE a.userName = :username AND a.applicationStatus = :status ORDER BY created DESC")
	List<EsignLicenseeApplication> findApplicationByUsernameAndStatus(String username, String status);

	@Query("FROM EsignLicenseeApplication a WHERE a.userName = :username AND a.applicationStatus != :status ORDER BY created DESC")
	List<EsignLicenseeApplication> getApplicationByUsernameAndNotStatus(String username, String status);

	@Query("FROM EsignLicenseeApplication a WHERE a.applicationStatus = 'Submitted' OR a.applicationStatus = 'Under Review' Or a.applicationStatus = 'Recommanded for Rejection' ORDER BY created DESC")
	List<EsignLicenseeApplication> findAllApplicationForReview();

	@Query("FROM EsignLicenseeApplication a WHERE a.esignLicenseeAppId = :id ORDER BY created DESC")
	EsignLicenseeApplication findApplicationById(Long id);
	
	@Query("FROM EsignLicenseeApplication a WHERE a.applicationStatus = :status ORDER BY created DESC")
	List<EsignLicenseeApplication> findAllApplicationByStatus(String status);

	@Query("FROM EsignLicenseeApplication a WHERE a.userName = :username AND a.applicationStatus != :status1 AND a.applicationStatus != :status2 ORDER BY created DESC")
	List<EsignLicenseeApplication> findApplicationByUsernameAndNotStatus(String username, String status1,
			String status2);

	@Query("FROM EsignLicenseeApplication a WHERE a.userName = :username AND (a.applicationStatus = :status1 OR a.applicationStatus = :status2) ORDER BY created DESC")
	List<EsignLicenseeApplication> findApplicationByUsernameAndStatus(String username, String status1, String status2);

	@Query("FROM EsignLicenseeApplication a WHERE a.userName = :username")
	List<EsignLicenseeApplication> findApplicationByUsername(String username);

}
