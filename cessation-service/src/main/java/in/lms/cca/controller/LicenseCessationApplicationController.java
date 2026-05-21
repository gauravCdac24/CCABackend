package in.lms.cca.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import in.lms.cca.dto.LicenseCessationApplicationDTO;
import in.lms.cca.entity.CessationAppUndertakingEntity;
import in.lms.cca.entity.LicenseCessationApplicationEntity;
import in.lms.cca.entity.ReviewCessationDocumentEntity;
import in.lms.cca.entity.ReviewDocumentEntity;
import in.lms.cca.service.CessationAppUndertakingService;
import in.lms.cca.service.LicenseCessationApplicationService;
import in.lms.cca.service.ReviewCessationDocumentService;
import in.lms.cca.service.ReviewDocumentService;
import in.lms.cca.util.api.LicenseCessationApplicationAPIs;
import in.lms.cca.util.golbal.DocumentFileUtil;
import in.lms.cca.util.golbal.EncryptionUtil;
import in.lms.cca.util.golbal.RealPath;



@RestController
@CrossOrigin
@RequestMapping(LicenseCessationApplicationAPIs.CESSATION_APPLICATION)
public class LicenseCessationApplicationController {
	
	@Autowired
	private LicenseCessationApplicationService licenseCessationApplicationService;
	
	@Autowired
	private CessationAppUndertakingService appUndertakingService; 
	
	@Autowired
	private ReviewDocumentService reviewDocumentService;
	
	@Autowired
	private ReviewCessationDocumentService cessationDocumentService;

	@PostMapping(LicenseCessationApplicationAPIs.ADD_LICENSE_CESSATION_APPLICATION)
	public ResponseEntity<?> addAllApplicationsReviewData(
	        @ModelAttribute LicenseCessationApplicationDTO licenseCessationApplicationDTO) {

	    try {
	        // Validate input
	    	
	    	
	    	 Random rand = new Random();
		        Integer rnum = rand.nextInt(1000);
	        if (licenseCessationApplicationDTO.getLicenseId() == null || 
	            licenseCessationApplicationDTO.getCessationNotice() == null) {
	            return ResponseEntity.badRequest().body("License ID and Cessation Notice are required");
	        }

	        // Fetch existing applications by license ID
	        List<LicenseCessationApplicationEntity> allApplications =
	                licenseCessationApplicationService.findByLicenseId(licenseCessationApplicationDTO.getLicenseId());

	        if (!allApplications.isEmpty()) {
	            // Mark all existing applications as inactive
	            for (LicenseCessationApplicationEntity applicationEntity : allApplications) {
	                applicationEntity.setStatus("Inactive");
	                applicationEntity.setUpdated(new Date());
	                licenseCessationApplicationService.updateAllData(applicationEntity); // Save updated entity
	            }
	        }

	        // Save the new cessation application
	        LicenseCessationApplicationEntity newApplication = new LicenseCessationApplicationEntity();

	        // Use UUID for unique filenames
	        String uniqueFilename = DocumentFileUtil.saveFile(
	                licenseCessationApplicationDTO.getCessationNotice(),
	                "CessationNotice",
	                rnum.toString(),
	                "CessationNotice"
	        );

	        newApplication.setCessationNotice(uniqueFilename);
	        newApplication.setLicenseId(licenseCessationApplicationDTO.getLicenseId());
	        newApplication.setStatus("Active");
	        newApplication.setUserName(licenseCessationApplicationDTO.getUserName());

	        Optional<LicenseCessationApplicationEntity> savedApplication =
	                licenseCessationApplicationService.saveAllData(newApplication);

	        if (savedApplication.isPresent()) {
	            return ResponseEntity.ok("Cessation application added successfully");
	        } else {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save cessation application");
	        }

	    } catch (IOException e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving cessation notice");
	    }
	}

