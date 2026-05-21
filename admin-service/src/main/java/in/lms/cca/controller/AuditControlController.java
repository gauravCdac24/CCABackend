package in.lms.cca.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Objects;

import in.lms.cca.dto.AuditControlDTO;
import in.lms.cca.dto.AuditReportCriteriaDTO;
import in.lms.cca.dto.AuditRequestDTO;
import in.lms.cca.entity.AuditControl;
import in.lms.cca.entity.AuditCriteria;
import in.lms.cca.entity.AuditParameter;
import in.lms.cca.entity.AuditSubCriteria;
import in.lms.cca.payload.AuditControlPayload;
import in.lms.cca.payload.AuditCriteriaPayload;
import in.lms.cca.payload.AuditParameterPayload;
import in.lms.cca.payload.AuditSubCriteriaPayload;
import in.lms.cca.repository.AuditSubCriteriaRepository;
import in.lms.cca.service.IAuditParameterService;
import in.lms.cca.service.IAuditSubCriteriaService;
import in.lms.cca.service.IAuditCheckService;
import in.lms.cca.service.IAuditControlService;
import in.lms.cca.service.IAuditControlTypeService;
import in.lms.cca.service.IAuditCriteriaService;
import in.lms.cca.util.api.AdminServiceAPIs;
import in.lms.cca.util.api.AuditControlServiceAPIs;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
@RequestMapping(AdminServiceAPIs.ADMIN_SERVICE_BASE_URL)
@CrossOrigin
public class AuditControlController {

	@Autowired
	private IAuditParameterService auditParameterServ;
	
	@Autowired
	private IAuditCheckService auditCheckServ;
	
	@Autowired
	private IAuditControlTypeService auditControlTypeServ;
	
	@Autowired
	private IAuditControlService auditControlServ;
	
	
	@Autowired
	private IAuditCriteriaService auditServ;
	
	@Autowired
	private IAuditSubCriteriaService auditSubCriteriaServ;
	
	
	
