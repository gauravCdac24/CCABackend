package in.lms.cca.dashboard;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;

import in.lms.cca.dto.ASPDetailsDTO;
import in.lms.cca.dto.AddressDTO;
import in.lms.cca.dto.AnnualAuditScheduleDTO;
import in.lms.cca.dto.AppLocationDTO;
import in.lms.cca.dto.ApplicationAuditorsDTO;
import in.lms.cca.dto.AuditAgencyDTO;
import in.lms.cca.dto.AuditAgencyDetailsDTO;
import in.lms.cca.dto.AuditAgencyEmailDTO;
import in.lms.cca.dto.AuditAgencyMobileDTO;
import in.lms.cca.dto.AuditAgencySelectionDTO;
import in.lms.cca.dto.AuditByAuditAgencyDTO;
import in.lms.cca.dto.CADetailsDTO;
import in.lms.cca.dto.CALicenseDetailsDTO;
import in.lms.cca.dto.CASiteLocationsDTO;
import in.lms.cca.dto.CAWithYearOfLicensing;
import in.lms.cca.dto.CCADashboardResponse;
import in.lms.cca.dto.CCAGraphDataRequest;
import in.lms.cca.dto.CCAGraphDataResponse;
import in.lms.cca.dto.CityDTO;
import in.lms.cca.dto.ClientLicenseDetailsDTO;
import in.lms.cca.dto.CountryDTO;
import in.lms.cca.dto.CustomizedLicensingReportDTO;
import in.lms.cca.dto.DSCeSignIssuedDTO;
import in.lms.cca.dto.DSCeSignIssuedPayload;
import in.lms.cca.dto.DataIssuedDTO;
import in.lms.cca.dto.ESPDTO;
import in.lms.cca.dto.ESPDetailsResponse;
import in.lms.cca.dto.ESPWithEkycModeDTO;
import in.lms.cca.dto.FirmApplicationMainDTO;
import in.lms.cca.dto.GovtOrganizationApplicationMainDTO;
import in.lms.cca.dto.IndivAddressMainDTO;
import in.lms.cca.dto.IndivApplicationMainDTO;
import in.lms.cca.dto.IndivEmailsDTO;
import in.lms.cca.dto.IntentUniqueCodeDTO;
import in.lms.cca.dto.LdifDocumentDTO;
import in.lms.cca.dto.LicenseeAuditorsDTO;
import in.lms.cca.dto.LicenseeDashboardResponse;
import in.lms.cca.dto.LicensingReportDTO;
import in.lms.cca.dto.StateDTO;
import in.lms.cca.dto.TotalDSCeSignIssuedResponse;
import in.lms.cca.dto.ViewDSCeSignIssuedDTO;
import in.lms.cca.entity.ASPDetails;
import in.lms.cca.entity.DSCeSignIssued;
import in.lms.cca.entity.LdifDocument;
import in.lms.cca.service.IASPDetailsService;
import in.lms.cca.service.IClientDashboardService;
import in.lms.cca.service.IDSCeSignIssuedService;
import in.lms.cca.service.ILdifDocumentService;
import in.lms.cca.util.api.DashboardAPIs;
import in.lms.cca.util.global.DocumentFileUtil;
import in.lms.cca.util.global.EncryptionUtil;

@RestController
@RequestMapping(DashboardAPIs.DASHBOARD_SERVICE_BASE_URL)
@CrossOrigin
public class DashoboardController {

	@Autowired
	private IASPDetailsService aspServ;
	
	@Autowired
	private IDSCeSignIssuedService dscServ;
	
	@Autowired
	private IClientDashboardService clientServ;
	
	@Autowired
	private ILdifDocumentService ldifServ;
	
