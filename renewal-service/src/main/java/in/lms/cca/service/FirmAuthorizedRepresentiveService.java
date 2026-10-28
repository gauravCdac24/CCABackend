package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.FirmAuthorizedRepresentative;

public interface FirmAuthorizedRepresentiveService {

	Optional<FirmAuthorizedRepresentative> addData(FirmAuthorizedRepresentative firmAuthorizedRepresentative);

	List<FirmAuthorizedRepresentative> findIntentAppById(Long intentAppId, String status);

	Optional<FirmAuthorizedRepresentative> updateData(FirmAuthorizedRepresentative firmAuthorizedRepresentative);

	List<FirmAuthorizedRepresentative> findIntentWitoutAppById(Long intentAppId);

}
