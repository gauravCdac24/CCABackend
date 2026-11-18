package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.FirmApplication2;
import in.lms.cca.repository.FirmApplication2Repository;
import in.lms.cca.service.FirmApplication2service;

@Service
@Transactional
public class FirmApplication2ServiceImpl implements FirmApplication2service {
	
	@Autowired
	private FirmApplication2Repository firmApplication2Repo;

	@Override
	public Optional<FirmApplication2> addData(FirmApplication2 firmApplication) {
	
		if(firmApplication==null) {
			return Optional.empty();
		}
		 try {
			 FirmApplication2 savedfirmApplication = firmApplication2Repo.save(firmApplication);
	            return Optional.of(savedfirmApplication);
	        } catch (Exception e) {
	        	e.printStackTrace();
	            return Optional.empty();
	        }
	}

	@Override
	public FirmApplication2 findIntentAppById(Long intentAppId) {
		// TODO Auto-generated method stub
		return firmApplication2Repo.findIntentAppById(intentAppId);
	}

	@Override
	public Optional<FirmApplication2> updateData(FirmApplication2 firmApplication) {
		if(firmApplication==null) {
			return Optional.empty();
		}
		if(firmApplication.getFirmApplicationId()==null) {
			return Optional.empty();
		}
		 try {
			 FirmApplication2 savedfirmApplication = firmApplication2Repo.save(firmApplication);
	            return Optional.of(savedfirmApplication);
	        } catch (Exception e) {
	        	e.printStackTrace();
	            return Optional.empty();
	        }
	}

	@Override
	public List<FirmApplication2> findIntentWithoutStatusAppById(Long intentAppId) {
		// TODO Auto-generated method stub
		return firmApplication2Repo.findIntentWithoutStatusAppById(intentAppId);
	}

	@Override
	public FirmApplication2 findIntentAppByIdWithStatus(Long intentAppId, String status) {
		// TODO Auto-generated method stub
		return firmApplication2Repo.findIntentAppByIdWithStatus(intentAppId,status);
	}
	
	
	

}
