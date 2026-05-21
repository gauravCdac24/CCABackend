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

import in.lms.cca.dto.AuditControlTypeDTO;
import in.lms.cca.entity.AuditControlType;
import in.lms.cca.service.IAuditControlTypeService;
import in.lms.cca.util.api.AdminServiceAPIs;
import in.lms.cca.util.api.AuditControlTypeServiceAPIs;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
@RequestMapping(AdminServiceAPIs.ADMIN_SERVICE_BASE_URL)
@CrossOrigin
public class AuditControlTypeController {

	@Autowired
	private IAuditControlTypeService auditControlTypeServ;
	
	@PostMapping(AuditControlTypeServiceAPIs.ADD_AUDIT_CONTORL_TYPE)
	public ResponseEntity<?> addNewAuditControlType(@RequestBody AuditControlTypeDTO auditControlTypeObj){
		
		//Server Side Validation
		
		if(auditControlTypeObj.getAuditControlDesc().equals("")) {
			return new ResponseEntity<>("Please enter audit control type.", HttpStatus.BAD_REQUEST);
		}else if(!auditControlTypeObj.getAuditControlDesc().trim().matches("^[A-Za-z ]+$")) {
			return new ResponseEntity<>("Only alphabets and spaces are allowed.", HttpStatus.BAD_REQUEST);
		}else if(auditControlTypeObj.getAuditControlDesc().length() < 3 || auditControlTypeObj.getAuditControlDesc().length() > 14) {
			return new ResponseEntity<>("The length must be between 3 and 14 characters.", HttpStatus.BAD_REQUEST);
		}
				
				
		//Check for Unique Audit Control Type
		AuditControlType cobj = auditControlTypeServ.getAuditControlTypeByDesc(auditControlTypeObj.getAuditControlDesc().trim());
		if(cobj != null) {
			return new ResponseEntity<>("Duplicate Audit Control Type is not allowed.", HttpStatus.BAD_REQUEST);
		}
		
		
		try {
			
			AuditControlType obj = new AuditControlType();
			obj.setAuditControlDesc(auditControlTypeObj.getAuditControlDesc());
			obj.setStatus("Active");
			
			Optional<AuditControlType> c = auditControlTypeServ.addAuditControlType(obj);
			
			if(c.isEmpty())
				return new ResponseEntity<>("An error occurred while saving the audit control type. Please Try again.", HttpStatus.OK);
			return new ResponseEntity<>("Audit control type successfully added.", HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("An error occurred while saving the audit control type. Please Try again.", HttpStatus.BAD_REQUEST);
		}
		
 
		
	}
	
	
	@GetMapping(AuditControlTypeServiceAPIs.GET_ALL_AUDIT_CONTORL_TYPE)
	public ResponseEntity<?> getAllAuditControlType(){
		
		List<AuditControlType> auditControlTypeList = auditControlTypeServ.getAllAuditControlType();
		return new ResponseEntity<>(auditControlTypeList, HttpStatus.OK); 
		
	}
	
	@GetMapping(AuditControlTypeServiceAPIs.CHANGE_AUDIT_CONTORL_TYPE_STATUS)
	public void changeAuditControlTypeStatus(@RequestParam("id") String typeId) {
		
		String id = EncryptionUtil.decrypt(typeId);
		
		try {
			AuditControlType c = auditControlTypeServ.getByAuditControlTypeId(Integer.parseInt(id));
			
			if(c.getStatus().equals("Active"))
				c.setStatus("Inactive");
			else
				c.setStatus("Active");
			
			auditControlTypeServ.updateAuditControlType(c);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	@GetMapping(AuditControlTypeServiceAPIs.GET_AUDIT_CONTORL_TYPE_BY_ID)
	public ResponseEntity<?> getAuditControlTypeById(@RequestParam("id") String typeId)
	{

		String id = EncryptionUtil.decrypt(typeId);

		try {
			AuditControlType auditObj = auditControlTypeServ.getByAuditControlTypeId(Integer.parseInt(id));
			return new ResponseEntity<>(auditObj, HttpStatus.OK); 
		}catch(Exception e) {
			return new ResponseEntity<>("Invalid Audit Control Type Id.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@GetMapping(AuditControlTypeServiceAPIs.DELETE_AUDIT_CONTORL_TYPE_BY_ID)
	public ResponseEntity<?> deleteAuditControlTypeById(@RequestParam("id") String typeId) {
		
		String id = EncryptionUtil.decrypt(typeId);
		
		
			boolean res = auditControlTypeServ.deleteByAuditControlTypeId(Integer.parseInt(id));
			
			if(res) {
				return new ResponseEntity<>("Audit Control Type is successfully deleted.", HttpStatus.OK); 
			}
			else {
				return new ResponseEntity<>("Error in deleting the control type.", HttpStatus.BAD_REQUEST);
			}
		
	}
	
	
	
	
	
	@PostMapping(AuditControlTypeServiceAPIs.UPDATE_AUDIT_CONTORL_TYPE)
	public ResponseEntity<?> updateAuditControlType(@RequestBody AuditControlTypeDTO auditControlTypeObj){
				
		Integer id = Integer.parseInt(EncryptionUtil.decrypt(auditControlTypeObj.getAuditControlTypeId()));
		
		//Server Side Validation
		
		if(auditControlTypeObj.getAuditControlDesc().equals("")) {
			return new ResponseEntity<>("Please enter audit control type.", HttpStatus.BAD_REQUEST);
		}else if(!auditControlTypeObj.getAuditControlDesc().trim().matches("^[A-Za-z ]+$")) {
			return new ResponseEntity<>("Only alphabets and spaces are allowed.", HttpStatus.BAD_REQUEST);
		}else if(auditControlTypeObj.getAuditControlDesc().length() < 3 || auditControlTypeObj.getAuditControlDesc().length() > 14) {
			return new ResponseEntity<>("The length must be between 3 and 14 characters.", HttpStatus.BAD_REQUEST);
		}
				
				
		//Check for Unique Audit Control Type
		AuditControlType cobj = auditControlTypeServ.getAuditControlTypeByDesc(auditControlTypeObj.getAuditControlDesc().trim());
		if(cobj != null && cobj.getAuditControlTypeId() != id) {
			return new ResponseEntity<>("Duplicate Audit Control Type is not allowed.", HttpStatus.BAD_REQUEST);
		}
		
		
		try {
			
			AuditControlType obj = new AuditControlType();
			obj.setAuditControlDesc(auditControlTypeObj.getAuditControlDesc());
			obj.setStatus("Active");
			obj.setAuditControlTypeId(id);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
			Date cdate = sdf.parse(auditControlTypeObj.getCreated());	
			obj.setCreated(cdate);
			
			
			Optional<AuditControlType> c = auditControlTypeServ.updateAuditControlType(obj);
			
			if(c.isEmpty())
				return new ResponseEntity<>("An error occurred while saving the audit control type. Please Try again.", HttpStatus.OK);
			return new ResponseEntity<>("Audit control type successfully added.", HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while saving the audit control type. Please Try again.", HttpStatus.BAD_REQUEST);
		}
		
 
		
	}
	
	
	
}
