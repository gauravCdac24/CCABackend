package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.ESPPurposeEntity;

public interface IESPPurposeService {

	void inactiveAllPurposeByAppId(Long esignLicenseeAppId);

	List<ESPPurposeEntity> getAllESPPurposeByStatusAndLicenseAppId(Long esignLicenseeAppId,
			String Status);

	Optional<ESPPurposeEntity> addPurpose(ESPPurposeEntity purposeEntity);

}
