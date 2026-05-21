package in.lms.cca.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.lms.cca.dto.client.ApplicationDTO;
import in.lms.cca.entity.ApplicationTimeLine;
import in.lms.cca.entity.IntentApplication;
import in.lms.cca.service.IApplicationTimeLineService;
import in.lms.cca.service.IIntentApplicationService;
import in.lms.cca.util.api.NewLicenseClientServiceAPIs;
import in.lms.cca.util.api.NewLicenseServiceAPIs;
import in.lms.cca.util.global.EncryptionUtil;


@RestController
@RequestMapping(NewLicenseServiceAPIs.NEW_LICENSE_SERVICE_BASE_URL)
@CrossOrigin
public class NewLicenseClientController {

	@Autowired
	private IIntentApplicationService intentAppServ;
	
	@Autowired
	private IApplicationTimeLineService appTimeLineServ;
	
	@GetMapping(NewLicenseClientServiceAPIs.GET_ALL_NEW_APPLICATIONS_RECOMMENDED_FOR_IN_PRINCIPAL_APPROVAL)
    public ResponseEntity<?> getAllApplications() {
		
        List<ApplicationDTO> applicationDTO = new ArrayList<>();
        
        //get all intent applications
        List<IntentApplication> intentApplications = intentAppServ.getAllIntentApplication();
        
        for(IntentApplication app: intentApplications) {
        	
        	if(app.getApplicationStatus() != null && app.getApplicationStatus().equals("RECOMMENDED_IN_PRINCIPLE_APPROVAL")) {
        	
	        	ApplicationDTO application = new ApplicationDTO();
	        	
	        	application.setAcknowledgementNo(app.getAcknowledgementNo());
	        	application.setApplicantUserName(app.getUserName());
	        	application.setApplicationCurrentStatus(app.getApplicationStatus());
	        	application.setApplicationInitiatedOn(app.getCreated());
	        	application.setApplicationType(app.getAppTypeMasterId());
	        	application.setIntentAppId(app.getIntentAppId().toString());
	        	application.setStatusInitiatedOn(null);
	        	
	        	//
	        	List<ApplicationTimeLine> list = appTimeLineServ.getByIntentApplicationId(app.getIntentAppId());
	        	if(list.size()>0)
	        		application.setStatusInitiatedOn(list.get(0).getCreated());
	        	else
	        		application.setStatusInitiatedOn(app.getUpdated());
	        	
	        	applicationDTO.add(application);
        	
        	}
        }
        
        return new ResponseEntity<>(applicationDTO, HttpStatus.OK);
    }
	
	
	
	@GetMapping(NewLicenseClientServiceAPIs.GET_ALL_NEW_APPLICATIONS_WITH_REQUIRED_DOCUMENTS)
    public ResponseEntity<?> getAllAppWithRequiredDocuments() {
		
        List<ApplicationDTO> applicationDTO = new ArrayList<>();
        
        //get all intent applications
        List<IntentApplication> intentApplications = intentAppServ.getAllIntentApplication();
        	
        for(IntentApplication app: intentApplications) {
        	
        	if(app.getApplicationStatus() != null && app.getApplicationStatus().equals("DOCUMENT_SUBMITTED_FOR_LICENSE")) {
        	
	        	ApplicationDTO application = new ApplicationDTO();
	        	
	        	application.setAcknowledgementNo(app.getAcknowledgementNo());
	        	application.setApplicantUserName(app.getUserName());
	        	application.setApplicationCurrentStatus(app.getApplicationStatus());
	        	application.setApplicationInitiatedOn(app.getCreated());
	        	application.setApplicationType(app.getAppTypeMasterId());
	        	application.setIntentAppId(app.getIntentAppId().toString());
	        	application.setStatusInitiatedOn(null);
	        	
	        	//
	        	List<ApplicationTimeLine> list = appTimeLineServ.getByIntentApplicationId(app.getIntentAppId());
	        	if(list.size()>0)
	        		application.setStatusInitiatedOn(list.get(0).getCreated());
	        	else
	        		application.setStatusInitiatedOn(app.getUpdated());
	        	
	        	applicationDTO.add(application);
        	
        	}
        }
        
        return new ResponseEntity<>(applicationDTO, HttpStatus.OK);
    }
	
	
	
	
	@GetMapping(NewLicenseClientServiceAPIs.GET_NEW_APPLICATION_WITH_BANK_GUARANTEE_PROOF)
    public ResponseEntity<?> getNewAppWithBankGuaranteeProof() {
		
        List<ApplicationDTO> applicationDTO = new ArrayList<>();
        
        //get all intent applications
        List<IntentApplication> intentApplications = intentAppServ.getAllIntentApplication();
        
        for(IntentApplication app: intentApplications) {
        	
        	if(app.getApplicationStatus() != null && app.getApplicationStatus().equals("BANK_GUARANTEE_PROOF_UPLOADED")) {
        	
	        	ApplicationDTO application = new ApplicationDTO();
	        	
	        	application.setAcknowledgementNo(app.getAcknowledgementNo());
	        	application.setApplicantUserName(app.getUserName());
	        	application.setApplicationCurrentStatus(app.getApplicationStatus());
	        	application.setApplicationInitiatedOn(app.getCreated());
	        	application.setApplicationType(app.getAppTypeMasterId());
	        	application.setIntentAppId(app.getIntentAppId().toString());
	        	application.setStatusInitiatedOn(null);
	        	
	        	//
	        	List<ApplicationTimeLine> list = appTimeLineServ.getByIntentApplicationId(app.getIntentAppId());
	        	if(list.size()>0)
	        		application.setStatusInitiatedOn(list.get(0).getCreated());
	        	else
	        		application.setStatusInitiatedOn(app.getUpdated());
	        	
	        	applicationDTO.add(application);
        	
        	}
        }
        
        return new ResponseEntity<>(applicationDTO, HttpStatus.OK);
    }
	
	
	
