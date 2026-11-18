package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.LicenseDetails;

public interface ILicenseDetailsService {

	Optional<LicenseDetails> addLicenseDetails(LicenseDetails obj);
	Optional<LicenseDetails> updateLicenseDetails(LicenseDetails obj);
	LicenseDetails getLicenseDetailsById(Long id);
	List<LicenseDetails> getAllLicenseDetails();
	List<LicenseDetails> getAllActiveLicenseDetails();
	List<LicenseDetails> getAllInactiveLicenseDetails();
	List<LicenseDetails> getActiveLicenseDetailsByUsername(String username);
	List<LicenseDetails> getInactiveLicenseDetailsByUsername(String applicantUsername);
	List<LicenseDetails> getAllLicenseDetailsByUsername(String username);
	List<LicenseDetails> getActiveLicenseDetails();
	void changedTheReviewStatus(String applicantUsername);
	void changedThePaymentStatus(String applicantUsername);

	
}
