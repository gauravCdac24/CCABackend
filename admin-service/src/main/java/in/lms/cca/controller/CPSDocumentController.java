package in.lms.cca.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import in.lms.cca.entity.CPSDocument;
import in.lms.cca.service.ICPSDocumentService;
import in.lms.cca.util.api.AdminServiceAPIs;
import in.lms.cca.util.api.CPSDocServiceAPIs;
import in.lms.cca.util.global.Constant;
import in.lms.cca.util.global.EncryptionUtil;
@RestController
@RequestMapping(AdminServiceAPIs.ADMIN_SERVICE_BASE_URL)
@CrossOrigin
public class CPSDocumentController 
{
	
	private static final String CPS_DOCUMENT_FOLDER = "cps/Document";
	
	@Autowired
	private ICPSDocumentService cpsDocServ;
	
	private Path getCpsDocumentDirectory() {
		return Paths.get(Constant.REAL_PATH, CPS_DOCUMENT_FOLDER.split("/"));
	}

	private Optional<Path> resolveCpsFilePath(String fileName) {
		if (fileName == null || fileName.isBlank()) {
			return Optional.empty();
		}
		Path[] candidates = new Path[] {
				getCpsDocumentDirectory().resolve(fileName),
				Paths.get(Constant.REAL_PATH, "webapp", "cps", "Document", fileName),
				Paths.get(Constant.REAL_PATH + "//webapp//cps//Document").resolve(fileName)
		};
		for (Path candidate : candidates) {
			Path normalized = candidate.normalize();
			if (Files.exists(normalized)) {
				return Optional.of(normalized);
			}
		}
		return Optional.empty();
	}

	private Long decryptCpsDocId(String encryptedId) {
		if (encryptedId == null || encryptedId.isBlank()) {
			return null;
		}
		String decrypted = EncryptionUtil.decrypt(encryptedId);
		if (decrypted == null || decrypted.isBlank()) {
			return null;
		}
		try {
			return Long.parseLong(decrypted.trim());
		} catch (NumberFormatException ex) {
			return null;
		}
	}
	
	private String saveCpsDocumentFile(MultipartFile file) throws IOException {
		String extension = FilenameUtils.getExtension(file.getOriginalFilename());
		Path directoryPath = getCpsDocumentDirectory();
		Files.createDirectories(directoryPath);
		SimpleDateFormat dateFormats = new SimpleDateFormat("MMddyyyyHHmmssSSS");
		String timestamp = dateFormats.format(new Date());
		String filename = "CPS" + timestamp + "." + extension;
		Files.write(directoryPath.resolve(filename), file.getBytes());
		return filename;
	}

