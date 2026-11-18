package in.lms.cca.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import in.lms.cca.dto.ESignAPIVersionMasterDTO;
import in.lms.cca.entity.ESignAPIVersionMaster;
import in.lms.cca.entity.EsignAPISpecificationMaster;
import in.lms.cca.service.IESignAPIVersionMasterService;
import in.lms.cca.service.IEsignAPISpecificationMasterService;
import in.lms.cca.util.api.AdminServiceAPIs;
import in.lms.cca.util.api.ESignAPIVersionMasterServiceAPIs;
import in.lms.cca.util.global.Constant;
import in.lms.cca.util.global.DocumentFileUtil;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
@RequestMapping(AdminServiceAPIs.ADMIN_SERVICE_BASE_URL)
@CrossOrigin
public class ESignAPIVersionMasterController {

	
	@Autowired
	private IEsignAPISpecificationMasterService specService;
	
	@Autowired
	private IESignAPIVersionMasterService apiverService;
	
	

	@PostMapping(value = ESignAPIVersionMasterServiceAPIs.ADD_ESIGN_API_VERSION)
    public ResponseEntity<?> addNewAPIVersion(@RequestPart("apiVersionObj") ESignAPIVersionMasterDTO obj, @RequestPart("file") MultipartFile file) {
		
		try {
			
				String createdby = obj.getCreatedBy().trim();
				String esignApiSpecId = obj.getEsignApiSpecId();
				
				if(EncryptionUtil.decrypt(esignApiSpecId) != null)
					esignApiSpecId = EncryptionUtil.decrypt(esignApiSpecId);
				
				if(obj.getEsignApiSpecId().isEmpty()) {
					return new ResponseEntity<>("Please select API Specification.", HttpStatus.BAD_REQUEST);
				}
				
				
				if(obj.getReleaseDate() == null) {
					return new ResponseEntity<>("Please select Release Date.", HttpStatus.BAD_REQUEST);
				}


				
				if (obj.getApiVersion().isEmpty()) {
		            return new ResponseEntity<>("API version cannot be empty.", HttpStatus.BAD_REQUEST);
		        } else if (!obj.getApiVersion().trim().matches("^[A-Za-z0-9. ]+$")) {
		            return new ResponseEntity<>("Only alphabets, numbers, dot and space are allowed.", HttpStatus.BAD_REQUEST);
		        } else if (obj.getApiVersion().trim().length() < 3 || obj.getApiVersion().trim().length() > 150) {
		            return new ResponseEntity<>("The length must be between 3 and 150 characters.", HttpStatus.BAD_REQUEST);
		        }
				
				//Check for duplicate
				ESignAPIVersionMaster cobj = apiverService.getEsignAPIVersionByAPIVersionName(obj.getApiVersion().trim());
				
				if(cobj != null)
					return new ResponseEntity<>("Duplicate API Version is not allowed.", HttpStatus.BAD_REQUEST);
				
				EsignAPISpecificationMaster apiSpecObj = specService.getEsignAPISpecificationMasterById(Long.parseLong(esignApiSpecId));
				
				
				if(EncryptionUtil.decrypt(createdby) == null)
					createdby = EncryptionUtil.encrypt(createdby);
				
				if(file == null) {
					return new ResponseEntity<>("Please upload document.", HttpStatus.BAD_REQUEST);
				}
				
				//save the file
				String filename = DocumentFileUtil.saveFile(file, "API", "APIVersion/Document");
					
				//save
				ESignAPIVersionMaster eobj = new ESignAPIVersionMaster();
				eobj.setEsignApiSpecId(apiSpecObj);
				eobj.setCreatedBy(createdby);
				eobj.setUpdatedBy(createdby);
				eobj.setStatus("Active");
				eobj.setApiVersion(obj.getApiVersion());
				eobj.setDeprecationDate(obj.getDeprecationDate());
				eobj.setEspReadinessBy(obj.getEspReadinessBy());
				eobj.setReleaseDate(obj.getReleaseDate());
				eobj.setFileName(filename);
				
				
				Optional<ESignAPIVersionMaster> asp = apiverService.addESignAPIVersionMaster(eobj);
				
				if (asp.isEmpty()) {
	                return new ResponseEntity<>("An error occurred while saving. Please Try again.", HttpStatus.BAD_REQUEST);
	            }
	            return new ResponseEntity<>("API Version has been successfully added.", HttpStatus.OK);
				
				
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while saving. Please Try again.", HttpStatus.BAD_REQUEST);
		}
		
		
	}
	
	
	@PostMapping(ESignAPIVersionMasterServiceAPIs.UPDATE_ESIGN_API_VERSION)
    public ResponseEntity<?> updateAPIVersion(@RequestPart("apiVersionObj") ESignAPIVersionMasterDTO obj, @RequestPart("file") MultipartFile file) {
		
		try {
		
				String updatedby = obj.getUpdatedBy().trim();
				String esignApiSpecId = obj.getEsignApiSpecId();
				String esignApiVerId = obj.getEsignApiVerId();
				
				if(EncryptionUtil.decrypt(esignApiSpecId) != null)
					esignApiSpecId = EncryptionUtil.decrypt(esignApiSpecId);
				
				if(EncryptionUtil.decrypt(esignApiVerId) != null)
					esignApiVerId = EncryptionUtil.decrypt(esignApiVerId);

				
				if(obj.getEsignApiSpecId().isEmpty()) {
					return new ResponseEntity<>("Please select API Specification.", HttpStatus.BAD_REQUEST);
				}
				
				
				if(obj.getReleaseDate() == null) {
					return new ResponseEntity<>("Please select Release Date.", HttpStatus.BAD_REQUEST);
				}


				
				if (obj.getApiVersion().isEmpty()) {
		            return new ResponseEntity<>("API version cannot be empty.", HttpStatus.BAD_REQUEST);
		        } else if (!obj.getApiVersion().trim().matches("^[A-Za-z0-9. ]+$")) {
		            return new ResponseEntity<>("Only alphabets, numbers, dot and space are allowed.", HttpStatus.BAD_REQUEST);
		        } else if (obj.getApiVersion().trim().length() < 3 || obj.getApiVersion().trim().length() > 150) {
		            return new ResponseEntity<>("The length must be between 3 and 150 characters.", HttpStatus.BAD_REQUEST);
		        }
				
				//Check for duplicate
				ESignAPIVersionMaster cobj = apiverService.getEsignAPIVersionByAPIVersionName(obj.getApiVersion().trim());
				
				if(cobj != null && cobj.getEsignApiVerId() != Long.parseLong(esignApiVerId))
					return new ResponseEntity<>("Duplicate API Version is not allowed.", HttpStatus.BAD_REQUEST);
				
				EsignAPISpecificationMaster apiSpecObj = specService.getEsignAPISpecificationMasterById(Long.parseLong(esignApiSpecId));
				
				
				if(EncryptionUtil.decrypt(updatedby) == null)
					updatedby = EncryptionUtil.encrypt(updatedby);
				
				if(file == null) {
					return new ResponseEntity<>("Please upload document.", HttpStatus.BAD_REQUEST);
				}
				
				ESignAPIVersionMaster apiverobj = apiverService.getESignAPIVersionMasterById(Long.parseLong(esignApiVerId));
				
				//Delete the file
				DocumentFileUtil.deleteFile(apiverobj.getFileName(), "APIVersion/Document");
				
				//save the file
				String filename = DocumentFileUtil.saveFile(file, "API", "APIVersion/Document");
				
				
				
				//save
				
				apiverobj.setEsignApiSpecId(apiSpecObj);
				apiverobj.setUpdatedBy(updatedby);
				apiverobj.setApiVersion(obj.getApiVersion());
				apiverobj.setDeprecationDate(obj.getDeprecationDate());
				apiverobj.setEspReadinessBy(obj.getEspReadinessBy());
				apiverobj.setReleaseDate(obj.getReleaseDate());
				apiverobj.setFileName(filename);
				
				
				Optional<ESignAPIVersionMaster> asp = apiverService.addESignAPIVersionMaster(apiverobj);
				
				if (asp.isEmpty()) {
	                return new ResponseEntity<>("An error occurred while updating. Please Try again.", HttpStatus.BAD_REQUEST);
	            }
	            return new ResponseEntity<>("API Version has been successfully updated.", HttpStatus.OK);
				
				
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while updating. Please Try again.", HttpStatus.BAD_REQUEST);
		}
		
		
	}
	
	
	@GetMapping(ESignAPIVersionMasterServiceAPIs.GET_ALL_ESIGN_API_VERSION)
	public ResponseEntity<?> getAllAPIVersion(){
		
		List<ESignAPIVersionMaster> list =apiverService.getAllESignAPIVersionMaster();
		return new ResponseEntity<>(list, HttpStatus.OK); 
		
	}
	
