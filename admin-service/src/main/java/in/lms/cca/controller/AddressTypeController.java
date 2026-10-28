package in.lms.cca.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.lms.cca.entity.AddressType;
import in.lms.cca.service.IAddressTypeService;
import in.lms.cca.util.api.AdminServiceAPIs;
import in.lms.cca.util.api.AddressTypeServiceAPIs;

@RestController
@RequestMapping(AdminServiceAPIs.ADMIN_SERVICE_BASE_URL)
@CrossOrigin
public class AddressTypeController {

	@Autowired
	private IAddressTypeService addressTypeServ;
	
	@PostMapping(AddressTypeServiceAPIs.ADD_ADDRESS_TYPE)
	public ResponseEntity<?> addAddressType(@RequestBody AddressType addressTypeObj){
		
		
		addressTypeServ.addAddressType(addressTypeObj);
		
		return new ResponseEntity<>("Address Type Successfully Added", HttpStatus.OK); 
		
	}
	
	
	@PostMapping(AddressTypeServiceAPIs.UPDATE_ADDRESS_TYPE)
	public ResponseEntity<?> updateAddressType(@RequestBody AddressType addressTypeObj){
		
		
		addressTypeServ.updateAddressType(addressTypeObj);
		
		return new ResponseEntity<>("Address Type Successfully Updated", HttpStatus.OK); 
		
	}
	
	
	@GetMapping(AddressTypeServiceAPIs.GET_ALL_ADDRESS_TYPE)
	public ResponseEntity<?> getAllAddressType(){
		
		
		List<AddressType> addressList = addressTypeServ.getAllAddressType();
		
		return new ResponseEntity<>(addressList, HttpStatus.OK); 
		
	}
	
}
