package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.AppLocation;

public interface AppLocationService {

	Optional<AppLocation> addAppLocation(AppLocation appLocation);

	List<AppLocation> findIntentAppById(Long intentAppId, String status);

	Optional<AppLocation> updateAppLocation(AppLocation resLocation);

	Optional<AppLocation> findById(Long addressId);

	Optional<AppLocation> findByEncryptedAddressId(String offApp);

	List<AppLocation> findIntentWithoutStatusAppById(Long intentAppId);

	List<AppLocation> findIntentById(Long intentAppId);

	List<AppLocation> getAllActiveAppLocation();

}