	@GetMapping(ESignAPIVersionMasterServiceAPIs.GET_ESIGN_API_VERSION_BY_ID)
	public ResponseEntity<?> getAPIVersionByID(@RequestParam("id") String esignApiVerId)
	{
		try {
			String id = EncryptionUtil.decrypt(esignApiVerId);
			ESignAPIVersionMaster obj = apiverService.getESignAPIVersionMasterById(Long.parseLong(id));
			return new ResponseEntity<>(obj, HttpStatus.OK); 
		}catch(Exception e) {
			return new ResponseEntity<>("Invalid API Version Id.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	@GetMapping(ESignAPIVersionMasterServiceAPIs.CHANGE_ESIGN_API_VERSION_STATUS)
	public ResponseEntity<?> changeAPIVersionStatus(@RequestParam("id") String esignApiVerId, @RequestParam("qid") String username)
	{

		try {
			String id = EncryptionUtil.decrypt(esignApiVerId);
			String uname = username;
			
			if(EncryptionUtil.decrypt(uname) == null)
				uname = EncryptionUtil.encrypt(uname);
			
			ESignAPIVersionMaster obj = apiverService.getESignAPIVersionMasterById(Long.parseLong(id));
			
			if(obj.getStatus().equals("Active"))
				obj.setStatus("Inactive");
			else
				obj.setStatus("Active");
			
			obj.setUpdatedBy(uname);
			obj.setUpdated(new Date());
			
			apiverService.updateESignAPIVersionMaster(obj);
			
			return new ResponseEntity<>(HttpStatus.OK); 
		}catch(Exception e) {
			return new ResponseEntity<>("Invalid API Version Id.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@GetMapping(ESignAPIVersionMasterServiceAPIs.GET_ESIGN_API_DOCUMENT_BY_ID)
	public ResponseEntity<?> getAPIVersionDocumentByID(@RequestParam("id") String esignApiVerId)
	{
		try {
			String id = EncryptionUtil.decrypt(esignApiVerId);
			ESignAPIVersionMaster obj = apiverService.getESignAPIVersionMasterById(Long.parseLong(id));
			
		            if (obj != null) {
		                
		                Path filePath = Paths.get(Constant.REAL_PATH + "//APIVersion//Document").resolve(obj.getFileName()).normalize();
		                
		                Resource resource = new UrlResource(filePath.toUri());
		                
		                if (resource.exists()) {
		                    String contentType = Files.probeContentType(filePath);
		                    
		                    return ResponseEntity.ok()
		                            .contentType(MediaType.parseMediaType(contentType != null ? contentType : "application/octet-stream"))
		                            .header("Content-Disposition", "attachment; filename=\"" + resource.getFilename() + "\"")
		                            .body(resource);
		                } else {
		                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		                }
		            } else {
		                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		            }
		        
		}catch(Exception e) {
			return new ResponseEntity<>("Invalid API Version Id.", HttpStatus.BAD_REQUEST);
		}
		
	
	}
	
	
}
