package in.lms.cca.controller;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
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
	
	@Autowired
	private ICPSDocumentService cpsDocServ;
	
	
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
	        
	        
	        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
	        String path = Constant.REAL_PATH + "//cps//Document";
	        SimpleDateFormat dateFormats = new SimpleDateFormat("MMddyyyyHHmmssSSS");
	        String timestamp = dateFormats.format(new Date());
	        String filename = "CPS" + timestamp + "." + extension;

	        try {
	            byte[] barr = file.getBytes();
	            BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(path + "//" + filename));
	            bout.write(barr);
	            bout.flush();
	            bout.close();
	            
	            CPSDocument newCPSDocument = new CPSDocument();
	            newCPSDocument.setFileName(filename);
	            newCPSDocument.setPublishDate(publishDateParsed);
	            newCPSDocument.setStatus("Active");
	            newCPSDocument.setVersion(version);

	            Optional<CPSDocument> c = cpsDocServ.addCPSDocument(newCPSDocument);
	            if (c.isEmpty())
	                return new ResponseEntity<>("An error occurred while saving the CPSDocument. Please try again.", HttpStatus.OK);
	            
	            return new ResponseEntity<>("CPSDocument successfully added.", HttpStatus.OK);
	        } catch (Exception e) {
	            e.printStackTrace();
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
	        
	        
	        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
	        String path = Constant.REAL_PATH + "//cps//Document";
	        SimpleDateFormat dateFormats = new SimpleDateFormat("MMddyyyyHHmmssSSS");
	        String timestamp = dateFormats.format(new Date());
	        String filename = "CPS" + timestamp + "." + extension;

	        try {
	            byte[] barr = file.getBytes();
	            BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(path + "//" + filename));
	            bout.write(barr);
	            bout.flush();
	            bout.close();
	            
	            CPSDocument newCPSDocument = new CPSDocument();
	            Date now = new Date();  
	            newCPSDocument.setCpsDocId(Long.valueOf(EncryptionUtil.decrypt(cpsDocId)));
	            newCPSDocument.setFileName(filename);
	            newCPSDocument.setVersion(version);
	            newCPSDocument.setPublishDate(publishDateParsed);
	            newCPSDocument.setStatus("Active");
	            newCPSDocument.setUpdated(now);  
	            
	            
	            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
	 		    Date cdate = sdf.parse(created);
	 		   newCPSDocument.setCreated(cdate);
	            
	            Optional<CPSDocument> c = cpsDocServ.updateCPSDocument(newCPSDocument);
	            if (c.isEmpty())
	                return new ResponseEntity<>("An error occurred while saving the CPSDocument. Please try again.", HttpStatus.OK);
	            
	            return new ResponseEntity<>("CPSDocument successfully added.", HttpStatus.OK);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return new ResponseEntity<>("An error occurred while saving the CPSDocument. Please try again.", HttpStatus.BAD_REQUEST);
	        }

		  
	    }
	  
	@GetMapping(CPSDocServiceAPIs.GET_ALL_CPS_DOCUMENT)
	public ResponseEntity<?> getAllAddress(){
		
		List<CPSDocument>cpsDocumentList = cpsDocServ.getAllCPSDocument();
		return new ResponseEntity<>(cpsDocumentList, HttpStatus.OK); 
		
	}
	
	@GetMapping(CPSDocServiceAPIs.CHANGE_CPSDOC_STATUS)
	public void changeCPSDOCStatus(@RequestParam("id") String cpsDocuId) {
		
		String id = EncryptionUtil.decrypt(cpsDocuId);
		
		try {
			CPSDocument c = cpsDocServ.getCPSDocById(Long.parseLong(id));
			
			if(c.getStatus().equals("Active"))
				c.setStatus("Inactive");
			else
				c.setStatus("Active");
			
			cpsDocServ.updateCPSDocument(c);
		}
		catch(Exception e) {
			
		}
		
		
		
	}
	
	 @GetMapping(CPSDocServiceAPIs.DOWNLOAD_FILE)
	    public ResponseEntity<Resource> downloadFile(@RequestParam("id") String cpsDocuId) {
		 
		 System.out.println(cpsDocuId);
		 
	        String id = EncryptionUtil.decrypt(cpsDocuId);
	        
	        System.out.println("--->"+id);
	        
	        try {
	            // Fetch the CPSDocument from the service
	            Optional<CPSDocument> cpsDocumentOpt = cpsDocServ.downloadfile(Long.parseLong(id));
	            
	            if (cpsDocumentOpt.isPresent()) {
	                CPSDocument c = cpsDocumentOpt.get();
	                System.out.println(c.getFileName());
	                Path filePath = Paths.get(Constant.REAL_PATH + "//webapp//cps//Document").resolve(c.getFileName()).normalize();
	                System.out.println(filePath);
	                Resource resource = new UrlResource(filePath.toUri());
	                System.out.println(resource);
	                if (resource.exists()) {
	                    String contentType = Files.probeContentType(filePath);
	                    System.out.println(contentType);
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
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	        }
	    }
	@GetMapping(CPSDocServiceAPIs.GET_CPS_DOCUMENT_BY_ID)
	public ResponseEntity<?> getCPSDocumentByID(@RequestParam("id") String cpsDocId)
	{
		String id = EncryptionUtil.decrypt(cpsDocId);
		
		try {
			CPSDocument c = cpsDocServ.getCPSDocById(Long.parseLong(id));
			return new ResponseEntity<>(c, HttpStatus.OK); 
		}catch(Exception e) {
			return new ResponseEntity<>("Invalid CPS Document Id.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@GetMapping(CPSDocServiceAPIs.DELETE_CPS_DOCUMENT_BY_ID)
	public ResponseEntity<?> deleteCountryById(@RequestParam("id") String cpsDocId) {
		
		String id = EncryptionUtil.decrypt(cpsDocId);
		
		
			boolean res = cpsDocServ.deleteBycpsDocId(Long.parseLong(id));
			
			if(res) {
				return new ResponseEntity<>("CPS Document is successfully deleted.", HttpStatus.OK); 
			}
			else {
				return new ResponseEntity<>("Error in deleting the CPS Document.", HttpStatus.BAD_REQUEST);
			}
		
	}
	
	
	
}