	@GetMapping(LicenseCessationApplicationAPIs.GET_ALL_DATA_BY_LICENSEID)
	public ResponseEntity<?> getAllDataLicenId(@RequestParam("licenseId") String licenseId) {
	    try {
	        
	    	System.out.println("-=-=-=-=-=-=-==->"+EncryptionUtil.decrypt(licenseId));
	    	
	        LicenseCessationApplicationEntity licenseCessationApplicationEntity = 
	            licenseCessationApplicationService.getByLicenseById(EncryptionUtil.decrypt(licenseId));
	        
	       
	        if (licenseCessationApplicationEntity != null) {
	            return ResponseEntity.ok(licenseCessationApplicationEntity); 
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                                 .body("No data found for the given license ID");
	        }
	    } catch (Exception e) {
	     
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("An error occurred while fetching the data");
	    }
	}
	
	
	@GetMapping(LicenseCessationApplicationAPIs.GET_ALL_DATA_BY_CESSATION_ID)
	public ResponseEntity<?> getAllDataCessationID(@RequestParam("cessationAppId") String cessationAppId) {
	    try {
	        
	    	System.out.println("-=-=-=-=-=-=-==->"+EncryptionUtil.decrypt(cessationAppId));
	    	
	        Optional<LicenseCessationApplicationEntity> licenseCessationApplicationEntity = 
	            licenseCessationApplicationService.getByCessationById(EncryptionUtil.decrypt(cessationAppId));
	        
	        LicenseCessationApplicationEntity cessationApplicationEntity=licenseCessationApplicationEntity.get();
	        
	        
	        
	        cessationApplicationEntity.setStatus("ApprovedNotice");
	        cessationApplicationEntity.setUpdated(new Date());
	        
	        licenseCessationApplicationService.updateAllData(cessationApplicationEntity);
	       
	        if (licenseCessationApplicationEntity != null) {
	            return ResponseEntity.ok("Data are updated"); 
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                                 .body("No data found for the given Cessation ID");
	        }
	    } catch (Exception e) {
	     
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("An error occurred while fetching the data");
	    }
	}

	
	
	@GetMapping(LicenseCessationApplicationAPIs.GET_REJECT_BY_CESSATION_ID)
	public ResponseEntity<?> getRejectByCessationID(@RequestParam("cessationAppId") String cessationAppId) {
	    try {
	        
	    	System.out.println("-=-=-=-=-=-=-==->"+EncryptionUtil.decrypt(cessationAppId));
	    	
	        Optional<LicenseCessationApplicationEntity> licenseCessationApplicationEntity = 
	            licenseCessationApplicationService.getByCessationById(EncryptionUtil.decrypt(cessationAppId));
	        
	        LicenseCessationApplicationEntity cessationApplicationEntity=licenseCessationApplicationEntity.get();
	        
	        
	        
	        cessationApplicationEntity.setStatus("Inactive");
	        cessationApplicationEntity.setUpdated(new Date());
	        
	        licenseCessationApplicationService.updateAllData(cessationApplicationEntity);
	       
	        if (licenseCessationApplicationEntity != null) {
	            return ResponseEntity.ok("Data are updated"); 
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                                 .body("No data found for the given Cessation ID");
	        }
	    } catch (Exception e) {
	     
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("An error occurred while fetching the data");
	    }
	}

		
	
	@GetMapping(LicenseCessationApplicationAPIs.GET_ALL_DATA)
	public ResponseEntity<?> getAllData() {
	    try {
	        
	    	
	        List<LicenseCessationApplicationEntity> licenseCessationApplicationEntity = 
	            licenseCessationApplicationService.getAllData();
	        
	       
	        if (licenseCessationApplicationEntity != null) {
	            return ResponseEntity.ok(licenseCessationApplicationEntity); 
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                                 .body("No data found");
	        }
	    } catch (Exception e) {
	     
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("An error occurred while fetching the data");
	    }
	}

	
	@GetMapping(LicenseCessationApplicationAPIs.GET_BY_CESSATION_ID)
	public ResponseEntity<?> getByCessationID(@RequestParam("cessationAppId") String cessationAppId) {
	    try {
	        
	    	System.out.println("-=-=-=-=-=-=-==->"+EncryptionUtil.decrypt(cessationAppId));
	    	
	        Optional<LicenseCessationApplicationEntity> licenseCessationApplicationEntity = 
	            licenseCessationApplicationService.getByCessationById(EncryptionUtil.decrypt(cessationAppId));
	        
	       
	        if (licenseCessationApplicationEntity.get() != null) {
	            return ResponseEntity.ok(licenseCessationApplicationEntity.get()); 
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                                 .body("No data found for the given Cessation ID");
	        }
	    } catch (Exception e) {
	     
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("An error occurred while fetching the data");
	    }
	}

