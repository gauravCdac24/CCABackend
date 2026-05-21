package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.FirmApplication;
import in.lms.cca.repository.FirmApplicationRepository;
import in.lms.cca.service.FirmApplicationService;

@Service
@Transactional
public class FirmApplicationServiceImpl implements FirmApplicationService {
	
	@Autowired
	private FirmApplicationRepository firmApplicationRepo;

	@Override
	public Optional<FirmApplication> addData(FirmApplication firmApplication) {
		if (firmApplication == null) {
            return Optional.empty();
        }

        try {
        	FirmApplication savedfirmApplication = firmApplicationRepo.save(firmApplication);
            return Optional.of(savedfirmApplication);
        } catch (Exception e) {
        	e.printStackTrace();
            return Optional.empty();
        }
}

	@Override
	public FirmApplication findIntentAppById(Long intentAppId,String status) {
		// TODO Auto-generated method stub
		return firmApplicationRepo.findIntentAppById(intentAppId,status);
	}

	@Override
	public Optional<FirmApplication> updateData(FirmApplication firmApplication) {
		if (firmApplication == null) {
            return Optional.empty();
        }
		if (firmApplication.getFirmApplicationId() == null) {
            return Optional.empty();
        }

        try {
        	FirmApplication savedfirmApplication = firmApplicationRepo.save(firmApplication);
            return Optional.of(savedfirmApplication);
        } catch (Exception e) {
        	e.printStackTrace();
            return Optional.empty();
        }
	}

	@Override
	public String getFirmNameByIntentAppId(Long intentAppId,String status) {
		// TODO Auto-generated method stub
		return firmApplicationRepo.getFirmNameByIntentAppId(intentAppId,status);
	}

	@Override
	public List<FirmApplication> findIntentWithoutStatusAppById(Long intentAppId) {
		// TODO Auto-generated method stub
		return firmApplicationRepo.findIntentWithoutStatusAppById(intentAppId);
	}
	

}
