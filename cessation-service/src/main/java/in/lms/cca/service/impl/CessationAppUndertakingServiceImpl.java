package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.CessationAppUndertakingEntity;
import in.lms.cca.repository.CessationAppUndertakingRepository;
import in.lms.cca.service.CessationAppUndertakingService;

@Service
@Transactional
public class CessationAppUndertakingServiceImpl implements CessationAppUndertakingService {
	
	@Autowired
	private CessationAppUndertakingRepository cessationAppUndertakingRepository;
	
	@Override
	public Optional<CessationAppUndertakingEntity> save(CessationAppUndertakingEntity entity) {
		if (entity == null)
	        return Optional.empty();

	    try {
	       
	    	CessationAppUndertakingEntity savedCessationAppUndertakingEntity = cessationAppUndertakingRepository.save(entity);
	       
	        return Optional.of(savedCessationAppUndertakingEntity);
	    } catch (Exception e) {
	        return Optional.empty();
	    }
	}

	@Override
	public List<CessationAppUndertakingEntity> getAllByCessationId(String cessationAppId) {
		// TODO Auto-generated method stub
		return cessationAppUndertakingRepository.getAllByCessationId(cessationAppId);
	}
	
	
	@Override
	public Optional<CessationAppUndertakingEntity> updateData(CessationAppUndertakingEntity entity) {
		if (entity == null)
	        return Optional.empty();
		
		if (entity.getAppUndertakingId() == null)
	        return Optional.empty();

	    try {
	       
	    	CessationAppUndertakingEntity savedCessationAppUndertakingEntity = cessationAppUndertakingRepository.save(entity);
	       
	        return Optional.of(savedCessationAppUndertakingEntity);
	    } catch (Exception e) {
	        return Optional.empty();
	    }
	}

	@Override
	public List<CessationAppUndertakingEntity> getAllByUndertaking() {
		// TODO Auto-generated method stub
		return cessationAppUndertakingRepository.getAllByUndertaking();
	}

	@Override
	public List<CessationAppUndertakingEntity> getAllByUndertakingByCessationId(String cessationAppId) {
		// TODO Auto-generated method stub
		return cessationAppUndertakingRepository.getAllByUndertakingByCessationId(cessationAppId);
	}

	@Override
	public Optional<CessationAppUndertakingEntity> downloadfile(Long id) {
		// TODO Auto-generated method stub
		return cessationAppUndertakingRepository.downloadfile(id);
	}

	@Override
	public CessationAppUndertakingEntity getAllDataByUndertakingId(String undertakingId) {
		// TODO Auto-generated method stub
		return cessationAppUndertakingRepository.getAllDataByUndertakingId(undertakingId);
	}

}