	@GetMapping(LicenseCessationApplicationAPIs.DOWNLOAD_STEP_TWO_DOCUMENT)
	public ResponseEntity<?> getStepTwoDocument(@RequestParam("id") String uid)
	{
		try {
			
			if (uid == null || uid.isEmpty()) {
				return new ResponseEntity<>("Error in downloading file.", HttpStatus.BAD_REQUEST);
	        }
			
			String id = EncryptionUtil.decrypt(uid);
			
			System.out.println("id-=-=-=-=-=-=-=>"+id);
			
			
			String filename = "";
			String filepath = "";
			
			
			LicenseCessationApplicationEntity noticeFile =  licenseCessationApplicationService.getNoticeDocumentById(Long.parseLong(id));
				
				
				
					filename = noticeFile.getCessationNotice();
					filepath = "CessationNotice";
				
			
		                
		                Path filePath = Paths.get(RealPath.REAL_PATH, filepath).resolve(filename).normalize();
		                
		                System.out.println("filePath=-=-=-=->"+filePath);
		                
		                Resource resource = new UrlResource(filePath.toUri());
		                
		                
		                
		                if (resource.exists() && resource.isReadable()) {
		                    String contentType = Files.probeContentType(filePath);

		                    return ResponseEntity.ok()
		                            .contentType(MediaType.parseMediaType(contentType != null ? contentType : MediaType.APPLICATION_OCTET_STREAM_VALUE))
		                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
		                            .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
		                            .body(resource);
		                } else {
		                	return new ResponseEntity<>("Error in downloading file.", HttpStatus.BAD_REQUEST);
		                }
		            
		        
		}catch(Exception e) {
			return new ResponseEntity<>("Error in downloading file.", HttpStatus.BAD_REQUEST);
		}
		
	
	}


