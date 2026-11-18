package in.lms.cca.controller;

import java.util.ArrayList;
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

import in.lms.cca.dto.EsignDocTypeDTO;
import in.lms.cca.entity.EsignDocTypeMaster;
import in.lms.cca.service.IEsignDocTypeMasterService;
import in.lms.cca.util.api.AdminServiceAPIs;
import in.lms.cca.util.api.EsignDocTypeMasterAPIs;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
@RequestMapping(AdminServiceAPIs.ADMIN_SERVICE_BASE_URL)
@CrossOrigin
public class EsignDocTypeController {
	
	@Autowired
	private IEsignDocTypeMasterService specService;
	
	

	@PostMapping(EsignDocTypeMasterAPIs.ADD_DOCUMENT_TYPE)
    public ResponseEntity<?> addNewDocType(@RequestBody EsignDocTypeDTO obj) {
	
		try {
			
			
					List<String> validDocType = new ArrayList<>();
					validDocType.add("Cover Letter");
					validDocType.add("CPS Document");
					validDocType.add("API Audit Report");
					validDocType.add("EKYC Approval");
					
			
		
				String createdby = obj.getCreatedBy().trim();
				
				
				if (obj.getDocType().isEmpty()) {
		            return new ResponseEntity<>("Document type cannot be empty.", HttpStatus.BAD_REQUEST);
		        } else if (!obj.getDocType().trim().matches("^[A-Za-z ]+$")) {
		            return new ResponseEntity<>("Only alphabets and space are allowed.", HttpStatus.BAD_REQUEST);
		        } else if (obj.getDocType().trim().length() < 3 || obj.getDocType().trim().length() > 16) {
		            return new ResponseEntity<>("The length must be between 3 and 16 characters.", HttpStatus.BAD_REQUEST);
		        }
				
				//Invalid Document Type
				
				if(!validDocType.contains(obj.getDocType().trim())) {
					return new ResponseEntity<>("The Document Type can only be one of the following: Cover Letter, CPS Document, API Audit Report or EKYC Approval.", HttpStatus.BAD_REQUEST);			
				}
				
				EsignDocTypeMaster cobj = specService.getByEsignDocType(obj.getDocType().trim());
				
				if(cobj != null)
					return new ResponseEntity<>("Duplicate document type is not allowed.", HttpStatus.BAD_REQUEST);
				
				
				
				
				if(EncryptionUtil.decrypt(createdby) == null)
					createdby = EncryptionUtil.encrypt(createdby);
				
					
				//save
				EsignDocTypeMaster eobj = new EsignDocTypeMaster();
				eobj.setDocType(obj.getDocType().trim());
				eobj.setCreatedBy(createdby);
				eobj.setUpdatedBy(createdby);
				eobj.setStatus("Active");
				Optional<EsignDocTypeMaster> asp = specService.addEsignDocTypeMaster(eobj);
				
				if (asp.isEmpty()) {
	                return new ResponseEntity<>("An error occurred while saving. Please Try again.", HttpStatus.BAD_REQUEST);
	            }
	            return new ResponseEntity<>("Esign Document Type has been successfully added.", HttpStatus.OK);
				
				
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while saving. Please Try again.", HttpStatus.BAD_REQUEST);
		}
		
		
	}
	
	
	@PostMapping(EsignDocTypeMasterAPIs.UPDATE_DOCUMENT_TYPE)
    public ResponseEntity<?> updateDocType(@RequestBody EsignDocTypeDTO obj) {
	
		try {
			
				List<String> validDocType = new ArrayList<>();
				validDocType.add("Cover Letter");
				validDocType.add("CPS Document");
				validDocType.add("API Audit Report");
				validDocType.add("EKYC Approval");
		
				String updatedby = obj.getUpdatedBy().trim();
				String id = obj.getEsignDocTypeId().trim();
				
				if(EncryptionUtil.decrypt(id) != null)
					id = EncryptionUtil.decrypt(id);
				
				
				if (obj.getDocType().isEmpty()) {
		            return new ResponseEntity<>("Document type cannot be empty.", HttpStatus.BAD_REQUEST);
		        } else if (!obj.getDocType().trim().matches("^[A-Za-z ]+$")) {
		            return new ResponseEntity<>("Only alphabets and space are allowed.", HttpStatus.BAD_REQUEST);
		        } else if (obj.getDocType().trim().length() < 3 || obj.getDocType().trim().length() > 16) {
		            return new ResponseEntity<>("The length must be between 3 and 16 characters.", HttpStatus.BAD_REQUEST);
		        }
				
				//Invalid Document Type
				
				if(!validDocType.contains(obj.getDocType().trim())) {
					return new ResponseEntity<>("The Document Type can only be one of the following: Cover Letter, CPS Document, API Audit Report or EKYC Approval.", HttpStatus.BAD_REQUEST);			
				}
				
				
				EsignDocTypeMaster cobj = specService.getByEsignDocType(obj.getDocType().trim());
				
				if(cobj != null && Long.parseLong(id) != cobj.getEsignDocTypeId())
					return new ResponseEntity<>("Duplicate document type is not allowed.", HttpStatus.BAD_REQUEST);
				
				
				if(EncryptionUtil.decrypt(updatedby) == null)
					updatedby = EncryptionUtil.encrypt(updatedby);
		
				
				
				EsignDocTypeMaster esingObj = specService.getEsignDocTypeMasterById(Long.parseLong(id));
					
				//update
				
				esingObj.setDocType(obj.getDocType().trim());
				esingObj.setUpdatedBy(updatedby);
				esingObj.setUpdated(new Date());
				Optional<EsignDocTypeMaster> asp = specService.updateEsignDocTypeMaster(esingObj);
				
				if (asp.isEmpty()) {
	                return new ResponseEntity<>("An error occurred while updating. Please Try again.", HttpStatus.BAD_REQUEST);
	            }
	            return new ResponseEntity<>("Esign Document Type has been successfully updated.", HttpStatus.OK);
				
				
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while updating. Please Try again.", HttpStatus.BAD_REQUEST);
		}
		
		
	}
	
	
	@GetMapping(EsignDocTypeMasterAPIs.GET_ALL_DOCUMENT_TYPE)
	public ResponseEntity<?> getAllDocType(){
		
		List<EsignDocTypeMaster> list =specService.getAllEsignDocTypeMaster();
		return new ResponseEntity<>(list, HttpStatus.OK); 
		
	}
	
