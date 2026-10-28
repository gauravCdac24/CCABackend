package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.EkycMode;

public interface IEkycModeService{

	Optional<EkycMode> addekycMode(EkycMode obj);
	List<EkycMode> getAllEKYCModeByAppId(Long esignLicenseeAppId);
	void deleteAllEKYCModeByAppId(Long esignLicenseeAppId);
	List<EkycMode> getAllEKYCModeByAppIdAndStatus(Long esignLicenseeAppId, String status);
	void inactiveAllEKYCModeByAppId(Long esignLicenseeAppId);
	EkycMode getEkycModeById(Long id);
	List<EkycMode> getAllEKYCModeByAppIdAndStatusAndRequired(Long esignLicenseeAppId, String status, Boolean isApprovalRequired);
	void changeStatusOfAllEKYCModeByAppIdAndStatus(Long id, String status);
	List<String> getDistinctApprovedEkycModesByUsername(String userName);
	
}