	private void cleanupSavedCpsFile(String filename) {
		if (filename == null || filename.isBlank()) {
			return;
		}
		try {
			Path filePath = getCpsDocumentDirectory().resolve(filename);
			Files.deleteIfExists(filePath);
		} catch (Exception cleanupEx) {
			cleanupEx.printStackTrace();
		}
	}
	
	
	  @PostMapping(CPSDocServiceAPIs.ADD_CPS_DOCUMENT)
	    public ResponseEntity<?> addNewCPSDoc(	            
	            @RequestParam("file") MultipartFile file,
	            @RequestParam("publishDate") String publishDate,
	            @RequestParam("version") String version) {
	      
		  
	        // Server Side Validation
	        if (version == null || version.trim().isEmpty()) {
	            return new ResponseEntity<>("Please enter version.", HttpStatus.BAD_REQUEST);
	        } else if (version.length() > 10) {
	            return new ResponseEntity<>("Version cannot exceed 10 characters.", HttpStatus.BAD_REQUEST);
	        }

	        if (publishDate == null || publishDate.trim().isEmpty()) {
	            return new ResponseEntity<>("Please enter publish date.", HttpStatus.BAD_REQUEST);
	        } else if (!publishDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
	            return new ResponseEntity<>("Publish date must be in the format YYYY-MM-DD.", HttpStatus.BAD_REQUEST);
	        }

	        if (file == null || file.isEmpty()) {
	            return new ResponseEntity<>("Please enter file name.", HttpStatus.BAD_REQUEST);
	        }

	        // Convert publishDate from String to Date
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        Date publishDateParsed;
	        try {
	            publishDateParsed = dateFormat.parse(publishDate);
	        } catch (ParseException e) {
	            return new ResponseEntity<>("Invalid publish date format.", HttpStatus.BAD_REQUEST);
	        }
	        
	        
	        String filename = null;
	        try {
	            filename = saveCpsDocumentFile(file);
	            
	            CPSDocument newCPSDocument = new CPSDocument();
	            newCPSDocument.setFileName(filename);
	            newCPSDocument.setPublishDate(publishDateParsed);
	            // New CPS templates start Inactive; admin activates explicitly.
	            newCPSDocument.setStatus("Inactive");
	            newCPSDocument.setVersion(version);

	            Optional<CPSDocument> c = cpsDocServ.addCPSDocument(newCPSDocument);
	            if (c.isEmpty()) {
	            	cleanupSavedCpsFile(filename);
	                return new ResponseEntity<>("An error occurred while saving the CPSDocument. Please try again.", HttpStatus.BAD_REQUEST);
	            }
	            
	            return new ResponseEntity<>("CPSDocument successfully added.", HttpStatus.OK);
	        } catch (Exception e) {
	            e.printStackTrace();
	            cleanupSavedCpsFile(filename);
	            return new ResponseEntity<>("An error occurred while saving the CPSDocument. Please try again.", HttpStatus.BAD_REQUEST);
	        }

		  
	    }
	
	  
	  @PostMapping(CPSDocServiceAPIs.UPDATE_CPS_DOCUMENT)
	    public ResponseEntity<?> UpdateCPSDoc(	            
	            @RequestParam("file") MultipartFile file,
	            @RequestParam("publishDate") String publishDate,
	            @RequestParam("version") String version,
	            @RequestParam("cpsDocId") String cpsDocId, 
	    		@RequestParam("created") String created){
		  
		  
	        // Server Side Validation
	        if (version == null || version.trim().isEmpty()) {
	            return new ResponseEntity<>("Please enter version.", HttpStatus.BAD_REQUEST);
	        } else if (version.length() > 10) {
	            return new ResponseEntity<>("Version cannot exceed 10 characters.", HttpStatus.BAD_REQUEST);
	        }

	        if (publishDate == null || publishDate.trim().isEmpty()) {
	            return new ResponseEntity<>("Please enter publish date.", HttpStatus.BAD_REQUEST);
	        } else if (!publishDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
	            return new ResponseEntity<>("Publish date must be in the format YYYY-MM-DD.", HttpStatus.BAD_REQUEST);
	        }

	        if (file == null || file.isEmpty()) {
	            return new ResponseEntity<>("Please enter file name.", HttpStatus.BAD_REQUEST);
	        }

	        // Convert publishDate from String to Date
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        Date publishDateParsed;
	        try {
	            publishDateParsed = dateFormat.parse(publishDate);
	        } catch (ParseException e) {
	            return new ResponseEntity<>("Invalid publish date format.", HttpStatus.BAD_REQUEST);
	        }

	        String filename = null;
	        try {
	            filename = saveCpsDocumentFile(file);
	            
	            CPSDocument newCPSDocument = new CPSDocument();
	            Date now = new Date();  
	            Long docId = Long.valueOf(EncryptionUtil.decrypt(cpsDocId));
	            CPSDocument existing = cpsDocServ.getCPSDocById(docId);
	            if (existing == null) {
	            	cleanupSavedCpsFile(filename);
	            	return new ResponseEntity<>("Invalid CPS Document Id.", HttpStatus.BAD_REQUEST);
	            }

	            newCPSDocument.setCpsDocId(docId);
	            newCPSDocument.setFileName(filename);
	            newCPSDocument.setVersion(version);
	            newCPSDocument.setPublishDate(publishDateParsed);
	            // Preserve existing status so edit does not force-activate.
	            newCPSDocument.setStatus(existing.getStatus() != null ? existing.getStatus() : "Inactive");
	            newCPSDocument.setUpdated(now);  
	            
	            
	            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
	 		    Date cdate = sdf.parse(created);
	 		   newCPSDocument.setCreated(cdate);
	            
	            Optional<CPSDocument> c = cpsDocServ.updateCPSDocument(newCPSDocument);
	            if (c.isEmpty()) {
	            	cleanupSavedCpsFile(filename);
	                return new ResponseEntity<>("An error occurred while saving the CPSDocument. Please try again.", HttpStatus.BAD_REQUEST);
	            }
	            
	            return new ResponseEntity<>("CPSDocument successfully updated.", HttpStatus.OK);
	        } catch (Exception e) {
	            e.printStackTrace();
	            cleanupSavedCpsFile(filename);
	            return new ResponseEntity<>("An error occurred while saving the CPSDocument. Please try again.", HttpStatus.BAD_REQUEST);
	        }

		  
	    }
	  
	@GetMapping(CPSDocServiceAPIs.GET_ALL_CPS_DOCUMENT)
	public ResponseEntity<?> getAllAddress(){
		
		List<CPSDocument>cpsDocumentList = cpsDocServ.getAllCPSDocument();
		return new ResponseEntity<>(cpsDocumentList, HttpStatus.OK); 
		
	}

	@GetMapping(CPSDocServiceAPIs.GET_ALL_ACTIVE_CPS_DOCUMENT)
	public ResponseEntity<?> getAllActiveCPSDocument() {
		List<CPSDocument> cpsDocumentList = cpsDocServ.getAllActiveCPSDocument();
		return new ResponseEntity<>(cpsDocumentList, HttpStatus.OK);
	}

