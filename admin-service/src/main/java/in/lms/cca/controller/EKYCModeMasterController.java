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

import in.lms.cca.dto.EKYCModeMasterDTO;
import in.lms.cca.entity.EKYCModeMaster;
import in.lms.cca.service.IEKYCModeMasterService;
import in.lms.cca.util.api.AdminServiceAPIs;
import in.lms.cca.util.api.EKYCModeMasterServiceAPIs;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
@RequestMapping(AdminServiceAPIs.ADMIN_SERVICE_BASE_URL)
@CrossOrigin
public class EKYCModeMasterController {
	
	@Autowired
	private IEKYCModeMasterService ekycModeServ;
	
	

	@PostMapping(EKYCModeMasterServiceAPIs.ADD_EKYC_MODE)
    public ResponseEntity<?> addNewEKYCMode(@RequestBody EKYCModeMasterDTO obj) {
	
		try {
		
				String createdby = obj.getCreatedBy().trim();
				
				
				if (obj.getEkycModeTitle().isEmpty()) {
		            return new ResponseEntity<>("EKYC Mode cannot be empty.", HttpStatus.BAD_REQUEST);
		        } else if (!obj.getEkycModeTitle().trim().matches("^[A-Za-z()& ]+$")) {
		            return new ResponseEntity<>("Only alphabets and space are allowed.", HttpStatus.BAD_REQUEST);
		        } else if (obj.getEkycModeTitle().trim().length() < 3 || obj.getEkycModeTitle().trim().length() > 150) {
		            return new ResponseEntity<>("The length must be between 3 and 150 characters.", HttpStatus.BAD_REQUEST);
		        }
				
				if (obj.getIsApprovalRequired().isEmpty()) {
		            return new ResponseEntity<>("Please select whether approval is required.", HttpStatus.BAD_REQUEST);
		        }
				
				if (!(obj.getIsApprovalRequired().equals("true") || obj.getIsApprovalRequired().equals("false"))) {
		            return new ResponseEntity<>("Please select valid type Approval Required.", HttpStatus.BAD_REQUEST);
		        }
				
				EKYCModeMaster cobj = ekycModeServ.getByEKYCModeTitle(obj.getEkycModeTitle().trim());
				
				if(cobj != null)
					return new ResponseEntity<>("Duplicate EKYC Mode is not allowed.", HttpStatus.BAD_REQUEST);
				
				
				
				
				if(EncryptionUtil.decrypt(createdby) == null)
					createdby = EncryptionUtil.encrypt(createdby);
				
					
				//save
				EKYCModeMaster eobj = new EKYCModeMaster();
				eobj.setEkycModeTitle(obj.getEkycModeTitle().trim());
				eobj.setIsApprovalRequired(obj.getIsApprovalRequired().equals("true")?true:false);
				eobj.setCreatedBy(createdby);
				eobj.setUpdatedBy(createdby);
				eobj.setStatus("Active");
				Optional<EKYCModeMaster> asp = ekycModeServ.addEKYCModeMaster(eobj);
				
				if (asp.isEmpty()) {
	                return new ResponseEntity<>("An error occurred while saving. Please Try again.", HttpStatus.BAD_REQUEST);
	            }
	            return new ResponseEntity<>("EKYC Mode has been successfully added.", HttpStatus.OK);
				
				
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while saving. Please Try again.", HttpStatus.BAD_REQUEST);
		}
		
		
	}
	
	
	@PostMapping(EKYCModeMasterServiceAPIs.UPDATE_EKYC_MODE)
    public ResponseEntity<?> updateEKYCMode(@RequestBody EKYCModeMasterDTO obj) {
	
		try {
		
				String updatedby = obj.getUpdatedBy().trim();
				String id = obj.getEkycModeId().trim();
				
				if(EncryptionUtil.decrypt(id) != null)
					id = EncryptionUtil.decrypt(id);
				
				
				if (obj.getEkycModeTitle().isEmpty()) {
		            return new ResponseEntity<>("EKYC Mode cannot be empty.", HttpStatus.BAD_REQUEST);
		        } else if (!obj.getEkycModeTitle().trim().matches("^[A-Za-z ]+$")) {
		            return new ResponseEntity<>("Only alphabet and space are allowed.", HttpStatus.BAD_REQUEST);
		        } else if (obj.getEkycModeTitle().trim().length() < 3 || obj.getEkycModeTitle().trim().length() > 150) {
		            return new ResponseEntity<>("The length must be between 3 and 150 characters.", HttpStatus.BAD_REQUEST);
		        }
				
				if (obj.getIsApprovalRequired().isEmpty()) {
		            return new ResponseEntity<>("Please select whether approval is required.", HttpStatus.BAD_REQUEST);
		        }
				
				if (!(obj.getIsApprovalRequired().equals("true") || obj.getIsApprovalRequired().equals("false"))) {
		            return new ResponseEntity<>("Please select valid type Approval Required.", HttpStatus.BAD_REQUEST);
		        }
				
				EKYCModeMaster cobj = ekycModeServ.getByEKYCModeTitle(obj.getEkycModeTitle().trim());
				
				if(cobj != null && Long.parseLong(id) != cobj.getEkycModeId())
					return new ResponseEntity<>("Duplicate EKYC Mode is not allowed.", HttpStatus.BAD_REQUEST);
				
				if(EncryptionUtil.decrypt(updatedby) == null)
					updatedby = EncryptionUtil.encrypt(updatedby);
		
				
				
				EKYCModeMaster esingObj = ekycModeServ.getEKYCModeMasterById(Long.parseLong(id));
					
				//update
				
				esingObj.setEkycModeTitle(obj.getEkycModeTitle().trim());
				esingObj.setIsApprovalRequired(obj.getIsApprovalRequired().equals("true")?true:false);
				esingObj.setUpdatedBy(updatedby);
				esingObj.setUpdated(new Date());
				Optional<EKYCModeMaster> asp = ekycModeServ.updateEKYCModeMaster(esingObj);
				
				if (asp.isEmpty()) {
	                return new ResponseEntity<>("An error occurred while updating. Please Try again.", HttpStatus.BAD_REQUEST);
	            }
	            return new ResponseEntity<>("EKYC Mode has been successfully updated.", HttpStatus.OK);
				
				
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while updating. Please Try again.", HttpStatus.BAD_REQUEST);
		}
		
		
	}
	
	
	@GetMapping(EKYCModeMasterServiceAPIs.GET_ALL_EKYC_MODE)
	public ResponseEntity<?> getAllEKYCMode(){
		
		List<EKYCModeMaster> list =ekycModeServ.getAllEKYCModeMaster();
		return new ResponseEntity<>(list, HttpStatus.OK); 
		
	}
	
