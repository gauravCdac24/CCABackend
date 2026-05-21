package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.dto.AddressDTO;
import in.lms.cca.dto.IndivAddressDTO;
import in.lms.cca.entity.IntentApplication;
import in.lms.cca.payload.LocationDetailsPayload;
import in.lms.cca.payload.LocationFacilityPayload;

public interface IIntentApplicationService {

	Optional<IntentApplication> addIntentApp(IntentApplication intentApp);

	Long addUsers(AddressDTO indivAddressDTO);

	IntentApplication findIntentAppById(String userName);

	Long addUser(AddressDTO addressDTO);

	AddressDTO getAllLocationByAddressId(String id);

	Optional<IntentApplication> updateIntentApp(IntentApplication intentApp);

	Long updateUsers(AddressDTO indivAddressDTO);

	Long updateUser(LocationDetailsPayload locationDetail);

	IntentApplication getIntentByIntentAppId(Long intentAppId);

	/*---*/
	List<IntentApplication> getAllIntentApplication();

	List<IntentApplication> getPaymentProofPendingVerification();

	List<IntentApplication> getAllServiceMaster();

	

}