	@PostMapping(LicenseCessationApplicationAPIs.SAVE_ALL_DATA)
	public ResponseEntity<String> handleSubmit(
	        @RequestParam Map<String, String> formData,
	        @RequestParam Map<String, MultipartFile> files,
	        @RequestParam("cessationAppId") String cessationAppId) {

	    Random rand = new Random();
	    Integer rnum = rand.nextInt(1000);

	   
	    System.out.println("FormData keys:");
	    formData.keySet().forEach(System.out::println);

	    System.out.println("Files keys:");
	    files.keySet().forEach(System.out::println);

	    List<CessationAppUndertakingEntity> appUndertakingEntities=appUndertakingService.getAllByCessationId(cessationAppId);
	    System.out.println("appUndertakingEntities size=-=-=-==->"+appUndertakingEntities.size());
	    
	    if(!appUndertakingEntities.isEmpty()) {
	    	for( CessationAppUndertakingEntity appUndertakingEntity: appUndertakingEntities)
	    	{
	    		appUndertakingEntity.setStatus("Inactive");
	    		appUndertakingEntity.setUpdated(new Date());
	    		Optional<CessationAppUndertakingEntity>optional=appUndertakingService.updateData(appUndertakingEntity);
	    	}
	    	
	    } 
	    formData.forEach((formKey, value) -> {
	        if (formKey.startsWith("key_")) {
	           
	            String fileKey = "file_" + formKey.substring(4); 
	            String undertakingId = formData.get("id_" + formKey.substring(4));

	            if (undertakingId == null) {
	                System.out.println("UndertakingId is null for key: " + formKey);
	                return; 
	            }

	           
	            MultipartFile file = files.get(fileKey);
	            if (file != null && !file.isEmpty()) {
	                System.out.println("Processing file for key: " + formKey);
	                System.out.println("File Name: " + file.getOriginalFilename());

	                CessationAppUndertakingEntity entity = new CessationAppUndertakingEntity();
	                entity.setUndertakingId(undertakingId);
	                entity.setCessationAppId(Long.valueOf(cessationAppId));
	                entity.setUndertakingKey(formKey);
	                entity.setStatus("Active");

	                try {
	                    
	                    String filename = DocumentFileUtil.saveFile(file, "ChecklistDocument", rnum.toString(), "ChecklistDocument");
	                    entity.setFilename(filename);
	                    System.out.println("File saved as: " + file.getOriginalFilename());

	                   
	                    Optional<CessationAppUndertakingEntity> optional = appUndertakingService.save(entity);
	                    if (optional.isPresent()) {
	                        System.out.println("Entity saved successfully: " + entity);
	                    } else {
	                        System.out.println("Failed to save entity for key: " + formKey);
	                    }
	                } catch (IOException e) {
	                    System.err.println("Error saving file: " + file.getOriginalFilename());
	                    e.printStackTrace();
	                }
	            } else {
	                System.out.println("No file found for key: " + fileKey);
	            }
	        }
	    });

	    Optional<LicenseCessationApplicationEntity> licenseCessationApplicationEntity = 
	            licenseCessationApplicationService.getByCessationById(cessationAppId);
	        
	        LicenseCessationApplicationEntity cessationApplicationEntity=licenseCessationApplicationEntity.get();
	        
	        cessationApplicationEntity.setStatus("cessation_form_submitted");
	        cessationApplicationEntity.setUpdated(new Date());
	        
	        licenseCessationApplicationService.updateAllData(cessationApplicationEntity);
	    
	    
	    return ResponseEntity.ok("Form data and files processed successfully!");
	}

	@GetMapping(LicenseCessationApplicationAPIs.GET_ALL_UNDERTAKING)
	public ResponseEntity<?> getAllDocumentUndertaking() {
	    
	    List<CessationAppUndertakingEntity> appUndertakingEntities = appUndertakingService.getAllByUndertaking();
	    
	    if (appUndertakingEntities.isEmpty()) {
	        return ResponseEntity.notFound().build();
	    }
	    return ResponseEntity.ok(appUndertakingEntities);
	}
	
	
	@GetMapping(LicenseCessationApplicationAPIs.GET_ALL_UNDERTAKING_BY_CESSATION_ID)
	public ResponseEntity<?> getAllDocumentUndertakingByCessationId(@RequestParam("cessationAppId")String cessationAppId) {
		
		 System.out.println("cessationAppId===-=-=-=-=-> " + EncryptionUtil.decrypt(cessationAppId));
	    
	    List<CessationAppUndertakingEntity> appUndertakingEntities = appUndertakingService.getAllByUndertakingByCessationId(EncryptionUtil.decrypt(cessationAppId));
	    
	    if (appUndertakingEntities.isEmpty()) {
	        return ResponseEntity.notFound().build();
	    }
	    return ResponseEntity.ok(appUndertakingEntities);
	}

	
	@GetMapping(LicenseCessationApplicationAPIs.GET_ALL_DATA_FOR_UNDERTAKING)
	public ResponseEntity<?> getAllDataForApprovalUndertaking() {
	    try {
	        
	    	
	        List<LicenseCessationApplicationEntity> licenseCessationApplicationEntity = 
	            licenseCessationApplicationService.getAllDataForUndertaking();
	        
	       
	        if (licenseCessationApplicationEntity != null) {
	            return ResponseEntity.ok(licenseCessationApplicationEntity); 
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                                 .body("No data found");
	        }
	    } catch (Exception e) {
	     
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("An error occurred while fetching the data");
	    }
	}

	
	
