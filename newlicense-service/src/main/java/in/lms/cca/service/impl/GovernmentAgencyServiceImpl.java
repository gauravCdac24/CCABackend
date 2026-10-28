package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.GovtOrganizationApplication;
import in.lms.cca.repository.GovernmentAgencyRepository;
import in.lms.cca.service.GovernmentAgencyService;

@Service
@Transactional
public class GovernmentAgencyServiceImpl implements GovernmentAgencyService{
	
	@Autowired
	private GovernmentAgencyRepository governmentAgencyRepo;

	@Override
	public Optional<GovtOrganizationApplication> addData(GovtOrganizationApplication govtOrganizationApplication) {
		 if (govtOrganizationApplication == null) {
	            return Optional.empty();
	        }

	        try {
	        	GovtOrganizationApplication savedGovtOrganizationApplication = governmentAgencyRepo.save(govtOrganizationApplication);
	            return Optional.of(savedGovtOrganizationApplication);
	        } catch (Exception e) {
	            return Optional.empty();
	        }
	}

	@Override
	public GovtOrganizationApplication findIntentAppById(Long intentAppId,String status) {
		// TODO Auto-generated method stub
		return governmentAgencyRepo.findIntentAppById(intentAppId,status);
	}

	@Override
	public Optional<GovtOrganizationApplication> UpdateData(GovtOrganizationApplication govtOrganizationApplication) {
		 if (govtOrganizationApplication == null) {
	            return Optional.empty();
	        }if (govtOrganizationApplication.getOrgApplicationId() == null) {
	            return Optional.empty();
	        }

	        try {
	        	GovtOrganizationApplication savedGovtOrganizationApplication = governmentAgencyRepo.save(govtOrganizationApplication);
	            return Optional.of(savedGovtOrganizationApplication);
	        } catch (Exception e) {
	            return Optional.empty();
	        }
	}

	@Override
	public List<String> getOrganizationNameByIntentAppId(Long intentAppId) {
		// TODO Auto-generated method stub
		return governmentAgencyRepo.getOrganizationNameByIntentAppId( intentAppId);
	}

	@Override
	public GovtOrganizationApplication findWithoutIntentAppById(Long intentAppId) {
		// TODO Auto-generated method stub
		return governmentAgencyRepo.findWithoutIntentAppById(intentAppId);
	}
	
	

}
