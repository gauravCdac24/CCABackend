package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.Country;
import in.lms.cca.repository.CountryRepository;
import in.lms.cca.service.ICountryService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class CountryServiceImpl implements ICountryService{

	@Autowired
	private CountryRepository countryRepo;
	
	@Override
	public Optional<Country> addCountry(Country countryObj) {
		
		if(countryObj == null)
			return Optional.empty();
		
		try {
			return Optional.of(countryRepo.save(countryObj));
		}catch(Exception e) {
			return Optional.empty();
		}
	}

	@Override
	public Optional<Country> updateCountry(Country countryObj) {
		
		if(countryObj == null)
			return Optional.empty();
		
		if(countryObj.getCountryId() == null)
			return Optional.empty();
		
		try {
			return Optional.of(countryRepo.save(countryObj));
		}catch(Exception e) {
			return Optional.empty();
		}
		
	}

	@Override
	public List<Country> getAllCountry() {

		return countryRepo.findAll(Sort.by(Sort.Direction.DESC, "created"));

	}

	@Override
	public List<Country> getAllActiveCountry() {

		return countryRepo.findAllActiveCountry();
		
	}

	@Override
	public List<Country> getAllInactiveCountry() {
		
		return countryRepo.findAllInActiveCountry();
	}

	@Override
	public boolean deleteByCountryId(Long countryId) {

		Country cobj = countryRepo.findByCountryId(countryId);
		
		if(cobj == null)
			return false;
		try {
			countryRepo.deleteByCountryId(countryId);
			return true;
		}catch(Exception e) {
			return false;
		}
		

	}

	@Override
	public Country getCountryById(Long countryId) {
		
		return countryRepo.findByCountryId(countryId);
	}

	@Override
	public Country getCountryByName(String countryName) {
		return countryRepo.findByCountryName(countryName);
	}

	@Override
	public Country getCountryByPhoneCode(String phoneCode) {
		return countryRepo.findByPhoneCode(phoneCode);
	}



}
