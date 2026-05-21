package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.EsignLicenseeApplication;

public interface IEsignLicenseeApplicationService{

	Optional<EsignLicenseeApplication> addEsignLicenseeApplication(EsignLicenseeApplication obj);
	List<EsignLicenseeApplication> getApplicationByUsernameAndStatus(String username, String status);
	List<EsignLicenseeApplication> getApplicationByUsernameAndNotStatus(String username, String status);
	List<EsignLicenseeApplication> getAllApplication();
	List<EsignLicenseeApplication> getAllApplicationForReview();
	EsignLicenseeApplication getApplicationById(Long id);
	List<EsignLicenseeApplication> getAllApplicationByStatus(String status);
	List<EsignLicenseeApplication> getApplicationByUsernameAndNotStatus(String username, String status1, String status2);
	List<EsignLicenseeApplication> getApplicationByUsernameAndStatus(String username, String status1, String status2);
	List<EsignLicenseeApplication> getApplicationByUsername(String username);
	Optional<EsignLicenseeApplication> updateEsignLicenseeApplication(EsignLicenseeApplication espApplication);

	
}
