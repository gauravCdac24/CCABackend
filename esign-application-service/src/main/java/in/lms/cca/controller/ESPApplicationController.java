package in.lms.cca.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.utils.PdfMerger;

import in.lms.cca.dto.ClientLicenseDetailsDTO;
import in.lms.cca.dto.EKYCApprovalDocDTO;
import in.lms.cca.dto.EKYCModeClientDTO;
import in.lms.cca.dto.ESPWithEkycModeDTO;
import in.lms.cca.dto.ESignAPIVersionMasterDTO;
import in.lms.cca.dto.EsignDocumentResponseDTO;
import in.lms.cca.dto.PreviewApplicationDTO;
import in.lms.cca.dto.PreviousReviewedApplicationDTO;
import in.lms.cca.dto.RecommendedRejectionDTO;
import in.lms.cca.dto.ReviewApplicationDTO;
import in.lms.cca.dto.StepThreeDTO;
import in.lms.cca.dto.StepThreeResponseDTO;
import in.lms.cca.dto.StepTwoDTO;
import in.lms.cca.dto.StepTwoResponseDTO;
import in.lms.cca.entity.ApplicationTimeLine;
import in.lms.cca.entity.ESPPurposeEntity;
import in.lms.cca.entity.EkycMode;
import in.lms.cca.entity.EkycModeReview;
import in.lms.cca.entity.EsignAPIVersion;
import in.lms.cca.entity.EsignDocument;
import in.lms.cca.entity.EsignLicenseeApplication;
import in.lms.cca.entity.ReviewESPApplication;
import in.lms.cca.entity.ReviewedApplication;
import in.lms.cca.payload.EKYCModePayload;
import in.lms.cca.service.IApplicationTimeLineService;
import in.lms.cca.service.IClientLicenseeService;
import in.lms.cca.service.IESPPurposeService;
import in.lms.cca.service.IEkycModeReviewService;
import in.lms.cca.service.IEkycModeService;
import in.lms.cca.service.IEsignAPIVersionService;
import in.lms.cca.service.IEsignDocumentService;
import in.lms.cca.service.IEsignLicenseeApplicationService;
import in.lms.cca.service.IReviewESPApplicationService;
import in.lms.cca.service.IReviewedApplicationService;
import in.lms.cca.service.IeKYCModeClientService;
import in.lms.cca.util.api.EsignApplicationServiceAPIs;
import in.lms.cca.util.golbal.DocumentFileUtil;
import in.lms.cca.util.golbal.EncryptionUtil;
import in.lms.cca.util.golbal.RealPath;

@RestController
@RequestMapping(EsignApplicationServiceAPIs.ESIGN_APPLICATION_SERVICE_BASE_URL)
@CrossOrigin
public class ESPApplicationController {

	@Autowired
	private IeKYCModeClientService ekycModeClientServ;
	
	@Autowired
	private IApplicationTimeLineService applicationTimeServ;
	
	@Autowired
	private IEkycModeService ekycModeServ;
	
	@Autowired
	private IEsignAPIVersionService esignAPIVersionServ;
	
	@Autowired
	private IEsignDocumentService esignDocumentServ; 
	
	@Autowired
	private IEsignLicenseeApplicationService applicationServ;
	
	@Autowired
	private IReviewESPApplicationService espAppServ;
	
	@Autowired
	private IEkycModeReviewService ekycReviewServ;
	
	@Autowired
	private IReviewedApplicationService reviewAppServ;
	
	@Autowired
	private IESPPurposeService purposeServ;
	
	@Autowired
	private IClientLicenseeService clientServ;
	
	
	
	@GetMapping(EsignApplicationServiceAPIs.DOWNLOAD_APPLICATION_FORM)
	public ResponseEntity<?> downloadApplicationForm(@RequestParam("id") String username){
		try {
			
			List<ESignAPIVersionMasterDTO> apiVerList = clientServ.getAllAPIVersion();
			
			List<EsignLicenseeApplication> objApp = applicationServ.getApplicationByUsernameAndNotStatus(EncryptionUtil.decrypt(username), "Rejected", "Expired");
			
			HashMap<String, String> ekycFilesMap = new HashMap<>();
			List<String> ekycFiles = new ArrayList<>();
			
			List<EkycMode> ekycModeList = ekycModeServ.getAllEKYCModeByAppIdAndStatus(objApp.get(0).getEsignLicenseeAppId(), "Active");
			List<EsignDocument> auditReport =  esignDocumentServ.getEsignDocumentByAppIdDocumentTypeAndStatus(objApp.get(0).getEsignLicenseeAppId(), "Audit Report", "Active");
			List<EsignDocument>  cps =  esignDocumentServ.getEsignDocumentByAppIdDocumentTypeAndStatus(objApp.get(0).getEsignLicenseeAppId(), "CPS", "Active");
			List<EsignDocument>  coverLetter =  esignDocumentServ.getEsignDocumentByAppIdDocumentTypeAndStatus(objApp.get(0).getEsignLicenseeAppId(), "Cover Letter", "Active");
			List<EsignAPIVersion> everobj = esignAPIVersionServ.getEsignAPIVersionByAppIdAndStatus(objApp.get(0).getEsignLicenseeAppId(), "Active");
								
			ESPPurposeEntity purposeObj = null;
			
			
			List<ESPPurposeEntity> purposeList = purposeServ.getAllESPPurposeByStatusAndLicenseAppId(objApp.get(0).getEsignLicenseeAppId(), "Active"); 
			
			if(!purposeList.isEmpty())
				purposeObj = purposeList.get(0);
			
			List<ReviewESPApplication> respObj = espAppServ.getReviewESPApplicationByStatusAndId(objApp.get(0).getEsignLicenseeAppId(), "Active");
			
			
			if(!respObj.isEmpty()) {
				
				if(ekycModeList.isEmpty()) {
					
					List<EkycModeReview> ekycmodereview =  ekycReviewServ.getEkycModeReviewByReviewId(respObj.get(0).getEspReviewId());
					
					List<EkycMode> aobj = new ArrayList<>();
					
					for(EkycModeReview e: ekycmodereview) {
						aobj.add(e.getEkycModeId());
					}
					
					ekycModeList = aobj;
					
				}
				
				List<ReviewedApplication> reviewAppObj = reviewAppServ.getReviewedApplicationByReviewId(respObj.get(0).getEspReviewId());
				
				if(auditReport.isEmpty()) {
					
					List<EsignDocument> e = new ArrayList<>();
					e.add(reviewAppObj.get(0).getAuditReport());
					auditReport = e;
				}
				
				if(cps.isEmpty()) {
					List<EsignDocument> e = new ArrayList<>();
					e.add(reviewAppObj.get(0).getCpsDocument());
					cps = e;					
				}
				
				if(coverLetter.isEmpty()) {
					List<EsignDocument> e = new ArrayList<>();
					e.add(reviewAppObj.get(0).getCoverLetter());
					coverLetter = e;					
				}
				
				if(everobj.isEmpty()) {
					
					List<EsignAPIVersion> e = new ArrayList<>();
					e.add(reviewAppObj.get(0).getEsignMainAppId());
					everobj = e;
				}
				
				if(purposeObj == null) {
					purposeObj = reviewAppObj.get(0).getPurposeId();
				}
				
			}
			
			
			String apiSpecification = "";
			String apiVersion = "";
			
			String apiverid = everobj.get(0).getEsignAPIVerId();
			
			if(!everobj.isEmpty()) {
				
				ESignAPIVersionMasterDTO api = apiVerList.stream()
						.filter((f)->f.getEsignApiVerId() == Long.parseLong(EncryptionUtil.decrypt(apiverid)))
						.findFirst()
						.orElse(null);
				
				if(api != null) {
					apiSpecification = api.getEsignApiSpecId().getApiSpecification();
					apiVersion = api.getApiVersion();
				}
			}
			
			
			
			String modes = ekycModeList.size() > 1? "s":"";
			
			String estr = "";
			estr += "<div style=\"margin-left: 20px;\"><b> Purpose of usage</b><ul style = \"margin-left: 10px;\"><li>"+purposeObj.getPurpose();
			estr += "</li></ul><b>eKYC Mode"+modes+"</b><ul style=\"margin-left: 10px;\">";
			
			
				for(EkycMode a : ekycModeList) {
					
					if(a.getFileName() != null) {
						estr += "<li>";
						estr += a.getEkycModeTitle();
						estr += "</li>";
						
						ekycFiles.add(a.getFileName());
						ekycFilesMap.put(a.getFileName(), a.getEkycModeTitle());
					}
					
				}
				
				estr += "</ul></div>";
			
			
				String undertakingText = "Whoever makes any misrepresentation to, or suppresses any material fact from, the Controller or the Certifying Authority for obtaining any licence or Digital Signature Certificate, as the case may be, shall be punished with imprisonment for a term which may extend to two years, or with fine which may extend to one lakh rupees, or with both.";
			
			
			StringBuilder contentBuilder = new StringBuilder();
			
			contentBuilder.append("<html>");
		    contentBuilder.append("<head>");
		    contentBuilder.append("<style>");
		    contentBuilder.append("@page { margin: 50pt;}");
		    contentBuilder.append("body { font-size: 18px; font-family: \"Times New Roman\", Times, serif; } p {text-align: justify; text-justify: inter-word;} "
		    		+ "div {text-align: justify; text-justify: inter-word;} table{border-collapse: collapse;} td{padding-left: 5px;}"); 
		    contentBuilder.append("</style>");
		    contentBuilder.append("</head>");
		    contentBuilder.append("<body>");
		    
		    
		    
		    contentBuilder.append("<p><b>Application Number: </b>"+objApp.get(0).getApplicationNumber()+"</p><p></p>");
		    
		    contentBuilder.append("<br/><p><b><u>Application for eSign Service Provider</u></b></p>");
		    contentBuilder.append("<br/>");
		    contentBuilder.append("<p><b>1. Following are the purpose of usage and applicable eKYC mode"+modes+".</b></p>"+estr+"</p>");
		    contentBuilder.append("<p><b>2. eSign API Version</b></p>");
		    contentBuilder.append("<div style=\"margin-left: 20px;\">API Specification: "+apiSpecification+"</div><br />");
		    contentBuilder.append("<div style=\"margin-left: 20px;\">API Version: "+apiVersion+"</div>");
		    contentBuilder.append("<p><b>3. Undertaking</b></p>");
		    contentBuilder.append("<div style=\"margin-left: 20px;\"><input type=\"checkbox\" id=\"myCheckbox\" checked>&nbsp;"+undertakingText+"</div>");
		    contentBuilder.append("</body>");
		    contentBuilder.append("</html>");
			
		    String htmlContent = contentBuilder.toString();

		    byte[] pdfBytes;
		    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
		       
		        ConverterProperties converterProperties = new ConverterProperties();
		        HtmlConverter.convertToPdf(htmlContent, outputStream, converterProperties);
		        pdfBytes = outputStream.toByteArray();
		    } catch (Exception e) {
		        e.printStackTrace();
		        return new ResponseEntity<>("Error in downloading application form.", HttpStatus.BAD_REQUEST); 
		    }

		    
		    
		    for(String a: ekycFiles) {
		    	
		    	pdfBytes = mergeSinglePdf(pdfBytes, ekycFilesMap.get(a), a, RealPath.REAL_PATH+"//eKYCApproval").toByteArray();
		    }
		    
		    ByteArrayOutputStream coverLetterDoc = mergeSinglePdf(pdfBytes, "Cover Letter", coverLetter.get(0).getFileName(), RealPath.REAL_PATH+"//CoverLetter");
		    ByteArrayOutputStream cpsDoc = mergeSinglePdf(coverLetterDoc.toByteArray(), "CPS", cps.get(0).getFileName(), RealPath.REAL_PATH+"//CPS");
		    ByteArrayOutputStream auditReportDoc = mergeSinglePdf(cpsDoc.toByteArray(), "Audit Report", auditReport.get(0).getFileName(), RealPath.REAL_PATH+"//AuditReport");
		    	
		    ByteArrayOutputStream finalOutputStream = addPageNumber(auditReportDoc.toByteArray());

		    return ResponseEntity.ok()
		            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ApplicationForm.pdf")
		            .contentType(MediaType.APPLICATION_PDF)
		            .body(finalOutputStream.toByteArray());
			
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	
	
	
	@GetMapping(EsignApplicationServiceAPIs.DOWNLOAD_COVER_LETTER)
    public ResponseEntity<?> downloadCoverLetter(@RequestParam("id") String id, @RequestParam("pid") String pid, @RequestParam("qid") String qid) {

		
		String ekycmodes = EncryptionUtil.decrypt(id).replace("eKYC", "").replace("ekyc", "").replace("EKYC", "").replace("modes","").replace("Modes", "");
		String purpose = EncryptionUtil.decrypt(pid);
		String username = EncryptionUtil.decrypt(qid);
		
		List<ClientLicenseDetailsDTO> licList = clientServ.getAllLicenseDetailsByUsername(username);
				
		ClientLicenseDetailsDTO license = licList.stream()
										 	.filter((f)->f.getStatus().equals("Active"))
										 	.findFirst()
										 	.orElse(null);
		
		String[] ekycmodesArray = ekycmodes.split(",");
		String modes = ekycmodesArray.length > 1? "s":"";
			
		String estr = "";
		estr += "<div style=\"margin-left: 20px;\"><b> Purpose of usage</b><ul style = \"margin-left: 10px;\"><li>"+purpose;
		estr += "</li></ul><b>eKYC Mode"+modes+"</b><ul style=\"margin-left: 10px;\">";
		
		
			for(int i=0; i<ekycmodesArray.length; i++) {
				
				estr += "<li>";
				estr += ekycmodesArray[i].trim();
				estr += "</li>";
			}
			
			estr += "</ul></div>";
		
			String sno = (license != null?license.getSerialNo():"-");
			
		
		//Create Cover Letter
		StringBuilder contentBuilder = new StringBuilder();
		
		contentBuilder.append("<html>");
	    contentBuilder.append("<head>");
	    contentBuilder.append("<style>");
	    contentBuilder.append("@page { margin: 15pt 60pt 15pt 60pt;}");
	    contentBuilder.append("body { font-size: 16px; font-family: \"Times New Roman\", Times, serif; } p {text-align: justify; text-justify: inter-word;} "
	    		+ "div {text-align: justify; text-justify: inter-word;} table{border-collapse: collapse;} td{padding-left: 5px;}"); 
	    contentBuilder.append("</style>");
	    contentBuilder.append("</head>");
	    contentBuilder.append("<body>");
	    
	    //Page 1
	    
	    contentBuilder.append("<p><b>CA License No.: </b>"+sno+"</p>");
	    
	    contentBuilder.append("<p><b>Annexure – Documents to be sent for making an Application</b></p>");
	    contentBuilder.append("<p><b>Annexure 1 – Cover Letter </b></p>");
	    contentBuilder.append("<p>(To be submitted on the letterhead of the applicant agency)</p>");
	    contentBuilder.append("<div><b>The  Controller</b></div>");
	    contentBuilder.append("<div><b>Office of Controller of Certifying Authorities</b></div>");
	    contentBuilder.append("<div><b>Ministry of Communications and Information Technology</b></div>");
	    contentBuilder.append("<div><b>Electronics Niketan</b></div>");
	    contentBuilder.append("<div><b>6, CGO Complex</b></div>");
	    contentBuilder.append("<div><b>New Delhi - 110003</b></div>");
	    
	    contentBuilder.append("<br/><p><b>Subject</b>: Technical empanelment of eSign Service Provider to provide eSign related \r\n"
	    		+ "services to Application Service Providers </p>"
	    		+"<p>Sir/ Ma'am,</p>"
	    		+"<p>We, the undersigned applicant agency, have read and examined in detail the entire \r\n"
	    		+ "subject document in respect for technical empanelment of eSign Service Provider to \r\n"
	    		+ "provide eSign related services to Application Service Providers based on the following purpose of usage and applicable eKYC mode"+modes+".</p>"+estr
	    		+"<p>We hereby declare that our application is made in good faith, and the information \r\n"
	    		+ "contained in this application is true and correct to the best of our knowledge and \r\n"
	    		+ "belief. We also declare that the services which we will be providing will be in \r\n"
	    		+ "accordance with the IT Act, Rules, Regulations and guidelines issued by the Office of \r\n"
	    		+ "CCA from time to time.</p>"
	    		+"<p>We understand that our application is binding on us and that you are not bound to \r\n"
	    		+ "accept the application you receive.</p>"
	    		+"<p>Thanking you,</p>"
	    		+"<br />"
	    		+"<p>Yours faithfully,</p>"
	    		+"<p>(Signature of applicant agency authorized representative)</p>"
	    		+"<p>Printed Name & Designation</p>"
	    		+"<div>Date:</div>"
	    		+"<div>Place:</div>"
	    		+"<div>Business Address:</div>"
	    		
	    );
	    
	    contentBuilder.append("<div style=\"page-break-before: always;\"></div>");
	    
	    //Page 2
	    contentBuilder.append("<p><b>Annexure 2 – Basic Details</b></p>");
	    contentBuilder.append("<p>Interested organizations can send their application in the format as given below:</p>");
	    contentBuilder.append("<table border=\"1\">"
	    		+ "<tr><td>Organization Name</td><td style=\"width: 50%;\"></td></tr>"
	    		+ "<tr><td>Name of Director</td><td></td></tr>"
	    		+ "<tr><td>Nature of Business</td><td></td></tr>"
	    		+ "<tr><td>Web Address</td><td></td></tr>"
	    		+ "<tr><td>Office Address</td><td></td></tr>"
	    		+ "<tr><td>Location of Facilities</td><td></td></tr>"
	    		+ "<tr><td>Telephone Number(s)</td><td></td></tr>"
	    		+ "<tr><td>Fax</td><td></td></tr>"
	    		+ "<tr><td>Email</td><td></td></tr>"
	    		+ "<tr><td>Authorized Representative</td><td></td></tr>"
	    		+ "<tr><td>Date of grant of CA licence</td><td></td></tr>"
	    		+ "<tr><td>CA licence valid upto</td><td></td></tr>"
	    		+ "<tr><td colspan=\"2\"><br/>I, the undersigned, certify that the information given above is true to the best of my \r\n"
	    		+ "knowledge and belief.<p></p>"
	    		+ "<div><b>Date:</b></div>"
	    		+ "<div><b>Place:</b></div>"
	    		+ "<div><b>Signature of the Authorized Signatory:</b></div>"
	    		+"</td></tr>"
	    		+ "</table>");
	    
	    
	    
	    contentBuilder.append("</body>");
	    contentBuilder.append("</html>");
		
	    String htmlContent = contentBuilder.toString();

	    byte[] pdfBytes;
	    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
	        // Convert HTML to PDF
	        ConverterProperties converterProperties = new ConverterProperties();
	        HtmlConverter.convertToPdf(htmlContent, outputStream, converterProperties);
	        pdfBytes = outputStream.toByteArray();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>("Error in downloading cover letter.", HttpStatus.BAD_REQUEST); 
	    }

	    
	    return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=coverletter.pdf")
	            .contentType(MediaType.APPLICATION_PDF)
	            .body(pdfBytes);
    }
	
