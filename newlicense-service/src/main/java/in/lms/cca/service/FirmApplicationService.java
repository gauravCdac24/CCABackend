package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.FirmApplication;

public interface FirmApplicationService {

	Optional<FirmApplication> addData(FirmApplication firmApplication);

	FirmApplication findIntentAppById(Long intentAppId, String status);

	Optional<FirmApplication> updateData(FirmApplication firmApplication);

	List<String> getFirmNameByIntentAppId(Long intentAppId);

	List<FirmApplication> findIntentWithoutStatusAppById(Long intentAppId);

}
