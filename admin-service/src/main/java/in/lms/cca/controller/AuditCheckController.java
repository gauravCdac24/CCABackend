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

import in.lms.cca.dto.AuditCheckDTO;
import in.lms.cca.entity.AuditCheck;
import in.lms.cca.service.IAuditCheckService;
import in.lms.cca.util.api.AdminServiceAPIs;
import in.lms.cca.util.api.AuditCheckServiceAPIs;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
@RequestMapping(AdminServiceAPIs.ADMIN_SERVICE_BASE_URL)
@CrossOrigin
public class AuditCheckController {

	@Autowired
	private IAuditCheckService auditCheckServ;
	
	@PostMapping(AuditCheckServiceAPIs.ADD_AUDIT_CHECK)
	public ResponseEntity<?> addNewAuditCheck(@RequestBody AuditCheckDTO auditCheckObj){
		
		//Server Side Validation
		
		if(auditCheckObj.getAuditCheckDesc().equals("")) {
			return new ResponseEntity<>("Please enter audit Check.", HttpStatus.BAD_REQUEST);
		}
				
				
		//Check for Unique Audit Check
		AuditCheck cobj = auditCheckServ.getAuditCheckByDesc(auditCheckObj.getAuditCheckDesc().trim());
		if(cobj != null) {
			return new ResponseEntity<>("Duplicate Audit Check is not allowed.", HttpStatus.BAD_REQUEST);
		}
		
		
		try {
			
			AuditCheck obj = new AuditCheck();
			obj.setAuditCheckDesc(auditCheckObj.getAuditCheckDesc());
			obj.setStatus("Active");
			
			Optional<AuditCheck> c = auditCheckServ.addAuditCheck(obj);
			
			if(c.isEmpty())
				return new ResponseEntity<>("An error occurred while saving the audit Check. Please Try again.", HttpStatus.OK);
			return new ResponseEntity<>("Audit Check successfully added.", HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("An error occurred while saving the audit Check. Please Try again.", HttpStatus.BAD_REQUEST);
		}
		
 
		
	}
	
	
	@GetMapping(AuditCheckServiceAPIs.GET_ALL_AUDIT_CHECK)
	public ResponseEntity<?> getAllAuditCheck(){
		
		List<AuditCheck> auditCheckList = auditCheckServ.getAllAuditCheck();
		return new ResponseEntity<>(auditCheckList, HttpStatus.OK); 
		
	}
	
	@GetMapping(AuditCheckServiceAPIs.CHANGE_AUDIT_CHECK_STATUS)
	public void changeAuditCheckStatus(@RequestParam("id") String typeId) {
		
		String id = EncryptionUtil.decrypt(typeId);
		
		try {
			AuditCheck c = auditCheckServ.getAuditCheckById(Long.parseLong(id));
			
			if(c.getStatus().equals("Active"))
				c.setStatus("Inactive");
			else
				c.setStatus("Active");
			
			auditCheckServ.updateAuditCheck(c);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	@GetMapping(AuditCheckServiceAPIs.GET_AUDIT_CHECK_BY_ID)
	public ResponseEntity<?> getAuditCheckById(@RequestParam("id") String typeId)
	{

		String id = EncryptionUtil.decrypt(typeId);

		try {
			AuditCheck auditObj = auditCheckServ.getAuditCheckById(Long.parseLong(id));
			return new ResponseEntity<>(auditObj, HttpStatus.OK); 
		}catch(Exception e) {
			return new ResponseEntity<>("Invalid Audit Check Id.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@GetMapping(AuditCheckServiceAPIs.DELETE_AUDIT_CHECK_BY_ID)
	public ResponseEntity<?> deleteAuditCheckById(@RequestParam("id") String typeId) {
		
		String id = EncryptionUtil.decrypt(typeId);
		
		
			boolean res = auditCheckServ.deleteAuditCheckById(Long.parseLong(id));
			
			if(res) {
				return new ResponseEntity<>("Audit Check is successfully deleted.", HttpStatus.OK); 
			}
			else {
				return new ResponseEntity<>("Error in deleting the Check.", HttpStatus.BAD_REQUEST);
			}
		
	}
	
	
	
	
	
	@PostMapping(AuditCheckServiceAPIs.UPDATE_AUDIT_CHECK)
	public ResponseEntity<?> updateAuditCheck(@RequestBody AuditCheckDTO auditCheckObj){
				
		String id1 = EncryptionUtil.decrypt(auditCheckObj.getAuditCheckId());
		
		Long id = Long.parseLong(id1);
		
		
		
		//Server Side Validation
		
		if(auditCheckObj.getAuditCheckDesc().equals("")) {
			return new ResponseEntity<>("Please enter audit Check.", HttpStatus.BAD_REQUEST);
		}
				
				
		//Check for Unique Audit Check
		AuditCheck cobj = auditCheckServ.getAuditCheckByDesc(auditCheckObj.getAuditCheckDesc().trim());
		if(cobj != null && cobj.getAuditCheckId() != id) {
			return new ResponseEntity<>("Duplicate Audit Check is not allowed.", HttpStatus.BAD_REQUEST);
		}
		
		
		try {
			
			AuditCheck obj = new AuditCheck();
			obj.setAuditCheckDesc(auditCheckObj.getAuditCheckDesc());
			obj.setStatus("Active");
			obj.setAuditCheckId(id);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
			Date cdate = sdf.parse(auditCheckObj.getCreated());	
			obj.setCreated(cdate);
			
			
			Optional<AuditCheck> c = auditCheckServ.updateAuditCheck(obj);
			
			if(c.isEmpty())
				return new ResponseEntity<>("An error occurred while updating the audit Check. Please Try again.", HttpStatus.OK);
			return new ResponseEntity<>("Audit Check successfully updated.", HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while saving the audit Check type. Please Try again.", HttpStatus.BAD_REQUEST);
		}
		
 
		
	}
	
	
	
}
