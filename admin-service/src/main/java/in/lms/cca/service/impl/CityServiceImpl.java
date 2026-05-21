package in.lms.cca.service.impl;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.lms.cca.dto.CityDTO;
import in.lms.cca.entity.City;
import in.lms.cca.entity.State;
import in.lms.cca.entity.logs.CityLogs;
import in.lms.cca.entity.logs.stateLogs;
import in.lms.cca.repository.CityRepository;
import in.lms.cca.repository.logs.CityRepositoryLogs;
import in.lms.cca.service.ICityService;
import in.lms.cca.util.global.Constant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class CityServiceImpl implements ICityService{

	
	@Autowired
	private CityRepository cityRepo;
	
	@Autowired
	private HttpServletRequest request;
	
	 private final Set<String> clientHostnames = new HashSet<>();
	 
	 @Autowired
	 private CityRepositoryLogs log;
	
	@Override
	public Optional<City> addCity(City cityObj) {
		
		if(cityObj == null)
			return Optional.empty();
		
		try {

		   
		        String ipAddress = request.getRemoteAddr();
		        
		        InetAddress clientAddress = InetAddress.getByName(ipAddress);
		        String clientHostname = clientAddress.getHostName();
		        String bearerToken = request.getHeader("Authorization");
		        String usernameFromToken = "1";
		        City savedCity = cityRepo.save(cityObj);
		        if (usernameFromToken != null) {
		            log.save(new CityLogs(cityObj, ipAddress, Constant.CREATED, usernameFromToken, clientHostname));
		        }
		        System.out.println("Client Hostnames: " + clientHostname);
		        System.out.println("Client IP Address: " + ipAddress);
		        System.out.println("Token: " + bearerToken);
		        System.out.println("Username from Token: " + usernameFromToken);

		        return Optional.of(savedCity);
		    
		}catch(Exception e) {
			return Optional.empty();
		}
		
	}

	@Override
	public Optional<City> updateCity(City cityObj) {
		if(cityObj == null)
			return Optional.empty();
		
		if(cityObj.getCityId() == null)
			return Optional.empty();
		
		try {

			   
	        String ipAddress = request.getRemoteAddr();
	        
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";
	        City savedCity = cityRepo.save(cityObj);
	        if (usernameFromToken != null) {
	            log.save(new CityLogs(cityObj, ipAddress, Constant.UPDATED, usernameFromToken, clientHostname));
	        }
	        System.out.println("Client Hostnames: " + clientHostname);
	        System.out.println("Client IP Address: " + ipAddress);
	        System.out.println("Token: " + bearerToken);
	        System.out.println("Username from Token: " + usernameFromToken);

	        return Optional.of(savedCity);
	    
	}catch(Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	@Override
	public List<City> getAllCity() {
		return cityRepo.findAll(Sort.by(Sort.Direction.DESC, "created"));
	}

	@Override
	public City getCityById(Long id) {
		return cityRepo.findByCityId(id);
	}

	@Override
	public List<City> getAllActiveCity() {
		return cityRepo.findAllActiveCity();
	}

	@Override
	public List<City> getAllInActiveCity() {
		return cityRepo.findAllInActiveCity();
	}

	@Override
	public List<City> getAllCityByStateId(Long id) {
		return cityRepo.findCityByStateId(id);
	}

	@Override
	public City getCityName(String cityName) {
		// TODO Auto-generated method stub
		return cityRepo.findByCityName(cityName);
	}

	@Override
	public boolean deleteByCityId(long cityId) {
	    City cityObj = cityRepo.findByCityId(cityId);

	    if (cityObj == null)
	        return false;

	    try {
	      
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";
	        boolean isDeleted = cityRepo.deletebyCityId(cityId);

	        // Log the deletion action if delete was successful
	        if (isDeleted && usernameFromToken != null) {
	            log.save(new CityLogs(cityObj, ipAddress, Constant.DELETED, usernameFromToken, clientHostname));
	        }

	        // Optionally, print debug information
	        System.out.println("Client Hostname: " + clientHostname);
	        System.out.println("Client IP Address: " + ipAddress);
	        System.out.println("Token: " + bearerToken);
	        System.out.println("Username from Token: " + usernameFromToken);

	        return isDeleted;
	    } catch (Exception e) {
	        return false;
	    }
	}

	
}