	@PostMapping(AuditControlServiceAPIs.ADD_AUDIT_CONTROL)
	public ResponseEntity<?> addNewAuditControl(@RequestBody AuditControlDTO auditControlObj){
		
		System.out.println("auditControlObj-=-=->"+auditControlObj.toString());
		
		Long aParameterId = Long.parseLong(EncryptionUtil.decrypt(auditControlObj.getAuditParameterId()));
		Long aCheckId = Long.parseLong(EncryptionUtil.decrypt(auditControlObj.getAuditCheckId()));
		Integer aControlTypeId = Integer.parseInt(EncryptionUtil.decrypt(auditControlObj.getAuditControlTypeId()));
		
		
		if(auditControlObj.getControlDesc().equals("")) {
			return new ResponseEntity<>("Please enter audit control.", HttpStatus.BAD_REQUEST);
		}
		
		if(auditControlObj.getReferences().equals("")) {
			return new ResponseEntity<>("Please enter references.", HttpStatus.BAD_REQUEST);
		}else if(!auditControlObj.getReferences().trim().matches("^[A-Za-z0-9(). ]+$")) {
			return new ResponseEntity<>("Alphabets, digits, parenthesis, . and spaces are allowed.", HttpStatus.BAD_REQUEST);
		}else if(auditControlObj.getReferences().length() < 3 || auditControlObj.getReferences().length() > 60) {
			return new ResponseEntity<>("The length must be between 3 and 60 characters.", HttpStatus.BAD_REQUEST);
		}
				
				
		AuditControl cobj = auditControlServ.getAuditControlByDesc(auditControlObj.getControlDesc().trim());
		if(cobj != null) {
			return new ResponseEntity<>("Duplicate Audit Control is not allowed.", HttpStatus.BAD_REQUEST);
		}
		
		
		try {
		    AuditControl obj = new AuditControl();
		    System.out.println("auditControlObj-=-=->"+auditControlObj.toString());
		    obj.setControlDesc(auditControlObj.getControlDesc());
		    obj.setStatus("Active");
		    obj.setAuditParameterId(auditParameterServ.getByAuditParameterId(aParameterId));
		    obj.setAuditCheckId(auditCheckServ.getAuditCheckById(aCheckId));
		    obj.setAuditControlTypeId(auditControlTypeServ.getByAuditControlTypeId(aControlTypeId));
		    obj.setReferences(auditControlObj.getReferences());
		    
		    Optional<AuditControl> c;

		    if ("notNow".equals(auditControlObj.getSelfAuditControlId())) {

		        c = auditControlServ.addAuditControl(obj);
		    } else {
		   
		        AuditControl existingAuditControl = auditControlServ.getByAuditControlId(Long.valueOf(auditControlObj.getSelfAuditControlId()));
		        
		        System.out.println("existingAuditControl-=-->"+existingAuditControl.getAuditControlId());
		        
		        if (existingAuditControl != null) {
		        	
		        	  System.out.println("getEffectiveDate-=--->"+auditControlObj.getEffectiveDate());
		        	
		            Date validTillDate = new SimpleDateFormat("yyyy-mm-dd").parse(auditControlObj.getEffectiveDate());
		            Calendar calendar = Calendar.getInstance();
		            calendar.setTime(validTillDate);
		            calendar.add(Calendar.DAY_OF_MONTH, -1); 
		            Date adjustedDate = calendar.getTime();
		            
		            System.out.println("adjustedDate-=--->"+adjustedDate);

		            existingAuditControl.setValidTill(adjustedDate);
		            existingAuditControl.setStatus("Inactive");
		            auditControlServ.updateAuditControl(existingAuditControl);
		        }

		      
		        obj.setSelfAuditControlId(existingAuditControl);
		        Date effectiveDate = new SimpleDateFormat("yyyy-mm-dd").parse(auditControlObj.getEffectiveDate());
		        obj.setEffectiveDate(effectiveDate);

		        c = auditControlServ.addAuditControl(obj);
		    }

		    if (c.isEmpty()) {
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                .body("An error occurred while saving the audit control. Please try again.");
		    }

		    return ResponseEntity.status(HttpStatus.CREATED)
		            .body("Audit Control successfully added.");
		} catch (Exception e) {
		    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
		            .body("An error occurred while saving the audit control. Please try again.");
		}
		
 
		
	}
	
	
	@GetMapping(AuditControlServiceAPIs.GET_ALL_AUDIT_CONTROL)
	public ResponseEntity<?> getAllAuditControl(){
		
		List<AuditControl> auditControlList = auditControlServ.getAllAuditControl();
		return new ResponseEntity<>(auditControlList, HttpStatus.OK); 
		
	}
	