	@GetMapping(EsignDocTypeMasterAPIs.GET_DOCUMENT_TYPE_BY_ID)
	public ResponseEntity<?> getDocTypeByID(@RequestParam("id") String esignApiSpecId)
	{
		try {
			String id = EncryptionUtil.decrypt(esignApiSpecId);
			EsignDocTypeMaster obj = specService.getEsignDocTypeMasterById(Long.parseLong(id));
			return new ResponseEntity<>(obj, HttpStatus.OK); 
		}catch(Exception e) {
			return new ResponseEntity<>("Invalid Document Type Id.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	@GetMapping(EsignDocTypeMasterAPIs.CHANGE_DOCUMENT_TYPE_STATUS)
	public ResponseEntity<?> changeDocTypeStatus(@RequestParam("id") String esignApiSpecId, @RequestParam("qid") String username)
	{

		try {
			String id = EncryptionUtil.decrypt(esignApiSpecId);
			String uname = username;
			
			if(EncryptionUtil.decrypt(uname) == null)
				uname = EncryptionUtil.encrypt(uname);
			
			EsignDocTypeMaster obj = specService.getEsignDocTypeMasterById(Long.parseLong(id));
			
			if(obj.getStatus().equals("Active"))
				obj.setStatus("Inactive");
			else
				obj.setStatus("Active");
			
			obj.setUpdatedBy(uname);
			obj.setUpdated(new Date());
			
			specService.updateEsignDocTypeMaster(obj);
			
			return new ResponseEntity<>(HttpStatus.OK); 
		}catch(Exception e) {
			return new ResponseEntity<>("Invalid Document Type Id.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	

}
