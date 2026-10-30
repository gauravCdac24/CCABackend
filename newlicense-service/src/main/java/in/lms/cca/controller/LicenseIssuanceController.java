// package in.lms.cca.controller;

// import java.util.Date;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.multipart.MultipartFile;

// import in.lms.cca.entity.ApplicationDocument;
// import in.lms.cca.entity.ApplicationTimeLine;
// import in.lms.cca.entity.IntentApplication;
// import in.lms.cca.service.ApplicationDocumentService;
// import in.lms.cca.service.IApplicationTimeLineService;
// import in.lms.cca.service.IIntentApplicationService;
// import in.lms.cca.util.api.LicenseIssuanceServiceAPIs;
// import in.lms.cca.util.api.NewLicenseServiceAPIs;
// import in.lms.cca.util.global.DocumentFileUtil;
// import in.lms.cca.util.global.EncryptionUtil;

// @RestController
// @RequestMapping(NewLicenseServiceAPIs.NEW_LICENSE_SERVICE_BASE_URL)
// @CrossOrigin
// public class LicenseIssuanceController {
	
// 	@Autowired
// 	private ApplicationDocumentService applicationDocServ;
	
// 	@Autowired
// 	private IIntentApplicationService intentService;
	
// 	@Autowired
// 	private IApplicationTimeLineService appTimeLineServ;
	
// 	@PostMapping(LicenseIssuanceServiceAPIs.UPLOAD_REQUIRED_DOCUMENTS_FOR_LICENSE_ISSUANCE)
// 	public ResponseEntity<?> uploadRequiredDocumentsForLicenseIssuance(
// 			@RequestParam(value = "intentAppId", required = true) String intentAppId,
// 			@RequestParam(value = "createdBy", required = true) String createdBy,
// 	        @RequestParam(value = "bankGuarantee", required = true) MultipartFile bankGuarantee,
// 	        @RequestParam(value = "csr", required = true) MultipartFile csr,
// 	        @RequestParam(value = "agreement", required = true) MultipartFile agreement
// 	        ){
	        
	
// 			try {
				
// 				String appId = intentAppId;
// 				if(EncryptionUtil.decrypt(appId) != null)
// 					appId = EncryptionUtil.decrypt(appId);
				
// 				String cby = createdBy;
// 				if(EncryptionUtil.decrypt(cby) == null)
// 					cby = EncryptionUtil.encrypt(cby);
				
// 				//save the file
				
// 				String bankGuaranteeFileName = DocumentFileUtil.saveFile(bankGuarantee, "BankGuarantee", appId, "IssuanceDocuments/Document");
// 				String csrFileName = DocumentFileUtil.saveFile(bankGuarantee, "CSR", appId, "IssuanceDocuments/Document");
// 				String agreementFileName = DocumentFileUtil.saveFile(bankGuarantee, "Agreement", appId, "IssuanceDocuments/Document");
				
// 				IntentApplication intent =  intentService.getIntentByIntentAppId(Long.parseLong(appId));
				
				
// 				ApplicationDocument app = new ApplicationDocument();
// 					app.setCreatedBy(cby);
// 					app.setDocumentTitle("Bank Guarantee Document");
// 					app.setFileName(bankGuaranteeFileName);
// 					app.setIntentAppId(intent);
// 					app.setStatus("Active");
// 					app.setUpdatedBy(cby);
					
// 					applicationDocServ.addApplicationDocument(app);

					
// 					ApplicationDocument app1 = new ApplicationDocument();
// 					app1.setCreatedBy(cby);
// 					app1.setDocumentTitle("CSR Document");
// 					app1.setFileName(csrFileName);
// 					app1.setIntentAppId(intent);
// 					app1.setStatus("Active");
// 					app1.setUpdatedBy(cby);
					
// 					applicationDocServ.addApplicationDocument(app1);

					
// 					ApplicationDocument app2 = new ApplicationDocument();
// 					app2.setCreatedBy(cby);
// 					app2.setDocumentTitle("Agreement Document");
// 					app2.setFileName(agreementFileName);
// 					app2.setIntentAppId(intent);
// 					app2.setStatus("Active");
// 					app2.setUpdatedBy(cby);
					
// 					applicationDocServ.addApplicationDocument(app2);
					
					
// 					//add application timeline
// 					ApplicationTimeLine obj = new ApplicationTimeLine();
// 					obj.setApplicationStatus("DOCUMENT_SUBMITTED_FOR_LICENSE");
// 					obj.setInitiatedBy(cby);
// 					obj.setIntentAppId(intent);
					
// 					appTimeLineServ.addApplicationTimeLine(obj);
					
// 					intent.setApplicationStatus("DOCUMENT_SUBMITTED_FOR_LICENSE");
// 					intent.setUpdated(new Date());
// 					intent.setUpdatedBy(cby);
// 					intentService.updateIntentApp(intent);

				
// 				return new ResponseEntity<>("You have successfully uploaded the documents.", HttpStatus.OK);
				
// 			}catch(Exception e) {
// 				e.printStackTrace();
// 				return new ResponseEntity<>("An error occurred while uploading the documents. Please try again later.", HttpStatus.BAD_REQUEST);
				
// 			}
	
	
	
	
	

// 	}
	
// }


