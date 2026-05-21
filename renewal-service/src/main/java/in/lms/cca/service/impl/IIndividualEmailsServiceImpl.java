package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.BankDraft;
import in.lms.cca.entity.IndivApplication;
import in.lms.cca.entity.IndivEmails;
import in.lms.cca.repository.indivEmailsRepository;
import in.lms.cca.service.IIndividualEmailsService;
import jakarta.transaction.Transactional;
@Service
@Transactional
public class IIndividualEmailsServiceImpl implements IIndividualEmailsService {
	
	@Autowired
	private indivEmailsRepository indivEmailRepo;

	@Override
	public Optional<IndivEmails> addIndivEmails(IndivEmails indivEmail) {
		if(indivEmail == null) {
			return Optional.empty();
		}
			
		try { 
			IndivEmails apptypeobj = indivEmailRepo.save(indivEmail);
	        return Optional.of(apptypeobj);
	    
		}catch(Exception e) {
				
				return Optional.empty();
		}		
	}

	@Override
	public List<IndivEmails> findByindivApplicationId(Long indivApplicationId) {
		
		return indivEmailRepo.findByindivApplicationId(indivApplicationId);
	}

	@Override
	public Optional<IndivEmails> updateIndivEmails(IndivEmails indivEmail) {
		if(indivEmail == null)
			return Optional.empty();
		
		if(indivEmail.getEmailId() == null)
			return Optional.empty();
		
		try {
			IndivEmails savedIndivEmails = indivEmailRepo.save(indivEmail);
            return Optional.of(savedIndivEmails);
			
		}catch (Exception e) {
        	e.printStackTrace();
            return Optional.empty();
        }
		
	}

}