	@GetMapping(EKYCModeMasterServiceAPIs.GET_EKYC_MODE_BY_ID)
	public ResponseEntity<?> getEKYCModeByID(@RequestParam("id") String ekycModeId)
	{
		
		
		try {
			String id = EncryptionUtil.decrypt(ekycModeId);
			EKYCModeMaster obj = ekycModeServ.getEKYCModeMasterById(Long.parseLong(id));
			return new ResponseEntity<>(obj, HttpStatus.OK); 
		}catch(Exception e) {
			return new ResponseEntity<>("Invalid EKYC Mode Id.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	@GetMapping(EKYCModeMasterServiceAPIs.CHANGE_EKYC_MODE_STATUS)
	public ResponseEntity<?> changeEKYCModeStatus(@RequestParam("id") String ekycModeId, @RequestParam("qid") String username)
	{

		try {
			String id = EncryptionUtil.decrypt(ekycModeId);
			String uname = username;
			
			if(EncryptionUtil.decrypt(uname) == null)
				uname = EncryptionUtil.encrypt(uname);
			EKYCModeMaster obj = ekycModeServ.getEKYCModeMasterById(Long.parseLong(id));
			
			if(obj.getStatus().equals("Active"))
				obj.setStatus("Inactive");
			else
				obj.setStatus("Active");
			
			obj.setUpdatedBy(uname);
			obj.setUpdated(new Date());
			
			ekycModeServ.updateEKYCModeMaster(obj);
			
			return new ResponseEntity<>(HttpStatus.OK); 
		}catch(Exception e) {
			return new ResponseEntity<>("Invalid EKYC Mode Id.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	

}
