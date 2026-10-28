package in.lms.cca.controller;


import java.util.ArrayList;
import java.util.HashMap;
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

import in.lms.cca.dto.IntentDTO;
import in.lms.cca.dto.IntentLoginDTO;
import in.lms.cca.dto.LoginDTO;
import in.lms.cca.dto.UserLoginDTO;
import in.lms.cca.entity.Address;
import in.lms.cca.entity.City;
import in.lms.cca.entity.Country;
import in.lms.cca.entity.Intent;
import in.lms.cca.entity.IntentUniqueCode;
import in.lms.cca.entity.State;
import in.lms.cca.service.IAddressService;
import in.lms.cca.service.IAddressTypeService;
import in.lms.cca.service.ICityService;
import in.lms.cca.service.ICountryService;
import in.lms.cca.service.IIntentService;
import in.lms.cca.service.IIntentUniqueCodeService;
import in.lms.cca.service.IStateService;
import in.lms.cca.service.IUserLoginService;
import in.lms.cca.util.api.AdminServiceAPIs;
import in.lms.cca.util.api.IntentServiceAPIs;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
//@RequestMapping(AdminServiceAPIs.ADMIN_SERVICE_BASE_URL)
@RequestMapping("/")
@CrossOrigin
public class IntentController {
	
	@Autowired
	private IStateService stateServ;
	
	@Autowired
	private ICountryService countryServ;
	
	@Autowired
	private ICityService cityServ;
	
	@Autowired
	private IAddressTypeService addressTypeServ;
	
	@Autowired
	private IAddressService addressServ;
	
	@Autowired
	private IIntentService intentServ;
	
	@Autowired
	private IIntentUniqueCodeService uniqueCodeServ;
	
	@Autowired
	private IUserLoginService userLoginServ;
	
