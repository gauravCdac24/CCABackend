package in.lms.cca.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

import in.lms.cca.dto.AuditSubCriteriaDTO;
import in.lms.cca.entity.AuditSubCriteria;
import in.lms.cca.service.IAuditCriteriaService;
import in.lms.cca.service.IAuditSubCriteriaService;
import in.lms.cca.util.api.AdminServiceAPIs;
import in.lms.cca.util.api.AuditSubCriteriaServiceAPIs;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
@RequestMapping(AdminServiceAPIs.ADMIN_SERVICE_BASE_URL)
@CrossOrigin
public class AuditSubCriteriaController {

	@Autowired
	private IAuditCriteriaService auditServ;
	
	@Autowired
	private IAuditSubCriteriaService auditSubCriteriaServ;
	
	
	@PostMapping(AuditSubCriteriaServiceAPIs.ADD_AUDIT_SUB_CRITERIA)
	public ResponseEntity<?> addNewAuditSubCriteria(@RequestBody AuditSubCriteriaDTO auditSubCriteriaObj){
		
		String id = EncryptionUtil.decrypt(auditSubCriteriaObj.getAuditCriteriaId());
		Long cid = Long.parseLong(id);
		
		//Server Side Validation
		
		if(auditSubCriteriaObj.getAuditSubCriteriaTitle().equals("")) {
			return new ResponseEntity<>("Please enter audit sub criteria.", HttpStatus.BAD_REQUEST);
		}else if(!auditSubCriteriaObj.getAuditSubCriteriaTitle().trim().matches("^[A-Za-z() ]+$")) {
			return new ResponseEntity<>("Only alphabets, parenthesis and spaces are allowed.", HttpStatus.BAD_REQUEST);
		}else if(auditSubCriteriaObj.getAuditSubCriteriaTitle().length() < 3 || auditSubCriteriaObj.getAuditSubCriteriaTitle().length() > 155) {
			return new ResponseEntity<>("The length must be between 3 and 155 characters.", HttpStatus.BAD_REQUEST);
		}
				
				
		//Check for Unique Audit Criteria Title
		AuditSubCriteria cobj = auditSubCriteriaServ.getAuditSubCriteriaByTitle(auditSubCriteriaObj.getAuditSubCriteriaTitle().trim());
		if(cobj != null) {
			return new ResponseEntity<>("Duplicate Audit Sub Criteria Title is not allowed.", HttpStatus.BAD_REQUEST);
		}
		
		
		try {
			
			AuditSubCriteria obj = new AuditSubCriteria();
			obj.setAuditSubCriteriaTitle(auditSubCriteriaObj.getAuditSubCriteriaTitle());
			obj.setStatus("Active");
			obj.setAuditCriteriaId(auditServ.getByAuditCriteriaId(cid));
			boolean visible = auditSubCriteriaObj.getVisible() != null
					&& auditSubCriteriaObj.getVisible().equalsIgnoreCase("TRUE");
			obj.setVisible(visible);
			
			Optional<AuditSubCriteria> c = auditSubCriteriaServ.addAuditSubCriteria(obj);
			
			if(c.isEmpty())
				return new ResponseEntity<>("An error occurred while saving the audit sub criteria. Please Try again.", HttpStatus.OK);
			return new ResponseEntity<>("Audit Sub Criteria successfully added.", HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("An error occurred while saving the audit sub criteria. Please Try again.", HttpStatus.BAD_REQUEST);
		}
		
 
		
	}
	
	
	@GetMapping(AuditSubCriteriaServiceAPIs.GET_ALL_AUDIT_SUB_CRITERIA)
	public ResponseEntity<?> getAllAuditSubCriteria(){
		
		List<AuditSubCriteria> auditSubCriteriaList = auditSubCriteriaServ.getAllAuditSubCriteria();
		return new ResponseEntity<>(auditSubCriteriaList, HttpStatus.OK); 
		
	}
	
	@GetMapping(AuditSubCriteriaServiceAPIs.CHANGE_AUDIT_SUB_CRITERIA_STATUS)
	public void changeAuditSubCriteriaStatus(@RequestParam("id") String auditId) {
		
		String id = EncryptionUtil.decrypt(auditId);
		System.out.println(id);
		
		try {
			AuditSubCriteria c = auditSubCriteriaServ.getByAuditSubCriteriaId(Long.parseLong(id));
			
			if(c.getStatus().equals("Active")) {
				c.setStatus("Inactive");
				c.setVisible(false);
			} else {
				c.setStatus("Active");
			}
			
			auditSubCriteriaServ.updateAuditSubCriteria(c);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	@GetMapping(AuditSubCriteriaServiceAPIs.GET_AUDIT_SUB_CRITERIA_BY_ID)
	public ResponseEntity<?> getAuditSubCriteriaById(@RequestParam("id") String auditId)
	{
		
		String id = EncryptionUtil.decrypt(auditId);
		
		try {
			AuditSubCriteria auditObj = auditSubCriteriaServ.getByAuditSubCriteriaId(Long.parseLong(id));
			return new ResponseEntity<>(auditObj, HttpStatus.OK); 
		}catch(Exception e) {
			return new ResponseEntity<>("Invalid Audit Sub Criteria Id.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@GetMapping(AuditSubCriteriaServiceAPIs.DELETE_AUDIT_SUB_CRITERIA_BY_ID)
	public ResponseEntity<?> deleteAuditSubCriteriaById(@RequestParam("id") String auditId) {
		
		String id = EncryptionUtil.decrypt(auditId);
		
		
			boolean res = auditSubCriteriaServ.deleteByAuditSubCriteriaId(Long.parseLong(id));
			
			if(res) {
				return new ResponseEntity<>("Audit Sub Criteria is successfully deleted.", HttpStatus.OK); 
			}
			else {
				return new ResponseEntity<>("Error in deleting the audit sub criteria.", HttpStatus.BAD_REQUEST);
			}
		
	}
	
	
	@PostMapping(AuditSubCriteriaServiceAPIs.UPDATE_AUDIT_SUB_CRITERIA)
	public ResponseEntity<?> updateAuditSubCriteria(@RequestBody AuditSubCriteriaDTO auditSubCriteriaObj){
		
				String id = EncryptionUtil.decrypt(auditSubCriteriaObj.getAuditCriteriaId());
				Long cid = Long.parseLong(id);
				String sid =  EncryptionUtil.decrypt(auditSubCriteriaObj.getAuditSubCriteriaId());
				Long aid = Long.parseLong(sid);
				//Server Side Validation
		
				if(auditSubCriteriaObj.getAuditSubCriteriaTitle().equals("")) {
					return new ResponseEntity<>("Please enter audit sub criteria.", HttpStatus.BAD_REQUEST);
				}else if(!auditSubCriteriaObj.getAuditSubCriteriaTitle().trim().matches("^[A-Za-z() ]+$")) {
					return new ResponseEntity<>("Only alphabets, parenthesis and spaces are allowed.", HttpStatus.BAD_REQUEST);
				}else if(auditSubCriteriaObj.getAuditSubCriteriaTitle().length() < 3 || auditSubCriteriaObj.getAuditSubCriteriaTitle().length() > 155) {
					return new ResponseEntity<>("The length must be between 3 and 155 characters.", HttpStatus.BAD_REQUEST);
				}
						
						
				//Check for Unique Audit Criteria Title
				AuditSubCriteria cobj = auditSubCriteriaServ.getAuditSubCriteriaByTitle(auditSubCriteriaObj.getAuditSubCriteriaTitle().trim());
				if(cobj != null && cobj.getAuditSubCriteriaId() != aid) {
					return new ResponseEntity<>("Duplicate Audit Sub Criteria Title is not allowed.", HttpStatus.BAD_REQUEST);
				}
				
				
				
				
				try {
					
					AuditSubCriteria obj = new AuditSubCriteria();
					obj.setAuditSubCriteriaTitle(auditSubCriteriaObj.getAuditSubCriteriaTitle().trim());
					obj.setStatus(auditSubCriteriaObj.getStatus() != null
							? auditSubCriteriaObj.getStatus().trim()
							: "Active");
					obj.setAuditCriteriaId(auditServ.getByAuditCriteriaId(cid));
					obj.setAuditSubCriteriaId(aid);
					
					//
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
					Date cdate = sdf.parse(auditSubCriteriaObj.getCreated());	
					obj.setCreated(cdate);
					
					
					boolean visible = auditSubCriteriaObj.getVisible() != null
							&& auditSubCriteriaObj.getVisible().equalsIgnoreCase("TRUE");
					if (auditSubCriteriaObj.getStatus() != null
							&& "Inactive".equalsIgnoreCase(auditSubCriteriaObj.getStatus().trim())) {
						visible = false;
					}
					obj.setVisible(visible);
					
					
					
					Optional<AuditSubCriteria> c = auditSubCriteriaServ.addAuditSubCriteria(obj);
					
					if(c.isEmpty())
						return new ResponseEntity<>("An error occurred while updating the audit sub criteria. Please Try again.", HttpStatus.OK);
					return new ResponseEntity<>("Audit Sub Criteria successfully updated.", HttpStatus.OK);
				}catch(Exception e) {
					e.printStackTrace();
					return new ResponseEntity<>("An error occurred while updating the audit sub criteria. Please Try again.", HttpStatus.BAD_REQUEST);
				}
	}
	
	
	
	@GetMapping(AuditSubCriteriaServiceAPIs.GET_ENABLED_AUDIT_SUB_CRITERIA_FOR_AUDITOR)
	public ResponseEntity<List<AuditSubCriteriaDTO>> getEnabledAuditSubCriteriaForAuditor() {
		List<AuditSubCriteriaDTO> enabledList = auditSubCriteriaServ.getAllEnabledForAuditorView().stream()
				.map(subCriteria -> {
					AuditSubCriteriaDTO dto = new AuditSubCriteriaDTO();
					dto.setAuditSubCriteriaId(String.valueOf(subCriteria.getAuditSubCriteriaId()));
					dto.setAuditSubCriteriaTitle(subCriteria.getAuditSubCriteriaTitle());
					dto.setStatus(subCriteria.getStatus());
					dto.setVisible(subCriteria.isVisible() ? "Yes" : "No");
					if (subCriteria.getAuditCriteriaId() != null) {
						dto.setAuditCriteriaId(
								String.valueOf(subCriteria.getAuditCriteriaId().getAuditCriteriaId()));
					}
					return dto;
				})
				.collect(Collectors.toList());
		return new ResponseEntity<>(enabledList, HttpStatus.OK);
	}

	@GetMapping(AuditSubCriteriaServiceAPIs.GET_AUDIT_SUB_CRITERIA_BY_AUDIT_CRITERIA_ID)
	public ResponseEntity<?> getAuditSubCriteriaByAuditCriteriaId(@RequestParam("id") String auditId)
	{
		String id = EncryptionUtil.decrypt(auditId);
		
		try {
			List<AuditSubCriteria> auditObj = auditSubCriteriaServ.getAllEnabledForAuditorView().stream()
					.filter(sc -> sc.getAuditCriteriaId() != null
							&& sc.getAuditCriteriaId().getAuditCriteriaId().equals(Long.parseLong(id)))
					.collect(Collectors.toList());
			return new ResponseEntity<>(auditObj, HttpStatus.OK); 
		}catch(Exception e) {
			return new ResponseEntity<>("Invalid Audit Criteria Id.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
}