	@GetMapping(AuditControlServiceAPIs.CHANGE_AUDIT_CONTROL_STATUS)
	public void changeAuditControlStatus(@RequestParam("id") String auditId) {
		
		String id = EncryptionUtil.decrypt(auditId);
		
		
		try {
			AuditControl c = auditControlServ.getByAuditControlId(Long.parseLong(id));
			
			if(c.getStatus().equals("Active"))
				c.setStatus("Inactive");
			else
				c.setStatus("Active");
			
			auditControlServ.updateAuditControl(c);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	@GetMapping(AuditControlServiceAPIs.GET_AUDIT_CONTROL_BY_ID)
	public ResponseEntity<?> getAuditControlById(@RequestParam("id") String auditId)
	{
		
		String id = EncryptionUtil.decrypt(auditId);
		
		try {
			AuditControl auditObj = auditControlServ.getByAuditControlId(Long.parseLong(id));
			return new ResponseEntity<>(auditObj, HttpStatus.OK); 
		}catch(Exception e) {
			return new ResponseEntity<>("Invalid Audit Control Id.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@GetMapping(AuditControlServiceAPIs.DELETE_AUDIT_CONTROL_BY_ID)
	public ResponseEntity<?> deleteAuditControlById(@RequestParam("id") String auditId) {
		
		String id = EncryptionUtil.decrypt(auditId);
		
		
			boolean res = auditControlServ.deleteByAuditControlId(Long.parseLong(id));
			
			if(res) {
				return new ResponseEntity<>("Audit control is successfully deleted.", HttpStatus.OK); 
			}
			else {
				return new ResponseEntity<>("Error in deleting the audit control.", HttpStatus.BAD_REQUEST);
			}
		
	}
	
	
	@PostMapping(AuditControlServiceAPIs.UPDATE_AUDIT_CONTROL)
	public ResponseEntity<?> updateAuditControl(@RequestBody AuditControlDTO auditControlObj){
		
		
		Long aParameterId = Long.parseLong(EncryptionUtil.decrypt(auditControlObj.getAuditParameterId()));
		Long aCheckId = Long.parseLong(EncryptionUtil.decrypt(auditControlObj.getAuditCheckId()));
		Integer aControlTypeId = Integer.parseInt(EncryptionUtil.decrypt(auditControlObj.getAuditControlTypeId()));
		Long aControlId = Long.parseLong(EncryptionUtil.decrypt(auditControlObj.getAuditControlId()));
		
		//Server Side Validation
		
		if(auditControlObj.getControlDesc().equals("")) {
			return new ResponseEntity<>("Please enter audit control.", HttpStatus.BAD_REQUEST);
		}
		
		if(auditControlObj.getReferences().equals("")) {
			return new ResponseEntity<>("Please enter references.", HttpStatus.BAD_REQUEST);
		}else if(!auditControlObj.getReferences().trim().matches("^[A-Za-z0-9().\\- ]+$")) {
			return new ResponseEntity<>("Alphabets, digits, parenthesis, . and spaces are allowed.", HttpStatus.BAD_REQUEST);
		}else if(auditControlObj.getReferences().length() < 3 || auditControlObj.getReferences().length() > 60) {
			return new ResponseEntity<>("The length must be between 3 and 60 characters.", HttpStatus.BAD_REQUEST);
		}
				
				
		//Check for Unique Audit Control
		AuditControl cobj = auditControlServ.getAuditControlByDesc(auditControlObj.getControlDesc().trim());
		
		if(cobj != null && !cobj.getAuditControlId().equals(aControlId)) {
			return new ResponseEntity<>("Duplicate Audit Control is not allowed.", HttpStatus.BAD_REQUEST);
		}
		
		
		try {
			
			AuditControl obj = new AuditControl();
			obj.setControlDesc(auditControlObj.getControlDesc());
			obj.setStatus(auditControlObj.getStatus());
			obj.setAuditParameterId(auditParameterServ.getByAuditParameterId(aParameterId));
			obj.setAuditCheckId(auditCheckServ.getAuditCheckById(aCheckId));
			obj.setAuditControlTypeId(auditControlTypeServ.getByAuditControlTypeId(aControlTypeId));
			obj.setReferences(auditControlObj.getReferences());
			obj.setAuditControlId(aControlId);
			//
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
			Date cdate = sdf.parse(auditControlObj.getCreated());	
			obj.setCreated(cdate);
			
			
			Optional<AuditControl> c = auditControlServ.updateAuditControl(obj);
			
			if(c.isEmpty())
				return new ResponseEntity<>("An error occurred while updating the audit control. Please Try again.", HttpStatus.OK);
			return new ResponseEntity<>("Audit Control successfully updated.", HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("An error occurred while updating the audit control. Please Try again.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	

	@GetMapping(AuditControlServiceAPIs.DATA)
	public List<AuditCriteriaPayload> getData(@RequestParam String userName) {
		
		System.out.println("userNAme=-=->"+userName);

	    List<AuditRequestDTO> auditRequestDTOs = auditControlServ.getAllAuditRequestByUserName(userName);

	    List<AuditControl> auditControlList = auditControlServ.getAllActiveAuditControl();
	    List<AuditSubCriteria> auditSubCriterias = auditSubCriteriaServ.getAllEnabledForAuditorView();
	    List<AuditCriteria> auditCriterias = auditServ.getAllActiveAuditCriteria();

	    Set<Long> enabledSubCriteriaIds = auditSubCriterias.stream()
	            .map(AuditSubCriteria::getAuditSubCriteriaId)
	            .collect(Collectors.toSet());

	    Set<Long> assignedControlIds = auditRequestDTOs.stream()
	            .map(dto -> Long.valueOf(dto.getControlId()))
	            .collect(Collectors.toSet());

	    Set<Long> requestedCriteriaIds = auditControlList.stream()
	            .filter(control -> control.getAuditParameterId() != null
	                    && control.getAuditParameterId().getAuditSubCriteriaId() != null
	                    && control.getAuditParameterId().getAuditSubCriteriaId().getAuditCriteriaId() != null)
	            .filter(control -> assignedControlIds.isEmpty()
	                    || assignedControlIds.contains(control.getAuditControlId()))
	            .map(control -> control.getAuditParameterId().getAuditSubCriteriaId().getAuditCriteriaId()
	                    .getAuditCriteriaId())
	            .collect(Collectors.toSet());

	    if (requestedCriteriaIds.isEmpty()) {
	    	requestedCriteriaIds = auditCriterias.stream()
	    			.map(AuditCriteria::getAuditCriteriaId)
	    			.collect(Collectors.toSet());
	    }

	    List<AuditParameter> auditParameters = auditParameterServ.getAllActiveAuditParameter().stream()
	            .filter(parameter -> parameter.getAuditSubCriteriaId() != null
	                    && enabledSubCriteriaIds.contains(parameter.getAuditSubCriteriaId().getAuditSubCriteriaId()))
	            .collect(Collectors.toList());

	    Map<Long, List<AuditControl>> controlMap = auditControlList.stream()
	            .filter(control -> control.getAuditParameterId() != null
	                    && control.getAuditParameterId().getAuditSubCriteriaId() != null
	                    && enabledSubCriteriaIds.contains(
	                            control.getAuditParameterId().getAuditSubCriteriaId().getAuditSubCriteriaId()))
	            .collect(Collectors.groupingBy(control -> control.getAuditParameterId().getAuditParameterId()));

	    Map<Long, List<AuditParameter>> parameterMap = auditParameters.stream()
	            .filter(parameter -> parameter.getAuditSubCriteriaId() != null)
	            .collect(Collectors.groupingBy(parameter -> parameter.getAuditSubCriteriaId().getAuditSubCriteriaId()));

	    Map<Long, List<AuditSubCriteria>> subCriteriaMap = auditSubCriterias.stream()
	            .filter(subCriteria -> subCriteria.getAuditCriteriaId() != null)
	            .collect(Collectors.groupingBy(subCriteria -> subCriteria.getAuditCriteriaId().getAuditCriteriaId()));

	    final Set<Long> criteriaIdsToShow = requestedCriteriaIds;
	    return auditCriterias.stream()
	            .filter(criteria -> criteriaIdsToShow.contains(criteria.getAuditCriteriaId()))
	            .map(criteria -> {
	                AuditCriteriaPayload criteriaPayload = new AuditCriteriaPayload();
	                criteriaPayload.setAuditCriteria(criteria.getAuditCriteriaTitle());

	                List<AuditSubCriteriaPayload> subCriteriaPayloads = subCriteriaMap.getOrDefault(criteria.getAuditCriteriaId(), List.of())
	                        .stream()
	                        .filter(this::isEnabledAuditSubCriteria)
	                        .map(subCriteria -> {
	                            AuditSubCriteriaPayload subPayload = new AuditSubCriteriaPayload();
	                            subPayload.setAuditSubCriteriaId(String.valueOf(subCriteria.getAuditSubCriteriaId()));
	                            subPayload.setAuditSubCriteria(subCriteria.getAuditSubCriteriaTitle());
	                            subPayload.setAuditCriteriaId(String.valueOf(subCriteria.getAuditCriteriaId().getAuditCriteriaId()));
	                            subPayload.setStatus(subCriteria.getStatus());
	                            subPayload.setVisible(subCriteria.isVisible());

	                            List<AuditParameterPayload> parameterPayloads = parameterMap.getOrDefault(subCriteria.getAuditSubCriteriaId(), List.of())
	                                    .stream().map(parameter -> {
	                                        AuditParameterPayload parameterPayload = new AuditParameterPayload();
	                                        parameterPayload.setAuditParameter(parameter.getAuditParameterTitle());

	                                        List<AuditControlPayload> controlPayloads = controlMap.getOrDefault(parameter.getAuditParameterId(), List.of())
	                                                .stream().map(this::mapControlToPayload)
	                                                .collect(Collectors.toList());

	                                        parameterPayload.setAuditControlPayload(controlPayloads);
	                                        return parameterPayload;
	                                    }).collect(Collectors.toList());

	                            subPayload.setAuditParameterPayload(parameterPayloads);
	                            return subPayload;
	                        }).collect(Collectors.toList());

	                criteriaPayload.setAuditSubCriteria(subCriteriaPayloads);
	                return criteriaPayload;
	            })
	            .filter(criteriaPayload -> criteriaPayload.getAuditSubCriteria() != null
	                    && !criteriaPayload.getAuditSubCriteria().isEmpty())
	            .collect(Collectors.toList());
	}

	private boolean isEnabledAuditSubCriteria(AuditSubCriteria subCriteria) {
		return subCriteria != null
				&& subCriteria.getStatus() != null
				&& "Active".equalsIgnoreCase(subCriteria.getStatus().trim())
				&& subCriteria.isVisible();
	}

	private AuditControlPayload mapControlToPayload(AuditControl control) {
	    AuditControlPayload controlPayload = new AuditControlPayload();
	    controlPayload.setAuditControllId(String.valueOf(control.getAuditControlId()));
	    controlPayload.setAuditControl(control.getControlDesc());
	    if (control.getAuditCheckId() != null) {
	        controlPayload.setAuditCheck(control.getAuditCheckId().getAuditCheckDesc());
	    }
	    if (control.getAuditControlTypeId() != null) {
	        controlPayload.setControlType(control.getAuditControlTypeId().getAuditControlDesc());
	    }
	    controlPayload.setReferences(control.getReferences());
	    return controlPayload;
	}
	
	
	@GetMapping(AuditControlServiceAPIs.NC_DATA)
	public List<AuditCriteriaPayload> getNCData(@RequestParam String userName) {

	    List<AuditReportCriteriaDTO> auditReportCriteriaDTOs = auditControlServ.getAllNCAuditReportCriteria(userName);

	    List<AuditReportCriteriaDTO> filteredCriteria = auditReportCriteriaDTOs.stream()
	            .filter(dto -> "No".equals(dto.getCompliance()))
	            .collect(Collectors.toList());

	    List<AuditControl> auditControlList = auditControlServ.getAllAuditControl();

	    // Filtering and matching AuditControls with the filtered criteria
	    List<AuditControl> matchedAuditControls = auditControlList.stream()
	            .filter(a -> filteredCriteria.stream()
	                    .anyMatch(f -> f.getAuditControlId().split("-")[1].equals(a.getAuditControlId().toString())))
	            .collect(Collectors.toList());
	    
	    

	    // Group by AuditCriteria, then by AuditSubCriteria, then by AuditParameter
	    Map<String, Map<String, Map<String, List<AuditControl>>>> groupedControls = matchedAuditControls.stream()
	            .collect(Collectors.groupingBy(
	                    a -> a.getAuditParameterId().getAuditSubCriteriaId().getAuditCriteriaId().getAuditCriteriaTitle(),
	                    Collectors.groupingBy(
	                            a -> a.getAuditParameterId().getAuditSubCriteriaId().getAuditSubCriteriaTitle(),
	                            Collectors.groupingBy(
	                                    a -> a.getAuditParameterId().getAuditParameterTitle()
	                            )
	                    )
	            ));

	    // Now create the AuditCriteriaPayload list based on the grouped data
	    List<AuditCriteriaPayload> auditCriteriaPayloadList = groupedControls.entrySet().stream()
	            .map(entry -> {
	                String auditCriteriaTitle = entry.getKey();
	                Map<String, Map<String, List<AuditControl>>> subCriteriaMap = entry.getValue();

	                List<AuditSubCriteriaPayload> auditSubCriteriaPayloadList = subCriteriaMap.entrySet().stream()
	                        .map(subEntry -> {
	                            String subCriteriaTitle = subEntry.getKey();
	                            Map<String, List<AuditControl>> parameterMap = subEntry.getValue();

	                            List<AuditParameterPayload> auditParameterPayloadList = parameterMap.entrySet().stream()
	                                    .map(paramEntry -> {
	                                        String parameterTitle = paramEntry.getKey();
	                                        List<AuditControl> auditControls = paramEntry.getValue();

	                                        List<AuditControlPayload> auditControlPayloadList = auditControls.stream()
	                                                .map(auditControl -> {
	                                                    AuditControlPayload auditControlPayload = new AuditControlPayload();
	                                                    auditControlPayload.setAuditControllId(auditControl.getAuditControlId().toString());
	                                                    auditControlPayload.setAuditControl(auditControl.getControlDesc());
	                                                    auditControlPayload.setAuditCheck(auditControl.getAuditCheckId().getAuditCheckDesc());
	                                                    auditControlPayload.setControlType(auditControl.getAuditControlTypeId().getAuditControlDesc());
	                                                    auditControlPayload.setReferences(auditControl.getReferences());
	                                                    return auditControlPayload;
	                                                })
	                                                .collect(Collectors.toList());

	                                        AuditParameterPayload auditParameterPayload = new AuditParameterPayload();
	                                        auditParameterPayload.setAuditParameter(parameterTitle);
	                                        auditParameterPayload.setAuditControlPayload(auditControlPayloadList);

	                                        return auditParameterPayload;
	                                    })
	                                    .collect(Collectors.toList());

	                            AuditSubCriteriaPayload auditSubCriteriaPayload = new AuditSubCriteriaPayload();
	                            auditSubCriteriaPayload.setAuditCriteriaId(auditCriteriaTitle);
	                            auditSubCriteriaPayload.setAuditSubCriteria(subCriteriaTitle);
	                            auditSubCriteriaPayload.setAuditParameterPayload(auditParameterPayloadList);

	                            return auditSubCriteriaPayload;
	                        })
	                        .collect(Collectors.toList());

	                AuditCriteriaPayload auditCriteriaPayload = new AuditCriteriaPayload();
	                auditCriteriaPayload.setAuditCriteria(auditCriteriaTitle);
	                auditCriteriaPayload.setAuditSubCriteria(auditSubCriteriaPayloadList);

	                return auditCriteriaPayload;
	            })
	            .collect(Collectors.toList());

	    return auditCriteriaPayloadList;
	}

	
	private AuditControlPayload mapControlToPayloads(AuditControl control) {
	    AuditControlPayload controlPayload = new AuditControlPayload();
	    controlPayload.setAuditControllId(String.valueOf(control.getAuditControlId())); // Ensure controlId is in String format
	    controlPayload.setAuditControl(control.getControlDesc());

	  
	    if (control.getAuditCheckId() != null) {
	        controlPayload.setAuditCheck(control.getAuditCheckId().getAuditCheckDesc());
	    }
	    if (control.getAuditControlTypeId() != null) {
	        controlPayload.setControlType(control.getAuditControlTypeId().getAuditControlDesc());
	    }

	    controlPayload.setReferences(control.getReferences());
	    return controlPayload;
	}

	
	
	
}
