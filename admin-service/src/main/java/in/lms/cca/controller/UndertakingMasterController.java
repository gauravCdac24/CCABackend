package in.lms.cca.controller;

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

import in.lms.cca.dto.UndertakingMasterDTO;
import in.lms.cca.entity.ApplicationTypeMaster;
import in.lms.cca.entity.UndertakingMasterEntity;
import in.lms.cca.service.IApplicationTypeMasterService;
import in.lms.cca.service.UndertakingMasterService;
import in.lms.cca.util.api.AdminServiceAPIs;
import in.lms.cca.util.api.UndertakingMasterAPIs;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
@CrossOrigin
@RequestMapping(AdminServiceAPIs.ADMIN_SERVICE_BASE_URL)
public class UndertakingMasterController {
	
	@Autowired
	private IApplicationTypeMasterService applicationTypeServ;
	
	@Autowired
	private UndertakingMasterService undertakingServ;
	
	
	
	  @PostMapping(UndertakingMasterAPIs.ADD_NEW_UNDERTAKING)
	    public ResponseEntity<?> addSubService(@RequestBody UndertakingMasterDTO undertakingMasterDTO) {
	        // Server-side validation
		  if (undertakingMasterDTO.getUndertakingsTitle() == null || undertakingMasterDTO.getUndertakingsTitle().trim().isEmpty()) {
	            return new ResponseEntity<>("undertaking title cannot be empty.", HttpStatus.BAD_REQUEST);
	        }



	        try {
	        	
	        	UndertakingMasterEntity undertakingMasterEntity = new UndertakingMasterEntity();
	        	
	        	String id = EncryptionUtil.decrypt(undertakingMasterDTO.getAppTypeMasterId());
				
				System.out.println("---->"+id);
				
				ApplicationTypeMaster service =  applicationTypeServ.getApplicationTypeMasterById(Long.parseLong(id)); 
				undertakingMasterEntity.setUndertakingsTitle(undertakingMasterDTO.getUndertakingsTitle());
	        	undertakingMasterEntity.setAppTypeMasterId(service);
	        	undertakingMasterEntity.setIsMandatory(undertakingMasterDTO.getIsMandatory());
	        	undertakingMasterEntity.setStatus("Active");

	            Optional<UndertakingMasterEntity> result = undertakingServ.addUnderTakingMaster(undertakingMasterEntity);
	            if (result.isEmpty()) {
	                return new ResponseEntity<>("Error occurred while saving UnderTaking.", HttpStatus.INTERNAL_SERVER_ERROR);
	            }
	            return new ResponseEntity<>("UnderTaking successfully added.", HttpStatus.OK);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return new ResponseEntity<>("An error occurred while saving the UnderTaking.", HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

	    @GetMapping(UndertakingMasterAPIs.GET_ALL_UNDERTAKING)
	    public ResponseEntity<?> getAllSubServices() {
	        List<UndertakingMasterEntity> subUndertakingMasterEntity = undertakingServ.getAllUndertakingMasterEntity();
	        return new ResponseEntity<>(subUndertakingMasterEntity, HttpStatus.OK);
	    }

	    @GetMapping(UndertakingMasterAPIs.GET_UNDERTAKING_BY_ID)
	    public ResponseEntity<?> getSubServiceById(@RequestParam("id") String subServiceId) {
	    	System.out.println(subServiceId);
	        try {
	            Long id = Long.parseLong(EncryptionUtil.decrypt(subServiceId));
	            
	            System.out.println("subServiceId======>"+id);
	            
	            UndertakingMasterEntity subService = undertakingServ.getUndertakingMasterById(id);
	        	System.out.println(id);

	            if (subService == null) {
	                return new ResponseEntity<>("UnderTaking not found.", HttpStatus.NOT_FOUND);
	            }
	            return new ResponseEntity<>(subService, HttpStatus.OK);
	        } catch (NumberFormatException e) {
	            return new ResponseEntity<>("Invalid UnderTaking ID format.", HttpStatus.BAD_REQUEST);
	        } catch (Exception e) {
	            return new ResponseEntity<>("Invalid UnderTaking ID.", HttpStatus.BAD_REQUEST);
	        }
	    }

	    @PostMapping(UndertakingMasterAPIs.UPDATE_UNDERTAKING)
	    public ResponseEntity<?> updateSubService(@RequestBody UndertakingMasterDTO undertakingMasterDTO) {
	        // Server-side validation
	        if (undertakingMasterDTO.getUndertakingsTitle() == null || undertakingMasterDTO.getUndertakingsTitle().trim().isEmpty()) {
	            return new ResponseEntity<>("undertaking title cannot be empty.", HttpStatus.BAD_REQUEST);
	        } 

	        try {
	        	
	        	UndertakingMasterEntity undertakingMasterEntity = new UndertakingMasterEntity();
	        	
	        	String id = EncryptionUtil.decrypt(undertakingMasterDTO.getAppTypeMasterId());
				
				System.out.println("---->"+id);
				undertakingMasterEntity.setUndertakingId(Long.valueOf(undertakingMasterDTO.getUndertakingId()));
				ApplicationTypeMaster service =  applicationTypeServ.getApplicationTypeMasterById(Long.parseLong((undertakingMasterDTO.getAppTypeMasterId()))); 
				undertakingMasterEntity.setUndertakingsTitle(undertakingMasterDTO.getUndertakingsTitle());
	        	undertakingMasterEntity.setAppTypeMasterId(service);
	        	undertakingMasterEntity.setIsMandatory(undertakingMasterDTO.getIsMandatory());
	        	undertakingMasterEntity.setStatus("Active");
	        	Date date=new Date();
	        	undertakingMasterEntity.setCreated(date);
	        	undertakingMasterEntity.setUpdated(date);

	            Optional<UndertakingMasterEntity> result = undertakingServ.updateUnderTakingMaster(undertakingMasterEntity);
	            if (result.isEmpty()) {
	                return new ResponseEntity<>("Error occurred while saving UnderTaking.", HttpStatus.INTERNAL_SERVER_ERROR);
	            }
	            return new ResponseEntity<>("UnderTaking successfully added.", HttpStatus.OK);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return new ResponseEntity<>("An error occurred while saving the UnderTaking.", HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }


}