	@GetMapping(EsignApplicationServiceAPIs.GET_FIRST_STEP_DATA)
	public ResponseEntity<?> getFirstStepData(@RequestParam("id") String username){
		try {
		
			List<EsignLicenseeApplication> objApp = applicationServ.getApplicationByUsernameAndStatus(EncryptionUtil.decrypt(username), "Not Completed");
			
			if(objApp.size() == 0) {
				objApp = applicationServ.getApplicationByUsernameAndStatus(EncryptionUtil.decrypt(username), "Under Review");	
				
			}
			
			ESPPurposeEntity purposeObj = null;
			
			List<EkycMode> obj = ekycModeServ.getAllEKYCModeByAppIdAndStatus(objApp.get(0).getEsignLicenseeAppId(), "Active");
			List<ESPPurposeEntity> purposeList = purposeServ.getAllESPPurposeByStatusAndLicenseAppId(objApp.get(0).getEsignLicenseeAppId(), "Active"); 
			
			if(!purposeList.isEmpty())
				purposeObj = purposeList.get(0);
			
			if(obj.size() == 0 && objApp.get(0).getApplicationStatus().equals("Under Review")) {
				
				List<ReviewESPApplication> respObj = espAppServ.getReviewESPApplicationByStatusAndId(objApp.get(0).getEsignLicenseeAppId(), "Active");
				
				List<EkycModeReview> ekycmodereview =  ekycReviewServ.getEkycModeReviewByReviewId(respObj.get(0).getEspReviewId());
				
				List<ReviewedApplication> reviewAppObj = null;
				
				if(respObj.size()>0)
					reviewAppObj = reviewAppServ.getReviewedApplicationByReviewId(respObj.get(0).getEspReviewId());
				
				purposeObj = reviewAppObj.get(0).getPurposeId();
				
				List<EkycMode> aobj = new ArrayList<>();
				
				for(EkycModeReview e: ekycmodereview) {
					aobj.add(e.getEkycModeId());
				}
				
				obj = aobj;
				
				
			}
			
			
			EKYCModePayload modelist = new EKYCModePayload();
			
			 List<Long> ekycMode = new ArrayList<>();
			 List<String> ekycModeTitles = new ArrayList<>();
			 
			 	
			 
			 
			for(EkycMode o: obj) {
				ekycMode.add(Long.parseLong(EncryptionUtil.decrypt(o.getEkycMode())));
				ekycModeTitles.add(o.getEkycModeTitle());
			}
			
			modelist.setEkycMode(ekycMode);
			modelist.setEkycModeTitles(ekycModeTitles);
			if(purposeObj != null)
				modelist.setPurpose(purposeObj.getPurpose());
			
			return new ResponseEntity<>(modelist, HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(EsignApplicationServiceAPIs.SAVE_STEP_ONE)
    public ResponseEntity<?> saveStepOne(@RequestParam("id") String id, 
    									 @RequestParam("qid") String userName, 
    									 @RequestParam("pid") String licenseId,
    									 @RequestParam("rid") String spurpose) {

		try {
		
			String[] ekycModes = EncryptionUtil.decrypt(id).split(","); 
			String purpose = EncryptionUtil.decrypt(spurpose).trim();
			
			
			
			
			//Check if first step is completed or not
			
			List<EsignLicenseeApplication> objApp = applicationServ.getApplicationByUsernameAndStatus(EncryptionUtil.decrypt(userName), "Not Completed");
			
			if(objApp.size() == 0) {
				objApp = applicationServ.getApplicationByUsernameAndStatus(EncryptionUtil.decrypt(userName), "Under Review");
			}
			
			//Save New Application Details
			
			EsignLicenseeApplication obj = new EsignLicenseeApplication();
			
			
			if(objApp.size()>0) {
				obj = objApp.get(0);
				obj.setUpdatedBy(userName);
				obj.setUpdated(new Date());
			}else {
				
				obj.setApplicationStatus("Not Completed");
				obj.setCreatedBy(userName);
				obj.setUpdatedBy(userName);
				obj.setLicenseId(licenseId);
				obj.setUserName(EncryptionUtil.decrypt(userName));
				
			}
			
			Optional<EsignLicenseeApplication> appObj = applicationServ.addEsignLicenseeApplication(obj);
			
			
			
			if(objApp.size()==0) {
				
				applicationServ.addEsignLicenseeApplication(appObj.get());
			}
			
			//Save Purpose
			
			List<ESPPurposeEntity> plist = purposeServ.getAllESPPurposeByStatusAndLicenseAppId(appObj.get().getEsignLicenseeAppId(), "Active"); 
			
			if(plist.size() > 0) {
				ESPPurposeEntity purposeEntity = plist.get(0);
				purposeEntity.setUpdatedBy(userName);
				purposeEntity.setPurpose(purpose);
				purposeEntity.setUpdated(new Date());
				
				purposeServ.addPurpose(purposeEntity);
			}else {
				ESPPurposeEntity purposeEntity = new ESPPurposeEntity(purpose, "Active", userName, userName, appObj.get());
				purposeServ.addPurpose(purposeEntity);
			}
			
			
			//Inactive All eKYC Modes  By Application Id whose status are active
			ekycModeServ.inactiveAllEKYCModeByAppId(appObj.get().getEsignLicenseeAppId());
			
			
			List<EKYCModeClientDTO> ekycClientList = ekycModeClientServ.getAlleKYCModes();
			List<EkycMode> ekycModeList = ekycModeServ.getAllEKYCModeByAppIdAndStatus(appObj.get().getEsignLicenseeAppId(), "Inactive");
			
			
			
			
			//Add Selected eKYC Modes
			
			for(String s: ekycModes) {
				
			
				EkycMode matchedEkycMode = ekycModeList.stream()
						.filter(e -> EncryptionUtil.decrypt(e.getEkycMode()).equals(s))
						.findFirst()
						.orElse(null);
				
				if(matchedEkycMode != null) {
					
					if(matchedEkycMode.getStatus().equals("Inactive")) {
						
						EKYCModeClientDTO matchedClient = ekycClientList.stream()
					            .filter(client -> client.getEkycModeId() == Long.parseLong(s)) 
					            .findFirst()
					            .orElse(null);
						
						matchedEkycMode.setUpdatedBy(userName);
						matchedEkycMode.setEkycMode(EncryptionUtil.encrypt(s));
						matchedEkycMode.setEsignLicenseeAppId(appObj.get());
						matchedEkycMode.setEkycModeTitle(matchedClient.getEkycModeTitle());
						matchedEkycMode.setIsApprovalRequired(matchedClient.getIsApprovalRequired());
						matchedEkycMode.setStatus("Active");
						
						ekycModeServ.addekycMode(matchedEkycMode);
						
						
					}
					
				}else {
				
				EKYCModeClientDTO matchedClient = ekycClientList.stream()
			            .filter(client -> client.getEkycModeId() == Long.parseLong(s)) 
			            .findFirst()
			            .orElse(null);
				
					EkycMode objMode = new EkycMode();
					objMode.setCreatedBy(userName);
					objMode.setUpdatedBy(userName);
					objMode.setEkycMode(EncryptionUtil.encrypt(s));
					objMode.setEsignLicenseeAppId(appObj.get());
					objMode.setEkycModeTitle(matchedClient.getEkycModeTitle());
					objMode.setStatus("Active");
					objMode.setIsApprovalRequired(matchedClient.getIsApprovalRequired());
					
					ekycModeServ.addekycMode(objMode);
					
				
				}
				
			}
			
			//Application Timeline
			
				if(objApp.size() == 0) {
				
				ApplicationTimeLine apptimelineObj = new ApplicationTimeLine();
				
				apptimelineObj.setApplicationStatus("Not Completed");
				apptimelineObj.setEsignLicenseeAppId(appObj.get());
				apptimelineObj.setInitiatedBy(EncryptionUtil.decrypt(userName));
				
				applicationTimeServ.addApplicationTimeLine(apptimelineObj);
			
			}
			
			return new ResponseEntity<>(HttpStatus.OK);
			
		}catch(Exception e) {
		
			return new ResponseEntity<>("Error in saving the details, please try again later.", HttpStatus.BAD_REQUEST);
			
		}
		
	}
	
	@GetMapping(EsignApplicationServiceAPIs.GET_SELECTED_EKYC_MODES)
	public ResponseEntity<?> getSelectedEKYCMode(@RequestParam("id") String username){
		try {
		
			List<EsignLicenseeApplication> objApp = applicationServ.getApplicationByUsernameAndStatus(EncryptionUtil.decrypt(username), "Not Completed");
			
			if(objApp.size() == 0) {
				objApp = applicationServ.getApplicationByUsernameAndStatus(EncryptionUtil.decrypt(username), "Under Review");
			}
			
			List<EkycMode> ekycModeList = ekycModeServ.getAllEKYCModeByAppIdAndStatus(objApp.get(0).getEsignLicenseeAppId(), "Active");
			
			
			if(ekycModeList.size() == 0 && objApp.get(0).getApplicationStatus().equals("Under Review")) {
				
				List<ReviewESPApplication> respObj = espAppServ.getReviewESPApplicationByStatusAndId(objApp.get(0).getEsignLicenseeAppId(), "Active");;
				
				List<EkycModeReview> ekycmodereview =  ekycReviewServ.getEkycModeReviewByReviewId(respObj.get(0).getEspReviewId());
				
				List<EkycMode> aobj = new ArrayList<>();
				
				for(EkycModeReview e: ekycmodereview) {
					aobj.add(e.getEkycModeId());
				}
				
				ekycModeList = aobj;
				
				
			}
			
			
			//Filter list for which approval is required
			List<EkycMode> ekycModeListApproval = ekycModeList.stream()
											.filter(e->e.getIsApprovalRequired() == true)
											.collect(Collectors.toList());
			
			return new ResponseEntity<>(ekycModeListApproval, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(EsignApplicationServiceAPIs.SAVE_STEP_TWO)
	public ResponseEntity<?> saveStepTwo(@ModelAttribute StepTwoDTO Obj){
	    
		
		List<EsignLicenseeApplication> objApp = applicationServ.getApplicationByUsernameAndStatus(EncryptionUtil.decrypt(Obj.getUserName()), "Not Completed");
		
		if(objApp.size() == 0) {
			objApp = applicationServ.getApplicationByUsernameAndStatus(EncryptionUtil.decrypt(Obj.getUserName()), "Under Review");
		}
		
		
		List<ReviewESPApplication> respObj = espAppServ.getReviewESPApplicationByStatusAndId(objApp.get(0).getEsignLicenseeAppId(), "Active");
		List<ReviewedApplication> reviewAppObj = null;
		
		List<EkycMode> rekycobj = new ArrayList<>();
		
		if(!respObj.isEmpty()) {
			
			reviewAppObj = reviewAppServ.getReviewedApplicationByReviewId(respObj.get(0).getEspReviewId());
			
			List<EkycModeReview> ekycmodereview =  ekycReviewServ.getEkycModeReviewByReviewId(respObj.get(0).getEspReviewId());
			
			for(EkycModeReview e: ekycmodereview) {
				rekycobj.add(e.getEkycModeId());
			}
			
			
			
			
		}
		
		
		//Validation
		if((Obj.getCoverLetterDoc().getFile() == null || Obj.getCoverLetterDoc().getFile().isEmpty()) && 
				(Obj.getCoverLetterDoc().getFileName() == null || Obj.getCoverLetterDoc().getFileName().isEmpty())) {
			
			 return new ResponseEntity<>("Please upload Cover Letter.", HttpStatus.BAD_REQUEST);
		}else if((Obj.getCoverLetterDoc().getFile() == null || Obj.getCoverLetterDoc().getFile().isEmpty()) && 
				(Obj.getCoverLetterDoc().getFileName() != null || !Obj.getCoverLetterDoc().getFileName().isEmpty())) {
			
			if(!respObj.isEmpty()  && respObj.get(0).getCoverLetter() && reviewAppObj.get(0).getCoverLetter().getFileName().equals(Obj.getCoverLetterDoc().getFileName())) {
				return new ResponseEntity<>("Please reupload the Cover Letter, as it has been marked by the reviewer for reuploading.", HttpStatus.BAD_REQUEST);
			}
		}
		
		if(Obj.geteKYCApprovalDoc() != null && !Obj.geteKYCApprovalDoc().isEmpty()) {
		
			for(EKYCApprovalDocDTO e: Obj.geteKYCApprovalDoc()) {
				
				
				
				if((e.getFile() == null || e.getFile().isEmpty()) && (e.getFileName() == null || e.getFileName().isEmpty())) {
					return new ResponseEntity<>("Please upload Required Approval.", HttpStatus.BAD_REQUEST);
				}else if(!respObj.isEmpty() && respObj.get(0).getEkycMode() && (e.getFile() == null || e.getFile().isEmpty()) && (e.getFileName() != null || !e.getFileName().isEmpty())) {
					
					EkycMode matchedEkycMode = rekycobj.stream()
				            .filter(client -> Long.parseLong(EncryptionUtil.decrypt(client.getEkycMode())) == Long.parseLong(EncryptionUtil.decrypt(e.getId()))) 
				            .findFirst()
				            .orElse(null);
					
					if(matchedEkycMode != null && matchedEkycMode.getFileName().equals(e.getFileName())) {
						return new ResponseEntity<>("Please reupload "+matchedEkycMode.getEkycModeTitle()+" approval, as it has been marked by the reviewer for reuploading.", HttpStatus.BAD_REQUEST);
					}
				}
				
			}
		}
		
		try {
		
			
			
			
			if (objApp == null || objApp.isEmpty()) {
	            return new ResponseEntity<>("No application found for the user.", HttpStatus.BAD_REQUEST);
	        }
			
			Random rand = new Random();
			Integer rnum = rand.nextInt(1000);
				
			
			// Save Cover Letter
			
			if(Obj.getCoverLetterDoc().getFile() != null && !Obj.getCoverLetterDoc().getFile().isEmpty()) {
			
			List<EsignDocument> coverLetter =  esignDocumentServ.getEsignDocumentByAppIdDocumentTypeAndStatus(objApp.get(0).getEsignLicenseeAppId(), "Cover Letter", "Active");
			
			EsignDocument edoc = new EsignDocument();
			
			if(!coverLetter.isEmpty()) {
				
				DocumentFileUtil.deleteFile(coverLetter.get(0).getFileName(), "CoverLetter");				
				edoc.setUpdatedBy(Obj.getUserName());
				edoc.seteSignDocId(coverLetter.get(0).geteSignDocId());
				edoc.setCreated(coverLetter.get(0).getCreated());
				edoc.setUpdated(new Date());
				edoc.setCreatedBy(coverLetter.get(0).getCreatedBy());
			}else {
				edoc.setCreatedBy(Obj.getUserName());
				edoc.setUpdatedBy(Obj.getUserName());
			}
			
			String filename = DocumentFileUtil.saveFile(Obj.getCoverLetterDoc().getFile(), "CoverLetter", rnum.toString(), "CoverLetter");
			
			edoc.setEsignDocType("Cover Letter");
			edoc.setEsignLicenseeAppId(objApp.get(0));
			edoc.setFileName(filename);
			edoc.setStatus("Active");
			
			esignDocumentServ.addEsignDocument(edoc);
			
			}else if(Obj.getCoverLetterDoc().getFileName() != null){
				
				EsignDocument coverLetter =  esignDocumentServ.getEsignDocumentByFileName(Obj.getCoverLetterDoc().getFileName());
				coverLetter.setStatus("Active");
				coverLetter.setUpdatedBy(Obj.getUserName());
				coverLetter.setUpdated(new Date());
				esignDocumentServ.addEsignDocument(coverLetter);
				
			}
			
			
			List<EkycMode> ekycMode = ekycModeServ.getAllEKYCModeByAppIdAndStatus(objApp.get(0).getEsignLicenseeAppId(), "Active");
			
			if(Obj.geteKYCApprovalDoc() != null && !Obj.geteKYCApprovalDoc().isEmpty()) {
				for(EKYCApprovalDocDTO e: Obj.geteKYCApprovalDoc()) {
					
					if(e.getFile() != null && !e.getFile().isEmpty()) {
					
						EkycMode ekycModeObj = ekycMode.stream()
												.filter((item)->EncryptionUtil.decrypt(item.getEkycMode()).equals(EncryptionUtil.decrypt(e.getId())))
												.findFirst()
												.orElse(null);
						
						if(ekycModeObj != null) {
											
							Integer rnums = rand.nextInt(1000);
							
							
							if(ekycModeObj.getFileName() != null && !ekycModeObj.getFileName().isEmpty()) {
								DocumentFileUtil.deleteFile(ekycModeObj.getFileName(), "eKYCApproval");
							}
							
							String ekycModeFileName = DocumentFileUtil.saveFile(e.getFile(), "eKYCApproval", rnums.toString(), "eKYCApproval");
							ekycModeObj.setUpdatedBy(Obj.getUserName());
							ekycModeObj.setUpdated(new Date());
							ekycModeObj.setFileName(ekycModeFileName);
							ekycModeServ.addekycMode(ekycModeObj);
						}
					}else if(e.getFileName() != null){
						
						EkycMode ekycModeObj = ekycMode.stream()
								.filter((item)->item.getFileName() != null && item.getFileName().equals(e.getFileName()))
								.findFirst()
								.orElse(null);
						
						if(ekycModeObj != null) {
							
							ekycModeObj.setStatus("Active");
							ekycModeObj.setUpdated(new Date());
							ekycModeObj.setUpdatedBy(Obj.getUserName());
							ekycModeServ.addekycMode(ekycModeObj);
						}
						
					}
					
					
				}
			}
			
		
			return new ResponseEntity<>(HttpStatus.OK);
			
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error in saving the details, please try again later.", HttpStatus.BAD_REQUEST);
			
		}
	   
		

	}
	
	
	@GetMapping(EsignApplicationServiceAPIs.GET_SECOND_STEP_DATA)
	public ResponseEntity<?> getStepTwoData(@RequestParam("id") String username){
		try {
		
			List<EsignLicenseeApplication> objApp = applicationServ.getApplicationByUsernameAndStatus(EncryptionUtil.decrypt(username), "Not Completed");
			
			if(objApp.size() == 0) {
				objApp = applicationServ.getApplicationByUsernameAndStatus(EncryptionUtil.decrypt(username), "Under Review");
			}
			
			List<EsignDocument> coverLetter =  esignDocumentServ.getEsignDocumentByAppIdDocumentTypeAndStatus(objApp.get(0).getEsignLicenseeAppId(), "Cover Letter", "Active");
			
			List<ReviewESPApplication> respObj = espAppServ.getReviewESPApplicationByStatusAndId(objApp.get(0).getEsignLicenseeAppId(), "Active");
			List<ReviewedApplication> reviewAppObj = null;
			
			if(respObj.size()>0)
				reviewAppObj = reviewAppServ.getReviewedApplicationByReviewId(respObj.get(0).getEspReviewId());
			
			
			if(coverLetter.isEmpty()) {
				List<EsignDocument> e = new ArrayList<>();
				e.add(reviewAppObj.get(0).getCoverLetter());
				
				coverLetter = e;
			}
			
			List<EkycMode> ekycMode = ekycModeServ.getAllEKYCModeByAppIdAndStatusAndRequired(objApp.get(0).getEsignLicenseeAppId(), "Active", true);
			
			List<EkycMode> aobj = new ArrayList<>();
		
			if(objApp.get(0).getApplicationStatus().equals("Under Review")) {
				
				List<EkycModeReview> ekycmodereview =  ekycReviewServ.getEkycModeReviewByReviewId(respObj.get(0).getEspReviewId());
				
				for(EkycMode e: ekycMode) {
					
					if(e.getFileName() == null) {
						
						EkycModeReview matched = ekycmodereview.stream()
												 .filter(f->Long.parseLong(EncryptionUtil.decrypt(f.getEkycModeId().getEkycMode())) == Long.parseLong(EncryptionUtil.decrypt(e.getEkycMode())))
												 .findFirst()
												 .orElse(null);
						
						if(matched != null) {
							aobj.add(matched.getEkycModeId());
						}else {
							aobj.add(e);
						}
						
					}else {
						aobj.add(e);
					}
					
				}
			
				ekycMode = aobj;
				
			}
			
			
			
			
			
			StepTwoResponseDTO response = new StepTwoResponseDTO();
			
			if(coverLetter.size()>0)
				response.setEsignDocument(coverLetter.get(0));
			if(ekycMode.size()>0)
				response.seteKYCMode(ekycMode);
			
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(EsignApplicationServiceAPIs.DOWNLOAD_STEP_TWO_DOCUMENT)
	public ResponseEntity<?> getStepTwoDocument(@RequestParam("id") String uid, @RequestParam("pid") String type)
	{
		try {
			
			if (uid == null || uid.isEmpty() || type == null || type.isEmpty()) {
				return new ResponseEntity<>("Error in downloading file.", HttpStatus.BAD_REQUEST);
	        }
			
			String id = EncryptionUtil.decrypt(uid);
			String docType = EncryptionUtil.decrypt(type);
			
			
			
	        if (id == null || docType == null) {
	        	return new ResponseEntity<>("Error in downloading file.", HttpStatus.BAD_REQUEST);
	        }
			
			String filename = "";
			String filepath = "";
			
			if(docType.equals("CoverLetter")) {
				EsignDocument coverLetter =  esignDocumentServ.getEsignDocumentById(Long.parseLong(id));
				
				 if (coverLetter == null) {
					 return new ResponseEntity<>("Error in downloading file.", HttpStatus.BAD_REQUEST);
		            }
				
					filename = coverLetter.getFileName();
					filepath = "CoverLetter";
				
			}
			
			if(docType.equals("eKYCApproval")) {
				EkycMode ekycObj = ekycModeServ.getEkycModeById(Long.parseLong(id));
				if (ekycObj == null) {
					return new ResponseEntity<>("Error in downloading file.", HttpStatus.BAD_REQUEST);
	            }
				filename = ekycObj.getFileName();
				filepath = "eKYCApproval";
			}
			
		           
		                
		                Path filePath = Paths.get(RealPath.REAL_PATH, filepath).resolve(filename).normalize();
		                
		                
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
	
	
	
	@PostMapping(EsignApplicationServiceAPIs.SAVE_STEP_THREE)
	public ResponseEntity<?> saveStepThree(@ModelAttribute StepThreeDTO Obj){
	    
		List<EsignLicenseeApplication> objApp = applicationServ.getApplicationByUsernameAndStatus(EncryptionUtil.decrypt(Obj.getUserName()), "Not Completed");
		
		if(objApp.size() == 0) {
			objApp = applicationServ.getApplicationByUsernameAndStatus(EncryptionUtil.decrypt(Obj.getUserName()), "Under Review");
		}
		
		List<ReviewESPApplication> respObj = espAppServ.getReviewESPApplicationByStatusAndId(objApp.get(0).getEsignLicenseeAppId(), "Active");
		
		List<ReviewedApplication> reviewAppObj = null;
		
		if(!respObj.isEmpty()) {
			reviewAppObj = reviewAppServ.getReviewedApplicationByReviewId(respObj.get(0).getEspReviewId());
		}
		
		//Validation
		
		if(Obj.getEsignApiSpecId() == null || Obj.getEsignApiSpecId().isEmpty())
			return new ResponseEntity<>("Please select eSign API Specification.", HttpStatus.BAD_REQUEST);
		
		
		if(Obj.getApiVersionId() == null || Obj.getApiVersionId().isEmpty())
			return new ResponseEntity<>("Please select eSign API Version.", HttpStatus.BAD_REQUEST);
		
		
		if((Obj.getAuditReportFile() == null || Obj.getAuditReportFile().isEmpty()) && 
				(Obj.getAuditReportFileName() == null || Obj.getAuditReportFileName().isEmpty())) {
			
			 return new ResponseEntity<>("Please upload Audit Report.", HttpStatus.BAD_REQUEST);
			 
		}else if((Obj.getAuditReportFile() == null || Obj.getAuditReportFile().isEmpty()) && 
				(Obj.getAuditReportFileName() != null || !Obj.getAuditReportFileName().isEmpty())) {
			
			if(!respObj.isEmpty() && respObj.get(0).getAuditReport() && reviewAppObj.get(0).getAuditReport().getFileName().equals(Obj.getAuditReportFileName())) {
				return new ResponseEntity<>("Please reupload the Audit Report, as it has been marked by the reviewer for reuploading.", HttpStatus.BAD_REQUEST);
			}
		}
		
		if((Obj.getCpsFile() == null || Obj.getCpsFile().isEmpty()) && 
				(Obj.getCpsFileName() == null || Obj.getCpsFileName().isEmpty())) {
			
			 return new ResponseEntity<>("Please upload CPS document.", HttpStatus.BAD_REQUEST);
		}else if((Obj.getCpsFile() == null || Obj.getCpsFile().isEmpty()) && 
				(Obj.getCpsFileName() != null || !Obj.getCpsFileName().isEmpty())) {
			
			if(!respObj.isEmpty() && respObj.get(0).getCpsDocument() && reviewAppObj.get(0).getCpsDocument().getFileName().equals(Obj.getCpsFileName())) {
				return new ResponseEntity<>("Please reupload the CPS Document, as it has been marked by the reviewer for reuploading.", HttpStatus.BAD_REQUEST);
			}
		}
		
		
		
		
		try {
		
			
			
			
			if (objApp == null || objApp.isEmpty()) {
	            return new ResponseEntity<>("No application found for the user.", HttpStatus.BAD_REQUEST);
	        }
			
			Random rand = new Random();
			Integer rnum = rand.nextInt(1000);
			
			// Save Audit Report
			
			if(Obj.getAuditReportFile() != null && !Obj.getAuditReportFile().isEmpty()) {
			
			List<EsignDocument> auditReport =  esignDocumentServ.getEsignDocumentByAppIdDocumentTypeAndStatus(objApp.get(0).getEsignLicenseeAppId(), "Audit Report", "Active");
			EsignDocument edoc = new EsignDocument();
			
			if(!auditReport.isEmpty()) {
				
				DocumentFileUtil.deleteFile(auditReport.get(0).getFileName(), "AuditReport");				
				edoc.setUpdatedBy(Obj.getUserName());
				edoc.seteSignDocId(auditReport.get(0).geteSignDocId());
				edoc.setCreated(auditReport.get(0).getCreated());
				edoc.setUpdated(new Date());
				edoc.setCreatedBy(auditReport.get(0).getCreatedBy());
			}else {
				edoc.setCreatedBy(Obj.getUserName());
				edoc.setUpdatedBy(Obj.getUserName());
			}
			
			String filename = DocumentFileUtil.saveFile(Obj.getAuditReportFile(), "AuditReport", rnum.toString(), "AuditReport");
			
			edoc.setEsignDocType("Audit Report");
			edoc.setEsignLicenseeAppId(objApp.get(0));
			edoc.setFileName(filename);
			edoc.setStatus("Active");
			
			esignDocumentServ.addEsignDocument(edoc);
			
			}else if(Obj.getAuditReportFileName() != null){
				
				EsignDocument auditfiles =  esignDocumentServ.getEsignDocumentByFileName(Obj.getAuditReportFileName());
				auditfiles.setStatus("Active");
				auditfiles.setUpdatedBy(Obj.getUserName());
				auditfiles.setUpdated(new Date());
				esignDocumentServ.addEsignDocument(auditfiles);
				
			}
			
			//Save CPS Document
			
			if(Obj.getCpsFile() != null && !Obj.getCpsFile().isEmpty()) {
				
				List<EsignDocument> cpsdocuemnt =  esignDocumentServ.getEsignDocumentByAppIdDocumentTypeAndStatus(objApp.get(0).getEsignLicenseeAppId(), "CPS", "Active");
				EsignDocument edoc = new EsignDocument();
				
				if(!cpsdocuemnt.isEmpty()) {
					
					DocumentFileUtil.deleteFile(cpsdocuemnt.get(0).getFileName(), "CPS");				
					edoc.setUpdatedBy(Obj.getUserName());
					edoc.seteSignDocId(cpsdocuemnt.get(0).geteSignDocId());
					edoc.setCreated(cpsdocuemnt.get(0).getCreated());
					edoc.setUpdated(new Date());
					edoc.setCreatedBy(cpsdocuemnt.get(0).getCreatedBy());
				}else {
					edoc.setCreatedBy(Obj.getUserName());
					edoc.setUpdatedBy(Obj.getUserName());
				}
				
				String filename = DocumentFileUtil.saveFile(Obj.getCpsFile(), "CPS", rnum.toString(), "CPS");
				
				edoc.setEsignDocType("CPS");
				edoc.setEsignLicenseeAppId(objApp.get(0));
				edoc.setFileName(filename);
				edoc.setStatus("Active");
				
				esignDocumentServ.addEsignDocument(edoc);
				
				}else if(Obj.getCpsFileName() != null){
					
					EsignDocument cpsdocs =  esignDocumentServ.getEsignDocumentByFileName(Obj.getCpsFileName());
					cpsdocs.setStatus("Active");
					cpsdocs.setUpdatedBy(Obj.getUserName());
					cpsdocs.setUpdated(new Date());
					esignDocumentServ.addEsignDocument(cpsdocs);
					
				}
			
			//Add API Specification and API Version
			
			List<EsignAPIVersion> everobj = esignAPIVersionServ.getEsignAPIVersionByAppIdAndStatus(objApp.get(0).getEsignLicenseeAppId(), "Active");
			
			EsignAPIVersion apiVerObj = new EsignAPIVersion();
			if(!everobj.isEmpty()) {
				apiVerObj.setUpdatedBy(Obj.getUserName());
				apiVerObj.setEsignMainAppId(everobj.get(0).getEsignMainAppId());
				apiVerObj.setCreated(everobj.get(0).getCreated());
				apiVerObj.setUpdated(new Date());
				apiVerObj.setCreatedBy(everobj.get(0).getCreatedBy());
			}else {
				apiVerObj.setCreatedBy(Obj.getUserName());
				apiVerObj.setUpdatedBy(Obj.getUserName());
			}
			
			apiVerObj.setEsignAPISpecId(Obj.getEsignApiSpecId());
			apiVerObj.setEsignAPIVerId(Obj.getApiVersionId());
			apiVerObj.setEsignLicenseeAppId(objApp.get(0));
			apiVerObj.setStatus("Active");
			
			esignAPIVersionServ.addEsignAPIVersion(apiVerObj);
			
			return new ResponseEntity<>(HttpStatus.OK);
			
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error in saving the details, please try again later.", HttpStatus.BAD_REQUEST);
			
		}
	   

	}
	
	
	@GetMapping(EsignApplicationServiceAPIs.GET_THIRD_STEP_DATA)
	public ResponseEntity<?> getStepThreeData(@RequestParam("id") String username){
		try {
			
			List<EsignLicenseeApplication> objApp = applicationServ.getApplicationByUsernameAndStatus(EncryptionUtil.decrypt(username), "Not Completed");
			
			if(objApp.size() == 0) {
				objApp = applicationServ.getApplicationByUsernameAndStatus(EncryptionUtil.decrypt(username), "Under Review");
				
			}
			
			List<ReviewESPApplication> respObj = espAppServ.getReviewESPApplicationByStatusAndId(objApp.get(0).getEsignLicenseeAppId(), "Active");
			List<ReviewedApplication> reviewAppObj = null;
			
			
			
			
			if(!respObj.isEmpty()) {
				reviewAppObj = reviewAppServ.getReviewedApplicationByReviewId(respObj.get(0).getEspReviewId());
			}
			
			List<EsignDocument> auditReport =  esignDocumentServ.getEsignDocumentByAppIdDocumentTypeAndStatus(objApp.get(0).getEsignLicenseeAppId(), "Audit Report", "Active");
			List<EsignDocument>  cps =  esignDocumentServ.getEsignDocumentByAppIdDocumentTypeAndStatus(objApp.get(0).getEsignLicenseeAppId(), "CPS", "Active");
			List<EsignAPIVersion> everobj = esignAPIVersionServ.getEsignAPIVersionByAppIdAndStatus(objApp.get(0).getEsignLicenseeAppId(), "Active");
			
			if(objApp.get(0).getApplicationStatus().equals("Under Review")) {
				
				if(auditReport.isEmpty()) {
					
					List<EsignDocument> e = new ArrayList<>();
					e.add(reviewAppObj.get(0).getAuditReport());
					auditReport = e;
				}
				
				if(cps.isEmpty()) {
					List<EsignDocument> e = new ArrayList<>();
					e.add(reviewAppObj.get(0).getCpsDocument());
					cps = e;					
				}
				
				if(everobj.isEmpty()) {
					
					List<EsignAPIVersion> e = new ArrayList<>();
					e.add(reviewAppObj.get(0).getEsignMainAppId());
					everobj = e;
				}
				
				
			}
			
			EsignDocumentResponseDTO auditReportDoc = new EsignDocumentResponseDTO();
			EsignDocumentResponseDTO cpsDoc = new EsignDocumentResponseDTO();
			
			auditReportDoc.setEsignDocId(auditReport.get(0).geteSignDocId().toString());
			auditReportDoc.setFileName(auditReport.get(0).getFileName());
			
			cpsDoc.setEsignDocId(cps.get(0).geteSignDocId().toString());
			cpsDoc.setFileName(cps.get(0).getFileName());
			
		
			StepThreeResponseDTO response = new StepThreeResponseDTO();
			response.setApiVersionId(everobj.get(0).getEsignAPIVerId());
			response.setEsignApiSpecId(everobj.get(0).getEsignAPISpecId());
			response.setCps(cpsDoc);
			response.setAuditReport(auditReportDoc);
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(EsignApplicationServiceAPIs.DOWNLOAD_STEP_THREE_DOCUMENT)
	public ResponseEntity<?> getStepThreeDocument(@RequestParam("id") String uid)
	{
		try {
			
			if (uid == null || uid.isEmpty()) {
				return new ResponseEntity<>("Error in downloading file.", HttpStatus.BAD_REQUEST);
	        }
			
			String id = EncryptionUtil.decrypt(uid);
			
			
	        if (id == null) {
	        	return new ResponseEntity<>("Error in downloading file.", HttpStatus.BAD_REQUEST);
	        }
			
			String filename = "";
			String filepath = "";
			
			EsignDocument document =  esignDocumentServ.getEsignDocumentById(Long.parseLong(id));
					           
			filename = document.getFileName();
			
			if(document.getEsignDocType().equals("CPS"))
				filepath = "CPS";
			else if(document.getEsignDocType().equals("Audit Report"))
				filepath="AuditReport";
				
			
		                
		                Path filePath = Paths.get(RealPath.REAL_PATH, filepath).resolve(filename).normalize();
		                
		                
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
	
	
	@GetMapping(EsignApplicationServiceAPIs.GET_PREVIEW)
	public ResponseEntity<?> getPreview(@RequestParam("id") String username){
		try {
			
			
		
			List<EsignLicenseeApplication> objApp = applicationServ.getApplicationByUsernameAndNotStatus(EncryptionUtil.decrypt(username), "Rejected", "Expired");
			
			
			
			List<EkycMode> ekycModeList = ekycModeServ.getAllEKYCModeByAppIdAndStatus(objApp.get(0).getEsignLicenseeAppId(), "Active");
			List<EsignDocument> auditReport =  esignDocumentServ.getEsignDocumentByAppIdDocumentTypeAndStatus(objApp.get(0).getEsignLicenseeAppId(), "Audit Report", "Active");
			List<EsignDocument>  cps =  esignDocumentServ.getEsignDocumentByAppIdDocumentTypeAndStatus(objApp.get(0).getEsignLicenseeAppId(), "CPS", "Active");
			List<EsignDocument>  coverLetter =  esignDocumentServ.getEsignDocumentByAppIdDocumentTypeAndStatus(objApp.get(0).getEsignLicenseeAppId(), "Cover Letter", "Active");
			List<EsignAPIVersion> everobj = esignAPIVersionServ.getEsignAPIVersionByAppIdAndStatus(objApp.get(0).getEsignLicenseeAppId(), "Active");
								
			ESPPurposeEntity purposeObj = null;
			
			
			List<ESPPurposeEntity> purposeList = purposeServ.getAllESPPurposeByStatusAndLicenseAppId(objApp.get(0).getEsignLicenseeAppId(), "Active"); 
			
			if(!purposeList.isEmpty())
				purposeObj = purposeList.get(0);
			
			List<ReviewESPApplication> respObj = espAppServ.getReviewESPApplicationByStatusAndId(objApp.get(0).getEsignLicenseeAppId(), "Active");
			
			
			if(!respObj.isEmpty()) {
				
				if(ekycModeList.isEmpty()) {
					
					List<EkycModeReview> ekycmodereview =  ekycReviewServ.getEkycModeReviewByReviewId(respObj.get(0).getEspReviewId());
					
					List<EkycMode> aobj = new ArrayList<>();
					
					for(EkycModeReview e: ekycmodereview) {
						aobj.add(e.getEkycModeId());
					}
					
					ekycModeList = aobj;
					
				}
				
				List<ReviewedApplication> reviewAppObj = reviewAppServ.getReviewedApplicationByReviewId(respObj.get(0).getEspReviewId());
				
				if(auditReport.isEmpty()) {
					
					List<EsignDocument> e = new ArrayList<>();
					e.add(reviewAppObj.get(0).getAuditReport());
					auditReport = e;
				}
				
				if(cps.isEmpty()) {
					List<EsignDocument> e = new ArrayList<>();
					e.add(reviewAppObj.get(0).getCpsDocument());
					cps = e;					
				}
				
				if(coverLetter.isEmpty()) {
					List<EsignDocument> e = new ArrayList<>();
					e.add(reviewAppObj.get(0).getCoverLetter());
					coverLetter = e;					
				}
				
				if(everobj.isEmpty()) {
					
					List<EsignAPIVersion> e = new ArrayList<>();
					e.add(reviewAppObj.get(0).getEsignMainAppId());
					everobj = e;
				}
				
				if(purposeObj == null) {
					purposeObj = reviewAppObj.get(0).getPurposeId();
				}
				
			}
			
			
			PreviewApplicationDTO response = new PreviewApplicationDTO();
			
			if(!auditReport.isEmpty())
				response.setAuditReportId(EncryptionUtil.encrypt(auditReport.get(0).geteSignDocId().toString()));
			else
				response.setAuditReportId(null);
			
			if(!coverLetter.isEmpty())
				response.setCoverLetterId(EncryptionUtil.encrypt(coverLetter.get(0).geteSignDocId().toString()));
			else
				response.setCoverLetterId(null);
			if(!cps.isEmpty())
				response.setCpsDocumentId(EncryptionUtil.encrypt(cps.get(0).geteSignDocId().toString()));
			else
				response.setCpsDocumentId(null);
			if(!ekycModeList.isEmpty())	
				response.setEkycModes(ekycModeList);
			else
				response.setEkycModes(null);
			if(!everobj.isEmpty())
				response.setEsignAPIVersion(everobj.get(0));
			else
				response.setEsignAPIVersion(null);
			
			if(purposeObj != null) {
				response.setPurpose(purposeObj.getPurpose());
			}else {
				response.setPurpose(null);
			}
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	@GetMapping(EsignApplicationServiceAPIs.GET_PREVIEW_BY_ESP_APPLICATION_ID)
	public ResponseEntity<?> getPreviewById(@RequestParam("id") String licenseid){
		try {
			
			
		
			EsignLicenseeApplication objApp = applicationServ.getApplicationById(Long.parseLong(EncryptionUtil.decrypt(licenseid)));
			
			
			
			List<EkycMode> ekycModeList = ekycModeServ.getAllEKYCModeByAppIdAndStatus(objApp.getEsignLicenseeAppId(), "Active");
			List<EsignDocument> auditReport =  esignDocumentServ.getEsignDocumentByAppIdDocumentTypeAndStatus(objApp.getEsignLicenseeAppId(), "Audit Report", "Active");
			List<EsignDocument>  cps =  esignDocumentServ.getEsignDocumentByAppIdDocumentTypeAndStatus(objApp.getEsignLicenseeAppId(), "CPS", "Active");
			List<EsignDocument>  coverLetter =  esignDocumentServ.getEsignDocumentByAppIdDocumentTypeAndStatus(objApp.getEsignLicenseeAppId(), "Cover Letter", "Active");
			List<EsignAPIVersion> everobj = esignAPIVersionServ.getEsignAPIVersionByAppIdAndStatus(objApp.getEsignLicenseeAppId(), "Active");
			
			
			List<ReviewESPApplication> respObj = espAppServ.getReviewESPApplicationByStatusAndId(objApp.getEsignLicenseeAppId(), "Active");
			
			ESPPurposeEntity purposeObj = null;
			
			
			List<ESPPurposeEntity> purposeList = purposeServ.getAllESPPurposeByStatusAndLicenseAppId(objApp.getEsignLicenseeAppId(), "Active"); 
			
			if(!purposeList.isEmpty())
				purposeObj = purposeList.get(0);
			
			if(!respObj.isEmpty()) {
				
				if(ekycModeList.isEmpty()) {
					
					List<EkycModeReview> ekycmodereview =  ekycReviewServ.getEkycModeReviewByReviewId(respObj.get(0).getEspReviewId());
					
					List<EkycMode> aobj = new ArrayList<>();
					
					for(EkycModeReview e: ekycmodereview) {
						aobj.add(e.getEkycModeId());
					}
					
					ekycModeList = aobj;
					
				}
				
				List<ReviewedApplication> reviewAppObj = reviewAppServ.getReviewedApplicationByReviewId(respObj.get(0).getEspReviewId());
				
				if(auditReport.isEmpty()) {
					
					List<EsignDocument> e = new ArrayList<>();
					e.add(reviewAppObj.get(0).getAuditReport());
					auditReport = e;
				}
				
				if(cps.isEmpty()) {
					List<EsignDocument> e = new ArrayList<>();
					e.add(reviewAppObj.get(0).getCpsDocument());
					cps = e;					
				}
				
				if(coverLetter.isEmpty()) {
					List<EsignDocument> e = new ArrayList<>();
					e.add(reviewAppObj.get(0).getCoverLetter());
					coverLetter = e;					
				}
				
				if(everobj.isEmpty()) {
					
					List<EsignAPIVersion> e = new ArrayList<>();
					e.add(reviewAppObj.get(0).getEsignMainAppId());
					everobj = e;
				}
				
				if(purposeObj == null) {
					purposeObj = reviewAppObj.get(0).getPurposeId();
				}
				
			}
			
			
			PreviewApplicationDTO response = new PreviewApplicationDTO();
			response.setAuditReportId(EncryptionUtil.encrypt(auditReport.get(0).geteSignDocId().toString()));
			response.setCoverLetterId(EncryptionUtil.encrypt(coverLetter.get(0).geteSignDocId().toString()));
			response.setCpsDocumentId(EncryptionUtil.encrypt(cps.get(0).geteSignDocId().toString()));
			response.setEkycModes(ekycModeList);
			response.setEsignAPIVersion(everobj.get(0));
			
			if(purposeObj != null) {
				response.setPurpose(purposeObj.getPurpose());
			}else {
				response.setPurpose(null);
			}
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	
	@GetMapping(EsignApplicationServiceAPIs.GET_APPLICATION_DETAILS)
	public ResponseEntity<?> getApplicationDetails(@RequestParam("id") String username){
		try {
		
			List<EsignLicenseeApplication> objApp = applicationServ.getApplicationByUsernameAndNotStatus(EncryptionUtil.decrypt(username), "Rejected", "Expired");
			
			if(objApp.size()>0)
				return new ResponseEntity<>(objApp.get(0), HttpStatus.OK);
			else
				return new ResponseEntity<>(null, HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@GetMapping(EsignApplicationServiceAPIs.GET_PREVIOUS_APPLICATION_DETAILS)
	public ResponseEntity<?> getpRreviousApplicationDetails(@RequestParam("id") String username){
		try {
		
			List<EsignLicenseeApplication> objApp = applicationServ.getApplicationByUsernameAndStatus(EncryptionUtil.decrypt(username), "Rejected", "Expired");
			
			return new ResponseEntity<>(objApp, HttpStatus.OK);
			
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@GetMapping(EsignApplicationServiceAPIs.SUBMIT_APPLICATION)
	public ResponseEntity<?> submitApplication(@RequestParam("id") String username){
		try {
		
			String msg = "";
			
			List<EsignLicenseeApplication> objApp = applicationServ.getApplicationByUsernameAndStatus(EncryptionUtil.decrypt(username), "Not Completed");
			
			List<ReviewESPApplication> respObj = null;
			
			if(objApp.size() == 0) {
				
				objApp = applicationServ.getApplicationByUsernameAndStatus(EncryptionUtil.decrypt(username), "Under Review");
				
				respObj = espAppServ.getReviewESPApplicationByStatusAndId(objApp.get(0).getEsignLicenseeAppId(), "Active");
				
				
				
				if(respObj.size()>0) {
					respObj.get(0).setStatus("Inactive");
					espAppServ.addReviewESPApplication(respObj.get(0));
				}
				
				msg="Review";
				
			}
			
			EsignLicenseeApplication obj = objApp.get(0);
			
			obj.setUpdated(new Date());
			obj.setUpdatedBy(username);
			
			if(objApp.get(0).getApplicationStatus().equals("Not Completed")) {
				
				List<ClientLicenseDetailsDTO> clist = clientServ.getAllLicenseDetailsByUsername(EncryptionUtil.decrypt(username));
				ClientLicenseDetailsDTO clicense = clist.stream().filter((f)->f.getStatus().equals("Active")).findFirst().orElse(null);
				
				List<EsignLicenseeApplication> esplist = applicationServ.getApplicationByUsernameAndStatus(EncryptionUtil.decrypt(username), "Approved for eSign Go-Live");
				
				
				Integer sno = esplist.isEmpty()? 1 : (esplist.size()+1);
				String licnum = clicense != null ? clicense.getSerialNo() : "-";
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				
				String date = sdf.format(new Date());
				
				String applicationNum = "CCA/ESP/"+licnum+"/"+date+"/"+sno;
				obj.setApplicationNumber(applicationNum);
				msg = applicationNum;
			}
			
			obj.setApplicationStatus("Submitted");
			
			Optional<EsignLicenseeApplication> savedApp = applicationServ.addEsignLicenseeApplication(obj);
			
			//Application Timeline
			
			ApplicationTimeLine apptimelineObj = new ApplicationTimeLine();
			
			apptimelineObj.setApplicationStatus(savedApp.get().getApplicationStatus());
			apptimelineObj.setEsignLicenseeAppId(obj);
			apptimelineObj.setInitiatedBy(EncryptionUtil.decrypt(username));
			
			applicationTimeServ.addApplicationTimeLine(apptimelineObj);
			
			
			return new ResponseEntity<>(EncryptionUtil.encrypt(msg), HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	
	@GetMapping(EsignApplicationServiceAPIs.GET_ESP_APPLICATION_FOR_REVIEW)
	public ResponseEntity<?> getAllespApplicationForReview(){
		try {
		
			List<EsignLicenseeApplication> objApp = applicationServ.getAllApplicationForReview();
			return new ResponseEntity<>(objApp, HttpStatus.OK);
			
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(EsignApplicationServiceAPIs.VIEW_ESP_APPLICATION)
	public ResponseEntity<?> getESPApplicationById(@RequestParam("id") String id){
		try {
		
			EsignLicenseeApplication objApp = applicationServ.getApplicationById(Long.parseLong(EncryptionUtil.decrypt(id)));
			
			if(objApp == null) {
				return new ResponseEntity<>("Invalid application details.", HttpStatus.BAD_REQUEST);	
			}
			
			List<EkycMode> ekycModeList = ekycModeServ.getAllEKYCModeByAppIdAndStatus(objApp.getEsignLicenseeAppId(), "Active");
			List<EsignDocument> auditReport =  esignDocumentServ.getEsignDocumentByAppIdDocumentTypeAndStatus(objApp.getEsignLicenseeAppId(), "Audit Report", "Active");
			List<EsignDocument>  cps =  esignDocumentServ.getEsignDocumentByAppIdDocumentTypeAndStatus(objApp.getEsignLicenseeAppId(), "CPS", "Active");
			List<EsignDocument>  coverLetter =  esignDocumentServ.getEsignDocumentByAppIdDocumentTypeAndStatus(objApp.getEsignLicenseeAppId(), "Cover Letter", "Active");
			List<EsignAPIVersion> everobj = esignAPIVersionServ.getEsignAPIVersionByAppIdAndStatus(objApp.getEsignLicenseeAppId(), "Active");
			
			
			ESPPurposeEntity purposeObj = null;
			
			
			List<ESPPurposeEntity> purposeList = purposeServ.getAllESPPurposeByStatusAndLicenseAppId(objApp.getEsignLicenseeAppId(), "Active"); 
			
			if(!purposeList.isEmpty())
				purposeObj = purposeList.get(0);

			
			List<ReviewESPApplication> respObj = espAppServ.getReviewESPApplicationByStatusAndId(objApp.getEsignLicenseeAppId(), "Active");
			
			
			if(!respObj.isEmpty()) {
				
				if(ekycModeList.isEmpty()) {
					
					List<EkycModeReview> ekycmodereview =  ekycReviewServ.getEkycModeReviewByReviewId(respObj.get(0).getEspReviewId());
					
					List<EkycMode> aobj = new ArrayList<>();
					
					for(EkycModeReview e: ekycmodereview) {
						aobj.add(e.getEkycModeId());
					}
					
					ekycModeList = aobj;
					
				}
				
				List<ReviewedApplication> reviewAppObj = reviewAppServ.getReviewedApplicationByReviewId(respObj.get(0).getEspReviewId());
				
				if(auditReport.isEmpty()) {
					
					List<EsignDocument> e = new ArrayList<>();
					e.add(reviewAppObj.get(0).getAuditReport());
					auditReport = e;
				}
				
				if(cps.isEmpty()) {
					List<EsignDocument> e = new ArrayList<>();
					e.add(reviewAppObj.get(0).getCpsDocument());
					cps = e;					
				}
				
				if(coverLetter.isEmpty()) {
					List<EsignDocument> e = new ArrayList<>();
					e.add(reviewAppObj.get(0).getCoverLetter());
					coverLetter = e;					
				}
				
				if(everobj.isEmpty()) {
					
					List<EsignAPIVersion> e = new ArrayList<>();
					e.add(reviewAppObj.get(0).getEsignMainAppId());
					everobj = e;
				}
				if(purposeObj == null) {
					purposeObj = reviewAppObj.get(0).getPurposeId();
				}

				
			}
			
			
			PreviewApplicationDTO response = new PreviewApplicationDTO();
			response.setAuditReportId(EncryptionUtil.encrypt(auditReport.get(0).geteSignDocId().toString()));
			response.setCoverLetterId(EncryptionUtil.encrypt(coverLetter.get(0).geteSignDocId().toString()));
			response.setCpsDocumentId(EncryptionUtil.encrypt(cps.get(0).geteSignDocId().toString()));
			response.setEkycModes(ekycModeList);
			response.setEsignAPIVersion(everobj.get(0));
			
			if(purposeObj != null) {
				response.setPurpose(purposeObj.getPurpose());
			}else {
				response.setPurpose(null);
			}

			
			return new ResponseEntity<>(response, HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@GetMapping(EsignApplicationServiceAPIs.UNDER_REVIEW_DATA)
	public ResponseEntity<?> underReviewApplicationData(@RequestParam("id") String id){
		
		try {
			
			List<ReviewESPApplication> respObj = espAppServ.getReviewESPApplicationByStatusAndId(Long.parseLong(EncryptionUtil.decrypt(id)), "Active");
			
			return new ResponseEntity<>(respObj.get(0), HttpStatus.OK);
			
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
		}
	}
	
	
	@GetMapping(EsignApplicationServiceAPIs.UNDER_REVIEW_DATA_BY_USERNAME)
	public ResponseEntity<?> underReviewApplicationDataByUsername(@RequestParam("id") String id){
		
		try {
			
			
			List<EsignLicenseeApplication> objApp = applicationServ.getApplicationByUsernameAndStatus(EncryptionUtil.decrypt(id), "Under Review");
			
			
			List<ReviewESPApplication> respObj = espAppServ.getReviewESPApplicationByStatusAndId(objApp.get(0).getEsignLicenseeAppId(), "Active");

			
			return new ResponseEntity<>(respObj.get(0), HttpStatus.OK);
			
		}catch(Exception e) {
		
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
		}
	}
	
	
	@GetMapping(EsignApplicationServiceAPIs.PREVIOUS_REVIEW_DATA)
	public ResponseEntity<?> inactiveReviewApplicationData(@RequestParam("id") String id){
		
		try {
			
			List<PreviousReviewedApplicationDTO> app = new ArrayList<>();
			
			List<ReviewESPApplication> respObj = espAppServ.getReviewESPApplicationByStatusAndId(Long.parseLong(EncryptionUtil.decrypt(id)), "Inactive");
			
			for(ReviewESPApplication e: respObj) {
				
				PreviousReviewedApplicationDTO a = new PreviousReviewedApplicationDTO();
				a.setReviewESPApp(e);
				
				PreviewApplicationDTO papp = new PreviewApplicationDTO();
				
				
				//Ekyc Mode
						List<EkycModeReview> ekycmodereview =  ekycReviewServ.getEkycModeReviewByReviewId(e.getEspReviewId());
						
						List<EkycMode> aobj = new ArrayList<>();
						
						for(EkycModeReview er: ekycmodereview) {
							aobj.add(er.getEkycModeId());
						}
						
						papp.setEkycModes(aobj);
						
						
						List<ReviewedApplication> reviewAppObj = reviewAppServ.getReviewedApplicationByReviewId(e.getEspReviewId());
				

						papp.setAuditReportId(EncryptionUtil.encrypt(reviewAppObj.get(0).getAuditReport().geteSignDocId().toString()));
						papp.setCoverLetterId(EncryptionUtil.encrypt(reviewAppObj.get(0).getCoverLetter().geteSignDocId().toString()));
						papp.setCpsDocumentId(EncryptionUtil.encrypt(reviewAppObj.get(0).getCpsDocument().geteSignDocId().toString()));
						papp.setEsignAPIVersion(reviewAppObj.get(0).getEsignMainAppId());
						papp.setPurpose(reviewAppObj.get(0).getPurposeId().getPurpose());
						
				a.setPreviousData(papp);

				app.add(a);
				
				
				
			}
			
			
			return new ResponseEntity<>(app, HttpStatus.OK);
			
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
		}
	}
	
	
	@PostMapping(EsignApplicationServiceAPIs.SUBMIT_APPLICATION_FOR_REVIEW)
	public ResponseEntity<?> submitApplicationForReview(@RequestBody ReviewApplicationDTO reviewObj){
		try {
			
					
			
			if(reviewObj.getRemarks().equals("") || reviewObj.getRemarks().isEmpty() || reviewObj.getRemarks().isBlank()) {
				return new ResponseEntity<>("Kindly provide your remarks.", HttpStatus.BAD_REQUEST);
			}else if(!reviewObj.getRemarks().trim().matches("^[A-Za-z0-9,./ ]+$")) {
				return new ResponseEntity<>("Only alphabets, numbers and the characters ,./ are allowed.", HttpStatus.BAD_REQUEST);
			}else if(reviewObj.getRemarks().length() < 3 || reviewObj.getRemarks().length() > 500) {
				return new ResponseEntity<>("The length must be between 3 and 500 characters.", HttpStatus.BAD_REQUEST);
			}
			
			
			
			EsignLicenseeApplication objApp = applicationServ.getApplicationById(Long.parseLong(EncryptionUtil.decrypt(reviewObj.getEsignLicenseeAppId())));
			List<ReviewESPApplication> respObj = espAppServ.getReviewESPApplicationByStatusAndId(objApp.getEsignLicenseeAppId(), "Active");
			
			if(respObj.size()>0)
				return new ResponseEntity<>("Application is already reviewed.", HttpStatus.BAD_REQUEST);
			
			if(reviewObj.getIsreject() == true) {
				objApp.setApplicationStatus("Recommanded for Rejection");
			}else {
				objApp.setApplicationStatus("Under Review");
			}
			objApp.setUpdated(new Date());
			objApp.setUpdatedBy(reviewObj.getUserName());
			applicationServ.addEsignLicenseeApplication(objApp);
			
			//Application Timeline
			
			ApplicationTimeLine apptimelineObj = new ApplicationTimeLine();
			if(reviewObj.getIsreject() == true) {
				apptimelineObj.setApplicationStatus("Recommanded for Rejection");
			}else {
				apptimelineObj.setApplicationStatus("Under Review");
			}
			
			apptimelineObj.setEsignLicenseeAppId(objApp);
			apptimelineObj.setInitiatedBy(EncryptionUtil.decrypt(reviewObj.getUserName()));
			
			applicationTimeServ.addApplicationTimeLine(apptimelineObj);
			
			ReviewESPApplication rObj = new ReviewESPApplication();
			rObj.setAuditReport(reviewObj.getAuditReport());
			rObj.setCoverLetter(reviewObj.getCoverLetter());
			rObj.setCpsDocument(reviewObj.getCpsDocument());
			rObj.setEkycMode(reviewObj.getEkycMode());
			rObj.setEsignAPIVersion(reviewObj.getEsignAPIVersion());
			rObj.setEsignLicenseeAppId(objApp);
			rObj.setRemarks(reviewObj.getRemarks());
			rObj.setPurpose(reviewObj.getPurpose());
			
			rObj.setStatus("Active");
			
			Optional<ReviewESPApplication> revApp =  espAppServ.addReviewESPApplication(rObj);
			
			
			List<EkycMode> ekycModeList = ekycModeServ.getAllEKYCModeByAppIdAndStatus(Long.parseLong(EncryptionUtil.decrypt(reviewObj.getEsignLicenseeAppId())), "Active");
			List<EsignDocument> auditReport =  esignDocumentServ.getEsignDocumentByAppIdDocumentTypeAndStatus(Long.parseLong(EncryptionUtil.decrypt(reviewObj.getEsignLicenseeAppId())), "Audit Report", "Active");
			List<EsignDocument>  cps =  esignDocumentServ.getEsignDocumentByAppIdDocumentTypeAndStatus(Long.parseLong(EncryptionUtil.decrypt(reviewObj.getEsignLicenseeAppId())), "CPS", "Active");
			List<EsignDocument>  coverLetter =  esignDocumentServ.getEsignDocumentByAppIdDocumentTypeAndStatus(Long.parseLong(EncryptionUtil.decrypt(reviewObj.getEsignLicenseeAppId())), "Cover Letter", "Active");
			List<EsignAPIVersion> everobj = esignAPIVersionServ.getEsignAPIVersionByAppIdAndStatus(Long.parseLong(EncryptionUtil.decrypt(reviewObj.getEsignLicenseeAppId())), "Active");
			List<ESPPurposeEntity> plist = purposeServ.getAllESPPurposeByStatusAndLicenseAppId(Long.parseLong(EncryptionUtil.decrypt(reviewObj.getEsignLicenseeAppId())), "Active");
			
			for(EkycMode e: ekycModeList) {
				
				EkycModeReview a = new EkycModeReview();
				a.setEkycModeId(e);
				a.setEspReviewId(revApp.get());
				ekycReviewServ.addEkycModeReview(a);
			}
			
			ReviewedApplication ra = new ReviewedApplication();
			ra.setAuditReport(auditReport.get(0));
			ra.setCoverLetter(coverLetter.get(0));
			ra.setCpsDocument(cps.get(0));
			ra.setEsignMainAppId(everobj.get(0));
			ra.setEspReviewId(rObj);
			ra.setPurposeId(plist.get(0));
			
			reviewAppServ.addReviewedApplication(ra);
			

			
			
			ekycModeServ.inactiveAllEKYCModeByAppId(Long.parseLong(EncryptionUtil.decrypt(reviewObj.getEsignLicenseeAppId())));
			ekycModeServ.changeStatusOfAllEKYCModeByAppIdAndStatus(Long.parseLong(EncryptionUtil.decrypt(reviewObj.getEsignLicenseeAppId())), "Review");
			esignDocumentServ.inactiveAllDocumentByAppId(Long.parseLong(EncryptionUtil.decrypt(reviewObj.getEsignLicenseeAppId())));
			esignAPIVersionServ.inactiveAllEsignApiVerByAppId(Long.parseLong(EncryptionUtil.decrypt(reviewObj.getEsignLicenseeAppId())));
			
			plist.get(0).setStatus("Inactive");
			purposeServ.addPurpose(plist.get(0));
			
			return new ResponseEntity<>(HttpStatus.OK);
			
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@GetMapping(EsignApplicationServiceAPIs.GET_ESP_APPLICATION_RECOMMANDED_FOR_REJECTION)
	public ResponseEntity<?> getAllespApplicationRecommandedForRejection(){
		try {
		
			List<EsignLicenseeApplication> objApp = applicationServ.getAllApplicationByStatus("Recommanded for Rejection");
			return new ResponseEntity<>(objApp, HttpStatus.OK);
			
		}catch(Exception e) {
			
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	

	@PostMapping(EsignApplicationServiceAPIs.SUBMIT_APPLICATION_FOR_REJECTION_OR_APPROVE)
	public ResponseEntity<?> submitApplicationForApproveReject(@RequestBody RecommendedRejectionDTO reviewObj){
		try {
			
			if(reviewObj.getRemarks().equals("") || reviewObj.getRemarks().isEmpty() || reviewObj.getRemarks().isBlank()) {
				return new ResponseEntity<>("Kindly provide your remarks.", HttpStatus.BAD_REQUEST);
			}else if(!reviewObj.getRemarks().trim().matches("^[A-Za-z0-9,./ ]+$")) {
				return new ResponseEntity<>("Only alphabets, numbers and the characters ,./ are allowed.", HttpStatus.BAD_REQUEST);
			}else if(reviewObj.getRemarks().length() < 3 || reviewObj.getRemarks().length() > 500) {
				return new ResponseEntity<>("The length must be between 3 and 500 characters.", HttpStatus.BAD_REQUEST);
			}
			
			EsignLicenseeApplication objApp = applicationServ.getApplicationById(Long.parseLong(EncryptionUtil.decrypt(reviewObj.getEsignLicenseeAppId())));
			List<ReviewESPApplication> respObj = espAppServ.getReviewESPApplicationByStatusAndId(objApp.getEsignLicenseeAppId(), "Active");
			
			if(reviewObj.getIsreject() == true) {
				objApp.setApplicationStatus("Rejected");
			}else {
				objApp.setApplicationStatus("Under Review");
			}
			objApp.setUpdated(new Date());
			objApp.setUpdatedBy(reviewObj.getUserName());
			
			applicationServ.addEsignLicenseeApplication(objApp);
			
			//Application Timeline
			
			ApplicationTimeLine apptimelineObj = new ApplicationTimeLine();
			if(reviewObj.getIsreject() == true) {
				apptimelineObj.setApplicationStatus("Rejected");
			}else {
				apptimelineObj.setApplicationStatus("Under Review");
			}
			
			apptimelineObj.setEsignLicenseeAppId(objApp);
			apptimelineObj.setInitiatedBy(EncryptionUtil.decrypt(reviewObj.getUserName()));
			
			applicationTimeServ.addApplicationTimeLine(apptimelineObj);
			
			if(!respObj.isEmpty()) {
				ReviewESPApplication r = respObj.get(0);
				r.setCcaRemarks(reviewObj.getRemarks());
				r.setUpdated(new Date());
				if(reviewObj.getIsreject() == true)
					r.setStatus("Inactive");
				espAppServ.addReviewESPApplication(r);

			}
						
			return new ResponseEntity<>(HttpStatus.OK);
			
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@PostMapping(EsignApplicationServiceAPIs.RECOMMEND_FOR_ESIGN_GO_LIVE)
	public ResponseEntity<?> recommendForEsignGoLive(@RequestBody ReviewApplicationDTO reviewObj){
		try {
			
			EsignLicenseeApplication objApp = applicationServ.getApplicationById(Long.parseLong(EncryptionUtil.decrypt(reviewObj.getEsignLicenseeAppId())));
			
			
			objApp.setApplicationStatus("Recommend for eSign Go-Live");
			objApp.setUpdated(new Date());
			objApp.setUpdatedBy(reviewObj.getUserName());
			applicationServ.addEsignLicenseeApplication(objApp);
			
			//Application Timeline
			
			ApplicationTimeLine apptimelineObj = new ApplicationTimeLine();
			
			apptimelineObj.setApplicationStatus("Recommend for eSign Go-Live");
			apptimelineObj.setEsignLicenseeAppId(objApp);
			apptimelineObj.setInitiatedBy(EncryptionUtil.decrypt(reviewObj.getUserName()));
			
			applicationTimeServ.addApplicationTimeLine(apptimelineObj);
			
			ReviewESPApplication rObj = new ReviewESPApplication();
			rObj.setAuditReport(reviewObj.getAuditReport());
			rObj.setCoverLetter(reviewObj.getCoverLetter());
			rObj.setCpsDocument(reviewObj.getCpsDocument());
			rObj.setEkycMode(reviewObj.getEkycMode());
			rObj.setEsignAPIVersion(reviewObj.getEsignAPIVersion());
			rObj.setEsignLicenseeAppId(objApp);
			rObj.setRemarks(reviewObj.getRemarks());
			rObj.setPurpose(reviewObj.getPurpose());
			rObj.setStatus("Inactive");
			
			Optional<ReviewESPApplication> revApp =  espAppServ.addReviewESPApplication(rObj);
			
			
			List<EkycMode> ekycModeList = ekycModeServ.getAllEKYCModeByAppIdAndStatus(Long.parseLong(EncryptionUtil.decrypt(reviewObj.getEsignLicenseeAppId())), "Active");
			List<EsignDocument> auditReport =  esignDocumentServ.getEsignDocumentByAppIdDocumentTypeAndStatus(Long.parseLong(EncryptionUtil.decrypt(reviewObj.getEsignLicenseeAppId())), "Audit Report", "Active");
			List<EsignDocument>  cps =  esignDocumentServ.getEsignDocumentByAppIdDocumentTypeAndStatus(Long.parseLong(EncryptionUtil.decrypt(reviewObj.getEsignLicenseeAppId())), "CPS", "Active");
			List<EsignDocument>  coverLetter =  esignDocumentServ.getEsignDocumentByAppIdDocumentTypeAndStatus(Long.parseLong(EncryptionUtil.decrypt(reviewObj.getEsignLicenseeAppId())), "Cover Letter", "Active");
			List<EsignAPIVersion> everobj = esignAPIVersionServ.getEsignAPIVersionByAppIdAndStatus(Long.parseLong(EncryptionUtil.decrypt(reviewObj.getEsignLicenseeAppId())), "Active");

			ESPPurposeEntity purposeObj = null;
			
			
			List<ESPPurposeEntity> purposeList = purposeServ.getAllESPPurposeByStatusAndLicenseAppId(Long.parseLong(EncryptionUtil.decrypt(reviewObj.getEsignLicenseeAppId())), "Active"); 
			
			if(!purposeList.isEmpty())
				purposeObj = purposeList.get(0);

			
			for(EkycMode e: ekycModeList) {
				
				EkycModeReview a = new EkycModeReview();
				a.setEkycModeId(e);
				a.setEspReviewId(revApp.get());
				ekycReviewServ.addEkycModeReview(a);
			}
			
			ReviewedApplication ra = new ReviewedApplication();
			ra.setAuditReport(auditReport.get(0));
			ra.setCoverLetter(coverLetter.get(0));
			ra.setCpsDocument(cps.get(0));
			ra.setEsignMainAppId(everobj.get(0));
			ra.setEspReviewId(rObj);
			ra.setPurposeId(purposeObj);
			
			reviewAppServ.addReviewedApplication(ra);
			
			return new ResponseEntity<>(HttpStatus.OK);
			
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}
	
	
	@GetMapping(EsignApplicationServiceAPIs.APPROVE_ESIGN_GO_LIVE)
	public ResponseEntity<?> approveForEsignGoLive(@RequestParam("id") String licenseAppId, @RequestParam("pid") String username){
		try {
			
			EsignLicenseeApplication objApp = applicationServ.getApplicationById(Long.parseLong(EncryptionUtil.decrypt(licenseAppId)));
			List<ReviewESPApplication> respObj = espAppServ.getReviewESPApplicationByStatusAndId(objApp.getEsignLicenseeAppId(), "Active");
			
			objApp.setApplicationStatus("Approved for eSign Go-Live");
			
			objApp.setUpdated(new Date());
			objApp.setUpdatedBy(username);
			objApp.setEmpanelmentDate(new Date());
			
			applicationServ.addEsignLicenseeApplication(objApp);
			
			//Application Timeline
			
			ApplicationTimeLine apptimelineObj = new ApplicationTimeLine();
			
			apptimelineObj.setApplicationStatus("Approved for eSign Go-Live");
			
			
			apptimelineObj.setEsignLicenseeAppId(objApp);
			apptimelineObj.setInitiatedBy(EncryptionUtil.decrypt(username));
			
			applicationTimeServ.addApplicationTimeLine(apptimelineObj);
			
			if(!respObj.isEmpty()) {
				ReviewESPApplication r = respObj.get(0);
				r.setStatus("Inactive");
				espAppServ.addReviewESPApplication(r);

			}
						
			return new ResponseEntity<>(HttpStatus.OK);
			
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@GetMapping(EsignApplicationServiceAPIs.GET_ESP_APPLICATION_RECOMMANDED_FOR_ESIGN_GO_LIVE)
	public ResponseEntity<?> getAllespApplicationRecommandedForEsignGoLive(){
		try {
		
			List<EsignLicenseeApplication> objApp = applicationServ.getAllApplicationByStatus("Recommend for eSign Go-Live");
			return new ResponseEntity<>(objApp, HttpStatus.OK);
			
		}catch(Exception e) {
			
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@GetMapping(EsignApplicationServiceAPIs.GET_ESP_APPLICATION_APPROVED_FOR_ESIGN_GO_LIVE)
	public ResponseEntity<?> getAllespApplicationApprovedForEsignGoLive(){
		try {
		
			List<EsignLicenseeApplication> objApp = applicationServ.getAllApplicationByStatus("Approved for eSign Go-Live");
			return new ResponseEntity<>(objApp, HttpStatus.OK);
			
		}catch(Exception e) {
			
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@GetMapping(EsignApplicationServiceAPIs.GET_ESP_APPLICATION_REJECTED)
	public ResponseEntity<?> getAllespApplicationRejected(){
		try {
		
			List<EsignLicenseeApplication> objApp = applicationServ.getAllApplicationByStatus("Rejected");
			return new ResponseEntity<>(objApp, HttpStatus.OK);
			
		}catch(Exception e) {
			
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@GetMapping(EsignApplicationServiceAPIs.GET_ESP_APPLICATION_EXPIRED)
	public ResponseEntity<?> getAllespApplicationExpired(){
		try {
		
			List<EsignLicenseeApplication> objApp = applicationServ.getAllApplicationByStatus("Expired");
			return new ResponseEntity<>(objApp, HttpStatus.OK);
			
		}catch(Exception e) {
			
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	
	@GetMapping(EsignApplicationServiceAPIs.CHECK_FOR_ESP)
	public ResponseEntity<?> checkForESP(@RequestParam("id") String username){
		try {
			

			String uname = EncryptionUtil.decrypt(username);

			List<EsignLicenseeApplication> objApp = applicationServ.getApplicationByUsernameAndStatus(uname, "Approved for eSign Go-Live");
			
			if(!objApp.isEmpty())
				return new ResponseEntity<>(EncryptionUtil.encrypt("true"), HttpStatus.OK);
			
			return new ResponseEntity<>(EncryptionUtil.encrypt("false"), HttpStatus.OK);
			
			
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	

	
	@GetMapping(EsignApplicationServiceAPIs.GET_ALL_ESP_WITH_EKYC_MODE_APPROVED)
	public ResponseEntity<?> getAllESPWithEkycModeApproved(){
		try {
			

			List<EsignLicenseeApplication> objApp = applicationServ.getAllApplicationByStatus("Approved for eSign Go-Live");
			
			List<ESPWithEkycModeDTO> response = new ArrayList<>();
			
			for(EsignLicenseeApplication e: objApp) {
		
				List<String> ekycModes = ekycModeServ.getDistinctApprovedEkycModesByUsername(e.getUserName());
				
				ESPWithEkycModeDTO d = new ESPWithEkycModeDTO();
				d.setEspUserName(e.getUserName());
				d.setEkycModeApproved(ekycModes);
				
				response.add(d);
				
			}
			
						
			return new ResponseEntity<>(response, HttpStatus.OK);
			
			
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	//Esign
	
	@GetMapping(EsignApplicationServiceAPIs.ESIGN_COVER_LETTER)
	public ResponseEntity<?> eSignCoverLetter(@RequestParam("id") String id, @RequestParam("did") String did){
		
		try {
			
			String eSignDocId = id;
			String  signedDocument = did;
			
			if(EncryptionUtil.decrypt(eSignDocId) != null)
				eSignDocId = EncryptionUtil.decrypt(eSignDocId);
			
			if(EncryptionUtil.decrypt(signedDocument) != null)
				signedDocument = EncryptionUtil.decrypt(signedDocument);
			
			EsignDocument edoc = esignDocumentServ.getEsignDocumentById(Long.parseLong(eSignDocId));
			
			if(edoc != null) {
				
				edoc.setEsignDocument(EncryptionUtil.encrypt(signedDocument));
				esignDocumentServ.addEsignDocument(edoc);
				
			}
			
			
			
			
			return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
		}
	}
	
	
	//Helper Method
	
	
	private ByteArrayOutputStream mergeSinglePdf(byte[] generatedPdfBytes, String title, String fileName, String filepath) {
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
	                    .moveText(20, firstPage.getPageSize().getTop() - 15)  // Position the title
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
	        return finalOutputStream;

	    } catch (IOException e) {
	        e.printStackTrace();
	        throw new RuntimeException("Error merging PDFs: " + e.getMessage());
	    }
	}

	
	
	
	private ByteArrayOutputStream addPageNumber(byte[] generatedPdfBytes) {
	    try (ByteArrayOutputStream finalOutputStream = new ByteArrayOutputStream()) {

	        
	        PdfWriter writer = new PdfWriter(finalOutputStream);
	        
	        
	        PdfDocument generatedPdf = new PdfDocument(new PdfReader(new ByteArrayInputStream(generatedPdfBytes)), writer);

	       
	        int totalPages = generatedPdf.getNumberOfPages();

	        
	        for (int i = 1; i <= totalPages; i++) {
	           
	            PdfPage page = generatedPdf.getPage(i);
	            
	            
	            PdfCanvas canvas = new PdfCanvas(page);
	            
	            
	            PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
	            canvas.beginText()
	                  .setFontAndSize(font, 12)
	                  .moveText(page.getPageSize().getRight()-100, 20) 
	                  .showText("Page " + i + " of " + totalPages)
	                  .endText();
	        }

	       
	        generatedPdf.close();

	        
	        return finalOutputStream;

	    } catch (IOException e) {
	        e.printStackTrace();
	        throw new RuntimeException("Error printing page number on it. " + e.getMessage());
	    }
	}


	@GetMapping("/change-review-status/{applicantUserName}")
    public ResponseEntity<?> changeStatusApproveForRejection(@PathVariable String applicantUserName) {

		   System.out.println("ghsygdc--->"+applicantUserName);
        List<EsignLicenseeApplication> activeEspApplications = applicationServ.getApplicationByUsername(applicantUserName);
        
        System.out.println("ghsygdc--->"+activeEspApplications.size());

        if (activeEspApplications.isEmpty()) {
            return ResponseEntity.ok("No active applications found for the user: " + applicantUserName);
        }

       
        for (EsignLicenseeApplication espApplication : activeEspApplications) {
            espApplication.setApplicationStatus("Expired");
            espApplication.setUpdated(new Date());
            applicationServ.updateEsignLicenseeApplication(espApplication); 
            ApplicationTimeLine applicationTimeLine=new ApplicationTimeLine();
            applicationTimeLine.setApplicationStatus("Expired"); 
            applicationTimeLine.setEsignLicenseeAppId(espApplication);
            applicationTimeLine.setInitiatedBy(applicantUserName);
            applicationTimeLine.setUpdated(new Date());
            applicationTimeServ.addApplicationTimeLine(applicationTimeLine);
        }

     
        return ResponseEntity.ok("Successfully updated the status of active applications for user: " + applicantUserName);
    }




	
}
