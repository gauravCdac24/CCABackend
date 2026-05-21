package in.lms.cca.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.utils.PdfMerger;

import in.lms.cca.dto.AnnualAuditScheduleDTO;
import in.lms.cca.entity.AnnualAuditScheduleEntity;
import in.lms.cca.entity.ApplicantShortcomingsReportEntity;
import in.lms.cca.entity.ApplicationAuditorsEntity;
import in.lms.cca.entity.AuditAgencySelectionEntity;
import in.lms.cca.entity.AuditNCSEntity;
import in.lms.cca.entity.AuditReportCriteriaEntity;
import in.lms.cca.entity.AuditReportDocumentEntity;
import in.lms.cca.entity.AuditScheduleEntity;
import in.lms.cca.entity.AuditShortComingsEntity;
import in.lms.cca.entity.LicenseeAuditEntity;
import in.lms.cca.payload.AuditAgencyFormDto;
import in.lms.cca.payload.AuditControlPayload;
import in.lms.cca.payload.AuditCriteriaPayload;
import in.lms.cca.payload.AuditParameterPayload;
import in.lms.cca.payload.AuditReportDocumentPayload;
import in.lms.cca.payload.AuditSubCriteriaPayload;
import in.lms.cca.payload.AuditorControlsNC;
import in.lms.cca.payload.NotificationsRequest;
import in.lms.cca.payload.UserLoginDTO;
import in.lms.cca.service.ApplicantShortcomingsReportService;
import in.lms.cca.service.ApplicationAuditService;
import in.lms.cca.service.ApplicationAuditorsService;
import in.lms.cca.service.AuditAgencySelectionService;
import in.lms.cca.service.AuditNCSService;
import in.lms.cca.service.AuditReportCriteriaService;
import in.lms.cca.service.AuditReportDocumentService;
import in.lms.cca.service.AuditScheduleService;
import in.lms.cca.service.AuditShortComingsService;
import in.lms.cca.service.LicenseeAuditservice;
import in.lms.cca.service.Impl.NotificationClientServiceImpl;
import in.lms.cca.util.api.AuditServiceAPIs;
import in.lms.cca.util.global.Constant;
import in.lms.cca.util.global.EncryptionUtil;
import in.lms.cca.util.golbal.DocumentFileUtil;
import jakarta.ws.rs.core.HttpHeaders;

@RestController
@CrossOrigin
@RequestMapping(AuditServiceAPIs.AUDIT_SERVICE_BASE_URL)
public class ApplicationAuditController {

	@Autowired
	private ApplicationAuditService applicationAuditServ;

	@Autowired
	private ApplicationAuditorsService applicationAuditorsService;

	@Autowired
	private AuditScheduleService auditScheduleService;

	@Autowired
	private AuditReportCriteriaService auditReportCriteriaService;

	@Autowired
	private NotificationClientServiceImpl notificationServ;

	@Autowired
	private AuditNCSService auditNCSService;
	
	@Autowired
	private AuditReportDocumentService auditReportDocumentService;

	@Autowired
	private AuditAgencySelectionService selectionServ;
	
	@Autowired
	private AuditShortComingsService auditShortComingsService;
	