	@GetMapping(LicenseCessationApplicationAPIs.VIEW_UNDERTAKING_FILE)
	public ResponseEntity<Resource> viewFile(@RequestParam("id") String cpsDocuId) {
		System.out.println(cpsDocuId);
		String id = EncryptionUtil.decrypt(cpsDocuId);

		System.out.println(id);
		try {
			Optional<CessationAppUndertakingEntity> cpsDocumentOpt = appUndertakingService
					.downloadfile(Long.parseLong(id));

			if (cpsDocumentOpt.isPresent()) {
				CessationAppUndertakingEntity c = cpsDocumentOpt.get();
				System.out.println(c.getFilename());
				Path filePath = Paths.get(RealPath.REAL_PATH + "//ChecklistDocument")
						.resolve(c.getFilename()).normalize();
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


	@PostMapping(LicenseCessationApplicationAPIs.APPROVE_FROM_CCA_OFFICER)
	public ResponseEntity<?> approveApplicationForCCAOfficer(@RequestParam("cessationAppId") String cessationAppId) {
	    try {
	        Optional<LicenseCessationApplicationEntity> licenseCessationApplicationEntityOpt =
	                licenseCessationApplicationService.getByCessationById(cessationAppId);
	       
	        if (licenseCessationApplicationEntityOpt.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body("License cessation application not found for ID: " + cessationAppId);
	        }
	        
	        LicenseCessationApplicationEntity cessationApplicationEntity = licenseCessationApplicationEntityOpt.get();
	        cessationApplicationEntity.setStatus("approval_from_cca_officer");
	        cessationApplicationEntity.setUpdated(new Date());
	        
	        licenseCessationApplicationService.updateAllData(cessationApplicationEntity);
	        
	        return ResponseEntity.ok("License cessation application approved successfully.");
	    } catch (Exception e) {
	        e.printStackTrace();
	        
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("An error occurred while approving the license cessation application.");
	    }
	}
	
	
	@PostMapping(LicenseCessationApplicationAPIs.REJECT_FROM_CCA_OFFICER)
	public ResponseEntity<?> rejectApplicationForCCAOfficer(
	        @RequestParam("cessationAppId") String cessationAppId,
	        @RequestParam("remarks") String remarks,
	        @RequestParam Map<String, String> formData,
	        @RequestParam("isBolean")boolean isBolean) {
	    try {
	      
	        Optional<ReviewDocumentEntity> optionalReviewDocuments = reviewDocumentService.getByCessationId(cessationAppId);

	        if (optionalReviewDocuments.isPresent()) {
	            ReviewDocumentEntity existingDocument = optionalReviewDocuments.get();
	            existingDocument.setStatus("Inactive");
	            existingDocument.setUpdated(new Date());
	            reviewDocumentService.updateData(existingDocument);

	          
	            List<ReviewCessationDocumentEntity> relatedDocuments = cessationDocumentService.getByRewievId(existingDocument.getReviewId());
	            for (ReviewCessationDocumentEntity cessationDoc : relatedDocuments) {
	                cessationDoc.setStatus("Inactive");
	                cessationDoc.setUpdated(new Date());
	                cessationDocumentService.updateData(cessationDoc);
	            }
	        }

	      
	        ReviewDocumentEntity newReviewDocument = new ReviewDocumentEntity();
	        newReviewDocument.setCessationAppId(Long.valueOf(cessationAppId));
	        newReviewDocument.setRemarks(remarks);
	        newReviewDocument.setStatus("Active");

	        Optional<ReviewDocumentEntity> savedReviewDocument = reviewDocumentService.saveData(newReviewDocument);

	        if (savedReviewDocument.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body("Failed to save the new review document.");
	        }

	     
	        savedReviewDocument.ifPresent(document -> {
	            formData.forEach((formKey, value) -> {
	                if (formKey.startsWith("key_")) {
	                    String undertakingId = formData.get("id_" + formKey.substring(4));

	                    if (undertakingId != null) {
	                        ReviewCessationDocumentEntity cessationDoc = new ReviewCessationDocumentEntity();
	                        cessationDoc.setReviewId(document);
	                        cessationDoc.setUndertakingId(Long.valueOf(undertakingId));
	                        cessationDoc.setStatus("Active");

	                        Optional<ReviewCessationDocumentEntity> savedCessationDoc = cessationDocumentService.saveData(cessationDoc);

	                        if (savedCessationDoc.isEmpty()) {
	                            System.out.println("Failed to save cessation document for undertaking ID: " + undertakingId);
	                        }
	                    } else {
	                        System.out.println("Undertaking ID is null for form key: " + formKey);
	                    }
	                }
	            });
	        });

	       
	        Optional<LicenseCessationApplicationEntity> licenseCessationOpt =
	                licenseCessationApplicationService.getByCessationById(cessationAppId);

	        if (licenseCessationOpt.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body("License cessation application not found for ID: " + cessationAppId);
	        }

	        LicenseCessationApplicationEntity cessationApplication = licenseCessationOpt.get();
	        
	        System.out.println("isBolean=-=-=--->"+isBolean);
	        
	        if(isBolean) {
	        cessationApplication.setStatus("Recommand For Rejection");
	        }else {
	        	 cessationApplication.setStatus("reject_from_cca_officer");
	        }
	        cessationApplication.setUpdated(new Date());

	        licenseCessationApplicationService.updateAllData(cessationApplication);

	        return ResponseEntity.ok("License cessation application rejected successfully.");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("An error occurred while rejecting the license cessation application.");
	    }
	}

	@GetMapping(LicenseCessationApplicationAPIs.GET_ALL_DATA_FROM_CCA_OFFICER)
	public ResponseEntity<?> getAllDataForCCAOfficer(@RequestParam("cessationAppId") String cessationAppId) {
	    try {
	     
	    	  Map<String, Object> responseData = new HashMap<>();
		        List<ReviewDocumentEntity> reviewDocuments = reviewDocumentService.getAllData(cessationAppId);
		        responseData.put("reviewDocuments", reviewDocuments);
		        for(ReviewDocumentEntity documentEntity:reviewDocuments)
		        {
		        List<ReviewCessationDocumentEntity> cessationDocuments = cessationDocumentService.getAllData(documentEntity);
		        responseData.put("cessationDocuments", cessationDocuments);
		        }
	        return ResponseEntity.ok(responseData);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("An error occurred while fetching data for CCA officer.");
	    }
	}
	
	
	@GetMapping(LicenseCessationApplicationAPIs.GET_ALL_ACTIVE_DATA_FROM_CCA_OFFICER)
	public ResponseEntity<?> getAllActiveDataForCCAOfficer(@RequestParam("cessationAppId") String cessationAppId) {
	    try {
	    	  Map<String, Object> responseData = new HashMap<>();
	        List<ReviewDocumentEntity> reviewDocuments = reviewDocumentService.getAllDatas(cessationAppId);
	        responseData.put("reviewDocument", reviewDocuments);
	        for(ReviewDocumentEntity documentEntity:reviewDocuments)
	        {
	        List<ReviewCessationDocumentEntity> cessationDocuments = cessationDocumentService.getAllDatas(documentEntity);
	        responseData.put("cessationDocument", cessationDocuments);
	        }
	        return ResponseEntity.ok(responseData);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("An error occurred while fetching data for CCA officer.");
	    }
	}

	@PostMapping(LicenseCessationApplicationAPIs.EDIT_ALL_DATA)
	public ResponseEntity<String> handleEditData(
	        @RequestParam Map<String, String> formData,
	        @RequestParam Map<String, MultipartFile> files,
	        @RequestParam("cessationAppId") String cessationAppId) {

	    Random rand = new Random();
	    Integer rnum = rand.nextInt(1000);

	   
	    System.out.println("FormData keys:");
	    formData.keySet().forEach(System.out::println);

	    System.out.println("Files keys:");
	    files.keySet().forEach(System.out::println);

	    formData.forEach((formKey, value) -> {
	        if (formKey.startsWith("key_")) {
	           
	            String fileKey = "file_" + formKey.substring(4); 
	            String undertakingId = formData.get("id_" + formKey.substring(4));

	            if (undertakingId == null) {
	                System.out.println("UndertakingId is null for key: " + formKey);
	                return; 
	            }
	            System.out.println("UndertakingId for key: " + undertakingId);
	        
	            MultipartFile file = files.get(fileKey);
	            if (file != null && !file.isEmpty()) {
	                System.out.println("Processing file for key: " + formKey);
	                System.out.println("File Name: " + file.getOriginalFilename());
	                
	                CessationAppUndertakingEntity cessationAppUndertakingEntity=appUndertakingService.getAllDataByUndertakingId(undertakingId);
	 	           
	                DocumentFileUtil.deleteFile(cessationAppUndertakingEntity.getFilename(), "ChecklistDocument");

	                try {
	                    
	                    String filename = DocumentFileUtil.saveFile(file, "ChecklistDocument", rnum.toString(), "ChecklistDocument");
	                    cessationAppUndertakingEntity.setFilename(filename);
	                    
	                    System.out.println("File saved as: " + file.getOriginalFilename());

	                   
	                    Optional<CessationAppUndertakingEntity> optional = appUndertakingService.updateData(cessationAppUndertakingEntity);
	                    if (optional.isPresent()) {
	                        System.out.println("Entity saved successfully: " + cessationAppUndertakingEntity);
	                    } else {
	                        System.out.println("Failed to save entity for key: " + formKey);
	                    }
	                } catch (IOException e) {
	                    System.err.println("Error saving file: " + file.getOriginalFilename());
	                    e.printStackTrace();
	                }
	            } else {
	                System.out.println("No file found for key: " + fileKey);
	            }
	        }
	    });

	    Optional<LicenseCessationApplicationEntity> licenseCessationApplicationEntity = 
	            licenseCessationApplicationService.getByCessationById(cessationAppId);
	        
	        LicenseCessationApplicationEntity cessationApplicationEntity=licenseCessationApplicationEntity.get();
	        
	        cessationApplicationEntity.setStatus("cessation_form_submitted");
	        cessationApplicationEntity.setUpdated(new Date());
	        
	        licenseCessationApplicationService.updateAllData(cessationApplicationEntity);
	    
	    
	    return ResponseEntity.ok("Form data and files processed successfully!");
	}

	
	
	@PostMapping(LicenseCessationApplicationAPIs.REJECT_FROM_CCA)
	public ResponseEntity<?> rejectApplicationForCCA(
	        @RequestParam("cessationAppId") String cessationAppId,
	        @RequestParam("remarks") String remarks) {
	    try {
	    	
	    	ReviewDocumentEntity documentEntity=reviewDocumentService.getDataByCessationId(cessationAppId);
	    	documentEntity.setCcaRemarks(remarks);
	    	documentEntity.setUpdated(new Date());
	    	
	    	reviewDocumentService.updateData(documentEntity);
	    	
	    	
	        Optional<LicenseCessationApplicationEntity> licenseCessationOpt =
	                licenseCessationApplicationService.getByCessationById(cessationAppId);
	        if (licenseCessationOpt.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body("License cessation application not found for ID: " + cessationAppId);
	        }
	        LicenseCessationApplicationEntity cessationApplication = licenseCessationOpt.get();
	      	cessationApplication.setStatus("reject_from_cca_officer");
	        cessationApplication.setUpdated(new Date());
	        licenseCessationApplicationService.updateAllData(cessationApplication);
	        return ResponseEntity.ok("License cessation application rejected successfully.");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("An error occurred while rejecting the license cessation application.");
	    }
	}

	
	
	@GetMapping(LicenseCessationApplicationAPIs.GET_ALL_DATA_FROM_CCA)
	public ResponseEntity<?> getAllDataForCCA() {
		 try {
		        
		    	
		        List<LicenseCessationApplicationEntity> licenseCessationApplicationEntity = 
		            licenseCessationApplicationService.getAllDataForUndertakings();
		        
		        System.out.println("LicenseCessationApplicationEntity size-=-=->"+licenseCessationApplicationEntity);
		        
		       
		        if (licenseCessationApplicationEntity != null) {
		            return ResponseEntity.ok(licenseCessationApplicationEntity); 
		        } else {
		            return ResponseEntity.status(HttpStatus.NOT_FOUND)
		                                 .body("No data found");
		        }
		    } catch (Exception e) {
		     
		        e.printStackTrace();
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                             .body("An error occurred while fetching the data");
		    }
	}
	

	@PostMapping(LicenseCessationApplicationAPIs.APPROVE_FROM_CCA)
	public ResponseEntity<?> approveApplicationForCCA(
	        @RequestParam("cessationAppId") String cessationAppId) {
	    try {
	    	
	    	LicenseCessationApplicationEntity documentEntity=licenseCessationApplicationService.getNoticeDocumentsById(Long.valueOf(cessationAppId));
	    	
	    	System.out.println("documentEntity=-=--->"+documentEntity.getUserName());
	    	
	    	String userName=documentEntity.getUserName();
	    	
	    	System.out.println("userName=-=--->"+userName);
	    	licenseCessationApplicationService.changeLicenseeRoleToExLicensee(userName);
	    	
	    	documentEntity.setStatus("cessace the licensee");
	    	documentEntity.setUpdated(new Date());
	    	licenseCessationApplicationService.updateAllData(documentEntity);
	    	
	    	
	        Optional<LicenseCessationApplicationEntity> licenseCessationOpt =
	                licenseCessationApplicationService.getByCessationById(cessationAppId);
	        if (licenseCessationOpt.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body("License cessation application not found for ID: " + cessationAppId);
	        }
	        LicenseCessationApplicationEntity cessationApplication = licenseCessationOpt.get();
	      	cessationApplication.setStatus("cessace the licensee");
	        cessationApplication.setUpdated(new Date());
	        licenseCessationApplicationService.updateAllData(cessationApplication);
	        return ResponseEntity.ok("License cessation application rejected successfully.");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("An error occurred while rejecting the license cessation application.");
	    }
	}

	
	@GetMapping(LicenseCessationApplicationAPIs.GET_ALL_DATA_FROM_CCA_OFFICER_TO_CCA)
	public ResponseEntity<?> getAllDataForCCAOfficerToCCA() {
		 try {
		        
		    	
		        List<LicenseCessationApplicationEntity> licenseCessationApplicationEntity = 
		            licenseCessationApplicationService.getAllDataForUndertakinges();
		        
		        System.out.println("LicenseCessationApplicationEntity size-=-=->"+licenseCessationApplicationEntity);
		        
		       
		        if (licenseCessationApplicationEntity != null) {
		            return ResponseEntity.ok(licenseCessationApplicationEntity); 
		        } else {
		            return ResponseEntity.status(HttpStatus.NOT_FOUND)
		                                 .body("No data found");
		        }
		    } catch (Exception e) {
		     
		        e.printStackTrace();
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                             .body("An error occurred while fetching the data");
		    }
	}

	
	
	@GetMapping(LicenseCessationApplicationAPIs.GET_ALL_DATA_FROM_CCA_OFFICERS)
	public ResponseEntity<?> getAllDataForCCAOfficer() {
		 try {
		        
		    	
		        List<LicenseCessationApplicationEntity> licenseCessationApplicationEntity = 
		            licenseCessationApplicationService.getAllDataForUndertakingess();
		        
		        System.out.println("LicenseCessationApplicationEntity size-=-=->"+licenseCessationApplicationEntity);
		        
		       
		        if (licenseCessationApplicationEntity != null) {
		            return ResponseEntity.ok(licenseCessationApplicationEntity); 
		        } else {
		            return ResponseEntity.status(HttpStatus.NOT_FOUND)
		                                 .body("No data found");
		        }
		    } catch (Exception e) {
		     
		        e.printStackTrace();
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                             .body("An error occurred while fetching the data");
		    }
	}


	
	
	

}
