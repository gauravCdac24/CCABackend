package in.lms.cca.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.DSCeSignIssued;
import in.lms.cca.repository.DSCeSignIssuedRepository;
import in.lms.cca.service.IDSCeSignIssuedService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class DSCeSignIssuedServiceImpl implements IDSCeSignIssuedService{

	@Autowired
	private DSCeSignIssuedRepository dscRepo;
	
	@Override
	public Optional<DSCeSignIssued> addDSCeSignIssued(DSCeSignIssued obj) {
		
		if (obj == null)
	        return Optional.empty();

	    try {
	       
	    	DSCeSignIssued savedObj = dscRepo.save(obj);
	       
	        return Optional.of(savedObj);
	    } catch (Exception e) {
	        return Optional.empty();
	    }
	    
	}

	@Override
	public List<DSCeSignIssued> getAllDSCeSignIssued() {
		return dscRepo.findAll(Sort.by(Sort.Direction.DESC, "created"));
	}

	@Override
	public List<DSCeSignIssued> getDSCeSignIssuedByUsername(String username) {
		return dscRepo.findDSCeSignIssuedByUsername(username);
	}

	@Override
	public DSCeSignIssued getDSCeSignIssuedById(Long id) {
		
		return dscRepo.findDSCeSignIssuedById(id);
	}

	@Override
	public DSCeSignIssued getDSCeSignIssuedByYearMonthCountryAndState(String countryId, String stateId, String year,
			String month) {
		
		return dscRepo.findDSCeSignIssuedByYearMonthCountryAndState(countryId, stateId, year, month);
	}

	@Override
	public List<DSCeSignIssued> getAllEsignDSCByCAMonthStateAndYear(List<String> ca, List<String> month,
			List<String> state, List<String> year) {
		
		return dscRepo.findAllEsignDSCByCAMonthStateAndYear(ca, month, state, year);
	}

	@Override
	public List<DSCeSignIssued> getMonthlyDSCEsignData(String month, String year) {
		
		return dscRepo.findMonthlyDSCEsignData(month, year);
	}

	@Override
	public List<DSCeSignIssued> getYearlyDSCEsignData(String years) {
		return dscRepo.findYearlyDSCEsignData(years);
	}

	@Override
	public DSCeSignIssued getDSCeSignIssuedByYearMonthCountryAndStateUsername(String caUsername, String countryId,
			String stateId, String year, String month) {
		return dscRepo.getDSCeSignIssuedByYearMonthCountryAndStateUsername(caUsername, countryId, stateId, year, month);
	}

	@Override
	public List<DSCeSignIssued> getAllDSCeSignIssuedByYearMonthAndUsername(String username, String month, String year) {
		
		
		
		return dscRepo.getAllDSCeSignIssuedByYearMonthAndUsername(username, month, year);
	}

	
}
