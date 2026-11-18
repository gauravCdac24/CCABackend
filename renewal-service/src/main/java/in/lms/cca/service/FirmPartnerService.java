package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.FirmPartnerDetails;

public interface FirmPartnerService {

	Optional<FirmPartnerDetails> addData(FirmPartnerDetails firmPartner);

	 List<FirmPartnerDetails> findIntentAppById(Long intentAppId);

	Optional<FirmPartnerDetails> updateData(FirmPartnerDetails firmPartner);

	List<FirmPartnerDetails> findIntentAppWithoutStatusById(Long intentAppId);

}
