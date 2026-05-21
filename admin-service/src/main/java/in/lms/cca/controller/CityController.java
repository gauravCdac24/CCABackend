package in.lms.cca.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.lms.cca.dto.CityDTO;
import in.lms.cca.entity.City;
import in.lms.cca.entity.State;
import in.lms.cca.service.ICityService;
import in.lms.cca.service.IStateService;
import in.lms.cca.util.api.AdminServiceAPIs;
import in.lms.cca.util.api.CityServiceAPIs;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
@RequestMapping(AdminServiceAPIs.ADMIN_SERVICE_BASE_URL)
@CrossOrigin
public class CityController {
	
	
	@Autowired
	private IStateService stateServ;
	
	@Autowired
	private ICityService cityServ;
	
	
	@PostMapping(CityServiceAPIs.ADD_CITY)
	public ResponseEntity<?> addNewCountry(@RequestBody CityDTO cityObj){
		
		
		
		//Server Side Validation
		
		if(cityObj.getCityName().equals("")) {
			return new ResponseEntity<>("Please enter state name.", HttpStatus.BAD_REQUEST);
		}else if(!cityObj.getCityName().trim().matches("^[A-Za-z ]+$")) {
			return new ResponseEntity<>("Only alphabets and spaces are allowed.", HttpStatus.BAD_REQUEST);
		}else if(cityObj.getCityName().length() < 3 || cityObj.getCityName().length() > 60) {
			return new ResponseEntity<>("The length must be between 3 and 60 characters.", HttpStatus.BAD_REQUEST);
		}
				
		
		if(cityObj.getStateId().equals("")) {
			return new ResponseEntity<>("Please select country Name.", HttpStatus.BAD_REQUEST);
		}
		System.out.println(cityObj);	
		//Check for Unique state Name
		City cobj = cityServ.getCityName(cityObj.getCityName().trim());
		if(cobj != null) {
			return new ResponseEntity<>("Duplicate State name is not allowed.", HttpStatus.BAD_REQUEST);
		}
		
		System.out.println(cityObj.toString());
		
		try {
			
			City newCity = new City();
			newCity.setCityName(cityObj.getCityName());
			
			String id = EncryptionUtil.decrypt(cityObj.getStateId());
			
			System.out.println("---->"+id);
			
			State state =  stateServ.getStateById(Long.parseLong(id)); 
			System.out.println(state. getStateName());
			newCity.setStateId(state);
			newCity.setStatus("Active");
			System.out.println(cityObj);
			Optional<City> c = cityServ.addCity(newCity);
			System.out.println(c.toString());
			if(c.isEmpty())
				return new ResponseEntity<>("An error occurred while saving the City. Please Try again to save.", HttpStatus.OK);
			return new ResponseEntity<>("City successfully added.", HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while saving the city. Please Try again.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	@GetMapping(CityServiceAPIs.GET_ALL_CITY)
	public ResponseEntity<?> getAllAddress(){
		
		List<City> cityList = cityServ.getAllCity();
		return new ResponseEntity<>(cityList, HttpStatus.OK); 
		
	}
	@GetMapping(CityServiceAPIs.CHANGE_CITY_STATUS)
	public void changeCityStatus(@RequestParam("id") String cityId) {
		
		String id = EncryptionUtil.decrypt(cityId);
		
		try {
			City c = cityServ.getCityById(Long.parseLong(id));
			
			if(c.getStatus().equals("Active"))
				c.setStatus("Inactive");
			else
				c.setStatus("Active");
			
			cityServ.updateCity(c);
		}
		catch(Exception e) {
			
		}
		
		
		
	}
	
	@GetMapping(CityServiceAPIs.GET_CITY_BY_ID)
	public ResponseEntity<?> getCityByID(@RequestParam("id") String cityId)
	{
		String id = EncryptionUtil.decrypt(cityId);
		
		try {
			City cityObj = cityServ.getCityById(Long.parseLong(id));
			return new ResponseEntity<>(cityObj, HttpStatus.OK); 
		}catch(Exception e) {
			return new ResponseEntity<>("Invalid city Id.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@GetMapping(CityServiceAPIs.DELETE_CITY_BY_ID)
	public ResponseEntity<?> deleteCityById(@RequestParam("id") String cityId) {
		
		String id = EncryptionUtil.decrypt(cityId);
		
		
			boolean res = cityServ.deleteByCityId(Long.parseLong(id));
			
			if(res) {
				return new ResponseEntity<>("city is successfully deleted.", HttpStatus.OK); 
			}
			else {
				return new ResponseEntity<>("Error in deleting the city.", HttpStatus.BAD_REQUEST);
			}
		
	}
	
	
	@PostMapping(CityServiceAPIs.UPDATE_CITY)
	public ResponseEntity<?> updateCity(@RequestBody CityDTO cityObj){
		
		
		
		//Server Side Validation
		
		if(cityObj.getCityName().equals("")) {
			return new ResponseEntity<>("Please enter state name.", HttpStatus.BAD_REQUEST);
		}else if(!cityObj.getCityName().trim().matches("^[A-Za-z ]+$")) {
			return new ResponseEntity<>("Only alphabets and spaces are allowed.", HttpStatus.BAD_REQUEST);
		}else if(cityObj.getCityName().length() < 3 || cityObj.getCityName().length() > 60) {
			return new ResponseEntity<>("The length must be between 3 and 60 characters.", HttpStatus.BAD_REQUEST);
		}
				
		
		if(cityObj.getStateId().equals("")) {
			return new ResponseEntity<>("Please select country Name.", HttpStatus.BAD_REQUEST);
		}
		
	
		
		try {
			
			City newCity = new City();
			newCity=cityServ.getCityById(Long.parseLong(EncryptionUtil.decrypt(cityObj.getStateId())));
			newCity.setCityName(cityObj.getCityName());
			
			String id = EncryptionUtil.decrypt(cityObj.getStateId());
			
			
			
			State state =  stateServ.getStateById(Long.parseLong(id)); 
			
			newCity.setStateId(state);
			newCity.setStatus("Active");
			
			Optional<City> c = cityServ.updateCity(newCity);
			
			if(c.isEmpty())
				return new ResponseEntity<>("An error occurred while saving the City. Please Try again to save.", HttpStatus.OK);
			return new ResponseEntity<>("City successfully updated.", HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while saving the city. Please Try again.", HttpStatus.BAD_REQUEST);
		}
	
}

}
