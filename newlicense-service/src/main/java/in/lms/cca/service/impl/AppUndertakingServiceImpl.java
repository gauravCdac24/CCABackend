package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.AppUndertakingEntity;
import in.lms.cca.repository.AppUndertakingRepository;
import in.lms.cca.service.AppUndertakingService;

@Service
@Transactional
public class AppUndertakingServiceImpl implements AppUndertakingService{
	
	@Autowired
	private AppUndertakingRepository appUndertakingRepository;

	@Override
	public Optional<AppUndertakingEntity> addData(AppUndertakingEntity appUndertakingEntity) {
	
		if(appUndertakingEntity==null)
		{
			return Optional.empty();
		}
		 try {
			 AppUndertakingEntity savedAppUndertakingEntity = appUndertakingRepository.save(appUndertakingEntity);
	            return Optional.of(savedAppUndertakingEntity);
	        } catch (Exception e) {
	            return Optional.empty();
	        }
		
	}

	@Override
	public Optional<AppUndertakingEntity> updateData(AppUndertakingEntity existingUndertaking) {
		if(existingUndertaking==null)
		{
			return Optional.empty();
		}
		
		if(existingUndertaking.getAppUndertakingId()==null)
		{
			return Optional.empty();
		}
		 try {
			 AppUndertakingEntity savedAppUndertakingEntity = appUndertakingRepository.save(existingUndertaking);
	            return Optional.of(savedAppUndertakingEntity);
	        } catch (Exception e) {
	            return Optional.empty();
	        }
	}

	@Override
	public List<AppUndertakingEntity> findByIntentAppId(Long intentAppId) {
		// TODO Auto-generated method stub
		return appUndertakingRepository.findByIntentAppId(intentAppId);
	}
	
	
	

}
