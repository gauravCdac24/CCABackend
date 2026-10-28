package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.UndertakingMasterEntity;
import in.lms.cca.repository.UndertakingMasterRepository;
import in.lms.cca.service.UndertakingMasterService;


@Service
@Transactional
public class UndertakingMasterServiceImpl implements UndertakingMasterService {
	
	@Autowired
	private UndertakingMasterRepository undertakingMasterRepo;

	@Override
	public Optional<UndertakingMasterEntity> addUnderTakingMaster(UndertakingMasterEntity undertakingMasterEntity) {
	if(undertakingMasterEntity==null) {
		return Optional.empty();
	}
	 try {
		 UndertakingMasterEntity savedUndertakingMaster = undertakingMasterRepo.save(undertakingMasterEntity);
         return Optional.of(savedUndertakingMaster);
     } catch (Exception e) {
         return Optional.empty();
     }
	}

	@Override
	public List<UndertakingMasterEntity> getAllUndertakingMasterEntity() {
		// TODO Auto-generated method stub
		return undertakingMasterRepo.findAll();
	}

	@Override
	public UndertakingMasterEntity getUndertakingMasterById(Long id) {
		// TODO Auto-generated method stub
		return undertakingMasterRepo.getUndertakingMasterById(id);
	}

	@Override
	public Optional<UndertakingMasterEntity> updateUnderTakingMaster(UndertakingMasterEntity undertakingMasterEntity) {
		if(undertakingMasterEntity==null) {
			return Optional.empty();
		}
		if(undertakingMasterEntity.getUndertakingId()==null) {
			return Optional.empty();
		}
		 try {
			 UndertakingMasterEntity savedUndertakingMaster = undertakingMasterRepo.save(undertakingMasterEntity);
	         return Optional.of(savedUndertakingMaster);
	     } catch (Exception e) {
	         return Optional.empty();
	     }
	}

}