	//Intent Registration
	  @PostMapping(value = {IntentServiceAPIs.ADD_INTENT, "/admin-service/add-intent"})
	    public ResponseEntity<?> addNewIntent(@RequestBody IntentDTO intentObj) {
		  
	        // Server Side Validation

	        if (intentObj.getSalutation().isEmpty()) {
	            return new ResponseEntity<>("Please enter salutation.", HttpStatus.BAD_REQUEST);
	        }

	        if (intentObj.getFirstName().isEmpty()) {
	            return new ResponseEntity<>("Please enter first name.", HttpStatus.BAD_REQUEST);
	        } else if (!intentObj.getFirstName().trim().matches("^[A-Za-z]+$")) {
	            return new ResponseEntity<>("Only alphabets are allowed in the first name.", HttpStatus.BAD_REQUEST);
	        } else if (intentObj.getFirstName().length() < 3 || intentObj.getFirstName().length() > 30) {
	            return new ResponseEntity<>("The length of the first name must be between 3 and 30 characters.", HttpStatus.BAD_REQUEST);
	        }
	        
	        if (intentObj.getMiddleName().isEmpty()) {
	            
	        } else if (!intentObj.getMiddleName().trim().matches("^[A-Za-z]+$")) {
	            return new ResponseEntity<>("Only alphabets are allowed in the middle name.", HttpStatus.BAD_REQUEST);
	        } else if (intentObj.getMiddleName().length() < 3 || intentObj.getMiddleName().length() > 30) {
	            return new ResponseEntity<>("The length of the middle name must be between 3 and 30 characters.", HttpStatus.BAD_REQUEST);
	        }


	        if (intentObj.getLastName().isEmpty()) {
	            
	        } else if (!intentObj.getLastName().trim().matches("^[A-Za-z]+$")) {
	            return new ResponseEntity<>("Only alphabets are allowed in the last name.", HttpStatus.BAD_REQUEST);
	        } else if (intentObj.getLastName().length() < 3 || intentObj.getLastName().length() > 45) {
	            return new ResponseEntity<>("The length of the last name must be between 3 and 45 characters.", HttpStatus.BAD_REQUEST);
	        }

	        if (intentObj.getMobile().isEmpty()) {
	            return new ResponseEntity<>("Please enter mobile number.", HttpStatus.BAD_REQUEST);
	        } else if (!intentObj.getMobile().matches("^[0-9]{10}$")) {
	            return new ResponseEntity<>("Mobile number must be 10 digits.", HttpStatus.BAD_REQUEST);
	        }

	        if (intentObj.getBlockNo().isEmpty()) {
	            return new ResponseEntity<>("Please enter block number.", HttpStatus.BAD_REQUEST);
	        } else if (!intentObj.getBlockNo().trim().matches("^[A-Za-z0-9 ]+$")) {
	            return new ResponseEntity<>("Only alphabets, numbers, and spaces are allowed in the block number.", HttpStatus.BAD_REQUEST);
	        } else if (intentObj.getBlockNo().length() < 3 || intentObj.getBlockNo().length() > 15) {
	            return new ResponseEntity<>("The length of the block number must be between 3 and 15 characters.", HttpStatus.BAD_REQUEST);
	        }

	        if (intentObj.getVillage().isEmpty()) {
	            return new ResponseEntity<>("Please enter village name.", HttpStatus.BAD_REQUEST);
	        } else if (!intentObj.getVillage().trim().matches("^[A-Za-z0-9 ]+$")) {
	            return new ResponseEntity<>("Only alphabets, numbers, and spaces are allowed in the village name.", HttpStatus.BAD_REQUEST);
	        } else if (intentObj.getVillage().length() < 3 || intentObj.getVillage().length() > 30) {
	            return new ResponseEntity<>("The length of the village name must be between 3 and 30 characters.", HttpStatus.BAD_REQUEST);
	        }

	        if (intentObj.getPostOffice().isEmpty()) {
	            return new ResponseEntity<>("Please enter post office.", HttpStatus.BAD_REQUEST);
	        } else if (!intentObj.getPostOffice().trim().matches("^[A-Za-z ]+$")) {
	            return new ResponseEntity<>("Only alphabets and spaces are allowed in the post office.", HttpStatus.BAD_REQUEST);
	        } else if (intentObj.getPostOffice().length() < 3 || intentObj.getPostOffice().length() > 30) {
	            return new ResponseEntity<>("The length of the post office must be between 3 and 30 characters.", HttpStatus.BAD_REQUEST);
	        }

	        if (intentObj.getSubDivision().isEmpty()) {
	            return new ResponseEntity<>("Please enter subdivision.", HttpStatus.BAD_REQUEST);
	        } else if (!intentObj.getSubDivision().trim().matches("^[A-Za-z ]+$")) {
	            return new ResponseEntity<>("Only alphabets and spaces are allowed in the subdivision.", HttpStatus.BAD_REQUEST);
	        } else if (intentObj.getSubDivision().length() < 3 || intentObj.getSubDivision().length() > 30) {
	            return new ResponseEntity<>("The length of the subdivision must be between 3 and 30 characters.", HttpStatus.BAD_REQUEST);
	        }

	        if (intentObj.getCountry().isEmpty()) {
	            return new ResponseEntity<>("Please select a country.", HttpStatus.BAD_REQUEST);
	        }

	        if (intentObj.getCity().isEmpty()) {
	            return new ResponseEntity<>("Please select a city.", HttpStatus.BAD_REQUEST);
	        }

	        if (intentObj.getState().isEmpty()) {
	            return new ResponseEntity<>("Please select a state.", HttpStatus.BAD_REQUEST);
	        }

	        if (intentObj.getPin().isEmpty()) {
	            return new ResponseEntity<>("Please enter PIN.", HttpStatus.BAD_REQUEST);
	        } else if (!intentObj.getPin().matches("^[0-9]{6}$")) {
	            return new ResponseEntity<>("PIN must be 6 digits.", HttpStatus.BAD_REQUEST);
	        }

	        if (intentObj.getEmailId().isEmpty()) {
	            return new ResponseEntity<>("Please enter email ID.", HttpStatus.BAD_REQUEST);
	        } else if (!intentObj.getEmailId().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
	            return new ResponseEntity<>("Please enter a valid email address.", HttpStatus.BAD_REQUEST);
	        }
	        
	        if (intentObj.getUniqueCode().isEmpty()) {
	            return new ResponseEntity<>("Please enter unique code.", HttpStatus.BAD_REQUEST);
	        } else if (!intentObj.getUniqueCode().matches("^[0-9]+$")) {
	            return new ResponseEntity<>("Please enter a valid code.", HttpStatus.BAD_REQUEST);
	        }else if (intentObj.getUniqueCode().length() !=8) {
	            return new ResponseEntity<>("Please enter a valid code.", HttpStatus.BAD_REQUEST);
	        }
	        
	        try {
	        	
	            intentServ.registerIntent(intentObj);
	            
	            return new ResponseEntity<>("You have successfully registered. Your username and password send to your registered email.", HttpStatus.OK);

	        } catch (IllegalArgumentException e) {
	            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return new ResponseEntity<>("An error occurred while registration. Please Try again.", HttpStatus.BAD_REQUEST);
	        }
	    }
	
	  
	//Get All Intent
		@GetMapping(IntentServiceAPIs.GET_ALL_INTENT)
		public ResponseEntity<?> getAllIntent(){
			
			List<Intent> intentList =intentServ.getAllIntent();
			
			return new ResponseEntity<>(intentList, HttpStatus.OK); 
			
		}
	
	
	 //Get All Intent With Account Status
		@GetMapping(IntentServiceAPIs.GET_ALL_INTENT_LOGIN_DETAILS)
		public ResponseEntity<?> getAllIntentLoginStatus(){
			
			List<Intent> intentList =intentServ.getAllIntent();
			List<UserLoginDTO> userLogin = userLoginServ.getAllIntentUserLogins();
			
			HashMap<Long, String> hashmap = new HashMap<>();
			
			for(UserLoginDTO i: userLogin) {
				hashmap.put(Long.parseLong(EncryptionUtil.decrypt(i.getUserId())), i.getStatus());
			}
			
			List<IntentLoginDTO> intent = new ArrayList<>();
			
			for(Intent in: intentList) {
				
				IntentLoginDTO l = new IntentLoginDTO();
				l.setIntent(in);
				
				if(hashmap.get(in.getIntentId()) != null && in.getStatus().equals("Active")) {
					l.setAccountStatus(hashmap.get(in.getIntentId()));
				}
				
				intent.add(l);
			}
			
			return new ResponseEntity<>(intent, HttpStatus.OK); 
			
		}
	
	
	//Verify Intent Account
	@GetMapping(IntentServiceAPIs.VERIFY_INTENT_DETAILS)
	public ResponseEntity<?> verifyIntentDetails(@RequestParam("id") String intentId, @RequestParam("qid") String username) {
		
		String id = EncryptionUtil.decrypt(intentId);
		
		
		
		try {
			
			Intent c = intentServ.getIntentById(Long.parseLong(id));
			
			
			//Create login account of intent
			UserLoginDTO u = new UserLoginDTO();
			u.setCreatedBy(username);
			u.setUpdatedBy(username);
			u.setEmailId(c.getUniqueCodeId().getEmailId());
			u.setMobile(c.getUniqueCodeId().getMobileNo());
			u.setSalutation(c.getSalutation());
			u.setFirstName(c.getFirstName());
			u.setMiddleName(c.getMiddleName());
			u.setLastName(c.getLastName());
			u.setStatus("Active");
			u.setUserId(EncryptionUtil.encrypt(c.getIntentId().toString()));
			
			userLoginServ.addUser(u);
			
			c.setStatus("Active");
			intentServ.updateIntent(c);
		
			return new ResponseEntity<>(HttpStatus.OK); 
		}
		catch(Exception e) {
			return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST); 
		}
	}
	
	/*------*/
	// Get Intent Details By Id	
	@GetMapping(IntentServiceAPIs.GET_INTENT_BY_ID)
	public ResponseEntity<?> getIntentByID(@RequestParam("id") String intentId)
	{
		String id = EncryptionUtil.decrypt(intentId);
		
		try {
			Intent intentObj = intentServ.getIntentById(Long.parseLong(id));
			return new ResponseEntity<>(intentObj, HttpStatus.OK); 
		}catch(Exception e) {
			return new ResponseEntity<>("Invalid Intent Id.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	// Change intent account status by user id
		@GetMapping(IntentServiceAPIs.CHANGE_INTENT_ACCOUNT_STATUS)
		public ResponseEntity<?> changeIntentAccountStatusByUserId(@RequestParam("id") String intentId, @RequestParam("qid") String username)
		{
			String id = EncryptionUtil.decrypt(intentId);
			try {
				userLoginServ.changeIntentLoginStatusByUserId(EncryptionUtil.encrypt(id), username);
				return new ResponseEntity<>(HttpStatus.OK); 
			}catch(Exception e) {
				return new ResponseEntity<>("Invalid Intent Id.", HttpStatus.BAD_REQUEST);
			}
			
		}
	
//		@GetMapping(IntentServiceAPIs.GET_INTENT_BY_USERNAME)
//		public ResponseEntity<?> getIntentByUserName(@RequestParam("userName") String userName) {
//		    try {
//		        // Decrypt userName
//		        String decryptedUserName = EncryptionUtil.decrypt(userName);
//		        
//		        // Fetch the LoginDTO based on the decrypted username
//		        LoginDTO loginDTO = intentServ.getIntentByUserName(decryptedUserName);
//		        
//		        if (loginDTO == null) {
//		            return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
//		        }
//		        
//		        // Decrypt the userId from loginDTO
//		        String decryptedUserId = EncryptionUtil.decrypt(loginDTO.getUserId());
//		        System.out.println("Decrypted UserId: " + decryptedUserId);
//		        
//		        // Fetch the Intent by the decrypted userId
//		        Intent intentObj = intentServ.getIntentByUserId(Long.parseLong(decryptedUserId));
//		        
//		        if (intentObj == null) {
//		            return new ResponseEntity<>("Intent not found.", HttpStatus.NOT_FOUND);
//		        }
//		        
//		        // Log intent details
//		        System.out.println("Intent First Name: " + intentObj.getFirstName());
//		        
//		        // Fetch the IntentUniqueCode object directly
//		        IntentUniqueCode uniqueCodeObj = intentObj.getUniqueCodeId();  // Assuming getUniqueCodeId() returns an object
//		        
//		        // Fetch details from IntentUniqueCode if necessary
//		        if (uniqueCodeObj != null) {
//		        	IntentUniqueCode intentUniqueCodeDTO = uniqueCodeServ.getUniqueCodeById(uniqueCodeObj.getUniqueCodeId());  // Assuming IntentUniqueCode has a getId() method
//		            return new ResponseEntity<>(intentUniqueCodeDTO, HttpStatus.OK);
//		        	//return new ResponseEntity<>(intentObj, HttpStatus.OK);
//		        } else {
//		            return new ResponseEntity<>("Unique Code not found.", HttpStatus.NOT_FOUND);
//		            //return new ResponseEntity<>(intentObj, HttpStatus.OK);
//		        }
//
//		    } catch (NumberFormatException e) {
//		        return new ResponseEntity<>("Invalid User ID format.", HttpStatus.BAD_REQUEST);
//		    } catch (Exception e) {
//		        e.printStackTrace();
//		        return new ResponseEntity<>("An error occurred while fetching intent.", HttpStatus.INTERNAL_SERVER_ERROR);
//		    }
//		}

		
		
		// *** THIS IS THE FIX: Explicit path matching your frontend ***
//				@GetMapping("/get-intent-by-username")
		//@GetMapping(IntentServiceAPIs.GET_INTENT_BY_USERNAME)
		@GetMapping(value = {"/get-intent-by-username", "/admin-service/get-intent-by-username"})
	    public ResponseEntity<?> getIntentByUserName(@RequestParam("userName") String userName) {
	        try {
	            // Decrypt userName
	            String decryptedUserName = EncryptionUtil.decrypt(userName);
	            
	            // Fetch the LoginDTO based on the decrypted username
	            LoginDTO loginDTO = intentServ.getIntentByUserName(decryptedUserName);
	            
	            if (loginDTO == null) {
	                return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
	            }
	            
	            // Decrypt the userId from loginDTO
	            String decryptedUserId = EncryptionUtil.decrypt(loginDTO.getUserId());
	            System.out.println("Decrypted UserId: " + decryptedUserId);
	            
	            // Fetch the Intent by the decrypted userId
	            Intent intentObj = intentServ.getIntentByUserId(Long.parseLong(decryptedUserId));
	            
//	            if (intentObj == null) {
//	                return new ResponseEntity<>("Intent not found.", HttpStatus.NOT_FOUND);
//	            }
//	            
	            
	            if (intentObj == null) {
	                // Return OK status but with null/empty body. 
	                // This tells the frontend "Request succeeded, but no data exists," stopping the red console error.
	                return new ResponseEntity<>(null, HttpStatus.OK); 
	            }
	            // Log intent details
	            System.out.println("Intent First Name: " + intentObj.getFirstName());
	            
	            // Fetch the IntentUniqueCode object directly
	            IntentUniqueCode uniqueCodeObj = intentObj.getUniqueCodeId(); 
	            
	            // Fetch details from IntentUniqueCode if necessary
	            if (uniqueCodeObj != null) {
	                IntentUniqueCode intentUniqueCodeDTO = uniqueCodeServ.getUniqueCodeById(uniqueCodeObj.getUniqueCodeId());
	                return new ResponseEntity<>(intentUniqueCodeDTO, HttpStatus.OK);
	            } else {
	                return new ResponseEntity<>("Unique Code not found.", HttpStatus.NOT_FOUND);
	            }

	        } catch (NumberFormatException e) {
	            return new ResponseEntity<>("Invalid User ID format.", HttpStatus.BAD_REQUEST);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return new ResponseEntity<>("An error occurred while fetching intent.", HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
}