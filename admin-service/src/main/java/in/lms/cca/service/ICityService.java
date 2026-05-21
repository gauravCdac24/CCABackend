package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.dto.CityDTO;
import in.lms.cca.entity.City;
import in.lms.cca.entity.State;

public interface ICityService {

	Optional<City> addCity(City cityObj);
	Optional<City> updateCity(City cityObj);
	List<City> getAllCity();
	City getCityById(Long id);
	List<City> getAllActiveCity();
	List<City> getAllInActiveCity();
	List<City> getAllCityByStateId(Long id);
	City getCityName(String cityName);
	boolean deleteByCityId(long cityId);

}
