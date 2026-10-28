package in.lms.cca.controller;

import java.text.ParseException;
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

import in.lms.cca.dto.SubServiceMasterDTO;
import in.lms.cca.entity.ServiceMaster;
import in.lms.cca.entity.SubServiceMasterEntity;
import in.lms.cca.service.IServiceMasterService;
import in.lms.cca.service.ISubServiceMasterService;
import in.lms.cca.util.api.AdminServiceAPIs;
import in.lms.cca.util.api.SubServiceAPIs;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
@RequestMapping(AdminServiceAPIs.ADMIN_SERVICE_BASE_URL)
@CrossOrigin
public class SubServiceMasterController {
	
	
	 @Autowired
	    private IServiceMasterService serviceMasterServ;
	 
	 @Autowired
	    private ISubServiceMasterService subServiceMasterServ;
	 
	  @PostMapping(SubServiceAPIs.ADD_SUB_SERVICE)
	    public ResponseEntity<?> addSubService(@RequestBody SubServiceMasterDTO subServiceDTO) {
	        // Server-side validation
	        if (subServiceDTO.getSubServiceName() == null || subServiceDTO.getSubServiceName().trim().isEmpty()) {
	            return new ResponseEntity<>("Sub Service title cannot be empty.", HttpStatus.BAD_REQUEST);
	        }

	        if (!subServiceDTO.getSubServiceName().trim().matches("^[A-Za-z0-9 ]+$")) {
	            return new ResponseEntity<>("Sub Service title can only contain alphanumeric characters and spaces.", HttpStatus.BAD_REQUEST);
	        }

	        if (subServiceDTO.getSubServiceName().length() < 2 || subServiceDTO.getSubServiceName().length() > 20) {
	            return new ResponseEntity<>("Sub Service title length must be between 2 and 20 characters.", HttpStatus.BAD_REQUEST);
	        }

	        // Check for unique service title
	        SubServiceMasterEntity existingSubService = subServiceMasterServ.getSubServiceMasterByName(subServiceDTO.getSubServiceName().trim());
//	        if (existingSubService != null) {
//	            return new ResponseEntity<>("Duplicate sub service title is not allowed.", HttpStatus.BAD_REQUEST);
//	        }

	        try {
	        	
	        	SubServiceMasterEntity subService = new SubServiceMasterEntity();
	        	
	        	String id = EncryptionUtil.decrypt(subServiceDTO.getServiceId());
				
				System.out.println("---->"+id);
				
				ServiceMaster service =  serviceMasterServ.getServiceMasterById(Long.parseLong(id)); 
	        	subService.setSubServiceName(subServiceDTO.getSubServiceName().trim());
	        	subService.setServiceId(service);
	        	subService.setIsMandatory(subServiceDTO.isIsMandatory());
	        	subService.setStatus("Active");
	        	subService.setCreatedBy(subServiceDTO.getCreatedBy());
	        	subService.setUpdatedBy(subServiceDTO.getUpdatedBy());

	            Optional<SubServiceMasterEntity> result = subServiceMasterServ.addSubServiceMaster(subService);
	            if (result.isEmpty()) {
	                return new ResponseEntity<>("Error occurred while saving Sub Service.", HttpStatus.INTERNAL_SERVER_ERROR);
	            }
	            return new ResponseEntity<>("Sub Service successfully added.", HttpStatus.OK);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return new ResponseEntity<>("An error occurred while saving the Sub Service.", HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

	    @GetMapping(SubServiceAPIs.GET_ALL_SUB_SERVICES)
	    public ResponseEntity<?> getAllSubServices() {
	        List<SubServiceMasterEntity> subServiceList = subServiceMasterServ.getAllSubServiceMaster();
	        return new ResponseEntity<>(subServiceList, HttpStatus.OK);
	    }

	    @GetMapping(SubServiceAPIs.GET_SUB_SERVICE_BY_ID)
	    public ResponseEntity<?> getSubServiceById(@RequestParam("id") String subServiceId) {
	    	System.out.println(subServiceId);
	        try {
	            Long id = Long.parseLong(EncryptionUtil.decrypt(subServiceId));
	            SubServiceMasterEntity subService = subServiceMasterServ.getSubServiceMasterById(id);
	        	System.out.println(id);

	            if (subService == null) {
	                return new ResponseEntity<>("Sub Service not found.", HttpStatus.NOT_FOUND);
	            }
	            return new ResponseEntity<>(subService, HttpStatus.OK);
	        } catch (NumberFormatException e) {
	            return new ResponseEntity<>("Invalid Sub Service ID format.", HttpStatus.BAD_REQUEST);
	        } catch (Exception e) {
	            return new ResponseEntity<>("Invalid Sub Service ID.", HttpStatus.BAD_REQUEST);
	        }
	    }

	    @PostMapping(SubServiceAPIs.UPDATE_SUB_SERVICE)
	    public ResponseEntity<?> updateService(@RequestBody SubServiceMasterDTO subServiceDTO) {
	    	  // Server-side validation
	        if (subServiceDTO.getSubServiceName() == null || subServiceDTO.getSubServiceName().trim().isEmpty()) {
	            return new ResponseEntity<>("Sub Service title cannot be empty.", HttpStatus.BAD_REQUEST);
	        }

	        if (!subServiceDTO.getSubServiceName().trim().matches("^[A-Za-z0-9 ]+$")) {
	            return new ResponseEntity<>("Sub Service title can only contain alphanumeric characters and spaces.", HttpStatus.BAD_REQUEST);
	        }

	        if (subServiceDTO.getSubServiceName().length() < 2 || subServiceDTO.getSubServiceName().length() > 20) {
	            return new ResponseEntity<>("Sub Service title length must be between 2 and 20 characters.", HttpStatus.BAD_REQUEST);
	        }

	        // Check for unique service title
	        SubServiceMasterEntity existingSubService = subServiceMasterServ.getSubServiceMasterByName(subServiceDTO.getSubServiceName().trim());
//	        if (existingSubService != null) {
//	            return new ResponseEntity<>("Duplicate sub service title is not allowed.", HttpStatus.BAD_REQUEST);
//	        }

	        try {
	        	System.out.println("all data ===>"+subServiceDTO);
	        	SubServiceMasterEntity subService = new SubServiceMasterEntity();
	        	
	        	String id = EncryptionUtil.decrypt(subServiceDTO.getServiceId());
				
				System.out.println("---->"+id);
				
				ServiceMaster service =  serviceMasterServ.getServiceMasterById(Long.parseLong(id)); 
				subService.setSubServiceId(Long.parseLong(subServiceDTO.getSubServiceId()));
	        	subService.setSubServiceName(subServiceDTO.getSubServiceName().trim());
	        	subService.setServiceId(service);
	        	subService.setIsMandatory(subServiceDTO.isIsMandatory());
	        	subService.setStatus("Active");
	        	subService.setCreatedBy(subServiceDTO.getCreatedBy());
	        	subService.setUpdatedBy(subServiceDTO.getUpdatedBy());
	        	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS"); // Adjust the pattern to match your date format

	        	try {
	        	    Date createdDate = dateFormat.parse(subServiceDTO.getCreated());
	        	    Date updatedDate = dateFormat.parse(subServiceDTO.getUpdated());

	        	    subService.setCreated(createdDate);
	        	    subService.setUpdated(updatedDate);
	        	} catch (ParseException e) {
	        	    e.printStackTrace(); // Handle the exception appropriately
	        	}

	            Optional<SubServiceMasterEntity> result = subServiceMasterServ.updateSubServiceMaster(subService);
	            if (result.isEmpty()) {
	                return new ResponseEntity<>("Error occurred while updating Sub Service.", HttpStatus.INTERNAL_SERVER_ERROR);
	            }
	            return new ResponseEntity<>("Sub Service successfully update.", HttpStatus.OK);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return new ResponseEntity<>("An error occurred while updating the Sub Service.", HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }



}