	@Autowired
	private ApplicantShortcomingsReportService applicantShortcomingsReportService;
	
	
	@Autowired
	private LicenseeAuditservice licenseeAuditservice;
	
	
	
	
	@PostMapping(value = "/licensee-audit")
	public ResponseEntity<?> submitLicenseeAuditForm(@RequestBody AnnualAuditScheduleDTO annualAuditScheduleDTO ) {

		try {
			
			System.out.println("annualAuditScheduleDTO=-=---0=->"+annualAuditScheduleDTO);
			
			AnnualAuditScheduleEntity newAnnualAuditScheduleEntity = new AnnualAuditScheduleEntity();
			
			String startDateString = annualAuditScheduleDTO.getStartDate(); // e.g. "2024-12-28"

			if (startDateString != null && !startDateString.isEmpty()) {
			    // Add time to match full datetime format
			    startDateString += " 00:00:00";

			    // Define formatter with date and time
			    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			    // Parse the complete string
			    Date startDate = formatter.parse(startDateString);

			    // Debug print in desired format
			    System.out.println("Parsed startDate ---> " + formatter.format(startDate));

			    // Set to entity
			    newAnnualAuditScheduleEntity.setScheduleStartDate(startDate);
			}
		            newAnnualAuditScheduleEntity.setLicenseId(annualAuditScheduleDTO.getLicenseId());
		            newAnnualAuditScheduleEntity.setUserName(annualAuditScheduleDTO.getUserName());
			       newAnnualAuditScheduleEntity.setStatus("Initiated");
			       
			    // Construct the notification
					String title = "Annual Audit Date";
					String message = "Dear Licensee,  Please verify your Date to schedule your audit agency : "+annualAuditScheduleDTO.getStartDate();

					NotificationsRequest notification = new NotificationsRequest();
					notification.setUsername(annualAuditScheduleDTO.getUserName());
					notification.setMessage(message);
					notification.setSubject(title);
					notification.setNotificationType("Email & Dashboard");
					notification.setNotificationDescription("Annual Audit Date");
					notification.setRole("ROLE_LICENSEE");
			       
					  notificationServ.sendNotification(notification);
			
			Optional<AnnualAuditScheduleEntity> c = licenseeAuditservice.addAnnualAudit(newAnnualAuditScheduleEntity);
			System.out.println(c.toString());
			if(c.isEmpty())
				return new ResponseEntity<>("An error occurred while saving the Audit Schedule. Please Try again to save.", HttpStatus.OK);
			return new ResponseEntity<>("Audit Schedule successfully added.", HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while saving the Audit Schedule. Please Try again.", HttpStatus.BAD_REQUEST);
		}
		
		
	}
	
	
	@GetMapping(value="/get-annual-audit-started")
	public ResponseEntity<?> getAnnualAuditStartDateByUsername(@RequestParam("id")String username)
	{
		
		System.out.println(username);
		
		try {
		
		String id = username;
		if(EncryptionUtil.decrypt(id) != null)
			id = EncryptionUtil.decrypt(id);
		System.out.println(id);
		List<AnnualAuditScheduleEntity> li = licenseeAuditservice.getAnnualAuditDetailsByUsername(id);
		
		System.out.println(li.size());
		
		AnnualAuditScheduleEntity liDetails = null;
		
		Date scheduleDate = null;
		
		String isStarted = "No";
		
		if(li.size()>0) {
			liDetails = li.get(0);
			scheduleDate = liDetails.getScheduleStartDate();
			
			
		    Date currentDate = new Date();
		    
		    if(currentDate.compareTo(scheduleDate) >=0)
		    	isStarted = "Yes";
			
				
		}
		
		
		
		return new ResponseEntity<>(EncryptionUtil.encrypt(isStarted), HttpStatus.OK);
		
		}catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.OK);
		}
				

	}
	
	
	
	
	
	

	@PostMapping(value = "/submit")
	public ResponseEntity<?> submitAuditForm(@ModelAttribute AuditAgencyFormDto auditAgencyFormDto) {
	    try {
	        // Generate a random number for file naming
	        Random rand = new Random();
	        Integer rnum = rand.nextInt(1000);

	        System.out.println(auditAgencyFormDto.getApplicantUserName());
	        
	        
	        // Get the application audit entity by applicant username
	        LicenseeAuditEntity applicationAuditEntity = applicationAuditServ
	                .getByApplicantUserName(auditAgencyFormDto.getApplicantUserName());

	        
	        if (applicationAuditEntity == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Application not found for username: " + auditAgencyFormDto.getApplicantUserName());
	        }
	        System.out.println(applicationAuditEntity.getLicenseeAuditId());
	        // Update all auditors' status to "Inactive"
	        List<ApplicationAuditorsEntity> applicationAuditorsEntities = applicationAuditorsService
	                .getAllDataByAuditId(applicationAuditEntity.getLicenseeAuditId());

	        System.out.println(applicationAuditorsEntities.size());
	        
	        for (ApplicationAuditorsEntity auditorsEntity : applicationAuditorsEntities) {
	            auditorsEntity.setStatus("Inactive");
	            auditorsEntity.setUpdated(new Date());
	            applicationAuditorsService.updateData(auditorsEntity);
	            
	            System.out.println(applicationAuditEntity.getLicenseeAuditId());

	            // Set audit schedule status to "Inactive"
	            List<AuditScheduleEntity> auditScheduleEntities = auditScheduleService
	                    .getAllDataByAuditId(applicationAuditEntity.getLicenseeAuditId());

	            for (AuditScheduleEntity scheduleEntity : auditScheduleEntities) {
	                scheduleEntity.setStatus("Inactive");
	                scheduleEntity.setUpdated(new Date());
	                auditScheduleService.updateData(scheduleEntity);
	                
	                System.out.println(applicationAuditEntity.getLicenseeAuditId());
	            }
	        }

	        // Save the auditor description documents
	        for (AuditAgencyFormDto.AuditorDescription auditorDescription : auditAgencyFormDto.getAuditorDesscription()) {
	            String filename = DocumentFileUtil.saveFile(auditorDescription.getFile(), "AuditorDecription", rnum.toString(), "AuditorDecription");

	            // Create a new auditor description entity
	            ApplicationAuditorsEntity auditorEntity = new ApplicationAuditorsEntity();
	            auditorEntity.setFullName(auditorDescription.getAuditorName());
	            auditorEntity.setCertificationType(auditorDescription.getCertificateType());
	            auditorEntity.setCertificateDocument(filename);
	            auditorEntity.setLicenseeAuditId(applicationAuditEntity);

	            // Save the undertaking file
	            String undertakingFilename = DocumentFileUtil.saveFile(auditAgencyFormDto.getUndertakingFile(), "AuditorUndertaking", rnum.toString(), "AuditorUndertaking");
	            auditorEntity.setUndertakingDocument(undertakingFilename);
	            auditorEntity.setStatus("Active");

	            // Save auditor data
	            applicationAuditorsService.processAuditForm(auditorEntity);
	        }

	        // Save audit scope data
	        for (AuditAgencyFormDto.AuditScope auditScope : auditAgencyFormDto.getAuditScope()) {
	            AuditScheduleEntity auditScheduleEntity = new AuditScheduleEntity();
	            auditScheduleEntity.setAuditType(auditScope.getAuditTitle());
	            auditScheduleEntity.setDescription(auditScope.getDescription());
	            auditScheduleEntity.setLicenseeAuditId(applicationAuditEntity);
	            auditScheduleEntity.setStatus("Active");

	            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	            // Parse and set the start date
	            if (auditScope.getStartDate() != null) {
	                Date startDate = dateFormat.parse(auditScope.getStartDate());
	                auditScheduleEntity.setStartDate(startDate);
	            }

	            // Parse and set the end date
	            if (auditScope.getEndDate() != null) {
	                Date endDate = dateFormat.parse(auditScope.getEndDate());
	                auditScheduleEntity.setEndDate(endDate);
	            }

	            // Save the audit schedule data
	            auditScheduleService.saveData(auditScheduleEntity);
	        }

	        // Update application audit status
	       // applicationAuditServ.changeIntentStatusByUserName(auditAgencyFormDto.getApplicantUserName());

	        // Return success response
	        return ResponseEntity.ok("Form submitted successfully");

	    } catch (Exception e) {
	        e.printStackTrace();
	        // Handle any errors that occur during the process
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error processing form: " + e.getMessage());
	    }
	}

	@GetMapping("/getAuditForm")
	public ResponseEntity<AuditAgencyFormDto> getAuditForm(@RequestParam("userName") String applicantUserName) {
	
		LicenseeAuditEntity applicationAuditEntity = applicationAuditServ.getByApplicantUserName(applicantUserName);

	
		AuditAgencyFormDto auditAgencyFormDto = new AuditAgencyFormDto();
	
		auditAgencyFormDto.setApplicantUserName(applicationAuditEntity.getApplicantUserName());
	

		
		List<ApplicationAuditorsEntity> applicationAuditorsEntities = applicationAuditorsService
				.getAllDataByAuditId(applicationAuditEntity.getLicenseeAuditId());
		List<AuditAgencyFormDto.AuditorDescription> auditorDescriptions = applicationAuditorsEntities.stream()
				.map(entity -> {
					AuditAgencyFormDto.AuditorDescription description = new AuditAgencyFormDto.AuditorDescription();
					description.setAuditorDescriptionId(String.valueOf(entity.getAppAuditorId()));
					description.setAuditorName(entity.getFullName());
					description.setCertificateType(entity.getCertificationType());
					description.setCertificateName(entity.getCertificateDocument());
					auditAgencyFormDto.setUndertakingFileName(entity.getUndertakingDocument());
					Date certificateExpiryDate = entity.getCertificateExpiry(); // Get the Date from entity

					if (certificateExpiryDate != null) {
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Adjust format as needed
						String formattedDate = dateFormat.format(certificateExpiryDate);
						description.setCertificateExpiry(formattedDate);
					} else {

						description.setCertificateExpiry(null);
					}

					return description;
				}).toList();
		auditAgencyFormDto.setAuditorDesscription(auditorDescriptions);

		// Retrieve AuditScheduleEntity and map to DTO
		List<AuditScheduleEntity> auditScheduleEntities = auditScheduleService
				.getAllDataByAuditId(applicationAuditEntity.getLicenseeAuditId());
		List<AuditAgencyFormDto.AuditScope> auditScopes = auditScheduleEntities.stream().map(entity -> {
			AuditAgencyFormDto.AuditScope scope = new AuditAgencyFormDto.AuditScope();
			scope.setAuditScopeId(String.valueOf(entity.getAuditScheduleId()));
			scope.setAuditTitle(entity.getAuditType());
			scope.setDescription(entity.getDescription());
			scope.setStartDate(entity.getStartDate().toString()); // Convert to String if needed
			scope.setEndDate(entity.getEndDate().toString()); // Convert to String if needed
			return scope;
		}).toList();
		auditAgencyFormDto.setAuditScope(auditScopes);

		return ResponseEntity.ok(auditAgencyFormDto);
	}

	@GetMapping("/downloadFile")
	public ResponseEntity<Resource> viewFile(@RequestParam("id") String cpsDocuId) {
		System.out.println(cpsDocuId);
		String id = EncryptionUtil.decrypt(cpsDocuId);

		System.out.println(id);
		try {
			Optional<ApplicationAuditorsEntity> cpsDocumentOpt = applicationAuditorsService
					.downloadfile(Long.parseLong(id));

			if (cpsDocumentOpt.isPresent()) {
				ApplicationAuditorsEntity c = cpsDocumentOpt.get();
				System.out.println(c.getCertificateDocument());
				Path filePath = Paths.get(Constant.REAL_PATH + "//AuditorDecription")
						.resolve(c.getCertificateDocument()).normalize();
				System.out.println(filePath);
				Resource resource = new UrlResource(filePath.toUri());
				System.out.println(resource);
				if (resource.exists()) {
					String contentType = Files.probeContentType(filePath);
					System.out.println(contentType);
					return ResponseEntity.ok()
							.contentType(MediaType
									.parseMediaType(contentType != null ? contentType : "application/octet-stream"))
							.header("Content-Disposition", "attachment; filename=\"" + resource.getFilename() + "\"")
							.body(resource);
				} else {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
				}
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PostMapping(value = "/approveSubmit")
	public ResponseEntity<?> approveAuditForm(@ModelAttribute AuditAgencyFormDto auditAgencyFormDto) {
		try {

			for (AuditAgencyFormDto.AuditorDescription auditorDescriptions : auditAgencyFormDto
					.getAuditorDesscription()) {
				
				System.out.println(auditAgencyFormDto.getRemark());
				
				Optional<ApplicationAuditorsEntity> auditorsOptional = applicationAuditorsService
						.downloadfile(Long.valueOf(auditorDescriptions.getAuditorDescriptionId()));
				ApplicationAuditorsEntity auditorDescriptiones = auditorsOptional.get();
				auditorDescriptiones.setRemarks(auditAgencyFormDto.getRemark());

				applicationAuditorsService.aprovedAuditForm(auditorDescriptiones);
			}

			//applicationAuditServ.approvedByUserName(auditAgencyFormDto.getApplicantUserName());

			// Return success response
			return ResponseEntity.ok("Form Aproved successfully");

		} catch (Exception e) {
			// Handle any errors
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error processing form: " + e.getMessage());
		}
	}

	@PostMapping(value = "/rejectSubmit")
	public ResponseEntity<?> rejectAuditForm(@ModelAttribute AuditAgencyFormDto auditAgencyFormDto) {
		try {

			for (AuditAgencyFormDto.AuditorDescription auditorDescriptions : auditAgencyFormDto
					.getAuditorDesscription()) {
				Optional<ApplicationAuditorsEntity> auditorsOptional = applicationAuditorsService
						.downloadfile(Long.valueOf(auditorDescriptions.getAuditorDescriptionId()));
				ApplicationAuditorsEntity auditorDescriptiones = auditorsOptional.get();
				auditorDescriptiones.setRemarks(auditAgencyFormDto.getRemark());
				auditorDescriptiones.setStatus("Inactive");

				applicationAuditorsService.aprovedAuditForm(auditorDescriptiones);
			}

			// Iterate through audit scopes and save them individually
			for (AuditAgencyFormDto.AuditScope auditScope : auditAgencyFormDto.getAuditScope()) {
				Optional<AuditScheduleEntity> auditScheduleOptional = auditScheduleService
						.getAllData(Long.valueOf(auditScope.getAuditScopeId()));
				AuditScheduleEntity auditScheduleEntity = auditScheduleOptional.get();
				auditScheduleEntity.setStatus("Inactive");
				auditScheduleService.aprovedData(auditScheduleEntity);
			}

			// Save the auditor data

			//applicationAuditServ.rejectedByUserName(auditAgencyFormDto.getApplicantUserName());

			// Return success response
			return ResponseEntity.ok("Form Rejected successfully");

		} catch (Exception e) {
			// Handle any errors
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error processing form: " + e.getMessage());
		}
	}

	@PostMapping(value = "/auditor-nc-form")
	public ResponseEntity<?> submitAuditNCForm(@ModelAttribute AuditorControlsNC auditorControlsNC) {
	    Random rand = new Random();
	    Integer rnum = rand.nextInt(1000);

	    try {
	        // Validate mandatory fields
	        if (auditorControlsNC.getApplicantUserName() == null || auditorControlsNC.getFile() == null) {
	            return ResponseEntity.badRequest().body("Applicant User Name and File are required.");
	        }

	        LicenseeAuditEntity applicationAuditEntity = applicationAuditServ
	                .getByApplicantUserName(auditorControlsNC.getApplicantUserName());

	        if (applicationAuditEntity == null) {
	            return ResponseEntity.badRequest().body("Applicant not found.");
	        }
	        String controlId = auditorControlsNC.getControlId();
	        AuditReportCriteriaEntity auditReportCriteriaEntity = auditReportCriteriaService
	                .getAllDataByControlId(controlId, applicationAuditEntity);

	        if (auditReportCriteriaEntity == null) {
	            // Save new audit report criteria if not found
	            return saveNewAuditReportCriteria(auditorControlsNC, applicationAuditEntity, rnum);
	        } else{
	          
	            return updateAuditReportCriteria(auditorControlsNC, auditReportCriteriaEntity, rnum);
	        }
	    } catch (IOException e) {
	        // Log the error
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save file or data.");
	    } catch (Exception e) {
	        // Catch other exceptions and log them
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred.");
	    }
	}

	private ResponseEntity<?> saveNewAuditReportCriteria(AuditorControlsNC auditorControlsNC,
	                                                     LicenseeAuditEntity applicationAuditEntity, Integer rnum) throws IOException {
	    String filename = DocumentFileUtil.saveFile(auditorControlsNC.getFile(), "AuditorNCFile",
	            rnum.toString(), "AuditorNCFile");

	    AuditReportCriteriaEntity newEntity = new AuditReportCriteriaEntity();
	    newEntity.setDocument(filename);
	    newEntity.setLicenseeAuditId(applicationAuditEntity);
	    newEntity.setAuditControlId(auditorControlsNC.getControlId());
	    newEntity.setCompliance(auditorControlsNC.getCompliance());
	    newEntity.setStatus("Active");

	    Optional<AuditReportCriteriaEntity> savedEntity = auditReportCriteriaService.saveData(newEntity);

	    if (!savedEntity.isPresent()) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Failed to save audit report criteria.");
	    }

	    return ResponseEntity.ok("Data submitted successfully.");
	}

	private ResponseEntity<?> updateAuditReportCriteria(AuditorControlsNC auditorControlsNC,
	                                                    AuditReportCriteriaEntity existingEntity, Integer rnum) throws IOException {
	    // Delete the old file
	    DocumentFileUtil.deleteFile(auditorControlsNC.getSelectedMultipartFile(), "AuditorNCFile");

	    String newFilename = DocumentFileUtil.saveFile(auditorControlsNC.getFile(), "AuditorNCFile",
	            rnum.toString(), "AuditorNCFile");

	    existingEntity.setDocument(newFilename);
	    existingEntity.setAuditControlId(auditorControlsNC.getControlId());
	    existingEntity.setCompliance(auditorControlsNC.getCompliance());
	    existingEntity.setStatus("Active");
	    existingEntity.setUpdated(new Date());

	    Optional<AuditReportCriteriaEntity> updatedEntity = auditReportCriteriaService.updateData(existingEntity);

	    if (!updatedEntity.isPresent()) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Failed to update audit report criteria.");
	    }

	    return ResponseEntity.ok("Data updated successfully.");
	}

	@GetMapping("/criteriaId")
	public ResponseEntity<?> getByCriteriaId(@RequestParam("id") Long criteriaId) {
		try {
			// Fetch the entity based on the criteriaId
			Optional<AuditReportCriteriaEntity> entity = auditReportCriteriaService.findById(criteriaId);

			// Check if the entity exists
			if (entity.isPresent()) {
				return ResponseEntity.ok(entity.get());
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Criteria not found with ID: " + criteriaId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while retrieving the data.");
		}
	}

	@GetMapping("/get-all-criteria")
	public ResponseEntity<?> getAllCriteria() {
		try {
			// Fetch all criteria
			List<AuditReportCriteriaEntity> entities = auditReportCriteriaService.getAllData();

			// Check if the list is empty
			if (entities.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No criteria found.");
			} else {
				return ResponseEntity.ok(entities);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while retrieving the data.");
		}
	}

	@GetMapping("/document-name")
	public ResponseEntity<?> getByDocumentName(@RequestParam("documentName") String documentName) {
		try {
			Optional<AuditReportCriteriaEntity> cpsDocumentOpt = auditReportCriteriaService
					.downloadfileBydDocumentName(EncryptionUtil.decrypt(documentName));

			if (cpsDocumentOpt.isPresent()) {
				AuditReportCriteriaEntity c = cpsDocumentOpt.get();
				System.out.println(c.getDocument());
				Path filePath = Paths.get(Constant.REAL_PATH + "//AuditorNCFile").resolve(c.getDocument()).normalize();
				System.out.println(filePath);
				Resource resource = new UrlResource(filePath.toUri());
				System.out.println(resource);
				if (resource.exists()) {
					String contentType = Files.probeContentType(filePath);
					System.out.println(contentType);
					return ResponseEntity.ok()
							.contentType(MediaType
									.parseMediaType(contentType != null ? contentType : "application/octet-stream"))
							.header("Content-Disposition", "attachment; filename=\"" + resource.getFilename() + "\"")
							.body(resource);
				} else {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
				}
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PostMapping("/remarks-applicant-UserName")
	public ResponseEntity<?> getByApplicantUserName(@RequestParam("applicantUserName") String applicantUserName,
			@RequestParam("remarks") String remarks) {

		try {
			// Fetch the application audit entity
			LicenseeAuditEntity applicationAuditEntity = applicationAuditServ
					.getByApplicantUserName(applicantUserName);
			if (applicationAuditEntity == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Application audit entity not found for username: " + applicantUserName);
			}

			// Update remarks and timestamp
			applicationAuditEntity.setApplicantRemarks(remarks);
			applicationAuditEntity.setUpdated(new Date());
			applicationAuditServ.updateAuditor(applicationAuditEntity);

			// Retrieve audit report criteria
			List<AuditReportCriteriaEntity> auditReportCriteriaEntities = auditReportCriteriaService
					.findByAuditId(applicationAuditEntity.getLicenseeAuditId());

			// Send notification to the applicant
			auditReportCriteriaService.sendTheApplicantUserName(applicantUserName);

			// Construct the notification
			String title = "NC Raised By Auditor";
			String message = "Dear Applicant, Some NC Generated Regarding Your Application. Please Check and Take Action: "
					+ remarks;

			NotificationsRequest notification = new NotificationsRequest();
			notification.setUsername(applicantUserName);
			notification.setMessage(message);
			notification.setSubject(title);
			notification.setNotificationType("Email & Dashboard");
			notification.setNotificationDescription("NC Generated By Auditor");
			notification.setRole("ROLE_APPLICANT");

			notificationServ.sendNotification(notification);

			return ResponseEntity.ok("Remarks updated and notification sent successfully.");

		} catch (Exception ex) {
			// Log and handle the error
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while processing the request: " + ex.getMessage());
		}
	}

	@GetMapping("/get-all-remarks")
	public ResponseEntity<?> getAllByApplicantUserName(@RequestParam("applicantUserName") String applicantUserName) {

		try {
			// Fetch the application audit entity
			LicenseeAuditEntity applicationAuditEntity = applicationAuditServ
					.getByApplicantUserName(applicantUserName);
			if (applicationAuditEntity == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Application audit entity not found for username: " + applicantUserName);
			}

			// Return the remarks list
			return ResponseEntity.ok(applicationAuditEntity);

		} catch (Exception ex) {
			// Log and return an error response
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while fetching remarks: " + ex.getMessage());
		}
	}

	@PostMapping("/applicant-remarks-by-applicant-UserName")
	public ResponseEntity<?> getApplicantRemarksByApplicantUserName(
			@RequestParam("applicantUserName") String applicantUserName, @RequestParam("remarks") String remarks,
			@RequestParam("file") MultipartFile file) {

		try {

			Random rand = new Random();
			Integer rnum = rand.nextInt(1000);
			// Fetch the application audit entity
			LicenseeAuditEntity applicationAuditEntity = applicationAuditServ
					.getByApplicantUserName(applicantUserName);
			if (applicationAuditEntity == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Application audit entity not found for username: " + applicantUserName);
			}
			AuditNCSEntity auditNCSEntity = new AuditNCSEntity();
			String filename = DocumentFileUtil.saveFile(file, "ApplicantNCActionFile", rnum.toString(),
					"ApplicantNCActionFile");
			auditNCSEntity.setNcs(filename);
			auditNCSEntity.setRemarks(remarks);
			auditNCSEntity.setStatus("Active");
			auditNCSEntity.setLicenseeAuditId(applicationAuditEntity);

			auditNCSService.saveData(auditNCSEntity);

			AuditAgencySelectionEntity agencySelectionEntity = selectionServ.findbyAuditId(applicationAuditEntity);

//System.out.println(EncryptionUtil.encrypt(agencySelectionEntity.getAuditAgencyId()));
			UserLoginDTO userLoginDTO = auditReportCriteriaService
					.findbyAgencyId(agencySelectionEntity.getAuditAgencyId());

			// Construct the notification
			String title = "NC Action Taken By Applicant";
			String message = "Dear Sir, We have Attached the Document and write Our remarks in Our Application. Please Check and Review it: "
					+ remarks;

			NotificationsRequest notification = new NotificationsRequest();
			notification.setUsername(userLoginDTO.getUserName());
			notification.setMessage(message);
			notification.setSubject(title);
			notification.setNotificationType("Email & Dashboard");
			notification.setNotificationDescription("NC Action Taken By Applicant");
			notification.setRole("ROLE_AUDIT_AGENCY");

			notificationServ.sendNotification(notification);
			// Send notification to the applicant
			auditReportCriteriaService.changedTheApplicantUserName(applicantUserName);

			return ResponseEntity.ok("Remarks updated and notification sent successfully.");

		} catch (Exception ex) {
			// Log and handle the error
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while processing the request: " + ex.getMessage());
		}
	}

	@GetMapping("/get-All-AuditNC-details")
	public ResponseEntity<?> getByApplicantUserName(@RequestParam("applicantUserName") String applicantUserName) {

		
		System.out.println("applicantUserName====>"+applicantUserName);
		try {
			// Fetch the application audit entity
			LicenseeAuditEntity applicationAuditEntity = applicationAuditServ
					.getByApplicantUserName(applicantUserName);

			AuditNCSEntity auditNCSEntity = auditNCSService.findByAuditId(applicationAuditEntity.getLicenseeAuditId());
			if (applicationAuditEntity == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Application audit entity not found for username: " + applicantUserName);
			}

			// Return the remarks list
			return ResponseEntity.ok(auditNCSEntity);

		} catch (Exception ex) {
			// Log and return an error response
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while fetching remarks: " + ex.getMessage());
		}
	}

	@GetMapping("/applicant-document-name")
	public ResponseEntity<?> getByApplicantDocumentName(@RequestParam("documentName") String documentName) {
		try {

			System.out.println("=-=-=-=-=-=->" + EncryptionUtil.decrypt(documentName));

			Optional<AuditNCSEntity> cpsDocumentOpt = auditNCSService
					.downloadfileBydDocumentName(EncryptionUtil.decrypt(documentName));

			if (cpsDocumentOpt.isPresent()) {
				AuditNCSEntity c = cpsDocumentOpt.get();
				System.out.println(c.getNcs());
				Path filePath = Paths.get(Constant.REAL_PATH + "//ApplicantNCActionFile").resolve(c.getNcs())
						.normalize();
				System.out.println(filePath);
				Resource resource = new UrlResource(filePath.toUri());
				System.out.println(resource);
				if (resource.exists()) {
					String contentType = Files.probeContentType(filePath);
					System.out.println(contentType);
					return ResponseEntity.ok()
							.contentType(MediaType
									.parseMediaType(contentType != null ? contentType : "application/octet-stream"))
							.header("Content-Disposition", "attachment; filename=\"" + resource.getFilename() + "\"")
							.body(resource);
				} else {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
				}
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PostMapping("/auditor-remarks-by-applicant-UserName")
	public ResponseEntity<?> getAuditorRemarksByApplicantUserName(
	        @RequestParam("applicantUserName") String applicantUserName, 
	        @RequestParam("userName") String userName, 
	        @RequestParam("remarks") String remarks,
	        @RequestParam("file") MultipartFile file) {

	    try {
	        Random rand = new Random();
	        Integer rnum = rand.nextInt(1000);

	        // Fetch the application audit entity
	        LicenseeAuditEntity applicationAuditEntity = applicationAuditServ
	                .getByApplicantUserName(applicantUserName);
	        if (applicationAuditEntity == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body("Application audit entity not found for username: " + applicantUserName);
	        }

	        // Check if document entity exists
	        AuditReportDocumentEntity documentEntity = auditReportDocumentService.getByAuditId(applicationAuditEntity);

	        if (documentEntity != null) {
	            // Set status to Inactive for the existing record
	            documentEntity.setStatus("Inactive");
	            documentEntity.setUpdated(new Date());
	            auditReportDocumentService.updateData(documentEntity);
	        }

	        // Create a new document entity
	        AuditReportDocumentEntity auditReportDocumentEntity = new AuditReportDocumentEntity();
	        String filename = DocumentFileUtil.saveFile(file, "AuditorNCReport", rnum.toString(), "AuditorNCReport");
	        auditReportDocumentEntity.setDocument(filename);
	        auditReportDocumentEntity.setRemarks(remarks);
	        auditReportDocumentEntity.setStatus("Active");
	        auditReportDocumentEntity.setLicenseeAuditId(applicationAuditEntity);
	        auditReportDocumentService.saveData(auditReportDocumentEntity);

	        // Handle agency selection and notification
	        AuditAgencySelectionEntity agencySelectionEntity = selectionServ.findbyAuditId(applicationAuditEntity);
	        UserLoginDTO userLoginDTO = auditReportCriteriaService.findbyAgencyId(agencySelectionEntity.getAuditAgencyId());

	        String title = "Review NC Report";
	        String message = "Dear Sir, We have Attached the Document and write Our remarks in Our Application. Please Check and Review it: " + remarks;

	        NotificationsRequest notification = new NotificationsRequest();
	        notification.setUsername(userName);
	        notification.setMessage(message);
	        notification.setSubject(title);
	        notification.setNotificationType("Email & Dashboard");
	        notification.setNotificationDescription("Review NC Report");
	        notification.setRole("ROLE_APPLICATION_REVIEW_COMMITTEE");

	        notificationServ.sendNotification(notification);
	        auditReportCriteriaService.changedTheStatus(applicantUserName);

	        return ResponseEntity.ok("Remarks updated and notification sent successfully.");

	    } catch (Exception ex) {
	        ex.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("An error occurred while processing the request: " + ex.getMessage());
	    }
	}

	
	@GetMapping("/get-all-data")
	public ResponseEntity<?> getAllData() {
	    try {
	       
	        List<LicenseeAuditEntity> applicationAuditEntities = applicationAuditServ.getAll();

	      
	        List<AuditReportDocumentPayload> allPayloads = new ArrayList<>();

	        for (LicenseeAuditEntity auditEntity : applicationAuditEntities) {
	        
	            List<AuditReportDocumentEntity> auditReportDocumentEntities =
	                    auditReportDocumentService.findByAuditId(auditEntity.getLicenseeAuditId());

	            if (auditReportDocumentEntities != null && !auditReportDocumentEntities.isEmpty()) {
	              
	                for (AuditReportDocumentEntity reportDocumentEntity : auditReportDocumentEntities) {
	                    AuditReportDocumentPayload payload = new AuditReportDocumentPayload();

	                  
	                    payload.setCriteriaDocId(reportDocumentEntity.getCriteriaDocId());
	                    payload.setAppAuditId(auditEntity.getLicenseeAuditId());
	                    payload.setDocument(reportDocumentEntity.getDocument());
	                    payload.setCreatedBy(reportDocumentEntity.getCreatedBy());
	                    payload.setUpdatedBy(reportDocumentEntity.getUpdatedBy());
	                    payload.setRemarks(reportDocumentEntity.getRemarks());
	                    payload.setStatus(reportDocumentEntity.getStatus());
	                   // payload.setIntentId(auditEntity.getLicenseeAuditId());
	                    payload.setApplicantUserName(auditEntity.getApplicantUserName());
	                   
	                    allPayloads.add(payload);
	                }
	            }
	        }

	       
	        return ResponseEntity.ok(allPayloads);

	    } catch (Exception ex) {
	        ex.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("An error occurred while fetching the data: " + ex.getMessage());
	    }
	}
	
	
	@GetMapping("/get-All-Audit-details")
	public ResponseEntity<?> getByAuditNCReportApplicantUserName(@RequestParam("applicantUserName") String applicantUserName) {

		
		System.out.println("applicantUserName====>"+applicantUserName);
		try {
			// Fetch the application audit entity
			LicenseeAuditEntity applicationAuditEntity = applicationAuditServ
					.getByApplicantUserName(applicantUserName);

			AuditReportDocumentEntity auditNCSEntity = auditReportDocumentService.findByAuditsId(applicationAuditEntity.getLicenseeAuditId());
			if (applicationAuditEntity == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Application audit entity not found for username: " + applicantUserName);
			}

			// Return the remarks list
			return ResponseEntity.ok(auditNCSEntity);

		} catch (Exception ex) {
			// Log and return an error response
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while fetching remarks: " + ex.getMessage());
		}
	}
	
	
	@PostMapping("/review-by-committee")
	public ResponseEntity<?> getapplicationRejection(
	        @RequestParam("applicantUserName") String applicantUserName, 
	        @RequestParam("remarks") String remarks,
	        @RequestParam("isreject") boolean isreject) {

	    try {
	        Random rand = new Random();
	        Integer rnum = rand.nextInt(1000);

	        // Fetch the application audit entity
	        LicenseeAuditEntity applicationAuditEntity = applicationAuditServ
	                .getByApplicantUserName(applicantUserName);
	        if (applicationAuditEntity == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body("Application audit entity not found for username: " + applicantUserName);
	        }

	        // Check if document entity exists
	        AuditShortComingsEntity documentEntity = auditShortComingsService.getByAuditId(applicationAuditEntity);

	        if (documentEntity != null) {
	            // Set status to Inactive for the existing record
	            documentEntity.setStatus("Inactive");
	            documentEntity.setUpdated(new Date());
	            auditShortComingsService.updateData(documentEntity);
	        }

	        // Create a new document entity
	        AuditShortComingsEntity auditShortComingsEntity = new AuditShortComingsEntity();
	        auditShortComingsEntity.setRemarks(remarks);
	        auditShortComingsEntity.setStatus("Active");
	        auditShortComingsEntity.setLicenseeAuditId(applicationAuditEntity);
	        auditShortComingsService.saveData(auditShortComingsEntity);

	        // Handle agency selection and notification
	        AuditAgencySelectionEntity agencySelectionEntity = selectionServ.findbyAuditId(applicationAuditEntity);
	        UserLoginDTO userLoginDTO = auditReportCriteriaService.findbyAgencyId(agencySelectionEntity.getAuditAgencyId());

	        String title = "NC Report Rejection";
	        String message = "Dear Applicant, your application has been rejected. Please check your email for more details and review it: " + remarks;

	        NotificationsRequest notification = new NotificationsRequest();
	        notification.setUsername(userLoginDTO.getUserName());
	        notification.setMessage(message);
	        notification.setSubject(title);
	        notification.setNotificationType("Email & Dashboard");
	        notification.setNotificationDescription("NC Report Rejection");
	        notification.setRole("ROLE_APPLICANT");

	        notificationServ.sendNotification(notification);
	        
	        if(isreject)
	        {
	        auditReportCriteriaService.changedTheAproveOfRejection(applicantUserName);
	        }else {
	        	  auditReportCriteriaService.changedTheStatus(applicantUserName);
	        }
	        return ResponseEntity.ok("Remarks updated and notification sent successfully.");

	    } catch (Exception ex) {
	        ex.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("An error occurred while processing the request: " + ex.getMessage());
	    }
	}


	@PostMapping("/review-by-committees")
	public ResponseEntity<?> getapplicationApprove(
	        @RequestParam("applicantUserName") String applicantUserName, 
	        @RequestParam("remarks") String remarks) {

	    try {
	        Random rand = new Random();
	        Integer rnum = rand.nextInt(1000);

	        // Fetch the application audit entity
	        LicenseeAuditEntity applicationAuditEntity = applicationAuditServ
	                .getByApplicantUserName(applicantUserName);
	        if (applicationAuditEntity == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body("Application audit entity not found for username: " + applicantUserName);
	        }

	        // Check if document entity exists
	        AuditShortComingsEntity documentEntity = auditShortComingsService.getByAuditId(applicationAuditEntity);

	        if (documentEntity != null) {
	            // Set status to Inactive for the existing record
	            documentEntity.setStatus("Inactive");
	            documentEntity.setUpdated(new Date());
	            auditShortComingsService.updateData(documentEntity);
	        }

	        // Create a new document entity
	        AuditShortComingsEntity auditShortComingsEntity = new AuditShortComingsEntity();
	        auditShortComingsEntity.setRemarks(remarks);
	        auditShortComingsEntity.setStatus("Active");
	        auditShortComingsEntity.setLicenseeAuditId(applicationAuditEntity);
	        auditShortComingsService.saveData(auditShortComingsEntity);

	        // Handle agency selection and notification
	        AuditAgencySelectionEntity agencySelectionEntity = selectionServ.findbyAuditId(applicationAuditEntity);
	        UserLoginDTO userLoginDTO = auditReportCriteriaService.findbyAgencyId(agencySelectionEntity.getAuditAgencyId());

	        String title = "NC Report Approve";
	        String message = "Dear Applicant, your application has been approved. Please check your email for more details and review it: " + remarks;

	        NotificationsRequest notification = new NotificationsRequest();
	        notification.setUsername(userLoginDTO.getUserName());
	        notification.setMessage(message);
	        notification.setSubject(title);
	        notification.setNotificationType("Email & Dashboard");
	        notification.setNotificationDescription("NC Report Approve");
	        notification.setRole("ROLE_APPLICANT");

	        notificationServ.sendNotification(notification);
	        auditReportCriteriaService.changedTheStatusApprove(applicantUserName);

	        return ResponseEntity.ok("Remarks updated and notification sent successfully.");

	    } catch (Exception ex) {
	        ex.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("An error occurred while processing the request: " + ex.getMessage());
	    }
	}

	
	@GetMapping("/get-All-ShortComming")
	public ResponseEntity<?> getByAllShortCommingApplicantUserName(@RequestParam("applicantUserName") String applicantUserName) {

		
		System.out.println("applicantUserName====>"+applicantUserName);
		try {
			// Fetch the application audit entity
			LicenseeAuditEntity applicationAuditEntity = applicationAuditServ
					.getByApplicantUserName(applicantUserName);

			List<AuditShortComingsEntity> auditShortComingsEntity = auditShortComingsService.findByAuditsId(applicationAuditEntity.getLicenseeAuditId());
			if (applicationAuditEntity == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Application audit entity not found for username: " + applicantUserName);
			}

			// Return the remarks list
			return ResponseEntity.ok(auditShortComingsEntity);

		} catch (Exception ex) {
			// Log and return an error response
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while fetching remarks: " + ex.getMessage());
		}
	}
	@PostMapping("/reviewer-remarks-by-applicant")
	public ResponseEntity<?> getApplicantRemarksToReviewer(
	        @RequestParam("applicantUserName") String applicantUserName, 
	        @RequestParam("remarks") String remarks,
	        @RequestParam("file") MultipartFile file) {

	    try {
	        Random rand = new Random();
	        Integer rnum = rand.nextInt(1000);

	        // Fetch the application audit entity
	        LicenseeAuditEntity applicationAuditEntity = applicationAuditServ
	                .getByApplicantUserName(applicantUserName);
	        if (applicationAuditEntity == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body("Application audit entity not found for username: " + applicantUserName);
	        }

	        AuditShortComingsEntity documentEntity = auditShortComingsService.getByAuditId(applicationAuditEntity);
	        
	        // Check if document entity exists
	        ApplicantShortcomingsReportEntity documentsEntity = applicantShortcomingsReportService.getByShortIdId(documentEntity.getShortcomingId());

	        if (documentsEntity != null) {
	            // Set status to Inactive for the existing record
	        	documentsEntity.setStatus("Inactive");
	        	documentsEntity.setUpdated(new Date());
	        	applicantShortcomingsReportService.updateData(documentsEntity);
	        }

	        // Create a new document entity
	        ApplicantShortcomingsReportEntity applicantShortcomingsReportEntity = new ApplicantShortcomingsReportEntity();
	        String filename = DocumentFileUtil.saveFile(file, "ShortCommingReport", rnum.toString(), "ShortCommingReport");
	        applicantShortcomingsReportEntity.setShortcomingReport(filename);
	        applicantShortcomingsReportEntity.setRemarks(remarks);
	        applicantShortcomingsReportEntity.setStatus("Active");
	        applicantShortcomingsReportEntity.setShortcomingId(documentEntity);
	        applicantShortcomingsReportService.saveData(applicantShortcomingsReportEntity);

	        // Handle agency selection and notification
	        AuditAgencySelectionEntity agencySelectionEntity = selectionServ.findbyAuditId(applicationAuditEntity);
	        UserLoginDTO userLoginDTO = auditReportCriteriaService.findbyAgencyId(agencySelectionEntity.getAuditAgencyId());

	        String title = "Review NC Report";
	        String message = "Dear Sir, We have Attached the Document and write Our remarks in Our Application. Please Check and Review it: " + remarks;

	        NotificationsRequest notification = new NotificationsRequest();
	        notification.setUsername(userLoginDTO.getUserName());
	        notification.setMessage(message);
	        notification.setSubject(title);
	        notification.setNotificationType("Email & Dashboard");
	        notification.setNotificationDescription("Review NC Report");
	        notification.setRole("ROLE_APPLICATION_REVIEW_COMMITTEE");

	        notificationServ.sendNotification(notification);
	        auditReportCriteriaService.changedTheStatus(applicantUserName);

	        return ResponseEntity.ok("Remarks updated and notification sent successfully.");

	    } catch (Exception ex) {
	        ex.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("An error occurred while processing the request: " + ex.getMessage());
	    }
	}

	@GetMapping("/get-All-ShortCommingReport")
	public ResponseEntity<?> getByAllShortCommingReportApplicantUserName(@RequestParam("applicantUserName") String applicantUserName) {

		
		System.out.println("applicantUserName====>"+applicantUserName);
		try {
			// Fetch the application audit entity
			LicenseeAuditEntity applicationAuditEntity = applicationAuditServ
					.getByApplicantUserName(applicantUserName);

			List<AuditShortComingsEntity> auditShortComingsEntity = auditShortComingsService.findByAuditsId(applicationAuditEntity.getLicenseeAuditId());
			if (applicationAuditEntity == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Application audit entity not found for username: " + applicantUserName);
			}

			// Return the remarks list
			return ResponseEntity.ok(auditShortComingsEntity);

		} catch (Exception ex) {
			// Log and return an error response
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while fetching remarks: " + ex.getMessage());
		}
	}
	
	
	@PostMapping("/review-by-cca")
	public ResponseEntity<?> getapplicationRejection(
	        @RequestParam("applicantUserName") String applicantUserName) {

	    try {
	        Random rand = new Random();
	        Integer rnum = rand.nextInt(1000);

	        // Fetch the application audit entity
	        LicenseeAuditEntity applicationAuditEntity = applicationAuditServ
	                .getByApplicantUserName(applicantUserName);
	        if (applicationAuditEntity == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body("Application audit entity not found for username: " + applicantUserName);
	        }

	        // Check if document entity exists
	        AuditShortComingsEntity documentEntity = auditShortComingsService.getByAuditId(applicationAuditEntity);

	        if (documentEntity != null) {
	            // Set status to Inactive for the existing record
	            documentEntity.setStatus("Inactive");
	            documentEntity.setUpdated(new Date());
	            auditShortComingsService.updateData(documentEntity);
	        }

	    

	        // Handle agency selection and notification
	        AuditAgencySelectionEntity agencySelectionEntity = selectionServ.findbyAuditId(applicationAuditEntity);
	        UserLoginDTO userLoginDTO = auditReportCriteriaService.findbyAgencyId(agencySelectionEntity.getAuditAgencyId());

	        String title = "Application Rejected";
	        String message = "Dear Applicant, your application has been rejected. Please check your email for more details and review it: " ;

	        NotificationsRequest notification = new NotificationsRequest();
	        notification.setUsername(applicantUserName);
	        notification.setMessage(message);
	        notification.setSubject(title);
	        notification.setNotificationType("Email & Dashboard");
	        notification.setNotificationDescription("NC Report Approve");
	        notification.setRole("ROLE_APPLICANT");

	        notificationServ.sendNotification(notification);
	        auditReportCriteriaService.changedTheApplicantRejection(applicantUserName);

	        return ResponseEntity.ok("Remarks updated and notification sent successfully.");

	    } catch (Exception ex) {
	        ex.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("An error occurred while processing the request: " + ex.getMessage());
	    }
	}

	
	@GetMapping("/get-all-active-auditors")
	public ResponseEntity<?> getAllActiveAuditors() {
	    try {
	        List<ApplicationAuditorsEntity> activeAuditors = applicationAuditorsService.getAllActiveActiveAuditor();

	        if (activeAuditors == null || activeAuditors.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No active auditors found.");
	        }

	        return ResponseEntity.ok(activeAuditors);
	    } catch (Exception e) {
	        // Log the error for debugging
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("An error occurred while retrieving active auditors: " + e.getMessage());
	    }
	}

	@GetMapping("/download-auditor-report")
	public ResponseEntity<?> downloadAuditorReport() {
	    try {
	        // Fetch the data for generating the report
	        List<AuditCriteriaPayload> criteriaPayload = applicationAuditorsService.getAllData();
	        List<AuditReportCriteriaEntity> auditReportCriteriaEntities = auditReportCriteriaService.getAllData();

	        System.out.println("abdg-=-=->" + criteriaPayload.size());

	        // Generate HTML content from the data
	        String htmlContent = generateHtmlContent(criteriaPayload, auditReportCriteriaEntities);

	        byte[] pdfBytes;

	        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
	            // Convert the HTML content to a PDF
	            ConverterProperties converterProperties = new ConverterProperties();
	            HtmlConverter.convertToPdf(htmlContent, outputStream, converterProperties);
	            pdfBytes = outputStream.toByteArray();
	        }

	        // Create a ByteArrayOutputStream to collect the merged PDF content
	        ByteArrayOutputStream mergedPdfOutputStream = new ByteArrayOutputStream();

	        // Check if auditReportCriteriaEntities is not empty or null before merging PDFs
	        if (auditReportCriteriaEntities != null && !auditReportCriteriaEntities.isEmpty()) {
	            // Loop through each audit report criteria entity and merge PDFs
	            for (AuditReportCriteriaEntity matchingEntity : auditReportCriteriaEntities) {
	                try {
	                    String documentPath = matchingEntity.getDocument();
	                    System.out.println("matchingEntity-=---0-0->" + documentPath);

	                    // Check if the document exists
	                    if (documentPath != null && !documentPath.isEmpty()) {
	                        // If the document exists, merge the PDF
	                        byte[] mergedBytes = mergeSinglePdf(pdfBytes, matchingEntity.getAuditControlId(), documentPath, Constant.REAL_PATH + "//AuditorNCFile");
	                        mergedPdfOutputStream.write(mergedBytes);
	                    } else {
	                        // If no document found, log a message and skip merging for this entity
	                        System.out.println("No document found for control ID: " + matchingEntity.getAuditControlId() + ". Skipping...");
	                    }

	                } catch (Exception e) {
	                    e.printStackTrace();
	                    throw new RuntimeException("Error occurred while merging PDFs for control ID: " + matchingEntity.getAuditControlId());
	                }
	            }
	        } else {
	            System.out.println("auditReportCriteriaEntities is empty or null, skipping PDF merge.");
	        }

	        // After all PDFs are merged, add page numbers (if needed)

	        // Return the final PDF file as a response to the client
	        return ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=AuditorReport.pdf")
	                .contentType(MediaType.APPLICATION_PDF)
	                .body(mergedPdfOutputStream.toByteArray());

	    } catch (Exception e) {
	        // Handle any errors that occur during the report generation process
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("An error occurred while generating the auditor report: " + e.getMessage());
	    }
	}


	private String generateHtmlContent(
	        List<AuditCriteriaPayload> criteriaPayload, 
	        List<AuditReportCriteriaEntity> auditReportCriteriaEntities) {

	    StringBuilder contentBuilder = new StringBuilder();

	    contentBuilder.append("<html><head><style>")
	            .append("@page { margin: 50pt; }")
	            .append("body { font-size: 12px; font-family: 'Times New Roman', serif; }")
	            .append("table { border-collapse: collapse; width: 100%; }")
	            .append("th, td { border: 1px solid black; padding: 5px; }")
	            .append("th { background-color: #d9534f; color: white; }")
	            .append("</style></head><body>");

	    contentBuilder.append("<p><b>3. Detailed Audit Controls</b></p>");

	    int subCriteriaIndex = 1;
	    for (AuditCriteriaPayload criteria : criteriaPayload) {
	        contentBuilder.append("<p><b>3.").append(subCriteriaIndex).append(". ")
	                .append(criteria.getAuditCriteria()).append("</b></p>");

	        int controlIndex = 1;
	        for (AuditSubCriteriaPayload subCriteria : criteria.getAuditSubCriteria()) {
	            contentBuilder.append("<p><b>3.").append(subCriteriaIndex).append(".")
	                    .append(controlIndex).append(". ").append(subCriteria.getAuditSubCriteria())
	                    .append("</b></p>");

	            contentBuilder.append("<table><tr>")
	                    .append("<th>Control No.</th><th>Control</th><th>Audit Checks</th>")
	                    .append("<th>Control Type</th><th>References</th>")
	                    .append("<th>Compliance</th><th>Document</th><th>Remarks</th></tr>");

	            int auditControlIndex = 1;
	            for (AuditParameterPayload parameter : subCriteria.getAuditParameterPayload()) {
	                contentBuilder.append("<tr><td colspan='6' style='font-weight: bold;'>")
	                        .append(parameter.getAuditParameter()).append("</td></tr>");

	                for (AuditControlPayload control : parameter.getAuditControlPayload()) {
	                    contentBuilder.append("<tr><td>")
	                            .append("3.").append(subCriteriaIndex).append(".")
	                            .append(controlIndex).append(".").append(auditControlIndex)
	                            .append("</td>")
	                            .append("<td>").append(control.getAuditControl()).append("</td>")
	                            .append("<td>").append(control.getAuditCheck()).append("</td>")
	                            .append("<td>").append(control.getControlType()).append("</td>")
	                            .append("<td>").append(control.getReferences()).append("</td>")
	                            .append("<td>");

	                    String controlId = "3." + subCriteriaIndex + "." + controlIndex + "." + auditControlIndex;

	                    // Add data from auditReportCriteriaEntities only if it's not empty
	                    if (auditReportCriteriaEntities != null && !auditReportCriteriaEntities.isEmpty()) {
	                        AuditReportCriteriaEntity matchingEntity = auditReportCriteriaEntities.stream()
	                                .filter(entity -> controlId.equals(entity.getAuditControlId()))
	                                .findFirst().orElse(null);

	                        if (matchingEntity != null) {
	                            contentBuilder.append(matchingEntity.getCompliance());
	                        }
	                    }

	                    contentBuilder.append("</td>");
	                    contentBuilder.append("<td>");
	                    if (auditReportCriteriaEntities != null && !auditReportCriteriaEntities.isEmpty()) {
	                        AuditReportCriteriaEntity matchingEntity = auditReportCriteriaEntities.stream()
	                                .filter(entity -> controlId.equals(entity.getAuditControlId()))
	                                .findFirst().orElse(null);

	                        if (matchingEntity != null) {
	                            contentBuilder.append(matchingEntity.getAuditControlId());
	                        }else
	                        {
	                        	contentBuilder.append("<td></td>");
	                        }
	                    }
	                    contentBuilder.append("</td>");
	                    contentBuilder.append("<td>");

	                    // You can add additional remarks or content here if required.
	                    contentBuilder.append("</td>");
	                    contentBuilder.append("</tr>");
	                    auditControlIndex++;
	                }
	            }

	            contentBuilder.append("</table><p></p>");
	            controlIndex++;
	        }
	        subCriteriaIndex++;
	    }

	    contentBuilder.append("</body></html>");
	    return contentBuilder.toString();
	}

	private byte[] mergeSinglePdf(byte[] generatedPdfBytes, String title, String fileName, String filepath) {
	    try (ByteArrayOutputStream finalOutputStream = new ByteArrayOutputStream()) {

	      
	        PdfDocument finalPdfDocument = new PdfDocument(new PdfWriter(finalOutputStream));
	        PdfDocument generatedPdf = new PdfDocument(new PdfReader(new ByteArrayInputStream(generatedPdfBytes)));
	        PdfMerger pdfMerger = new PdfMerger(finalPdfDocument);

	       
	        pdfMerger.merge(generatedPdf, 1, generatedPdf.getNumberOfPages());

	       
	        Path filePath = Paths.get(filepath).resolve(fileName).normalize();
	        if (Files.exists(filePath) && Files.isReadable(filePath)) {
	            try (PdfReader existingPdfReader = new PdfReader(filePath.toString());
	                 ByteArrayOutputStream modifiedOutputStream = new ByteArrayOutputStream()) {
	                PdfWriter modifiedWriter = new PdfWriter(modifiedOutputStream);
	                PdfDocument existingPdf = new PdfDocument(existingPdfReader, modifiedWriter);

	             
	                PdfPage firstPage = existingPdf.getPage(1);
	                PdfCanvas canvas = new PdfCanvas(firstPage);
	                PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
	                canvas.beginText()
	                    .setFontAndSize(font, 12)
	                    .moveText(20, firstPage.getPageSize().getTop() - 15) 
	                    .showText(title)
	                    .endText();
	                existingPdf.close();

	             
	                PdfDocument tempDoc = new PdfDocument(new PdfReader(new ByteArrayInputStream(modifiedOutputStream.toByteArray())));
	                pdfMerger.merge(tempDoc, 1, tempDoc.getNumberOfPages());
	                tempDoc.close();
	            } catch (IOException e) {
	                throw new RuntimeException("Error reading the file to merge: " + e.getMessage());
	            }
	        } else {
	            throw new RuntimeException("File not found or not readable: " + filePath);
	        }

	    
	        finalPdfDocument.close();
	        return finalOutputStream.toByteArray();

	    } catch (IOException e) {
	        e.printStackTrace();
	        throw new RuntimeException("Error merging PDFs: " + e.getMessage());
	    }
	}

	
	
	
	private ByteArrayOutputStream addPageNumber(byte[] generatedPdfBytes) {
	    try (ByteArrayOutputStream finalOutputStream = new ByteArrayOutputStream()) {

	        // Verify if PDF is not empty or corrupted
	        if (generatedPdfBytes == null || generatedPdfBytes.length == 0) {
	            throw new IOException("Generated PDF is empty or corrupted.");
	        }

	        // Read the generated PDF into PdfDocument
	        PdfDocument generatedPdf = null;
	        try {
	            PdfReader reader = new PdfReader(new ByteArrayInputStream(generatedPdfBytes));
	            PdfWriter writer = new PdfWriter(finalOutputStream);
	            generatedPdf = new PdfDocument(reader, writer);
	        } catch (IOException e) {
	            throw new IOException("Error opening PDF: " + e.getMessage());
	        }

	        // Add page numbers to the document
	        int totalPages = generatedPdf.getNumberOfPages();
	        for (int i = 1; i <= totalPages; i++) {
	            PdfPage page = generatedPdf.getPage(i);
	            PdfCanvas canvas = new PdfCanvas(page);
	            PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
	            canvas.beginText()
	                  .setFontAndSize(font, 12)
	                  .moveText(page.getPageSize().getRight() - 100, 20)
	                  .showText("Page " + i + " of " + totalPages)
	                  .endText();
	        }

	        // Close and return the final output stream
	        generatedPdf.close();
	        return finalOutputStream;

	    } catch (IOException e) {
	        e.printStackTrace();
	        throw new RuntimeException("Error printing page number: " + e.getMessage());
	    }
	}

	
	
}
