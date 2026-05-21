package in.lms.cca.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import in.lms.cca.entity.ApplicationAuditEntity;
import in.lms.cca.entity.AuditAgencySelectionEntity;
import in.lms.cca.payload.AuditAgencySelectionDTO;
import in.lms.cca.payload.SelectionData;
import in.lms.cca.service.ApplicationAuditService;
import in.lms.cca.service.AuditAgencySelectionService;
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
	private RestTemplate restTemplate;
	
	@Value("${apigateway.service.url}")
	private String apigatewayServiceUrl;
	
	
	@PostMapping(AuditAgencySelectionAPIs.AUDIT_AGENCY_SELECTION)
	public ResponseEntity<String> addAuditAgencySelection(@RequestBody SelectionData selectionData) {
	    try {
	        // Fetch existing audit entities for the applicant
	        List<ApplicationAuditEntity> applicationAuditEntities = 
	            applicationAuditServ.getByApplicantUserNames(selectionData.getApplicantUserName());

	        if (!applicationAuditEntities.isEmpty()) {
	            deactivateExistingAuditRecords(applicationAuditEntities);
	        }

	    
	        Optional<ApplicationAuditEntity> auditResult = createNewAuditEntity(selectionData);

	        if (auditResult.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                                 .body("Error creating application audit entity.");
	        }

	    
	        Optional<AuditAgencySelectionEntity> selectionResult = createNewAgencySelection(selectionData, auditResult.get());

	        if (selectionResult.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                                 .body("Error saving Audit Agency Selection.");
	        }

	        return ResponseEntity.ok("Audit Agency Selection successfully added.");

	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("An error occurred while processing the request.");
	    }
	}
	 
	private void deactivateExistingAuditRecords(List<ApplicationAuditEntity> applicationAuditEntities) {
	    for (ApplicationAuditEntity auditEntity : applicationAuditEntities) {
	        auditEntity.setStatus("Inactive");
	        auditEntity.setUpdated(new Date());
	        applicationAuditServ.updateData(auditEntity);

	        List<AuditAgencySelectionEntity> agencySelectionEntities = 
	            selectionServ.getAllAuditId(auditEntity.getAppAuditId());

	        for (AuditAgencySelectionEntity selectionEntity : agencySelectionEntities) {
	            selectionEntity.setStatus("Inactive");
	            selectionEntity.setUpdated(new Date());
	            selectionServ.upDateData(selectionEntity);
	        }
	    }
	}

	private Optional<ApplicationAuditEntity> createNewAuditEntity(SelectionData selectionData) {
	    ApplicationAuditEntity applicationAuditEntity = new ApplicationAuditEntity();
	    applicationAuditEntity.setIntentAppId(selectionData.getIntentAppId());
	    applicationAuditEntity.setApplicantUserName(selectionData.getApplicantUserName());
	    applicationAuditEntity.setStatus("Active");
	    return applicationAuditServ.addApplicationAudit(applicationAuditEntity);
	}

	private Optional<AuditAgencySelectionEntity> createNewAgencySelection(
	        SelectionData selectionData, ApplicationAuditEntity auditEntity) {

	    AuditAgencySelectionEntity agencySelectionEntity = new AuditAgencySelectionEntity();
	    agencySelectionEntity.setAuditAgencyId(selectionData.getAuditAgencyId());
	    agencySelectionEntity.setAppAuditId(auditEntity);
	    agencySelectionEntity.setStatus("Active");

	    selectionServ.changedTheApplicantStatus(selectionData.getIntentAppId());

	    return selectionServ.addAuditAgencySelection(agencySelectionEntity);
	}

	 
	  @GetMapping(AuditAgencySelectionAPIs.GET_ALL_AUDIT_SELECTION)
	    public ResponseEntity<?> getAllServices() {
	        List<AuditAgencySelectionEntity> agencySelectionEntities = selectionServ.getAllServiceMaster();
	        return new ResponseEntity<>(agencySelectionEntities, HttpStatus.OK);
	    }

	  @GetMapping(AuditAgencySelectionAPIs.GET_ALL_DATA_BY_USERNAME)
	  public ResponseEntity<?> getAllDetailsByUserName(@PathVariable String userId) {
	      try {
	          // Decrypt the userId (this is the user's encrypted username)
	          String decryptedUserId = EncryptionUtil.decrypt(userId);
	          System.out.println("Looking up audit agency for user: " + decryptedUserId);

	          // Step 1: Call admin-service to get the numeric audit_agency_id
	          String auditAgencyId = null;
	          
	          // Method 1: Try by createdBy (encrypted username)
	          try {
	              String adminServiceUrl = apigatewayServiceUrl + "/admin-service/get-audit-agency-by-username/" + userId;
	              System.out.println("Calling admin-service: " + adminServiceUrl);
	              ResponseEntity<String> adminResponse = restTemplate.getForEntity(adminServiceUrl, String.class);
	              if (adminResponse.getStatusCode() == HttpStatus.OK && adminResponse.getBody() != null 
	                  && !adminResponse.getBody().contains("No audit agency found")) {
	                  auditAgencyId = adminResponse.getBody();
	                  System.out.println("Got audit_agency_id from admin-service: " + auditAgencyId);
	              }
	          } catch (Exception e) {
	              System.out.println("Admin service lookup by username failed: " + e.getMessage());
	          }
	          
	          // Method 2: Get user's first_name from auth-service, then find agency by name
	          if (auditAgencyId == null) {
	              try {
	                  // Get user details from auth-service
	                  String authServiceUrl = apigatewayServiceUrl + "/auth-service/get-all-detail-by-username/" + decryptedUserId;
	                  System.out.println("Calling auth-service: " + authServiceUrl);
	                  ResponseEntity<java.util.Map> authResponse = restTemplate.getForEntity(authServiceUrl, java.util.Map.class);
	                  if (authResponse.getStatusCode() == HttpStatus.OK && authResponse.getBody() != null) {
	                      String firstName = (String) authResponse.getBody().get("firstName");
	                      System.out.println("Got firstName from auth-service: " + firstName);
	                      
	                      if (firstName != null && !firstName.isEmpty()) {
	                          // Now lookup agency by name
	                          String adminByNameUrl = apigatewayServiceUrl + "/admin-service/get-audit-agency-by-name/" + firstName;
	                          System.out.println("Calling admin-service by name: " + adminByNameUrl);
	                          ResponseEntity<String> nameResponse = restTemplate.getForEntity(adminByNameUrl, String.class);
	                          if (nameResponse.getStatusCode() == HttpStatus.OK && nameResponse.getBody() != null
	                              && !nameResponse.getBody().contains("No audit agency found")) {
	                              auditAgencyId = nameResponse.getBody();
	                              System.out.println("Got audit_agency_id by name: " + auditAgencyId);
	                          }
	                      }
	                  }
	              } catch (Exception e) {
	                  System.out.println("Auth service lookup failed: " + e.getMessage());
	              }
	          }

	          // Step 2: Query the selection table
	          List<AuditAgencySelectionEntity> agencySelectionEntities = null;
	          
	          // First, try with the audit_agency_id from admin-service
	          if (auditAgencyId != null && !auditAgencyId.isEmpty()) {
	              agencySelectionEntities = selectionServ.getAllDetailsByAuditAgencyId(auditAgencyId);
	              System.out.println("Querying with audit_agency_id: " + auditAgencyId + ", found: " + 
	                  (agencySelectionEntities != null ? agencySelectionEntities.size() : 0));
	          }
	          
	          // Fallback: try with decrypted userId
	          if (agencySelectionEntities == null || agencySelectionEntities.isEmpty()) {
	              agencySelectionEntities = selectionServ.getAllDetailsByAuditAgencyId(decryptedUserId);
	          }
	          
	          // Fallback: try with encrypted userId
	          if (agencySelectionEntities == null || agencySelectionEntities.isEmpty()) {
	              agencySelectionEntities = selectionServ.getAllDetailsByAuditAgencyId(userId);
	          }

	          // If no entities are found, return a NOT_FOUND response
	          if (agencySelectionEntities == null || agencySelectionEntities.isEmpty()) {
	              return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                      .body("No audit agency selection details found for user: " + userId);
	          }

	          // Convert the list of entities to a list of DTOs
	          List<AuditAgencySelectionDTO> agencySelectionDTOs = new ArrayList<>();
	          for (AuditAgencySelectionEntity entity : agencySelectionEntities) {
	              try {
	                  AuditAgencySelectionDTO dto = new AuditAgencySelectionDTO();
	                  dto.setAgencySelectionId(entity.getAgencySelectionId());
	                  
	                  // Get ApplicationAuditEntity - may be null if the record was deleted
	                  ApplicationAuditEntity applicationAuditEntity = null;
	                  if (entity.getAppAuditId() != null) {
	                      applicationAuditEntity = applicationAuditServ.getAuditDetailsById(entity.getAppAuditId().getAppAuditId());
	                  }
	                  
	                  // Only add DTO if we have valid data
	                  if (applicationAuditEntity != null) {
	                      dto.setAplicantUserName(applicationAuditEntity.getApplicantUserName());
	                  } else {
	                      // Use entity's appAuditId reference if available
	                      dto.setAplicantUserName(entity.getAppAuditId() != null ? 
	                          entity.getAppAuditId().getApplicantUserName() : "Unknown");
	                  }
	                  
	                  dto.setAuditAgencyId(entity.getAuditAgencyId());
	                  dto.setAppAuditId(entity.getAppAuditId() != null 
	                          ? entity.getAppAuditId().getAppAuditId() 
	                          : null);
	                  dto.setReviewedBy(entity.getReviewedBy());
	                  dto.setRemarks(entity.getRemarks());
	                  dto.setCreatedBy(entity.getCreatedBy());
	                  dto.setUpdatedBy(entity.getUpdatedBy());
	                  dto.setStatus(entity.getStatus());
	                  agencySelectionDTOs.add(dto);
	              } catch (Exception ex) {
	                  System.out.println("Error processing selection entity " + entity.getAgencySelectionId() + ": " + ex.getMessage());
	              }
	          }

	          System.out.println("Returning " + agencySelectionDTOs.size() + " DTOs");
	          return ResponseEntity.ok(agencySelectionDTOs);

	      } catch (Exception e) {
	          e.printStackTrace();
	          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                  .body("An error occurred while fetching details: " + e.getMessage());
	      }
	  }

	  
	  
}