	@GetMapping(CPSDocServiceAPIs.GET_ALL_INACTIVE_CPS_DOCUMENT)
	public ResponseEntity<?> getAllInactiveCPSDocument() {
		List<CPSDocument> cpsDocumentList = cpsDocServ.getAllInactiveCPSDocument();
		return new ResponseEntity<>(cpsDocumentList, HttpStatus.OK);
	}
	
	@GetMapping(CPSDocServiceAPIs.CHANGE_CPSDOC_STATUS)
	public ResponseEntity<?> changeCPSDOCStatus(@RequestParam("id") String cpsDocuId) {
		Long docId = decryptCpsDocId(cpsDocuId);
		if (docId == null) {
			return new ResponseEntity<>("Invalid CPS Document Id.", HttpStatus.BAD_REQUEST);
		}

		try {
			CPSDocument c = cpsDocServ.getCPSDocById(docId);
			if (c == null) {
				return new ResponseEntity<>("Invalid CPS Document Id.", HttpStatus.BAD_REQUEST);
			}

			String previousStatus = c.getStatus();
			if ("Active".equalsIgnoreCase(previousStatus)) {
				c.setStatus("Inactive");
			} else {
				// Only one Active CPS template is allowed at a time.
				List<CPSDocument> activeList = cpsDocServ.getAllActiveCPSDocument();
				boolean anotherActiveExists = activeList.stream()
						.anyMatch(active -> active.getCpsDocId() != null
								&& !active.getCpsDocId().equals(docId));
				if (anotherActiveExists) {
					return new ResponseEntity<>("Active cps already present.", HttpStatus.BAD_REQUEST);
				}
				c.setStatus("Active");
			}

			Optional<CPSDocument> updated = cpsDocServ.updateCPSDocument(c);
			if (updated.isEmpty()) {
				return new ResponseEntity<>("Unable to change CPS document status. Please try again.", HttpStatus.BAD_REQUEST);
			}

			return new ResponseEntity<>(
					"CPS document status changed to " + updated.get().getStatus() + ".",
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Unable to change CPS document status. Please try again.",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	 @GetMapping(CPSDocServiceAPIs.DOWNLOAD_FILE)
	    public ResponseEntity<Resource> downloadFile(
	    		@RequestParam("id") String cpsDocuId,
	    		@RequestParam(value = "activeOnly", required = false, defaultValue = "false") boolean activeOnly) {
	        Long cpsDocId = decryptCpsDocId(cpsDocuId);
	        if (cpsDocId == null) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	        }

	        try {
	            Optional<CPSDocument> cpsDocumentOpt = cpsDocServ.downloadfile(cpsDocId);
	            
	            if (cpsDocumentOpt.isPresent()) {
	                CPSDocument c = cpsDocumentOpt.get();
	                if (activeOnly && !"Active".equalsIgnoreCase(c.getStatus())) {
	                	return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
	                }
	                Optional<Path> filePathOpt = resolveCpsFilePath(c.getFileName());
	                if (filePathOpt.isEmpty()) {
	                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	                }
	                Path filePath = filePathOpt.get();
	                Resource resource = new UrlResource(filePath.toUri());
	                if (resource.exists()) {
	                    String contentType = Files.probeContentType(filePath);
	                    return ResponseEntity.ok()
	                            .contentType(MediaType.parseMediaType(contentType != null ? contentType : "application/octet-stream"))
	                            .header("Content-Disposition", "attachment; filename=\"" + resource.getFilename() + "\"")
	                            .body(resource);
	                }
	            }
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	        }
	    }
	@GetMapping(CPSDocServiceAPIs.GET_CPS_DOCUMENT_BY_ID)
	public ResponseEntity<?> getCPSDocumentByID(@RequestParam("id") String cpsDocId)
	{
		Long docId = decryptCpsDocId(cpsDocId);
		if (docId == null) {
			return new ResponseEntity<>("Invalid CPS Document Id.", HttpStatus.BAD_REQUEST);
		}
		
		try {
			CPSDocument c = cpsDocServ.getCPSDocById(docId);
			if (c == null) {
				return new ResponseEntity<>("Invalid CPS Document Id.", HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>(c, HttpStatus.OK); 
		}catch(Exception e) {
			return new ResponseEntity<>("Invalid CPS Document Id.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@GetMapping({CPSDocServiceAPIs.DELETE_CPS_DOCUMENT_BY_ID, CPSDocServiceAPIs.DELETE_CPS_DOCUMENT_BY_ID_LEGACY})
	public ResponseEntity<?> deleteCountryById(@RequestParam("id") String cpsDocId) {
		Long docId = decryptCpsDocId(cpsDocId);
		if (docId == null) {
			return new ResponseEntity<>("Invalid CPS Document Id.", HttpStatus.BAD_REQUEST);
		}

		try {
			boolean res = cpsDocServ.deleteBycpsDocId(docId);
			if (res) {
				return new ResponseEntity<>("CPS Document is successfully deleted.", HttpStatus.OK); 
			}
			return new ResponseEntity<>("Error in deleting the CPS Document.", HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error in deleting the CPS Document.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
}