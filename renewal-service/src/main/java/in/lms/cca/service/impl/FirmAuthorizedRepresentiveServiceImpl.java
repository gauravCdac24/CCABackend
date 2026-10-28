package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.FirmAuthorizedRepresentative;
import in.lms.cca.repository.FirmRepresentativeRepo;
import in.lms.cca.service.FirmAuthorizedRepresentiveService;

@Service
@Transactional
public class FirmAuthorizedRepresentiveServiceImpl implements FirmAuthorizedRepresentiveService{
	
	@Autowired
	private FirmRepresentativeRepo firmRepresentativeRepo;

	@Override
	public Optional<FirmAuthorizedRepresentative> addData(FirmAuthorizedRepresentative firmAuthorizedRepresentative) {
		if (firmAuthorizedRepresentative == null) {
            return Optional.empty();
        }

        try {
        	FirmAuthorizedRepresentative savedFirmAuthorizedRepresentative = firmRepresentativeRepo.save(firmAuthorizedRepresentative);
            return Optional.of(savedFirmAuthorizedRepresentative);
        } catch (Exception e) {
        	e.printStackTrace();
            return Optional.empty();
        }	}

	@Override
	public List<FirmAuthorizedRepresentative> findIntentAppById(Long intentAppId,String status) {
		// TODO Auto-generated method stub
		return firmRepresentativeRepo.findIntentAppById(intentAppId,status);
	}

	@Override
	public Optional<FirmAuthorizedRepresentative> updateData(
			FirmAuthorizedRepresentative firmAuthorizedRepresentative) {
		if (firmAuthorizedRepresentative == null) {
            return Optional.empty();
        }
		if (firmAuthorizedRepresentative.getAuthorizedRepresentativeId() == null) {
            return Optional.empty();
        }
		

        try {
        	FirmAuthorizedRepresentative savedFirmAuthorizedRepresentative = firmRepresentativeRepo.save(firmAuthorizedRepresentative);
            return Optional.of(savedFirmAuthorizedRepresentative);
        } catch (Exception e) {
        	e.printStackTrace();
            return Optional.empty();
        }	}

	@Override
	public List<FirmAuthorizedRepresentative> findIntentWitoutAppById(Long intentAppId) {
		// TODO Auto-generated method stub
		return  firmRepresentativeRepo.findIntentWitoutAppById(intentAppId);
	}
	
	
	

}