//updated
package in.lms.cca.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import in.lms.cca.entity.ApplicationDocument;
import in.lms.cca.entity.ApplicationTimeLine;
import in.lms.cca.entity.IntentApplication;
import in.lms.cca.service.ApplicationDocumentService;
import in.lms.cca.service.IApplicationTimeLineService;
import in.lms.cca.service.IIntentApplicationService;
import in.lms.cca.util.api.LicenseIssuanceServiceAPIs;
import in.lms.cca.util.api.NewLicenseServiceAPIs;
import in.lms.cca.util.global.DocumentFileUtil;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
@RequestMapping(NewLicenseServiceAPIs.NEW_LICENSE_SERVICE_BASE_URL)
@CrossOrigin
public class LicenseIssuanceController {
	
	@Autowired
	private ApplicationDocumentService applicationDocServ;
	
	@Autowired
	private IIntentApplicationService intentService;
	
	@Autowired
	private IApplicationTimeLineService appTimeLineServ;
	
	@Autowired
    private ResourceLoader resourceLoader;
	
	@PostMapping(LicenseIssuanceServiceAPIs.UPLOAD_REQUIRED_DOCUMENTS_FOR_LICENSE_ISSUANCE)
	public ResponseEntity<?> uploadRequiredDocumentsForLicenseIssuance(
			@RequestParam(value = "intentAppId", required = true) String intentAppId,
			@RequestParam(value = "createdBy", required = true) String createdBy,
			@RequestParam(value = "bankGuarantee", required = true) MultipartFile bankGuarantee,
			@RequestParam(value = "csr", required = true) MultipartFile csr,
			@RequestParam(value = "agreement", required = true) MultipartFile agreement
	){
		try {
			String appId = intentAppId;
			if(EncryptionUtil.decrypt(appId) != null)
				appId = EncryptionUtil.decrypt(appId);
			
			String cby = createdBy;
			if(EncryptionUtil.decrypt(cby) == null)
				cby = EncryptionUtil.encrypt(cby);
			
			// FIXED: Save each respective file correctly instead of using bankGuarantee for all
			String bankGuaranteeFileName = DocumentFileUtil.saveFile(bankGuarantee, "BankGuarantee", appId, "IssuanceDocuments/Document");
			String csrFileName = DocumentFileUtil.saveFile(csr, "CSR", appId, "IssuanceDocuments/Document");
			String agreementFileName = DocumentFileUtil.saveFile(agreement, "Agreement", appId, "IssuanceDocuments/Document");
			
			IntentApplication intent = intentService.getIntentByIntentAppId(Long.parseLong(appId));
			
			ApplicationDocument app = new ApplicationDocument();
			app.setCreatedBy(cby);
			app.setDocumentTitle("Bank Guarantee Document");
			app.setFileName(bankGuaranteeFileName);
			app.setIntentAppId(intent);
			app.setStatus("Active");
			app.setUpdatedBy(cby);
			applicationDocServ.addApplicationDocument(app);

			ApplicationDocument app1 = new ApplicationDocument();
			app1.setCreatedBy(cby);
			app1.setDocumentTitle("CSR Document");
			app1.setFileName(csrFileName);
			app1.setIntentAppId(intent);
			app1.setStatus("Active");
			app1.setUpdatedBy(cby);
			applicationDocServ.addApplicationDocument(app1);

			ApplicationDocument app2 = new ApplicationDocument();
			app2.setCreatedBy(cby);
			app2.setDocumentTitle("Agreement Document");
			app2.setFileName(agreementFileName);
			app2.setIntentAppId(intent);
			app2.setStatus("Active");
			app2.setUpdatedBy(cby);
			applicationDocServ.addApplicationDocument(app2);
			
			// Add application timeline
			ApplicationTimeLine obj = new ApplicationTimeLine();
			obj.setApplicationStatus("DOCUMENT_SUBMITTED_FOR_LICENSE");
			obj.setInitiatedBy(cby);
			obj.setIntentAppId(intent);
			appTimeLineServ.addApplicationTimeLine(obj);
			
			intent.setApplicationStatus("DOCUMENT_SUBMITTED_FOR_LICENSE");
			intent.setUpdated(new Date());
			intent.setUpdatedBy(cby);
			intentService.updateIntentApp(intent);

			return new ResponseEntity<>("You have successfully uploaded the documents.", HttpStatus.OK);
			
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while uploading the documents. Please try again later.", HttpStatus.BAD_REQUEST);
		}
	}

	// Endpoint to download document templates
	@GetMapping("/download-template/{templateName}")
	public ResponseEntity<Resource> downloadTemplate(@PathVariable String templateName) {
		/*try {
			// Define path where templates are stored on your server (e.g., src/main/resources/templates/ or external folder)
			String filePath = "templates/" + templateName + ".pdf"; 
			Path path = Paths.get(filePath);
			
			if (!Files.exists(path)) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}*/
		
		try {
            // Loads from src/main/resources/templates/{templateName}.pdf
            Resource resource = resourceLoader.getResource("classpath:templates/" + templateName + ".pdf");
            
            if (!resource.exists() || !resource.isReadable()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
			
			//ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
			
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + templateName + ".pdf");
			headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
			headers.add(HttpHeaders.PRAGMA, "no-cache");
			headers.add(HttpHeaders.EXPIRES, "0");

			return ResponseEntity.ok()
					.headers(headers)
					//.contentLength(path.toFile().length())
					.contentType(MediaType.APPLICATION_PDF)
					.body(resource);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}