	@PostMapping(DashboardAPIs.ADD_DSC_ESIGN_ISSUED)
    public ResponseEntity<?> addDSCeSignIssued(@ModelAttribute DSCeSignIssuedDTO obj) {
	
		try {
			
				if (obj.getMonth().isEmpty() || obj.getMonth().isBlank()) {
		            return new ResponseEntity<>("Please select month.", HttpStatus.BAD_REQUEST);
		        } else if (!obj.getMonth().trim().matches("^[A-Za-z]+$")) {
		            return new ResponseEntity<>("Only alphabets are allowed.", HttpStatus.BAD_REQUEST);
		        } else if (obj.getMonth().trim().length() < 3 || obj.getMonth().trim().length() > 9) {
		            return new ResponseEntity<>("The length must be between 3 and 9 characters.", HttpStatus.BAD_REQUEST);
		        }
				
				
				if (obj.getYear().isEmpty() || obj.getYear().isBlank()) {
		            return new ResponseEntity<>("Please select year.", HttpStatus.BAD_REQUEST);
		        } else if (!obj.getYear().trim().matches("^[0-9]+$")) {
		            return new ResponseEntity<>("Please enter valid year.", HttpStatus.BAD_REQUEST);
		        } else if (obj.getYear().trim().length() != 4) {
		            return new ResponseEntity<>("The length must be 4 characters.", HttpStatus.BAD_REQUEST);
		        }

				
				if(obj.getCaUsername() == null || obj.getCaUsername().isBlank() || (obj.getCaUsername() != null && EncryptionUtil.decrypt(obj.getCaUsername()) == null)) {
					return new ResponseEntity<>("Licensee details not found, try after some time.", HttpStatus.BAD_REQUEST);
				}
				
				
				//check for esp
				String isesp = clientServ.isESP(EncryptionUtil.decrypt(obj.getCaUsername()));
				
				
				for(DataIssuedDTO d: obj.getDscesignList()) {
					
					if (d.getCountryId().isEmpty() || (!d.getCountryId().isEmpty() && EncryptionUtil.decrypt(d.getCountryId()) == null)) {
			            return new ResponseEntity<>("Please select country.", HttpStatus.BAD_REQUEST);
			        }
					
					if (d.getStateId().isEmpty() || (!d.getStateId().isEmpty() && EncryptionUtil.decrypt(d.getStateId()) == null)) {
			            return new ResponseEntity<>("Please select state.", HttpStatus.BAD_REQUEST);
			        }
					
					if (d.getDscIssued().isEmpty()) {
			            return new ResponseEntity<>("Please enter number of DSC Issued.", HttpStatus.BAD_REQUEST);
			        } else if (!d.getDscIssued().trim().matches("^(0|[1-9][0-9]*)$")) {
			            return new ResponseEntity<>("Only digits are allowed, and leading zeros are not permitted.", HttpStatus.BAD_REQUEST);
			        } else if (d.getDscIssued().trim().length() < 1 || d.getDscIssued().trim().length() > 10) {
			            return new ResponseEntity<>("The length must be between 1 and 10 characters.", HttpStatus.BAD_REQUEST);
			        }
					
					if (d.geteSignIssued().isEmpty()) {
			            return new ResponseEntity<>("Please enter number of eSign Issued.", HttpStatus.BAD_REQUEST);
			        } else if (!d.geteSignIssued().trim().matches("^(0|[1-9][0-9]*)$")) {
			            return new ResponseEntity<>("Only digits are allowed, and leading zeros are not permitted.", HttpStatus.BAD_REQUEST);
			        } else if (d.geteSignIssued().trim().length() < 1 || d.geteSignIssued().trim().length() > 10) {
			            return new ResponseEntity<>("The length must be between 1 and 10 characters.", HttpStatus.BAD_REQUEST);
			        }
					
					
					if(EncryptionUtil.decrypt(isesp).equals("false") && Long.parseLong(d.geteSignIssued())>0){
						return new ResponseEntity<>("You have not become an eSign Service Provider; therefore, you cannot issue eSign certificates.", HttpStatus.BAD_REQUEST);
					}
					
					//Check if already submitted for particular year, month and country and state by username
					DSCeSignIssued filterObj = dscServ.getDSCeSignIssuedByYearMonthCountryAndStateUsername(EncryptionUtil.decrypt(obj.getCaUsername()), d.getCountryId(), d.getStateId(), obj.getYear(), obj.getMonth());
				
					if(filterObj != null) {
						
						return new ResponseEntity<>("You have already submitted data.", HttpStatus.BAD_REQUEST);
					}
				
				}
				
				
				for(DataIssuedDTO d: obj.getDscesignList()) {
					
					DSCeSignIssued newObj = new DSCeSignIssued();
					
					newObj.setCountryId(d.getCountryId());
					newObj.setUpdatedBy(obj.getCaUsername());
					newObj.setDscIssued(d.getDscIssued());
					newObj.seteSignIssued(d.geteSignIssued());
					newObj.setMonth(obj.getMonth());
					newObj.setStateId(d.getStateId());
					newObj.setYear(obj.getYear());
					newObj.setCreatedBy(obj.getCaUsername());
					newObj.setCaUsername(EncryptionUtil.decrypt(obj.getCaUsername()));
					
					Optional<DSCeSignIssued> asp = dscServ.addDSCeSignIssued(newObj);
					
				}
				
				//Save files
				
				if(obj.getLdifFile() != null) {
					
					Random rand = new Random();
					Integer rnum = rand.nextInt(1000);
					
					for(LdifDocumentDTO ld: obj.getLdifFile()) {
						
						String filename = DocumentFileUtil.saveFile(ld.getFile(), "LDIF", rnum.toString(), "LDIF");
						
						LdifDocument d = new LdifDocument();
						d.setCaUsername(EncryptionUtil.decrypt(obj.getCaUsername()));
						d.setCreatedBy(obj.getCaUsername());
						d.setFileName(filename);
						d.setMonth(obj.getMonth());
						d.setUpdatedBy(obj.getCaUsername());
						d.setYear(obj.getYear());
						
						ldifServ.addLdifDocument(d);
						
					}
					
				}
				
	            return new ResponseEntity<>("Your data has been successfully added.", HttpStatus.OK);
				
				
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while saving. Please Try again.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	
	@PostMapping(DashboardAPIs.UPDATE_DSC_ESIGN_ISSUED)
    public ResponseEntity<?> updateDSCeSignIssued(@RequestBody DSCeSignIssuedDTO obj) {
	
		try {
			
				if (obj.getMonth().isEmpty() || obj.getMonth().isBlank()) {
		            return new ResponseEntity<>("Please select month.", HttpStatus.BAD_REQUEST);
		        } else if (!obj.getMonth().trim().matches("^[A-Za-z]+$")) {
		            return new ResponseEntity<>("Only alphabets are allowed.", HttpStatus.BAD_REQUEST);
		        } else if (obj.getMonth().trim().length() < 3 || obj.getMonth().trim().length() > 9) {
		            return new ResponseEntity<>("The length must be between 3 and 9 characters.", HttpStatus.BAD_REQUEST);
		        }
				
				
				if (obj.getYear().isEmpty() || obj.getYear().isBlank()) {
		            return new ResponseEntity<>("Please select year.", HttpStatus.BAD_REQUEST);
		        } else if (!obj.getYear().trim().matches("^[0-9]+$")) {
		            return new ResponseEntity<>("Please enter valid year.", HttpStatus.BAD_REQUEST);
		        } else if (obj.getYear().trim().length() != 4) {
		            return new ResponseEntity<>("The length must be 4 characters.", HttpStatus.BAD_REQUEST);
		        }

				
				if(obj.getCaUsername() == null || obj.getCaUsername().isBlank() || (obj.getCaUsername() != null && EncryptionUtil.decrypt(obj.getCaUsername()) == null)) {
					return new ResponseEntity<>("Licensee details not found, try after some time.", HttpStatus.BAD_REQUEST);
				}
				
				
				//check for esp
				String isesp = clientServ.isESP(EncryptionUtil.decrypt(obj.getCaUsername()));
				
				
				for(DataIssuedDTO d: obj.getDscesignList()) {
					
					if (d.getCountryId().isEmpty() || (!d.getCountryId().isEmpty() && EncryptionUtil.decrypt(d.getCountryId()) == null)) {
			            return new ResponseEntity<>("Please select country.", HttpStatus.BAD_REQUEST);
			        }
					
					if (d.getStateId().isEmpty() || (!d.getStateId().isEmpty() && EncryptionUtil.decrypt(d.getStateId()) == null)) {
			            return new ResponseEntity<>("Please select state.", HttpStatus.BAD_REQUEST);
			        }
					
					if (d.getDscIssued().isEmpty()) {
			            return new ResponseEntity<>("Please enter number of DSC Issued.", HttpStatus.BAD_REQUEST);
			        } else if (!d.getDscIssued().trim().matches("^(0|[1-9][0-9]*)$")) {
			            return new ResponseEntity<>("Only digits are allowed, and leading zeros are not permitted.", HttpStatus.BAD_REQUEST);
			        } else if (d.getDscIssued().trim().length() < 1 || d.getDscIssued().trim().length() > 10) {
			            return new ResponseEntity<>("The length must be between 1 and 10 characters.", HttpStatus.BAD_REQUEST);
			        }
					
					if (d.geteSignIssued().isEmpty()) {
			            return new ResponseEntity<>("Please enter number of eSign Issued.", HttpStatus.BAD_REQUEST);
			        } else if (!d.geteSignIssued().trim().matches("^(0|[1-9][0-9]*)$")) {
			            return new ResponseEntity<>("Only digits are allowed, and leading zeros are not permitted.", HttpStatus.BAD_REQUEST);
			        } else if (d.geteSignIssued().trim().length() < 1 || d.geteSignIssued().trim().length() > 10) {
			            return new ResponseEntity<>("The length must be between 1 and 10 characters.", HttpStatus.BAD_REQUEST);
			        }
					
					
					if(EncryptionUtil.decrypt(isesp).equals("false") && Long.parseLong(d.geteSignIssued())>0){
						return new ResponseEntity<>("You have not become an eSign Service Provider; therefore, you cannot issue eSign certificates.", HttpStatus.BAD_REQUEST);
					}
					
					
				
				}
				
				
				for(DataIssuedDTO d: obj.getDscesignList()) {
					
					String id = EncryptionUtil.decrypt(d.getDscesignIssuedId());
					
					if(id != null) {
						
						DSCeSignIssued uobj = dscServ.getDSCeSignIssuedById(Long.parseLong(id)); 
						uobj.setDscIssued(d.getDscIssued());
						uobj.seteSignIssued(d.geteSignIssued());
						uobj.setUpdated(new Date());
						uobj.setUpdatedBy(obj.getCaUsername());
						
						Optional<DSCeSignIssued> asp = dscServ.addDSCeSignIssued(uobj);
					}
				
					
				}
				
	            return new ResponseEntity<>("Your data has been successfully updated.", HttpStatus.OK);
				
				
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while updating. Please Try again.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	@GetMapping(DashboardAPIs.GET_ALL_DSC_ESIGN_ISSUED_BY_YEAR_MONTH_AND_USERNAME)
	public ResponseEntity<?> getAllDSCeSignIssuedByYearMonthAndUsername(@RequestParam("id") String username,
																		@RequestParam("pid") String month,
																		@RequestParam("qid") String year){
		
		String uname = EncryptionUtil.decrypt(username).trim();
		String mname = EncryptionUtil.decrypt(month).trim();
		String year1 = EncryptionUtil.decrypt(year).trim();
		
		
		try {
		List<DSCeSignIssued> list = dscServ.getAllDSCeSignIssuedByYearMonthAndUsername(uname, mname, year1);
		return new ResponseEntity<>(list, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
		}
		
	}
	
	
	@GetMapping(DashboardAPIs.VIEW_ALL_DSC_ESIGN_ISSUED)
	public ResponseEntity<?> viewAllDSCeSignIssued(@RequestParam("id") String username){
		try {
		    String uname = EncryptionUtil.decrypt(username);

		    String[] monthList = {
		        "January", "February", "March", "April", "May", "June", 
		        "July", "August", "September", "October", "November", "December"
		    };

		    List<DSCeSignIssued> list = dscServ.getDSCeSignIssuedByUsername(uname);

		    
		    Map<String, List<DSCeSignIssued>> groupedByYearAndMonth = list.stream()
		        .collect(Collectors.groupingBy(record -> record.getYear() + "-" + record.getMonth()));

		    
		    List<ViewDSCeSignIssuedDTO> result = groupedByYearAndMonth.entrySet().stream()
		        .map(entry -> {
		            String yearMonth = entry.getKey();
		            List<DSCeSignIssued> records = entry.getValue();

		    
		            String[] parts = yearMonth.split("-");
		            String year = parts[0];
		            String month = parts[1];

		    
		            int totalDSCIssued = records.stream()
		                .mapToInt(record -> Integer.parseInt(record.getDscIssued()))
		                .sum();

		            int totalEsignIssued = records.stream()
		                .mapToInt(record -> Integer.parseInt(record.geteSignIssued()))
		                .sum();

		    
		            ViewDSCeSignIssuedDTO dto = new ViewDSCeSignIssuedDTO();
		            dto.setMonth(month);
		            dto.setYear(year);
		            dto.setTotalDSCIssued(String.valueOf(totalDSCIssued));
		            dto.setTotalEsignIssued(String.valueOf(totalEsignIssued));

		            return dto;
		        })
		        .collect(Collectors.toList());

		    
		    List<ViewDSCeSignIssuedDTO> sortedResult = result.stream()
		    		.sorted(Comparator.comparing(ViewDSCeSignIssuedDTO::getYear).reversed()
		            .thenComparing(dto -> {

		                List<String> monthOrder = Arrays.asList(monthList);
		                return monthOrder.indexOf(dto.getMonth());
		            }))
		        .collect(Collectors.toList());

		    return new ResponseEntity<>(sortedResult, HttpStatus.OK);

		} catch (Exception e) {
		    return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
		}

		
	}
	
	
	
	
	@GetMapping(DashboardAPIs.GET_ALL_DSC_ESIGN_ISSUED)
	public ResponseEntity<?> getAllDSCeSignIssued(){
		try {
		List<DSCeSignIssued> list = dscServ.getAllDSCeSignIssued();
		return new ResponseEntity<>(list, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("Error in fetching data, try after some time.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@GetMapping(DashboardAPIs.GET_DSC_ESIGN_ISSUED_BY_ID)
	public ResponseEntity<?> getEKYCModeByID(@RequestParam("id") String id)
	{
		
		
		try {
			String  issueId = EncryptionUtil.decrypt(id);
			DSCeSignIssued obj = dscServ.getDSCeSignIssuedById(Long.parseLong(issueId));
			return new ResponseEntity<>(obj, HttpStatus.OK); 
		}catch(Exception e) {
			return new ResponseEntity<>("Invalid Id.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	@PostMapping(DashboardAPIs.ADD_ASP_DETAILS)
    public ResponseEntity<?> addASPDetails(@RequestBody ASPDetailsDTO obj) {
	
		try {
			
			
				Date onBoardingDate = null;
				Date exitDate = null;
				
				 // Server Side Validation
				
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		        try {
		        	onBoardingDate = dateFormat.parse(obj.getOnBoardingDate());
		        	
		        	if(obj.getExitDate() != null && !obj.getExitDate().equals(""))
		        		exitDate = dateFormat.parse(obj.getExitDate());   
		        }catch (ParseException e) {
		            return new ResponseEntity<>("Error parsing dates", HttpStatus.BAD_REQUEST);
		        }
			
						
				if (obj.getCountryId().isEmpty()) {
		            return new ResponseEntity<>("Please select country.", HttpStatus.BAD_REQUEST);
		        }
				
				if (obj.getStateId().isEmpty()) {
		            return new ResponseEntity<>("Please select state.", HttpStatus.BAD_REQUEST);
		        }
				
				
				if (obj.getAspName().isEmpty() || obj.getAspName().isEmpty()) {
		            return new ResponseEntity<>("Please enter ASP name.", HttpStatus.BAD_REQUEST);
		        } else if (!obj.getAspName().trim().matches("^[A-Za-z/().&\\- ]+$")) {
		            return new ResponseEntity<>("Only alphabets and /().-& are allowed.", HttpStatus.BAD_REQUEST);
		        } else if (obj.getAspName().trim().length() < 3 || obj.getAspName().trim().length() > 100) {
		            return new ResponseEntity<>("The length must be between 3 and 100 characters.", HttpStatus.BAD_REQUEST);
		        }
				
				if (obj.getEmailId().isEmpty()) {
		            return new ResponseEntity<>("Please enter email ID.", HttpStatus.BAD_REQUEST);
		        } else if (!obj.getEmailId().matches("^[A-Za-z0-9.]+@[A-Za-z]+.[a-zA-Z]+[a-zA-Z.]+$")) {
		            return new ResponseEntity<>("Please enter a valid email address.", HttpStatus.BAD_REQUEST);
		        }else if (obj.getEmailId().trim().length() < 3 || obj.getEmailId().trim().length() > 50) {
		            return new ResponseEntity<>("The length must be between 3 and 50 characters.", HttpStatus.BAD_REQUEST);
		        }
				
				if(onBoardingDate == null) {
					
		            return new ResponseEntity<>("Please select onboarding date.", HttpStatus.BAD_REQUEST);
					
				}
				
				if(exitDate != null) {
					
					if(exitDate.before(onBoardingDate) || exitDate.compareTo(onBoardingDate) == 0) {
				    	return new ResponseEntity<>("Exit date must be greater than Onboarding Date.", HttpStatus.BAD_REQUEST);
				    }
					
				}
				
				
			
				
				if(obj.getCaUsername().trim() == null || obj.getCaUsername().isBlank()) {
					return new ResponseEntity<>("Licensee details not found, try after some time.", HttpStatus.BAD_REQUEST);
				}
				
				
				//check name and email already added by the CA
				
				ASPDetails filterObjName = aspServ.getASPDetailsByUsernameAndName(EncryptionUtil.decrypt(obj.getCaUsername()), obj.getAspName());
				ASPDetails filterObjEmail = aspServ.getASPDetailsByUsernameAndEmailId(EncryptionUtil.decrypt(obj.getCaUsername()), obj.getEmailId());
				
				
		
				ASPDetails newObj = new ASPDetails();
				
				if(obj.getAspId() != null && !obj.getAspId().isBlank()) {
					
					
					if(filterObjName != null && filterObjEmail != null && filterObjName.getAspId() != filterObjEmail.getAspId()) {
						
						return new ResponseEntity<>("The ASP name is already registered with the provided email ID.", HttpStatus.BAD_REQUEST);
					}
					
					
					ASPDetails updateObj = aspServ.getASPDetailsById(Long.parseLong(EncryptionUtil.decrypt(obj.getAspId())));
					newObj = updateObj;
					
					newObj.setCountryId(obj.getCountryId());
					newObj.setUpdatedBy(obj.getCaUsername());
					newObj.setStateId(obj.getStateId());
					newObj.setAspName(obj.getAspName());
					newObj.setEmailId(obj.getEmailId());
					newObj.setExitDate(exitDate);
					newObj.setOnBoardingDate(onBoardingDate);
					newObj.setUpdated(new Date());
					
				}else {
					
					
					
					if(filterObjName != null) {
						
						return new ResponseEntity<>("The ASP Name is already registered. ", HttpStatus.BAD_REQUEST);
					}
					
					if(filterObjEmail != null) {
						return new ResponseEntity<>("The ASP name is already registered with the provided email ID.", HttpStatus.BAD_REQUEST);
					}
					
					newObj.setCaUsername(EncryptionUtil.decrypt(obj.getCaUsername()));
					newObj.setCountryId(obj.getCountryId());
					newObj.setCreatedBy(obj.getCaUsername());
					newObj.setUpdatedBy(obj.getCaUsername());
					newObj.setStateId(obj.getStateId());
					newObj.setAspName(obj.getAspName());
					newObj.setEmailId(obj.getEmailId());
					newObj.setExitDate(exitDate);
					newObj.setOnBoardingDate(onBoardingDate);

					
				}
							
				
				Optional<ASPDetails> asp = aspServ.addASPDetails(newObj);
				
				if (asp.isEmpty()) {
	                return new ResponseEntity<>("An error occurred while saving. Please Try again.", HttpStatus.BAD_REQUEST);
	            }
	            return new ResponseEntity<>("Your data has been successfully added.", HttpStatus.OK);
				
				
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("An error occurred while saving. Please Try again.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	
	
	@GetMapping(DashboardAPIs.GET_ALL_ASP)
	public ResponseEntity<?> getAllASP(){
		
		try {
		List<ASPDetails> list = aspServ.getAllASPDetails();
		return new ResponseEntity<>(list, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("Error in fetching data, try after some time.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@GetMapping(DashboardAPIs.GET_ASP_BY_ID)
	public ResponseEntity<?> getASPByID(@RequestParam("id") String id)
	{
		try {
			String  issueId = EncryptionUtil.decrypt(id);
			ASPDetails obj = aspServ.getASPDetailsById(Long.parseLong(issueId));
			return new ResponseEntity<>(obj, HttpStatus.OK); 
		}catch(Exception e) {
			return new ResponseEntity<>("Invalid Id.", HttpStatus.BAD_REQUEST);
		}
		
	}

	
	
	//-----------Dashboard Data----------------
	
	
	@GetMapping(DashboardAPIs.GET_CCA_DASHBOARD_DETAILS)
	public ResponseEntity<?> getCCADashboardDetails()
	{
		try {
			List<ClientLicenseDetailsDTO> ccalist = clientServ.getAllActiveLicenseDetails();
			List<ESPDTO> esplist = clientServ.getAllApprovedESP();
			List<DSCeSignIssued> dsceSignIssued = dscServ.getAllDSCeSignIssued();
			
			//total esp
			long totalESP = esplist.stream()
		            .map(ESPDTO::getUserName) 
		            .distinct()              
		            .count();
			
			long totalDSCIssued = 0;
			long totaleSignIssued = 0;
			
			for(DSCeSignIssued e: dsceSignIssued) {
				
				totalDSCIssued += Long.parseLong(e.getDscIssued());
				totaleSignIssued += Long.parseLong(e.geteSignIssued());
			}
			
			CCADashboardResponse res = new CCADashboardResponse();
			res.setTotalCA(Long.valueOf(ccalist.size()));
			res.setTotalESP(totalESP);
			res.setTotalDSCIssued(totalDSCIssued);
			res.setTotaleSignIssued(totaleSignIssued);
			
			return new ResponseEntity<>(res, HttpStatus.OK); 
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	@GetMapping(DashboardAPIs.GET_ALL_ACTIVE_CA)
	public ResponseEntity<?> getAllActiveCA(){
		
		try {
		List<ClientLicenseDetailsDTO> ccalist = clientServ.getAllActiveLicenseDetails();
		return new ResponseEntity<>(ccalist, HttpStatus.OK); 
		}catch(Exception e) {
			return new ResponseEntity<>("Error in fetching data, try after some time.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@GetMapping(DashboardAPIs.GET_ALL_ACTIVE_ESP)
	public ResponseEntity<?> getAllActiveESP(){
		
		try {
		List<ClientLicenseDetailsDTO> ccalist = clientServ.getAllActiveLicenseDetails();
		List<ESPDTO> esplist = clientServ.getAllApprovedESP();
		
		List<ESPDetailsResponse> response = new ArrayList<>();
		
		
		for(ESPDTO e: esplist) {
			
			ClientLicenseDetailsDTO d = ccalist.stream()
										.filter(ca->ca.getUserName().equals(e.getUserName()))
										.findFirst()
										.orElse(null);
			
		
			ESPDetailsResponse espRes = new ESPDetailsResponse();
			espRes.setEmpanelmentDate(e.getEmpanelmentDate());
			espRes.setLicenseeName(d.getLicenseName());
			espRes.setLicenseExpiryDate(d.getExpiryDate());
			espRes.setLicenseIssueDate(d.getIssueDate());
			
			response.add(espRes);
			
		}
		
		
		return new ResponseEntity<>(response, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("Error in fetching data, try after some time.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	@GetMapping(DashboardAPIs.GET_TOTAL_ESIGN_AND_DSC_ISSUED)
	public ResponseEntity<?> getAllEsignAndDSCIssued(){
		
		try {
			List<ClientLicenseDetailsDTO> ccalist = clientServ.getAllActiveLicenseDetails();
			List<DSCeSignIssued> dsceSignIssued = dscServ.getAllDSCeSignIssued();
			
			
			
			List<TotalDSCeSignIssuedResponse> response = new ArrayList<>();
			
			
			for(ClientLicenseDetailsDTO e: ccalist) {
				
				long totalDSCIssued  = dsceSignIssued.stream()
										.filter(ca->ca.getCaUsername().equals(e.getUserName()))
										.mapToLong(ca->Long.parseLong(ca.getDscIssued()))
										.sum();
											
				long totaleSignIssued = dsceSignIssued.stream()
										.filter(ca->ca.getCaUsername().equals(e.getUserName()))
										.mapToLong(ca->Long.parseLong(ca.geteSignIssued()))
										.sum();
				
				
			
				TotalDSCeSignIssuedResponse r = new TotalDSCeSignIssuedResponse();
				r.setDscIssued(totalDSCIssued);
				r.seteSignIssued(totaleSignIssued);
				r.setLicenseeName(e.getLicenseName());
				
				response.add(r);
				
			}
			
			
			return new ResponseEntity<>(response, HttpStatus.OK); 
		}catch(Exception e) {
			return new ResponseEntity<>("Error in fetching data, try after some time.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	
	@PostMapping(DashboardAPIs.GET_GRAPH_DATA_ESIGN_DSC)
    public ResponseEntity<?> getGraphDataEsignDSC(@RequestBody CCAGraphDataRequest obj) {
	
		try {
		
			String decodedSelectedCA = new String(Base64.getDecoder().decode(obj.getSelectedCA()));
		    String decodedSelectedMonth = new String(Base64.getDecoder().decode(obj.getSelectedMonth()));
		    String decodedSelectedState = new String(Base64.getDecoder().decode(obj.getSelectedState()));
		    String decodedSelectedYear = new String(Base64.getDecoder().decode(obj.getSelectedYear()));
		
		    
		    List<String> ca = Arrays.asList(decodedSelectedCA.split(",\\s*"));
		    List<String> month = Arrays.asList(decodedSelectedMonth.split(",\\s*"));
		    List<String> state = Arrays.asList(decodedSelectedState.split(",\\s*"));
		    List<String> year = Arrays.asList(decodedSelectedYear.split(",\\s*"));
		    
		
		   List<DSCeSignIssued> alldsc = dscServ.getAllEsignDSCByCAMonthStateAndYear(ca, month, state, year);
		    
		
		   //Year wise
		   
		   //DSC
		   Map<Integer, Long> sortedDscIssuedByYear = alldsc.stream()
	                .filter(dsc -> dsc.getYear() != null && dsc.getDscIssued() != null)
	                .collect(Collectors.groupingBy(
	                        dsc -> Integer.parseInt(dsc.getYear()),
	                        Collectors.summingLong(dsc -> Long.parseLong(dsc.getDscIssued())) 
	                ))
	                .entrySet().stream()
	                .sorted(Map.Entry.<Integer, Long>comparingByKey().reversed()) 
	                .collect(Collectors.toMap(
	                        Map.Entry::getKey, 
	                        Map.Entry::getValue, 
	                        (oldValue, newValue) -> oldValue, 
	                        LinkedHashMap::new 
	                ));

		   //eSign
		   Map<Integer, Long> sortedeSignIssuedByYear = alldsc.stream()
				   				.filter(esign -> esign.getYear() != null && esign.geteSignIssued() != null)
				   				.collect(Collectors.groupingBy(
				   						esign -> Integer.parseInt(esign.getYear()),
				   						Collectors.summingLong(esign -> Long.parseLong(esign.geteSignIssued()))
				   				))
				   				.entrySet().stream()
				   				.sorted(Map.Entry.<Integer, Long>comparingByKey().reversed())
				   				.collect(Collectors.toMap(
				   						Map.Entry::getKey,
				   						Map.Entry::getValue,
				   						(oldValue, newValue) -> oldValue,
				   						LinkedHashMap::new
				   				));


		   
		   
		   //Month Wise
		   
	        String[] monthlist = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

	        // DSC 
	        Map<String, Long> sortedDscIssuedByMonth = Arrays.stream(monthlist)
	                .collect(Collectors.toMap(
	                        m1 -> m1,
	                        m1 -> alldsc.stream()
	                                .filter(dsc -> dsc.getMonth() != null && dsc.getMonth().equals(m1) && dsc.getDscIssued() != null)
	                                .mapToLong(dsc -> Long.parseLong(dsc.getDscIssued()))
	                                .sum(),
	                        (oldValue, newValue) -> oldValue, 
	                        LinkedHashMap::new 
	                ));

	        // eSign 
	        Map<String, Long> sortedeSignIssuedByMonth = Arrays.stream(monthlist)
	                .collect(Collectors.toMap(
	                        m2 -> m2, 
	                        m2 -> alldsc.stream()
	                                .filter(esign -> esign.getMonth() != null && esign.getMonth().equals(m2) && esign.geteSignIssued() != null)
	                                .mapToLong(esign -> Long.parseLong(esign.geteSignIssued()))
	                                .sum(), 
	                        (oldValue, newValue) -> oldValue, 
	                        LinkedHashMap::new 
	                ));

		   
		   
		   
	        
	        //State wise
	        
	        List<StateDTO> allstates = clientServ.getAllStates();
	        
	        Map<String, String> stateIdToNameMap = allstates.stream()
	                								.collect(Collectors.toMap(s1->EncryptionUtil.encrypt(s1.getStateId().toString()), StateDTO::getStateName, (oldVal, newVal) -> oldVal));

	        //DSC
	        Map<String, Long> sortedDscIssuedByState = alldsc.stream()
	                .filter(dsc -> dsc.getStateId() != null && dsc.getDscIssued() != null)
	                .collect(Collectors.groupingBy(
	                        DSCeSignIssued::getStateId,
	                        Collectors.summingLong(dsc -> Long.parseLong(dsc.getDscIssued()))
	                ))
	                .entrySet().stream()
	                .sorted(Map.Entry.<String, Long>comparingByKey())
	                .collect(Collectors.toMap(
	                        entry -> stateIdToNameMap.getOrDefault(entry.getKey(), entry.getKey()),
	                        Map.Entry::getValue,
	                        (oldValue, newValue) -> oldValue,
	                        LinkedHashMap::new
	                ));

	        // eSign 
	        Map<String, Long> sortedeSignIssuedByState = alldsc.stream()
	                .filter(esign -> esign.getYear() != null && esign.geteSignIssued() != null)
	                .collect(Collectors.groupingBy(
	                        DSCeSignIssued::getStateId,
	                        Collectors.summingLong(esign -> Long.parseLong(esign.geteSignIssued()))
	                ))
	                .entrySet().stream()
	                .sorted(Map.Entry.<String, Long>comparingByKey())
	                .collect(Collectors.toMap(
	                        entry -> stateIdToNameMap.getOrDefault(entry.getKey(), entry.getKey()), 
	                        Map.Entry::getValue,
	                        (oldValue, newValue) -> oldValue,
	                        LinkedHashMap::new
	                ));
	        

	        
	        
	        
	        //CA wise
	        
	        List<ClientLicenseDetailsDTO> allLicensee = clientServ.getAllActiveLicenseDetails();
	        
	        Map<String, String> userNameToLicenseeName = allLicensee.stream()
	                								.collect(Collectors.toMap(ClientLicenseDetailsDTO::getUserName, ClientLicenseDetailsDTO::getLicenseName, (oldVal, newVal) -> oldVal));

	        //DSC
	        Map<String, Long> sortedDscIssuedByCA = alldsc.stream()
	                .filter(dsc -> dsc.getCaUsername() != null && dsc.getDscIssued() != null)
	                .collect(Collectors.groupingBy(
	                        DSCeSignIssued::getCaUsername,
	                        Collectors.summingLong(dsc -> Long.parseLong(dsc.getDscIssued()))
	                ))
	                .entrySet().stream()
	                .sorted(Map.Entry.<String, Long>comparingByKey())
	                .collect(Collectors.toMap(
	                        entry -> userNameToLicenseeName.getOrDefault(entry.getKey(), entry.getKey()),
	                        Map.Entry::getValue,
	                        (oldValue, newValue) -> oldValue,
	                        LinkedHashMap::new
	                ));

	        // eSign 
	        Map<String, Long> sortedeSignIssuedByCA = alldsc.stream()
	                .filter(esign -> esign.getCaUsername() != null && esign.geteSignIssued() != null)
	                .collect(Collectors.groupingBy(
	                        DSCeSignIssued::getCaUsername,
	                        Collectors.summingLong(esign -> Long.parseLong(esign.geteSignIssued()))
	                ))
	                .entrySet().stream()
	                .sorted(Map.Entry.<String, Long>comparingByKey())
	                .collect(Collectors.toMap(
	                        entry -> userNameToLicenseeName.getOrDefault(entry.getKey(), entry.getKey()), 
	                        Map.Entry::getValue,
	                        (oldValue, newValue) -> oldValue,
	                        LinkedHashMap::new
	                ));

	        
	        
	        
	        
		   
		   	//convert to graph format
		   
		   CCAGraphDataResponse graphResponse = new CCAGraphDataResponse();
		   
		   
		   
		   //Year wise
		   List<DSCeSignIssuedPayload> yearwisedata = new ArrayList<>();
		   List<Integer> yearlist = new ArrayList<>();
		   List<Long> dscIssued = new ArrayList<>();
		   List<Long> esignIssued = new ArrayList<>();
		   
		   sortedDscIssuedByYear.forEach((years, count) ->{
				yearlist.add(years);
				dscIssued.add(count);
				esignIssued.add(sortedeSignIssuedByYear.getOrDefault(years, 0L));   
		   	}
           );
		   
		   DSCeSignIssuedPayload yearwisedsc = new DSCeSignIssuedPayload();
		   yearwisedsc.setName("DSC Issued");
		   yearwisedsc.setData(dscIssued);

		   DSCeSignIssuedPayload yearwiseesign = new DSCeSignIssuedPayload();
		   yearwiseesign.setName("eSign Issued");
		   yearwiseesign.setData(esignIssued);
		   
		   yearwisedata.add(yearwisedsc);
		   yearwisedata.add(yearwiseesign);
		   
		   
		   
		   //month wise
		   List<DSCeSignIssuedPayload> monthwisedata = new ArrayList<>();
		   List<String> mlist = Arrays.asList(monthlist);
		   List<Long> mdscIssued = new ArrayList<>();
		   List<Long> mesignIssued = new ArrayList<>();
		   
		   sortedDscIssuedByMonth.forEach((months, count) ->{
				mdscIssued.add(count);
				mesignIssued.add(sortedeSignIssuedByMonth.getOrDefault(months, 0L));   
		   	}
           );
		   
		   
		   
		   
		   DSCeSignIssuedPayload monthwisedsc = new DSCeSignIssuedPayload();
		   monthwisedsc.setName("DSC Issued");
		   monthwisedsc.setData(mdscIssued);

		   DSCeSignIssuedPayload monthwiseesign = new DSCeSignIssuedPayload();
		   monthwiseesign.setName("eSign Issued");
		   monthwiseesign.setData(mesignIssued);
		   
		   monthwisedata.add(monthwisedsc);
		   monthwisedata.add(monthwiseesign);
		   
		   
		   
		   //State Wise

		   List<DSCeSignIssuedPayload> statewisedata = new ArrayList<>();
		   List<String> statelist = new ArrayList<>();
		   List<Long> sdscIssued = new ArrayList<>();
		   List<Long> sesignIssued = new ArrayList<>();
		   
		   sortedDscIssuedByState.forEach((s1, count) ->{
			   statelist.add(s1);
				sdscIssued.add(count);
				sesignIssued.add(sortedeSignIssuedByState.getOrDefault(s1, 0L));   
		   	}
           );
		   
		  
		   DSCeSignIssuedPayload statewisedsc = new DSCeSignIssuedPayload();
		   statewisedsc.setName("DSC Issued");
		   statewisedsc.setData(sdscIssued);

		   DSCeSignIssuedPayload statewiseesign = new DSCeSignIssuedPayload();
		   statewiseesign.setName("eSign Issued");
		   statewiseesign.setData(sesignIssued);
		   
		   statewisedata.add(statewisedsc);
		   statewisedata.add(statewiseesign);

		   
		   // CA Wise
		 
		   
		   List<DSCeSignIssuedPayload> cawisedata = new ArrayList<>();
		   List<String> calist = new ArrayList<>();
		   List<Long> cadscIssued = new ArrayList<>();
		   List<Long> caesignIssued = new ArrayList<>();
		   
		   sortedDscIssuedByCA.forEach((ca1, count) ->{
			   calist.add(ca1);
			   cadscIssued.add(count);
			   caesignIssued.add(sortedeSignIssuedByCA.getOrDefault(ca1, 0L));   
		   	}
           );
		   
		  
		   DSCeSignIssuedPayload cawisedsc = new DSCeSignIssuedPayload();
		   cawisedsc.setName("DSC Issued");
		   cawisedsc.setData(cadscIssued);

		   DSCeSignIssuedPayload cawiseesign = new DSCeSignIssuedPayload();
		   cawiseesign.setName("eSign Issued");
		   cawiseesign.setData(caesignIssued);
		   
		   cawisedata.add(cawisedsc);
		   cawisedata.add(cawiseesign);
		   
		   
		   //------ CA with year of licensing
		   
		   
		   List<ClientLicenseDetailsDTO> allcalists = clientServ.getAllLicenseDetails();
		   
		   List<ClientLicenseDetailsDTO> allcalist = allcalists.stream()
				    .filter(clientLicense -> ca.contains(clientLicense.getUserName()))
				    .collect(Collectors.toList());
		   
		   List<CAWithYearOfLicensing> caWithYearList = allcalist.stream()
				    
				    .collect(Collectors.groupingBy(ClientLicenseDetailsDTO::getUserName))
				    .entrySet().stream()
				    .map(entry -> {
				        String licenseeName = entry.getValue().get(0).getLicenseName();
				        
				       
				        List<CALicenseDetailsDTO> caLicense = entry.getValue().stream()
				            .sorted((client1, client2) -> {
				                if (client1.getIssueDate() == null && client2.getIssueDate() == null) return 0;
				                if (client1.getIssueDate() == null) return 1;
				                if (client2.getIssueDate() == null) return -1;
				                return client2.getIssueDate().compareTo(client1.getIssueDate());
				            })
				            .map(clientLicense -> {
				                CALicenseDetailsDTO caLicenseDetails = new CALicenseDetailsDTO();
				               
				                caLicenseDetails.setCertificateNumber(clientLicense.getSerialNo()); 
				                caLicenseDetails.setValidFrom(clientLicense.getIssueDate()); 
				                caLicenseDetails.setValidTo(clientLicense.getExpiryDate()); 
				                return caLicenseDetails;
				            })
				            .collect(Collectors.toList());

				        
				        CAWithYearOfLicensing caWithYear = new CAWithYearOfLicensing();
				        caWithYear.setCaName(licenseeName); 
				        caWithYear.setCaLicense(caLicense); 
				        return caWithYear;
				    })
				    .collect(Collectors.toList());
		   
		   
		   //ESP wise approval of eKYC Modes
		  
		   
		   List<ESPWithEkycModeDTO> esplist = clientServ.getAllESPWithEkycModeApproved().stream()
				    .filter(esp -> ca.contains(esp.getEspUserName()))
				    .collect(Collectors.toList());
		   
		   List<ESPWithEkycModeDTO> updatedEsplist = esplist.stream()
				    .map(esp -> {
				        
				        Optional<ClientLicenseDetailsDTO> matchingLicensee = allcalists.stream()
				            .filter(licensee -> licensee.getUserName().equals(esp.getEspUserName()))
				            .findFirst();
				        
				        // If a matching LicenseeDetails is found, update the espUserName with the licenseeName
				        matchingLicensee.ifPresent(licensee -> esp.setEspUserName(licensee.getLicenseName()));
				        
				        return esp;
				    })
				    .collect(Collectors.toList());
		   
		   graphResponse.setYears(yearlist);	   				
	       graphResponse.setYearwise(yearwisedata);
	       graphResponse.setMonths(mlist);
	       graphResponse.setMonthwise(monthwisedata);
	       graphResponse.setStates(statelist);
	       graphResponse.setStatewise(statewisedata);
		   graphResponse.setCa(calist);
		   graphResponse.setCawise(cawisedata);
		   graphResponse.setCayoflicensing(caWithYearList);
		   graphResponse.setEspEkycModeApproved(updatedEsplist);
	       return new ResponseEntity<>(graphResponse, HttpStatus.OK);
				
				
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error fetching graph data: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
	}


	
	@GetMapping(DashboardAPIs.GET_LICENSEE_DASHBOARD_DETAILS)
	public ResponseEntity<?> getLicenseeDashboardDetails(@RequestParam("id") String id)
	{
		try {
			
			String userName = EncryptionUtil.decrypt(id);
			
			List<DSCeSignIssued> dsceSignIssued = dscServ.getDSCeSignIssuedByUsername(userName);
			List<ASPDetails> aspDetails = aspServ.getASPDetailsByUsername(userName);			
			
			long totalDSCIssued = 0;
			long totaleSignIssued = 0;
			
			for(DSCeSignIssued e: dsceSignIssued) {
				
				totalDSCIssued += Long.parseLong(e.getDscIssued());
				totaleSignIssued += Long.parseLong(e.geteSignIssued());
			}
			
			LicenseeDashboardResponse res = new LicenseeDashboardResponse();
			
			
			res.setTotalDSCIssued(totalDSCIssued);
			res.setTotaleSignIssued(totaleSignIssued);
			res.setTotalASP(Long.valueOf(aspDetails.size()));
			
			return new ResponseEntity<>(res, HttpStatus.OK); 
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	/*---------------------Reports------------------*/
	
	@GetMapping(DashboardAPIs.GET_MONTHLY_DSC_ESIGN_ISSUED)
	public ResponseEntity<?> getMonthlyDSCEsignData(@RequestParam("id") String month, @RequestParam("pid") String year)
	{
		
		
		try {
			String  months = EncryptionUtil.decrypt(month);
			String years = EncryptionUtil.decrypt(year);

			
			
			List<ClientLicenseDetailsDTO> ccalist = clientServ.getAllActiveLicenseDetails();
			List<DSCeSignIssued> dsceSignIssued = dscServ.getMonthlyDSCEsignData(months, years);
			
			
			
			List<TotalDSCeSignIssuedResponse> response = new ArrayList<>();
			
			
			for(ClientLicenseDetailsDTO e: ccalist) {
				
				long totalDSCIssued  = dsceSignIssued.stream()
										.filter(ca->ca.getCaUsername().equals(e.getUserName()))
										.mapToLong(ca->Long.parseLong(ca.getDscIssued()))
										.sum();
											
				long totaleSignIssued = dsceSignIssued.stream()
										.filter(ca->ca.getCaUsername().equals(e.getUserName()))
										.mapToLong(ca->Long.parseLong(ca.geteSignIssued()))
										.sum();
				
				
			
				TotalDSCeSignIssuedResponse r = new TotalDSCeSignIssuedResponse();
				r.setDscIssued(totalDSCIssued);
				r.seteSignIssued(totaleSignIssued);
				r.setLicenseeName(e.getLicenseName());
				
				response.add(r);
				
			}
			
			
			return new ResponseEntity<>(response, HttpStatus.OK); 

		}catch(Exception e) {
			return new ResponseEntity<>("Unable to get monthly report data.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	@GetMapping(DashboardAPIs.DOWNLOAD_MONTHLY_REPORT_DSC_ESIGN_ISSUED)
	public ResponseEntity<?> downloadMonthlyDSCEsignDataReport(@RequestParam("id") String month, @RequestParam("pid") String year) {
	    try {
	        String months = EncryptionUtil.decrypt(month);
	        String years = EncryptionUtil.decrypt(year);

	        List<ClientLicenseDetailsDTO> ccalist = clientServ.getAllActiveLicenseDetails();
	        List<DSCeSignIssued> dsceSignIssued = dscServ.getMonthlyDSCEsignData(months, years);

	        List<TotalDSCeSignIssuedResponse> response = new ArrayList<>();

	        for (ClientLicenseDetailsDTO e : ccalist) {
	            long totalDSCIssued = dsceSignIssued.stream()
	                .filter(ca -> ca.getCaUsername().equals(e.getUserName()))
	                .mapToLong(ca -> Long.parseLong(ca.getDscIssued()))
	                .sum();

	            long totaleSignIssued = dsceSignIssued.stream()
	                .filter(ca -> ca.getCaUsername().equals(e.getUserName()))
	                .mapToLong(ca -> Long.parseLong(ca.geteSignIssued()))
	                .sum();

	            TotalDSCeSignIssuedResponse r = new TotalDSCeSignIssuedResponse();
	            r.setDscIssued(totalDSCIssued);
	            r.seteSignIssued(totaleSignIssued);
	            r.setLicenseeName(e.getLicenseName());
	            response.add(r);
	        }

	        // Generate HTML content
	        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
	        StringBuilder contentBuilder = new StringBuilder();
	        contentBuilder.append("<html>")
	            .append("<head>")
	            .append("<style>")
	            .append("@page { margin: 20pt; }")
	            .append("body { font-size: 16px; font-family: \"Times New Roman\", Times, serif; }")
	            .append("table { border-collapse: collapse; width: 100%;} td, th { border: 1px solid black; padding: 5px; text-align: left;}")
	            .append("</style>")
	            .append("</head>")
	            .append("<body>")
	            .append("<div style=\"text-align: center; font-size: 24px;\"><b>Controller of Certifying Authorities (CCA)</b></div>")
	            .append("<div style=\"text-align: center; font-size: 18px;\"><u>Monthly Report of eSign & DSC</u></div>")
	            .append("<p style=\"text-align: right;\"><b>As on ").append(currentDate).append("</b></p>")
	            .append("<p style=\"margin-top: 20px;text-align: center;text-justify: inter-word; font-weight: 600;\">Monthly report of eSign and Digital Signature Certificate(s) (DSCs) issued by each CA/ESP under the Controller of Certifying Authorities (CCA)</p>")
	            .append("<table>")
	            .append("<thead><tr><th>Sl. No.</th><th>CA/ESP Name</th><th>"+months+", "+years+" (eSign)</th><th>"+months+", "+years+" (DSC)</th></tr></thead>")
	            .append("<tbody>");

	        int serialNumber = 1;
	        
	        Long totalDSCIssued = 0L;
	        Long totalEsingIssued = 0L;
	        
	        for (TotalDSCeSignIssuedResponse record : response) {
	            contentBuilder.append("<tr>")
	                .append("<td>").append(serialNumber++).append(".</td>")
	                .append("<td>").append(record.getLicenseeName()).append("</td>")
	                .append("<td>").append(record.geteSignIssued()).append("</td>")
	                .append("<td>").append(record.getDscIssued()).append("</td>")
	                .append("</tr>");
	            
	            totalDSCIssued += record.getDscIssued();
	            totalEsingIssued += record.geteSignIssued();
	        }
	        
	        contentBuilder.append("<tr><td colspan=2><b>Total:</b> </td><td>"+totalEsingIssued+"</td><td>"+totalDSCIssued+"</td></tr>");
	        
	        contentBuilder.append("</tbody></table></body></html>");

	        // Convert HTML to PDF
	        String htmlContent = contentBuilder.toString();
	        byte[] pdfBytes;
	        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
	            ConverterProperties converterProperties = new ConverterProperties();
	            HtmlConverter.convertToPdf(htmlContent, outputStream, converterProperties);
	            pdfBytes = outputStream.toByteArray();
	        }

	        // Return PDF response
	        return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=MonthlyReport.pdf")
	            .contentType(MediaType.APPLICATION_PDF)
	            .body(pdfBytes);

	    } catch (Exception e) {
	        
	        return new ResponseEntity<>("Unable to get monthly report data.", HttpStatus.BAD_REQUEST);
	    }
	}
	
	
	
	@GetMapping(DashboardAPIs.GET_YEARLY_DSC_ESIGN_ISSUED)
	public ResponseEntity<?> getYearlyDSCEsignData(@RequestParam("id") String year)
	{
		
		
		try {
			
			String years = EncryptionUtil.decrypt(year);

			
			
			List<ClientLicenseDetailsDTO> ccalist = clientServ.getAllActiveLicenseDetails();
			List<DSCeSignIssued> dsceSignIssued = dscServ.getYearlyDSCEsignData(years);
			
			
			
			List<TotalDSCeSignIssuedResponse> response = new ArrayList<>();
			
			
			for(ClientLicenseDetailsDTO e: ccalist) {
				
				long totalDSCIssued  = dsceSignIssued.stream()
										.filter(ca->ca.getCaUsername().equals(e.getUserName()))
										.mapToLong(ca->Long.parseLong(ca.getDscIssued()))
										.sum();
											
				long totaleSignIssued = dsceSignIssued.stream()
										.filter(ca->ca.getCaUsername().equals(e.getUserName()))
										.mapToLong(ca->Long.parseLong(ca.geteSignIssued()))
										.sum();
				
				
			
				TotalDSCeSignIssuedResponse r = new TotalDSCeSignIssuedResponse();
				r.setDscIssued(totalDSCIssued);
				r.seteSignIssued(totaleSignIssued);
				r.setLicenseeName(e.getLicenseName());
				
				response.add(r);
				
			}
			
			
			return new ResponseEntity<>(response, HttpStatus.OK); 

		}catch(Exception e) {
			return new ResponseEntity<>("Unable to get yearly report data.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	@GetMapping(DashboardAPIs.DOWNLOAD_YEARLY_REPORT_DSC_ESIGN_ISSUED)
	public ResponseEntity<?> downloadYearlyDSCEsignDataReport(@RequestParam("id") String year) {
	    try {
	       
	        String years = EncryptionUtil.decrypt(year);

	        List<ClientLicenseDetailsDTO> ccalist = clientServ.getAllActiveLicenseDetails();
	        List<DSCeSignIssued> dsceSignIssued = dscServ.getYearlyDSCEsignData(years);

	        List<TotalDSCeSignIssuedResponse> response = new ArrayList<>();

	        for (ClientLicenseDetailsDTO e : ccalist) {
	            long totalDSCIssued = dsceSignIssued.stream()
	                .filter(ca -> ca.getCaUsername().equals(e.getUserName()))
	                .mapToLong(ca -> Long.parseLong(ca.getDscIssued()))
	                .sum();

	            long totaleSignIssued = dsceSignIssued.stream()
	                .filter(ca -> ca.getCaUsername().equals(e.getUserName()))
	                .mapToLong(ca -> Long.parseLong(ca.geteSignIssued()))
	                .sum();

	            TotalDSCeSignIssuedResponse r = new TotalDSCeSignIssuedResponse();
	            r.setDscIssued(totalDSCIssued);
	            r.seteSignIssued(totaleSignIssued);
	            r.setLicenseeName(e.getLicenseName());
	            response.add(r);
	        }

	        // Generate HTML content
	        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
	        StringBuilder contentBuilder = new StringBuilder();
	        contentBuilder.append("<html>")
	            .append("<head>")
	            .append("<style>")
	            .append("@page { margin: 20pt; }")
	            .append("body { font-size: 16px; font-family: \"Times New Roman\", Times, serif; }")
	            .append("table { border-collapse: collapse; width: 100%;} td, th { border: 1px solid black; padding: 5px; text-align: left;}")
	            .append("</style>")
	            .append("</head>")
	            .append("<body>")
	            .append("<div style=\"text-align: center; font-size: 24px;\"><b>Controller of Certifying Authorities (CCA)</b></div>")
	            .append("<div style=\"text-align: center; font-size: 18px;\"><u>Yearly Report of eSign & DSC</u></div>")
	            .append("<p style=\"text-align: right;\"><b>As on ").append(currentDate).append("</b></p>")
	            .append("<p style=\"margin-top: 20px;text-align: center;text-justify: inter-word; font-weight: 600;\">Yearly report of eSign and Digital Signature Certificate(s) (DSCs) issued by each CA/ESP under the Controller of Certifying Authorities (CCA)</p>")
	            .append("<table>")
	            .append("<thead><tr><th>Sl. No.</th><th>CA/ESP Name</th><th>"+years+" (eSign)</th><th>"+years+" (DSC)</th></tr></thead>")
	            .append("<tbody>");

	        int serialNumber = 1;
	        
	        Long totalDSCIssued = 0L;
	        Long totalEsingIssued = 0L;
	        	        
	        
	        for (TotalDSCeSignIssuedResponse record : response) {
	            contentBuilder.append("<tr>")
	                .append("<td>").append(serialNumber++).append(".</td>")
	                .append("<td>").append(record.getLicenseeName()).append("</td>")
	                .append("<td>").append(record.geteSignIssued()).append("</td>")
	                .append("<td>").append(record.getDscIssued()).append("</td>")
	                .append("</tr>");
	            
	            totalDSCIssued += record.getDscIssued();
	            totalEsingIssued += record.geteSignIssued();
	            
	        }
	        
	        contentBuilder.append("<tr><td colspan=2><b>Total:</b> </td><td>"+totalEsingIssued+"</td><td>"+totalDSCIssued+"</td></tr>");
	        contentBuilder.append("</tbody></table></body></html>");

	        // Convert HTML to PDF
	        String htmlContent = contentBuilder.toString();
	        byte[] pdfBytes;
	        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
	            ConverterProperties converterProperties = new ConverterProperties();
	            HtmlConverter.convertToPdf(htmlContent, outputStream, converterProperties);
	            pdfBytes = outputStream.toByteArray();
	        }

	        // Return PDF response
	        return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=YearlyReport.pdf")
	            .contentType(MediaType.APPLICATION_PDF)
	            .body(pdfBytes);

	    } catch (Exception e) {
	        
	        return new ResponseEntity<>("Unable to get yearly report data.", HttpStatus.BAD_REQUEST);
	    }
	}
	
	
	@GetMapping(DashboardAPIs.GET_CUSTOMIZED_PERIOD_DSC_ESIGN_ISSUED)
	public ResponseEntity<?> getCustomizedPeriodDSCEsignData(@RequestParam("id") String month, @RequestParam("pid") String year, @RequestParam("qid") String month1, @RequestParam("rid") String year1)
	{
		
		
		try {
			
			String months = EncryptionUtil.decrypt(month);
			String years = EncryptionUtil.decrypt(year);
			String months1 = EncryptionUtil.decrypt(month1);
			String years1 = EncryptionUtil.decrypt(year1);

			
			
			List<ClientLicenseDetailsDTO> ccalist = clientServ.getAllActiveLicenseDetails();
			
			
			
	        int startMonth = getMonthNumber(months);
	        int startYear = Integer.parseInt(years);
	        int endMonth = getMonthNumber(months1);
	        int endYear = Integer.parseInt(years1);
	        
	        
	        List<DSCeSignIssued> dsceSignIssued1 = dscServ.getAllDSCeSignIssued();
	        
	        List<DSCeSignIssued> dsceSignIssued =  dsceSignIssued1.stream()
	                .filter(d -> {
	                    int recordYear = Integer.parseInt(d.getYear());
	                    int recordMonth = getMonthNumber(d.getMonth());
	                    
	                    
	                    return (recordYear > startYear || 
	                            (recordYear == startYear && recordMonth >= startMonth)) && 
	                           (recordYear < endYear || 
	                            (recordYear == endYear && recordMonth <= endMonth));
	                })
	                .collect(Collectors.toList());
			
			
			
			List<TotalDSCeSignIssuedResponse> response = new ArrayList<>();
			
			
			for(ClientLicenseDetailsDTO e: ccalist) {
				
				long totalDSCIssued  = dsceSignIssued.stream()
										.filter(ca->ca.getCaUsername().equals(e.getUserName()))
										.mapToLong(ca->Long.parseLong(ca.getDscIssued()))
										.sum();
											
				long totaleSignIssued = dsceSignIssued.stream()
										.filter(ca->ca.getCaUsername().equals(e.getUserName()))
										.mapToLong(ca->Long.parseLong(ca.geteSignIssued()))
										.sum();
				
				
			
				TotalDSCeSignIssuedResponse r = new TotalDSCeSignIssuedResponse();
				r.setDscIssued(totalDSCIssued);
				r.seteSignIssued(totaleSignIssued);
				r.setLicenseeName(e.getLicenseName());
				
				response.add(r);
				
			}
			
			
			return new ResponseEntity<>(response, HttpStatus.OK); 

		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Unable to get report data.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	@GetMapping(DashboardAPIs.DOWNLOAD_CUSTOMIZED_PERIOD_REPORT_DSC_ESIGN_ISSUED)
	public ResponseEntity<?> downloadCustomizedPeriodDSCEsignDataReport(@RequestParam("id") String month, @RequestParam("pid") String year, @RequestParam("qid") String month1, @RequestParam("rid") String year1) {
	    try {
	       
	    	String months = EncryptionUtil.decrypt(month);
			String years = EncryptionUtil.decrypt(year);
			String months1 = EncryptionUtil.decrypt(month1);
			String years1 = EncryptionUtil.decrypt(year1);
			
			String startDate =  getStartDate(months, years);
			String endDate =  getEndDate(months1, years1);

	        List<ClientLicenseDetailsDTO> ccalist = clientServ.getAllActiveLicenseDetails();
	        
	        
	        
	        int startMonth = getMonthNumber(months);
	        int startYear = Integer.parseInt(years);
	        int endMonth = getMonthNumber(months1);
	        int endYear = Integer.parseInt(years1);
	        
	        
	        List<DSCeSignIssued> dsceSignIssued1 = dscServ.getAllDSCeSignIssued();
	        
	        List<DSCeSignIssued> dsceSignIssued =  dsceSignIssued1.stream()
	                .filter(d -> {
	                    int recordYear = Integer.parseInt(d.getYear());
	                    int recordMonth = getMonthNumber(d.getMonth());
	                    
	                   
	                    return (recordYear > startYear || 
	                            (recordYear == startYear && recordMonth >= startMonth)) && 
	                           (recordYear < endYear || 
	                            (recordYear == endYear && recordMonth <= endMonth));
	                })
	                .collect(Collectors.toList());
	        

	        List<TotalDSCeSignIssuedResponse> response = new ArrayList<>();

	        for (ClientLicenseDetailsDTO e : ccalist) {
	            long totalDSCIssued = dsceSignIssued.stream()
	                .filter(ca -> ca.getCaUsername().equals(e.getUserName()))
	                .mapToLong(ca -> Long.parseLong(ca.getDscIssued()))
	                .sum();

	            long totaleSignIssued = dsceSignIssued.stream()
	                .filter(ca -> ca.getCaUsername().equals(e.getUserName()))
	                .mapToLong(ca -> Long.parseLong(ca.geteSignIssued()))
	                .sum();

	            TotalDSCeSignIssuedResponse r = new TotalDSCeSignIssuedResponse();
	            r.setDscIssued(totalDSCIssued);
	            r.seteSignIssued(totaleSignIssued);
	            r.setLicenseeName(e.getLicenseName());
	            response.add(r);
	        }

	        // Generate HTML content
	        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
	        StringBuilder contentBuilder = new StringBuilder();
	        contentBuilder.append("<html>")
	            .append("<head>")
	            .append("<style>")
	            .append("@page { margin: 20pt; }")
	            .append("body { font-size: 16px; font-family: \"Times New Roman\", Times, serif; }")
	            .append("table { border-collapse: collapse; width: 100%;} td, th { border: 1px solid black; padding: 5px; text-align: left;}")
	            .append("</style>")
	            .append("</head>")
	            .append("<body>")
	            .append("<div style=\"text-align: center; font-size: 24px;\"><b>Controller of Certifying Authorities (CCA)</b></div>")
	            .append("<div style=\"text-align: center; font-size: 18px;\"><u>Customized Report of eSign & DSC</u></div>")
	            .append("<p style=\"text-align: right;\"><b>As on ").append(currentDate).append("</b></p>")
	            .append("<p style=\"margin-top: 20px;text-align: center;text-justify: inter-word; font-weight: 600;\">Report of eSign and Digital Signature Certificate(s) (DSCs) from "+startDate+" to "+endDate+" issued by each CA/ESP under the Controller of Certifying Authorities (CCA)</p>")
	            .append("<table>")
	            .append("<thead><tr><th>Sl. No.</th><th>CA/ESP Name</th><th>Total eSign Issued</th><th>Total DSC Issued</th></tr></thead>")
	            .append("<tbody>");

	        int serialNumber = 1;
	        
	        Long totalDSCIssued = 0L;
	        Long totalEsingIssued = 0L;
	        	        
	        
	        for (TotalDSCeSignIssuedResponse record : response) {
	            contentBuilder.append("<tr>")
	                .append("<td>").append(serialNumber++).append(".</td>")
	                .append("<td>").append(record.getLicenseeName()).append("</td>")
	                .append("<td>").append(record.geteSignIssued()).append("</td>")
	                .append("<td>").append(record.getDscIssued()).append("</td>")
	                .append("</tr>");
	            
	            totalDSCIssued += record.getDscIssued();
	            totalEsingIssued += record.geteSignIssued();
	            
	        }
	        
	        contentBuilder.append("<tr><td colspan=2><b>Total:</b> </td><td>"+totalEsingIssued+"</td><td>"+totalDSCIssued+"</td></tr>");
	        contentBuilder.append("</tbody></table></body></html>");

	        // Convert HTML to PDF
	        String htmlContent = contentBuilder.toString();
	        byte[] pdfBytes;
	        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
	            ConverterProperties converterProperties = new ConverterProperties();
	            HtmlConverter.convertToPdf(htmlContent, outputStream, converterProperties);
	            pdfBytes = outputStream.toByteArray();
	        }

	        // Return PDF response
	        return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=CustomizedPeriodReport.pdf")
	            .contentType(MediaType.APPLICATION_PDF)
	            .body(pdfBytes);

	    } catch (Exception e) {
	        
	        return new ResponseEntity<>("Unable to get report data.", HttpStatus.BAD_REQUEST);
	    }
	}


	
	
	
	
	
	@GetMapping(DashboardAPIs.GET_CUMULATIVE_DSC_ESIGN_ISSUED)
	public ResponseEntity<?> getCumulativeDSCEsignData(@RequestParam("id") String month, @RequestParam("pid") String year) {
	    try {
	       
	        String months = EncryptionUtil.decrypt(month);
	        String years = EncryptionUtil.decrypt(year);

	       
	        
	        LocalDate currentDate = LocalDate.now();
	        int endMonth = currentDate.getMonthValue();  
	        int endYear = currentDate.getYear();        

	        
			
	        
	        int startMonth = getMonthNumber(months);
	        int startYear = Integer.parseInt(years);

	        
	        List<StateDTO> stateList = clientServ.getAllStateByCountryName("India");
	       
	        List<DSCeSignIssued> dsceSignIssued1 = dscServ.getAllDSCeSignIssued();

	        
	        List<DSCeSignIssued> dsceSignIssued = dsceSignIssued1.stream()
	                .filter(d -> {
	                    int recordYear = Integer.parseInt(d.getYear());
	                    int recordMonth = getMonthNumber(d.getMonth());

	                    return (recordYear > startYear || 
	                            (recordYear == startYear && recordMonth >= startMonth)) && 
	                           (recordYear < endYear || 
	                            (recordYear == endYear && recordMonth <= endMonth));
	                })
	                .collect(Collectors.toList());

	        
	        List<TotalDSCeSignIssuedResponse> response = new ArrayList<>();

	        for (StateDTO e : stateList) {
	           
	            long totalDSCIssued = dsceSignIssued.stream()
	                    .filter(ca -> ca.getStateId().equals(EncryptionUtil.encrypt(e.getStateId().toString())))
	                    .mapToLong(ca -> Long.parseLong(ca.getDscIssued()))
	                    .sum();

	            long totaleSignIssued = dsceSignIssued.stream()
	                    .filter(ca -> ca.getStateId().equals(EncryptionUtil.encrypt(e.getStateId().toString())))
	                    .mapToLong(ca -> Long.parseLong(ca.geteSignIssued()))
	                    .sum();

	            
	            TotalDSCeSignIssuedResponse r = new TotalDSCeSignIssuedResponse();
	            r.setDscIssued(totalDSCIssued);
	            r.seteSignIssued(totaleSignIssued);
	            r.setLicenseeName(e.getStateName());

	            response.add(r);
	        }

	        
	        return new ResponseEntity<>(response, HttpStatus.OK);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>("Unable to get report data.", HttpStatus.BAD_REQUEST);
	    }
	}
	
	
	
	
	
	
	@GetMapping(DashboardAPIs.DOWNLOAD_CUMULATIVE_REPORT_DSC_ESIGN_ISSUED)
	public ResponseEntity<?> downloadCumulativeDSCEsignDataReport(@RequestParam("id") String month, @RequestParam("pid") String year) {
	    try {
	       
	    	 	String months = EncryptionUtil.decrypt(month);
		        String years = EncryptionUtil.decrypt(year);

		        
		        LocalDate cDate = LocalDate.now();
		        int endMonth = cDate.getMonthValue();  
		        int endYear = cDate.getYear();        

		        
		        int startMonth = getMonthNumber(months);
		        int startYear = Integer.parseInt(years);

		        String startDate =  getStartDate(months, years);
		        
		        
		        List<StateDTO> stateList = clientServ.getAllStateByCountryName("India");
		       
		        List<DSCeSignIssued> dsceSignIssued1 = dscServ.getAllDSCeSignIssued();

		        
		        List<DSCeSignIssued> dsceSignIssued = dsceSignIssued1.stream()
		                .filter(d -> {
		                    int recordYear = Integer.parseInt(d.getYear());
		                    int recordMonth = getMonthNumber(d.getMonth());

		                    return (recordYear > startYear || 
		                            (recordYear == startYear && recordMonth >= startMonth)) && 
		                           (recordYear < endYear || 
		                            (recordYear == endYear && recordMonth <= endMonth));
		                })
		                .collect(Collectors.toList());

		        
		        List<TotalDSCeSignIssuedResponse> response = new ArrayList<>();

		        for (StateDTO e : stateList) {
		           
		            long totalDSCIssued = dsceSignIssued.stream()
		                    .filter(ca -> ca.getStateId().equals(EncryptionUtil.encrypt(e.getStateId().toString())))
		                    .mapToLong(ca -> Long.parseLong(ca.getDscIssued()))
		                    .sum();

		            long totaleSignIssued = dsceSignIssued.stream()
		                    .filter(ca -> ca.getStateId().equals(EncryptionUtil.encrypt(e.getStateId().toString())))
		                    .mapToLong(ca -> Long.parseLong(ca.geteSignIssued()))
		                    .sum();

		            
		            TotalDSCeSignIssuedResponse r = new TotalDSCeSignIssuedResponse();
		            r.setDscIssued(totalDSCIssued);
		            r.seteSignIssued(totaleSignIssued);
		            r.setLicenseeName(e.getStateName());

		            response.add(r);
		        }
	    	
	    	
	    	

	        // Generate HTML content
	        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
	        String currentDates = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	        
	        StringBuilder contentBuilder = new StringBuilder();
	        contentBuilder.append("<html>")
	            .append("<head>")
	            .append("<style>")
	            .append("@page { margin: 20pt; }")
	            .append("body { font-size: 16px; font-family: \"Times New Roman\", Times, serif; }")
	            .append("table { border-collapse: collapse; width: 100%;} td, th { border: 1px solid black; padding: 5px; text-align: left;}")
	            .append("</style>")
	            .append("</head>")
	            .append("<body>")
	            .append("<div style=\"text-align: center; font-size: 24px;\"><b>Controller of Certifying Authorities (CCA)</b></div>")
	            .append("<div style=\"text-align: center; font-size: 18px;\"><u>Cumulative Report of eSign & DSC</u></div>")
	            .append("<p style=\"text-align: right;\"><b>As on ").append(currentDate).append("</b></p>")
	            .append("<p style=\"margin-top: 20px;text-align: center;text-justify: inter-word; font-weight: 600;\">Report of eSign and Digital Signature Certificate(s) (DSCs) from "+startDate+" to "+currentDates+" issued by each CA/ESP under the Controller of Certifying Authorities (CCA)</p>")
	            .append("<table>")
	            .append("<thead><tr><th>Sl. No.</th><th>State/Union Territory</th><th>Total eSign Issued</th><th>Total DSC Issued</th></tr></thead>")
	            .append("<tbody>");

	        int serialNumber = 1;
	        
	        Long totalDSCIssued = 0L;
	        Long totalEsingIssued = 0L;

	        for (TotalDSCeSignIssuedResponse record : response) {
	            contentBuilder.append("<tr>")
	                .append("<td>").append(serialNumber++).append(".</td>")
	                .append("<td>").append(record.getLicenseeName()).append("</td>")
	                .append("<td>").append(record.geteSignIssued()).append("</td>")
	                .append("<td>").append(record.getDscIssued()).append("</td>")
	                .append("</tr>");
	            
	            totalDSCIssued += record.getDscIssued();
	            totalEsingIssued += record.geteSignIssued();
	            
	        }
	        
	        contentBuilder.append("<tr><td colspan=2><b>Total:</b> </td><td>"+totalEsingIssued+"</td><td>"+totalDSCIssued+"</td></tr>");
	        contentBuilder.append("</tbody></table></body></html>");

	        // Convert HTML to PDF
	        String htmlContent = contentBuilder.toString();
	        byte[] pdfBytes;
	        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
	            ConverterProperties converterProperties = new ConverterProperties();
	            HtmlConverter.convertToPdf(htmlContent, outputStream, converterProperties);
	            pdfBytes = outputStream.toByteArray();
	        }

	        // Return PDF response
	        return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=CumulativeReport.pdf")
	            .contentType(MediaType.APPLICATION_PDF)
	            .body(pdfBytes);

	    } catch (Exception e) {
	        
	        return new ResponseEntity<>("Unable to get report data.", HttpStatus.BAD_REQUEST);
	    }
	}

	
	
	
	
	
	@GetMapping(DashboardAPIs.GET_CUSTOMIZED_CUMULATIVE_DSC_ESIGN_ISSUED)
	public ResponseEntity<?> getCustomizedCumulativeDSCEsignData(@RequestParam("id") String month, @RequestParam("pid") String year, @RequestParam("qid") String month1, @RequestParam("rid") String year1) {
	    try {
	       
	        String months = EncryptionUtil.decrypt(month);
	        String years = EncryptionUtil.decrypt(year);
	        String months1 = EncryptionUtil.decrypt(month1);
			String years1 = EncryptionUtil.decrypt(year1);
	       
	        
			int startMonth = getMonthNumber(months);
	        int startYear = Integer.parseInt(years);
	        int endMonth = getMonthNumber(months1);
	        int endYear = Integer.parseInt(years1);

	        
	        List<StateDTO> stateList = clientServ.getAllStateByCountryName("India");
	       
	        List<DSCeSignIssued> dsceSignIssued1 = dscServ.getAllDSCeSignIssued();

	        
	        List<DSCeSignIssued> dsceSignIssued = dsceSignIssued1.stream()
	                .filter(d -> {
	                    int recordYear = Integer.parseInt(d.getYear());
	                    int recordMonth = getMonthNumber(d.getMonth());

	                    return (recordYear > startYear || 
	                            (recordYear == startYear && recordMonth >= startMonth)) && 
	                           (recordYear < endYear || 
	                            (recordYear == endYear && recordMonth <= endMonth));
	                })
	                .collect(Collectors.toList());

	        
	        List<TotalDSCeSignIssuedResponse> response = new ArrayList<>();

	        for (StateDTO e : stateList) {
	           
	            long totalDSCIssued = dsceSignIssued.stream()
	                    .filter(ca -> ca.getStateId().equals(EncryptionUtil.encrypt(e.getStateId().toString())))
	                    .mapToLong(ca -> Long.parseLong(ca.getDscIssued()))
	                    .sum();

	            long totaleSignIssued = dsceSignIssued.stream()
	                    .filter(ca -> ca.getStateId().equals(EncryptionUtil.encrypt(e.getStateId().toString())))
	                    .mapToLong(ca -> Long.parseLong(ca.geteSignIssued()))
	                    .sum();

	            
	            TotalDSCeSignIssuedResponse r = new TotalDSCeSignIssuedResponse();
	            r.setDscIssued(totalDSCIssued);
	            r.seteSignIssued(totaleSignIssued);
	            r.setLicenseeName(e.getStateName());

	            response.add(r);
	        }

	        
	        return new ResponseEntity<>(response, HttpStatus.OK);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>("Unable to get report data.", HttpStatus.BAD_REQUEST);
	    }
	}
	
	
	
	
	
	
	@GetMapping(DashboardAPIs.DOWNLOAD_CUSTOMIZED_CUMULATIVE_REPORT_DSC_ESIGN_ISSUED)
	public ResponseEntity<?> downloadCustomizedCumulativeDSCEsignDataReport(@RequestParam("id") String month, @RequestParam("pid") String year, @RequestParam("qid") String month1, @RequestParam("rid") String year1) {
	    try {
	       
	    	String months = EncryptionUtil.decrypt(month);
	        String years = EncryptionUtil.decrypt(year);
	        String months1 = EncryptionUtil.decrypt(month1);
			String years1 = EncryptionUtil.decrypt(year1);
	       
	        
			int startMonth = getMonthNumber(months);
	        int startYear = Integer.parseInt(years);
	        int endMonth = getMonthNumber(months1);
	        int endYear = Integer.parseInt(years1);

	        String startDate =  getStartDate(months, years);
			String endDate =  getEndDate(months1, years1);
	        
	        List<StateDTO> stateList = clientServ.getAllStateByCountryName("India");
	       
	        List<DSCeSignIssued> dsceSignIssued1 = dscServ.getAllDSCeSignIssued();

	        
	        List<DSCeSignIssued> dsceSignIssued = dsceSignIssued1.stream()
	                .filter(d -> {
	                    int recordYear = Integer.parseInt(d.getYear());
	                    int recordMonth = getMonthNumber(d.getMonth());

	                    return (recordYear > startYear || 
	                            (recordYear == startYear && recordMonth >= startMonth)) && 
	                           (recordYear < endYear || 
	                            (recordYear == endYear && recordMonth <= endMonth));
	                })
	                .collect(Collectors.toList());

	        
	        List<TotalDSCeSignIssuedResponse> response = new ArrayList<>();

	        for (StateDTO e : stateList) {
	           
	            long totalDSCIssued = dsceSignIssued.stream()
	                    .filter(ca -> ca.getStateId().equals(EncryptionUtil.encrypt(e.getStateId().toString())))
	                    .mapToLong(ca -> Long.parseLong(ca.getDscIssued()))
	                    .sum();

	            long totaleSignIssued = dsceSignIssued.stream()
	                    .filter(ca -> ca.getStateId().equals(EncryptionUtil.encrypt(e.getStateId().toString())))
	                    .mapToLong(ca -> Long.parseLong(ca.geteSignIssued()))
	                    .sum();

	            
	            TotalDSCeSignIssuedResponse r = new TotalDSCeSignIssuedResponse();
	            r.setDscIssued(totalDSCIssued);
	            r.seteSignIssued(totaleSignIssued);
	            r.setLicenseeName(e.getStateName());

	            response.add(r);
	        }
	    	
	    	
	    	

	        // Generate HTML content
	        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
	        
	        
	        StringBuilder contentBuilder = new StringBuilder();
	        contentBuilder.append("<html>")
	            .append("<head>")
	            .append("<style>")
	            .append("@page { margin: 20pt; }")
	            .append("body { font-size: 16px; font-family: \"Times New Roman\", Times, serif; }")
	            .append("table { border-collapse: collapse; width: 100%;} td, th { border: 1px solid black; padding: 5px; text-align: left;}")
	            .append("</style>")
	            .append("</head>")
	            .append("<body>")
	            .append("<div style=\"text-align: center; font-size: 24px;\"><b>Controller of Certifying Authorities (CCA)</b></div>")
	            .append("<div style=\"text-align: center; font-size: 18px;\"><u>Customized Cumulative Report of eSign & DSC</u></div>")
	            .append("<p style=\"text-align: right;\"><b>As on ").append(currentDate).append("</b></p>")
	            .append("<p style=\"margin-top: 20px;text-align: center;text-justify: inter-word; font-weight: 600;\">Report of eSign and Digital Signature Certificate(s) (DSCs) from "+startDate+" to "+endDate+" issued by each CA/ESP under the Controller of Certifying Authorities (CCA)</p>")
	            .append("<table>")
	            .append("<thead><tr><th>Sl. No.</th><th>State/Union Territory</th><th>Total eSign Issued</th><th>Total DSC Issued</th></tr></thead>")
	            .append("<tbody>");

	        int serialNumber = 1;
	        
	        Long totalDSCIssued = 0L;
	        Long totalEsingIssued = 0L;

	        
	        for (TotalDSCeSignIssuedResponse record : response) {
	            contentBuilder.append("<tr>")
	                .append("<td>").append(serialNumber++).append(".</td>")
	                .append("<td>").append(record.getLicenseeName()).append("</td>")
	                .append("<td>").append(record.geteSignIssued()).append("</td>")
	                .append("<td>").append(record.getDscIssued()).append("</td>")
	                .append("</tr>");
	            
	            totalDSCIssued += record.getDscIssued();
	            totalEsingIssued += record.geteSignIssued();
	        }
	        
	        contentBuilder.append("<tr><td colspan=2><b>Total:</b> </td><td>"+totalEsingIssued+"</td><td>"+totalDSCIssued+"</td></tr>");
	        contentBuilder.append("</tbody></table></body></html>");
	        

	        // Convert HTML to PDF
	        String htmlContent = contentBuilder.toString();
	        byte[] pdfBytes;
	        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
	            ConverterProperties converterProperties = new ConverterProperties();
	            HtmlConverter.convertToPdf(htmlContent, outputStream, converterProperties);
	            pdfBytes = outputStream.toByteArray();
	        }

	        // Return PDF response
	        return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=CustomizedCumulativeReport.pdf")
	            .contentType(MediaType.APPLICATION_PDF)
	            .body(pdfBytes);

	    } catch (Exception e) {
	        
	        return new ResponseEntity<>("Unable to get report data.", HttpStatus.BAD_REQUEST);
	    }
	}
	
	
	
	
	@GetMapping(DashboardAPIs.GET_CA_ESP_LICENSING_REPORT)
	public ResponseEntity<?> getLicensingReport(){
		
		try {
		List<ClientLicenseDetailsDTO> ccalist = clientServ.getAllLicenseDetails();
		
		ccalist = ccalist.stream()
			    .sorted(Comparator.comparing(ClientLicenseDetailsDTO::getUserName, Comparator.reverseOrder())
			        .thenComparing(ClientLicenseDetailsDTO::getIssueDate, Comparator.reverseOrder()))
			    .collect(Collectors.toList());

		
		List<ESPDTO> esplist = clientServ.getAllApprovedESP();
		
		List<LicensingReportDTO> response = new ArrayList<>();
		
		
		for(ClientLicenseDetailsDTO e: ccalist) {
			
			ESPDTO d = esplist.stream()
					   .filter(esp->esp.getUserName().equals(e.getUserName()))
					   .findFirst()
					   .orElse(null);
			
		
			LicensingReportDTO report = new LicensingReportDTO();
			report.setLicenseeName(e.getLicenseName());
			report.setLicenseExpiryDate(e.getExpiryDate());
			report.setLicenseIssueDate(e.getIssueDate());
			report.setLicenseeType("CA");
			if(d != null && e.getStatus().equals("Active")) {
				report.setLicenseeType("ESP");
				report.setEmpanelmentDate(d.getEmpanelmentDate());
			}
			
			response.add(report);
			
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("Error in fetching data, try after some time.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	
	@GetMapping(DashboardAPIs.DOWNLOAD_CA_ESP_LICENSING_REPORT)
	public ResponseEntity<?> downloadLicensingReport() {
	    try {
	       
	    	List<ClientLicenseDetailsDTO> ccalist = clientServ.getAllLicenseDetails();
			
			ccalist = ccalist.stream()
				    .sorted(Comparator.comparing(ClientLicenseDetailsDTO::getUserName, Comparator.reverseOrder())
				        .thenComparing(ClientLicenseDetailsDTO::getIssueDate, Comparator.reverseOrder()))
				    .collect(Collectors.toList());

			
			List<ESPDTO> esplist = clientServ.getAllApprovedESP();
			
			List<LicensingReportDTO> response = new ArrayList<>();
			
			
			for(ClientLicenseDetailsDTO e: ccalist) {
				
				ESPDTO d = esplist.stream()
						   .filter(esp->esp.getUserName().equals(e.getUserName()))
						   .findFirst()
						   .orElse(null);
				
			
				LicensingReportDTO report = new LicensingReportDTO();
				report.setLicenseeName(e.getLicenseName());
				report.setLicenseExpiryDate(e.getExpiryDate());
				report.setLicenseIssueDate(e.getIssueDate());
				report.setLicenseeType("CA");
				if(d != null && e.getStatus().equals("Active")) {
					report.setLicenseeType("ESP");
					report.setEmpanelmentDate(d.getEmpanelmentDate());
				}
				
				response.add(report);
			}
	    	
	    	
	    	

	        // Generate HTML content
	        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
	        
	        StringBuilder contentBuilder = new StringBuilder();
	        contentBuilder.append("<html>")
	            .append("<head>")
	            .append("<style>")
	            .append("@page { margin: 20pt; }")
	            .append("body { font-size: 16px; font-family: \"Times New Roman\", Times, serif; }")
	            .append("table { border-collapse: collapse; width: 100%;} td, th { border: 1px solid black; padding: 5px; text-align: left;}")
	            .append("</style>")
	            .append("</head>")
	            .append("<body>")
	            .append("<div style=\"text-align: center; font-size: 24px;\"><b>Controller of Certifying Authorities (CCA)</b></div>")
	            .append("<div style=\"text-align: center; font-size: 18px;\"><u>Licensing Report of CAs and ESPs from beginning</u></div>")
	            .append("<p style=\"text-align: right;\"><b>As on ").append(currentDate).append("</b></p>")
	            .append("<p style=\"margin-top: 20px;text-align: center;text-justify: inter-word; font-weight: 600;\">Report on CAs and ESPs that obtained licenses from the beginning under the Controller of Certifying Authorities (CCA)</p>")
	            .append("<table>")
	            .append("<thead><tr><th>Sl. No.</th><th>CA/ESP Name</th><th>Type (CA/ESP)</th><th>License Issue Date</th><th>License Valid Up To</th><th>ESP Empanelment Date</th></tr></thead>")
	            .append("<tbody>");

	        int serialNumber = 1;
	        
	        for (LicensingReportDTO r : response) {
	            contentBuilder.append("<tr>")
	                .append("<td>").append(serialNumber++).append(".</td>")
	                .append("<td>").append(r.getLicenseeName()).append("</td>")
	                .append("<td>").append(r.getLicenseeType()).append("</td>")
	                .append("<td>").append(convertToLocalDate(r.getLicenseIssueDate()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("</td>")
	                .append("<td>").append(convertToLocalDate(r.getLicenseExpiryDate()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("</td>")
	                .append("<td>").append(r.getEmpanelmentDate() != null ? 
	                                        convertToLocalDate(r.getEmpanelmentDate()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "-")
	                                .append("</td>")
	                .append("</tr>");
	        }



	        
	        contentBuilder.append("</tbody></table></body></html>");

	        // Convert HTML to PDF
	        String htmlContent = contentBuilder.toString();
	        byte[] pdfBytes;
	        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
	            ConverterProperties converterProperties = new ConverterProperties();
	            HtmlConverter.convertToPdf(htmlContent, outputStream, converterProperties);
	            pdfBytes = outputStream.toByteArray();
	        }

	        // Return PDF response
	        return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=LicensingReport.pdf")
	            .contentType(MediaType.APPLICATION_PDF)
	            .body(pdfBytes);

	    } catch (Exception e) {
	        
	        return new ResponseEntity<>("Unable to get report data.", HttpStatus.BAD_REQUEST);
	    }
	}
	
	
	
	
	@GetMapping(DashboardAPIs.GET_CUSTOMIZED_CA_ESP_LICENSING_REPORT)
	public ResponseEntity<?> getCustomizedLicensingReport(@RequestParam("id") String fDate, @RequestParam("pid") String tDate){
		
		try {
		    if (isNullOrEmpty(fDate) || isNullOrEmpty(tDate)) {
		    	return ResponseEntity.ok(Collections.emptyList());
		    }

		    String fromDate = EncryptionUtil.decrypt(fDate);
		    String toDate = EncryptionUtil.decrypt(tDate);

		    if (isNullOrEmpty(fromDate) || isNullOrEmpty(toDate)) {
		    	return ResponseEntity.ok(Collections.emptyList());
		    }

		    // Parse dates
		    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		    LocalDate startDate;
		    LocalDate endDate;

		    try {
		        startDate = LocalDate.parse(fromDate, formatter);
		        endDate = LocalDate.parse(toDate, formatter);
		    } catch (DateTimeParseException e) {
		    	return ResponseEntity.ok(Collections.emptyList());
		    }

		    if (!endDate.isAfter(startDate)) {
		    	return ResponseEntity.ok(Collections.emptyList());
		    }

		    // Fetch data
		    List<ClientLicenseDetailsDTO> activeLicenses = clientServ.getAllActiveLicenseDetails();
		    List<ClientLicenseDetailsDTO> inactiveLicenses = clientServ.getAllInactiveLicenseDetails();
		    List<ESPDTO> approvedESPs = clientServ.getAllApprovedESP();

		    // Filter and map data
		    List<CustomizedLicensingReportDTO> reports = activeLicenses.stream()
		        .filter(e -> !e.getIssueDate().before(java.sql.Date.valueOf(startDate)) &&
		                     !e.getIssueDate().after(java.sql.Date.valueOf(endDate)))
		        .map(e -> {
		            ESPDTO esp = approvedESPs.stream()
		                .filter(espItem -> espItem.getUserName().equals(e.getUserName()))
		                .findFirst()
		                .orElse(null);

		            boolean isRenewed = inactiveLicenses.stream()
		                .anyMatch(ca -> ca.getUserName().equals(e.getUserName()));

		            CustomizedLicensingReportDTO report = new CustomizedLicensingReportDTO();
		            report.setEmpanelmentDate(esp != null ? esp.getEmpanelmentDate() : null);
		            report.setLicenseeName(e.getLicenseName());
		            report.setLicenseExpiryDate(e.getExpiryDate());
		            report.setLicenseIssueDate(e.getIssueDate());
		            report.setLicenseeType(esp != null ? "ESP" : "CA");
		            report.setLicenseType(isRenewed ? "Renewed" : "New");
		            return report;
		        })
		        .collect(Collectors.toList());

		    return ResponseEntity.ok(reports);
		} catch (Exception e) {
		   
		    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                         .body("Error in fetching data, please try again later.");
		}

	}
	
	
	@GetMapping(DashboardAPIs.DOWNLOAD_CUSTOMIZED_CA_ESP_LICENSING_REPORT)
	public ResponseEntity<?> downloadCustomizedLicensingReport(@RequestParam("id") String fDate, @RequestParam("pid") String tDate){
		
		try {
		    if (isNullOrEmpty(fDate) || isNullOrEmpty(tDate)) {
		    	return ResponseEntity.ok(Collections.emptyList());
		    }

		    String fromDate = EncryptionUtil.decrypt(fDate);
		    String toDate = EncryptionUtil.decrypt(tDate);

		    if (isNullOrEmpty(fromDate) || isNullOrEmpty(toDate)) {
		    	return ResponseEntity.ok(Collections.emptyList());
		    }

		    // Parse dates
		    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		    LocalDate startDate;
		    LocalDate endDate;

		    try {
		        startDate = LocalDate.parse(fromDate, formatter);
		        endDate = LocalDate.parse(toDate, formatter);
		    } catch (DateTimeParseException e) {
		    	return ResponseEntity.ok(Collections.emptyList());
		    }

		    if (!endDate.isAfter(startDate)) {
		    	return ResponseEntity.ok(Collections.emptyList());
		    }

		    // Fetch data
		    List<ClientLicenseDetailsDTO> activeLicenses = clientServ.getAllActiveLicenseDetails();
		    List<ClientLicenseDetailsDTO> inactiveLicenses = clientServ.getAllInactiveLicenseDetails();
		    List<ESPDTO> approvedESPs = clientServ.getAllApprovedESP();

		    // Filter and map data
		    List<CustomizedLicensingReportDTO> reports = activeLicenses.stream()
		        .filter(e -> !e.getIssueDate().before(java.sql.Date.valueOf(startDate)) &&
		                     !e.getIssueDate().after(java.sql.Date.valueOf(endDate)))
		        .map(e -> {
		            ESPDTO esp = approvedESPs.stream()
		                .filter(espItem -> espItem.getUserName().equals(e.getUserName()))
		                .findFirst()
		                .orElse(null);

		            boolean isRenewed = inactiveLicenses.stream()
		                .anyMatch(ca -> ca.getUserName().equals(e.getUserName()));

		            CustomizedLicensingReportDTO report = new CustomizedLicensingReportDTO();
		            report.setEmpanelmentDate(esp != null ? esp.getEmpanelmentDate() : null);
		            report.setLicenseeName(e.getLicenseName());
		            report.setLicenseExpiryDate(e.getExpiryDate());
		            report.setLicenseIssueDate(e.getIssueDate());
		            report.setLicenseeType(esp != null ? "ESP" : "CA");
		            report.setLicenseType(isRenewed ? "Renewed" : "New");
		            return report;
		        })
		        .collect(Collectors.toList());

		 // Generate HTML content
	        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
	        
	        StringBuilder contentBuilder = new StringBuilder();
	        contentBuilder.append("<html>")
	            .append("<head>")
	            .append("<style>")
	            .append("@page { margin: 20pt; }")
	            .append("body { font-size: 16px; font-family: \"Times New Roman\", Times, serif; }")
	            .append("table { border-collapse: collapse; width: 100%;} td, th { border: 1px solid black; padding: 5px; text-align: left;}")
	            .append("</style>")
	            .append("</head>")
	            .append("<body>")
	            .append("<div style=\"text-align: center; font-size: 24px;\"><b>Controller of Certifying Authorities (CCA)</b></div>")
	            .append("<div style=\"text-align: center; font-size: 18px;\"><u>Customized Licensing Report of CAs and ESPs</u></div>")
	            .append("<p style=\"text-align: right;\"><b>As on ").append(currentDate).append("</b></p>")
	            .append("<p style=\"margin-top: 20px;text-align: center;text-justify: inter-word; font-weight: 600;\">Report on CAs and ESPs that obtained licenses from "+startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+" to "+endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+" under the Controller of Certifying Authorities (CCA)</p>")
	            .append("<table>")
	            .append("<thead><tr><th>Sl. No.</th><th>CA/ESP Name</th><th>Type (CA/ESP)</th><th>License Type (New/Renewed)</th><th>License Issued/Renewed Date</th><th>License Valid Up To</th><th>ESP Empanelment Date</th></tr></thead>")
	            .append("<tbody>");

	        int serialNumber = 1;
	        
	        for (CustomizedLicensingReportDTO r : reports) {
	            contentBuilder.append("<tr>")
	                .append("<td>").append(serialNumber++).append(".</td>")
	                .append("<td>").append(r.getLicenseeName()).append("</td>")
	                .append("<td>").append(r.getLicenseeType()).append("</td>")
	                .append("<td>").append(r.getLicenseType()).append("</td>")
	                .append("<td>").append(convertToLocalDate(r.getLicenseIssueDate()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("</td>")
	                .append("<td>").append(convertToLocalDate(r.getLicenseExpiryDate()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("</td>")
	                .append("<td>").append(r.getEmpanelmentDate() != null ? 
	                                        convertToLocalDate(r.getEmpanelmentDate()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "-")
	                                .append("</td>")
	                .append("</tr>");
	        }



	        
	        contentBuilder.append("</tbody></table></body></html>");

	        // Convert HTML to PDF
	        String htmlContent = contentBuilder.toString();
	        byte[] pdfBytes;
	        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
	            ConverterProperties converterProperties = new ConverterProperties();
	            HtmlConverter.convertToPdf(htmlContent, outputStream, converterProperties);
	            pdfBytes = outputStream.toByteArray();
	        }

	        // Return PDF response
	        return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=CustomizedLicensingReport.pdf")
	            .contentType(MediaType.APPLICATION_PDF)
	            .body(pdfBytes);

	    } catch (Exception e) {
	        
	        return new ResponseEntity<>("Unable to get report data.", HttpStatus.BAD_REQUEST);
	    }


	}

	
	
	@GetMapping(DashboardAPIs.GET_ANNUAL_AUDIT_INITIATED_ON_TIME)
	public ResponseEntity<?> getAnnualAuditInitiatedOnTime(){
		
		try {
			
		List<AnnualAuditScheduleDTO> auditlist = clientServ.getAllAnnualAuditSchedule()
												.stream()
												.sorted(Comparator.comparing(AnnualAuditScheduleDTO::getUserName)
											            .thenComparing(AnnualAuditScheduleDTO::getScheduleStartDate))
											        .collect(Collectors.toList());
		
		
		List<ClientLicenseDetailsDTO> ccalist = clientServ.getAllActiveLicenseDetails();
		
		
		
		Map<String, String> userNameToLicenseeName = ccalist.stream()
		        .collect(Collectors.toMap(ClientLicenseDetailsDTO::getUserName, ClientLicenseDetailsDTO::getLicenseName));

		    
		    List<AnnualAuditScheduleDTO> alist = auditlist.stream()
		    
		        .filter(audit -> audit.getActualStartDate() != null && audit.getActualStartDate().equals(audit.getScheduleStartDate()))
		        
		        .peek(audit -> {
		            String licenseeName = userNameToLicenseeName.get(audit.getUserName());
		            if (licenseeName != null) {
		                audit.setUserName(licenseeName); 
		            }
		        })
		        .collect(Collectors.toList());
		
		
		
		
		return new ResponseEntity<>(alist, HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error in fetching data, try after some time.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	@GetMapping(DashboardAPIs.DOWNLOAD_ANNUAL_AUDIT_INITIATED_ON_TIME)
	public ResponseEntity<?> downloadAnnualAuditInitiatedOnTime(){
		
		try {
			
		List<AnnualAuditScheduleDTO> auditlist = clientServ.getAllAnnualAuditSchedule()
												.stream()
												.sorted(Comparator.comparing(AnnualAuditScheduleDTO::getUserName)
											            .thenComparing(AnnualAuditScheduleDTO::getScheduleStartDate))
											        .collect(Collectors.toList());
		
		
		List<ClientLicenseDetailsDTO> ccalist = clientServ.getAllActiveLicenseDetails();
		
		
		
		Map<String, String> userNameToLicenseeName = ccalist.stream()
		        .collect(Collectors.toMap(ClientLicenseDetailsDTO::getUserName, ClientLicenseDetailsDTO::getLicenseName));

		    
		    List<AnnualAuditScheduleDTO> alist = auditlist.stream()
		    
		        .filter(audit ->  audit.getActualStartDate() != null && audit.getActualStartDate().equals(audit.getScheduleStartDate()))
		        
		        .peek(audit -> {
		            String licenseeName = userNameToLicenseeName.get(audit.getUserName());
		            if (licenseeName != null) {
		                audit.setUserName(licenseeName); 
		            }
		        })
		        .collect(Collectors.toList());
		
		
		
		
		 // Generate HTML content
	        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
	        
	        StringBuilder contentBuilder = new StringBuilder();
	        contentBuilder.append("<html>")
	            .append("<head>")
	            .append("<style>")
	            .append("@page { margin: 20pt; }")
	            .append("body { font-size: 16px; font-family: \"Times New Roman\", Times, serif; }")
	            .append("table { border-collapse: collapse; width: 100%;} td, th { border: 1px solid black; padding: 5px; text-align: left;}")
	            .append("</style>")
	            .append("</head>")
	            .append("<body>")
	            .append("<div style=\"text-align: center; font-size: 24px;\"><b>Controller of Certifying Authorities (CCA)</b></div>")
	            .append("<div style=\"text-align: center; font-size: 18px;\"><u>Report on Annual Audits Initiated on Time</u></div>")
	            .append("<p style=\"text-align: right;\"><b>As on ").append(currentDate).append("</b></p>")
	            .append("<p style=\"margin-top: 20px;text-align: center;text-justify: inter-word; font-weight: 600;\">Report on Annual Audits initiated on time for each CA under the Controller of Certifying Authorities (CCA)</p>")
	            .append("<table>")
	            .append("<thead><tr><th>Sl. No.</th><th>CA Name</th><th>Scheduled Audit Date</th><th>Audit Start Date</th></tr></thead>")
	            .append("<tbody>");

	        int serialNumber = 1;
	        
	        for (AnnualAuditScheduleDTO r : alist) {
	            contentBuilder.append("<tr>")
	                .append("<td>").append(serialNumber++).append(".</td>")
	                .append("<td>").append(r.getUserName()).append("</td>")
	                .append("<td>").append(convertToLocalDate(r.getScheduleStartDate()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("</td>")
	                .append("<td>").append(convertToLocalDate(r.getActualStartDate()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("</td>")
	                .append("</tr>");
	        }



	        
	        contentBuilder.append("</tbody></table></body></html>");

	        // Convert HTML to PDF
	        String htmlContent = contentBuilder.toString();
	        byte[] pdfBytes;
	        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
	            ConverterProperties converterProperties = new ConverterProperties();
	            HtmlConverter.convertToPdf(htmlContent, outputStream, converterProperties);
	            pdfBytes = outputStream.toByteArray();
	        }

	        // Return PDF response
	        return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=AnnualAuditInitiatedOnTime.pdf")
	            .contentType(MediaType.APPLICATION_PDF)
	            .body(pdfBytes);

	    } catch (Exception e) {
	        
	        return new ResponseEntity<>("Unable to get report data.", HttpStatus.BAD_REQUEST);
	    }

		
	}

	
	
	
	@GetMapping(DashboardAPIs.GET_ANNUAL_AUDIT_DELAYED)
	public ResponseEntity<?> getAnnualAuditDelayed(){
		
		try {
		
			List<AnnualAuditScheduleDTO> auditlist = clientServ.getAllAnnualAuditSchedule()
					.stream()
					.sorted(Comparator.comparing(AnnualAuditScheduleDTO::getUserName)
				            .thenComparing(AnnualAuditScheduleDTO::getScheduleStartDate))
				        .collect(Collectors.toList());


					List<ClientLicenseDetailsDTO> ccalist = clientServ.getAllActiveLicenseDetails();
					
					
					
					Map<String, String> userNameToLicenseeName = ccalist.stream()
					.collect(Collectors.toMap(ClientLicenseDetailsDTO::getUserName, ClientLicenseDetailsDTO::getLicenseName));
					
					
					List<AnnualAuditScheduleDTO> alist = auditlist.stream()
					
					.filter(audit -> audit.getActualStartDate() == null || audit.getScheduleStartDate().before(audit.getActualStartDate()))
					
					.peek(audit -> {
					String licenseeName = userNameToLicenseeName.get(audit.getUserName());
					if (licenseeName != null) {
					audit.setUserName(licenseeName); 
					}
					})
					.collect(Collectors.toList());
			
			
		return new ResponseEntity<>(alist, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("Error in fetching data, try after some time.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	@GetMapping(DashboardAPIs.DOWNLOAD_ANNUAL_AUDIT_DELAYED)
	public ResponseEntity<?> downloadAnnualAuditDelayed(){
		
		try {
		
			List<AnnualAuditScheduleDTO> auditlist = clientServ.getAllAnnualAuditSchedule()
					.stream()
					.sorted(Comparator.comparing(AnnualAuditScheduleDTO::getUserName)
				            .thenComparing(AnnualAuditScheduleDTO::getScheduleStartDate))
				        .collect(Collectors.toList());


					List<ClientLicenseDetailsDTO> ccalist = clientServ.getAllActiveLicenseDetails();
					
					
					
					Map<String, String> userNameToLicenseeName = ccalist.stream()
					.collect(Collectors.toMap(ClientLicenseDetailsDTO::getUserName, ClientLicenseDetailsDTO::getLicenseName));
					
					
					List<AnnualAuditScheduleDTO> alist = auditlist.stream()
					
					.filter(audit -> audit.getActualStartDate() == null || audit.getScheduleStartDate().before(audit.getActualStartDate()))
					
					.peek(audit -> {
					String licenseeName = userNameToLicenseeName.get(audit.getUserName());
					if (licenseeName != null) {
					audit.setUserName(licenseeName); 
					}
					})
					.collect(Collectors.toList());
			
			
					// Generate HTML content
			        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
			        
			        StringBuilder contentBuilder = new StringBuilder();
			        contentBuilder.append("<html>")
			            .append("<head>")
			            .append("<style>")
			            .append("@page { margin: 20pt; }")
			            .append("body { font-size: 16px; font-family: \"Times New Roman\", Times, serif; }")
			            .append("table { border-collapse: collapse; width: 100%;} td, th { border: 1px solid black; padding: 5px; text-align: left;}")
			            .append("</style>")
			            .append("</head>")
			            .append("<body>")
			            .append("<div style=\"text-align: center; font-size: 24px;\"><b>Controller of Certifying Authorities (CCA)</b></div>")
			            .append("<div style=\"text-align: center; font-size: 18px;\"><u>Report on Annual Audits Delayed</u></div>")
			            .append("<p style=\"text-align: right;\"><b>As on ").append(currentDate).append("</b></p>")
			            .append("<p style=\"margin-top: 20px;text-align: center;text-justify: inter-word; font-weight: 600;\">Report on Annual Audits delayed for each CA under the Controller of Certifying Authorities (CCA)</p>")
			            .append("<table>")
			            .append("<thead><tr><th>Sl. No.</th><th>CA Name</th><th>Scheduled Audit Date</th><th>Audit Started (Yes/No)</th><th>Audit Start Date</th></tr></thead>")
			            .append("<tbody>");

			        int serialNumber = 1;
			        
			        for (AnnualAuditScheduleDTO r : alist) {
			            contentBuilder.append("<tr>")
			                .append("<td>").append(serialNumber++).append(".</td>")
			                .append("<td>").append(r.getUserName()).append("</td>")
			                .append("<td>").append(convertToLocalDate(r.getScheduleStartDate()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("</td>")
			                .append("<td>").append(r.getActualStartDate() != null ? "Yes": "No").append("</td>")
			                .append("<td>").append((r.getActualStartDate() != null? convertToLocalDate(r.getActualStartDate()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")): "-")).append("</td>")
			                .append("</tr>");
			        }



			        
			        contentBuilder.append("</tbody></table></body></html>");

			        // Convert HTML to PDF
			        String htmlContent = contentBuilder.toString();
			        byte[] pdfBytes;
			        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			            ConverterProperties converterProperties = new ConverterProperties();
			            HtmlConverter.convertToPdf(htmlContent, outputStream, converterProperties);
			            pdfBytes = outputStream.toByteArray();
			        }

			        // Return PDF response
			        return ResponseEntity.ok()
			            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=AnnualAuditDelayed.pdf")
			            .contentType(MediaType.APPLICATION_PDF)
			            .body(pdfBytes);

			    } catch (Exception e) {
			        
			        return new ResponseEntity<>("Unable to get report data.", HttpStatus.BAD_REQUEST);
			    }

		
	}
	
	
	
	@GetMapping(DashboardAPIs.GET_ANNUAL_AUDIT_SUBMITTED_ON_TIME)
	public ResponseEntity<?> getAnnualAuditSubmittedOnTime(){
		
		
		
		try {
			
			List<AnnualAuditScheduleDTO> auditlist = clientServ.getAllAnnualAuditSchedule()
					.stream()
					.sorted(Comparator.comparing(AnnualAuditScheduleDTO::getUserName)
				            .thenComparing(AnnualAuditScheduleDTO::getScheduleStartDate))
				        .collect(Collectors.toList());


				List<ClientLicenseDetailsDTO> ccalist = clientServ.getAllActiveLicenseDetails();
				
				
				
				Map<String, String> userNameToLicenseeName = ccalist.stream()
				.collect(Collectors.toMap(ClientLicenseDetailsDTO::getUserName, ClientLicenseDetailsDTO::getLicenseName));
				
				
				List<AnnualAuditScheduleDTO> alist = auditlist.stream()
				
				.filter(audit -> audit.getScheduleSubmissionDate() != null && audit.getScheduleSubmissionDate().equals(audit.getDateOfSubmission()))
				
				.peek(audit -> {
				String licenseeName = userNameToLicenseeName.get(audit.getUserName());
				if (licenseeName != null) {
				audit.setUserName(licenseeName); 
				}
				})
				.collect(Collectors.toList());
			
			
		return new ResponseEntity<>(alist, HttpStatus.OK);
		}catch(Exception e) {
			
			return new ResponseEntity<>("Error in fetching data, try after some time.", HttpStatus.BAD_REQUEST);
		}
		
		
		
	}
	
	
	@GetMapping(DashboardAPIs.DOWNLOAD_ANNUAL_AUDIT_SUBMITTED_ON_TIME)
	public ResponseEntity<?> downloadAnnualAuditSubmittedOnTime(){
		
		
		
		try {
			
			List<AnnualAuditScheduleDTO> auditlist = clientServ.getAllAnnualAuditSchedule()
					.stream()
					.sorted(Comparator.comparing(AnnualAuditScheduleDTO::getUserName)
				            .thenComparing(AnnualAuditScheduleDTO::getScheduleStartDate))
				        .collect(Collectors.toList());


				List<ClientLicenseDetailsDTO> ccalist = clientServ.getAllActiveLicenseDetails();
				
				
				
				Map<String, String> userNameToLicenseeName = ccalist.stream()
				.collect(Collectors.toMap(ClientLicenseDetailsDTO::getUserName, ClientLicenseDetailsDTO::getLicenseName));
				
				
				List<AnnualAuditScheduleDTO> alist = auditlist.stream()
				
				.filter(audit -> audit.getScheduleSubmissionDate() != null && audit.getScheduleSubmissionDate().equals(audit.getDateOfSubmission()))
				
				.peek(audit -> {
				String licenseeName = userNameToLicenseeName.get(audit.getUserName());
				if (licenseeName != null) {
				audit.setUserName(licenseeName); 
				}
				})
				.collect(Collectors.toList());
			
			
				// Generate HTML content
		        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
		        
		        StringBuilder contentBuilder = new StringBuilder();
		        contentBuilder.append("<html>")
		            .append("<head>")
		            .append("<style>")
		            .append("@page { margin: 20pt; }")
		            .append("body { font-size: 16px; font-family: \"Times New Roman\", Times, serif; }")
		            .append("table { border-collapse: collapse; width: 100%;} td, th { border: 1px solid black; padding: 5px; text-align: left;}")
		            .append("</style>")
		            .append("</head>")
		            .append("<body>")
		            .append("<div style=\"text-align: center; font-size: 24px;\"><b>Controller of Certifying Authorities (CCA)</b></div>")
		            .append("<div style=\"text-align: center; font-size: 18px;\"><u>Report on Annual Audits Submitted on Time</u></div>")
		            .append("<p style=\"text-align: right;\"><b>As on ").append(currentDate).append("</b></p>")
		            .append("<p style=\"margin-top: 20px;text-align: center;text-justify: inter-word; font-weight: 600;\">Report on Annual Audits submitted on time for each CA under the Controller of Certifying Authorities (CCA)</p>")
		            .append("<table>")
		            .append("<thead><tr><th>Sl. No.</th><th>CA Name</th><th>Scheduled Audit Submission Date</th><th>Date of Submission</th></tr></thead>")
		            .append("<tbody>");

		        int serialNumber = 1;
		        
		        for (AnnualAuditScheduleDTO r : alist) {
		            contentBuilder.append("<tr>")
		                .append("<td>").append(serialNumber++).append(".</td>")
		                .append("<td>").append(r.getUserName()).append("</td>")
		                .append("<td>").append(convertToLocalDate(r.getScheduleSubmissionDate()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("</td>")
		                .append("<td>").append(convertToLocalDate(r.getDateOfSubmission()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("</td>")
		                .append("</tr>");
		        }



		        
		        contentBuilder.append("</tbody></table></body></html>");

		        // Convert HTML to PDF
		        String htmlContent = contentBuilder.toString();
		        byte[] pdfBytes;
		        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
		            ConverterProperties converterProperties = new ConverterProperties();
		            HtmlConverter.convertToPdf(htmlContent, outputStream, converterProperties);
		            pdfBytes = outputStream.toByteArray();
		        }

		        // Return PDF response
		        return ResponseEntity.ok()
		            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=AnnualAuditSubmittedOnTime.pdf")
		            .contentType(MediaType.APPLICATION_PDF)
		            .body(pdfBytes);

		    } catch (Exception e) {
		        
		        return new ResponseEntity<>("Unable to get report data.", HttpStatus.BAD_REQUEST);
		    }

		
		
		
	}
	
	
	
	@GetMapping(DashboardAPIs.GET_ANNUAL_AUDIT_SUBMITTED_LATE)
	public ResponseEntity<?> getAnnualAuditSubmittedLate(){
		

		try {
		
			List<AnnualAuditScheduleDTO> auditlist = clientServ.getAllAnnualAuditSchedule()
					.stream()
					.sorted(Comparator.comparing(AnnualAuditScheduleDTO::getUserName)
				            .thenComparing(AnnualAuditScheduleDTO::getScheduleStartDate))
				        .collect(Collectors.toList());


					List<ClientLicenseDetailsDTO> ccalist = clientServ.getAllActiveLicenseDetails();
					
					
					
					Map<String, String> userNameToLicenseeName = ccalist.stream()
					.collect(Collectors.toMap(ClientLicenseDetailsDTO::getUserName, ClientLicenseDetailsDTO::getLicenseName));
					
					
					List<AnnualAuditScheduleDTO> alist = auditlist.stream()
					
					.filter(audit -> audit.getDateOfSubmission() != null && audit.getDateOfSubmission().after(audit.getScheduleSubmissionDate()))
					
					.peek(audit -> {
					String licenseeName = userNameToLicenseeName.get(audit.getUserName());
					if (licenseeName != null) {
					audit.setUserName(licenseeName); 
					}
					})
					.collect(Collectors.toList());
			
			
		return new ResponseEntity<>(alist, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("Error in fetching data, try after some time.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@GetMapping(DashboardAPIs.DOWNLOAD_ANNUAL_AUDIT_SUBMITTED_LATE)
	public ResponseEntity<?> downloadAnnualAuditSubmittedLate(){
		

		try {
		
			List<AnnualAuditScheduleDTO> auditlist = clientServ.getAllAnnualAuditSchedule()
					.stream()
					.sorted(Comparator.comparing(AnnualAuditScheduleDTO::getUserName)
				            .thenComparing(AnnualAuditScheduleDTO::getScheduleStartDate))
				        .collect(Collectors.toList());


					List<ClientLicenseDetailsDTO> ccalist = clientServ.getAllActiveLicenseDetails();
					
					
					
					Map<String, String> userNameToLicenseeName = ccalist.stream()
					.collect(Collectors.toMap(ClientLicenseDetailsDTO::getUserName, ClientLicenseDetailsDTO::getLicenseName));
					
					
					List<AnnualAuditScheduleDTO> alist = auditlist.stream()
					
					.filter(audit -> audit.getDateOfSubmission() != null && audit.getDateOfSubmission().after(audit.getScheduleSubmissionDate()))
					
					.peek(audit -> {
					String licenseeName = userNameToLicenseeName.get(audit.getUserName());
					if (licenseeName != null) {
					audit.setUserName(licenseeName); 
					}
					})
					.collect(Collectors.toList());
			
			
					// Generate HTML content
			        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
			        
			        StringBuilder contentBuilder = new StringBuilder();
			        contentBuilder.append("<html>")
			            .append("<head>")
			            .append("<style>")
			            .append("@page { margin: 20pt; }")
			            .append("body { font-size: 16px; font-family: \"Times New Roman\", Times, serif; }")
			            .append("table { border-collapse: collapse; width: 100%;} td, th { border: 1px solid black; padding: 5px; text-align: left;}")
			            .append("</style>")
			            .append("</head>")
			            .append("<body>")
			            .append("<div style=\"text-align: center; font-size: 24px;\"><b>Controller of Certifying Authorities (CCA)</b></div>")
			            .append("<div style=\"text-align: center; font-size: 18px;\"><u>Report on Annual Audits Submitted Late</u></div>")
			            .append("<p style=\"text-align: right;\"><b>As on ").append(currentDate).append("</b></p>")
			            .append("<p style=\"margin-top: 20px;text-align: center;text-justify: inter-word; font-weight: 600;\">Report on Annual Audits submitted Late for each CA under the Controller of Certifying Authorities (CCA)</p>")
			            .append("<table>")
			            .append("<thead><tr><th>Sl. No.</th><th>CA Name</th><th>Scheduled Audit Submission Date</th><th>Date of Submission</th></tr></thead>")
			            .append("<tbody>");

			        int serialNumber = 1;
			        
			        for (AnnualAuditScheduleDTO r : alist) {
			            contentBuilder.append("<tr>")
			                .append("<td>").append(serialNumber++).append(".</td>")
			                .append("<td>").append(r.getUserName()).append("</td>")
			                .append("<td>").append(convertToLocalDate(r.getScheduleSubmissionDate()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("</td>")
			                .append("<td>").append(convertToLocalDate(r.getDateOfSubmission()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("</td>")
			                .append("</tr>");
			        }



			        
			        contentBuilder.append("</tbody></table></body></html>");

			        // Convert HTML to PDF
			        String htmlContent = contentBuilder.toString();
			        byte[] pdfBytes;
			        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			            ConverterProperties converterProperties = new ConverterProperties();
			            HtmlConverter.convertToPdf(htmlContent, outputStream, converterProperties);
			            pdfBytes = outputStream.toByteArray();
			        }

			        // Return PDF response
			        return ResponseEntity.ok()
			            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=AnnualAuditSubmittedOnTime.pdf")
			            .contentType(MediaType.APPLICATION_PDF)
			            .body(pdfBytes);

			    } catch (Exception e) {
			        
			        return new ResponseEntity<>("Unable to get report data.", HttpStatus.BAD_REQUEST);
			    }
		
	}

	
	
	@GetMapping(DashboardAPIs.GET_ANNUAL_AUDIT_CLOSURE_REPORT)
	public ResponseEntity<?> getAnnualAuditClosureReport(){
		
		try {
		
			
			List<AnnualAuditScheduleDTO> auditlist = clientServ.getAllAnnualAuditSchedule()
				    .stream()
				    .sorted(Comparator.comparing(AnnualAuditScheduleDTO::getUserName)
				        .thenComparing(AnnualAuditScheduleDTO::getScheduleStartDate))
				    .collect(Collectors.toList());

				
				List<ClientLicenseDetailsDTO> ccalist = clientServ.getAllActiveLicenseDetails();
				Map<String, String> userNameToLicenseeName = ccalist.stream()
				    .collect(Collectors.toMap(ClientLicenseDetailsDTO::getUserName, ClientLicenseDetailsDTO::getLicenseName));

				
				auditlist.forEach(audit -> {
				    String licenseeName = userNameToLicenseeName.get(audit.getUserName());
				    if (licenseeName != null) {
				        audit.setUserName(licenseeName); 
				    }
				});
					
					
					
					
			
			
					return new ResponseEntity<>(auditlist, HttpStatus.OK);
			
			
		}catch(Exception e) {
			return new ResponseEntity<>("Error in fetching data, try after some time.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	@GetMapping(DashboardAPIs.DOWNLOAD_ANNUAL_AUDIT_CLOSURE_REPORT)
	public ResponseEntity<?> downloadAnnualAuditClosureReport(){
		
		try {
		
			
			List<AnnualAuditScheduleDTO> auditlist = clientServ.getAllAnnualAuditSchedule()
				    .stream()
				    .sorted(Comparator.comparing(AnnualAuditScheduleDTO::getUserName)
				        .thenComparing(AnnualAuditScheduleDTO::getScheduleStartDate))
				    .collect(Collectors.toList());

				
				List<ClientLicenseDetailsDTO> ccalist = clientServ.getAllActiveLicenseDetails();
				Map<String, String> userNameToLicenseeName = ccalist.stream()
				    .collect(Collectors.toMap(ClientLicenseDetailsDTO::getUserName, ClientLicenseDetailsDTO::getLicenseName));

				
				auditlist.forEach(audit -> {
				    String licenseeName = userNameToLicenseeName.get(audit.getUserName());
				    if (licenseeName != null) {
				        audit.setUserName(licenseeName); 
				    }
				});
					
				// Generate HTML content
		        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
		        
		        StringBuilder contentBuilder = new StringBuilder();
		        contentBuilder.append("<html>")
		            .append("<head>")
		            .append("<style>")
		            .append("@page { margin: 20pt; }")
		            .append("body { font-size: 16px; font-family: \"Times New Roman\", Times, serif; }")
		            .append("table { border-collapse: collapse; width: 100%;} td, th { border: 1px solid black; padding: 5px; text-align: left;}")
		            .append("</style>")
		            .append("</head>")
		            .append("<body>")
		            .append("<div style=\"text-align: center; font-size: 24px;\"><b>Controller of Certifying Authorities (CCA)</b></div>")
		            .append("<div style=\"text-align: center; font-size: 18px;\"><u>Annual Audits Closure Report</u></div>")
		            .append("<p style=\"text-align: right;\"><b>As on ").append(currentDate).append("</b></p>")
		            .append("<p style=\"margin-top: 20px;text-align: center;text-justify: inter-word; font-weight: 600;\">Closure Report on Annual Audits for each CA under the Controller of Certifying Authorities (CCA)</p>")
		            .append("<table>")
		            .append("<thead><tr><th>Sl. No.</th><th>CA Name</th><th>Scheduled Audit Date</th><th>Audit Start Date</th><th>Scheduled Audit Submission Date</th><th>Audit Submission Date</th><th>Audit Completion Date</th></tr></thead>")
		            .append("<tbody>");

		        int serialNumber = 1;
		        
		        for (AnnualAuditScheduleDTO r : auditlist) {
		            contentBuilder.append("<tr>")
		                .append("<td>").append(serialNumber++).append(".</td>")
		                .append("<td>").append(r.getUserName()).append("</td>")
		                .append("<td>").append((r.getScheduleStartDate() != null? convertToLocalDate(r.getScheduleStartDate()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")): "-")).append("</td>")
		                .append("<td>").append((r.getActualStartDate() != null? convertToLocalDate(r.getActualStartDate()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")): "-")).append("</td>")
		                .append("<td>").append((r.getScheduleSubmissionDate() != null? convertToLocalDate(r.getScheduleSubmissionDate()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")): "-")).append("</td>")
		                .append("<td>").append((r.getDateOfSubmission() != null? convertToLocalDate(r.getDateOfSubmission()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")): "-")).append("</td>")
		                .append("<td>").append((r.getDateOfCompletion() != null? convertToLocalDate(r.getDateOfCompletion()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")): "-")).append("</td>")
		                .append("</tr>");
		        }



		        
		        contentBuilder.append("</tbody></table></body></html>");

		        // Convert HTML to PDF
		        String htmlContent = contentBuilder.toString();
		        byte[] pdfBytes;
		        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
		            ConverterProperties converterProperties = new ConverterProperties();
		            HtmlConverter.convertToPdf(htmlContent, outputStream, converterProperties);
		            pdfBytes = outputStream.toByteArray();
		        }

		        // Return PDF response
		        return ResponseEntity.ok()
		            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=AnnualAuditClosureReport.pdf")
		            .contentType(MediaType.APPLICATION_PDF)
		            .body(pdfBytes);

		    } catch (Exception e) {
		        
		        return new ResponseEntity<>("Unable to get report data.", HttpStatus.BAD_REQUEST);
		    }
		
	}
	
	
	
	
	
	@GetMapping(DashboardAPIs.GET_AUDIT_CONDUCTED_BY_AUDIT_AGENCY)
	public ResponseEntity<?> getAuditConductedByAuditAgency(@RequestParam("id") String agencyId, @RequestParam("pid") String fDate, @RequestParam("qid") String tDate){
		
		
		try {
		
		String auditAgencyId = EncryptionUtil.decrypt(agencyId);
		
		if (isNullOrEmpty(fDate) || isNullOrEmpty(tDate)) {
	    	return ResponseEntity.ok(Collections.emptyList());
	    }

	    String fromDate = EncryptionUtil.decrypt(fDate);
	    String toDate = EncryptionUtil.decrypt(tDate);

	    if (isNullOrEmpty(fromDate) || isNullOrEmpty(toDate)) {
	    	return ResponseEntity.ok(Collections.emptyList());
	    }

	    // Parse dates
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    LocalDate startDate;
	    LocalDate endDate;

	    try {
	        startDate = LocalDate.parse(fromDate, formatter);
	        endDate = LocalDate.parse(toDate, formatter);
	    } catch (DateTimeParseException e) {
	    	return ResponseEntity.ok(Collections.emptyList());
	    }

	    if (!endDate.isAfter(startDate)) {
	    	return ResponseEntity.ok(Collections.emptyList());
	    }

		
		
		List<ApplicationAuditorsDTO> applicationAuditors = clientServ.getAllActiveApplicationAuditors();
		List<AuditAgencySelectionDTO> auditAgencySelected = clientServ.getAllSelectedAuditAgency();
		
		List<AuditAgencySelectionDTO> selectedAgency = auditAgencySelected.stream()
			    .filter(agency -> agency.getAuditAgencyId() != null && agency.getStatus().equals("Active") && agency.getAuditAgencyId().equals(auditAgencyId))
			    .collect(Collectors.toList());
			
		
		List<AuditByAuditAgencyDTO> auditByAuditAgencyList = new ArrayList<>();
		
		
		if(!selectedAgency.isEmpty()) {
		
			for(AuditAgencySelectionDTO ele: selectedAgency) {
			
				
				AuditByAuditAgencyDTO obj = new AuditByAuditAgencyDTO();
				
				List<ApplicationAuditorsDTO> sapplicationAuditors = applicationAuditors.stream()
																	.filter((auditor)->auditor.getAppAuditId().getAppAuditId() == ele.getAppAuditId().getAppAuditId())
																	.collect(Collectors.toList());
				
				String auditorsName = "";
				
				int i=0;
				for(ApplicationAuditorsDTO a: sapplicationAuditors) {
					if(a.getStatus().equals("Active")) {
						if(i>0 && i<sapplicationAuditors.size())
							auditorsName += ", ";
						auditorsName += a.getFullName();
						i++;
					}
				}
				
				if(auditorsName.isEmpty() || auditorsName.isBlank())
					auditorsName = "-";
				
				String applicantUsername = null;

				FirmApplicationMainDTO firmApp = clientServ.getFirmApplicationByUsername(ele.getAppAuditId().getApplicantUserName());
				
				if(firmApp != null && firmApp.getAppFirmApplication() != null) {
					
					applicantUsername = firmApp.getAppFirmApplication().getOfficeName()!=null?formatName(firmApp.getAppFirmApplication().getOfficeName()):"-";
					
				}else {
				
					GovtOrganizationApplicationMainDTO govtApp = clientServ.getGovtOrgAppByUsername(ele.getAppAuditId().getApplicantUserName());
					
					if(govtApp != null && govtApp.getAppGovtOrganizationApplication() != null) {
						
						applicantUsername = govtApp.getAppGovtOrganizationApplication().getOrgName() != null?formatName(govtApp.getAppGovtOrganizationApplication().getOrgName()):"-";
						
					}else {
				
						IndivApplicationMainDTO indivApp = clientServ.getIndivAppByUsername(ele.getAppAuditId().getApplicantUserName());
						
							if(indivApp != null && indivApp.getApplication() != null) {
								
								String fname = formatName(indivApp.getApplication().getFirstName1());
								String mname = formatName(indivApp.getApplication().getMiddleName1());
								String lname = formatName(indivApp.getApplication().getLastName1());
								applicantUsername = fname + mname + lname;
						}else {
							
							applicantUsername = "-";	
						}	
					}					
				}

				obj.setAuditConductedFor(applicantUsername);
				obj.setAuditCompletedOn(ele.getAppAuditId().getAuditCompletedOn());
				obj.setAuditInitiatedOn(ele.getAppAuditId().getAuditInitiatedOn());
				obj.setAuditors(auditorsName);
				obj.setAuditType(ele.getAppAuditId().getAuditType() != null ? ele.getAppAuditId().getAuditType() : "-" );
				
		
				auditByAuditAgencyList.add(obj);
				
			}
		
		}
		
		
		
		List<AnnualAuditScheduleDTO> auditlists = clientServ.getAllAnnualAuditSchedule().stream()
												.filter(audit -> audit.getAuditAgencyId().equals(auditAgencyId))
												.collect(Collectors.toList());
		
		List<LicenseeAuditorsDTO> annualAuditors = clientServ.getAllActiveAnnualApplicationAuditors();
		
		List<ClientLicenseDetailsDTO> ccalist = clientServ.getAllActiveLicenseDetails();
		Map<String, String> userNameToLicenseeName = ccalist.stream()
		    .collect(Collectors.toMap(ClientLicenseDetailsDTO::getUserName, ClientLicenseDetailsDTO::getLicenseName));

		
		for(AnnualAuditScheduleDTO a: auditlists) {
		
			
			AuditByAuditAgencyDTO obj = new AuditByAuditAgencyDTO();
			
			List<LicenseeAuditorsDTO> sapplicationAuditors = annualAuditors.stream()
																.filter((auditor)->auditor.getLicenseeAuditId().getAuditScheduleId().getAuditScheduleId() == a.getAuditScheduleId())
																.collect(Collectors.toList());
			
			String auditorsName = "";
			
			int i=0;
			for(LicenseeAuditorsDTO a11: sapplicationAuditors) {
				if(a.getStatus().equals("Active")) {
					if(i>0 && i<sapplicationAuditors.size())
						auditorsName += ", ";
					auditorsName += a11.getFullName();
					i++;
				}
			}
			
			if(auditorsName.isEmpty() || auditorsName.isBlank())
				auditorsName = "-";
			
			
			String licenseeName = userNameToLicenseeName.get(a.getUserName());

			

			obj.setAuditConductedFor(licenseeName != null ? licenseeName : "-");
			obj.setAuditCompletedOn(a.getDateOfCompletion());
			obj.setAuditInitiatedOn(a.getActualStartDate());
			obj.setAuditors(auditorsName);
			obj.setAuditType("Annual Audit");
			
			
			
			auditByAuditAgencyList.add(obj);
			
			
		}
		
		
		List<AuditByAuditAgencyDTO> filteredAuditByAuditAgencyList = auditByAuditAgencyList.stream()
		    .filter(audit -> {
		        LocalDate auditStartDate = convertToLocalDate(audit.getAuditInitiatedOn());
		        LocalDate auditEndDate = convertToLocalDate(audit.getAuditCompletedOn());
		        
		        return auditStartDate != null && auditEndDate != null
		        		&& !auditStartDate.isBefore(startDate) 
		                && !auditEndDate.isAfter(endDate);
		    })
		    .collect(Collectors.toList());

		
		
		return new ResponseEntity<>(filteredAuditByAuditAgencyList, HttpStatus.OK);
		
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
		}
		

	}
	
	
	@GetMapping(DashboardAPIs.DOWNLOAD_AUDIT_CONDUCTED_BY_AUDIT_AGENCY)
	public ResponseEntity<?> downloadAuditConductedByAuditAgency(@RequestParam("id") String agencyId, @RequestParam("pid") String fDate, @RequestParam("qid") String tDate){
		
		try {
			
		String auditAgencyId = EncryptionUtil.decrypt(agencyId);
		
		if (isNullOrEmpty(fDate) || isNullOrEmpty(tDate)) {
	    	return ResponseEntity.ok(Collections.emptyList());
	    }

	    String fromDate = EncryptionUtil.decrypt(fDate);
	    String toDate = EncryptionUtil.decrypt(tDate);

	    if (isNullOrEmpty(fromDate) || isNullOrEmpty(toDate)) {
	    	return ResponseEntity.ok(Collections.emptyList());
	    }

	    // Parse dates
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    LocalDate startDate;
	    LocalDate endDate;

	    try {
	        startDate = LocalDate.parse(fromDate, formatter);
	        endDate = LocalDate.parse(toDate, formatter);
	    } catch (DateTimeParseException e) {
	    	return ResponseEntity.ok(Collections.emptyList());
	    }

	    if (!endDate.isAfter(startDate)) {
	    	return ResponseEntity.ok(Collections.emptyList());
	    }

		
		
		List<ApplicationAuditorsDTO> applicationAuditors = clientServ.getAllActiveApplicationAuditors();
		List<AuditAgencySelectionDTO> auditAgencySelected = clientServ.getAllSelectedAuditAgency();
		List<AuditAgencyDTO> agencylist = clientServ.getAllAuditAgencyList();
		
		AuditAgencyDTO agencys = agencylist.stream()
								.filter((f)->Long.parseLong(f.getAuditAgencyId().trim()) == Long.parseLong(auditAgencyId.trim()))
								.findFirst()
								.orElse(null);
		
		String auditAgencyName = agencys != null ? agencys.getAuditAgencyName() : "<Name Not Found>";
		
		
		List<AuditAgencySelectionDTO> selectedAgency = auditAgencySelected.stream()
			    .filter(agency -> agency.getAuditAgencyId() != null && agency.getStatus().equals("Active") && agency.getAuditAgencyId().equals(auditAgencyId))
			    .collect(Collectors.toList());
		
		
		List<AuditByAuditAgencyDTO> auditByAuditAgencyList = new ArrayList<>();
		
		
		if(!selectedAgency.isEmpty()) {
		
			for(AuditAgencySelectionDTO ele: selectedAgency) {
			
				
				AuditByAuditAgencyDTO obj = new AuditByAuditAgencyDTO();
				
				List<ApplicationAuditorsDTO> sapplicationAuditors = applicationAuditors.stream()
																	.filter((auditor)->auditor.getAppAuditId().getAppAuditId() == ele.getAppAuditId().getAppAuditId())
																	.collect(Collectors.toList());
				
				String auditorsName = "";
				
				int i=0;
				for(ApplicationAuditorsDTO a: sapplicationAuditors) {
					if(a.getStatus().equals("Active")) {
						if(i>0 && i<sapplicationAuditors.size())
							auditorsName += ", ";
						auditorsName += a.getFullName();
						i++;
					}
				}
				
				if(auditorsName.isEmpty() || auditorsName.isBlank())
					auditorsName = "-";
				
				String applicantUsername = null;

				FirmApplicationMainDTO firmApp = clientServ.getFirmApplicationByUsername(ele.getAppAuditId().getApplicantUserName());
				
				if(firmApp != null && firmApp.getAppFirmApplication() != null) {
					
					applicantUsername = firmApp.getAppFirmApplication().getOfficeName()!=null?formatName(firmApp.getAppFirmApplication().getOfficeName()):"-";
					
				}else {
				
					GovtOrganizationApplicationMainDTO govtApp = clientServ.getGovtOrgAppByUsername(ele.getAppAuditId().getApplicantUserName());
					
					if(govtApp != null && govtApp.getAppGovtOrganizationApplication() != null) {
						
						applicantUsername = govtApp.getAppGovtOrganizationApplication().getOrgName() != null?formatName(govtApp.getAppGovtOrganizationApplication().getOrgName()):"-";
						
					}else {
				
						IndivApplicationMainDTO indivApp = clientServ.getIndivAppByUsername(ele.getAppAuditId().getApplicantUserName());
						
							if(indivApp != null && indivApp.getApplication() != null) {
								
								String fname = formatName(indivApp.getApplication().getFirstName1());
								String mname = formatName(indivApp.getApplication().getMiddleName1());
								String lname = formatName(indivApp.getApplication().getLastName1());
								applicantUsername = fname + mname + lname;
						}else {
							
							applicantUsername = "-";	
						}	
					}					
				}

				obj.setAuditConductedFor(applicantUsername);
				obj.setAuditCompletedOn(ele.getAppAuditId().getAuditCompletedOn());
				obj.setAuditInitiatedOn(ele.getAppAuditId().getAuditInitiatedOn());
				obj.setAuditors(auditorsName);
				obj.setAuditType(ele.getAppAuditId().getAuditType() != null ? ele.getAppAuditId().getAuditType() : "-" );
				
		
				auditByAuditAgencyList.add(obj);
				
			}
		
		}
		
		
		
		List<AnnualAuditScheduleDTO> auditlists = clientServ.getAllAnnualAuditSchedule().stream()
												.filter(audit -> audit.getAuditAgencyId().equals(auditAgencyId))
												.collect(Collectors.toList());
		
		List<LicenseeAuditorsDTO> annualAuditors = clientServ.getAllActiveAnnualApplicationAuditors();
		
		List<ClientLicenseDetailsDTO> ccalist = clientServ.getAllActiveLicenseDetails();
		Map<String, String> userNameToLicenseeName = ccalist.stream()
		    .collect(Collectors.toMap(ClientLicenseDetailsDTO::getUserName, ClientLicenseDetailsDTO::getLicenseName));

		
		for(AnnualAuditScheduleDTO a: auditlists) {
		
			
			AuditByAuditAgencyDTO obj = new AuditByAuditAgencyDTO();
			
			List<LicenseeAuditorsDTO> sapplicationAuditors = annualAuditors.stream()
																.filter((auditor)->auditor.getLicenseeAuditId().getAuditScheduleId().getAuditScheduleId() == a.getAuditScheduleId())
																.collect(Collectors.toList());
			
			String auditorsName = "";
			
			int i=0;
			for(LicenseeAuditorsDTO a11: sapplicationAuditors) {
				if(a.getStatus().equals("Active")) {
					if(i>0 && i<sapplicationAuditors.size())
						auditorsName += ", ";
					auditorsName += a11.getFullName();
					i++;
				}
			}
			
			if(auditorsName.isEmpty() || auditorsName.isBlank())
				auditorsName = "-";
			
			String licenseeName = userNameToLicenseeName.get(a.getUserName());

			

			obj.setAuditConductedFor(licenseeName != null ? licenseeName : "-");
			obj.setAuditCompletedOn(a.getDateOfCompletion());
			obj.setAuditInitiatedOn(a.getActualStartDate());
			obj.setAuditors(auditorsName);
			obj.setAuditType("Annual Audit");
			
	
			auditByAuditAgencyList.add(obj);
			
			
		}
		
		
		List<AuditByAuditAgencyDTO> filteredAuditByAuditAgencyList = auditByAuditAgencyList.stream()
		    .filter(audit -> {
		        LocalDate auditStartDate = convertToLocalDate(audit.getAuditInitiatedOn());
		        LocalDate auditEndDate = convertToLocalDate(audit.getAuditCompletedOn());
		        
		        return auditStartDate != null && auditEndDate != null
		        		&& !auditStartDate.isBefore(startDate) 
		                && !auditEndDate.isAfter(endDate);
		    })
		    .collect(Collectors.toList());

		
		
		// Generate HTML content
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append("<html>")
            .append("<head>")
            .append("<style>")
            .append("@page { margin: 20pt; }")
            .append("body { font-size: 16px; font-family: \"Times New Roman\", Times, serif; }")
            .append("table { border-collapse: collapse; width: 100%;} td, th { border: 1px solid black; padding: 5px; text-align: left;}")
            .append("</style>")
            .append("</head>")
            .append("<body>")
            .append("<div style=\"text-align: center; font-size: 24px;\"><b>Controller of Certifying Authorities (CCA)</b></div>")
            .append("<div style=\"text-align: center; font-size: 18px;\"><u>Report on Audits Conducted By "+auditAgencyName+"</u></div>")
            .append("<p style=\"text-align: right;\"><b>As on ").append(currentDate).append("</b></p>")
            .append("<p style=\"margin-top: 20px;text-align: center;text-justify: inter-word; font-weight: 600;\">Report on Audits conducted from "+startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+" to "+endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+" by "+auditAgencyName+" empanelled under the Controller of Certifying Authorities (CCA)")
            .append("<table>")
            .append("<thead><tr><th>Sl. No.</th><th>Auditors</th><th>Audit Conducted For</th><th>Audit Type</th><th>Audit Initiated On</th><th>Audit Completed On</th></tr></thead>")
            .append("<tbody>");

        int serialNumber = 1;
        
        for (AuditByAuditAgencyDTO r : filteredAuditByAuditAgencyList) {
            contentBuilder.append("<tr>")
                .append("<td>").append(serialNumber++).append(".</td>")
                .append("<td>").append(r.getAuditors()).append("</td>")
                .append("<td>").append(r.getAuditConductedFor()).append("</td>")
                .append("<td>").append(r.getAuditType()).append("</td>")
                .append("<td>").append((r.getAuditInitiatedOn() != null? convertToLocalDate(r.getAuditInitiatedOn()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")): "-")).append("</td>")
                .append("<td>").append((r.getAuditCompletedOn() != null? convertToLocalDate(r.getAuditCompletedOn()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")): "-")).append("</td>")
                .append("</tr>");
        }



        
        contentBuilder.append("</tbody></table></body></html>");

        // Convert HTML to PDF
        String htmlContent = contentBuilder.toString();
        byte[] pdfBytes;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ConverterProperties converterProperties = new ConverterProperties();
            HtmlConverter.convertToPdf(htmlContent, outputStream, converterProperties);
            pdfBytes = outputStream.toByteArray();
        }

        // Return PDF response
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=AuditBy"+auditAgencyName+".pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdfBytes);

    } catch (Exception e) {
        
        return new ResponseEntity<>("Unable to get report data.", HttpStatus.BAD_REQUEST);
    }


	}
	
	
	
	@GetMapping(DashboardAPIs.GET_CA_LICENSE_DUE_FOR_RENEWAL)
	public ResponseEntity<?> getCALicenseDueForRenewal(){
		
		try {
		
		List<ClientLicenseDetailsDTO> ccalist = clientServ.getAllActiveLicenseDetails().stream()
			    .filter(f -> {
			        LocalDate expiryDate =  convertToLocalDate(f.getExpiryDate());
			        LocalDate currentDate = LocalDate.now();

			        
			        if (expiryDate != null) {
			            long daysBetween = ChronoUnit.DAYS.between(currentDate, expiryDate);
			            return daysBetween >= 0 && daysBetween <= 45;
			        }
			        return false;
			    })
			    .collect(Collectors.toList());
		
		
		
		return new ResponseEntity<>(ccalist, HttpStatus.OK);
		
		}catch(Exception e) {
			return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
		}
	}
	
	@GetMapping(DashboardAPIs.DOWNLOAD_CA_LICENSE_DUE_FOR_RENEWAL)
	public ResponseEntity<?> downloadCALicenseDueForRenewal(){
		
		try {
		
		List<ClientLicenseDetailsDTO> ccalist = clientServ.getAllActiveLicenseDetails().stream()
			    .filter(f -> {
			        LocalDate expiryDate =  convertToLocalDate(f.getExpiryDate());
			        LocalDate currentDate = LocalDate.now();

			        
			        if (expiryDate != null) {
			            long daysBetween = ChronoUnit.DAYS.between(currentDate, expiryDate);
			            return daysBetween >= 0 && daysBetween <= 45;
			        }
			        return false;
			    })
			    .collect(Collectors.toList());
		
		
		
		// Generate HTML content
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append("<html>")
            .append("<head>")
            .append("<style>")
            .append("@page { margin: 20pt; }")
            .append("body { font-size: 16px; font-family: \"Times New Roman\", Times, serif; }")
            .append("table { border-collapse: collapse; width: 100%;} td, th { border: 1px solid black; padding: 5px; text-align: left;}")
            .append("</style>")
            .append("</head>")
            .append("<body>")
            .append("<div style=\"text-align: center; font-size: 24px;\"><b>Controller of Certifying Authorities (CCA)</b></div>")
            .append("<div style=\"text-align: center; font-size: 18px;\"><u>Report on CAs licenses due for renewal</u></div>")
            .append("<p style=\"text-align: right;\"><b>As on ").append(currentDate).append("</b></p>")
            .append("<p style=\"margin-top: 20px;text-align: center;text-justify: inter-word; font-weight: 600;\">Report on Certifying Authorities (CAs) Licenses Pending For Renewal under the Controller of Certifying Authorities (CCA)")
            .append("<table>")
            .append("<thead><tr><th>Sl. No.</th><th>CA Name</th><th>License Expiry Date</th></tr></thead>")
            .append("<tbody>");

        int serialNumber = 1;
        
        for (ClientLicenseDetailsDTO r : ccalist) {
            contentBuilder.append("<tr>")
                .append("<td>").append(serialNumber++).append(".</td>")
                .append("<td>").append(r.getLicenseName()).append("</td>")
                .append("<td>").append((r.getExpiryDate() != null? convertToLocalDate(r.getExpiryDate()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")): "-")).append("</td>")
                .append("</tr>");
        }



        
        contentBuilder.append("</tbody></table></body></html>");

        // Convert HTML to PDF
        String htmlContent = contentBuilder.toString();
        byte[] pdfBytes;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ConverterProperties converterProperties = new ConverterProperties();
            HtmlConverter.convertToPdf(htmlContent, outputStream, converterProperties);
            pdfBytes = outputStream.toByteArray();
        }

        // Return PDF response
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=CALicenseDueForRenewal.pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdfBytes);

    } catch (Exception e) {
        
        return new ResponseEntity<>("Unable to get report data.", HttpStatus.BAD_REQUEST);
    }

	}
	
	
	@GetMapping(DashboardAPIs.GET_CA_DETAILS)
	public ResponseEntity<?> getCADetails() {
	    try {
	    	
	    	//
	    	List<ClientLicenseDetailsDTO> liceneelist = clientServ.getAllActiveLicenseDetails();
	    	List <IntentUniqueCodeDTO> unicodelist = clientServ.getAllIntentUniqueCode();
	    	List<AddressDTO> addressList = clientServ.getAllAddress();
	    	List<CADetailsDTO> list = new ArrayList<>();
	    	
	    	for(ClientLicenseDetailsDTO a: liceneelist) {
	    		
	    		String mobileNum="";
	    		String emails="";
	    		
	    		FirmApplicationMainDTO firmApp = clientServ.getFirmApplicationByUsername(a.getUserName());
				
				if(firmApp != null && firmApp.getAppFirmApplication() != null) {
					
					mobileNum = firmApp.getAppFirmApplication().getTelephoneNo();
					IntentUniqueCodeDTO u = unicodelist.stream()
											.filter((f)->f.getUniqueCode() == Long.parseLong(firmApp.getAppFirmApplication().getIntentAppId().getUniqueCode()))
											.findFirst()
											.orElse(null);
					
					if(u != null)
						emails = u.getEmailId().replace(".", "[dot]").replace("@", "[at]");
					else
						emails = "-";
					
				}else {
					
					GovtOrganizationApplicationMainDTO govtApp = clientServ.getGovtOrgAppByUsername(a.getUserName());
					
					if(govtApp != null && govtApp.getAppGovtOrganizationApplication() != null) {
						
						emails = govtApp.getAppGovtOrganizationApplication().getEmailId().replace(".", "[dot]").replace("@", "[at]");
						mobileNum = govtApp.getAppGovtOrganizationApplication().getTelephoneNo();
						
					}else {
						
						IndivApplicationMainDTO indivApp = clientServ.getIndivAppByUsername(a.getUserName());
						
						if(indivApp != null && indivApp.getApplication() != null && indivApp.getEmails() != null) {
							
							int k=0;
							for(IndivEmailsDTO iv: indivApp.getEmails()) {
								if(k > 0 && k < indivApp.getEmails().size())
								{
									emails += ", ";
								}
								
								emails += iv.getEmailId().replace(".", "[dot]").replace("@", "[at]");
								k++;
							}
							
							
							//
							IndivAddressMainDTO indivAddress = clientServ.getIndivAddressByUsername(a.getUserName());
							
							
							
							//
							if(indivAddress != null) {
								AddressDTO add = addressList.stream()
												 .filter((f)->f.getAddressId() == indivAddress.getIndivAddressDTO().getResidential().getAddressId())
												 .findFirst()
												 .orElse(null);
								
								if(add != null) {
									
									if(add.getMobile() != null && !add.getMobile().isEmpty()) {
										mobileNum = add.getMobile();
									}
									
									if(add.getTelephoneNo() != null && !add.getTelephoneNo().isEmpty()) {
										if(!mobileNum.isEmpty())
											mobileNum += add.getTelephoneNo();
										else
											mobileNum += " / "+add.getTelephoneNo();
									}
									
									
									
								}
								
							}
							
							
						}else {
							mobileNum="-";
				    		emails="-";
						}
						
						
					}
					
				}
				
	    		
	    	
	    		CADetailsDTO obj = new CADetailsDTO(a.getLicenseName(), mobileNum, emails);
	    	
	    		list.add(obj);
	    	}
	    	
	    	
	    	return new ResponseEntity<>(list, HttpStatus.OK);
	    	
	    }catch(Exception e) {
	    	e.printStackTrace();
	    	return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
	    	
	    }
	}
	
	@GetMapping(DashboardAPIs.GET_AUDIT_AGENCY_DETAILS)
	public ResponseEntity<?> getAuditAgencyDetails() {
	    try {
	    	
	    	List<AuditAgencyDTO> agencylist = clientServ.getAllAuditAgencyList();
	    	List<AuditAgencyDetailsDTO> list = new ArrayList<>();
	    	
	    	for(AuditAgencyDTO a: agencylist) {
	    
	    		String mobiles = "";
	    		String emails = "";
	    		
	    		int k=0;
	    		for(AuditAgencyMobileDTO m: a.getPhoneRecord()) {
	    			
	    			if(k>0 && k<a.getPhoneRecord().size())
	    				mobiles += " / ";
	    			
	    			if(m.getAreaCode() != null && !m.getAreaCode().isEmpty()) {
	    				
	    				if(m.getAreaCode().trim().equals("91")) {
	    					mobiles+= "+"+m.getAreaCode().trim()+" - "+m.getMobile().trim();
	    				}else {
	    					mobiles+= m.getAreaCode().trim()+" - "+m.getMobile().trim();
	    				}
	    				
	    			}else {
	    				mobiles += m.getMobile().trim();
	    			}
	    			
	    			
	    			k++;
	    		}
	    		
	    		int r=0;
	    		for(AuditAgencyEmailDTO m: a.getEmailId()) {
	    			
	    			if(r>0 && r<a.getEmailId().size())
	    				emails += ", ";
	    				emails += m.getEmail().replace(".", "[dot]").replace("@", "[at]");
	    				r++;
	    		}
	    		
	    	
	    		AuditAgencyDetailsDTO obj = new AuditAgencyDetailsDTO(a.getAuditAgencyName(), mobiles, emails);
	    	
	    		list.add(obj);
	    		
	    	}

	    	return new ResponseEntity<>(list, HttpStatus.OK);
	    	
	    }catch(Exception e) {
	    	
	    	return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
	    	
	    }
	}
	
	@GetMapping(DashboardAPIs.GET_CA_SITE_LOCATIONS)
	public ResponseEntity<?> getCASiteLocations() {
	    try {
	    	
	    	List<ClientLicenseDetailsDTO> licenseelist = clientServ.getAllActiveLicenseDetails();
	    	List<AppLocationDTO> applocationlist = clientServ.getAllActiveAppLocations();
	    	List<AddressDTO> addresslist = clientServ.getAllAddress();

	    	
	    	Map<Long, AddressDTO> addressMap = addresslist.stream()
	    	    .collect(Collectors.toMap(AddressDTO::getAddressId, Function.identity()));

	    	List<CASiteLocationsDTO> list = new ArrayList<>();
	    	
	    	for (ClientLicenseDetailsDTO license : licenseelist) {
	    	    
	    	    Map<String, AppLocationDTO> locationMap = applocationlist.stream()
	    	        .filter(location -> location.getIntentAppId().getUserName().trim().equals(license.getUserName().trim()))
	    	        .collect(Collectors.toMap(AppLocationDTO::getLocationName, Function.identity(), (a, b) -> a)); 

	    	    Function<AppLocationDTO, String> decryptAddressId = location -> {
	    	    	
	    	        if (location == null || location.getAddressId() == null) return null;
	    	        
	    	        
	    	        String decrypted = location.getAddressId() != null ? 
	    	        						EncryptionUtil.decrypt(location.getAddressId()) != null ? EncryptionUtil.decrypt(location.getAddressId()) : location.getAddressId()
	    	        								: null;
	    	        return decrypted != null ? decrypted : null;
	    	    };

	    	    String prSiteId = decryptAddressId.apply(locationMap.get("PR"));
	    	    String drSiteId = decryptAddressId.apply(locationMap.get("DR"));
	    	    String registeredOfficeId = decryptAddressId.apply(locationMap.get("Registered Office"));
	    	    String otherSiteId = decryptAddressId.apply(locationMap.get("Other"));

	    	    
	    	    
	    	    AddressDTO prAddressObj = prSiteId != null ? addressMap.get(Long.parseLong(prSiteId)) : null;
	    	    AddressDTO drAddressObj = drSiteId != null ? addressMap.get(Long.parseLong(drSiteId)) : null;
	    	    AddressDTO registeredOfficeAddressObj = registeredOfficeId != null ? addressMap.get(Long.parseLong(registeredOfficeId)) : null;
	    	    AddressDTO otherAddressObj = otherSiteId != null ? addressMap.get(Long.parseLong(otherSiteId)) : null;

	    	    
	    	    
	    	    String prAddress = "-";
	    	    String drAddress = "-";
	    	    String registeredOfficeAddress = "-";
	    	    String otherAddress = "-";
	    	    
	    	    if(prAddressObj != null) {
	    	    	
	    	    	prAddress =  prAddressObj.getBlockNo()+", Vill.: ";
	    	    	prAddress += prAddressObj.getVillage()+", P.O.: ";
	    	    	prAddress += prAddressObj.getPostOffice()+", ";
	    	    	prAddress += prAddressObj.getSubDivision()+ ", ";
	    	    	prAddress += prAddressObj.getCityId().getCityName()+", ";
	    	    	prAddress += prAddressObj.getStateId().getStateName()+", ";
	    	    	prAddress += prAddressObj.getCountryId().getCountryName()+ " - ";
	    	    	prAddress += prAddressObj.getPincode();
	    	    	
	    	    }
	    	    
				if(drAddressObj != null) {
					    	    	
					drAddress =  drAddressObj.getBlockNo()+", Vill.: ";
					drAddress += drAddressObj.getVillage()+", P.O.: ";
					drAddress += drAddressObj.getPostOffice()+", ";
					drAddress += drAddressObj.getSubDivision()+ ", ";
					drAddress += drAddressObj.getCityId().getCityName()+", ";
					drAddress += drAddressObj.getStateId().getStateName()+", ";
					drAddress += drAddressObj.getCountryId().getCountryName()+ " - ";
					drAddress += drAddressObj.getPincode();
					    	    	
				}
				
				
				if(registeredOfficeAddressObj != null) {
					
					registeredOfficeAddress =  registeredOfficeAddressObj.getBlockNo()+", Vill.: ";
					registeredOfficeAddress += registeredOfficeAddressObj.getVillage()+", P.O.: ";
					registeredOfficeAddress += registeredOfficeAddressObj.getPostOffice()+", ";
					registeredOfficeAddress += registeredOfficeAddressObj.getSubDivision()+ ", ";
					registeredOfficeAddress += registeredOfficeAddressObj.getCityId().getCityName()+", ";
					registeredOfficeAddress += registeredOfficeAddressObj.getStateId().getStateName()+", ";
					registeredOfficeAddress += registeredOfficeAddressObj.getCountryId().getCountryName()+ " - ";
					registeredOfficeAddress += registeredOfficeAddressObj.getPincode();
					
				}
				
				
				if(otherAddressObj != null) {
					
					otherAddress =  otherAddressObj.getBlockNo()+", Vill.: ";
					otherAddress += otherAddressObj.getVillage()+", P.O.: ";
					otherAddress += otherAddressObj.getPostOffice()+", ";
					otherAddress += otherAddressObj.getSubDivision()+ ", ";
					otherAddress += otherAddressObj.getCityId().getCityName()+", ";
					otherAddress += otherAddressObj.getStateId().getStateName()+", ";
					otherAddress += otherAddressObj.getCountryId().getCountryName()+ " - ";
					otherAddress += otherAddressObj.getPincode();
					
				}
	    	    
	    	    
	    	    CASiteLocationsDTO obj = new CASiteLocationsDTO(license.getLicenseName(), prAddress, drAddress, registeredOfficeAddress, otherAddress);
		    	list.add(obj);
	    	}

	    		    	
	    	return new ResponseEntity<>(list, HttpStatus.OK);
	    	
	    }catch(Exception e) {
	    	e.printStackTrace();
	    	return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
	    	
	    }
	}
	
	
	@GetMapping(DashboardAPIs.DOWNLOAD_CA_DETAILS)
	public ResponseEntity<?> downloadCADetails() {
	    try {
	    	 
	    	//
	    	List<ClientLicenseDetailsDTO> liceneelist = clientServ.getAllActiveLicenseDetails();
	    	List <IntentUniqueCodeDTO> unicodelist = clientServ.getAllIntentUniqueCode();
	    	List<AddressDTO> addressList = clientServ.getAllAddress();
	    	List<CADetailsDTO> list = new ArrayList<>();
	    	
	    	for(ClientLicenseDetailsDTO a: liceneelist) {
	    		
	    		String mobileNum="";
	    		String emails="";
	    		
	    		FirmApplicationMainDTO firmApp = clientServ.getFirmApplicationByUsername(a.getUserName());
				
				if(firmApp != null && firmApp.getAppFirmApplication() != null) {
					
					mobileNum = firmApp.getAppFirmApplication().getTelephoneNo();
					IntentUniqueCodeDTO u = unicodelist.stream()
											.filter((f)->f.getUniqueCode() == Long.parseLong(firmApp.getAppFirmApplication().getIntentAppId().getUniqueCode()))
											.findFirst()
											.orElse(null);
					
					if(u != null)
						emails = u.getEmailId().replace(".", "[dot]").replace("@", "[at]");
					else
						emails = "-";
					
				}else {
					
					GovtOrganizationApplicationMainDTO govtApp = clientServ.getGovtOrgAppByUsername(a.getUserName());
					
					if(govtApp != null && govtApp.getAppGovtOrganizationApplication() != null) {
						
						emails = govtApp.getAppGovtOrganizationApplication().getEmailId().replace(".", "[dot]").replace("@", "[at]");
						mobileNum = govtApp.getAppGovtOrganizationApplication().getTelephoneNo();
						
					}else {
						
						IndivApplicationMainDTO indivApp = clientServ.getIndivAppByUsername(a.getUserName());
						
						if(indivApp != null && indivApp.getApplication() != null && indivApp.getEmails() != null) {
							
							int k=0;
							for(IndivEmailsDTO iv: indivApp.getEmails()) {
								if(k > 0 && k < indivApp.getEmails().size())
								{
									emails += ", ";
								}
								
								emails += iv.getEmailId().replace(".", "[dot]").replace("@", "[at]");
								k++;
							}
							
							
							//
							IndivAddressMainDTO indivAddress = clientServ.getIndivAddressByUsername(a.getUserName());
							//
							if(indivAddress != null) {
								AddressDTO add = addressList.stream()
												 .filter((f)->f.getAddressId() == indivAddress.getIndivAddressDTO().getResidential().getAddressId())
												 .findFirst()
												 .orElse(null);
								
								if(add != null) {
									
									if(add.getMobile() != null && !add.getMobile().isEmpty()) {
										mobileNum = add.getMobile();
									}
									
									if(add.getTelephoneNo() != null && !add.getTelephoneNo().isEmpty()) {
										if(!mobileNum.isEmpty())
											mobileNum += add.getTelephoneNo();
										else
											mobileNum += " / "+add.getTelephoneNo();
									}
									
									
									
								}
								
							}
							
							
						}else {
							mobileNum="-";
				    		emails="-";
						}
						
						
					}
					
				}
				
	    		
	    	
	    		CADetailsDTO obj = new CADetailsDTO(a.getLicenseName(), mobileNum, emails);
	    	
	    		list.add(obj);
	    	}
	    	
	    	// Generate HTML content
	        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
	        
	        StringBuilder contentBuilder = new StringBuilder();
	        contentBuilder.append("<html>")
	            .append("<head>")
	            .append("<style>")
	            .append("@page { margin: 20pt; }")
	            .append("body { font-size: 16px; font-family: \"Times New Roman\", Times, serif; }")
	            .append("table { border-collapse: collapse; width: 100%;} td, th { border: 1px solid black; padding: 5px; text-align: left;}")
	            .append("</style>")
	            .append("</head>")
	            .append("<body>")
	            .append("<div style=\"text-align: center; font-size: 24px;\"><b>Controller of Certifying Authorities (CCA)</b></div>")
	            .append("<div style=\"text-align: center; font-size: 18px;\"><u>Report on CA Details</u></div>")
	            .append("<p style=\"text-align: right;\"><b>As on ").append(currentDate).append("</b></p>")
	            .append("<p style=\"margin-top: 20px;text-align: center;text-justify: inter-word; font-weight: 600;\">Report on CA details under the Controller of Certifying Authorities (CCA)")
	            .append("<table>")
	            .append("<thead><tr><th>Sl. No.</th><th>CA Name</th><th>Telephone/Mobile Number(s)</th><th>Email Id (s)</th></tr></thead>")
	            .append("<tbody>");

	        int serialNumber = 1;
	        
	        for (CADetailsDTO r : list) {
	            contentBuilder.append("<tr>")
	                .append("<td>").append(serialNumber++).append(".</td>")
	                .append("<td>").append(r.getCaName()).append("</td>")
	                .append("<td>").append(r.getMobileNumbers()).append("</td>")
	                .append("<td>").append(r.getEmailIds()).append("</td>")
	                .append("</tr>");
	        }



	        
	        contentBuilder.append("</tbody></table></body></html>");

	        // Convert HTML to PDF
	        String htmlContent = contentBuilder.toString();
	        byte[] pdfBytes;
	        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
	            ConverterProperties converterProperties = new ConverterProperties();
	            HtmlConverter.convertToPdf(htmlContent, outputStream, converterProperties);
	            pdfBytes = outputStream.toByteArray();
	        }

	        // Return PDF response
	        return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=CADetails.pdf")
	            .contentType(MediaType.APPLICATION_PDF)
	            .body(pdfBytes);

	    } catch (Exception e) {
	        
	        return new ResponseEntity<>("Unable to get report data.", HttpStatus.BAD_REQUEST);
	    }
	}
	
	
	@GetMapping(DashboardAPIs.DOWNLOAD_AUDIT_AGENCY_DETAILS)
	public ResponseEntity<?> downloadAuditAgencyDetails() {
	    try {
	    	
	    	List<AuditAgencyDTO> agencylist = clientServ.getAllAuditAgencyList();
	    	List<AuditAgencyDetailsDTO> list = new ArrayList<>();
	    	
	    	for(AuditAgencyDTO a: agencylist) {
	    
	    		String mobiles = "";
	    		String emails = "";
	    		
	    		int k=0;
	    		for(AuditAgencyMobileDTO m: a.getPhoneRecord()) {
	    			
	    			if(k>0 && k<a.getPhoneRecord().size())
	    				mobiles += " / ";
	    			
	    			if(m.getAreaCode() != null && !m.getAreaCode().isEmpty()) {
	    				
	    				if(m.getAreaCode().trim().equals("91")) {
	    					mobiles+= "+"+m.getAreaCode().trim()+" - "+m.getMobile().trim();
	    				}else {
	    					mobiles+= m.getAreaCode().trim()+" - "+m.getMobile().trim();
	    				}
	    				
	    			}else {
	    				mobiles += m.getMobile().trim();
	    			}
	    			
	    			
	    			k++;
	    		}
	    		
	    		int r=0;
	    		for(AuditAgencyEmailDTO m: a.getEmailId()) {
	    			
	    			if(r>0 && r<a.getEmailId().size())
	    				emails += ", ";
	    				emails += m.getEmail();
	    				r++;
	    		}
	    		
	    	
	    		AuditAgencyDetailsDTO obj = new AuditAgencyDetailsDTO(a.getAuditAgencyName(), mobiles, emails);
	    	
	    		list.add(obj);
	    		
	    	}
	    	
	    	// Generate HTML content
	        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
	        
	        StringBuilder contentBuilder = new StringBuilder();
	        contentBuilder.append("<html>")
	            .append("<head>")
	            .append("<style>")
	            .append("@page { margin: 20pt; }")
	            .append("body { font-size: 16px; font-family: \"Times New Roman\", Times, serif; }")
	            .append("table { border-collapse: collapse; width: 100%;} td, th { border: 1px solid black; padding: 5px; text-align: left;}")
	            .append("</style>")
	            .append("</head>")
	            .append("<body>")
	            .append("<div style=\"text-align: center; font-size: 24px;\"><b>Controller of Certifying Authorities (CCA)</b></div>")
	            .append("<div style=\"text-align: center; font-size: 18px;\"><u>Report on Audit Agency Details</u></div>")
	            .append("<p style=\"text-align: right;\"><b>As on ").append(currentDate).append("</b></p>")
	            .append("<p style=\"margin-top: 20px;text-align: center;text-justify: inter-word; font-weight: 600;\">Report on Empanelled Audit Agency details under the Controller of Certifying Authorities (CCA)")
	            .append("<table>")
	            .append("<thead><tr><th>Sl. No.</th><th>Audit Agency Name</th><th>Telephone/Mobile Number(s)</th><th>Email Id (s)</th></tr></thead>")
	            .append("<tbody>");

	        int serialNumber = 1;
	        
	        for (AuditAgencyDetailsDTO r : list) {
	            contentBuilder.append("<tr>")
	                .append("<td>").append(serialNumber++).append(".</td>")
	                .append("<td>").append(r.getAuditAgencyName()).append("</td>")
	                .append("<td>").append(r.getMobileNumbers()).append("</td>")
	                .append("<td>").append(r.getEmailIds()).append("</td>")
	                .append("</tr>");
	        }



	        
	        contentBuilder.append("</tbody></table></body></html>");

	        // Convert HTML to PDF
	        String htmlContent = contentBuilder.toString();
	        byte[] pdfBytes;
	        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
	            ConverterProperties converterProperties = new ConverterProperties();
	            HtmlConverter.convertToPdf(htmlContent, outputStream, converterProperties);
	            pdfBytes = outputStream.toByteArray();
	        }

	        // Return PDF response
	        return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=AuditAgencyDetails.pdf")
	            .contentType(MediaType.APPLICATION_PDF)
	            .body(pdfBytes);

	    } catch (Exception e) {
	        
	        return new ResponseEntity<>("Unable to get report data.", HttpStatus.BAD_REQUEST);
	    }
	}
	
	
	@GetMapping(DashboardAPIs.DOWNLOAD_CA_SITE_LOCATIONS)
	public ResponseEntity<?> downloadCASiteLocations() {
	    try {
	    	
	    	List<ClientLicenseDetailsDTO> licenseelist = clientServ.getAllActiveLicenseDetails();
	    	List<AppLocationDTO> applocationlist = clientServ.getAllActiveAppLocations();
	    	List<AddressDTO> addresslist = clientServ.getAllAddress();

	    	
	    	Map<Long, AddressDTO> addressMap = addresslist.stream()
	    	    .collect(Collectors.toMap(AddressDTO::getAddressId, Function.identity()));

	    	List<CASiteLocationsDTO> list = new ArrayList<>();
	    	
	    	for (ClientLicenseDetailsDTO license : licenseelist) {
	    	    
	    	    Map<String, AppLocationDTO> locationMap = applocationlist.stream()
	    	        .filter(location -> location.getIntentAppId().getUserName().trim().equals(license.getUserName().trim()))
	    	        .collect(Collectors.toMap(AppLocationDTO::getLocationName, Function.identity(), (a, b) -> a)); 

	    	    Function<AppLocationDTO, String> decryptAddressId = location -> {
	    	    	
	    	        if (location == null || location.getAddressId() == null) return null;
	    	        
	    	        
	    	        String decrypted = location.getAddressId() != null ? 
	    	        						EncryptionUtil.decrypt(location.getAddressId()) != null ? EncryptionUtil.decrypt(location.getAddressId()) : location.getAddressId()
	    	        								: null;
	    	        return decrypted != null ? decrypted : null;
	    	    };

	    	    String prSiteId = decryptAddressId.apply(locationMap.get("PR"));
	    	    String drSiteId = decryptAddressId.apply(locationMap.get("DR"));
	    	    String registeredOfficeId = decryptAddressId.apply(locationMap.get("Registered Office"));
	    	    String otherSiteId = decryptAddressId.apply(locationMap.get("Other"));

	    	    
	    	    
	    	    AddressDTO prAddressObj = prSiteId != null ? addressMap.get(Long.parseLong(prSiteId)) : null;
	    	    AddressDTO drAddressObj = drSiteId != null ? addressMap.get(Long.parseLong(drSiteId)) : null;
	    	    AddressDTO registeredOfficeAddressObj = registeredOfficeId != null ? addressMap.get(Long.parseLong(registeredOfficeId)) : null;
	    	    AddressDTO otherAddressObj = otherSiteId != null ? addressMap.get(Long.parseLong(otherSiteId)) : null;

	    	    
	    	    
	    	    String prAddress = "-";
	    	    String drAddress = "-";
	    	    String registeredOfficeAddress = "-";
	    	    String otherAddress = "-";
	    	    
	    	    if(prAddressObj != null) {
	    	    	
	    	    	prAddress =  prAddressObj.getBlockNo()+", Vill.: ";
	    	    	prAddress += prAddressObj.getVillage()+", P.O.: ";
	    	    	prAddress += prAddressObj.getPostOffice()+", ";
	    	    	prAddress += prAddressObj.getSubDivision()+ ", ";
	    	    	prAddress += prAddressObj.getCityId().getCityName()+", ";
	    	    	prAddress += prAddressObj.getStateId().getStateName()+", ";
	    	    	prAddress += prAddressObj.getCountryId().getCountryName()+ " - ";
	    	    	prAddress += prAddressObj.getPincode();
	    	    	
	    	    }
	    	    
				if(drAddressObj != null) {
					    	    	
					drAddress =  drAddressObj.getBlockNo()+", Vill.: ";
					drAddress += drAddressObj.getVillage()+", P.O.: ";
					drAddress += drAddressObj.getPostOffice()+", ";
					drAddress += drAddressObj.getSubDivision()+ ", ";
					drAddress += drAddressObj.getCityId().getCityName()+", ";
					drAddress += drAddressObj.getStateId().getStateName()+", ";
					drAddress += drAddressObj.getCountryId().getCountryName()+ " - ";
					drAddress += drAddressObj.getPincode();
					    	    	
				}
				
				
				if(registeredOfficeAddressObj != null) {
					
					registeredOfficeAddress =  registeredOfficeAddressObj.getBlockNo()+", Vill.: ";
					registeredOfficeAddress += registeredOfficeAddressObj.getVillage()+", P.O.: ";
					registeredOfficeAddress += registeredOfficeAddressObj.getPostOffice()+", ";
					registeredOfficeAddress += registeredOfficeAddressObj.getSubDivision()+ ", ";
					registeredOfficeAddress += registeredOfficeAddressObj.getCityId().getCityName()+", ";
					registeredOfficeAddress += registeredOfficeAddressObj.getStateId().getStateName()+", ";
					registeredOfficeAddress += registeredOfficeAddressObj.getCountryId().getCountryName()+ " - ";
					registeredOfficeAddress += registeredOfficeAddressObj.getPincode();
					
				}
				
				
				if(otherAddressObj != null) {
					
					otherAddress =  otherAddressObj.getBlockNo()+", Vill.: ";
					otherAddress += otherAddressObj.getVillage()+", P.O.: ";
					otherAddress += otherAddressObj.getPostOffice()+", ";
					otherAddress += otherAddressObj.getSubDivision()+ ", ";
					otherAddress += otherAddressObj.getCityId().getCityName()+", ";
					otherAddress += otherAddressObj.getStateId().getStateName()+", ";
					otherAddress += otherAddressObj.getCountryId().getCountryName()+ " - ";
					otherAddress += otherAddressObj.getPincode();
					
				}
	    	    
	    	    
	    	    CASiteLocationsDTO obj = new CASiteLocationsDTO(license.getLicenseName(), prAddress, drAddress, registeredOfficeAddress, otherAddress);
		    	list.add(obj);
	    	}

	    	
	    	// Generate HTML content
	        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
	        
	        StringBuilder contentBuilder = new StringBuilder();
	        contentBuilder.append("<html>")
	            .append("<head>")
	            .append("<style>")
	            .append("@page { margin: 20pt; }")
	            .append("body { font-size: 16px; font-family: \"Times New Roman\", Times, serif; }")
	            .append("table { border-collapse: collapse; width: 100%;} td, th { border: 1px solid black; padding: 5px; text-align: left;}")
	            .append("</style>")
	            .append("</head>")
	            .append("<body>")
	            .append("<div style=\"text-align: center; font-size: 24px;\"><b>Controller of Certifying Authorities (CCA)</b></div>")
	            .append("<div style=\"text-align: center; font-size: 18px;\"><u>Report on CA Site Location(s)</u></div>")
	            .append("<p style=\"text-align: right;\"><b>As on ").append(currentDate).append("</b></p>")
	            .append("<p style=\"margin-top: 20px;text-align: center;text-justify: inter-word; font-weight: 600;\">Report on Physical Locations of CA sites under the Controller of Certifying Authorities (CCA)")
	            .append("<table>")
	            .append("<thead><tr><th>Sl. No.</th><th>CA Name</th><th>Primary Site</th><th>DR Site</th><th>Registered Office</th><th>Other Site</th></tr></thead>")
	            .append("<tbody>");

	        int serialNumber = 1;
	        
	        for (CASiteLocationsDTO r : list) {
	            contentBuilder.append("<tr>")
	                .append("<td>").append(serialNumber++).append(".</td>")
	                .append("<td>").append(r.getCaName()).append("</td>")
	                .append("<td>").append(r.getPrimarySite()).append("</td>")
	                .append("<td>").append(r.getDrSite()).append("</td>")
	                .append("<td>").append(r.getRegisteredOffice()).append("</td>")
	                .append("<td>").append(r.getOtherSite()).append("</td>")
	                .append("</tr>");
	        }



	        
	        contentBuilder.append("</tbody></table></body></html>");

	        // Convert HTML to PDF
	        String htmlContent = contentBuilder.toString();
	        byte[] pdfBytes;
	        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
	            ConverterProperties converterProperties = new ConverterProperties();
	            HtmlConverter.convertToPdf(htmlContent, outputStream, converterProperties);
	            pdfBytes = outputStream.toByteArray();
	        }

	        // Return PDF response
	        return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=CASiteLocations.pdf")
	            .contentType(MediaType.APPLICATION_PDF)
	            .body(pdfBytes);

	    } catch (Exception e) {
	        
	        return new ResponseEntity<>("Unable to get report data.", HttpStatus.BAD_REQUEST);
	    }
	}
	
	
	
	//Helper Methods
	
	private String formatName(String name) {
	    if (name != null && !name.isEmpty()) {
	        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase() + " ";
	    }
	    return "";
	}
	
	private boolean isNullOrEmpty(String str) {
	    return str == null || str.isEmpty();
	}	
	
	
	private  String getStartDate(String month, String year) {
        int monthNumber = getMonthNumber(month); 
        int yearNumber = Integer.parseInt(year); 
        
        LocalDate startDate = LocalDate.of(yearNumber, monthNumber, 1);

        
        return startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private  String getEndDate(String month, String year) {
        int monthNumber = getMonthNumber(month); 
        int yearNumber = Integer.parseInt(year); 

        
        YearMonth yearMonth = YearMonth.of(yearNumber, monthNumber);
        LocalDate endDate = yearMonth.atEndOfMonth();

        
        return endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
    
    
    
    private int getMonthNumber(String monthName) {
	    Map<String, Integer> monthMap = Map.ofEntries(
	        Map.entry("January", 1),
	        Map.entry("February", 2),
	        Map.entry("March", 3),
	        Map.entry("April", 4),
	        Map.entry("May", 5),
	        Map.entry("June", 6),
	        Map.entry("July", 7),
	        Map.entry("August", 8),
	        Map.entry("September", 9),
	        Map.entry("October", 10),
	        Map.entry("November", 11),
	        Map.entry("December", 12)
	    );
	    return monthMap.getOrDefault(monthName, 0); 
	}

    private LocalDate convertToLocalDate(Date date) {
    	
    	if(date == null)
    		return null;
    	
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

	
}
