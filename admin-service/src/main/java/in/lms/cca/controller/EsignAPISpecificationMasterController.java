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

import in.lms.cca.dto.EsignAPISpecificationDTO;
import in.lms.cca.entity.EsignAPISpecificationMaster;
import in.lms.cca.service.IEsignAPISpecificationMasterService;
import in.lms.cca.util.api.AdminServiceAPIs;
import in.lms.cca.util.api.EsignAPISpecificationMasterAPIs;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
@RequestMapping(AdminServiceAPIs.ADMIN_SERVICE_BASE_URL)
@CrossOrigin
public class EsignAPISpecificationMasterController {
	
	@Autowired
	private IEsignAPISpecificationMasterService specService;
	
	

	@PostMapping(EsignAPISpecificationMasterAPIs.ADD_API_SPECIFICATION)
    public ResponseEntity<?> addNewAPISpecification(@RequestBody EsignAPISpecificationDTO obj) {
	
		try {
		
				String createdby = obj.getCreatedBy().trim();
				
				
				if (obj.getApiSpecification().isEmpty()) {
		            return new ResponseEntity<>("API specification cannot be empty.", HttpStatus.BAD_REQUEST);
		        } else if (!obj.getApiSpecification().trim().matches("^[A-Za-z()& ]+$")) {
		            return new ResponseEntity<>("Only alphabets, () and & are allowed.", HttpStatus.BAD_REQUEST);
		        } else if (obj.getApiSpecification().trim().length() < 3 || obj.getApiSpecification().trim().length() > 250) {
		            return new ResponseEntity<>("The length must be between 3 and 250 characters.", HttpStatus.BAD_REQUEST);
		        }
				
				EsignAPISpecificationMaster cobj = specService.getByEsignAPISpecification(obj.getApiSpecification().trim());
				
				if(cobj != null)
					return new ResponseEntity<>("Duplicate name is not allowed.", HttpStatus.BAD_REQUEST);
				
				
				
				
				if(EncryptionUtil.decrypt(createdby) == null)
					createdby = EncryptionUtil.encrypt(createdby);
				
					
				//save
				EsignAPISpecificationMaster eobj = new EsignAPISpecificationMaster();
				eobj.setApiSpecification(obj.getApiSpecification().trim());
				eobj.setCreatedBy(createdby);
				eobj.setUpdatedBy(createdby);
				eobj.setStatus("Active");
				Optional<EsignAPISpecificationMaster> asp = specService.addEsignAPISpecificationMaster(eobj);
				
				if (asp.isEmpty()) {
	                return new ResponseEntity<>("An error occurred while saving. Please Try again.", HttpStatus.BAD_REQUEST);
	            }
	            return new ResponseEntity<>("Esign API Specification has been successfully added.", HttpStatus.OK);
				
				
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while saving. Please Try again.", HttpStatus.BAD_REQUEST);
		}
		
		
	}
	
	
	@PostMapping(EsignAPISpecificationMasterAPIs.UPDATE_API_SPECIFICATION)
    public ResponseEntity<?> updateAPISpecification(@RequestBody EsignAPISpecificationDTO obj) {
	
		try {
		
				String updatedby = obj.getUpdatedBy().trim();
				String id = obj.getEsignApiSpecId().trim();
				
				if(EncryptionUtil.decrypt(id) != null)
					id = EncryptionUtil.decrypt(id);
				
				
				if (obj.getApiSpecification().isEmpty()) {
		            return new ResponseEntity<>("API specification cannot be empty.", HttpStatus.BAD_REQUEST);
		        } else if (!obj.getApiSpecification().trim().matches("^[A-Za-z()& ]+$")) {
		            return new ResponseEntity<>("Only alphabets, () and & are allowed.", HttpStatus.BAD_REQUEST);
		        } else if (obj.getApiSpecification().trim().length() < 3 || obj.getApiSpecification().trim().length() > 250) {
		            return new ResponseEntity<>("The length must be between 3 and 250 characters.", HttpStatus.BAD_REQUEST);
		        }
				
				EsignAPISpecificationMaster cobj = specService.getByEsignAPISpecification(obj.getApiSpecification().trim());
				
				if(cobj != null && Long.parseLong(id) != cobj.getEsignApiSpecId())
					return new ResponseEntity<>("Duplicate name is not allowed.", HttpStatus.BAD_REQUEST);
				
				if(EncryptionUtil.decrypt(updatedby) == null)
					updatedby = EncryptionUtil.encrypt(updatedby);
		
				
				
				EsignAPISpecificationMaster esingObj = specService.getEsignAPISpecificationMasterById(Long.parseLong(id));
					
				//update
				
				esingObj.setApiSpecification(obj.getApiSpecification().trim());
				esingObj.setUpdatedBy(updatedby);
				esingObj.setUpdated(new Date());
				Optional<EsignAPISpecificationMaster> asp = specService.updateEsignAPISpecificationMaster(esingObj);
				
				if (asp.isEmpty()) {
	                return new ResponseEntity<>("An error occurred while updating. Please Try again.", HttpStatus.BAD_REQUEST);
	            }
	            return new ResponseEntity<>("Esign API Specification has been successfully updated.", HttpStatus.OK);
				
				
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while updating. Please Try again.", HttpStatus.BAD_REQUEST);
		}
		
		
	}
	
	
	@GetMapping(EsignAPISpecificationMasterAPIs.GET_ALL_API_SPECIFICATION)
	public ResponseEntity<?> getAllAPISpecification(){
		
		List<EsignAPISpecificationMaster> list =specService.getAllEsignAPISpecificationMaster();
		return new ResponseEntity<>(list, HttpStatus.OK); 
		
	}
	
	@GetMapping(EsignAPISpecificationMasterAPIs.GET_API_SPECIFICATION_BY_ID)
	public ResponseEntity<?> getAPISpecificationByID(@RequestParam("id") String esignApiSpecId)
	{
		try {
			String id = EncryptionUtil.decrypt(esignApiSpecId);
			EsignAPISpecificationMaster obj = specService.getEsignAPISpecificationMasterById(Long.parseLong(id));
			return new ResponseEntity<>(obj, HttpStatus.OK); 
		}catch(Exception e) {
			return new ResponseEntity<>("Invalid API Specification Id.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	@GetMapping(EsignAPISpecificationMasterAPIs.CHANGE_API_SPECIFICATION_STATUS)
	public ResponseEntity<?> changeAPISpecificationStatus(@RequestParam("id") String esignApiSpecId, @RequestParam("qid") String username)
	{

		try {
			String id = EncryptionUtil.decrypt(esignApiSpecId);
			String uname = username;
			
			if(EncryptionUtil.decrypt(uname) == null)
				uname = EncryptionUtil.encrypt(uname);
			
			EsignAPISpecificationMaster obj = specService.getEsignAPISpecificationMasterById(Long.parseLong(id));
			
			if(obj.getStatus().equals("Active"))
				obj.setStatus("Inactive");
			else
				obj.setStatus("Active");
			
			obj.setUpdatedBy(uname);
			obj.setUpdated(new Date());
			
			specService.updateEsignAPISpecificationMaster(obj);
			
			return new ResponseEntity<>(HttpStatus.OK); 
		}catch(Exception e) {
			return new ResponseEntity<>("Invalid API Specification Id.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	

}