	@GetMapping(NewLicenseClientServiceAPIs.GET_LICENSE_ISSUED_APPLICATIONS)
    public ResponseEntity<?> getLicenseIssuedApplication() {
		
        List<ApplicationDTO> applicationDTO = new ArrayList<>();
        
        //get all intent applications
        List<IntentApplication> intentApplications = intentAppServ.getAllIntentApplication();
        
        for(IntentApplication app: intentApplications) {
        	
        	if(app.getApplicationStatus() != null && app.getApplicationStatus().equals("LICENSE_ISSUED")) {
        	
	        	ApplicationDTO application = new ApplicationDTO();
	        	
	        	application.setAcknowledgementNo(app.getAcknowledgementNo());
	        	application.setApplicantUserName(app.getUserName());
	        	application.setApplicationCurrentStatus(app.getApplicationStatus());
	        	application.setApplicationInitiatedOn(app.getCreated());
	        	application.setApplicationType(app.getAppTypeMasterId());
	        	application.setIntentAppId(app.getIntentAppId().toString());
	        	application.setStatusInitiatedOn(null);
	        	
	        	//
	        	List<ApplicationTimeLine> list = appTimeLineServ.getByIntentApplicationId(app.getIntentAppId());
	        	if(list.size()>0)
	        		application.setStatusInitiatedOn(list.get(0).getCreated());
	        	else
	        		application.setStatusInitiatedOn(app.getUpdated());
	        	
	        	applicationDTO.add(application);
        	
        	}
        }
        
        return new ResponseEntity<>(applicationDTO, HttpStatus.OK);
    }
		
	
	
	
	@GetMapping(NewLicenseClientServiceAPIs.CHANGE_INTENT_APPLICATION_STATUS)
	public ResponseEntity<?> changeIntentApplicationStatus(
			    @RequestParam("id") String intentAppId, 
			    @RequestParam("status") String status, 
			    @RequestParam("initiatedby") String initiatedBy) {
		
		String id = intentAppId;
		String appstatus = status;
		String appInitiatedBy= initiatedBy;
		
		
		if(EncryptionUtil.decrypt(intentAppId) != null)
			id = EncryptionUtil.decrypt(intentAppId);
		
		if(EncryptionUtil.decrypt(status) != null)
			appstatus = EncryptionUtil.decrypt(status);
		
		if(EncryptionUtil.decrypt(initiatedBy) == null)
			appInitiatedBy = EncryptionUtil.encrypt(initiatedBy);
		
		
		
		try {
			IntentApplication inApp = intentAppServ.getIntentByIntentAppId(Long.parseLong(id));
			
			//add application timeline
			ApplicationTimeLine obj = new ApplicationTimeLine();
			obj.setApplicationStatus(appstatus);
			obj.setInitiatedBy(appInitiatedBy);
			obj.setIntentAppId(inApp);
			
			appTimeLineServ.addApplicationTimeLine(obj);
			
			//update Intent Application Status;
			inApp.setApplicationStatus(appstatus);
			inApp.setUpdatedBy(appInitiatedBy);
			inApp.setUpdated(new Date());
			
			intentAppServ.updateIntentApp(inApp);
			
			return new ResponseEntity<>("Success", HttpStatus.OK);
		}
		catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
		}
		
		
		
	}
	
	
	@GetMapping(NewLicenseClientServiceAPIs.GET_ALL_APPLICATION_TIMELINE)
    public ResponseEntity<?> getAllApplicationTimeLine() {
	
	try {
		
		List<ApplicationTimeLine> apptimelineList = appTimeLineServ.getAllApplicationTimeLine();
		
		return new ResponseEntity<>(apptimelineList, HttpStatus.OK);
		
	}catch(Exception e){
		
		return new ResponseEntity<>(null, HttpStatus.OK);
		
	}
	}
	
}
