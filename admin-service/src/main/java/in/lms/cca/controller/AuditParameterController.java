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

import in.lms.cca.dto.AuditParameterDTO;
import in.lms.cca.entity.AuditParameter;
import in.lms.cca.service.IAuditParameterService;
import in.lms.cca.service.IAuditSubCriteriaService;
import in.lms.cca.util.api.AdminServiceAPIs;
import in.lms.cca.util.api.AuditParameterServiceAPIs;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
@RequestMapping(AdminServiceAPIs.ADMIN_SERVICE_BASE_URL)
@CrossOrigin
public class AuditParameterController {

	@Autowired
	private IAuditParameterService auditParameterServ;
	
	@Autowired
	private IAuditSubCriteriaService auditSubCriteriaServ;
	
	
	@PostMapping(AuditParameterServiceAPIs.ADD_AUDIT_PARAMETER)
	public ResponseEntity<?> addNewParameter (@RequestBody AuditParameterDTO auditParameterObj){
		
		String id = EncryptionUtil.decrypt(auditParameterObj.getAuditSubCriteriaId());
		Long cid = Long.parseLong(id);
		
		//Server Side Validation
		
		if(auditParameterObj.getAuditParameterTitle().equals("")) {
			return new ResponseEntity<>("Please enter audit parameter.", HttpStatus.BAD_REQUEST);
		}else if(!auditParameterObj.getAuditParameterTitle().trim().matches("^[A-Za-z() ]+$")) {
			return new ResponseEntity<>("Only alphabets, parenthesis and spaces are allowed.", HttpStatus.BAD_REQUEST);
		}else if(auditParameterObj.getAuditParameterTitle().length() < 3 || auditParameterObj.getAuditParameterTitle().length() > 155) {
			return new ResponseEntity<>("The length must be between 3 and 155 characters.", HttpStatus.BAD_REQUEST);
		}
				
				
		//Check for Unique Parameter
		AuditParameter cobj = auditParameterServ.getAuditParameterByTitle(auditParameterObj.getAuditParameterTitle().trim());
		if(cobj != null) {
			return new ResponseEntity<>("Duplicate Audit Parameter Title is not allowed.", HttpStatus.BAD_REQUEST);
		}
		
		
		try {
			
			AuditParameter obj = new AuditParameter();
			obj.setAuditParameterTitle(auditParameterObj.getAuditParameterTitle());
			obj.setStatus("Active");
			obj.setAuditSubCriteriaId(auditSubCriteriaServ.getByAuditSubCriteriaId(cid));

			Optional<AuditParameter> c = auditParameterServ.addAuditParameter(obj);
			
			if(c.isEmpty())
				return new ResponseEntity<>("An error occurred while saving the audit parameter. Please Try again.", HttpStatus.OK);
			return new ResponseEntity<>("Audit Parameter successfully added.", HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("An error occurred while saving the parameter. Please Try again.", HttpStatus.BAD_REQUEST);
		}
		
 
		
	}
	
	
	@GetMapping(AuditParameterServiceAPIs.GET_ALL_AUDIT_PARAMETER)
	public ResponseEntity<?> getAllAuditParameter(){
		
		List<AuditParameter> auditParameterList = auditParameterServ.getAllAuditParameter();
		return new ResponseEntity<>(auditParameterList, HttpStatus.OK); 
		
	}
	
	@GetMapping(AuditParameterServiceAPIs.CHANGE_AUDIT_PARAMETER_STATUS)
	public void changeAuditParameterStatus(@RequestParam("id") String auditId) {
		
		String id = EncryptionUtil.decrypt(auditId);
		
		
		try {
			AuditParameter c = auditParameterServ.getByAuditParameterId(Long.parseLong(id));
			
			if(c.getStatus().equals("Active"))
				c.setStatus("Inactive");
			else
				c.setStatus("Active");
			
			auditParameterServ.updateAuditParameter(c);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	@GetMapping(AuditParameterServiceAPIs.GET_AUDIT_PARAMETER_BY_ID)
	public ResponseEntity<?> getAuditParameterById(@RequestParam("id") String auditId)
	{
		
		String id = EncryptionUtil.decrypt(auditId);
		
		try {
			AuditParameter auditObj = auditParameterServ.getByAuditParameterId(Long.parseLong(id));
			return new ResponseEntity<>(auditObj, HttpStatus.OK); 
		}catch(Exception e) {
			return new ResponseEntity<>("Invalid Audit Parameter Id.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@GetMapping(AuditParameterServiceAPIs.DELETE_AUDIT_PARAMETER_BY_ID)
	public ResponseEntity<?> deleteAuditParameterById(@RequestParam("id") String auditId) {
		
		String id = EncryptionUtil.decrypt(auditId);
		
		
			boolean res = auditParameterServ.deleteByAuditParameterId(Long.parseLong(id));
			
			if(res) {
				return new ResponseEntity<>("Audit Parameter is successfully deleted.", HttpStatus.OK); 
			}
			else {
				return new ResponseEntity<>("Error in deleting the audit parameter.", HttpStatus.BAD_REQUEST);
			}
		
	}
	
	
	@PostMapping(AuditParameterServiceAPIs.UPDATE_AUDIT_PARAMETER)
	public ResponseEntity<?> updateAuditSubCriteria(@RequestBody AuditParameterDTO auditParameterObj){
		
				String id = EncryptionUtil.decrypt(auditParameterObj.getAuditParameterId());
				Long cid = Long.parseLong(id);
				String sid =  EncryptionUtil.decrypt(auditParameterObj.getAuditSubCriteriaId());
				Long aid = Long.parseLong(sid);
				//Server Side Validation
		
				if(auditParameterObj.getAuditParameterTitle().equals("")) {
					return new ResponseEntity<>("Please enter audit parameter.", HttpStatus.BAD_REQUEST);
				}else if(!auditParameterObj.getAuditParameterTitle().trim().matches("^[A-Za-z() ]+$")) {
					return new ResponseEntity<>("Only alphabets, parenthesis and spaces are allowed.", HttpStatus.BAD_REQUEST);
				}else if(auditParameterObj.getAuditParameterTitle().length() < 3 || auditParameterObj.getAuditParameterTitle().length() > 155) {
					return new ResponseEntity<>("The length must be between 3 and 155 characters.", HttpStatus.BAD_REQUEST);
				}
						
						
				//Check for Unique Audit Parameter Title
				AuditParameter cobj = auditParameterServ.getAuditParameterByTitle(auditParameterObj.getAuditParameterTitle().trim());
				if(cobj != null && cobj.getAuditParameterId() != cid) {
					return new ResponseEntity<>("Duplicate Audit Parameter Title is not allowed.", HttpStatus.BAD_REQUEST);
				}
				
				
				
				
				try {
					
					AuditParameter obj = new AuditParameter();
					obj.setAuditParameterTitle(auditParameterObj.getAuditParameterTitle());
					obj.setStatus(auditParameterObj.getStatus());
					obj.setAuditSubCriteriaId(auditSubCriteriaServ.getByAuditSubCriteriaId(aid));
					obj.setAuditParameterId(aid);
					
					//
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
					Date cdate = sdf.parse(auditParameterObj.getCreated());	
					obj.setCreated(cdate);
					

					Optional<AuditParameter> c = auditParameterServ.addAuditParameter(obj);
					
					if(c.isEmpty())
						return new ResponseEntity<>("An error occurred while updating the audit parameter. Please Try again.", HttpStatus.OK);
					return new ResponseEntity<>("Audit Parameter successfully updated.", HttpStatus.OK);
				}catch(Exception e) {
					e.printStackTrace();
					return new ResponseEntity<>("An error occurred while updating the audit parameter. Please Try again.", HttpStatus.BAD_REQUEST);
				}
	}
	
	
	
	@GetMapping(AuditParameterServiceAPIs.GET_AUDIT_PARAMETER_BY_AUDIT_SUB_CRITERIA_ID)
	public ResponseEntity<?> getAuditParameterByAuditSubCriteriaId(@RequestParam("id") String auditId)
	{
		String id = EncryptionUtil.decrypt(auditId);
		
		try {
			List<AuditParameter> auditObj = auditParameterServ.getAllByAuditSubCriteriaId(Long.parseLong(id));
			return new ResponseEntity<>(auditObj, HttpStatus.OK); 
		}catch(Exception e) {
			return new ResponseEntity<>("Invalid Audit Parameter Id.", HttpStatus.BAD_REQUEST);
		}
		
	}

	
	
}
