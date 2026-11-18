package in.lms.cca.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.lms.cca.dto.AddressDTO;
import in.lms.cca.dto.IndivAddressDTO;
import in.lms.cca.dto.LocationDetailsDTO;
import in.lms.cca.entity.Address;
import in.lms.cca.entity.City;
import in.lms.cca.entity.Country;
import in.lms.cca.entity.State;
import in.lms.cca.service.IAddressService;
import in.lms.cca.service.ICityService;
import in.lms.cca.service.ICountryService;
import in.lms.cca.service.IStateService;
import in.lms.cca.util.api.AdminServiceAPIs;
import in.lms.cca.util.api.CountryServiceAPIs;
import in.lms.cca.util.global.LocationMapping;
import in.lms.cca.util.api.AddressServiceAPIs;

@RestController
@RequestMapping(AdminServiceAPIs.ADMIN_SERVICE_BASE_URL)
@CrossOrigin
public class AddressController {
	
	@Autowired
	private IAddressService addressServ;

	@Autowired
	private ICountryService countryServ;

	@Autowired
	private IStateService stateServ;

	@Autowired
	private ICityService cityServ;

	private void applyMappedLocation(Address address, String countryId, String stateId, String cityId) {
		Country country = countryServ.getCountryById(LocationMapping.parseId(countryId));
		State state = stateServ.getStateById(LocationMapping.parseId(stateId));
		City city = cityServ.getCityById(LocationMapping.parseId(cityId));
		LocationMapping.assertMapped(country, state, city);
		address.setCountryId(country);
		address.setStateId(state);
		address.setCityId(city);
	}
	
