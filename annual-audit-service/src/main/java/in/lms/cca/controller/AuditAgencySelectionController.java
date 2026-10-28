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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.lms.cca.dto.StatusChangedOfLicense;
import in.lms.cca.entity.AnnualAuditScheduleEntity;
import in.lms.cca.entity.AuditAgencySelectionEntity;
import in.lms.cca.entity.LicenseeAuditEntity;
import in.lms.cca.payload.AuditAgencySelectionDTO;
import in.lms.cca.payload.SelectionData;
import in.lms.cca.service.ApplicationAuditService;
import in.lms.cca.service.AuditAgencySelectionService;
import in.lms.cca.service.LicenseeAuditservice;
import in.lms.cca.util.api.AuditAgencySelectionAPIs;
import in.lms.cca.util.api.AuditServiceAPIs;
import in.lms.cca.util.global.EncryptionUtil;




@RestController
@CrossOrigin
@RequestMapping(AuditServiceAPIs.AUDIT_SERVICE_BASE_URL)
public class AuditAgencySelectionController {
	
	@Autowired
	private AuditAgencySelectionService selectionServ;
	
	@Autowired
	private ApplicationAuditService applicationAuditServ;

	@Autowired
	private LicenseeAuditservice licenseeAuditservice;
	
	
	@PostMapping(AuditAgencySelectionAPIs.AUDIT_AGENCY_SELECTION)
	public ResponseEntity<?> addAuditAgencySelection(@RequestBody SelectionData selectionData) {
	    try {
	        Long auditScheduleId = null;
	        if (selectionData.getAuditScheduleId() != null && !selectionData.getAuditScheduleId().isBlank()) {
	            auditScheduleId = Long.parseLong(selectionData.getAuditScheduleId());
	        }

	        if (auditScheduleId != null) {
	            LicenseeAuditEntity activeAudit =
	                    applicationAuditServ.findActiveByAuditScheduleId(auditScheduleId);
	            if (activeAudit != null) {
	                AuditAgencySelectionEntity existingSelection =
	                        selectionServ.findbyAuditId(activeAudit);
	                if (existingSelection != null
	                        && "Active".equalsIgnoreCase(existingSelection.getStatus())) {
	                    existingSelection.setAuditAgencyId(selectionData.getAuditAgencyId());
	                    existingSelection.setAuditAgencyName(selectionData.getAuditAgencyName());
	                    existingSelection.setUpdated(new Date());
	                    selectionServ.upDateData(existingSelection);
	                    return new ResponseEntity<>(
	                            "Audit Agency Selection successfully updated.", HttpStatus.OK);
	                }

	                AuditAgencySelectionEntity agencySelectionEntity = new AuditAgencySelectionEntity();
	                agencySelectionEntity.setAuditAgencyName(selectionData.getAuditAgencyName());
	                agencySelectionEntity.setAuditAgencyId(selectionData.getAuditAgencyId());
	                agencySelectionEntity.setLicenseeAuditId(activeAudit);
	                agencySelectionEntity.setStatus("Active");
	                Optional<AuditAgencySelectionEntity> selectionResult =
	                        selectionServ.addAuditAgencySelection(agencySelectionEntity);
	                if (selectionResult.isEmpty()) {
	                    return new ResponseEntity<>("Error saving Audit Agency Selection.",
	                            HttpStatus.INTERNAL_SERVER_ERROR);
	                }
	                return new ResponseEntity<>("Audit Agency Selection successfully added.",
	                        HttpStatus.OK);
	            }
	        } else {
	            List<LicenseeAuditEntity> applicationAuditEntities =
	                    applicationAuditServ.getByApplicantUserNames(selectionData.getApplicantUserName());
	            if (!applicationAuditEntities.isEmpty()) {
	                for (LicenseeAuditEntity auditEntity : applicationAuditEntities) {
	                    auditEntity.setStatus("Inactive");
	                    auditEntity.setUpdated(new Date());
	                    applicationAuditServ.updateData(auditEntity);

	                    List<AuditAgencySelectionEntity> agencySelectionEntities =
	                            selectionServ.getAllAuditId(auditEntity.getLicenseeAuditId());
	                    for (AuditAgencySelectionEntity selectionEntity : agencySelectionEntities) {
	                        selectionEntity.setStatus("Inactive");
	                        selectionEntity.setUpdated(new Date());
	                        selectionServ.upDateData(selectionEntity);
	                    }
	                }
	            }
	        }

	        LicenseeAuditEntity applicationAuditEntity = new LicenseeAuditEntity();
	        applicationAuditEntity.setApplicantUserName(selectionData.getApplicantUserName());
	        applicationAuditEntity.setStatus("Active");

	        if (auditScheduleId != null) {
	            Optional<AnnualAuditScheduleEntity> scheduleEntity =
	                    licenseeAuditservice.getAnnualAuditScheduleById(auditScheduleId);
	            if (scheduleEntity.isEmpty()) {
	                return new ResponseEntity<>("Annual audit schedule not found.",
	                        HttpStatus.BAD_REQUEST);
	            }
	            applicationAuditEntity.setAuditScheduleId(scheduleEntity.get());
	        }

	        Optional<LicenseeAuditEntity> auditResult =
	            applicationAuditServ.addApplicationAudit(applicationAuditEntity);

	        if (auditResult.isEmpty()) {
	            return new ResponseEntity<>("Error creating application audit entity.",
	                                        HttpStatus.INTERNAL_SERVER_ERROR);
	        }

	        AuditAgencySelectionEntity agencySelectionEntity = new AuditAgencySelectionEntity();
	        agencySelectionEntity.setAuditAgencyName(selectionData.getAuditAgencyName());
	        agencySelectionEntity.setAuditAgencyId(selectionData.getAuditAgencyId());
	        agencySelectionEntity.setLicenseeAuditId(auditResult.get());
	        agencySelectionEntity.setStatus("Active");

	        Optional<AuditAgencySelectionEntity> selectionResult =
	            selectionServ.addAuditAgencySelection(agencySelectionEntity);

	        if (selectionResult.isEmpty()) {
	            return new ResponseEntity<>("Error saving Audit Agency Selection.",
	                                        HttpStatus.INTERNAL_SERVER_ERROR);
	        }

	        return new ResponseEntity<>("Audit Agency Selection successfully added.", HttpStatus.OK);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>("An error occurred while processing the request.",
	                                    HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@GetMapping(AuditAgencySelectionAPIs.GET_AUDIT_AGENCY_SELECTION_BY_SCHEDULE)
	public ResponseEntity<?> getAuditAgencySelectionBySchedule(
	        @RequestParam String auditScheduleId) {
	    try {
	        String decryptedScheduleId = auditScheduleId;
	        if (EncryptionUtil.decrypt(auditScheduleId) != null) {
	            decryptedScheduleId = EncryptionUtil.decrypt(auditScheduleId);
	        }
	        Long scheduleId = Long.parseLong(decryptedScheduleId);
	        LicenseeAuditEntity activeAudit =
	                applicationAuditServ.findActiveByAuditScheduleId(scheduleId);
	        if (activeAudit == null) {
	            return ResponseEntity.ok(null);
	        }

	        AuditAgencySelectionEntity selectionEntity =
	                selectionServ.findbyAuditId(activeAudit);
	        if (selectionEntity == null
	                || !"Active".equalsIgnoreCase(selectionEntity.getStatus())) {
	            return ResponseEntity.ok(null);
	        }

	        AuditAgencySelectionDTO dto = new AuditAgencySelectionDTO();
	        dto.setAgencySelectionId(selectionEntity.getAgencySelectionId());
	        dto.setAuditAgencyId(selectionEntity.getAuditAgencyId());
	        dto.setAuditAgencyName(selectionEntity.getAuditAgencyName());
	        dto.setAppAuditId(activeAudit.getLicenseeAuditId());
	        dto.setAplicantUserName(activeAudit.getApplicantUserName());
	        dto.setStatus(selectionEntity.getStatus());
	        dto.setAuditScheduleId(String.valueOf(scheduleId));
	        return ResponseEntity.ok(dto);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("An error occurred while fetching audit agency selection.");
	    }
	}
	 
	
	 
	  @GetMapping(AuditAgencySelectionAPIs.GET_ALL_AUDIT_SELECTION)
	    public ResponseEntity<?> getAllServices() {
	        List<AuditAgencySelectionEntity> agencySelectionEntities = selectionServ.getAllServiceMaster();
	        return new ResponseEntity<>(agencySelectionEntities, HttpStatus.OK);
	    }

	  @GetMapping(AuditAgencySelectionAPIs.GET_ALL_DATA_BY_USERNAME)
	  public ResponseEntity<?> getAllDetailsByUserName(@PathVariable String userId) {
	      try {
	          // Decrypt the userId
	          String decryptedUserId = EncryptionUtil.decrypt(userId);

	          // Fetch all agency selection entities for the given userId
	          List<AuditAgencySelectionEntity> agencySelectionEntities = 
	                  selectionServ.getAllDetailsByAuditAgencyId(decryptedUserId);

	          // If no entities are found, return a NOT_FOUND response
	          if (agencySelectionEntities == null || agencySelectionEntities.isEmpty()) {
	              return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                      .body("No audit agency selection details found for user: " + userId);
	          }

	          // Convert the list of entities to a list of DTOs
	          List<AuditAgencySelectionDTO> agencySelectionDTOs = new ArrayList<>();
	          for (AuditAgencySelectionEntity entity : agencySelectionEntities) {
	              AuditAgencySelectionDTO dto = new AuditAgencySelectionDTO();
	              dto.setAgencySelectionId(entity.getAgencySelectionId());
	              LicenseeAuditEntity applicationAuditEntity=applicationAuditServ.getAuditDetailsById(entity.getLicenseeAuditId().getLicenseeAuditId());
	              
	              dto.setAplicantUserName(applicationAuditEntity.getApplicantUserName());
	              dto.setAuditAgencyId(entity.getAuditAgencyId());
	              dto.setAppAuditId(entity.getLicenseeAuditId() != null 
	                      ? entity.getLicenseeAuditId().getLicenseeAuditId() 
	                      : null);
	              dto.setReviewedBy(entity.getReviewedBy());
	              dto.setRemarks(entity.getRemarks());
	              dto.setCreatedBy(entity.getCreatedBy());
	              dto.setUpdatedBy(entity.getUpdatedBy());
	              dto.setStatus(entity.getStatus());
	              agencySelectionDTOs.add(dto);
	          }

	          // Log the DTOs for debugging
	          System.out.println("agencySelectionDTOs: " + agencySelectionDTOs);

	          // Return the list of DTOs
	          return ResponseEntity.ok(agencySelectionDTOs);

	      } catch (Exception e) {
	          e.printStackTrace();
	          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                  .body("An error occurred while fetching details: " + e.getMessage());
	      }
	  }

	  
	  
}
