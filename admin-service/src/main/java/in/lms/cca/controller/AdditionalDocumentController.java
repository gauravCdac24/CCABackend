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

import in.lms.cca.dto.AdditionaldocumentDTO;
import in.lms.cca.entity.AdditionalDocumentEntity;
import in.lms.cca.entity.ApplicationTypeMaster;
import in.lms.cca.service.IAdditionalDocumentService;
import in.lms.cca.service.IApplicationTypeMasterService;
import in.lms.cca.util.api.AdditionalDocumentServiceAPIs;
import in.lms.cca.util.api.AdminServiceAPIs;
import in.lms.cca.util.api.StateServiceAPIs;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
@RequestMapping(AdminServiceAPIs.ADMIN_SERVICE_BASE_URL)
@CrossOrigin
public class AdditionalDocumentController {
	
	
	@Autowired
	private IAdditionalDocumentService additionalDocumentServ;
	
	@Autowired
	private IApplicationTypeMasterService applicationtypeServ;
	
	
	@PostMapping(AdditionalDocumentServiceAPIs.ADD_NEW_ADDITIONAL_DOCUMENT)
	public ResponseEntity<?> addNewCountry(@RequestBody AdditionaldocumentDTO additionaldocumentObj){
		
		
		
		//Server Side Validation
		
		if(additionaldocumentObj.getAdditionalDocument().equals("")) {
			return new ResponseEntity<>("Please enter Additional Document name.", HttpStatus.BAD_REQUEST);
		}else if(!additionaldocumentObj.getAdditionalDocument().trim().matches("^[A-Za-z ]+$")) {
			return new ResponseEntity<>("Only alphabets and spaces are allowed.", HttpStatus.BAD_REQUEST);
		}else if(additionaldocumentObj.getAdditionalDocument().length() < 3 || additionaldocumentObj.getAdditionalDocument().length() > 45) {
			return new ResponseEntity<>("The length must be between 3 and 45 characters.", HttpStatus.BAD_REQUEST);
		}
		
		if(additionaldocumentObj.getAppTypeMasterId().equals("")) {
			return new ResponseEntity<>("Please select appplication Name.", HttpStatus.BAD_REQUEST);
		}
		System.out.println(additionaldocumentObj);	
		//Check for Unique state Name
		AdditionalDocumentEntity cobj = additionalDocumentServ.getDocumentName(additionaldocumentObj.getAdditionalDocument().trim());
		if(cobj != null) {
			return new ResponseEntity<>("Duplicate Additional Document is not allowed.", HttpStatus.BAD_REQUEST);
		}
		
		System.out.println(additionaldocumentObj.toString());
		
		try {
			
			AdditionalDocumentEntity newAdditionalDocumentEntity = new AdditionalDocumentEntity();
			newAdditionalDocumentEntity.setDocumentName(additionaldocumentObj.getAdditionalDocument());
			
			String id = EncryptionUtil.decrypt(additionaldocumentObj.getAppTypeMasterId());
			
			System.out.println("---->"+id);
			
			ApplicationTypeMaster applicationType =  applicationtypeServ.getApplicationTypeMasterById(Long.parseLong(id)); 
			System.out.println(applicationType.getAppType());
			newAdditionalDocumentEntity.setAppTypeMasterId(applicationType);
			newAdditionalDocumentEntity.setStatus("Active");
			
			Optional<AdditionalDocumentEntity> c = additionalDocumentServ.addState(newAdditionalDocumentEntity);
			System.out.println(c.toString());
			if(c.isEmpty())
				return new ResponseEntity<>("An error occurred while saving the state. Please Try again to save.", HttpStatus.OK);
			return new ResponseEntity<>("State successfully added.", HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while saving the state. Please Try again.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	@GetMapping(AdditionalDocumentServiceAPIs.GET_ALL_ADDITIONAL_DOCUMENT)
	public ResponseEntity<?> getAllAddress(){
		
		List<AdditionalDocumentEntity> stateList = additionalDocumentServ.getAllAdditionalDocumentEntity();
		return new ResponseEntity<>(stateList, HttpStatus.OK); 
		
	}
	
	@GetMapping(AdditionalDocumentServiceAPIs.CHANGE_ADDITIONAL_DOCUMENT_STATUS)
	public void changeStateStatus(@RequestParam("id") String stateId) {
		
		String id = EncryptionUtil.decrypt(stateId);
		
		try {
			AdditionalDocumentEntity c = additionalDocumentServ.getAdditionalDocumentEntityById(Long.parseLong(id));
			
			if(c.getStatus().equals("Active"))
				c.setStatus("Inactive");
			else
				c.setStatus("Active");
			
			additionalDocumentServ.updateState(c);
		}
		catch(Exception e) {
			
		}
		
		
		
	}
	
	@GetMapping(AdditionalDocumentServiceAPIs.GET_ADDITIONAL_DOCUMENT_BY_ID)
	public ResponseEntity<?> getStateByID(@RequestParam("id") String stateId)
	{
		String id = EncryptionUtil.decrypt(stateId);
		
		try {
			AdditionalDocumentEntity stateObj = additionalDocumentServ.getAdditionalDocumentEntityById(Long.parseLong(id));
			return new ResponseEntity<>(stateObj, HttpStatus.OK); 
		}catch(Exception e) {
			return new ResponseEntity<>("Invalid state Id.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@GetMapping(AdditionalDocumentServiceAPIs.DELETE_ADDITIONAL_DOCUMENT_BY_ID)
	public ResponseEntity<?> deleteCountryById(@RequestParam("id") String stateId) {
		
		String id = EncryptionUtil.decrypt(stateId);
		
		
			boolean res = additionalDocumentServ.deleteByAdditionalDocumentEntityId(Long.parseLong(id));
			
			if(res) {
				return new ResponseEntity<>("Additional Document is successfully deleted.", HttpStatus.OK); 
			}
			else {
				return new ResponseEntity<>("Error in deleting the country.", HttpStatus.BAD_REQUEST);
			}
		
	}
	
	
	@PostMapping(AdditionalDocumentServiceAPIs.UPDATE_ADDITIONAL_DOCUMENT)
	public ResponseEntity<?> updateCountry(@RequestBody AdditionaldocumentDTO additionaldocumentObj){
		
		
		
		//Server Side Validation
		
				if(additionaldocumentObj.getAdditionalDocument().equals("")) {
					return new ResponseEntity<>("Please enter Additional Document name.", HttpStatus.BAD_REQUEST);
				}else if(!additionaldocumentObj.getAdditionalDocument().trim().matches("^[A-Za-z ]+$")) {
					return new ResponseEntity<>("Only alphabets and spaces are allowed.", HttpStatus.BAD_REQUEST);
				}else if(additionaldocumentObj.getAdditionalDocument().length() < 3 || additionaldocumentObj.getAdditionalDocument().length() > 45) {
					return new ResponseEntity<>("The length must be between 3 and 45 characters.", HttpStatus.BAD_REQUEST);
				}
				
				if(additionaldocumentObj.getAppTypeMasterId().equals("")) {
					return new ResponseEntity<>("Please select appplication Name.", HttpStatus.BAD_REQUEST);
				}
				System.out.println(additionaldocumentObj);	
				//Check for Unique state Name
				AdditionalDocumentEntity cobj = additionalDocumentServ.getDocumentName(additionaldocumentObj.getAdditionalDocument().trim());
				if(cobj != null) {
					return new ResponseEntity<>("Duplicate Additional Document is not allowed.", HttpStatus.BAD_REQUEST);
				}
				
				System.out.println(additionaldocumentObj.toString());
				
				try {
					
					AdditionalDocumentEntity newAdditionalDocumentEntity = new AdditionalDocumentEntity();
					newAdditionalDocumentEntity.setDocumentName(additionaldocumentObj.getAdditionalDocument());
					
					String id = EncryptionUtil.decrypt(additionaldocumentObj.getAppTypeMasterId());
					
					System.out.println("---->"+id);
					
					ApplicationTypeMaster applicationType =  applicationtypeServ.getApplicationTypeMasterById(Long.parseLong(id)); 
					System.out.println(applicationType.getAppType());
					newAdditionalDocumentEntity.setAppTypeMasterId(applicationType);
					newAdditionalDocumentEntity.setStatus("Active");
					
					Optional<AdditionalDocumentEntity> c = additionalDocumentServ.updateState(newAdditionalDocumentEntity);
					System.out.println(c.toString());
					if(c.isEmpty())
						return new ResponseEntity<>("An error occurred while saving the state. Please Try again to save.", HttpStatus.OK);
					return new ResponseEntity<>("State successfully added.", HttpStatus.OK);
				}catch(Exception e) {
					e.printStackTrace();
					return new ResponseEntity<>("An error occurred while saving the state. Please Try again.", HttpStatus.BAD_REQUEST);
				}
				
			}
}
