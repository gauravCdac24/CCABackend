package in.lms.cca.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
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

import in.lms.cca.dto.CountryDTO;
import in.lms.cca.entity.Country;
import in.lms.cca.service.ICountryService;
import in.lms.cca.util.api.AdminServiceAPIs;
import in.lms.cca.util.api.CountryServiceAPIs;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
@RequestMapping(AdminServiceAPIs.ADMIN_SERVICE_BASE_URL)
@CrossOrigin
public class CountryController {

	@Autowired
	private ICountryService countryServ;
	
	@PostMapping(CountryServiceAPIs.ADD_NEW_COUNTRY)
	public ResponseEntity<?> addNewCountry(@RequestBody Country countryObj){
		
		
		
		//Server Side Validation
		
		if(countryObj.getCountryName().equals("")) {
			return new ResponseEntity<>("Please enter country name.", HttpStatus.BAD_REQUEST);
		}else if(!countryObj.getCountryName().trim().matches("^[A-Za-z ]+$")) {
			return new ResponseEntity<>("Only alphabets and spaces are allowed.", HttpStatus.BAD_REQUEST);
		}else if(countryObj.getCountryName().length() < 3 || countryObj.getCountryName().length() > 60) {
			return new ResponseEntity<>("The length must be between 3 and 60 characters.", HttpStatus.BAD_REQUEST);
		}
				
		
		if(countryObj.getPhoneCode().equals("")) {
			return new ResponseEntity<>("Please enter phone code.", HttpStatus.BAD_REQUEST);
		}else if(!countryObj.getPhoneCode().trim().matches("^\\d+(-\\d+)?$")) {
			return new ResponseEntity<>("Only digits and hyphens are allowed. The first character must be a digit, only one hyphen is permitted, and the last character cannot be a hyphen.", HttpStatus.BAD_REQUEST);
		}else if(countryObj.getPhoneCode().length() < 2 || countryObj.getPhoneCode().length() > 5) {
			return new ResponseEntity<>("The length must be between 2 and 5 characters.", HttpStatus.BAD_REQUEST);
		}	
				
		//Check for Unique Country Name
		Country cobj = countryServ.getCountryByName(countryObj.getCountryName().trim());
		if(cobj != null) {
			return new ResponseEntity<>("Duplicate Country name is not allowed.", HttpStatus.BAD_REQUEST);
		}
		
		//Check for Unique Phone Code
		Country cobj1 = countryServ.getCountryByPhoneCode(countryObj.getPhoneCode().trim());
		if(cobj1 != null) {
			return new ResponseEntity<>("Duplicate Phone Code is not allowed.", HttpStatus.BAD_REQUEST);
		}
		
		
		try {
			
			Country newCountry = new Country();
			newCountry.setCountryName(countryObj.getCountryName());
			newCountry.setPhoneCode(countryObj.getPhoneCode());
			newCountry.setStatus("Active");
			
			Optional<Country> c = countryServ.addCountry(newCountry);
			
			if(c.isEmpty())
				return new ResponseEntity<>("An error occurred while saving the country. Please Try again.", HttpStatus.OK);
			return new ResponseEntity<>("Country successfully added.", HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("An error occurred while saving the country. Please Try again.", HttpStatus.BAD_REQUEST);
		}
		
 
		
	}
	
	
	@GetMapping(CountryServiceAPIs.GET_ALL_COUNTRY)
	public ResponseEntity<?> getAllCountry(){
		
		List<Country> countryList = countryServ.getAllCountry();
		return new ResponseEntity<>(countryList, HttpStatus.OK); 
		
	}
	
	@GetMapping(CountryServiceAPIs.CHANGE_COUNTRY_STATUS)
	public void changeCountryStatus(@RequestParam("id") String countryId) {
		
		String id = EncryptionUtil.decrypt(countryId);
		
		try {
			Country c = countryServ.getCountryById(Long.parseLong(id));
			
			if(c.getStatus().equals("Active"))
				c.setStatus("Inactive");
			else
				c.setStatus("Active");
			
			countryServ.updateCountry(c);
		}
		catch(Exception e) {
			
		}
		
		
		
	}
	
	@GetMapping(CountryServiceAPIs.GET_COUNTRY_BY_ID)
	public ResponseEntity<?> getCountryByID(@RequestParam("id") String countryId)
	{
		String id = EncryptionUtil.decrypt(countryId);
		
		try {
			Country countryObj = countryServ.getCountryById(Long.parseLong(id));
			return new ResponseEntity<>(countryObj, HttpStatus.OK); 
		}catch(Exception e) {
			return new ResponseEntity<>("Invalid Country Id.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@GetMapping(CountryServiceAPIs.DELETE_COUNTRY_BY_ID)
	public ResponseEntity<?> deleteCountryById(@RequestParam("id") String countryId) {
		
		String id = EncryptionUtil.decrypt(countryId);
		
		
			boolean res = countryServ.deleteByCountryId(Long.parseLong(id));
			
			if(res) {
				return new ResponseEntity<>("Country is successfully deleted.", HttpStatus.OK); 
			}
			else {
				return new ResponseEntity<>("Error in deleting the country.", HttpStatus.BAD_REQUEST);
			}
		
	}
	
	
	@PostMapping(CountryServiceAPIs.UPDATE_COUNTRY)
	public ResponseEntity<?> updateCountry(@RequestBody CountryDTO countryObj){
		
		
		String id = EncryptionUtil.decrypt(countryObj.getCountryId());
		System.out.println("cid---->"+id);
		Long cid = Long.parseLong(id);
		
		
		//Server Side Validation
		
		if(countryObj.getCountryName().equals("")) {
			return new ResponseEntity<>("Please enter country name.", HttpStatus.BAD_REQUEST);
		}else if(!countryObj.getCountryName().trim().matches("^[A-Za-z ]+$")) {
			return new ResponseEntity<>("Only alphabets and spaces are allowed.", HttpStatus.BAD_REQUEST);
		}else if(countryObj.getCountryName().length() < 3 || countryObj.getCountryName().length() > 60) {
			return new ResponseEntity<>("The length must be between 3 and 60 characters.", HttpStatus.BAD_REQUEST);
		}
				
		
		if(countryObj.getPhoneCode().equals("")) {
			return new ResponseEntity<>("Please enter phone code.", HttpStatus.BAD_REQUEST);
		}else if(!countryObj.getPhoneCode().trim().matches("^\\d+(-\\d+)?$")) {
			return new ResponseEntity<>("Only digits and hyphens are allowed. The first character must be a digit, only one hyphen is permitted, and the last character cannot be a hyphen.", HttpStatus.BAD_REQUEST);
		}else if(countryObj.getPhoneCode().length() < 2 || countryObj.getPhoneCode().length() > 5) {
			return new ResponseEntity<>("The length must be between 2 and 5 characters.", HttpStatus.BAD_REQUEST);
		}	
				
		//Check for Unique Country Name
		Country cobj = countryServ.getCountryByName(countryObj.getCountryName().trim());
		if(cobj != null && cobj.getCountryId() != cid) {
			return new ResponseEntity<>("Duplicate Country name is not allowed.", HttpStatus.BAD_REQUEST);
		}
		
		//Check for Unique Phone Code
		Country cobj1 = countryServ.getCountryByPhoneCode(countryObj.getPhoneCode().trim());
		if(cobj1 != null && cobj1.getCountryId() != cid) {
			return new ResponseEntity<>("Duplicate Phone Code is not allowed.", HttpStatus.BAD_REQUEST);
		}
		
		
		try {
			
			Country uCountry = new Country();
			uCountry.setCountryName(countryObj.getCountryName());
			uCountry.setPhoneCode(countryObj.getPhoneCode());
			uCountry.setStatus(countryObj.getStatus());
			uCountry.setCountryId(cid);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
			Date cdate = sdf.parse(countryObj.getCreated());	
			uCountry.setCreated(cdate);
			
			
			Optional<Country> c = countryServ.updateCountry(uCountry);
			
			if(c.isEmpty())
				return new ResponseEntity<>("An error occurred while updating the country. Please Try again.", HttpStatus.OK);
			return new ResponseEntity<>("Country Updated Successfully.", HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("An error occurred while updating the country. Please Try again.", HttpStatus.BAD_REQUEST);
		}
		
 
		
	}


	
}
