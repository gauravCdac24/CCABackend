package in.lms.cca.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.lms.cca.dto.LicenseAcquisitionWorkflowDTO;
import in.lms.cca.dto.LicenseFormContextDTO;
import in.lms.cca.dto.client.ApplicationDTO;
import in.lms.cca.entity.AppLocation;
import in.lms.cca.entity.ApplicationDocument;
import in.lms.cca.entity.ApplicationTimeLine;
import in.lms.cca.entity.IntentApplication;
import in.lms.cca.service.AppLocationService;
import in.lms.cca.service.ApplicationDocumentService;
import in.lms.cca.service.IApplicationTimeLineService;
import in.lms.cca.service.IIntentApplicationService;
import in.lms.cca.service.LicenseAcquisitionWorkflowService;
import in.lms.cca.util.api.NewLicenseClientServiceAPIs;
import in.lms.cca.util.api.NewLicenseServiceAPIs;
import in.lms.cca.util.global.ApplicationStatusUtil;
import in.lms.cca.util.global.EncryptionUtil;


@RestController
@RequestMapping(NewLicenseServiceAPIs.NEW_LICENSE_SERVICE_BASE_URL)
@CrossOrigin
public class NewLicenseClientController {

	@Autowired
	private IIntentApplicationService intentAppServ;
	
	@Autowired
	private IApplicationTimeLineService appTimeLineServ;

	@Autowired
	private LicenseAcquisitionWorkflowService licenseAcquisitionWorkflowService;

	@Autowired
	private AppLocationService appLocationService;

	@Autowired
	private ApplicationDocumentService applicationDocumentService;
	
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

	        	applyWorkflow(application, app, "CCA");
	        	
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

	        	applyWorkflow(application, app, "CCA_OFFICER");
	        	
	        	applicationDTO.add(application);
        	
        	}
        }
        
        return new ResponseEntity<>(applicationDTO, HttpStatus.OK);
    }
	
	
	
	
	@GetMapping(NewLicenseClientServiceAPIs.GET_NEW_APPLICATION_WITH_BANK_GUARANTEE_PROOF)
    public ResponseEntity<?> getNewAppWithBankGuaranteeProof(
			@RequestParam(value = "role", required = false, defaultValue = "ADMIN") String role) {
		
        List<ApplicationDTO> applicationDTO = new ArrayList<>();
        
        //get all intent applications
        List<IntentApplication> intentApplications = intentAppServ.getAllIntentApplication();
        
        for(IntentApplication app: intentApplications) {
        	
        	if (isLicenseDetailsQueue(app.getApplicationStatus())) {
        	
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

	        	applyWorkflow(application, app, role);
	        	
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

	        	applyWorkflow(application, app, "CCA");
	        	
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

	@GetMapping(NewLicenseClientServiceAPIs.GET_LICENSE_FORM_CONTEXT)
	public ResponseEntity<?> getLicenseFormContext(@RequestParam("id") String intentAppId) {
		String id = intentAppId;
		if (EncryptionUtil.decrypt(id) != null) {
			id = EncryptionUtil.decrypt(id);
		}
		try {
			Long appId = Long.parseLong(id);
			LicenseFormContextDTO context = new LicenseFormContextDTO();
			context.setInPrincipleApprovalDate(findInPrincipleApprovalDate(appId));
			context.setRootCaKeyCount(countRootCaKeys(appId));
			return new ResponseEntity<>(context, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Unable to load license form context.", HttpStatus.BAD_REQUEST);
		}
	}

	private boolean isLicenseDetailsQueue(String status) {
		if (status == null) {
			return false;
		}
		return "BANK_GUARANTEE_PROOF_UPLOADED".equals(status)
				|| ApplicationStatusUtil.LICENSE_DETAILS_PENDING_CCA.equals(status)
				|| ApplicationStatusUtil.LICENSE_DETAILS_SENT_TO_ADMIN.equals(status);
	}

	private String findInPrincipleApprovalDate(Long intentAppId) {
		List<ApplicationTimeLine> timeline = appTimeLineServ.getByIntentApplicationId(intentAppId);
		if (timeline == null) {
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		for (ApplicationTimeLine entry : timeline) {
			if (entry.getApplicationStatus() != null
					&& ApplicationStatusUtil.normalize(entry.getApplicationStatus())
							.equals(ApplicationStatusUtil.normalize(ApplicationStatusUtil.IN_PRINCIPLE_GRANTED))
					&& entry.getCreated() != null) {
				return format.format(entry.getCreated());
			}
		}
		return null;
	}

	private int countRootCaKeys(Long intentAppId) {
		Set<String> rootFiles = new LinkedHashSet<>();
		Set<String> certificateFiles = new LinkedHashSet<>();
		List<AppLocation> locations = appLocationService.findIntentWithoutStatusAppById(intentAppId);
		if (locations != null) {
			for (AppLocation location : locations) {
				String file = location.getCertificateFile();
				if (file == null || file.isBlank()) {
					continue;
				}
				certificateFiles.add(file.trim());
				String level = location.getCertificateLevel() == null ? "" : location.getCertificateLevel().toLowerCase();
				if (level.contains("root")) {
					rootFiles.add(file.trim());
				}
			}
		}
		if (!rootFiles.isEmpty()) {
			return rootFiles.size();
		}
		if (!certificateFiles.isEmpty()) {
			return certificateFiles.size();
		}
		List<ApplicationDocument> documents = applicationDocumentService.findIntentWithStatusAppById(intentAppId);
		int csrCount = 0;
		if (documents != null) {
			for (ApplicationDocument document : documents) {
				String title = document.getDocumentTitle() == null ? "" : document.getDocumentTitle().toLowerCase();
				if (title.contains("csr")) {
					csrCount++;
				}
			}
		}
		return csrCount;
	}

	private void applyWorkflow(ApplicationDTO application, IntentApplication app, String role) {
		LicenseAcquisitionWorkflowDTO workflow =
				licenseAcquisitionWorkflowService.buildWorkflow(app, role);
		application.setCurrentStep(workflow.getCurrentStep());
		application.setReadOnly(workflow.isReadOnly());
		application.setAllowedActions(workflow.getAllowedActions());
	}
	
}
