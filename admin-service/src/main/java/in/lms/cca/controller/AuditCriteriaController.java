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

import in.lms.cca.dto.AuditCriteriaDTO;
import in.lms.cca.entity.AuditCriteria;
import in.lms.cca.service.IAuditCriteriaService;
import in.lms.cca.util.api.AdminServiceAPIs;
import in.lms.cca.util.api.AuditCriteriaServiceAPIs;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
@RequestMapping(AdminServiceAPIs.ADMIN_SERVICE_BASE_URL)
@CrossOrigin
public class AuditCriteriaController {
	
	@Autowired
	private IAuditCriteriaService auditServ;
	
	
	@PostMapping(AuditCriteriaServiceAPIs.ADD_AUDIT_CRITERIA)
	public ResponseEntity<?> addNewAuditCriteria(@RequestBody AuditCriteria auditCriteriaObj){
		
		
		
		//Server Side Validation
		
		if(auditCriteriaObj.getAuditCriteriaTitle().equals("")) {
			return new ResponseEntity<>("Please enter audit criteria.", HttpStatus.BAD_REQUEST);
		}else if(!auditCriteriaObj.getAuditCriteriaTitle().trim().matches("^[A-Za-z() ]+$")) {
			return new ResponseEntity<>("Only alphabets, parenthesis and spaces are allowed.", HttpStatus.BAD_REQUEST);
		}else if(auditCriteriaObj.getAuditCriteriaTitle().length() < 3 || auditCriteriaObj.getAuditCriteriaTitle().length() > 155) {
			return new ResponseEntity<>("The length must be between 3 and 155 characters.", HttpStatus.BAD_REQUEST);
		}
				
				
		//Check for Unique Audit Criteria Title
		AuditCriteria cobj = auditServ.getAuditCriteriaByTitle(auditCriteriaObj.getAuditCriteriaTitle().trim());
		if(cobj != null) {
			return new ResponseEntity<>("Duplicate Audit Criteria Title is not allowed.", HttpStatus.BAD_REQUEST);
		}
		
		
		
		
		try {
			
			AuditCriteria obj = new AuditCriteria();
			obj.setAuditCriteriaTitle(auditCriteriaObj.getAuditCriteriaTitle());
			obj.setStatus("Active");
			
			Optional<AuditCriteria> c = auditServ.addAuditCriteria(obj);
			
			if(c.isEmpty())
				return new ResponseEntity<>("An error occurred while saving the audit criteria. Please Try again.", HttpStatus.OK);
			return new ResponseEntity<>("Audit Criteria successfully added.", HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("An error occurred while saving the audit criteria. Please Try again.", HttpStatus.BAD_REQUEST);
		}
		
 
		
	}
	
	
	@GetMapping(AuditCriteriaServiceAPIs.GET_ALL_AUDIT_CRITERIA)
	public ResponseEntity<?> getAllAuditCriteria(){
		
		List<AuditCriteria> auditCriteriaList = auditServ.getAllAuditCriteria();
		return new ResponseEntity<>(auditCriteriaList, HttpStatus.OK); 
		
	}
	
	@GetMapping(AuditCriteriaServiceAPIs.CHANGE_AUDIT_CRITERIA_STATUS)
	public void changeAuditCriteriaStatus(@RequestParam("id") String auditId) {
		
		String id = EncryptionUtil.decrypt(auditId);
		
		try {
			AuditCriteria c = auditServ.getByAuditCriteriaId(Long.parseLong(id));
			
			if(c.getStatus().equals("Active"))
				c.setStatus("Inactive");
			else
				c.setStatus("Active");
			
			auditServ.updateAuditCriteria(c);
		}
		catch(Exception e) {
			
		}
		
		
		
	}
	
	@GetMapping(AuditCriteriaServiceAPIs.GET_AUDIT_CRITERIA_BY_ID)
	public ResponseEntity<?> getAuditCriteriaById(@RequestParam("id") String auditId)
	{
		String id = EncryptionUtil.decrypt(auditId);
		
		try {
			AuditCriteria auditObj = auditServ.getByAuditCriteriaId(Long.parseLong(id));
			return new ResponseEntity<>(auditObj, HttpStatus.OK); 
		}catch(Exception e) {
			return new ResponseEntity<>("Invalid Audit Criteria Id.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@GetMapping(AuditCriteriaServiceAPIs.DELETE_AUDIT_CRITERIA_BY_ID)
	public ResponseEntity<?> deleteAuditCriteriaById(@RequestParam("id") String auditId) {
		
		String id = EncryptionUtil.decrypt(auditId);
		
		
			boolean res = auditServ.deleteByAuditCriteriaId(Long.parseLong(id));
			
			if(res) {
				return new ResponseEntity<>("Audit Criteria is successfully deleted.", HttpStatus.OK); 
			}
			else {
				return new ResponseEntity<>("Error in deleting the audit criteria.", HttpStatus.BAD_REQUEST);
			}
		
	}
	
	
	@PostMapping(AuditCriteriaServiceAPIs.UPDATE_AUDIT_CRITERIA)
	public ResponseEntity<?> updateAuditCriteria(@RequestBody AuditCriteriaDTO auditCriteriaObj){
		
		
		String id = EncryptionUtil.decrypt(auditCriteriaObj.getAuditCriteriaId());
		Long cid = Long.parseLong(id);
		
		//Server Side Validation
		
		if(auditCriteriaObj.getAuditCriteriaTitle().equals("")) {
			return new ResponseEntity<>("Please enter audit criteria.", HttpStatus.BAD_REQUEST);
		}else if(!auditCriteriaObj.getAuditCriteriaTitle().trim().matches("^[A-Za-z() ]+$")) {
			return new ResponseEntity<>("Only alphabets, parenthesis and spaces are allowed.", HttpStatus.BAD_REQUEST);
		}else if(auditCriteriaObj.getAuditCriteriaTitle().length() < 3 || auditCriteriaObj.getAuditCriteriaTitle().length() > 155) {
			return new ResponseEntity<>("The length must be between 3 and 155 characters.", HttpStatus.BAD_REQUEST);
		}
				
				
		//Check for Unique Audit Criteria Title
		AuditCriteria cobj = auditServ.getAuditCriteriaByTitle(auditCriteriaObj.getAuditCriteriaTitle().trim());
		if(cobj != null && cobj.getAuditCriteriaId() != cid) {
			return new ResponseEntity<>("Duplicate Audit Criteria Title is not allowed.", HttpStatus.BAD_REQUEST);
		}
		
		
		
		
		try {
			
			AuditCriteria uobj = new AuditCriteria();
			uobj.setAuditCriteriaTitle(auditCriteriaObj.getAuditCriteriaTitle());
			uobj.setStatus(auditCriteriaObj.getStatus());
			uobj.setAuditCriteriaId(cid);
			
			System.out.println(auditCriteriaObj.getCreated());
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
			Date cdate = sdf.parse(auditCriteriaObj.getCreated());
			
			uobj.setCreated(cdate);
			
			Optional<AuditCriteria> c = auditServ.updateAuditCriteria(uobj);
			
			
			
			if(c.isEmpty())
				return new ResponseEntity<>("An error occurred while updating the audit criteria. Please Try again.", HttpStatus.OK);
			return new ResponseEntity<>("Audit Criteria successfully updated.", HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("An error occurred while updating the audit criteria. Please Try again.", HttpStatus.BAD_REQUEST);
		}
		
 
		
	}
	
	
		
		

	

}
