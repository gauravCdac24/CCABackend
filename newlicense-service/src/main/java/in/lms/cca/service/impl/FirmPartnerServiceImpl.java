package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.FirmPartnerDetails;
import in.lms.cca.repository.FirmPartnerRepository;
import in.lms.cca.service.FirmPartnerService;

@Service
@Transactional
public class FirmPartnerServiceImpl implements FirmPartnerService {
	
	@Autowired
	private FirmPartnerRepository firmPartnerRepo;

	@Override
	public Optional<FirmPartnerDetails> addData(FirmPartnerDetails firmPartner) {
		if (firmPartner == null) {
            return Optional.empty();
        }

        try {
        	FirmPartnerDetails savedFirmPartnerDetails = firmPartnerRepo.save(firmPartner);
            return Optional.of(savedFirmPartnerDetails);
        } catch (Exception e) {
        	e.printStackTrace();
            return Optional.empty();
        }	}

	@Override
	public  List<FirmPartnerDetails> findIntentAppById(Long intentAppId) {
		// TODO Auto-generated method stub
		return firmPartnerRepo.findIntentAppById(intentAppId);
	}

	@Override
	public Optional<FirmPartnerDetails> updateData(FirmPartnerDetails firmPartner) {
		if (firmPartner == null) {
            return Optional.empty();
        }
		if (firmPartner.getPartnerDetailId() == null) {
            return Optional.empty();
        }

        try {
        	FirmPartnerDetails savedFirmPartnerDetails = firmPartnerRepo.save(firmPartner);
            return Optional.of(savedFirmPartnerDetails);
        } catch (Exception e) {
        	e.printStackTrace();
            return Optional.empty();
        }	}

	@Override
	public List<FirmPartnerDetails> findIntentAppWithoutStatusById(Long intentAppId) {
		// TODO Auto-generated method stub
		return firmPartnerRepo.findIntentAppWithoutStatusById(intentAppId);
	}
	
	

}
