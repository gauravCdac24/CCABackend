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

import in.lms.cca.dto.StateDTO;
import in.lms.cca.entity.Country;
import in.lms.cca.entity.State;
import in.lms.cca.service.ICountryService;
import in.lms.cca.service.IStateService;
import in.lms.cca.util.api.AdminServiceAPIs;
import in.lms.cca.util.api.StateServiceAPIs;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
@RequestMapping(AdminServiceAPIs.ADMIN_SERVICE_BASE_URL)
@CrossOrigin
public class StateController {

	@Autowired
	private IStateService stateServ;
	
	@Autowired
	private ICountryService countryServ;
	
	@PostMapping(StateServiceAPIs.ADD_NEW_STATE)
	public ResponseEntity<?> addNewCountry(@RequestBody StateDTO stateObj){
		
		
		
		//Server Side Validation
		
		if(stateObj.getStateName().equals("")) {
			return new ResponseEntity<>("Please enter state name.", HttpStatus.BAD_REQUEST);
		}else if(!stateObj.getStateName().trim().matches("^[A-Za-z ]+$")) {
			return new ResponseEntity<>("Only alphabets and spaces are allowed.", HttpStatus.BAD_REQUEST);
		}else if(stateObj.getStateName().length() < 3 || stateObj.getStateName().length() > 60) {
			return new ResponseEntity<>("The length must be between 3 and 60 characters.", HttpStatus.BAD_REQUEST);
		}
				
		
		if(stateObj.getCountryId().equals("")) {
			return new ResponseEntity<>("Please select country Name.", HttpStatus.BAD_REQUEST);
		}
		System.out.println(stateObj);	
		//Check for Unique state Name
		State cobj = stateServ.getStateName(stateObj.getStateName().trim());
		if(cobj != null) {
			return new ResponseEntity<>("Duplicate State name is not allowed.", HttpStatus.BAD_REQUEST);
		}
		
		System.out.println(stateObj.toString());
		
		try {
			
			State newState = new State();
			newState.setStateName(stateObj.getStateName());
			
			String id = EncryptionUtil.decrypt(stateObj.getCountryId());
			
			System.out.println("---->"+id);
			
			Country country =  countryServ.getCountryById(Long.parseLong(id)); 
			System.out.println(country.getCountryName());
			newState.setCountryId(country);
			newState.setStatus("Active");
			System.out.println(stateObj);
			Optional<State> c = stateServ.addState(newState);
			System.out.println(c.toString());
			if(c.isEmpty())
				return new ResponseEntity<>("An error occurred while saving the state. Please Try again to save.", HttpStatus.OK);
			return new ResponseEntity<>("State successfully added.", HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while saving the state. Please Try again.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	@GetMapping(StateServiceAPIs.GET_ALL_STATE)
	public ResponseEntity<?> getAllAddress(){
		
		List<State> stateList = stateServ.getAllState();
		return new ResponseEntity<>(stateList, HttpStatus.OK); 
		
	}
	
	@GetMapping(StateServiceAPIs.CHANGE_STATE_STATUS)
	public void changeStateStatus(@RequestParam("id") String stateId) {
		
		String id = EncryptionUtil.decrypt(stateId);
		
		try {
			State c = stateServ.getStateById(Long.parseLong(id));
			
			if(c.getStatus().equals("Active"))
				c.setStatus("Inactive");
			else
				c.setStatus("Active");
			
			stateServ.updateState(c);
		}
		catch(Exception e) {
			
		}
		
		
		
	}
	
	@GetMapping(StateServiceAPIs.GET_STATE_BY_ID)
	public ResponseEntity<?> getStateByID(@RequestParam("id") String stateId)
	{
		String id = EncryptionUtil.decrypt(stateId);
		
		try {
			State stateObj = stateServ.getStateById(Long.parseLong(id));
			return new ResponseEntity<>(stateObj, HttpStatus.OK); 
		}catch(Exception e) {
			return new ResponseEntity<>("Invalid state Id.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@GetMapping(StateServiceAPIs.DELETE_STATE_BY_ID)
	public ResponseEntity<?> deleteCountryById(@RequestParam("id") String stateId) {
		
		String id = EncryptionUtil.decrypt(stateId);
		
		
			boolean res = stateServ.deleteByStateId(Long.parseLong(id));
			
			if(res) {
				return new ResponseEntity<>("State is successfully deleted.", HttpStatus.OK); 
			}
			else {
				return new ResponseEntity<>("Error in deleting the country.", HttpStatus.BAD_REQUEST);
			}
		
	}
	
	
	@PostMapping(StateServiceAPIs.UPDATE_STATE)
	public ResponseEntity<?> updateCountry(@RequestBody StateDTO stateObj){
		
		
		
		//Server Side Validation
		
		if(stateObj.getStateName().equals("")) {
			return new ResponseEntity<>("Please enter state name.", HttpStatus.BAD_REQUEST);
		}else if(!stateObj.getStateName().trim().matches("^[A-Za-z ]+$")) {
			return new ResponseEntity<>("Only alphabets and spaces are allowed.", HttpStatus.BAD_REQUEST);
		}else if(stateObj.getStateName().length() < 3 || stateObj.getStateName().length() > 60) {
			return new ResponseEntity<>("The length must be between 3 and 60 characters.", HttpStatus.BAD_REQUEST);
		}
				
		
		if(stateObj.getCountryId().equals("")) {
			return new ResponseEntity<>("Please select country Name.", HttpStatus.BAD_REQUEST);
		}
		System.out.println(stateObj);	
		//Check for Unique state Name
		State cobj = stateServ.getStateName(stateObj.getStateName().trim());
		if(cobj != null) {
			return new ResponseEntity<>("Duplicate State name is not allowed.", HttpStatus.BAD_REQUEST);
		}
		
		System.out.println(stateObj.toString());
		
		try {
			
			State newState = new State();
			newState=stateServ.getStateById(Long.parseLong(EncryptionUtil.decrypt(stateObj.getStateId())));
			newState.setStateName(stateObj.getStateName());
			
			String id = EncryptionUtil.decrypt(stateObj.getCountryId());
			
			System.out.println("---->"+id);
			
			Country country =  countryServ.getCountryById(Long.parseLong(id)); 
			System.out.println(country.getCountryName());
			newState.setCountryId(country);
			newState.setStatus("Active");
			System.out.println(stateObj);
			Optional<State> c = stateServ.updateState(newState);
			System.out.println(c.toString());
			if(c.isEmpty())
				return new ResponseEntity<>("An error occurred while saving the state. Please Try again to save.", HttpStatus.OK);
			return new ResponseEntity<>("State successfully updated.", HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while saving the state. Please Try again.", HttpStatus.BAD_REQUEST);
		}
			
}
	
	
	
	@GetMapping(StateServiceAPIs.GET_ALL_STATE_BY_COUNTRY_NAME)
	public ResponseEntity<?> getAllStateByCountryBy(@RequestParam("id") String countryName) {
		
		
		
	
		
		List<State> stateList = stateServ.getAllStateByCountryName(countryName);
			
	
		return new ResponseEntity<>(stateList, HttpStatus.OK); 
			
		
	}
	
	
	
	
	
	
	
	
	
	
}