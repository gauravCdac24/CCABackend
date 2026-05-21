package in.lms.cca.controller;

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

import in.lms.cca.dto.DocumentDTO;
import in.lms.cca.entity.Country;
import in.lms.cca.entity.DocumentMaster;
import in.lms.cca.service.IDocumentService;
import in.lms.cca.util.api.AdminServiceAPIs;
import in.lms.cca.util.api.CountryServiceAPIs;
import in.lms.cca.util.api.DocumentMasterAPIs;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
@RequestMapping(AdminServiceAPIs.ADMIN_SERVICE_BASE_URL)
@CrossOrigin
public class DocumentController {
	
	@Autowired
	private IDocumentService iDocumentService;
	
	
	
	
	@PostMapping(DocumentMasterAPIs.ADD_DOCUMENT)
	public ResponseEntity<?> addDocument(@RequestBody DocumentDTO documentObj){
		
		
		
		//Server Side Validation
		
		if(documentObj.getDocumentName().equals("")) {
			return new ResponseEntity<>("Please enter document name.", HttpStatus.BAD_REQUEST);
		}else if(!documentObj.getDocumentName().trim().matches("^[A-Za-z ]+$")) {
			return new ResponseEntity<>("Only alphabets and spaces are allowed.", HttpStatus.BAD_REQUEST);
		}else if(documentObj.getDocumentName().length() < 3 || documentObj.getDocumentName().length() > 50) {
			return new ResponseEntity<>("The length must be between 3 and 50 characters.", HttpStatus.BAD_REQUEST);
		}
				
		
		
				
		//Check for Unique Country Name
		DocumentMaster cobj = iDocumentService.getDocumentByName(documentObj.getDocumentName().trim());
		if(cobj != null) {
			return new ResponseEntity<>("Duplicate Document name is not allowed.", HttpStatus.BAD_REQUEST);
		}
		
		
		
		try {
			String createdby = documentObj.getCreatedBy().trim();
			
			DocumentMaster newDocument = new DocumentMaster();
			newDocument.setDocumentName(documentObj.getDocumentName());
			newDocument.setStatus("Active");
			newDocument.setCreatedBy(createdby);
			newDocument.setUpdatedBy(createdby);
			Optional<DocumentMaster> c = iDocumentService.addDocument(newDocument);
			
			if(c.isEmpty())
				return new ResponseEntity<>("An error occurred while saving the document name. Please Try again.", HttpStatus.OK);
			return new ResponseEntity<>("Document Name successfully added.", HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("An error occurred while saving the document name. Please Try again.", HttpStatus.BAD_REQUEST);
		}
		
 
		
	}
	
	
	@GetMapping(DocumentMasterAPIs.GET_ALL_DOCUMENT)
	public ResponseEntity<?> getAllDocument(){
		
		List<DocumentMaster> documentList = iDocumentService.getAllDocument();
		return new ResponseEntity<>(documentList, HttpStatus.OK); 
		
	}
	
	@GetMapping(DocumentMasterAPIs.GET_ALL_ACTIVE_DOCUMENT)
	public ResponseEntity<?> getAllActiveDocument(){
		
		List<DocumentMaster> documentList = iDocumentService.getAllActiveDocument();
		return new ResponseEntity<>(documentList, HttpStatus.OK); 
		
	}
	
	@GetMapping(DocumentMasterAPIs.CHANGE_DOCUMENT_STATUS)
	public void changeCountryStatus(@RequestParam("id") String documentId) {
		
		String id = EncryptionUtil.decrypt(documentId);
		
		try {
			DocumentMaster c = iDocumentService.getDocumentById(Long.parseLong(id));
			
			if(c.getStatus().equals("Active"))
				c.setStatus("Inactive");
			else
				c.setStatus("Active");
			
			iDocumentService.updateDocument(c);
		}
		catch(Exception e) {
			
		}
		
	}
	
	
	@GetMapping(DocumentMasterAPIs.GET_DOCUMENT_BY_ID)
	public ResponseEntity<?> getDocumentByID(@RequestParam("id") String documentId)
	{
		String id = EncryptionUtil.decrypt(documentId);
		
		try {
			DocumentMaster documentObj = iDocumentService.getDocumentById(Long.parseLong(id));
			return new ResponseEntity<>(documentObj, HttpStatus.OK); 
		}catch(Exception e) {
			return new ResponseEntity<>("Invalid Document Id.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	
	@PostMapping(DocumentMasterAPIs.UPDATE_DOCUMENT)
	public ResponseEntity<?>updateDocument(@RequestBody DocumentDTO documentObj){
		
		
		System.out.println("---->"+documentObj.toString());
		//Server Side Validation
		
		if(documentObj.getDocumentName().equals("")) {
			return new ResponseEntity<>("Please enter document name.", HttpStatus.BAD_REQUEST);
		}else if(!documentObj.getDocumentName().trim().matches("^[A-Za-z ]+$")) {
			return new ResponseEntity<>("Only alphabets and spaces are allowed.", HttpStatus.BAD_REQUEST);
		}else if(documentObj.getDocumentName().length() < 3 || documentObj.getDocumentName().length() > 50) {
			return new ResponseEntity<>("The length must be between 3 and 50 characters.", HttpStatus.BAD_REQUEST);
		}
				
		
		
				
		//Check for Unique Country Name
		DocumentMaster cobj = iDocumentService.getDocumentByName(documentObj.getDocumentName().trim());
		if(cobj != null) {
			return new ResponseEntity<>("Duplicate Document name is not allowed.", HttpStatus.BAD_REQUEST);
		}
		
		
		
		try {
			Long cid = Long.parseLong(EncryptionUtil.decrypt(documentObj.getDocumentId()));
			
			System.out.println(cid);
			
			String createdby = documentObj.getUpdatedBy().trim();
			DocumentMaster newDocument = new DocumentMaster();
			newDocument.setDocumentId(cid);
			newDocument.setDocumentName(documentObj.getDocumentName());
			newDocument.setStatus("Active");
			newDocument.setUpdated(new Date());
			newDocument.setUpdatedBy(createdby);
			newDocument.setCreatedBy(createdby);
			newDocument.setCreated(new Date());
			Optional<DocumentMaster> c = iDocumentService.updateDocument(newDocument);
			
			if(c.isEmpty())
				return new ResponseEntity<>("An error occurred while saving the document name. Please Try again.", HttpStatus.OK);
			return new ResponseEntity<>("Document Name successfully updated.", HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while saving the document name. Please Try again.", HttpStatus.BAD_REQUEST);
		}
		
 
		
	}

	

}
