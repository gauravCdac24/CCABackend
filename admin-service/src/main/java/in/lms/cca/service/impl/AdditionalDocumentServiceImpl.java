package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.AdditionalDocumentEntity;
import in.lms.cca.repository.AdditionalDocumentRepository;
import in.lms.cca.service.IAdditionalDocumentService;

@Service
@Transactional
public class AdditionalDocumentServiceImpl implements IAdditionalDocumentService{
	
	@Autowired
	private AdditionalDocumentRepository additionalDocumentRepo;

	@Override
	public AdditionalDocumentEntity getDocumentName(String documentname) {
		// TODO Auto-generated method stub
		return additionalDocumentRepo.getDocumentName(documentname);
	}

	@Override
	public Optional<AdditionalDocumentEntity> addState(AdditionalDocumentEntity newAdditionalDocumentEntity) {
		 if (newAdditionalDocumentEntity == null)
		        return Optional.empty();

		    try {
		       
		    	AdditionalDocumentEntity savedAdditionalDocumentEntity = additionalDocumentRepo.save(newAdditionalDocumentEntity);
		       
		        return Optional.of(savedAdditionalDocumentEntity);
		    } catch (Exception e) {
		        return Optional.empty();
		    }
	}
	
	@Override
	public Optional<AdditionalDocumentEntity> updateState(AdditionalDocumentEntity newAdditionalDocumentEntity) {
		if(newAdditionalDocumentEntity == null) {
			return Optional.empty();
		}
		if(newAdditionalDocumentEntity.getAdditionalDocumentId() == null) {
			return Optional.empty();
		}
		 try {
		       
		    	AdditionalDocumentEntity savedAdditionalDocumentEntity = additionalDocumentRepo.save(newAdditionalDocumentEntity);
		       
		        return Optional.of(savedAdditionalDocumentEntity);
		    } catch (Exception e) {
		        return Optional.empty();
		    }
	
	}

	@Override
	public List<AdditionalDocumentEntity> getAllAdditionalDocumentEntity() {
		return additionalDocumentRepo.findAll(Sort.by(Sort.Direction.DESC, "created"));
	}

	@Override
	public List<AdditionalDocumentEntity> getAllActiveAdditionalDocumentEntity() {
		return additionalDocumentRepo.findAllActiveAdditionalDocumentEntity();
	}

	@Override
	public List<AdditionalDocumentEntity> getAllInactiveAdditionalDocumentEntity() {
		return additionalDocumentRepo.findAllInActiveAdditionalDocumentEntity();
	}


	@Override
	public AdditionalDocumentEntity getAdditionalDocumentEntityById(Long id) {
		return additionalDocumentRepo.findByAdditionalDocumentEntityId(id);
	}

	

	@Override
	public boolean deleteByAdditionalDocumentEntityId(Long stateId) {
		AdditionalDocumentEntity cobj = additionalDocumentRepo.findByAdditionalDocumentEntityId(stateId);
	    if (cobj == null)
	        return false;
	    try {
	       
	    	additionalDocumentRepo.deleteByAdditionalDocumentEntityId(stateId);
	       
	        return true;
	    } catch (Exception e) {
	        return false;
	    }
	}

	
	
	

}
