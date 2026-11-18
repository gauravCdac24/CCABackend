package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import in.lms.cca.entity.ApplicationDocument;
import in.lms.cca.repository.ApplicationDocumentRepository;
import in.lms.cca.service.ApplicationDocumentService;

@Service
@Transactional
public class ApplicationDocumentServiceImpl implements ApplicationDocumentService {
	
	@Autowired
	private ApplicationDocumentRepository applicationDocumentRepo;
	
	@Override
	public Optional<ApplicationDocument> addApplicationDocument(ApplicationDocument passportDocument) {
		  if (passportDocument == null) {
	            return Optional.empty();
	        }

	        try {
	        	ApplicationDocument savedApplicationDocument = applicationDocumentRepo.save(passportDocument);
	            return Optional.of(savedApplicationDocument);
	        } catch (Exception e) {
	        	e.printStackTrace();
	            return Optional.empty();
	        }
	}

	@Override
	public List<ApplicationDocument> findByindivApplicationId(Long intentAppId) {
		// TODO Auto-generated method stub
		return applicationDocumentRepo.findByindivApplicationId(intentAppId);
	}

	@Override
	public List<ApplicationDocument> findIntentAppById(Long intentAppId) {
		// TODO Auto-generated method stub
		return applicationDocumentRepo.findIntentAppById(intentAppId);
	}

	@Override
	public Optional<ApplicationDocument> updateApplicationDocument(ApplicationDocument passportDocument) {
		// TODO Auto-generated method stub
		if(passportDocument == null)
			return Optional.empty();
		
		if(passportDocument.getAppDocId() == null)
			return Optional.empty();
		
		try {
			ApplicationDocument savedApplicationDocument = applicationDocumentRepo.save(passportDocument);
            return Optional.of(savedApplicationDocument);
			
		}catch (Exception e) {
        	e.printStackTrace();
            return Optional.empty();
        }
		
	}

	@Override
	public Optional<ApplicationDocument> downloadfile(Long id) {
		// TODO Auto-generated method stub
		return applicationDocumentRepo.downloadfile(id);
	}

	@Override
	public List<ApplicationDocument> findIntentWithoutStatusAppById(Long intentAppId) {
		// TODO Auto-generated method stub
		return applicationDocumentRepo.findIntentWithoutStatusAppById(intentAppId);
	}
	

}
