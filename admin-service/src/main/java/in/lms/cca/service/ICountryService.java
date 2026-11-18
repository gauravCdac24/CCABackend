package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.Country;

public interface ICountryService {

	Optional<Country> addCountry(Country countryObj);
	Optional<Country> updateCountry(Country countryObj);
	List<Country> getAllCountry();
	List<Country> getAllActiveCountry();
	List<Country> getAllInactiveCountry();
	boolean deleteByCountryId(Long countryId);
	Country getCountryById(Long countryId);
	Country getCountryByName(String countryName);
	Country getCountryByPhoneCode(String phoneCode);
}