	@PostMapping(AddressServiceAPIs.ADD_ADDRESS)
	public ResponseEntity<?> addAddress(@RequestBody Address addressObj){
		
		
		addressServ.addAddress(addressObj);
		
		return new ResponseEntity<>("Address Successfully Added", HttpStatus.OK); 
		
	}
	@PostMapping(AddressServiceAPIs.ADDS_ADDRESS)
	 public ResponseEntity<?> addAddress(@RequestBody AddressDTO addressObj) {
        if (addressObj == null) {
            return ResponseEntity.badRequest().body("Invalid address data.");
        }

        try {
            
        		Address address = new Address();

        		address.setBlockNo(addressObj.getBlockNo());
        		address.setVillage(addressObj.getVillage());
        		address.setPostOffice(addressObj.getPostOffice());
        		address.setSubDivision(addressObj.getSubDivision());
                applyMappedLocation(address, addressObj.getCountry(), addressObj.getState(), addressObj.getCity());
                address.setPincode(addressObj.getPin());
                address.setFax(addressObj.getFax());
                address.setTelephoneNo(addressObj.getTelephoneNo());
                address.setMobile(addressObj.getMobile());
                address.setCommicationAddress(addressObj.getCommunicationAddress());
                
                System.out.println("----->"+addressObj.toString());

            Optional<Address> resAddObj = addressServ.addAddres(address);
            	
            return new ResponseEntity<>(resAddObj.get().getAddressId(), HttpStatus.OK);
	            
           
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            
            e.printStackTrace(); 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }
	
	@PostMapping(AddressServiceAPIs.SAVE_ADDRESS)
	 public ResponseEntity<?> addAddress(@RequestBody LocationDetailsDTO locationDetailsDTO) {
       if (locationDetailsDTO == null) {
           return ResponseEntity.badRequest().body("Invalid address data.");
       }

       try {
           
       	Address address = new Address();
           address.setBlockNo(locationDetailsDTO.getBlockNo());
           	address.setVillage(locationDetailsDTO.getVillage());
           address.setPostOffice(locationDetailsDTO.getPostOffice());
           	address.setSubDivision(locationDetailsDTO.getSubDivision());
               applyMappedLocation(address, locationDetailsDTO.getCountry(), locationDetailsDTO.getState(), locationDetailsDTO.getCity());
               address.setPincode(locationDetailsDTO.getPin());
               
           

           // Set Official Address if available
        

           Optional<Address> resAddObj = addressServ.addAddres(address);
         

           if (resAddObj.isPresent()) {
	            	return new ResponseEntity<>(resAddObj.get().getAddressId(), HttpStatus.OK);
           }
           else {
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add address.");
           }
       } catch (IllegalArgumentException e) {
           return ResponseEntity.badRequest().body(e.getMessage());
       } catch (Exception e) {
           // Log the exception with a proper logging framework
           e.printStackTrace(); // Replace with logger in real applications
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
       }
   }
	
	
	@GetMapping("/get-address-by-id/{addressId}")
	public ResponseEntity<?> getCountryByID(@PathVariable String addressId) {
	    // Optionally decrypt the ID if encryption is being used
	    // String id = EncryptionUtil.decrypt(addressId);
	    // addressId = id; // Assign decrypted id to addressId

	    System.out.println("Id ==> " + addressId);

	    try {
	        // Try to parse the addressId to Long
	        Long parsedAddressId = Long.parseLong(addressId);
	        
	        // Retrieve the address object using the parsed id
	        Address addressObj = addressServ.getAddressById(parsedAddressId);

	        if (addressObj == null) {
	            // Handle case when the address is not found
	            return new ResponseEntity<>("Address not found for the given ID.", HttpStatus.NOT_FOUND);
	        }

	        // Map the entity to DTO
	        AddressDTO addressDTO = new AddressDTO();
	        addressDTO.setAddressId(String.valueOf(addressObj.getAddressId()));
	        addressDTO.setBlockNo(addressObj.getBlockNo());
	        if (addressObj.getCityId() != null) {
	            addressDTO.setCity(String.valueOf(addressObj.getCityId().getCityId()));
	            addressDTO.setCityName(addressObj.getCityId().getCityName());
	        }
	        if (addressObj.getStateId() != null) {
	            addressDTO.setState(String.valueOf(addressObj.getStateId().getStateId()));
	            addressDTO.setStateName(addressObj.getStateId().getStateName());
	        }
	        if (addressObj.getCountryId() != null) {
	            addressDTO.setCountry(String.valueOf(addressObj.getCountryId().getCountryId()));
	            addressDTO.setCountryName(addressObj.getCountryId().getCountryName());
	        }
	        addressDTO.setBlockNo(addressObj.getBlockNo());
	        addressDTO.setVillage(addressObj.getVillage());
	        addressDTO.setPostOffice(addressObj.getPostOffice());
	        addressDTO.setSubDivision(addressObj.getSubDivision());
	        addressDTO.setPin(addressObj.getPincode());
	        addressDTO.setFax(addressObj.getFax());
	        addressDTO.setMobile(addressObj.getMobile());
	        addressDTO.setTelephoneNo(addressObj.getTelephoneNo());
	        addressDTO.setCommunicationAddress(addressObj.getCommicationAddress());
	        addressDTO.setStatus(addressObj.getStatus());

	        System.out.println("Country Name ==> " + addressObj.getCountryId().getCountryName());

	        // Return the DTO in response
	        return new ResponseEntity<>(addressDTO, HttpStatus.OK);

	    } catch (NumberFormatException e) {
	        // Handle invalid addressId format (not a number)
	        return new ResponseEntity<>("Invalid Address ID format. It must be a number.", HttpStatus.BAD_REQUEST);
	    } catch (Exception e) {
	        // Handle other exceptions
	        return new ResponseEntity<>("An error occurred while fetching the address.", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	
	@PostMapping(AddressServiceAPIs.UPDATE_ADDRESS)
	public ResponseEntity<?> updateAddress(@RequestBody Address addressObj){
		
		
		addressServ.updateAddress(addressObj);
		
		return new ResponseEntity<>("Address Successfully Updated", HttpStatus.OK); 
		
	}
	
	
	@GetMapping(AddressServiceAPIs.GET_ALL_ADDRESS)
	public ResponseEntity<?> getAllAddress(){
		
		
		List<Address> addressList = addressServ.getAllAddress();
		
		return new ResponseEntity<>(addressList, HttpStatus.OK); 
		
	}

	@PostMapping(AddressServiceAPIs.UPDATES_ADDRESS)
	 public ResponseEntity<?> updateAddress(@RequestBody AddressDTO addressObj) {
       if (addressObj == null) {
           return ResponseEntity.badRequest().body("Invalid address data.");
       }

       try {
    	   Long parsedAddressId = Long.parseLong(addressObj.getAddressId());
           
       		Address address = new Address();
       		
            address.setAddressId(parsedAddressId);
       		address.setBlockNo(addressObj.getBlockNo());
       		address.setVillage(addressObj.getVillage());
       		address.setPostOffice(addressObj.getPostOffice());
       		address.setSubDivision(addressObj.getSubDivision());
               applyMappedLocation(address, addressObj.getCountry(), addressObj.getState(), addressObj.getCity());
               address.setPincode(addressObj.getPin());
               address.setFax(addressObj.getFax());
               address.setTelephoneNo(addressObj.getTelephoneNo());
               address.setMobile(addressObj.getMobile());
               address.setCommicationAddress(addressObj.getCommunicationAddress());
               Date date=new Date();
               address.setCreated(date);
               address.setUpdated(date);
               address.setStatus("Active");
               
           Optional<Address> resAddObj = addressServ.updateAddres(address);
           	
           return new ResponseEntity<>(resAddObj.get().getAddressId(), HttpStatus.OK);
	            
          
       } catch (IllegalArgumentException e) {
           return ResponseEntity.badRequest().body(e.getMessage());
       } catch (Exception e) {
           
           e.printStackTrace(); 
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
       }
   }

}
