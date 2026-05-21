package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.AppLocation;
import in.lms.cca.entity.ApplicationDocument;
import in.lms.cca.repository.AppLocationRepository;
import in.lms.cca.service.AppLocationService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class AppLocationServiceImpl implements AppLocationService {
	
	@Autowired
	private AppLocationRepository appLocationRepo;

	@Override
	public Optional<AppLocation> addAppLocation(AppLocation appLocation) {
		  if (appLocation == null) {
	            return Optional.empty();
	        }

	        try {
	        	AppLocation savedLocation = appLocationRepo.save(appLocation);
	            return Optional.of(savedLocation);
	        } catch (Exception e) {
	            return Optional.empty();
	        }
	}

	@Override
	public List<AppLocation> findIntentAppById(Long intentAppId,String status) {
		// TODO Auto-generated method stub
		return appLocationRepo.findIntentAppById(intentAppId,status);
	}

	@Override
	public Optional<AppLocation> updateAppLocation(AppLocation resLocation) {
		if(resLocation == null)
			return Optional.empty();
		
		if(resLocation.getLocationId() == null)
			return Optional.empty();
		
		try {
			AppLocation savedAppLocation = appLocationRepo.save(resLocation);
            return Optional.of(savedAppLocation);
			
		}catch (Exception e) {
        	e.printStackTrace();
            return Optional.empty();
        }
		
	}

	@Override
	public Optional<AppLocation> findById(Long addressId) {
		// TODO Auto-generated method stub
		return appLocationRepo.findById(addressId);
	}

	@Override
	public Optional<AppLocation> findByEncryptedAddressId(String offApp) {
		// TODO Auto-generated method stub
		return appLocationRepo.findByEncryptedAddressId(offApp);
	}

	@Override
	public List<AppLocation> findIntentWithoutStatusAppById(Long intentAppId) {
		// TODO Auto-generated method stub
		return appLocationRepo.findIntentWithoutStatusAppById(intentAppId);
	}

	@Override
	public List<AppLocation> findIntentById(Long intentAppId) {
		// TODO Auto-generated method stub
		return appLocationRepo.findIntentById(intentAppId);
	}

	@Override
	public List<AppLocation> getAllActiveAppLocation() {
		// TODO Auto-generated method stub
		return appLocationRepo.getAllActiveAppLocation();
	}

	
	
}
