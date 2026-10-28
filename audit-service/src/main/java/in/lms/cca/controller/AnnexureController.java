package in.lms.cca.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.expression.ParseException;
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
import org.springframework.web.multipart.MultipartFile;

import in.lms.cca.dto.annexure.ASPDetailsDTO;
import in.lms.cca.dto.annexure.AnnualAuditPeriodDTO;
import in.lms.cca.dto.annexure.AnnualAuditPeriodDetailsDTO;
import in.lms.cca.dto.annexure.AuditorVerificationDTO;
import in.lms.cca.dto.annexure.CAServicesDetailsDTO;
import in.lms.cca.dto.annexure.CAServicesMainDTO;
import in.lms.cca.dto.annexure.CATrustedPersonDTO;
import in.lms.cca.dto.annexure.CATrustedPersonMainDTO;
import in.lms.cca.dto.annexure.CaSwWebDetailsDTO;
import in.lms.cca.dto.annexure.CaSwWebMainDTO;
import in.lms.cca.dto.annexure.CertificateCostDTO;
import in.lms.cca.dto.annexure.ConnectivityDetailsDTO;
import in.lms.cca.dto.annexure.ConnectivityMainDTO;
import in.lms.cca.dto.annexure.CourtCasesMainDTO;
import in.lms.cca.dto.annexure.CryptoTokenDetailsDTO;
import in.lms.cca.dto.annexure.CryptoTokenMainDTO;
import in.lms.cca.dto.annexure.DownTimeDTO;
import in.lms.cca.dto.annexure.EKYCAcMonthDetailsDTO;
import in.lms.cca.dto.annexure.EKYCAccountBasedMainDTO;
import in.lms.cca.dto.annexure.EKYCMonthMainDTO;
import in.lms.cca.dto.annexure.InternalAuditDetailsDTO;
import in.lms.cca.dto.annexure.InternalAuditMainDTO;
import in.lms.cca.dto.annexure.LocationDetailsDTO;
import in.lms.cca.dto.annexure.LocationMainDTO;
import in.lms.cca.dto.annexure.PublicInfoDetailsDTO;
import in.lms.cca.dto.annexure.PublicInfoMainDTO;
import in.lms.cca.dto.annexure.RaAuditMainDTO;
import in.lms.cca.dto.annexure.RevocationMainDTO;
import in.lms.cca.dto.annexure.SelfAssessmentMainDTO;
import in.lms.cca.entity.annexure.ASPDetails;
import in.lms.cca.entity.annexure.AnnexureMain;
import in.lms.cca.entity.annexure.AnnualAuditPeriodDetails;
import in.lms.cca.entity.annexure.AnnualAuditPeriodMain;
import in.lms.cca.entity.annexure.CAServicesDetails;
import in.lms.cca.entity.annexure.CAServicesMain;
import in.lms.cca.entity.annexure.CATrustedPerson;
import in.lms.cca.entity.annexure.CATrustedPersonMain;
import in.lms.cca.entity.annexure.CaSwWebDetails;
import in.lms.cca.entity.annexure.CaSwWebMain;
import in.lms.cca.entity.annexure.CertificateCost;
import in.lms.cca.entity.annexure.ConnectivityDetails;
import in.lms.cca.entity.annexure.ConnectivityMain;
import in.lms.cca.entity.annexure.CourtCasesMain;
import in.lms.cca.entity.annexure.CryptoTokenDetails;
import in.lms.cca.entity.annexure.CryptoTokenMain;
import in.lms.cca.entity.annexure.DownTime;
import in.lms.cca.entity.annexure.EKYCAcMonthDetails;
import in.lms.cca.entity.annexure.EKYCAccountBasedMain;
import in.lms.cca.entity.annexure.EKYCMonthMain;
import in.lms.cca.entity.annexure.InternalAuditDetails;
import in.lms.cca.entity.annexure.InternalAuditMain;
import in.lms.cca.entity.annexure.LocationDetails;
import in.lms.cca.entity.annexure.LocationMain;
import in.lms.cca.entity.annexure.PublicInfoDetails;
import in.lms.cca.entity.annexure.PublicInfoMain;
import in.lms.cca.entity.annexure.RaAuditMain;
import in.lms.cca.entity.annexure.RevocationMain;
import in.lms.cca.entity.annexure.SelfAssessmentMain;
import in.lms.cca.payload.EmployeeDetailsDTO;
import in.lms.cca.service.annexure.IASPDetailsService;
import in.lms.cca.service.annexure.IAnnexureMainService;
import in.lms.cca.service.annexure.IAnnualAuditPeriodDetailsService;
import in.lms.cca.service.annexure.IAnnualAuditPeriodMainService;
import in.lms.cca.service.annexure.ICAServicesDetailsService;
import in.lms.cca.service.annexure.ICAServicesMainService;
import in.lms.cca.service.annexure.ICATrustedPersonMainService;
import in.lms.cca.service.annexure.ICATrustedPersonService;
import in.lms.cca.service.annexure.ICaSwWebDetailsService;
import in.lms.cca.service.annexure.ICaSwWebMainService;
import in.lms.cca.service.annexure.ICertificateCostService;
import in.lms.cca.service.annexure.IConnectivityDetailsService;
import in.lms.cca.service.annexure.IConnectivityMainService;
import in.lms.cca.service.annexure.ICourtCasesMainService;
import in.lms.cca.service.annexure.ICryptoTokenDetailsService;
import in.lms.cca.service.annexure.ICryptoTokenMainService;
import in.lms.cca.service.annexure.IDownTimeService;
import in.lms.cca.service.annexure.IEKYCAcMonthDetailsService;
import in.lms.cca.service.annexure.IEKYCAccountBasedMainService;
import in.lms.cca.service.annexure.IEKYCMonthMainService;
import in.lms.cca.service.annexure.IInternalAuditDetailsService;
import in.lms.cca.service.annexure.IInternalAuditMainService;
import in.lms.cca.service.annexure.ILocationDetailsService;
import in.lms.cca.service.annexure.ILocationMainService;
import in.lms.cca.service.annexure.IPublicInfoDetailsService;
import in.lms.cca.service.annexure.IPublicInfoMainService;
import in.lms.cca.service.annexure.IRaAuditMainService;
import in.lms.cca.service.annexure.IRevocationMainService;
import in.lms.cca.service.annexure.ISelfAssessmentMainService;
import in.lms.cca.util.api.AnnexureAPIs;
import in.lms.cca.util.api.AuditServiceAPIs;
import in.lms.cca.util.global.Constant;
import in.lms.cca.util.global.EncryptionUtil;
import in.lms.cca.util.golbal.DocumentFileUtil;

@RestController
@CrossOrigin
@RequestMapping(AuditServiceAPIs.AUDIT_SERVICE_BASE_URL)
public class AnnexureController {

	@Autowired
	private IAnnualAuditPeriodMainService periodMainServ;

	@Autowired
	private IAnnualAuditPeriodDetailsService periodServ;

	@Autowired
	private IAnnexureMainService annexServ;

	@Autowired
	private IInternalAuditMainService internalMainServ;

	@Autowired
	private IInternalAuditDetailsService internalServ;

	@Autowired
	private IEKYCMonthMainService monthMainServ;

	@Autowired
	private IEKYCAcMonthDetailsService monthDetailsServ;

	@Autowired
	private IEKYCAccountBasedMainService accountBasedServ;

	@Autowired
	private IRaAuditMainService raAuditServ;

	@Autowired
	private ICourtCasesMainService courtCasesServ;

	@Autowired
	private IRevocationMainService revocationServ;

	@Autowired
	private ICryptoTokenMainService cryptoTokenMainServ;

	@Autowired
	private ICryptoTokenDetailsService cryptoTokenDetailsServ;

	@Autowired
	private ICaSwWebMainService caSwWebMainServ;

	@Autowired
	private ICaSwWebDetailsService caSwWebDetailsServ;

	@Autowired
	private ILocationMainService locationMainServ;

	@Autowired
	private ILocationDetailsService locationDetailsServ;

	@Autowired
	private ICAServicesMainService caServicesMainServ;

	@Autowired
	private ICAServicesDetailsService caServicesDetailsServ;

	@Autowired
	private IASPDetailsService aspDetailsServ;

	@Autowired
	private IPublicInfoMainService publicInfoMainServ;

	@Autowired
	private IPublicInfoDetailsService publicInfoDetailsServ;

	@Autowired
	private ICertificateCostService certificateCostServ;

	@Autowired
	private IDownTimeService downTimeServ;

	@Autowired
	private ISelfAssessmentMainService selfServ;

	@Autowired
	private IConnectivityDetailsService connectivityDetailsServ;

	@Autowired
	private IConnectivityMainService connectivityMainServ;

	@Autowired
	private ICATrustedPersonService personServ;

	@Autowired
	private ICATrustedPersonMainService personMainServ;

	// 1. Annual Audit Period Details

	// Add and Update
	@PostMapping(AnnexureAPIs.ADD_ANNUAL_AUDIT_PERIOD_DETAILS)
	public ResponseEntity<?> addAnnualAuditPeriodDetails(@RequestBody AnnualAuditPeriodDTO period) {

		try {

			List<AnnualAuditPeriodDetailsDTO> obj = period.getAuditPeriods();
			String userName = period.getUserName();

			// Validation (blank checks optional; format/length only when present)

			String OBSERVATION_LENGTH = "The length must be between 3 and 500 characters.";
			String OBSERVATION_FORMAT = "Only alphabets, numbers, characters ().,-& and spaces are allowed.";
			String OBSERVATION_WORDS = "Maximum 50 words are allowed.";
			String FROM_DATE_DIFF = "From Date must be less than to date.";
			String TO_DATE_DIFF = "To date must be greater than from date.";

			Pattern OBSERVATION_PATTERN = Pattern.compile("^[A-Za-z0-9().,&\\- ]+$");
			Pattern DESCRIPTION_PATTERN = Pattern.compile("^[A-Za-z0-9 ]+$");

			if (obj != null) {
			for (AnnualAuditPeriodDetailsDTO item : obj) {

				if (item.getDescription() != null && !item.getDescription().trim().isEmpty()) {
					if (item.getDescription().length() < 3 || item.getDescription().length() > 50) {
						return new ResponseEntity<>("Description length must be between 3 and 50 characters.",
								HttpStatus.BAD_REQUEST);
					} else if (!DESCRIPTION_PATTERN.matcher(item.getDescription()).matches()) {
						return new ResponseEntity<>("Only alphabets, numbers, characters ().,&- are allowed",
								HttpStatus.BAD_REQUEST);
					}
				}

				if (item.getObservations() != null && !item.getObservations().trim().isEmpty()) {
					if (countWords(item.getObservations()) > 50) {
						return new ResponseEntity<>(OBSERVATION_WORDS, HttpStatus.BAD_REQUEST);
					} else if (item.getObservations().length() < 3 || item.getObservations().length() > 500) {
						return new ResponseEntity<>(OBSERVATION_LENGTH, HttpStatus.BAD_REQUEST);
					} else if (!OBSERVATION_PATTERN.matcher(item.getObservations()).matches()) {
						return new ResponseEntity<>(OBSERVATION_FORMAT, HttpStatus.BAD_REQUEST);
					}
				}

				if (item.getFromDate() != null && item.getToDate() != null
						&& !item.getFromDate().before(item.getToDate())) {
					return new ResponseEntity<>(FROM_DATE_DIFF, HttpStatus.BAD_REQUEST);
				}

				if (item.getToDate() != null && item.getFromDate() != null
						&& !item.getFromDate().before(item.getToDate())) {
					return new ResponseEntity<>(TO_DATE_DIFF, HttpStatus.BAD_REQUEST);
				}
			}
			}

			// Save Annexure

			AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", EncryptionUtil.decrypt(userName));

			if (amain == null) {

				AnnexureMain sobj = new AnnexureMain();
				sobj.setCompleted(1);
				sobj.setCreatedBy(userName);
				sobj.setStatus("Active");
				sobj.setUpdatedBy(userName);
				sobj.setUserName(EncryptionUtil.decrypt(userName));

				AnnexureMain savedObj = annexServ.addAnnexureMain(sobj);

				amain = savedObj;
			}

			// Save annual audit period details

			AnnualAuditPeriodMain mainObj = periodMainServ.getByAnnexureId(amain.getAnnexureMainId());

			if (mainObj == null) {

				AnnualAuditPeriodMain mObj = new AnnualAuditPeriodMain();

				mObj.setAnnexureMainId(amain);
				AnnualAuditPeriodMain savedObj = periodMainServ.addAnnualAuditPeriodMain(mObj);

				mainObj = savedObj;
			}

			if (obj != null) {
				for (AnnualAuditPeriodDetailsDTO a : obj) {

					AnnualAuditPeriodDetails objPeriod = null;

					if (a.getPeriodDetailsId() != null && !a.getPeriodDetailsId().isEmpty()
							&& EncryptionUtil.decrypt(a.getPeriodDetailsId()) != null) {
						objPeriod = periodServ.getByAnnualAuditPeriodId(
								Long.parseLong(EncryptionUtil.decrypt(a.getPeriodDetailsId())));
					}

					if (objPeriod == null) {

						AnnualAuditPeriodDetails d = new AnnualAuditPeriodDetails();
						d.setDescription(a.getDescription());
						d.setFromDate(a.getFromDate());
						d.setObservations(a.getObservations());
						d.setPeriodMainId(mainObj);
						d.setToDate(a.getToDate());
						periodServ.addAnnualAuditPeriodDetails(d);

					} else {

						objPeriod.setDescription(a.getDescription());
						objPeriod.setFromDate(a.getFromDate());
						objPeriod.setObservations(a.getObservations());
						objPeriod.setPeriodMainId(mainObj);
						objPeriod.setToDate(a.getToDate());
						objPeriod.setUpdated(new Date());

						periodServ.addAnnualAuditPeriodDetails(objPeriod);

					}

				}
			}

			return new ResponseEntity<>("Your data has been successfully added.", HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error occured while saving the details, try again.", HttpStatus.BAD_REQUEST);

		}

	}

	@GetMapping(AnnexureAPIs.GET_ANNUAL_AUDIT_PERIOD_DETAILS)
	public ResponseEntity<?> getAnnualAuditPeriodDetails(@RequestParam("id") String username) {

		try {

			AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", EncryptionUtil.decrypt(username));
			AnnualAuditPeriodMain mainObj = periodMainServ.getByAnnexureId(amain.getAnnexureMainId());
			List<AnnualAuditPeriodDetails> list = periodServ.getByAnnualAuditPeriodMainId(mainObj.getPeriodMainId());

			return new ResponseEntity<>(list, HttpStatus.OK);

		} catch (Exception e) {

			return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);

		}

	}

	// 2. Add and Update
	@PostMapping(AnnexureAPIs.ADD_INTERNAL_AUDIT_DETAILS)
	public ResponseEntity<?> addInternalAuditDetails(@RequestBody InternalAuditMainDTO period) {

		try {

			List<InternalAuditDetailsDTO> obj = period.getInternalAudits();
			String userName = period.getUserName();

			// Validation (blank checks optional; format/length only when present)

			String OBSERVATION_LENGTH = "The length must be between 3 and 500 characters.";
			String OBSERVATION_FORMAT = "Only alphabets, numbers, characters ().,-& and spaces are allowed.";
			String OBSERVATION_WORDS = "Maximum 50 words are allowed.";
			String FROM_DATE_DIFF = "From Date must be less than to date.";
			String TO_DATE_DIFF = "To date must be greater than from date.";
			String AUDITOR_LENGTH = "The length must be between 3 and 64 characters.";
			String AUDITOR_FORMAT = "Only alphabets, numbers, characters ().,-& and spaces are allowed.";

			Pattern OBSERVATION_PATTERN = Pattern.compile("^[A-Za-z0-9().,&\\- ]+$");
			Pattern AUDITOR_PATTERN = Pattern.compile("^[A-Za-z0-9().,&\\- ]+$");
			Pattern DESCRIPTION_PATTERN = Pattern.compile("^[A-Za-z0-9 ]+$");

			if (obj != null) {
			for (InternalAuditDetailsDTO item : obj) {

				if (item.getDescription() != null && !item.getDescription().trim().isEmpty()) {
					if (item.getDescription().length() < 3 || item.getDescription().length() > 50) {
						return new ResponseEntity<>("Description length must be between 3 and 50 characters.",
								HttpStatus.BAD_REQUEST);
					} else if (!DESCRIPTION_PATTERN.matcher(item.getDescription()).matches()) {
						return new ResponseEntity<>("Only alphabets, numbers, characters ().,&- are allowed",
								HttpStatus.BAD_REQUEST);
					}
				}

				if (item.getObservations() != null && !item.getObservations().trim().isEmpty()) {
					if (countWords(item.getObservations()) > 50) {
						return new ResponseEntity<>(OBSERVATION_WORDS, HttpStatus.BAD_REQUEST);
					} else if (item.getObservations().length() < 3 || item.getObservations().length() > 500) {
						return new ResponseEntity<>(OBSERVATION_LENGTH, HttpStatus.BAD_REQUEST);
					} else if (!OBSERVATION_PATTERN.matcher(item.getObservations()).matches()) {
						return new ResponseEntity<>(OBSERVATION_FORMAT, HttpStatus.BAD_REQUEST);
					}
				}

				if (item.getFromDate() != null && item.getToDate() != null
						&& !item.getFromDate().before(item.getToDate())) {
					return new ResponseEntity<>(FROM_DATE_DIFF, HttpStatus.BAD_REQUEST);
				}

				if (item.getToDate() != null && item.getFromDate() != null
						&& !item.getFromDate().before(item.getToDate())) {
					return new ResponseEntity<>(TO_DATE_DIFF, HttpStatus.BAD_REQUEST);
				}

				if (item.getAuditorDetails() != null && !item.getAuditorDetails().trim().isEmpty()) {
					if (item.getAuditorDetails().length() < 3 || item.getAuditorDetails().length() > 64) {
						return new ResponseEntity<>(AUDITOR_LENGTH, HttpStatus.BAD_REQUEST);
					} else if (!AUDITOR_PATTERN.matcher(item.getAuditorDetails()).matches()) {
						return new ResponseEntity<>(AUDITOR_FORMAT, HttpStatus.BAD_REQUEST);
					}
				}

			}
			}

			// Save Annexure

			AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", EncryptionUtil.decrypt(userName));

			if (amain == null) {

				AnnexureMain sobj = new AnnexureMain();
				sobj.setCompleted(2);
				sobj.setCreatedBy(userName);
				sobj.setStatus("Active");
				sobj.setUpdatedBy(userName);
				sobj.setUserName(EncryptionUtil.decrypt(userName));

				amain = annexServ.addAnnexureMain(sobj);
			} else {

				if (amain.getCompleted() == null || amain.getCompleted() < 2) {
					amain.setCompleted(2);
				}
				amain.setUpdated(new Date());

				AnnexureMain savedObj = annexServ.addAnnexureMain(amain);

				amain = savedObj;
			}

			// Save internal audit details

			InternalAuditMain mainObj = internalMainServ.getByAnnexureId(amain.getAnnexureMainId());

			if (mainObj == null) {

				InternalAuditMain mObj = new InternalAuditMain();

				mObj.setAnnexureMainId(amain);
				InternalAuditMain savedObj = internalMainServ.addInternalAuditMain(mObj);

				mainObj = savedObj;
			}

			if (obj != null) {
				for (InternalAuditDetailsDTO a : obj) {

					InternalAuditDetails objPeriod = null;

					if (a.getInAuditDetailsId() != null && !a.getInAuditDetailsId().isEmpty()
							&& EncryptionUtil.decrypt(a.getInAuditDetailsId()) != null) {
						objPeriod = internalServ.getByInternalAuditDetailsId(
								Long.parseLong(EncryptionUtil.decrypt(a.getInAuditDetailsId())));
					}

					if (objPeriod == null) {

						InternalAuditDetails d = new InternalAuditDetails();
						d.setDescription(a.getDescription());
						d.setFromDate(a.getFromDate());
						d.setObservations(a.getObservations());
						d.setInAuditMainId(mainObj);
						d.setAuditorDetails(a.getAuditorDetails());
						d.setToDate(a.getToDate());
						internalServ.addInternalAuditDetails(d);

					} else {

						objPeriod.setDescription(a.getDescription());
						objPeriod.setFromDate(a.getFromDate());
						objPeriod.setObservations(a.getObservations());
						objPeriod.setInAuditMainId(mainObj);
						objPeriod.setToDate(a.getToDate());
						objPeriod.setUpdated(new Date());
						objPeriod.setAuditorDetails(a.getAuditorDetails());

						internalServ.addInternalAuditDetails(objPeriod);

					}

				}
			}

			return new ResponseEntity<>("Your data has been successfully added.", HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error occured while saving the details, try again.", HttpStatus.BAD_REQUEST);

		}

	}

	@GetMapping(AnnexureAPIs.GET_INTERNAL_AUDIT_DETAILS)
	public ResponseEntity<?> getInternalAuditDetails(@RequestParam("id") String username) {

		try {

			AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", EncryptionUtil.decrypt(username));
			InternalAuditMain mainObj = internalMainServ.getByAnnexureId(amain.getAnnexureMainId());
			List<InternalAuditDetails> list = internalServ.getByInternalAuditMainId(mainObj.getInAuditMainId());

			return new ResponseEntity<>(list, HttpStatus.OK);

		} catch (Exception e) {

			return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);

		}

	}

	// 3. Add and Update
	@PostMapping(AnnexureAPIs.ADD_EKYC_MONTH_DETAILS)
	public ResponseEntity<?> addEKYCAcMonthDetails(@RequestBody EKYCMonthMainDTO period) {

		try {

			List<EKYCAcMonthDetailsDTO> obj = period.getEkycMonthDetails();
			String userName = period.getUserName();

			// Validation (blank checks optional; format/length only when present)

			String OBSERVATION_LENGTH = "The length must be between 3 and 500 characters.";
			String OBSERVATION_FORMAT = "Only alphabets, numbers, characters ().,-& and spaces are allowed.";
			String FROM_DATE_DIFF = "From Date must be less than to date.";
			String TO_DATE_DIFF = "To date must be greater than from date.";
			String AUDITOR_LENGTH = "The length must be between 3 and 64 characters.";
			String AUDITOR_FORMAT = "Only alphabets, numbers, characters ().,-& and spaces are allowed.";

			Pattern OBSERVATION_PATTERN = Pattern.compile("^[A-Za-z0-9().,&\\- ]+$");
			Pattern AUDITOR_PATTERN = Pattern.compile("^[A-Za-z0-9().,&\\- ]+$");

			if (obj != null) {
			for (EKYCAcMonthDetailsDTO item : obj) {
				if (item.getObservations() != null && !item.getObservations().trim().isEmpty()) {
					if (item.getObservations().length() < 3 || item.getObservations().length() > 500) {
						return new ResponseEntity<>(OBSERVATION_LENGTH, HttpStatus.BAD_REQUEST);
					} else if (!OBSERVATION_PATTERN.matcher(item.getObservations()).matches()) {
						return new ResponseEntity<>(OBSERVATION_FORMAT, HttpStatus.BAD_REQUEST);
					}
				}

				if (item.getFromDate() != null && item.getToDate() != null
						&& !item.getFromDate().before(item.getToDate())) {
					return new ResponseEntity<>(FROM_DATE_DIFF, HttpStatus.BAD_REQUEST);
				}

				if (item.getToDate() != null && item.getFromDate() != null
						&& !item.getFromDate().before(item.getToDate())) {
					return new ResponseEntity<>(TO_DATE_DIFF, HttpStatus.BAD_REQUEST);
				}

				if (item.getAuditorDetails() != null && !item.getAuditorDetails().trim().isEmpty()) {
					if (item.getAuditorDetails().length() < 3 || item.getAuditorDetails().length() > 64) {
						return new ResponseEntity<>(AUDITOR_LENGTH, HttpStatus.BAD_REQUEST);
					} else if (!AUDITOR_PATTERN.matcher(item.getAuditorDetails()).matches()) {
						return new ResponseEntity<>(AUDITOR_FORMAT, HttpStatus.BAD_REQUEST);
					}
				}

			}
			}

			// Save Annexure

			AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", EncryptionUtil.decrypt(userName));

			if (amain == null) {

				AnnexureMain sobj = new AnnexureMain();
				sobj.setCompleted(3);
				sobj.setCreatedBy(userName);
				sobj.setStatus("Active");
				sobj.setUpdatedBy(userName);
				sobj.setUserName(EncryptionUtil.decrypt(userName));

				amain = annexServ.addAnnexureMain(sobj);
			} else {

				if (amain.getCompleted() == null || amain.getCompleted() < 3) {
					amain.setCompleted(3);
				}
				amain.setUpdated(new Date());

				AnnexureMain savedObj = annexServ.addAnnexureMain(amain);

				amain = savedObj;
			}

			// Save main

			EKYCMonthMain mainObj = monthMainServ.getByAnnexureId(amain.getAnnexureMainId());

			if (mainObj == null) {

				EKYCMonthMain mObj = new EKYCMonthMain();

				mObj.setAnnexureMainId(amain);
				EKYCMonthMain savedObj = monthMainServ.addEKYCMonthMainn(mObj);

				mainObj = savedObj;
			}

			if (obj != null) {
				for (EKYCAcMonthDetailsDTO a : obj) {

					EKYCAcMonthDetails objPeriod = null;

					if (a.geteKYCAcMonthId() != null && !a.geteKYCAcMonthId().isEmpty()
							&& EncryptionUtil.decrypt(a.geteKYCAcMonthId()) != null) {
						objPeriod = monthDetailsServ
								.getByeKYCAcMonthId(Long.parseLong(EncryptionUtil.decrypt(a.geteKYCAcMonthId())));
					}

					if (objPeriod == null) {

						EKYCAcMonthDetails d = new EKYCAcMonthDetails();
						d.setMonth(a.getMonth());
						d.setFromDate(a.getFromDate());
						d.setObservations(a.getObservations());
						d.seteKYCMonthMainId(mainObj);
						d.setAuditorDetails(a.getAuditorDetails());
						d.setToDate(a.getToDate());
						monthDetailsServ.addEKYCAcMonthDetails(d);

					} else {

						objPeriod.setMonth(a.getMonth());
						objPeriod.setFromDate(a.getFromDate());
						objPeriod.setObservations(a.getObservations());
						objPeriod.seteKYCMonthMainId(mainObj);
						objPeriod.setToDate(a.getToDate());
						objPeriod.setUpdated(new Date());
						objPeriod.setAuditorDetails(a.getAuditorDetails());

						monthDetailsServ.addEKYCAcMonthDetails(objPeriod);

					}

				}
			}

			boolean isUpdate = obj != null
					&& obj.stream().anyMatch(a -> a.geteKYCAcMonthId() != null && !a.geteKYCAcMonthId().isEmpty());
			return new ResponseEntity<>(
					isUpdate ? "Your data has been successfully updated." : "Your data has been successfully added.",
					HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error occured while saving the details, try again.", HttpStatus.BAD_REQUEST);

		}

	}

	@GetMapping(AnnexureAPIs.GET_EKYC_MONTH_DETAILS)
	public ResponseEntity<?> getEKYCAcMonthDetails(@RequestParam("id") String username) {

		try {

			AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", EncryptionUtil.decrypt(username));
			EKYCMonthMain mainObj = monthMainServ.getByAnnexureId(amain.getAnnexureMainId());
			List<EKYCAcMonthDetails> list = monthDetailsServ.getByEKYCAcMonthMainId(mainObj.geteKYCMonthMainId());

			return new ResponseEntity<>(list, HttpStatus.OK);

		} catch (Exception e) {

			return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);

		}

	}

	// 4. Add and Update
	@PostMapping(AnnexureAPIs.ADD_ACCOUNT_BASED_DETAILS)
	public ResponseEntity<?> addAccountBasedDetails(@RequestBody EKYCAccountBasedMainDTO obj) {

		try {

			String userName = obj.getUserName();

			// Validation (blank checks optional; format/length only when present)

			String DSC_COUNT_FORMAT = "Only digits are allowed.";
			String DSC_COUNT_LENGTH = "The length must not exceed 9 digits.";

			String BANK_NAME_LENGTH = "The length must be between 3 and 200 characters.";
			String BANK_NAME_FORMAT = "Only alphabets, numbers, characters .-& and spaces are allowed.";

			String EXT_SERVICE_LENGTH = "The length must be between 3 and 200 characters.";
			String EXT_SERVICE_FORMAT = "Only alphabets, numbers, characters ().,-& and spaces are allowed.";

			Pattern DSC_COUNT_PATTERN = Pattern.compile("^[0-9]+$"); // 1 to 9 digits
			Pattern BANK_NAME_PATTERN = Pattern.compile("^[A-Za-z0-9.&\\- ]+$");
			Pattern EXT_SERVICE_PATTERN = Pattern.compile("^[A-Za-z0-9().,&\\- ]+$");

			// DSC Counts Validation for Offline Aadhaar
			if (obj.getOfflineAadhaarDSCCount() != null && !obj.getOfflineAadhaarDSCCount().toString().trim().isEmpty()) {
				if (obj.getOfflineAadhaarDSCCount().toString().length() > 9) {
					return new ResponseEntity<>(DSC_COUNT_LENGTH, HttpStatus.BAD_REQUEST);
				} else if (!DSC_COUNT_PATTERN.matcher(obj.getOfflineAadhaarDSCCount().toString()).matches()) {
					return new ResponseEntity<>(DSC_COUNT_FORMAT, HttpStatus.BAD_REQUEST);
				}
			}

			// DSC Counts Validation for Online Aadhaar
			if (obj.getOnlineAadharDSCCount() != null && !obj.getOnlineAadharDSCCount().toString().trim().isEmpty()) {
				if (obj.getOnlineAadharDSCCount().toString().length() > 9) {
					return new ResponseEntity<>(DSC_COUNT_LENGTH, HttpStatus.BAD_REQUEST);
				} else if (!DSC_COUNT_PATTERN.matcher(obj.getOnlineAadharDSCCount().toString()).matches()) {
					return new ResponseEntity<>(DSC_COUNT_FORMAT, HttpStatus.BAD_REQUEST);
				}
			}

			// DSC Counts Validation for PAN eKYC
			if (obj.getPanDSCCount() != null && !obj.getPanDSCCount().toString().trim().isEmpty()) {
				if (obj.getPanDSCCount().toString().length() > 9) {
					return new ResponseEntity<>(DSC_COUNT_LENGTH, HttpStatus.BAD_REQUEST);
				} else if (!DSC_COUNT_PATTERN.matcher(obj.getPanDSCCount().toString()).matches()) {
					return new ResponseEntity<>(DSC_COUNT_FORMAT, HttpStatus.BAD_REQUEST);
				}
			}

			// DSC Counts Validation for CA eKYC
			if (obj.getCaDSCCount() != null && !obj.getCaDSCCount().toString().trim().isEmpty()) {
				if (obj.getCaDSCCount().toString().length() > 9) {
					return new ResponseEntity<>(DSC_COUNT_LENGTH, HttpStatus.BAD_REQUEST);
				} else if (!DSC_COUNT_PATTERN.matcher(obj.getCaDSCCount().toString()).matches()) {
					return new ResponseEntity<>(DSC_COUNT_FORMAT, HttpStatus.BAD_REQUEST);
				}
			}

			// DSC Counts Validation for Organisational eKYC
			if (obj.getOrganisationalDSCCount() != null
					&& !obj.getOrganisationalDSCCount().toString().trim().isEmpty()) {
				if (obj.getOrganisationalDSCCount().toString().length() > 9) {
					return new ResponseEntity<>(DSC_COUNT_LENGTH, HttpStatus.BAD_REQUEST);
				} else if (!DSC_COUNT_PATTERN.matcher(obj.getOrganisationalDSCCount().toString()).matches()) {
					return new ResponseEntity<>(DSC_COUNT_FORMAT, HttpStatus.BAD_REQUEST);
				}
			}

			// DSC Counts Validation for Banking eKYC
			if (obj.getBankingDSCCount() != null && !obj.getBankingDSCCount().toString().trim().isEmpty()) {
				if (obj.getBankingDSCCount().toString().length() > 9) {
					return new ResponseEntity<>(DSC_COUNT_LENGTH, HttpStatus.BAD_REQUEST);
				} else if (!DSC_COUNT_PATTERN.matcher(obj.getBankingDSCCount().toString()).matches()) {
					return new ResponseEntity<>(DSC_COUNT_FORMAT, HttpStatus.BAD_REQUEST);
				}
			}

			// Bank Name Validation
			if (obj.getNameOfBanks() != null && !obj.getNameOfBanks().trim().isEmpty()) {
				if (obj.getNameOfBanks().length() < 3 || obj.getNameOfBanks().length() > 200) {
					return new ResponseEntity<>(BANK_NAME_LENGTH, HttpStatus.BAD_REQUEST);
				} else if (!BANK_NAME_PATTERN.matcher(obj.getNameOfBanks()).matches()) {
					return new ResponseEntity<>(BANK_NAME_FORMAT, HttpStatus.BAD_REQUEST);
				}
			}

			// External Service Validation
			if (obj.getExtServOffAadhar() != null && !obj.getExtServOffAadhar().trim().isEmpty()) {
				if (obj.getExtServOffAadhar().length() < 3 || obj.getExtServOffAadhar().length() > 200) {
					return new ResponseEntity<>(EXT_SERVICE_LENGTH, HttpStatus.BAD_REQUEST);
				} else if (!EXT_SERVICE_PATTERN.matcher(obj.getExtServOffAadhar()).matches()) {
					return new ResponseEntity<>(EXT_SERVICE_FORMAT, HttpStatus.BAD_REQUEST);
				}
			}

			if (obj.getExtServOnAadhar() != null && !obj.getExtServOnAadhar().trim().isEmpty()) {
				if (obj.getExtServOnAadhar().length() < 3 || obj.getExtServOnAadhar().length() > 200) {
					return new ResponseEntity<>(EXT_SERVICE_LENGTH, HttpStatus.BAD_REQUEST);
				} else if (!EXT_SERVICE_PATTERN.matcher(obj.getExtServOnAadhar()).matches()) {
					return new ResponseEntity<>(EXT_SERVICE_FORMAT, HttpStatus.BAD_REQUEST);
				}
			}

			if (obj.getExtServPAN() != null && !obj.getExtServPAN().trim().isEmpty()) {
				if (obj.getExtServPAN().length() < 3 || obj.getExtServPAN().length() > 200) {
					return new ResponseEntity<>(EXT_SERVICE_LENGTH, HttpStatus.BAD_REQUEST);
				} else if (!EXT_SERVICE_PATTERN.matcher(obj.getExtServPAN()).matches()) {
					return new ResponseEntity<>(EXT_SERVICE_FORMAT, HttpStatus.BAD_REQUEST);
				}
			}

			if (obj.getExtServCA() != null && !obj.getExtServCA().trim().isEmpty()) {
				if (obj.getExtServCA().length() < 3 || obj.getExtServCA().length() > 200) {
					return new ResponseEntity<>(EXT_SERVICE_LENGTH, HttpStatus.BAD_REQUEST);
				} else if (!EXT_SERVICE_PATTERN.matcher(obj.getExtServCA()).matches()) {
					return new ResponseEntity<>(EXT_SERVICE_FORMAT, HttpStatus.BAD_REQUEST);
				}
			}

			if (obj.getExtServOrg() != null && !obj.getExtServOrg().trim().isEmpty()) {
				if (obj.getExtServOrg().length() < 3 || obj.getExtServOrg().length() > 200) {
					return new ResponseEntity<>(EXT_SERVICE_LENGTH, HttpStatus.BAD_REQUEST);
				} else if (!EXT_SERVICE_PATTERN.matcher(obj.getExtServOrg()).matches()) {
					return new ResponseEntity<>(EXT_SERVICE_FORMAT, HttpStatus.BAD_REQUEST);
				}
			}

			if (obj.getExtServBanking() != null && !obj.getExtServBanking().trim().isEmpty()) {
				if (obj.getExtServBanking().length() < 3 || obj.getExtServBanking().length() > 200) {
					return new ResponseEntity<>(EXT_SERVICE_LENGTH, HttpStatus.BAD_REQUEST);
				} else if (!EXT_SERVICE_PATTERN.matcher(obj.getExtServBanking()).matches()) {
					return new ResponseEntity<>(EXT_SERVICE_FORMAT, HttpStatus.BAD_REQUEST);
				}
			}

			// Save Annexure

			AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", EncryptionUtil.decrypt(userName));

			if (amain == null) {

				AnnexureMain sobj = new AnnexureMain();
				sobj.setCompleted(4);
				sobj.setCreatedBy(userName);
				sobj.setStatus("Active");
				sobj.setUpdatedBy(userName);
				sobj.setUserName(EncryptionUtil.decrypt(userName));

				amain = annexServ.addAnnexureMain(sobj);
			} else {

				if (amain.getCompleted() == null || amain.getCompleted() < 4) {
					amain.setCompleted(4);
				}
				amain.setUpdated(new Date());

				AnnexureMain savedObj = annexServ.addAnnexureMain(amain);

				amain = savedObj;
			}

			// Details

			//
			EKYCAccountBasedMain objAcBased = null;

			if (obj.getEkycAcMainId() != null && !obj.getEkycAcMainId().isEmpty()) {
				try {
					String decryptedId = EncryptionUtil.decrypt(obj.getEkycAcMainId()).replace("\"", "");
					if (decryptedId != null && !decryptedId.isEmpty()) {
						objAcBased = accountBasedServ.getByeKYCAcMainId(Long.parseLong(decryptedId));
					}
				} catch (Exception ignored) {
					// Fall through to annexure lookup
				}
			}

			if (objAcBased == null) {
				objAcBased = accountBasedServ.getByAnnexureId(amain.getAnnexureMainId());
			}

			boolean isUpdate = objAcBased != null;

			if (objAcBased == null) {

				EKYCAccountBasedMain d = new EKYCAccountBasedMain();
				d.setAnnexureMainId(amain);
				d.setApprovalDateBanking(obj.getApprovalDateBanking());
				d.setApprovalDateCA(obj.getApprovalDateCA());
				d.setApprovalDateOfflineAadhaar(obj.getApprovalDateOfflineAadhaar());
				d.setApprovalDateOnlineAadhar(obj.getApprovalDateOnlineAadhar());
				d.setApprovalDateOrganisational(obj.getApprovalDateOrganisational());
				d.setApprovalDatePAN(obj.getApprovalDatePAN());
				d.setBankingDSCCount(parseLongOrNull(obj.getBankingDSCCount()));
				d.setCaDSCCount(parseLongOrNull(obj.getCaDSCCount()));
				d.setExtServBanking(obj.getExtServBanking());
				d.setExtServCA(obj.getExtServCA());
				d.setExtServOffAadhar(obj.getExtServOffAadhar());
				d.setExtServOnAadhar(obj.getExtServOnAadhar());
				d.setExtServOrg(obj.getExtServOrg());
				d.setExtServPAN(obj.getExtServPAN());
				d.setGstServiceDetails(obj.getGstServiceDetails());
				d.setKuaLicenseDate(obj.getKuaLicenseDate());
				d.setNameOfBanks(obj.getNameOfBanks());
				d.setOfflineAadhaarDSCCount(parseLongOrNull(obj.getOfflineAadhaarDSCCount()));
				d.setOnlineAadharDSCCount(parseLongOrNull(obj.getOnlineAadharDSCCount()));
				d.setOrganisationalDSCCount(parseLongOrNull(obj.getOrganisationalDSCCount()));
				d.setPanDSCCount(parseLongOrNull(obj.getPanDSCCount()));
				d.setPanServDetails(obj.getPanServDetails());

				accountBasedServ.addeKYCAcMain(d);

			} else {

				objAcBased.setUpdated(new Date());

				objAcBased.setApprovalDateBanking(obj.getApprovalDateBanking());
				objAcBased.setApprovalDateCA(obj.getApprovalDateCA());
				objAcBased.setApprovalDateOfflineAadhaar(obj.getApprovalDateOfflineAadhaar());
				objAcBased.setApprovalDateOnlineAadhar(obj.getApprovalDateOnlineAadhar());
				objAcBased.setApprovalDateOrganisational(obj.getApprovalDateOrganisational());
				objAcBased.setApprovalDatePAN(obj.getApprovalDatePAN());
				objAcBased.setBankingDSCCount(parseLongOrNull(obj.getBankingDSCCount()));
				objAcBased.setCaDSCCount(parseLongOrNull(obj.getCaDSCCount()));
				objAcBased.setExtServBanking(obj.getExtServBanking());
				objAcBased.setExtServCA(obj.getExtServCA());
				objAcBased.setExtServOffAadhar(obj.getExtServOffAadhar());
				objAcBased.setExtServOnAadhar(obj.getExtServOnAadhar());
				objAcBased.setExtServOrg(obj.getExtServOrg());
				objAcBased.setExtServPAN(obj.getExtServPAN());
				objAcBased.setGstServiceDetails(obj.getGstServiceDetails());
				objAcBased.setKuaLicenseDate(obj.getKuaLicenseDate());
				objAcBased.setNameOfBanks(obj.getNameOfBanks());
				objAcBased.setOfflineAadhaarDSCCount(parseLongOrNull(obj.getOfflineAadhaarDSCCount()));
				objAcBased.setOnlineAadharDSCCount(parseLongOrNull(obj.getOnlineAadharDSCCount()));
				objAcBased.setOrganisationalDSCCount(parseLongOrNull(obj.getOrganisationalDSCCount()));
				objAcBased.setPanDSCCount(parseLongOrNull(obj.getPanDSCCount()));
				objAcBased.setPanServDetails(obj.getPanServDetails());

				accountBasedServ.addeKYCAcMain(objAcBased);

			}

			return new ResponseEntity<>(
					isUpdate ? "Your data has been successfully updated." : "Your data has been successfully added.",
					HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error occured while saving the details, try again.", HttpStatus.BAD_REQUEST);

		}

	}

	@GetMapping(AnnexureAPIs.GET_ACCOUNT_BASED_DETAILS)
	public ResponseEntity<?> getAccountBasedDetails(@RequestParam("id") String username) {

		try {
			String decryptedUser = EncryptionUtil.decrypt(username).replace("\"", "");
			AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", decryptedUser);
			if (amain == null) {
				return new ResponseEntity<>(null, HttpStatus.OK);
			}
			EKYCAccountBasedMain mainObj = accountBasedServ.getByAnnexureId(amain.getAnnexureMainId());
			return new ResponseEntity<>(mainObj, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.OK);
		}

	}

	// 5. Add and Update
	@PostMapping(AnnexureAPIs.ADD_RA_AUDIT)
	public ResponseEntity<?> addRAAudit(@RequestBody RaAuditMainDTO obj) {

		try {

			String userName = obj.getUserName();

			Pattern DIGITS_PATTERN = Pattern.compile("^[0-9]+$"); // 1 to 9 digits
			Pattern STRING_PATTERN = Pattern.compile("^[A-Za-z0-9.,\\- ]+$");

			// Validation (blank checks optional; format/length only when present)
			if (obj.getTotalRA() != null && !obj.getTotalRA().trim().isEmpty()) {
				if (!DIGITS_PATTERN.matcher(obj.getTotalRA()).matches()) {
					return new ResponseEntity<>("Only alphabets are allowed", HttpStatus.BAD_REQUEST);
				} else if (obj.getTotalRA().length() > 9) {
					return new ResponseEntity<>("Total RA cannot exceed 9 digits", HttpStatus.BAD_REQUEST);
				}
			}

			if (obj.getActiveRA() != null && !obj.getActiveRA().trim().isEmpty()) {
				if (!DIGITS_PATTERN.matcher(obj.getActiveRA()).matches()) {
					return new ResponseEntity<>("Only alphabets are allowed", HttpStatus.BAD_REQUEST);
				} else if (obj.getActiveRA().length() > 9) {
					return new ResponseEntity<>("Active RA cannot exceed 9 digits", HttpStatus.BAD_REQUEST);
				}
			}

			if (obj.getDatesOfRAAudit() != null && !obj.getDatesOfRAAudit().trim().isEmpty()) {
				if (obj.getDatesOfRAAudit().length() < 3 || obj.getDatesOfRAAudit().length() > 200) {
					return new ResponseEntity<>("The length must be between 3 and 200 characters.", HttpStatus.BAD_REQUEST);
				} else if (!STRING_PATTERN.matcher(obj.getDatesOfRAAudit()).matches()) {
					return new ResponseEntity<>("Only alphabets, numbers, characters .,- are allowed",
							HttpStatus.BAD_REQUEST);
				}
			}

			if (obj.getNcReportedByRA() != null && !obj.getNcReportedByRA().trim().isEmpty()) {
				if (obj.getNcReportedByRA().length() < 3 || obj.getNcReportedByRA().length() > 200) {
					return new ResponseEntity<>("The length must be between 3 and 200 characters.", HttpStatus.BAD_REQUEST);
				} else if (!STRING_PATTERN.matcher(obj.getNcReportedByRA()).matches()) {
					return new ResponseEntity<>("Only alphabets, numbers, characters .,- are allowed",
							HttpStatus.BAD_REQUEST);
				}
			}

			if (obj.getCaActionTaken() != null && !obj.getCaActionTaken().trim().isEmpty()) {
				if (obj.getCaActionTaken().length() < 3 || obj.getCaActionTaken().length() > 200) {
					return new ResponseEntity<>("The length must be between 3 and 200 characters.", HttpStatus.BAD_REQUEST);
				} else if (!STRING_PATTERN.matcher(obj.getCaActionTaken()).matches()) {
					return new ResponseEntity<>("Only alphabets, numbers, characters .,- are allowed",
							HttpStatus.BAD_REQUEST);
				}
			}

			// Save Annexure

			AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", EncryptionUtil.decrypt(userName));

			if (amain == null) {

				AnnexureMain sobj = new AnnexureMain();
				sobj.setCompleted(5);
				sobj.setCreatedBy(userName);
				sobj.setStatus("Active");
				sobj.setUpdatedBy(userName);
				sobj.setUserName(EncryptionUtil.decrypt(userName));

				amain = annexServ.addAnnexureMain(sobj);
			} else {

				if (amain.getCompleted() == null || amain.getCompleted() < 5) {
					amain.setCompleted(5);
				}
				amain.setUpdated(new Date());

				AnnexureMain savedObj = annexServ.addAnnexureMain(amain);

				amain = savedObj;
			}

			// Details

			//
			RaAuditMain objRaAuditMain = null;

			if (obj.getRaAuditMainId() != null && !obj.getRaAuditMainId().isEmpty()
					&& EncryptionUtil.decrypt(obj.getRaAuditMainId()) != null) {
				objRaAuditMain = raAuditServ
						.getByRaAuditMainId(Long.parseLong(EncryptionUtil.decrypt(obj.getRaAuditMainId())));
			}

			if (objRaAuditMain == null) {

				RaAuditMain d = new RaAuditMain();
				d.setActiveRA(parseLongOrNull(obj.getActiveRA()));
				d.setAnnexureMainId(amain);
				d.setCaActionTaken(obj.getCaActionTaken());
				d.setDatesOfRAAudit(obj.getDatesOfRAAudit());
				d.setNcReportedByRA(obj.getNcReportedByRA());
				d.setTotalRA(parseLongOrNull(obj.getTotalRA()));

				raAuditServ.addRaAuditMain(d);

			} else {

				objRaAuditMain.setUpdated(new Date());
				objRaAuditMain.setActiveRA(parseLongOrNull(obj.getActiveRA()));
				objRaAuditMain.setAnnexureMainId(amain);
				objRaAuditMain.setCaActionTaken(obj.getCaActionTaken());
				objRaAuditMain.setDatesOfRAAudit(obj.getDatesOfRAAudit());
				objRaAuditMain.setNcReportedByRA(obj.getNcReportedByRA());
				objRaAuditMain.setTotalRA(parseLongOrNull(obj.getTotalRA()));

				raAuditServ.addRaAuditMain(objRaAuditMain);

			}

			return new ResponseEntity<>("Your data has been successfully added.", HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error occured while saving the details, try again.", HttpStatus.BAD_REQUEST);

		}

	}

	@GetMapping(AnnexureAPIs.GET_RA_AUDIT)
	public ResponseEntity<?> getRAAudit(@RequestParam("id") String username) {

		try {

			AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", EncryptionUtil.decrypt(username));
			RaAuditMain mainObj = raAuditServ.getByAnnexureId(amain.getAnnexureMainId());

			return new ResponseEntity<>(mainObj, HttpStatus.OK);

		} catch (Exception e) {

			return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);

		}

	}

	// 6. Add and Update
	@PostMapping(AnnexureAPIs.ADD_COURT_CASES)
	public ResponseEntity<?> addCourtCases(@RequestBody CourtCasesMainDTO obj) {

		try {

			String userName = obj.getUserName();

			Pattern STRING_PATTERN = Pattern.compile("^[A-Za-z0-9().,&\\- ]+$");

			// Validation (blank checks optional; format/length only when present)

			if (obj.getActiveCourtCases() != null && !obj.getActiveCourtCases().trim().isEmpty()) {
				if (obj.getActiveCourtCases().length() < 3 || obj.getActiveCourtCases().length() > 200) {
					return new ResponseEntity<>("The length must be between 3 and 200 characters.", HttpStatus.BAD_REQUEST);
				} else if (!STRING_PATTERN.matcher(obj.getActiveCourtCases()).matches()) {
					return new ResponseEntity<>("Only alphabets, numbers, characters ().,&- are allowed",
							HttpStatus.BAD_REQUEST);
				}
			}

			if (obj.getCourtCasesCount() != null && !obj.getCourtCasesCount().trim().isEmpty()) {
				if (obj.getCourtCasesCount().length() < 3 || obj.getCourtCasesCount().length() > 200) {
					return new ResponseEntity<>("The length must be between 3 and 200 characters.", HttpStatus.BAD_REQUEST);
				} else if (!STRING_PATTERN.matcher(obj.getCourtCasesCount()).matches()) {
					return new ResponseEntity<>("Only alphabets, numbers, characters ().,&- are allowed",
							HttpStatus.BAD_REQUEST);
				}
			}

			if (obj.getPoliceComplaintsCount() != null && !obj.getPoliceComplaintsCount().trim().isEmpty()) {
				if (obj.getPoliceComplaintsCount().length() < 3 || obj.getPoliceComplaintsCount().length() > 200) {
					return new ResponseEntity<>("The length must be between 3 and 200 characters.", HttpStatus.BAD_REQUEST);
				} else if (!STRING_PATTERN.matcher(obj.getPoliceComplaintsCount()).matches()) {
					return new ResponseEntity<>("Only alphabets, numbers, characters ().,&- are allowed",
							HttpStatus.BAD_REQUEST);
				}
			}

			// Save Annexure

			AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", EncryptionUtil.decrypt(userName));

			if (amain == null) {

				AnnexureMain sobj = new AnnexureMain();
				sobj.setCompleted(6);
				sobj.setCreatedBy(userName);
				sobj.setStatus("Active");
				sobj.setUpdatedBy(userName);
				sobj.setUserName(EncryptionUtil.decrypt(userName));

				amain = annexServ.addAnnexureMain(sobj);
			} else {

				if (amain.getCompleted() == null || amain.getCompleted() < 6) {
					amain.setCompleted(6);
				}
				amain.setUpdated(new Date());

				AnnexureMain savedObj = annexServ.addAnnexureMain(amain);

				amain = savedObj;
			}

			// Details

			//
			CourtCasesMain objCourtCasesMain = null;

			if (obj.getCourtCasesMainId() != null && !obj.getCourtCasesMainId().isEmpty()
					&& EncryptionUtil.decrypt(obj.getCourtCasesMainId()) != null) {
				objCourtCasesMain = courtCasesServ
						.getByCourtCasesMainId(Long.parseLong(EncryptionUtil.decrypt(obj.getCourtCasesMainId())));
			}

			if (objCourtCasesMain == null) {

				CourtCasesMain d = new CourtCasesMain();
				d.setActiveCourtCases(obj.getActiveCourtCases());
				d.setAnnexureMainId(amain);
				d.setCourtCasesCount(obj.getCourtCasesCount());
				d.setPoliceComplaintsCount(obj.getPoliceComplaintsCount());

				courtCasesServ.addCourtCasesMain(d);

			} else {

				objCourtCasesMain.setUpdated(new Date());
				objCourtCasesMain.setActiveCourtCases(obj.getActiveCourtCases());
				objCourtCasesMain.setAnnexureMainId(amain);
				objCourtCasesMain.setCourtCasesCount(obj.getCourtCasesCount());
				objCourtCasesMain.setPoliceComplaintsCount(obj.getPoliceComplaintsCount());

				courtCasesServ.addCourtCasesMain(objCourtCasesMain);

			}

			return new ResponseEntity<>("Your data has been successfully added.", HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error occured while saving the details, try again.", HttpStatus.BAD_REQUEST);

		}

	}

	@GetMapping(AnnexureAPIs.GET_COURT_CASES)
	public ResponseEntity<?> getCourtCases(@RequestParam("id") String username) {

		try {

			AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", EncryptionUtil.decrypt(username));
			CourtCasesMain mainObj = courtCasesServ.getByAnnexureId(amain.getAnnexureMainId());

			return new ResponseEntity<>(mainObj, HttpStatus.OK);

		} catch (Exception e) {

			return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);

		}

	}

	// 7

	@PostMapping(AnnexureAPIs.ADD_REVOCATION_DETAILS)
	public ResponseEntity<?> addRevocationDetails(@RequestBody RevocationMainDTO obj) {

		try {

			String userName = obj.getUserName();

			Pattern STRING_PATTERN = Pattern.compile("^[A-Za-z0-9().,&\\- ]+$");
			Pattern DIGITS_PATTERN = Pattern.compile("^[0-9]+$"); // 1 to 9 digits

			// Validation (blank checks optional; format/length only when present)

			if (obj.getDscRevokedCount() != null && !obj.getDscRevokedCount().trim().isEmpty()) {
				if (obj.getDscRevokedCount().length() > 9) {
					return new ResponseEntity<>("Maximum 9 digits are allowed.", HttpStatus.BAD_REQUEST);
				} else if (!DIGITS_PATTERN.matcher(obj.getDscRevokedCount()).matches()) {
					return new ResponseEntity<>("Only digits are allowed.", HttpStatus.BAD_REQUEST);
				}
			}

			if (obj.getRevocReqReason() != null && !obj.getRevocReqReason().trim().isEmpty()) {
				if (obj.getRevocReqReason().length() < 3 || obj.getRevocReqReason().length() > 200) {
					return new ResponseEntity<>("The length must be between 3 and 200 characters.", HttpStatus.BAD_REQUEST);
				} else if (!STRING_PATTERN.matcher(obj.getRevocReqReason()).matches()) {
					return new ResponseEntity<>("Only alphabets, numbers, characters ().,&- are allowed",
							HttpStatus.BAD_REQUEST);
				}
			}

			if (obj.getDscRevokedReason() != null && !obj.getDscRevokedReason().trim().isEmpty()) {
				if (obj.getDscRevokedReason().length() < 3 || obj.getDscRevokedReason().length() > 200) {
					return new ResponseEntity<>("The length must be between 3 and 200 characters.", HttpStatus.BAD_REQUEST);
				} else if (!STRING_PATTERN.matcher(obj.getDscRevokedReason()).matches()) {
					return new ResponseEntity<>("Only alphabets, numbers, characters ().,&- are allowed",
							HttpStatus.BAD_REQUEST);
				}
			}

			// Save Annexure

			AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", EncryptionUtil.decrypt(userName));

			if (amain == null) {

				AnnexureMain sobj = new AnnexureMain();
				sobj.setCompleted(7);
				sobj.setCreatedBy(userName);
				sobj.setStatus("Active");
				sobj.setUpdatedBy(userName);
				sobj.setUserName(EncryptionUtil.decrypt(userName));

				amain = annexServ.addAnnexureMain(sobj);
			} else {

				if (amain.getCompleted() == null || amain.getCompleted() < 7) {
					amain.setCompleted(7);
				}
				amain.setUpdated(new Date());

				AnnexureMain savedObj = annexServ.addAnnexureMain(amain);

				amain = savedObj;
			}

			// Details

			//
			RevocationMain revocationMainObj = null;

			if (obj.getRevocationMainId() != null && !obj.getRevocationMainId().isEmpty()
					&& EncryptionUtil.decrypt(obj.getRevocationMainId()) != null) {
				revocationMainObj = revocationServ
						.getByRevocationMainId(Long.parseLong(EncryptionUtil.decrypt(obj.getRevocationMainId())));
			}

			if (revocationMainObj == null) {

				RevocationMain d = new RevocationMain();
				d.setDscRevokedCount(parseLongOrNull(obj.getDscRevokedCount()));
				d.setDscRevokedReason(obj.getDscRevokedReason());
				d.setRevocReqReason(obj.getRevocReqReason());
				d.setAnnexureMainId(amain);

				revocationServ.addRevocationMain(d);

			} else {

				revocationMainObj.setUpdated(new Date());
				revocationMainObj.setDscRevokedCount(parseLongOrNull(obj.getDscRevokedCount()));
				revocationMainObj.setDscRevokedReason(obj.getDscRevokedReason());
				revocationMainObj.setRevocReqReason(obj.getRevocReqReason());

				revocationServ.addRevocationMain(revocationMainObj);

			}

			return new ResponseEntity<>("Your data has been successfully added.", HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error occured while saving the details, try again.", HttpStatus.BAD_REQUEST);

		}

	}

	@GetMapping(AnnexureAPIs.GET_REVOCATION_DETAILS)
	public ResponseEntity<?> getRevocationDetails(@RequestParam("id") String username) {

		try {

			AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", EncryptionUtil.decrypt(username));
			RevocationMain mainObj = revocationServ.getByAnnexureId(amain.getAnnexureMainId());

			return new ResponseEntity<>(mainObj, HttpStatus.OK);

		} catch (Exception e) {

			return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);

		}

	}

//8

	// Add and Update
	@PostMapping(AnnexureAPIs.ADD_CRYPTO_TOKEN_DETAILS)
	public ResponseEntity<?> addCryptoTokenDetails(@ModelAttribute CryptoTokenMainDTO cmainObj) {

		try {

			List<CryptoTokenDetailsDTO> obj = cmainObj.getCryptoTokenDetails();
			String userName = cmainObj.getUserName();

			// Validation (blank checks optional; format/length only when present)

			Pattern STRING_PATTERN = Pattern.compile("^[A-Za-z0-9().,&\\- ]+$");
			Pattern DIGITS_PATTERN = Pattern.compile("^[0-9]+$");

			if (obj != null) {
			for (CryptoTokenDetailsDTO item : obj) {

				if (item.getBrandName() != null && !item.getBrandName().trim().isEmpty()) {
					if (item.getBrandName().length() < 3 || item.getBrandName().length() > 50) {
						return new ResponseEntity<>("Brand name length must be between 3 and 50 characters.",
								HttpStatus.BAD_REQUEST);
					} else if (!STRING_PATTERN.matcher(item.getBrandName()).matches()) {
						return new ResponseEntity<>("Only alphabets, numbers, characters ().,&- are allowed",
								HttpStatus.BAD_REQUEST);
					}
				}

				if (item.getOemDetails() != null && !item.getOemDetails().trim().isEmpty()) {
					if (item.getOemDetails().length() < 3 || item.getOemDetails().length() > 100) {
						return new ResponseEntity<>("OEM details must be between 3 and 100 characters.",
								HttpStatus.BAD_REQUEST);
					}
				}

				if (item.getMakInPercentage() != null && !item.getMakInPercentage().trim().isEmpty()) {
					if (!DIGITS_PATTERN.matcher(item.getMakInPercentage()).matches()) {
						return new ResponseEntity<>("Only digits are allowed.", HttpStatus.BAD_REQUEST);
					} else if (item.getMakInPercentage().length() < 1 || item.getMakInPercentage().length() > 3) {
						return new ResponseEntity<>("Maximum 3 digits are allowed.", HttpStatus.BAD_REQUEST);
					}
				}

				boolean hasNewFile = item.getSecAuditDetails() != null && !item.getSecAuditDetails().isEmpty();
				if (hasNewFile && !isPdfAndSizeValid(item.getSecAuditDetails())) {
					return new ResponseEntity<>("Invalid file. It must be a PDF and not exceed 5 MB.",
							HttpStatus.BAD_REQUEST);
				}

			}
			}

			// Save Annexure

			String decryptedUser = EncryptionUtil.decrypt(userName).replace("\"", "");
			AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", decryptedUser);

			if (amain == null) {

				AnnexureMain sobj = new AnnexureMain();
				sobj.setCompleted(8);
				sobj.setCreatedBy(userName);
				sobj.setStatus("Active");
				sobj.setUpdatedBy(userName);
				sobj.setUserName(EncryptionUtil.decrypt(userName));

				amain = annexServ.addAnnexureMain(sobj);
			} else {

				if (amain.getCompleted() == null || amain.getCompleted() < 8) {
					amain.setCompleted(8);
				}
				amain.setUpdated(new Date());

				AnnexureMain savedObj = annexServ.addAnnexureMain(amain);

				amain = savedObj;
			}

			CryptoTokenMain mainObj = cryptoTokenMainServ.getByAnnexureId(amain.getAnnexureMainId());

			if (mainObj == null) {

				CryptoTokenMain mObj = new CryptoTokenMain();

				mObj.setAnnexureMainId(amain);
				CryptoTokenMain savedObj = cryptoTokenMainServ.addCryptoTokenMain(mObj);

				mainObj = savedObj;
			}

			if (obj != null) {
				for (CryptoTokenDetailsDTO a : obj) {

					Random rand = new Random();
					Integer rnum = rand.nextInt(1000);

					CryptoTokenDetails objPeriod = null;

					if (a.getCryptoTokDetailsId() != null && !a.getCryptoTokDetailsId().isEmpty()) {
						try {
							String decryptedId = EncryptionUtil.decrypt(a.getCryptoTokDetailsId()).replace("\"", "");
							if (decryptedId != null && !decryptedId.isEmpty()) {
								objPeriod = cryptoTokenDetailsServ
										.getByCryptoTokenDetailsId(Long.parseLong(decryptedId));
							}
						} catch (Exception ignored) {
							// Fall through to insert
						}
					}

					if (objPeriod == null) {

						CryptoTokenDetails d = new CryptoTokenDetails();
						d.setBrandName(a.getBrandName());
						d.setCryptoTokMainId(mainObj);
						if (a.getFipCertUpTo() != null && !a.getFipCertUpTo().trim().isEmpty()) {
							d.setFipCertUpTo(new SimpleDateFormat("yyyy-MM-dd").parse(a.getFipCertUpTo()));
						}
						if (a.getMakInPercentage() != null && !a.getMakInPercentage().trim().isEmpty()) {
							d.setMakInPercentage(Integer.parseInt(a.getMakInPercentage()));
						}
						d.setOemDetails(a.getOemDetails());

						if (a.getSecAuditDetails() != null && !a.getSecAuditDetails().isEmpty()) {
							String filename = DocumentFileUtil.saveFile(a.getSecAuditDetails(), "Crypto", rnum.toString(),
									"Annexure//Crypto");
							d.setSecAuditDetails(filename);
						}

						cryptoTokenDetailsServ.addCryptoTokenDetails(d);

					} else {

						if (a.getSecAuditDetails() != null && !a.getSecAuditDetails().isEmpty()) {
							if (objPeriod.getSecAuditDetails() != null) {
								DocumentFileUtil.deleteFile(objPeriod.getSecAuditDetails(), "Annexure//Crypto");
							}
							String filename = DocumentFileUtil.saveFile(a.getSecAuditDetails(), "Crypto",
									rnum.toString(), "Annexure//Crypto");

							objPeriod.setSecAuditDetails(filename);
						}

						objPeriod.setBrandName(a.getBrandName());
						if (a.getFipCertUpTo() != null && !a.getFipCertUpTo().trim().isEmpty()) {
							objPeriod.setFipCertUpTo(new SimpleDateFormat("yyyy-MM-dd").parse(a.getFipCertUpTo()));
						} else {
							objPeriod.setFipCertUpTo(null);
						}
						if (a.getMakInPercentage() != null && !a.getMakInPercentage().trim().isEmpty()) {
							objPeriod.setMakInPercentage(Integer.parseInt(a.getMakInPercentage()));
						} else {
							objPeriod.setMakInPercentage(null);
						}
						objPeriod.setOemDetails(a.getOemDetails());
						objPeriod.setUpdated(new Date());

						cryptoTokenDetailsServ.addCryptoTokenDetails(objPeriod);
					}

				}
			}

			return new ResponseEntity<>("Your data has been successfully added.", HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error occured while saving the details, try again.", HttpStatus.BAD_REQUEST);

		}

	}

	@GetMapping(AnnexureAPIs.GET_CRYPTO_TOKEN_DETAILS)
	public ResponseEntity<?> getCryptoTokenDetails(@RequestParam("id") String username) {

		try {

			String decryptedUser = EncryptionUtil.decrypt(username).replace("\"", "");
			AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", decryptedUser);
			if (amain == null) {
				return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
			}
			CryptoTokenMain mainObj = cryptoTokenMainServ.getByAnnexureId(amain.getAnnexureMainId());
			if (mainObj == null) {
				return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
			}
			List<CryptoTokenDetails> list = cryptoTokenDetailsServ.getByCryptoTokenMainId(mainObj.getCryptoTokMainId());

			return new ResponseEntity<>(list, HttpStatus.OK);

		} catch (Exception e) {

			return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);

		}

	}

	@GetMapping(AnnexureAPIs.DOWNLOAD_CRYPTO_TOKEN_DOCUMENT)
	public ResponseEntity<?> downloadCryptoTokenDocument(@RequestParam("id") String uid) {
		try {

			if (uid == null || uid.isEmpty()) {
				return new ResponseEntity<>("Error in downloading file.", HttpStatus.BAD_REQUEST);
			}

			String id = EncryptionUtil.decrypt(uid);

			if (id == null) {
				return new ResponseEntity<>("Error in downloading file.", HttpStatus.BAD_REQUEST);
			}

			String filename = "";
			String filepath = "Annexure//Crypto";

			CryptoTokenDetails document = cryptoTokenDetailsServ.getByCryptoTokenDetailsId(Long.parseLong(id));

			filename = document.getSecAuditDetails();

			Path filePath = Paths.get(Constant.REAL_PATH, filepath).resolve(filename).normalize();

			Resource resource = new UrlResource(filePath.toUri());

			if (resource.exists() && resource.isReadable()) {
				String contentType = Files.probeContentType(filePath);

				return ResponseEntity.ok()
						.contentType(MediaType.parseMediaType(
								contentType != null ? contentType : MediaType.APPLICATION_OCTET_STREAM_VALUE))
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
						.header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
						.body(resource);
			} else {
				return new ResponseEntity<>("Error in downloading file.", HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			return new ResponseEntity<>("Error in downloading file.", HttpStatus.BAD_REQUEST);
		}

	}

//9				

	@PostMapping(AnnexureAPIs.ADD_CA_SOFTWARE_DETAILS)
	public ResponseEntity<?> addCASoftwareDetailsDetails(@RequestBody CaSwWebMainDTO caSwWebObj) {

		try {

			List<CaSwWebDetailsDTO> obj = caSwWebObj.getCaSwWebDetails();
			String userName = caSwWebObj.getUserName();

			// Validation (blank checks optional; format/length only when present)

			Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z, ]+$");
			Pattern STRING_PATTERN = Pattern.compile("^[A-Za-z0-9().,&\\- ]+$");
			Pattern DESCRIPTION_PATTERN = Pattern.compile("^[A-Za-z0-9 ]+$");

			if (obj != null) {
			for (CaSwWebDetailsDTO item : obj) {

				if (item.getDescription() != null && !item.getDescription().trim().isEmpty()) {
					if (item.getDescription().length() < 3 || item.getDescription().length() > 50) {
						return new ResponseEntity<>("Description length must be between 3 and 50 characters.",
								HttpStatus.BAD_REQUEST);
					} else if (!DESCRIPTION_PATTERN.matcher(item.getDescription()).matches()) {
						return new ResponseEntity<>("Only alphabets, numbers, characters ().,&- are allowed",
								HttpStatus.BAD_REQUEST);
					}
				}

				if (item.getDevelopedBy() != null && !item.getDevelopedBy().trim().isEmpty()) {
					if (item.getDevelopedBy().length() < 3 || item.getDevelopedBy().length() > 64) {
						return new ResponseEntity<>("Length must be between 3 and 64 characters.", HttpStatus.BAD_REQUEST);
					} else if (!NAME_PATTERN.matcher(item.getDevelopedBy()).matches()) {
						return new ResponseEntity<>("Only alphabets, character , and space are allowed",
								HttpStatus.BAD_REQUEST);
					}
				}

				if (item.getDatabaseUsed() != null && !item.getDatabaseUsed().trim().isEmpty()) {
					if (item.getDatabaseUsed().length() < 3 || item.getDatabaseUsed().length() > 30) {
						return new ResponseEntity<>("Length must be between 3 and 30 characters.", HttpStatus.BAD_REQUEST);
					} else if (!NAME_PATTERN.matcher(item.getDatabaseUsed()).matches()) {
						return new ResponseEntity<>("Only alphabets, character , and space are allowed",
								HttpStatus.BAD_REQUEST);
					}
				}

				if (item.getCertification() != null && !item.getCertification().trim().isEmpty()) {
					if (item.getCertification().length() < 3 || item.getCertification().length() > 64) {
						return new ResponseEntity<>("Length must be between 3 and 64 characters.", HttpStatus.BAD_REQUEST);
					} else if (!STRING_PATTERN.matcher(item.getCertification()).matches()) {
						return new ResponseEntity<>("Only alphabets, numbers, characters ().,&- are allowed",
								HttpStatus.BAD_REQUEST);
					}
				}

			}
			}

			// Save Annexure

			AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", EncryptionUtil.decrypt(userName));

			if (amain == null) {

				AnnexureMain sobj = new AnnexureMain();
				sobj.setCompleted(9);
				sobj.setCreatedBy(userName);
				sobj.setStatus("Active");
				sobj.setUpdatedBy(userName);
				sobj.setUserName(EncryptionUtil.decrypt(userName));

				amain = annexServ.addAnnexureMain(sobj);
			} else {

				if (amain.getCompleted() == null || amain.getCompleted() < 9) {
					amain.setCompleted(9);
				}
				amain.setUpdated(new Date());

				AnnexureMain savedObj = annexServ.addAnnexureMain(amain);

				amain = savedObj;
			}

			// Save main

			CaSwWebMain mainObj = caSwWebMainServ.getByAnnexureId(amain.getAnnexureMainId());

			if (mainObj == null) {

				CaSwWebMain mObj = new CaSwWebMain();

				mObj.setAnnexureMainId(amain);
				CaSwWebMain savedObj = caSwWebMainServ.addCaSwWebMain(mObj);

				mainObj = savedObj;
			}

			if (obj != null) {
				for (CaSwWebDetailsDTO a : obj) {

					CaSwWebDetails objPeriod = null;

					if (a.getCaWebDetailsId() != null && !a.getCaWebDetailsId().isEmpty()
							&& EncryptionUtil.decrypt(a.getCaWebDetailsId()) != null) {
						objPeriod = caSwWebDetailsServ
								.getByCaSwWebDetailsId(Long.parseLong(EncryptionUtil.decrypt(a.getCaWebDetailsId())));
					}

					if (objPeriod == null) {

						CaSwWebDetails d = new CaSwWebDetails();
						d.setCaWebMainId(mainObj);
						d.setCertification(a.getCertification());
						d.setDatabaseUsed(a.getDatabaseUsed());
						d.setDescription(a.getDescription());
						d.setDevelopedBy(a.getDevelopedBy());
						d.setLastSecurityAudit(a.getLastSecurityAudit());
						caSwWebDetailsServ.addCaSwWebDetails(d);

					} else {

						objPeriod.setCertification(a.getCertification());
						objPeriod.setDatabaseUsed(a.getDatabaseUsed());
						objPeriod.setDescription(a.getDescription());
						objPeriod.setDevelopedBy(a.getDevelopedBy());
						objPeriod.setLastSecurityAudit(a.getLastSecurityAudit());
						objPeriod.setUpdated(new Date());

						caSwWebDetailsServ.addCaSwWebDetails(objPeriod);

					}

				}
			}

			return new ResponseEntity<>("Your data has been successfully added.", HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error occured while saving the details, try again.", HttpStatus.BAD_REQUEST);

		}

	}

	@GetMapping(AnnexureAPIs.GET_CA_SOFTWARE_DETAILS)
	public ResponseEntity<?> getCASoftwareDetailsDetails(@RequestParam("id") String username) {

		try {

			AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", EncryptionUtil.decrypt(username));
			CaSwWebMain mainObj = caSwWebMainServ.getByAnnexureId(amain.getAnnexureMainId());
			List<CaSwWebDetails> list = caSwWebDetailsServ.getByCaSwWebMainId(mainObj.getCaWebMainId());

			return new ResponseEntity<>(list, HttpStatus.OK);

		} catch (Exception e) {

			return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);

		}

	}

//10
	// Add and Update
	@PostMapping(AnnexureAPIs.ADD_CA_LOCATION_DETAILS)
	public ResponseEntity<?> addLocationDetails(@ModelAttribute LocationMainDTO cmainObj) {

		try {

			List<LocationDetailsDTO> obj = cmainObj.getLocationDetails();
			String userName = cmainObj.getUserName();

			// Validation (blank checks optional; format/length/file type only when present)

			Pattern STRING_PATTERN = Pattern.compile("^[A-Za-z0-9().,&\\- ]+$");

			if (obj != null) {
			for (LocationDetailsDTO item : obj) {

				if (item.getLocation() != null && !item.getLocation().trim().isEmpty()) {
					if (item.getLocation().length() < 3 || item.getLocation().length() > 100) {
						return new ResponseEntity<>("Brand name length must be between 3 and 100 characters.",
								HttpStatus.BAD_REQUEST);
					} else if (!STRING_PATTERN.matcher(item.getLocation()).matches()) {
						return new ResponseEntity<>("Only alphabets, numbers, characters ().,&- are allowed",
								HttpStatus.BAD_REQUEST);
					}
				}

				if (item.getCaAdministratorCount() != null
						&& isExcelAndSizeValid(item.getCaAdministratorCount()) == false) {
					return new ResponseEntity<>("Invalid file. It must be a xls and not exceed 5 MB.",
							HttpStatus.BAD_REQUEST);
				}

				if (item.getSysAdministratorCount() != null
						&& isExcelAndSizeValid(item.getSysAdministratorCount()) == false) {
					return new ResponseEntity<>("Invalid file. It must be a xls and not exceed 5 MB.",
							HttpStatus.BAD_REQUEST);
				}

				if (item.getCaOperatorsCount() != null
						&& isExcelAndSizeValid(item.getCaOperatorsCount()) == false) {
					return new ResponseEntity<>("Invalid file. It must be a xls and not exceed 5 MB.",
							HttpStatus.BAD_REQUEST);
				}

				if (item.getVerificationOfficersCount() != null
						&& isExcelAndSizeValid(item.getVerificationOfficersCount()) == false) {
					return new ResponseEntity<>("Invalid file. It must be a xls and not exceed 5 MB.",
							HttpStatus.BAD_REQUEST);
				}

				if (item.getCaManpowerCount() != null
						&& isExcelAndSizeValid(item.getCaManpowerCount()) == false) {
					return new ResponseEntity<>("Invalid file. It must be a xls and not exceed 5 MB.",
							HttpStatus.BAD_REQUEST);
				}

			}
			}

			// Save Annexure

			AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", EncryptionUtil.decrypt(userName));

			if (amain == null) {

				AnnexureMain sobj = new AnnexureMain();
				sobj.setCompleted(10);
				sobj.setCreatedBy(userName);
				sobj.setStatus("Active");
				sobj.setUpdatedBy(userName);
				sobj.setUserName(EncryptionUtil.decrypt(userName));

				amain = annexServ.addAnnexureMain(sobj);
			} else {

				if (amain.getCompleted() == null || amain.getCompleted() < 10) {
					amain.setCompleted(10);
				}
				amain.setUpdated(new Date());

				AnnexureMain savedObj = annexServ.addAnnexureMain(amain);

				amain = savedObj;
			}

			LocationMain mainObj = locationMainServ.getByAnnexureId(amain.getAnnexureMainId());

			if (mainObj == null) {

				LocationMain mObj = new LocationMain();

				mObj.setAnnexureMainId(amain);
				LocationMain savedObj = locationMainServ.addLocationMain(mObj);

				mainObj = savedObj;
			}

			if (obj != null) {
				for (LocationDetailsDTO a : obj) {

					Random rand = new Random();
					Integer rnum = rand.nextInt(1000);

					LocationDetails objPeriod = null;

					if (a.getLocationDetailsId() != null && !a.getLocationDetailsId().isEmpty()
							&& EncryptionUtil.decrypt(a.getLocationDetailsId()) != null) {
						objPeriod = locationDetailsServ.getByLocationDetailsId(
								Long.parseLong(EncryptionUtil.decrypt(a.getLocationDetailsId())));
					}

					if (objPeriod == null) {

						LocationDetails d = new LocationDetails();
						d.setLocationMainId(mainObj);
						d.setDescription(a.getDescription());
						d.setLocation(a.getLocation());

						if (a.getCaAdministratorCount() != null && !a.getCaAdministratorCount().isEmpty()) {
							d.setCaAdministratorCount(DocumentFileUtil.saveFile(a.getCaAdministratorCount(), "CAADMIN",
									rnum.toString(), "Annexure//Location"));
						}
						if (a.getSysAdministratorCount() != null && !a.getSysAdministratorCount().isEmpty()) {
							d.setSysAdministratorCount(DocumentFileUtil.saveFile(a.getSysAdministratorCount(), "SYSADMIN",
									rnum.toString(), "Annexure//Location"));
						}
						if (a.getCaOperatorsCount() != null && !a.getCaOperatorsCount().isEmpty()) {
							d.setCaOperatorsCount(DocumentFileUtil.saveFile(a.getCaOperatorsCount(), "OPERATOR",
									rnum.toString(), "Annexure//Location"));
						}
						if (a.getVerificationOfficersCount() != null && !a.getVerificationOfficersCount().isEmpty()) {
							d.setVerificationOfficersCount(DocumentFileUtil.saveFile(a.getVerificationOfficersCount(),
									"VERIFICATION", rnum.toString(), "Annexure//Location"));
						}
						if (a.getCaManpowerCount() != null && !a.getCaManpowerCount().isEmpty()) {
							d.setCaManpowerCount(DocumentFileUtil.saveFile(a.getCaManpowerCount(), "CAMANPOWER",
									rnum.toString(), "Annexure//Location"));
						}

						locationDetailsServ.addLocationDetails(d);

					} else {

						if (a.getVerificationOfficersCount() != null && !a.getVerificationOfficersCount().isEmpty()) {
							DocumentFileUtil.deleteFile(objPeriod.getVerificationOfficersCount(), "Annexure//Location");
							String filename = DocumentFileUtil.saveFile(a.getVerificationOfficersCount(),
									"VERIFICATION", rnum.toString(), "Annexure//Locatioin");

							objPeriod.setVerificationOfficersCount(filename);
						}

						if (a.getSysAdministratorCount() != null && !a.getSysAdministratorCount().isEmpty()) {
							DocumentFileUtil.deleteFile(objPeriod.getSysAdministratorCount(), "Annexure//Location");
							String filename = DocumentFileUtil.saveFile(a.getSysAdministratorCount(), "SYSADMIN",
									rnum.toString(), "Annexure//Locatioin");

							objPeriod.setSysAdministratorCount(filename);
						}

						if (a.getCaAdministratorCount() != null && !a.getCaAdministratorCount().isEmpty()) {
							DocumentFileUtil.deleteFile(objPeriod.getCaAdministratorCount(), "Annexure//Location");
							String filename = DocumentFileUtil.saveFile(a.getCaAdministratorCount(), "CAADMIN",
									rnum.toString(), "Annexure//Locatioin");

							objPeriod.setCaAdministratorCount(filename);
						}

						if (a.getCaManpowerCount() != null && !a.getCaManpowerCount().isEmpty()) {
							DocumentFileUtil.deleteFile(objPeriod.getCaManpowerCount(), "Annexure//Location");
							String filename = DocumentFileUtil.saveFile(a.getCaManpowerCount(), "CAMANPOWER",
									rnum.toString(), "Annexure//Locatioin");

							objPeriod.setCaManpowerCount(filename);
						}

						if (a.getCaOperatorsCount() != null && !a.getCaOperatorsCount().isEmpty()) {
							DocumentFileUtil.deleteFile(objPeriod.getCaOperatorsCount(), "Annexure//Location");
							String filename = DocumentFileUtil.saveFile(a.getCaOperatorsCount(), "OPERATOR",
									rnum.toString(), "Annexure//Locatioin");

							objPeriod.setCaOperatorsCount(filename);
						}

						objPeriod.setLocation(a.getLocation());
						objPeriod.setUpdated(new Date());

						locationDetailsServ.addLocationDetails(objPeriod);
					}

				}
			}

			return new ResponseEntity<>("Your data has been successfully added.", HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error occured while saving the details, try again.", HttpStatus.BAD_REQUEST);

		}

	}

	@GetMapping(AnnexureAPIs.GET_CA_LOCATION_DETAILS)
	public ResponseEntity<?> getCaLocationDetails(@RequestParam("id") String username) {

		try {

			AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", EncryptionUtil.decrypt(username));
			LocationMain mainObj = locationMainServ.getByAnnexureId(amain.getAnnexureMainId());
			List<LocationDetails> list = locationDetailsServ.getByLocationMainId(mainObj.getLocationMainId());

			return new ResponseEntity<>(list, HttpStatus.OK);

		} catch (Exception e) {

			return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);

		}

	}

	@GetMapping(AnnexureAPIs.DOWNLOAD_CA_LOCATION_DOCUMENT)
	public ResponseEntity<?> downloadLocationDocument(@RequestParam("id") String uid, @RequestParam("pid") String pid) {
		try {

			if (uid == null || uid.isEmpty()) {
				return new ResponseEntity<>("Error in downloading file.", HttpStatus.BAD_REQUEST);
			}

			String id = EncryptionUtil.decrypt(uid);

			if (id == null) {
				return new ResponseEntity<>("Error in downloading file.", HttpStatus.BAD_REQUEST);
			}

			String filename = "";
			String filepath = "Annexure//Location";

			LocationDetails document = locationDetailsServ.getByLocationDetailsId(Long.parseLong(id));

			if (pid.equals("1")) {
				filename = document.getCaAdministratorCount();
			} else if (pid.equals("2")) {
				filename = document.getSysAdministratorCount();
			} else if (pid.equals("3")) {
				filename = document.getCaOperatorsCount();
			} else if (pid.equals("4")) {
				filename = document.getVerificationOfficersCount();
			} else if (pid.equals("5")) {
				filename = document.getCaManpowerCount();
			}

			Path filePath = Paths.get(Constant.REAL_PATH, filepath).resolve(filename).normalize();

			Resource resource = new UrlResource(filePath.toUri());

			if (resource.exists() && resource.isReadable()) {
				String contentType = Files.probeContentType(filePath);

				return ResponseEntity.ok()
						.contentType(MediaType.parseMediaType(
								contentType != null ? contentType : MediaType.APPLICATION_OCTET_STREAM_VALUE))
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
						.header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
						.body(resource);
			} else {
				return new ResponseEntity<>("Error in downloading file.", HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			return new ResponseEntity<>("Error in downloading file.", HttpStatus.BAD_REQUEST);
		}

	}

//11

	// Add and Update
	@PostMapping(AnnexureAPIs.ADD_CA_SERVICE_DETAILS)
	public ResponseEntity<?> addCAServicesDetails(@ModelAttribute CAServicesMainDTO cmainObj) {

		try {

			List<CAServicesDetailsDTO> obj = cmainObj.getCaServicesDetails();
			String userName = cmainObj.getUserName();

			// Validation (blank checks optional; format/length/file type only when present)

			Pattern DIGITS_PATTERN = Pattern.compile("^[0-9]+$");
			Pattern DESCRIPTION_PATTERN = Pattern.compile("^[A-Za-z0-9 ]+$");

			if (obj != null) {
			for (CAServicesDetailsDTO item : obj) {

				if (item.getDescription() != null && !item.getDescription().trim().isEmpty()) {
					if (item.getDescription().length() < 3 || item.getDescription().length() > 50) {
						return new ResponseEntity<>("Description length must be between 3 and 50 characters.",
								HttpStatus.BAD_REQUEST);
					} else if (!DESCRIPTION_PATTERN.matcher(item.getDescription()).matches()) {
						return new ResponseEntity<>("Only alphabets, numbers, characters ().,&- are allowed",
								HttpStatus.BAD_REQUEST);
					}
				}

				if (item.getInternalOnly() != null && !item.getInternalOnly().trim().isEmpty()
						&& !item.getInternalOnly().equals("Yes") && !item.getInternalOnly().equals("No")) {
					return new ResponseEntity<>("Please select Yes or No.1", HttpStatus.BAD_REQUEST);
				}

				if (item.getExternalOnly() != null && !item.getExternalOnly().trim().isEmpty()
						&& !item.getExternalOnly().equals("Yes") && !item.getExternalOnly().equals("No")) {
					return new ResponseEntity<>("Please select Yes or No.2", HttpStatus.BAD_REQUEST);
				}

				if (item.getAspOrgCount() != null && !item.getAspOrgCount().trim().isEmpty()) {
					if (!DIGITS_PATTERN.matcher(item.getAspOrgCount()).matches()) {
						return new ResponseEntity<>("Only digits are allowed.", HttpStatus.BAD_REQUEST);
					} else if (item.getAspOrgCount().length() > 9) {
						return new ResponseEntity<>("Maximum 9 digits are allowed.", HttpStatus.BAD_REQUEST);
					}
				}

				if (item.getAspOrgCountFile() != null && !item.getAspOrgCountFile().isEmpty()
						&& isExcelAndSizeValid(item.getAspOrgCountFile()) == false) {
					return new ResponseEntity<>("Invalid file. It must be a xls and not exceed 5 MB.",
							HttpStatus.BAD_REQUEST);
				}

			}
			}

			// Save Annexure

			AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", EncryptionUtil.decrypt(userName));

			if (amain == null) {

				AnnexureMain sobj = new AnnexureMain();
				sobj.setCompleted(11);
				sobj.setCreatedBy(userName);
				sobj.setStatus("Active");
				sobj.setUpdatedBy(userName);
				sobj.setUserName(EncryptionUtil.decrypt(userName));

				amain = annexServ.addAnnexureMain(sobj);
			} else {

				if (amain.getCompleted() == null || amain.getCompleted() < 11) {
					amain.setCompleted(11);
				}
				amain.setUpdated(new Date());

				AnnexureMain savedObj = annexServ.addAnnexureMain(amain);

				amain = savedObj;
			}

			CAServicesMain mainObj = caServicesMainServ.getByAnnexureId(amain.getAnnexureMainId());

			if (mainObj == null) {

				CAServicesMain mObj = new CAServicesMain();

				mObj.setAnnexureMainId(amain);
				CAServicesMain savedObj = caServicesMainServ.addCAServicesMain(mObj);

				mainObj = savedObj;
			}

			if (obj != null) {
				for (CAServicesDetailsDTO a : obj) {

					Random rand = new Random();
					Integer rnum = rand.nextInt(1000);

					CAServicesDetails objPeriod = null;

					if (a.getCaServicesDetailsId() != null && !a.getCaServicesDetailsId().isEmpty()
							&& EncryptionUtil.decrypt(a.getCaServicesDetailsId()) != null) {
						objPeriod = caServicesDetailsServ.getByCAServicesDetailsId(
								Long.parseLong(EncryptionUtil.decrypt(a.getCaServicesDetailsId())));
					}

					if (objPeriod == null) {

						CAServicesDetails d = new CAServicesDetails();
						d.setAspOrgCount(parseLongOrNull(a.getAspOrgCount()));
						d.setCaServicesMainId(mainObj);
						d.setDescription(a.getDescription());
						d.setExternalOnly(a.getExternalOnly());
						d.setInternalOnly(a.getInternalOnly());

						if (a.getAspOrgCountFile() != null && !a.getAspOrgCountFile().isEmpty()) {
							String filename = DocumentFileUtil.saveFile(a.getAspOrgCountFile(), "SERVICE", rnum.toString(),
									"Annexure//Service");
							d.setAspOrgCountFile(filename);
						}

						caServicesDetailsServ.addCAServicesDetails(d);

					} else {

						if (a.getAspOrgCountFile() != null && !a.getAspOrgCountFile().isEmpty()) {
							DocumentFileUtil.deleteFile(objPeriod.getAspOrgCountFile(), "Annexure//Service");
							String filename = DocumentFileUtil.saveFile(a.getAspOrgCountFile(), "SERVICE",
									rnum.toString(), "Annexure//Service");

							objPeriod.setAspOrgCountFile(filename);
						}

						objPeriod.setAspOrgCount(parseLongOrNull(a.getAspOrgCount()));
						objPeriod.setDescription(a.getDescription());
						objPeriod.setExternalOnly(a.getExternalOnly());
						objPeriod.setInternalOnly(a.getInternalOnly());

						objPeriod.setUpdated(new Date());

						caServicesDetailsServ.addCAServicesDetails(objPeriod);
					}

				}
			}

			return new ResponseEntity<>("Your data has been successfully added.", HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error occured while saving the details, try again.", HttpStatus.BAD_REQUEST);

		}

	}

	@GetMapping(AnnexureAPIs.GET_CA_SERVICE_DETAILS)
	public ResponseEntity<?> getCaServiceDetails(@RequestParam("id") String username) {

		try {

			AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", EncryptionUtil.decrypt(username));
			CAServicesMain mainObj = caServicesMainServ.getByAnnexureId(amain.getAnnexureMainId());
			List<CAServicesDetails> list = caServicesDetailsServ.getByCAServicesMainId(mainObj.getCaServicesMainId());

			return new ResponseEntity<>(list, HttpStatus.OK);

		} catch (Exception e) {

			return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);

		}

	}

	@GetMapping(AnnexureAPIs.DOWNLOAD_CA_SERVICE_DOCUMENT)
	public ResponseEntity<?> downloadServiceDocument(@RequestParam("id") String uid) {
		try {

			if (uid == null || uid.isEmpty()) {
				return new ResponseEntity<>("Error in downloading file.", HttpStatus.BAD_REQUEST);
			}

			String id = EncryptionUtil.decrypt(uid);

			if (id == null) {
				return new ResponseEntity<>("Error in downloading file.", HttpStatus.BAD_REQUEST);
			}

			String filename = "";
			String filepath = "Annexure//Service";

			CAServicesDetails document = caServicesDetailsServ.getByCAServicesDetailsId(Long.parseLong(id));

			filename = document.getAspOrgCountFile();

			Path filePath = Paths.get(Constant.REAL_PATH, filepath).resolve(filename).normalize();

			Resource resource = new UrlResource(filePath.toUri());

			if (resource.exists() && resource.isReadable()) {
				String contentType = Files.probeContentType(filePath);

				return ResponseEntity.ok()
						.contentType(MediaType.parseMediaType(
								contentType != null ? contentType : MediaType.APPLICATION_OCTET_STREAM_VALUE))
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
						.header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
						.body(resource);
			} else {
				return new ResponseEntity<>("Error in downloading file.", HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			return new ResponseEntity<>("Error in downloading file.", HttpStatus.BAD_REQUEST);
		}

	}

	// 12. Add and Update
	@PostMapping(AnnexureAPIs.ADD_ASP_DETAILS)
	public ResponseEntity<?> addASPDetails(@ModelAttribute ASPDetailsDTO obj) {

		try {

			String userName = obj.getUserName();

			// Validation (blank checks optional; file type only when present)

			if (obj.getAspCount() != null && !obj.getAspCount().isEmpty()
					&& isExcelAndSizeValid(obj.getAspCount()) == false) {
				return new ResponseEntity<>("Invalid file. It must be a xls and not exceed 5 MB.",
						HttpStatus.BAD_REQUEST);
			}

			if (obj.getAspsAuditOverdueCount() != null && !obj.getAspsAuditOverdueCount().isEmpty()
					&& isExcelAndSizeValid(obj.getAspsAuditOverdueCount()) == false) {
				return new ResponseEntity<>("Invalid file. It must be a xls and not exceed 5 MB.",
						HttpStatus.BAD_REQUEST);
			}

			// Save Annexure

			String decryptedUser = EncryptionUtil.decrypt(userName).replace("\"", "");
			AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", decryptedUser);

			if (amain == null) {

				AnnexureMain sobj = new AnnexureMain();
				sobj.setCompleted(12);
				sobj.setCreatedBy(userName);
				sobj.setStatus("Active");
				sobj.setUpdatedBy(userName);
				sobj.setUserName(EncryptionUtil.decrypt(userName));

				amain = annexServ.addAnnexureMain(sobj);
			} else {

				if (amain.getCompleted() == null || amain.getCompleted() < 12) {
					amain.setCompleted(12);
				}
				amain.setUpdated(new Date());

				AnnexureMain savedObj = annexServ.addAnnexureMain(amain);

				amain = savedObj;
			}

			// Details

			Random rand = new Random();
			Integer rnum = rand.nextInt(1000);

			ASPDetails objASPDetails = null;

			if (obj.getAspDetailsId() != null && !obj.getAspDetailsId().isEmpty()) {
				try {
					String decryptedId = EncryptionUtil.decrypt(obj.getAspDetailsId()).replace("\"", "");
					if (decryptedId != null && !decryptedId.isEmpty()) {
						objASPDetails = aspDetailsServ.getByASPDetailsId(Long.parseLong(decryptedId));
					}
				} catch (Exception ignored) {
					// Fall through to annexure lookup
				}
			}

			if (objASPDetails == null) {
				objASPDetails = aspDetailsServ.getByAnnexureId(amain.getAnnexureMainId());
			}

			if (objASPDetails == null) {

				ASPDetails d = new ASPDetails();
				d.setAnnexureMainId(amain);

				if (obj.getAspCount() != null && !obj.getAspCount().isEmpty()) {
					d.setAspCount(DocumentFileUtil.saveFile(obj.getAspCount(), "ASP", rnum.toString(),
							"Annexure//ASP"));
				}
				if (obj.getAspsAuditOverdueCount() != null && !obj.getAspsAuditOverdueCount().isEmpty()) {
					d.setAspsAuditOverdueCount(DocumentFileUtil.saveFile(obj.getAspsAuditOverdueCount(), "ASPAUDIT",
							rnum.toString(), "Annexure//ASP"));
				}

				aspDetailsServ.addASPDetails(d);

			} else {

				if (obj.getAspCount() != null && !obj.getAspCount().isEmpty()) {
					DocumentFileUtil.deleteFile(objASPDetails.getAspCount(), "Annexure//ASP");
					String filename = DocumentFileUtil.saveFile(obj.getAspCount(), "ASP", rnum.toString(),
							"Annexure//ASP");

					objASPDetails.setAspCount(filename);
				}

				if (obj.getAspsAuditOverdueCount() != null && !obj.getAspsAuditOverdueCount().isEmpty()) {
					DocumentFileUtil.deleteFile(objASPDetails.getAspsAuditOverdueCount(), "Annexure//ASP");
					String filename = DocumentFileUtil.saveFile(obj.getAspsAuditOverdueCount(), "ASPAUDIT",
							rnum.toString(), "Annexure//ASP");

					objASPDetails.setAspsAuditOverdueCount(filename);
				}

				aspDetailsServ.addASPDetails(objASPDetails);

			}

			return new ResponseEntity<>("Your data has been successfully added.", HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error occured while saving the details, try again.", HttpStatus.BAD_REQUEST);

		}

	}

	@GetMapping(AnnexureAPIs.GET_ASP_DETAILS)
	public ResponseEntity<?> getASPDetails(@RequestParam("id") String username) {

		try {

			String decryptedUser = EncryptionUtil.decrypt(username).replace("\"", "");
			AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", decryptedUser);
			if (amain == null) {
				return new ResponseEntity<>(null, HttpStatus.OK);
			}
			ASPDetails mainObj = aspDetailsServ.getByAnnexureId(amain.getAnnexureMainId());
			if (mainObj == null) {
				return new ResponseEntity<>(null, HttpStatus.OK);
			}

			return new ResponseEntity<>(mainObj, HttpStatus.OK);

		} catch (Exception e) {

			return new ResponseEntity<>(null, HttpStatus.OK);

		}

	}

	@GetMapping(AnnexureAPIs.DOWNLOAD_ASP_DETAILS_DOCUMENT)
	public ResponseEntity<?> downloadASPDetailsDocument(@RequestParam("id") String uid,
			@RequestParam("pid") String pid) {
		try {

			if (uid == null || uid.isEmpty()) {
				return new ResponseEntity<>("Error in downloading file.", HttpStatus.BAD_REQUEST);
			}

			String id = EncryptionUtil.decrypt(uid);

			if (id == null) {
				return new ResponseEntity<>("Error in downloading file.", HttpStatus.BAD_REQUEST);
			}

			String filename = "";
			String filepath = "Annexure//ASP";

			ASPDetails document = aspDetailsServ.getByASPDetailsId(Long.parseLong(id));

			if (pid.equals("1")) {
				filename = document.getAspCount();
			} else if (pid.equals("2")) {
				filename = document.getAspsAuditOverdueCount();
			}

			Path filePath = Paths.get(Constant.REAL_PATH, filepath).resolve(filename).normalize();

			Resource resource = new UrlResource(filePath.toUri());

			if (resource.exists() && resource.isReadable()) {
				String contentType = Files.probeContentType(filePath);

				return ResponseEntity.ok()
						.contentType(MediaType.parseMediaType(
								contentType != null ? contentType : MediaType.APPLICATION_OCTET_STREAM_VALUE))
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
						.header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
						.body(resource);
			} else {
				return new ResponseEntity<>("Error in downloading file.", HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			return new ResponseEntity<>("Error in downloading file.", HttpStatus.BAD_REQUEST);
		}

	}

	// 13

	// Add and Update
	@PostMapping(AnnexureAPIs.ADD_PUBLIC_INFO_DETAILS)
	public ResponseEntity<?> addPublicInfoDetails(@RequestBody PublicInfoMainDTO cmainObj) {

		try {

			List<PublicInfoDetailsDTO> obj = cmainObj.getPublicInfoDetails();
			String userName = cmainObj.getUserName();

			// Validation (blank checks optional; format/length only when present)

			Pattern WEBLINK_PATTERN = Pattern.compile("^[A-Za-z0-9().,:\\\\&\\- ]+$");
			Pattern DESCRIPTION_PATTERN = Pattern.compile("^[A-Za-z0-9().,:\\\\&\\- ]+$");

			if (obj != null) {
			for (PublicInfoDetailsDTO item : obj) {

				if (item.getDescription() != null && !item.getDescription().trim().isEmpty()) {
					if (item.getDescription().length() < 3 || item.getDescription().length() > 50) {
						return new ResponseEntity<>("Description length must be between 3 and 50 characters.",
								HttpStatus.BAD_REQUEST);
					} else if (!DESCRIPTION_PATTERN.matcher(item.getDescription()).matches()) {
						return new ResponseEntity<>("Only alphabets, numbers, characters ().,&-:\\\\ are allowed",
								HttpStatus.BAD_REQUEST);
					}
				}

				if (item.getWebLink() != null && !item.getWebLink().trim().isEmpty()) {
					if (item.getWebLink().length() < 3 || item.getWebLink().length() > 500) {
						return new ResponseEntity<>("Description length must be between 3 and 500 characters.",
								HttpStatus.BAD_REQUEST);
					} else if (!WEBLINK_PATTERN.matcher(item.getWebLink()).matches()) {
						return new ResponseEntity<>("Only alphabets, numbers, characters ().,&-:\\ are allowed",
								HttpStatus.BAD_REQUEST);
					}
				}

			}
			}

			// Save Annexure

			String decryptedUser = EncryptionUtil.decrypt(userName).replace("\"", "");
			AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", decryptedUser);

			if (amain == null) {

				AnnexureMain sobj = new AnnexureMain();
				sobj.setCompleted(13);
				sobj.setCreatedBy(userName);
				sobj.setStatus("Active");
				sobj.setUpdatedBy(userName);
				sobj.setUserName(EncryptionUtil.decrypt(userName));

				amain = annexServ.addAnnexureMain(sobj);
			} else {

				if (amain.getCompleted() == null || amain.getCompleted() < 13) {
					amain.setCompleted(13);
				}
				amain.setUpdated(new Date());

				AnnexureMain savedObj = annexServ.addAnnexureMain(amain);

				amain = savedObj;
			}

			PublicInfoMain mainObj = publicInfoMainServ.getByAnnexureId(amain.getAnnexureMainId());

			if (mainObj == null) {

				PublicInfoMain mObj = new PublicInfoMain();

				mObj.setAnnexureMainId(amain);
				PublicInfoMain savedObj = publicInfoMainServ.addPublicInfoMain(mObj);

				mainObj = savedObj;
			}

			List<PublicInfoDetails> existingRows = publicInfoDetailsServ
					.getByPublicInfoMainId(mainObj.getPublicInfoMainId());

			if (obj != null) {
				for (PublicInfoDetailsDTO a : obj) {

					PublicInfoDetails objPeriod = null;

					if (a.getPublicInfoDetailsId() != null && !a.getPublicInfoDetailsId().isEmpty()) {
						try {
							String decryptedId = EncryptionUtil.decrypt(a.getPublicInfoDetailsId()).replace("\"", "");
							if (decryptedId != null && !decryptedId.isEmpty()) {
								objPeriod = publicInfoDetailsServ
										.getByPublicInfoDetailsId(Long.parseLong(decryptedId));
							}
						} catch (Exception ignored) {
							// Fall through to description match
						}
					}

					if (objPeriod == null && a.getDescription() != null && existingRows != null) {
						for (PublicInfoDetails existing : existingRows) {
							if (existing.getDescription() != null
									&& existing.getDescription().trim().equalsIgnoreCase(a.getDescription().trim())) {
								objPeriod = existing;
								break;
							}
						}
					}

					if (objPeriod == null) {

						PublicInfoDetails d = new PublicInfoDetails();
						d.setDescription(a.getDescription());
						d.setPublicInfoMainId(mainObj);
						d.setWebLink(a.getWebLink());

						publicInfoDetailsServ.addPublicInfoDetails(d);

					} else {

						objPeriod.setDescription(a.getDescription());
						objPeriod.setPublicInfoMainId(mainObj);
						objPeriod.setWebLink(a.getWebLink());
						objPeriod.setUpdated(new Date());

						publicInfoDetailsServ.addPublicInfoDetails(objPeriod);
					}

				}
			}

			return new ResponseEntity<>("Your data has been successfully added.", HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error occured while saving the details, try again.", HttpStatus.BAD_REQUEST);

		}

	}

	@GetMapping(AnnexureAPIs.GET_PUBLIC_INFO_DETAILS)
	public ResponseEntity<?> getPublicInfoDetails(@RequestParam("id") String username) {

		try {

			String decryptedUser = EncryptionUtil.decrypt(username).replace("\"", "");
			AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", decryptedUser);
			if (amain == null) {
				return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
			}
			PublicInfoMain mainObj = publicInfoMainServ.getByAnnexureId(amain.getAnnexureMainId());
			if (mainObj == null) {
				return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
			}
			List<PublicInfoDetails> list = publicInfoDetailsServ.getByPublicInfoMainId(mainObj.getPublicInfoMainId());

			return new ResponseEntity<>(list, HttpStatus.OK);

		} catch (Exception e) {

			return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);

		}

	}

	// 14

	@PostMapping(AnnexureAPIs.ADD_CERTIFICATE_COST_DETAILS)
	public ResponseEntity<?> addCertificateDetails(@RequestBody CertificateCostDTO obj) {

		try {

			String userName = obj.getUserName();

			Pattern STRING_PATTERN = Pattern.compile("^[A-Za-z0-9().,&\\- ]+$");
			Pattern DIGITS_PATTERN = Pattern.compile("^[0-9]+$"); // 1 to 9 digits

			// Validation (blank checks optional; format/length only when present)

			if (obj.getAvgDscIssuMaintenanceCost() != null && !obj.getAvgDscIssuMaintenanceCost().trim().isEmpty()) {
				if (!DIGITS_PATTERN.matcher(obj.getAvgDscIssuMaintenanceCost()).matches()) {
					return new ResponseEntity<>("Only digits are allowed.", HttpStatus.BAD_REQUEST);
				} else if (Integer.parseInt(obj.getAvgDscIssuMaintenanceCost()) > 25000) {
					return new ResponseEntity<>("Maximum cost not exceeds 25000.", HttpStatus.BAD_REQUEST);
				}
			}

			if (obj.getAvgFeeChargedByCA() != null && !obj.getAvgFeeChargedByCA().trim().isEmpty()) {
				if (!DIGITS_PATTERN.matcher(obj.getAvgFeeChargedByCA()).matches()) {
					return new ResponseEntity<>("Only digits are allowed.", HttpStatus.BAD_REQUEST);
				} else if (Integer.parseInt(obj.getAvgFeeChargedByCA()) > 25000) {
					return new ResponseEntity<>("Maximum fee not exceeds 25000.", HttpStatus.BAD_REQUEST);
				}
			}

			if (obj.getExplanationForCostMismatch() != null && !obj.getExplanationForCostMismatch().trim().isEmpty()) {
				if (obj.getExplanationForCostMismatch().length() < 3
						|| obj.getExplanationForCostMismatch().length() > 250) {
					return new ResponseEntity<>("The length must be between 3 and 250 characters.", HttpStatus.BAD_REQUEST);
				} else if (!STRING_PATTERN.matcher(obj.getExplanationForCostMismatch()).matches()) {
					return new ResponseEntity<>("Only alphabets, numbers, characters ().,&- are allowed",
							HttpStatus.BAD_REQUEST);
				}
			}

			// Save Annexure

			AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", EncryptionUtil.decrypt(userName));

			if (amain == null) {

				AnnexureMain sobj = new AnnexureMain();
				sobj.setCompleted(14);
				sobj.setCreatedBy(userName);
				sobj.setStatus("Active");
				sobj.setUpdatedBy(userName);
				sobj.setUserName(EncryptionUtil.decrypt(userName));

				amain = annexServ.addAnnexureMain(sobj);
			} else {

				if (amain.getCompleted() == null || amain.getCompleted() < 14) {
					amain.setCompleted(14);
				}
				amain.setUpdated(new Date());

				AnnexureMain savedObj = annexServ.addAnnexureMain(amain);

				amain = savedObj;
			}

			// Details

			//
			CertificateCost certObj = null;

			if (obj.getCertCostId() != null && !obj.getCertCostId().isEmpty()
					&& EncryptionUtil.decrypt(obj.getCertCostId()) != null) {
				certObj = certificateCostServ
						.getByCertificateCostId(Long.parseLong(EncryptionUtil.decrypt(obj.getCertCostId())));
			}

			if (certObj == null) {

				CertificateCost d = new CertificateCost();
				d.setAvgDscIssuMaintenanceCost(parseIntegerOrNull(obj.getAvgDscIssuMaintenanceCost()));
				d.setAvgFeeChargedByCA(parseIntegerOrNull(obj.getAvgFeeChargedByCA()));
				d.setExplanationForCostMismatch(obj.getExplanationForCostMismatch());
				d.setAnnexureMainId(amain);

				certificateCostServ.addCertificateCost(d);

			} else {

				certObj.setUpdated(new Date());
				certObj.setAvgDscIssuMaintenanceCost(parseIntegerOrNull(obj.getAvgDscIssuMaintenanceCost()));
				certObj.setAvgFeeChargedByCA(parseIntegerOrNull(obj.getAvgFeeChargedByCA()));
				certObj.setExplanationForCostMismatch(obj.getExplanationForCostMismatch());

				certificateCostServ.addCertificateCost(certObj);

			}

			return new ResponseEntity<>("Your data has been successfully added.", HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error occured while saving the details, try again.", HttpStatus.BAD_REQUEST);

		}

	}

	@GetMapping(AnnexureAPIs.GET_CERTIFICATE_COST_DETAILS)
	public ResponseEntity<?> getCertificateCost(@RequestParam("id") String username) {

		try {

			AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", EncryptionUtil.decrypt(username));
			CertificateCost mainObj = certificateCostServ.getByAnnexureId(amain.getAnnexureMainId());

			return new ResponseEntity<>(mainObj, HttpStatus.OK);

		} catch (Exception e) {

			return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);

		}

	}

//15

	// 6. Add and Update
	@PostMapping(AnnexureAPIs.ADD_SELF_ASSESSMENT_DETAILS)
	public ResponseEntity<?> addSelfAssessmentDetails(@RequestBody SelfAssessmentMainDTO obj) {

		try {

			String userName = obj.getUserName();

			String DSC_ISSUED_COUNT_W_APP_FORM_LENGTH = "DSC issued count with application form must not exceed 9 digits.";
			String DSC_ISSUED_COUNT_W_APP_FORM_FORMAT = "Only digits are allowed for DSC issued count with application form.";

			String NC_REASON_LENGTH = "NC reason must be between 3 and 200 characters.";

			String DSC_ISSUED_W_FEE_LENGTH = "DSC issued with fee must not exceed 9 digits.";
			String DSC_ISSUED_W_FEE_FORMAT = "Only digits are allowed for DSC issued with fee.";

			String DETAILS_DSC_ISSUED_W_FEE_LENGTH = "Details of DSC issued with fee must be between 1 and 200 characters.";

			String DSC_ISSUED_W_PHYSICAL_VER_LENGTH = "DSC issued with physical verification must not exceed 9 digits.";
			String DSC_ISSUED_W_PHYSICAL_VER_FORMAT = "Only digits are allowed for DSC issued with physical verification.";

			String DSC_ISSUED_W_MATCH_LENGTH = "DSC issued with match must not exceed 9 digits.";
			String DSC_ISSUED_W_MATCH_FORMAT = "Only digits are allowed for DSC issued with match.";

			String NC_REASON_WITH_MATCH_LENGTH = "NC reason with match must be between 1 and 200 characters.";

			String IS_OTP_SENT = "OTP sent must be Yes or No.";

			String OTP_REASON_NC_LENGTH = "OTP reason NC must be between 3 and 200 characters.";

			String CA_SYSTEM_ACCESS_DETAILS_LENGTH = "CA system access details must be between 3 and 250 characters.";

			String OWN_NC_DETAILS_LENGTH = "Own NC details must be between 3 and 200 characters.";

			String STRING_VALIDATION_MSG = "Only alphabets, numbers, characters ().,&- are allowed";

			Pattern STRING_PATTERN = Pattern.compile("^[A-Za-z0-9().,&\\- ]+$");
			Pattern DIGITS_PATTERN = Pattern.compile("^[0-9]+$");

			// Validation (blank checks optional; format/length only when present)

			if (obj.getDscIssuedCountWAppForm() != null && !obj.getDscIssuedCountWAppForm().trim().isEmpty()) {
				if (!DIGITS_PATTERN.matcher(obj.getDscIssuedCountWAppForm().toString()).matches()) {
					return new ResponseEntity<>(DSC_ISSUED_COUNT_W_APP_FORM_FORMAT, HttpStatus.BAD_REQUEST);
				} else if (obj.getDscIssuedCountWAppForm().toString().length() > 9) {
					return new ResponseEntity<>(DSC_ISSUED_COUNT_W_APP_FORM_LENGTH, HttpStatus.BAD_REQUEST);
				}
			}

			if (obj.getNcReason() != null && !obj.getNcReason().trim().isEmpty()) {
				if (!STRING_PATTERN.matcher(obj.getNcReason().toString()).matches()) {
					return new ResponseEntity<>(STRING_VALIDATION_MSG, HttpStatus.BAD_REQUEST);
				} else if (obj.getNcReason().length() < 3 || obj.getNcReason().length() > 200) {
					return new ResponseEntity<>(NC_REASON_LENGTH, HttpStatus.BAD_REQUEST);
				}
			}

			if (obj.getDscIssuedWFee() != null && !obj.getDscIssuedWFee().trim().isEmpty()) {
				if (!DIGITS_PATTERN.matcher(obj.getDscIssuedWFee().toString()).matches()) {
					return new ResponseEntity<>(DSC_ISSUED_W_FEE_FORMAT, HttpStatus.BAD_REQUEST);
				} else if (obj.getDscIssuedWFee().toString().length() > 9) {
					return new ResponseEntity<>(DSC_ISSUED_W_FEE_LENGTH, HttpStatus.BAD_REQUEST);
				}
			}

			if (obj.getDetailsDSCIssuedWFee() != null && !obj.getDetailsDSCIssuedWFee().trim().isEmpty()) {
				if (!STRING_PATTERN.matcher(obj.getDetailsDSCIssuedWFee().toString()).matches()) {
					return new ResponseEntity<>(STRING_VALIDATION_MSG, HttpStatus.BAD_REQUEST);
				} else if (obj.getDetailsDSCIssuedWFee().length() < 3 || obj.getDetailsDSCIssuedWFee().length() > 200) {
					return new ResponseEntity<>(DETAILS_DSC_ISSUED_W_FEE_LENGTH, HttpStatus.BAD_REQUEST);
				}
			}

			if (obj.getDscIssuedWPhysicalVer() != null && !obj.getDscIssuedWPhysicalVer().trim().isEmpty()) {
				if (!DIGITS_PATTERN.matcher(obj.getDscIssuedWPhysicalVer().toString()).matches()) {
					return new ResponseEntity<>(DSC_ISSUED_W_PHYSICAL_VER_FORMAT, HttpStatus.BAD_REQUEST);
				} else if (obj.getDscIssuedWPhysicalVer().toString().length() > 9) {
					return new ResponseEntity<>(DSC_ISSUED_W_PHYSICAL_VER_LENGTH, HttpStatus.BAD_REQUEST);
				}
			}

			if (obj.getDscIssuedWMatch() != null && !obj.getDscIssuedWMatch().trim().isEmpty()) {
				if (!DIGITS_PATTERN.matcher(obj.getDscIssuedWMatch().toString()).matches()) {
					return new ResponseEntity<>(DSC_ISSUED_W_MATCH_FORMAT, HttpStatus.BAD_REQUEST);
				} else if (obj.getDscIssuedWMatch().toString().length() > 9) {
					return new ResponseEntity<>(DSC_ISSUED_W_MATCH_LENGTH, HttpStatus.BAD_REQUEST);
				}
			}

			if (obj.getNcReasonWithMatch() != null && !obj.getNcReasonWithMatch().trim().isEmpty()) {
				if (!STRING_PATTERN.matcher(obj.getNcReasonWithMatch().toString()).matches()) {
					return new ResponseEntity<>(STRING_VALIDATION_MSG, HttpStatus.BAD_REQUEST);
				} else if (obj.getNcReasonWithMatch().length() < 3 || obj.getNcReasonWithMatch().length() > 200) {
					return new ResponseEntity<>(NC_REASON_WITH_MATCH_LENGTH, HttpStatus.BAD_REQUEST);
				}
			}

			if (obj.getIsOTPSent() != null && !obj.getIsOTPSent().trim().isEmpty()
					&& !obj.getIsOTPSent().equals("Yes") && !obj.getIsOTPSent().equals("No")) {
				return new ResponseEntity<>(IS_OTP_SENT, HttpStatus.BAD_REQUEST);
			}

			if (obj.getOtpReasonNC() != null && !obj.getOtpReasonNC().trim().isEmpty()) {
				if (!STRING_PATTERN.matcher(obj.getOtpReasonNC().toString()).matches()) {
					return new ResponseEntity<>(STRING_VALIDATION_MSG, HttpStatus.BAD_REQUEST);
				} else if (obj.getOtpReasonNC().length() < 3 || obj.getOtpReasonNC().length() > 200) {
					return new ResponseEntity<>(OTP_REASON_NC_LENGTH, HttpStatus.BAD_REQUEST);
				}
			}

			if (obj.getCaSystemAccessDetails() != null && !obj.getCaSystemAccessDetails().trim().isEmpty()) {
				if (!STRING_PATTERN.matcher(obj.getCaSystemAccessDetails().toString()).matches()) {
					return new ResponseEntity<>(STRING_VALIDATION_MSG, HttpStatus.BAD_REQUEST);
				} else if (obj.getCaSystemAccessDetails().length() < 3 || obj.getCaSystemAccessDetails().length() > 250) {
					return new ResponseEntity<>(CA_SYSTEM_ACCESS_DETAILS_LENGTH, HttpStatus.BAD_REQUEST);
				}
			}

			if (obj.getOwnNCDetails() != null && !obj.getOwnNCDetails().trim().isEmpty()) {
				if (!STRING_PATTERN.matcher(obj.getOwnNCDetails().toString()).matches()) {
					return new ResponseEntity<>(STRING_VALIDATION_MSG, HttpStatus.BAD_REQUEST);
				} else if (obj.getOwnNCDetails().length() < 3 || obj.getOwnNCDetails().length() > 200) {
					return new ResponseEntity<>(OWN_NC_DETAILS_LENGTH, HttpStatus.BAD_REQUEST);
				}
			}

			// Save Annexure

			AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", EncryptionUtil.decrypt(userName));

			if (amain == null) {

				AnnexureMain sobj = new AnnexureMain();
				sobj.setCompleted(15);
				sobj.setCreatedBy(userName);
				sobj.setStatus("Active");
				sobj.setUpdatedBy(userName);
				sobj.setUserName(EncryptionUtil.decrypt(userName));

				amain = annexServ.addAnnexureMain(sobj);
			} else {

				if (amain.getCompleted() == null || amain.getCompleted() < 15) {
					amain.setCompleted(15);
				}
				amain.setUpdated(new Date());

				AnnexureMain savedObj = annexServ.addAnnexureMain(amain);

				amain = savedObj;
			}

			// Details

			//
			SelfAssessmentMain objSelfMain = null;

			if (obj.getSelfAssMainId() != null && !obj.getSelfAssMainId().isEmpty()
					&& EncryptionUtil.decrypt(obj.getSelfAssMainId()) != null) {
				objSelfMain = selfServ
						.getBySelfAssessmentMainId(Long.parseLong(EncryptionUtil.decrypt(obj.getSelfAssMainId())));
			}

			if (objSelfMain == null) {

				SelfAssessmentMain d = new SelfAssessmentMain();
				d.setAnnexureMainId(amain);
				d.setCaSystemAccessDetails(obj.getCaSystemAccessDetails());
				d.setDetailsDSCIssuedWFee(obj.getDetailsDSCIssuedWFee());
				d.setDscIssuedCountWAppForm(parseLongOrNull(obj.getDscIssuedCountWAppForm()));
				d.setDscIssuedWFee(parseLongOrNull(obj.getDscIssuedWFee()));
				d.setDscIssuedWMatch(parseLongOrNull(obj.getDscIssuedWMatch()));
				d.setDscIssuedWPhysicalVer(parseLongOrNull(obj.getDscIssuedWPhysicalVer()));
				d.setIsOTPSent(obj.getIsOTPSent());
				d.setNcReason(obj.getNcReason());
				d.setNcReasonWithMatch(obj.getNcReasonWithMatch());
				d.setOtpReasonNC(obj.getOtpReasonNC());
				d.setOwnNCDetails(obj.getOwnNCDetails());

				selfServ.addSelfAssessmentMain(d);

			} else {

				objSelfMain.setUpdated(new Date());
				objSelfMain.setCaSystemAccessDetails(obj.getCaSystemAccessDetails());
				objSelfMain.setDetailsDSCIssuedWFee(obj.getDetailsDSCIssuedWFee());
				objSelfMain.setDscIssuedCountWAppForm(parseLongOrNull(obj.getDscIssuedCountWAppForm()));
				objSelfMain.setDscIssuedWFee(parseLongOrNull(obj.getDscIssuedWFee()));
				objSelfMain.setDscIssuedWMatch(parseLongOrNull(obj.getDscIssuedWMatch()));
				objSelfMain.setDscIssuedWPhysicalVer(parseLongOrNull(obj.getDscIssuedWPhysicalVer()));
				objSelfMain.setIsOTPSent(obj.getIsOTPSent());
				objSelfMain.setNcReason(obj.getNcReason());
				objSelfMain.setNcReasonWithMatch(obj.getNcReasonWithMatch());
				objSelfMain.setOtpReasonNC(obj.getOtpReasonNC());
				objSelfMain.setOwnNCDetails(obj.getOwnNCDetails());

				selfServ.addSelfAssessmentMain(objSelfMain);

			}

			return new ResponseEntity<>("Your data has been successfully added.", HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error occured while saving the details, try again.", HttpStatus.BAD_REQUEST);

		}

	}

	@GetMapping(AnnexureAPIs.GET_SELF_ASSESSMENT_DETAILS)
	public ResponseEntity<?> getSelfAssessmentDetails(@RequestParam("id") String username) {

		try {

			AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", EncryptionUtil.decrypt(username));
			SelfAssessmentMain mainObj = selfServ.getByAnnexureId(amain.getAnnexureMainId());

			return new ResponseEntity<>(mainObj, HttpStatus.OK);

		} catch (Exception e) {

			return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);

		}

	}

//16

	// Add and Update
	@PostMapping(AnnexureAPIs.ADD_CONNECTIVITY_DETAILS)
	public ResponseEntity<?> addConnectivityDetails(@RequestBody ConnectivityMainDTO cmainObj) {

		try {

			List<ConnectivityDetailsDTO> obj = cmainObj.getConnectivityDetails();
			String userName = cmainObj.getUserName();

			// Validation (blank checks optional; format/length only when present)

			Pattern DESCRIPTION_PATTERN = Pattern.compile("^[A-Za-z0-9().,?&\\- ]+$");

			if (obj != null) {
			for (ConnectivityDetailsDTO item : obj) {

				if (item.getName() != null && !item.getName().trim().isEmpty()) {
					if (item.getName().length() < 3 || item.getName().length() > 250) {
						return new ResponseEntity<>("Name length must be between 3 and 250 characters.",
								HttpStatus.BAD_REQUEST);
					} else if (!DESCRIPTION_PATTERN.matcher(item.getName()).matches()) {
						return new ResponseEntity<>("Only alphabets, numbers, characters ().,?&- are allowed",
								HttpStatus.BAD_REQUEST);
					}
				}

				if (item.getDescription() != null && !item.getDescription().trim().isEmpty()) {
					if (item.getDescription().length() < 3 || item.getDescription().length() > 250) {
						return new ResponseEntity<>("Description length must be between 3 and 250 characters.",
								HttpStatus.BAD_REQUEST);
					} else if (!DESCRIPTION_PATTERN.matcher(item.getDescription()).matches()) {
						return new ResponseEntity<>("Only alphabets, numbers, characters ().,?&- are allowed",
								HttpStatus.BAD_REQUEST);
					}
				}

			}
			}

			// Save Annexure

			AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", EncryptionUtil.decrypt(userName));

			if (amain == null) {

				AnnexureMain sobj = new AnnexureMain();
				sobj.setCompleted(16);
				sobj.setCreatedBy(userName);
				sobj.setStatus("Active");
				sobj.setUpdatedBy(userName);
				sobj.setUserName(EncryptionUtil.decrypt(userName));

				amain = annexServ.addAnnexureMain(sobj);
			} else {

				if (amain.getCompleted() == null || amain.getCompleted() < 16) {
					amain.setCompleted(16);
				}
				amain.setUpdated(new Date());

				AnnexureMain savedObj = annexServ.addAnnexureMain(amain);

				amain = savedObj;
			}

			ConnectivityMain mainObj = connectivityMainServ.getByAnnexureId(amain.getAnnexureMainId());

			if (mainObj == null) {

				ConnectivityMain mObj = new ConnectivityMain();

				mObj.setAnnexureMainId(amain);
				ConnectivityMain savedObj = connectivityMainServ.addConnectivityMain(mObj);

				mainObj = savedObj;
			}

			if (obj != null) {
				for (ConnectivityDetailsDTO a : obj) {

					ConnectivityDetails objConnectivity = null;

					if (a.getConnectivityDetailsId() != null && !a.getConnectivityDetailsId().isEmpty()
							&& EncryptionUtil.decrypt(a.getConnectivityDetailsId()) != null) {
						objConnectivity = connectivityDetailsServ.getByConnectivityDetailsId(
								Long.parseLong(EncryptionUtil.decrypt(a.getConnectivityDetailsId())));
					}

					if (objConnectivity == null) {

						ConnectivityDetails d = new ConnectivityDetails();
						d.setConnectivityMainId(mainObj);
						d.setDescription(a.getDescription());
						d.setName(a.getName());

						connectivityDetailsServ.addConnectivityDetails(d);

					} else {

						objConnectivity.setConnectivityMainId(mainObj);
						objConnectivity.setDescription(a.getDescription());
						objConnectivity.setName(a.getName());
						objConnectivity.setUpdated(new Date());

						connectivityDetailsServ.addConnectivityDetails(objConnectivity);
					}

				}
			}

			return new ResponseEntity<>("Your data has been successfully added.", HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error occured while saving the details, try again.", HttpStatus.BAD_REQUEST);

		}

	}

	@GetMapping(AnnexureAPIs.GET_CONNECTIVITY_DETAILS)
	public ResponseEntity<?> getConnectivityDetails(@RequestParam("id") String username) {

		try {

			AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", EncryptionUtil.decrypt(username));
			ConnectivityMain mainObj = connectivityMainServ.getByAnnexureId(amain.getAnnexureMainId());
			List<ConnectivityDetails> list = connectivityDetailsServ
					.getByConnectivityMainId(mainObj.getConnectivityMainId());

			return new ResponseEntity<>(list, HttpStatus.OK);

		} catch (Exception e) {

			return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);

		}

	}

//17

	@PostMapping(AnnexureAPIs.ADD_DOWNTIME_DETAILS)
	public ResponseEntity<?> addDownTimeDetails(@RequestBody DownTimeDTO obj) {

		try {

			String userName = obj.getUserName();

			Pattern STRING_PATTERN = Pattern.compile("^[A-Za-z0-9().,&\\- ]+$");
			Pattern DIGITS_PATTERN = Pattern.compile("^[0-9]+$"); // 1 to 9 digits

			// Validation (blank checks optional; format/length only when present)

			if (obj.getDownTimeSecond() != null && !obj.getDownTimeSecond().trim().isEmpty()) {
				if (!DIGITS_PATTERN.matcher(obj.getDownTimeSecond()).matches()) {
					return new ResponseEntity<>("Only digits are allowed.", HttpStatus.BAD_REQUEST);
				} else if (Integer.parseInt(obj.getDownTimeSecond()) > 59) {
					return new ResponseEntity<>("Maximum 59 seconds.", HttpStatus.BAD_REQUEST);
				}
			}

			if (obj.getDownTimeMinute() != null && !obj.getDownTimeMinute().trim().isEmpty()) {
				if (!DIGITS_PATTERN.matcher(obj.getDownTimeMinute()).matches()) {
					return new ResponseEntity<>("Only digits are allowed.", HttpStatus.BAD_REQUEST);
				} else if (Integer.parseInt(obj.getDownTimeMinute()) > 59) {
					return new ResponseEntity<>("Maximum 59 minutes.", HttpStatus.BAD_REQUEST);
				}
			}

			if (obj.getDownTimeHour() != null && !obj.getDownTimeHour().trim().isEmpty()) {
				if (!DIGITS_PATTERN.matcher(obj.getDownTimeHour()).matches()) {
					return new ResponseEntity<>("Only digits are allowed.", HttpStatus.BAD_REQUEST);
				} else if (obj.getDownTimeHour().length() > 4) {
					return new ResponseEntity<>("Only 4 digits are allowed.", HttpStatus.BAD_REQUEST);
				}
			}

			if (obj.getReasonAndMeasuresTaken() != null && !obj.getReasonAndMeasuresTaken().trim().isEmpty()) {
				if (obj.getReasonAndMeasuresTaken().length() < 3 || obj.getReasonAndMeasuresTaken().length() > 500) {
					return new ResponseEntity<>("The length must be between 3 and 500 characters.", HttpStatus.BAD_REQUEST);
				} else if (!STRING_PATTERN.matcher(obj.getReasonAndMeasuresTaken()).matches()) {
					return new ResponseEntity<>("Only alphabets, numbers, characters ().,&- are allowed",
							HttpStatus.BAD_REQUEST);
				}
			}

			// Save Annexure

			AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", EncryptionUtil.decrypt(userName));

			if (amain == null) {

				AnnexureMain sobj = new AnnexureMain();
				sobj.setCompleted(17);
				sobj.setCreatedBy(userName);
				sobj.setStatus("Active");
				sobj.setUpdatedBy(userName);
				sobj.setUserName(EncryptionUtil.decrypt(userName));

				amain = annexServ.addAnnexureMain(sobj);
			} else {

				if (amain.getCompleted() == null || amain.getCompleted() < 17) {
					amain.setCompleted(17);
				}
				amain.setUpdated(new Date());

				AnnexureMain savedObj = annexServ.addAnnexureMain(amain);

				amain = savedObj;
			}

			// Details

			//
			DownTime downtimeObj = null;

			if (obj.getDownTimeId() != null && !obj.getDownTimeId().isEmpty()
					&& EncryptionUtil.decrypt(obj.getDownTimeId()) != null) {
				downtimeObj = downTimeServ.getByDownTimeId(Long.parseLong(EncryptionUtil.decrypt(obj.getDownTimeId())));
			}

			if (downtimeObj == null) {

				DownTime d = new DownTime();
				d.setDownTimeHour(parseIntegerOrNull(obj.getDownTimeHour()));
				d.setDownTimeMinute(parseIntegerOrNull(obj.getDownTimeMinute()));
				d.setDownTimeSecond(parseIntegerOrNull(obj.getDownTimeSecond()));
				d.setReasonAndMeasuresTaken(obj.getReasonAndMeasuresTaken());

				d.setAnnexureMainId(amain);

				downTimeServ.addDownTime(d);

			} else {

				downtimeObj.setUpdated(new Date());
				downtimeObj.setDownTimeHour(parseIntegerOrNull(obj.getDownTimeHour()));
				downtimeObj.setDownTimeMinute(parseIntegerOrNull(obj.getDownTimeMinute()));
				downtimeObj.setDownTimeSecond(parseIntegerOrNull(obj.getDownTimeSecond()));
				downtimeObj.setReasonAndMeasuresTaken(obj.getReasonAndMeasuresTaken());

				downTimeServ.addDownTime(downtimeObj);

			}

			return new ResponseEntity<>("Your data has been successfully added.", HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error occured while saving the details, try again.", HttpStatus.BAD_REQUEST);

		}

	}

	@GetMapping(AnnexureAPIs.GET_DOWNTIME_DETAILS)
	public ResponseEntity<?> getDownTimeDetails(@RequestParam("id") String username) {

		try {

			AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", EncryptionUtil.decrypt(username));
			DownTime mainObj = downTimeServ.getByAnnexureId(amain.getAnnexureMainId());

			return new ResponseEntity<>(mainObj, HttpStatus.OK);

		} catch (Exception e) {

			return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);

		}

	}

//18				

	@PostMapping(AnnexureAPIs.ADD_TRUSTED_PERSON_DETAILS)
	public ResponseEntity<?> addTrustedPerson(@RequestBody CATrustedPersonMainDTO objMain) {

		try {

			String userName = objMain.getUserName();
			List<CATrustedPersonDTO> objDetails = objMain.getCaTrustedPerson();

			// Validation (blank checks optional; format/length only when present)

			String FULL_NAME_LENGTH = "Full name must be between 3 and 100 characters.";
			String FULL_NAME_FORMAT = "Full name can only contain alphabets and spaces.";

			String DESIGNATION_LENGTH = "Designation must be between 3 and 50 characters.";
			String DESIGNATION_FORMAT = "Designation can only contain alphabets and spaces.";

			String LOCATION_OF_POSTING_LENGTH = "Location of posting must be between 3 and 100 characters.";

			String ROLE_LENGTH = "Role must be between 3 and 50 characters.";
			String ROLE_FORMAT = "Role can only contain alphabets and spaces.";

			String ID_CARD_NO_LENGTH = "ID card number must be between 3 and 20 characters.";
			String ID_CARD_NO_FORMAT = "ID card number can only contain alphanumeric characters.";

			String MOBILE_NO_FORMAT = "Mobile number must start with 6, 7, 8, or 9 and be exactly 10 digits.";

			String IDENTIFICATION_DETAILS_LENGTH = "Identification details must be between 3 and 250 characters.";

			String TRAINING_DETAILS_LENGTH = "Training details must be between 3 and 250 characters.";

			String STRING_VALIDATION_MSG = "Only alphabets, numbers, characters ().,&- are allowed";

			Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z ]+$");
			Pattern ID_CARD_PATTERN = Pattern.compile("^[A-Za-z0-9]+$");
			Pattern MOBILE_PATTERN = Pattern.compile("^[6-9]{1}[0-9]{9}$");
			Pattern STRING_PATTERN = Pattern.compile("^[A-Za-z0-9().,&\\- ]+$");
			Pattern ROLE_PATTERN = Pattern.compile("^[A-Za-z ]+$");

			if (objDetails != null) {
			for (CATrustedPersonDTO obj : objDetails) {

				if (obj.getFullName() != null && !obj.getFullName().trim().isEmpty()) {
					if (obj.getFullName().length() < 3 || obj.getFullName().length() > 100) {
						return new ResponseEntity<>(FULL_NAME_LENGTH, HttpStatus.BAD_REQUEST);
					} else if (!NAME_PATTERN.matcher(obj.getFullName()).matches()) {
						return new ResponseEntity<>(FULL_NAME_FORMAT, HttpStatus.BAD_REQUEST);
					}
				}

				if (obj.getDesignation() != null && !obj.getDesignation().trim().isEmpty()) {
					if (obj.getDesignation().length() < 3 || obj.getDesignation().length() > 100) {
						return new ResponseEntity<>(DESIGNATION_LENGTH, HttpStatus.BAD_REQUEST);
					} else if (!NAME_PATTERN.matcher(obj.getDesignation()).matches()) {
						return new ResponseEntity<>(DESIGNATION_FORMAT, HttpStatus.BAD_REQUEST);
					}
				}

				if (obj.getLocationOfPosting() != null && !obj.getLocationOfPosting().trim().isEmpty()) {
					if (obj.getLocationOfPosting().length() < 3 || obj.getLocationOfPosting().length() > 100) {
						return new ResponseEntity<>(LOCATION_OF_POSTING_LENGTH, HttpStatus.BAD_REQUEST);
					} else if (!STRING_PATTERN.matcher(obj.getLocationOfPosting()).matches()) {
						return new ResponseEntity<>(STRING_VALIDATION_MSG, HttpStatus.BAD_REQUEST);
					}
				}

				if (obj.getRole() != null && !obj.getRole().trim().isEmpty()) {
					if (obj.getRole().length() < 3 || obj.getRole().length() > 50) {
						return new ResponseEntity<>(ROLE_LENGTH, HttpStatus.BAD_REQUEST);
					} else if (!ROLE_PATTERN.matcher(obj.getRole()).matches()) {
						return new ResponseEntity<>(ROLE_FORMAT, HttpStatus.BAD_REQUEST);
					}
				}

				if (obj.getIdCardNo() != null && !obj.getIdCardNo().trim().isEmpty()) {
					if (!ID_CARD_PATTERN.matcher(obj.getIdCardNo()).matches()) {
						return new ResponseEntity<>(ID_CARD_NO_FORMAT, HttpStatus.BAD_REQUEST);
					} else if (obj.getIdCardNo().length() < 3 || obj.getIdCardNo().length() > 20) {
						return new ResponseEntity<>(ID_CARD_NO_LENGTH, HttpStatus.BAD_REQUEST);
					}
				}

				if (obj.getMobileNo() != null && !obj.getMobileNo().trim().isEmpty()) {
					if (!MOBILE_PATTERN.matcher(obj.getMobileNo()).matches()) {
						return new ResponseEntity<>(MOBILE_NO_FORMAT, HttpStatus.BAD_REQUEST);
					}
				}

				if (obj.getIdentificationDetails() != null && !obj.getIdentificationDetails().trim().isEmpty()) {
					if (obj.getIdentificationDetails().length() < 3
							|| obj.getIdentificationDetails().length() > 250) {
						return new ResponseEntity<>(IDENTIFICATION_DETAILS_LENGTH, HttpStatus.BAD_REQUEST);
					} else if (!STRING_PATTERN.matcher(obj.getIdentificationDetails()).matches()) {
						return new ResponseEntity<>(STRING_VALIDATION_MSG, HttpStatus.BAD_REQUEST);
					}
				}

				if (obj.getTrainingDetails() != null && !obj.getTrainingDetails().trim().isEmpty()) {
					if (obj.getTrainingDetails().length() < 3 || obj.getTrainingDetails().length() > 250) {
						return new ResponseEntity<>(TRAINING_DETAILS_LENGTH, HttpStatus.BAD_REQUEST);
					} else if (!STRING_PATTERN.matcher(obj.getTrainingDetails()).matches()) {
						return new ResponseEntity<>(STRING_VALIDATION_MSG, HttpStatus.BAD_REQUEST);
					}
				}

			}
			}

			// Save Annexure

			AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", EncryptionUtil.decrypt(userName));

			if (amain == null) {

				AnnexureMain sobj = new AnnexureMain();
				sobj.setCompleted(18);
				sobj.setCreatedBy(userName);
				sobj.setStatus("Active");
				sobj.setUpdatedBy(userName);
				sobj.setUserName(EncryptionUtil.decrypt(userName));

				amain = annexServ.addAnnexureMain(sobj);
			} else {

				if (amain.getCompleted() == null || amain.getCompleted() < 18) {
					amain.setCompleted(18);
				}
				amain.setUpdated(new Date());

				AnnexureMain savedObj = annexServ.addAnnexureMain(amain);

				amain = savedObj;
			}

			CATrustedPersonMain mainObj = personMainServ.getByAnnexureId(amain.getAnnexureMainId());

			if (mainObj == null) {

				CATrustedPersonMain mObj = new CATrustedPersonMain();

				mObj.setAnnexureMainId(amain);
				CATrustedPersonMain savedObj = personMainServ.addCATrustedPersonMain(mObj);

				mainObj = savedObj;
			}

			if (objDetails != null) {
				for (CATrustedPersonDTO obj : objDetails) {

					CATrustedPerson trustedPersonObj = null;

					if (obj.getPersonId() != null && !obj.getPersonId().isEmpty()
							&& EncryptionUtil.decrypt(obj.getPersonId()) != null) {
						trustedPersonObj = personServ
								.getByCATrustedPersonId(Long.parseLong(EncryptionUtil.decrypt(obj.getPersonId())));
					}

					if (trustedPersonObj == null) {

						CATrustedPerson d = new CATrustedPerson();

						d.setPersonMainId(mainObj);
						d.setDesignation(obj.getDesignation());
						d.setEmployedSince(obj.getEmployedSince());
						d.setFullName(obj.getFullName());
						d.setIdCardNo(obj.getIdCardNo());
						d.setIdentificationDetails(obj.getIdentificationDetails());
						d.setLastBackVerDate(obj.getLastBackVerDate());
						d.setLocationOfPosting(obj.getLocationOfPosting());
						d.setMobileNo(obj.getMobileNo());
						d.setRole(obj.getRole());
						d.setTrainingDetails(obj.getTrainingDetails());

						personServ.addCATrustedPerson(d);

					} else {

						trustedPersonObj.setUpdated(new Date());
						trustedPersonObj.setDesignation(obj.getDesignation());
						trustedPersonObj.setEmployedSince(obj.getEmployedSince());
						trustedPersonObj.setFullName(obj.getFullName());
						trustedPersonObj.setIdCardNo(obj.getIdCardNo());
						trustedPersonObj.setIdentificationDetails(obj.getIdentificationDetails());
						trustedPersonObj.setLastBackVerDate(obj.getLastBackVerDate());
						trustedPersonObj.setLocationOfPosting(obj.getLocationOfPosting());
						trustedPersonObj.setMobileNo(obj.getMobileNo());
						trustedPersonObj.setRole(obj.getRole());
						trustedPersonObj.setTrainingDetails(obj.getTrainingDetails());

						personServ.addCATrustedPerson(trustedPersonObj);

					}

				}
			}

			return new ResponseEntity<>("Your data has been successfully added.", HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error occured while saving the details, try again.", HttpStatus.BAD_REQUEST);

		}

	}

	@GetMapping(AnnexureAPIs.GET_TRUSTED_PERSON_DETAILS)
	public ResponseEntity<?> getTrustedPersonDetails(@RequestParam("id") String username) {

		try {
			String decrypted = EncryptionUtil.decrypt(username);
			if (decrypted != null) {
				decrypted = decrypted.replace("\"", "").trim();
			}
			AnnexureMain amain = annexServ.getAnnexureForAuditorView(decrypted);
			if (amain == null) {
				return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
			}
			CATrustedPersonMain mainObj = personMainServ.getByAnnexureId(amain.getAnnexureMainId());
			if (mainObj == null) {
				return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
			}
			List<CATrustedPerson> list = personServ.getByCATrustedPersonMainId(mainObj.getPersonMainId());

			return new ResponseEntity<>(list, HttpStatus.OK);

		} catch (Exception e) {

			return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);

		}

	}

	// Auditor Verification

	@PostMapping(AnnexureAPIs.ADD_AUDITOR_VERIFICATION)
	public ResponseEntity<?> addAuditorVerification(@RequestBody AuditorVerificationDTO object) {

		try {

			String username = EncryptionUtil.decrypt(object.getUserName());
			if (username != null) {
				username = username.replace("\"", "").trim();
			}
			String apiNumber = EncryptionUtil.decrypt(object.getApiNumber());
			String auditorVerificaton = object.getAuditorVerification();

			System.out.println(username + "" + apiNumber + "" + auditorVerificaton);

			/*--
			* 1. Annual Audit period details
			* 2. Internal Audit Details
			* 3. eKYC account Audit Details of last one year-Month wise -– during the annual audit period
			* 4. eKYC account-based verification enabled by CA- Details of the audit Period
			* 5. RA audit details – during the annual audit period
			* 6. No of Court Cases /Police Complaints
			* 7. No of revocation of DSC during the audit period
			* 8. Empanelment of crypto Token
			* 9. Details of CA software/Website
			* 10. Details of DC & DR Site
			* 11. Details of CA Services
			* 12. Details of ASP
			* 13. Public Information maintained at the website of CA
			* 14. Cost of Certificates issued during audit period.
			* 15. CA Self-assessment for audit period.
			* 16. CA Software and external connectivity.
			* 17. Down time during the Audit period.
			* 18. List of CA trusted persons
			*/

			AnnexureMain amain = annexServ.getAnnexureForAuditorView(username);
			if (amain == null) {
				return new ResponseEntity<>("Annexure not found for the provided username.", HttpStatus.BAD_REQUEST);
			}

			// Annexure A2 sections are optional for the applicant. Auditors may verify
			// any filled section in any order — do not require previous section remarks.
			int sectionNumber;
			try {
				sectionNumber = Integer.parseInt(apiNumber);
			} catch (NumberFormatException nfe) {
				return new ResponseEntity<>("Invalid API Number.", HttpStatus.BAD_REQUEST);
			}

			if (amain.getAuditorTracker() == null || amain.getAuditorTracker() < sectionNumber) {
				amain.setAuditorTracker(sectionNumber);
				amain.setUpdated(new Date());
				amain = annexServ.addAnnexureMain(amain);
			}

			switch (apiNumber) {

			case "1": {
				AnnualAuditPeriodMain mainObj = periodMainServ.getByAnnexureId(amain.getAnnexureMainId());
				if (mainObj == null) {
					return new ResponseEntity<>("No applicant data found for this section.", HttpStatus.OK);
				}
				mainObj.setAuditorVerification(auditorVerificaton);
				mainObj.setUpdated(new Date());
				periodMainServ.addAnnualAuditPeriodMain(mainObj);
				break;
			}
			case "2": {
				InternalAuditMain mainObj1 = internalMainServ.getByAnnexureId(amain.getAnnexureMainId());
				if (mainObj1 == null) {
					return new ResponseEntity<>("No applicant data found for this section.", HttpStatus.OK);
				}
				mainObj1.setAuditorVerification(auditorVerificaton);
				mainObj1.setUpdated(new Date());
				internalMainServ.addInternalAuditMain(mainObj1);
				break;
			}
			case "3": {
				EKYCMonthMain mainObj2 = monthMainServ.getByAnnexureId(amain.getAnnexureMainId());
				if (mainObj2 == null) {
					return new ResponseEntity<>("No applicant data found for this section.", HttpStatus.OK);
				}
				mainObj2.setAuditorVerification(auditorVerificaton);
				mainObj2.setUpdated(new Date());
				monthMainServ.addEKYCMonthMainn(mainObj2);
				break;
			}
			case "4": {
				EKYCAccountBasedMain mainObj3 = accountBasedServ.getByAnnexureId(amain.getAnnexureMainId());
				if (mainObj3 == null) {
					return new ResponseEntity<>("No applicant data found for this section.", HttpStatus.OK);
				}
				mainObj3.setAuditorVerification(auditorVerificaton);
				mainObj3.setUpdated(new Date());
				accountBasedServ.addeKYCAcMain(mainObj3);
				break;
			}
			case "5": {
				RaAuditMain mainObj4 = raAuditServ.getByAnnexureId(amain.getAnnexureMainId());
				if (mainObj4 == null) {
					return new ResponseEntity<>("No applicant data found for this section.", HttpStatus.OK);
				}
				mainObj4.setAuditorVerification(auditorVerificaton);
				mainObj4.setUpdated(new Date());
				raAuditServ.addRaAuditMain(mainObj4);
				break;
			}
			case "6": {
				CourtCasesMain mainObj5 = courtCasesServ.getByAnnexureId(amain.getAnnexureMainId());
				if (mainObj5 == null) {
					return new ResponseEntity<>("No applicant data found for this section.", HttpStatus.OK);
				}
				mainObj5.setAuditorVerification(auditorVerificaton);
				mainObj5.setUpdated(new Date());
				courtCasesServ.addCourtCasesMain(mainObj5);
				break;
			}
			case "7": {
				RevocationMain mainObj6 = revocationServ.getByAnnexureId(amain.getAnnexureMainId());
				if (mainObj6 == null) {
					return new ResponseEntity<>("No applicant data found for this section.", HttpStatus.OK);
				}
				mainObj6.setAuditorVerification(auditorVerificaton);
				mainObj6.setUpdated(new Date());
				revocationServ.addRevocationMain(mainObj6);
				break;
			}
			case "8": {
				CryptoTokenMain mainObj7 = cryptoTokenMainServ.getByAnnexureId(amain.getAnnexureMainId());
				if (mainObj7 == null) {
					return new ResponseEntity<>("No applicant data found for this section.", HttpStatus.OK);
				}
				mainObj7.setAuditorVerification(auditorVerificaton);
				mainObj7.setUpdated(new Date());
				cryptoTokenMainServ.addCryptoTokenMain(mainObj7);
				break;
			}
			case "9": {
				CaSwWebMain mainObj8 = caSwWebMainServ.getByAnnexureId(amain.getAnnexureMainId());
				if (mainObj8 == null) {
					return new ResponseEntity<>("No applicant data found for this section.", HttpStatus.OK);
				}
				mainObj8.setAuditorVerification(auditorVerificaton);
				mainObj8.setUpdated(new Date());
				caSwWebMainServ.addCaSwWebMain(mainObj8);
				break;
			}
			case "10": {
				LocationMain mainObj9 = locationMainServ.getByAnnexureId(amain.getAnnexureMainId());
				if (mainObj9 == null) {
					return new ResponseEntity<>("No applicant data found for this section.", HttpStatus.OK);
				}
				mainObj9.setAuditorVerification(auditorVerificaton);
				mainObj9.setUpdated(new Date());
				locationMainServ.addLocationMain(mainObj9);
				break;
			}
			case "11": {
				CAServicesMain mainObj10 = caServicesMainServ.getByAnnexureId(amain.getAnnexureMainId());
				if (mainObj10 == null) {
					return new ResponseEntity<>("No applicant data found for this section.", HttpStatus.OK);
				}
				mainObj10.setAuditorVerification(auditorVerificaton);
				mainObj10.setUpdated(new Date());
				caServicesMainServ.addCAServicesMain(mainObj10);
				break;
			}
			case "12": {
				ASPDetails mainObj11 = aspDetailsServ.getByAnnexureId(amain.getAnnexureMainId());
				if (mainObj11 == null) {
					return new ResponseEntity<>("No applicant data found for this section.", HttpStatus.OK);
				}
				mainObj11.setAuditorVerification(auditorVerificaton);
				mainObj11.setUpdated(new Date());
				aspDetailsServ.addASPDetails(mainObj11);
				break;
			}
			case "13": {
				PublicInfoMain mainObj12 = publicInfoMainServ.getByAnnexureId(amain.getAnnexureMainId());
				if (mainObj12 == null) {
					return new ResponseEntity<>("No applicant data found for this section.", HttpStatus.OK);
				}
				mainObj12.setAuditorVerification(auditorVerificaton);
				mainObj12.setUpdated(new Date());
				publicInfoMainServ.addPublicInfoMain(mainObj12);
				break;
			}
			case "14": {
				CertificateCost mainObj13 = certificateCostServ.getByAnnexureId(amain.getAnnexureMainId());
				if (mainObj13 == null) {
					return new ResponseEntity<>("No applicant data found for this section.", HttpStatus.OK);
				}
				mainObj13.setAuditorVerification(auditorVerificaton);
				mainObj13.setUpdated(new Date());
				certificateCostServ.addCertificateCost(mainObj13);
				break;
			}
			case "15": {
				SelfAssessmentMain mainObj14 = selfServ.getByAnnexureId(amain.getAnnexureMainId());
				if (mainObj14 == null) {
					return new ResponseEntity<>("No applicant data found for this section.", HttpStatus.OK);
				}
				mainObj14.setAuditorVerification(auditorVerificaton);
				mainObj14.setUpdated(new Date());
				selfServ.addSelfAssessmentMain(mainObj14);
				break;
			}
			case "16": {
				ConnectivityMain mainObj15 = connectivityMainServ.getByAnnexureId(amain.getAnnexureMainId());
				if (mainObj15 == null) {
					return new ResponseEntity<>("No applicant data found for this section.", HttpStatus.OK);
				}
				mainObj15.setAuditorVerification(auditorVerificaton);
				mainObj15.setUpdated(new Date());
				connectivityMainServ.addConnectivityMain(mainObj15);
				break;
			}
			case "17": {
				DownTime mainObj16 = downTimeServ.getByAnnexureId(amain.getAnnexureMainId());
				if (mainObj16 == null) {
					return new ResponseEntity<>("No applicant data found for this section.", HttpStatus.OK);
				}
				mainObj16.setAuditorVerification(auditorVerificaton);
				mainObj16.setUpdated(new Date());
				downTimeServ.addDownTime(mainObj16);
				break;
			}
			case "18": {
				CATrustedPersonMain mainObj17 = personMainServ.getByAnnexureId(amain.getAnnexureMainId());
				if (mainObj17 == null) {
					mainObj17 = new CATrustedPersonMain();
					mainObj17.setAnnexureMainId(amain);
					mainObj17 = personMainServ.addCATrustedPersonMain(mainObj17);
				}
				mainObj17.setAuditorVerification(auditorVerificaton);
				mainObj17.setUpdated(new Date());
				personMainServ.addCATrustedPersonMain(mainObj17);
				break;
			}
			default:
				return new ResponseEntity<>("Invalid API Number.", HttpStatus.BAD_REQUEST);

			}

			return new ResponseEntity<>("Auditor Verification has been added.", HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Auditor Verification not added.", HttpStatus.BAD_REQUEST);

		}

	}

	@GetMapping(AnnexureAPIs.GET_AUDITOR_VERIFICATION)
	public ResponseEntity<?> getAuditorVerification(@RequestParam("id") String userName,
			@RequestParam("pid") String apiNum) {
		try {
			// Decrypt the parameters
			String username = EncryptionUtil.decrypt(userName);
			String apiNumber = EncryptionUtil.decrypt(apiNum);
			if (username != null) {
				username = username.replace("\"", "").trim();
			}

			System.out.println(username + "" + apiNumber);

			/*--
			 * 1.  Annual Audit period details
			 * 2.  Internal Audit Details 
			 * 3.  eKYC account Audit Details of last one year-Month wise -– during the annual audit period 
			 * 4.  eKYC account-based verification enabled by CA- Details of the audit Period
			 * 5.  RA audit details – during the annual audit period  
			 * 6.  No of Court Cases /Police Complaints
			 * 7.  No of revocation of DSC  during the audit period
			 * 8.  Empanelment of crypto Token 
			 * 9.  Details of CA software/Website
			 * 10. Details of  DC & DR Site 
			 * 11. Details of CA Services 
			 * 12. Details of ASP 
			 * 13. Public Information maintained at the website of CA
			 * 14. Cost of Certificates  issued during audit period. 
			 * 15. CA Self-assessment for  audit period.
			 * 16. CA Software and external connectivity.
			 * 17. Down time during the Audit period.
			 * 18. List of CA trusted persons
			 */

			// Active annexure first; after final submit remarks live on Inactive row
			AnnexureMain amain = annexServ.getAnnexureForAuditorView(username);
			if (amain == null) {
				return new ResponseEntity<>("Annexure not found for the provided username.", HttpStatus.NOT_FOUND);
			}

			// Prepare the response map
			Map<String, Object> response = new HashMap<>();

			switch (apiNumber) {
			case "1":
				AnnualAuditPeriodMain mainObj = periodMainServ.getByAnnexureId(amain.getAnnexureMainId());
				if (mainObj != null) {
					List<AnnualAuditPeriodDetails> list = periodServ
							.getByAnnualAuditPeriodMainId(mainObj.getPeriodMainId());
					response.put("details", list);
					response.put("auditorVerification", mainObj.getAuditorVerification());
					return new ResponseEntity<>(response, HttpStatus.OK);
				} else {
					return new ResponseEntity<>("Annual Audit Period Main object not found.", HttpStatus.NOT_FOUND);
				}

			case "2":
				InternalAuditMain mainObj1 = internalMainServ.getByAnnexureId(amain.getAnnexureMainId());
				if (mainObj1 != null) {
					List<InternalAuditDetails> list1 = internalServ
							.getByInternalAuditMainId(mainObj1.getInAuditMainId());
					response.put("details", list1);
					response.put("auditorVerification", mainObj1.getAuditorVerification());
					return new ResponseEntity<>(response, HttpStatus.OK);
				} else {
					return new ResponseEntity<>("Internal Audit Main object not found.", HttpStatus.NOT_FOUND);
				}

			case "3":
				EKYCMonthMain mainObj2 = monthMainServ.getByAnnexureId(amain.getAnnexureMainId());
				if (mainObj2 != null) {
					List<EKYCAcMonthDetails> list2 = monthDetailsServ
							.getByEKYCAcMonthMainId(mainObj2.geteKYCMonthMainId());
					response.put("details", list2);
					response.put("auditorVerification", mainObj2.getAuditorVerification());
					return new ResponseEntity<>(response, HttpStatus.OK);
				} else {
					return new ResponseEntity<>("EKYC Month Main object not found.", HttpStatus.NOT_FOUND);
				}

			case "4":
				EKYCAccountBasedMain mainObj3 = accountBasedServ.getByAnnexureId(amain.getAnnexureMainId());
				if (mainObj3 != null) {
					return new ResponseEntity<>(mainObj3, HttpStatus.OK);
				} else {
					return new ResponseEntity<>("EKYC Account-Based Main object not found.", HttpStatus.NOT_FOUND);
				}

			case "5":
				RaAuditMain mainObj4 = raAuditServ.getByAnnexureId(amain.getAnnexureMainId());
				if (mainObj4 != null) {
					return new ResponseEntity<>(mainObj4, HttpStatus.OK);
				} else {
					return new ResponseEntity<>("RA Audit Main object not found.", HttpStatus.NOT_FOUND);
				}

			case "6":
				CourtCasesMain mainObj5 = courtCasesServ.getByAnnexureId(amain.getAnnexureMainId());
				if (mainObj5 != null) {
					return new ResponseEntity<>(mainObj5, HttpStatus.OK);
				} else {
					return new ResponseEntity<>("Court Cases Main object not found.", HttpStatus.NOT_FOUND);
				}

			case "7":
				RevocationMain mainObj6 = revocationServ.getByAnnexureId(amain.getAnnexureMainId());
				if (mainObj6 != null) {
					return new ResponseEntity<>(mainObj6, HttpStatus.OK);
				} else {
					return new ResponseEntity<>("Revocation Main object not found.", HttpStatus.NOT_FOUND);
				}

			case "8":
				CryptoTokenMain mainObj7 = cryptoTokenMainServ.getByAnnexureId(amain.getAnnexureMainId());
				if (mainObj7 != null) {
					List<CryptoTokenDetails> list3 = cryptoTokenDetailsServ
							.getByCryptoTokenMainId(mainObj7.getCryptoTokMainId());
					response.put("cryptoTokenDetails", list3);
					response.put("auditorVerification", mainObj7.getAuditorVerification());
					return new ResponseEntity<>(response, HttpStatus.OK);
				} else {
					return new ResponseEntity<>("Crypto Token Main object not found.", HttpStatus.NOT_FOUND);
				}

			case "9":
				CaSwWebMain mainObj8 = caSwWebMainServ.getByAnnexureId(amain.getAnnexureMainId());
				if (mainObj8 != null) {
					List<CaSwWebDetails> list4 = caSwWebDetailsServ.getByCaSwWebMainId(mainObj8.getCaWebMainId());
					response.put("caSwWebDetails", list4);
					response.put("auditorVerification", mainObj8.getAuditorVerification());
					return new ResponseEntity<>(response, HttpStatus.OK);
				} else {
					return new ResponseEntity<>("CA Software/Website Main object not found.", HttpStatus.NOT_FOUND);
				}

			case "10":
				LocationMain mainObj9 = locationMainServ.getByAnnexureId(amain.getAnnexureMainId());
				if (mainObj9 != null) {
					List<LocationDetails> list5 = locationDetailsServ.getByLocationMainId(mainObj9.getLocationMainId());
					response.put("locationDetails", list5);
					response.put("auditorVerification", mainObj9.getAuditorVerification());
					return new ResponseEntity<>(response, HttpStatus.OK);
				} else {
					return new ResponseEntity<>("Location Main object not found.", HttpStatus.NOT_FOUND);
				}

			case "11":
				CAServicesMain mainObj10 = caServicesMainServ.getByAnnexureId(amain.getAnnexureMainId());
				if (mainObj10 != null) {
					List<CAServicesDetails> list6 = caServicesDetailsServ
							.getByCAServicesMainId(mainObj10.getCaServicesMainId());
					response.put("caServicesDetails", list6);
					response.put("auditorVerification", mainObj10.getAuditorVerification());
					return new ResponseEntity<>(response, HttpStatus.OK);
				} else {
					return new ResponseEntity<>("CA Services Main object not found.", HttpStatus.NOT_FOUND);
				}

			case "12":
				ASPDetails mainObj11 = aspDetailsServ.getByAnnexureId(amain.getAnnexureMainId());
				if (mainObj11 != null) {
					return new ResponseEntity<>(mainObj11, HttpStatus.OK);
				} else {
					return new ResponseEntity<>("ASP Details object not found.", HttpStatus.NOT_FOUND);
				}

			case "13":
				PublicInfoMain mainObj12 = publicInfoMainServ.getByAnnexureId(amain.getAnnexureMainId());
				if (mainObj12 != null) {
					List<PublicInfoDetails> list7 = publicInfoDetailsServ
							.getByPublicInfoMainId(mainObj12.getPublicInfoMainId());
					response.put("publicInfoDetails", list7);
					response.put("auditorVerification", mainObj12.getAuditorVerification());
					return new ResponseEntity<>(response, HttpStatus.OK);
				} else {
					return new ResponseEntity<>("Public Info Main object not found.", HttpStatus.NOT_FOUND);
				}

			case "14":
				CertificateCost mainObj13 = certificateCostServ.getByAnnexureId(amain.getAnnexureMainId());
				if (mainObj13 != null) {
					return new ResponseEntity<>(mainObj13, HttpStatus.OK);
				} else {
					return new ResponseEntity<>("Certificate Cost object not found.", HttpStatus.NOT_FOUND);
				}

			case "15":
				SelfAssessmentMain mainObj14 = selfServ.getByAnnexureId(amain.getAnnexureMainId());
				if (mainObj14 != null) {
					return new ResponseEntity<>(mainObj14, HttpStatus.OK);
				} else {
					return new ResponseEntity<>("Self Assessment Main object not found.", HttpStatus.NOT_FOUND);
				}

			case "16":
				ConnectivityMain mainObj15 = connectivityMainServ.getByAnnexureId(amain.getAnnexureMainId());
				if (mainObj15 != null) {
					List<ConnectivityDetails> list8 = connectivityDetailsServ
							.getByConnectivityMainId(mainObj15.getConnectivityMainId());
					response.put("connectivityDetails", list8);
					response.put("auditorVerification", mainObj15.getAuditorVerification());
					return new ResponseEntity<>(response, HttpStatus.OK);
				} else {
					return new ResponseEntity<>("Connectivity Main object not found.", HttpStatus.NOT_FOUND);
				}

			case "17":
				DownTime mainObj16 = downTimeServ.getByAnnexureId(amain.getAnnexureMainId());
				if (mainObj16 != null) {
					return new ResponseEntity<>(mainObj16, HttpStatus.OK);
				} else {
					return new ResponseEntity<>("Down Time object not found.", HttpStatus.NOT_FOUND);
				}

			case "18":
				CATrustedPersonMain mainObj17 = personMainServ.getByAnnexureId(amain.getAnnexureMainId());
				if (mainObj17 != null) {
					List<CATrustedPerson> list9 = personServ.getByCATrustedPersonMainId(mainObj17.getPersonMainId());
					response.put("trustedPersonDetails", list9);
					response.put("auditorVerification", mainObj17.getAuditorVerification());
				} else {
					response.put("trustedPersonDetails", Collections.emptyList());
					response.put("auditorVerification", null);
				}
				return new ResponseEntity<>(response, HttpStatus.OK);

			default:
				return new ResponseEntity<>("Invalid API Number.", HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error in getting the details: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(AnnexureAPIs.INACTIVE_PREVIOUS_ANNEXURE_SUBMITTED)
	public ResponseEntity<?> inactiveAuditorVerification(@RequestParam("id") String username) {

		try {
			AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", EncryptionUtil.decrypt(username));
			amain.setStatus("Inactive");
			amain.setUpdated(new Date());
			annexServ.addAnnexureMain(amain);
			return new ResponseEntity<>("Annexure deactivation is successful.", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Annexure deactivation was unsuccessful. Please try again.",
					HttpStatus.BAD_REQUEST);
		}
	}

	// Helper Methods
	private static int countWords(String text) {
		return text.trim().split("\\s+").length;
	}

	private static Long parseLongOrNull(String value) {
		if (value == null || value.trim().isEmpty()) {
			return null;
		}
		return Long.parseLong(value.trim());
	}

	private static Integer parseIntegerOrNull(String value) {
		if (value == null || value.trim().isEmpty()) {
			return null;
		}
		return Integer.parseInt(value.trim());
	}

	public static boolean isPdfAndSizeValid(MultipartFile file) {
		// Check if the file is empty
		if (file.isEmpty()) {
			return false;
		}

		// Check if the file is a PDF (based on the extension)
		String originalFilename = file.getOriginalFilename();
		if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".pdf")) {
			return false;
		}

		// Check if the file size is less than or equal to 5 MB
		long maxSize = 5 * 1024 * 1024; // 5 MB in bytes
		if (file.getSize() > maxSize) {
			return false;
		}

		return true;
	}

	public static boolean isExcelAndSizeValid(MultipartFile file) {
		// Check if the file is empty
		if (file.isEmpty()) {
			return false;
		}

		// Check if the file is an Excel file (based on the extension)
		String originalFilename = file.getOriginalFilename();
		if (originalFilename == null || !(originalFilename.toLowerCase().endsWith(".xls")
				|| originalFilename.toLowerCase().endsWith(".xlsx"))) {
			return false;
		}

		// Check if the file size is less than or equal to 5 MB
		long maxSize = 5 * 1024 * 1024; // 5 MB in bytes
		if (file.getSize() > maxSize) {
			return false;
		}

		return true;
	}
	
	
	@GetMapping(AnnexureAPIs.GET_ALL_ANNEXURE_MAIN)
	public ResponseEntity<?> getAllAuditorMain() {

		try {
			List<AnnexureMain> amain = annexServ.getAllAnnexureMain();
			
			return new ResponseEntity<>(amain, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Annexure deactivation was unsuccessful. Please try again.",
					HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(AnnexureAPIs.GET_ANNEXURE_MAIN_BY_USERNAME)
	public ResponseEntity<?> getAnnexureMainByUsername(@RequestParam("username") String username) {
		String uname = username;
		if (EncryptionUtil.decrypt(uname) != null) {
			uname = EncryptionUtil.decrypt(uname);
		}
		if (uname != null) {
			uname = uname.replace("\"", "").trim();
		}
		try {
			AnnexureMain amain = annexServ.getAnnexureForAuditorView(uname);
			if (amain == null) {
				return new ResponseEntity<>("Annexure not found for the provided username.", HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(amain, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Unable to fetch annexure details.", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(AnnexureAPIs.CHANGE_STATUS_ANNEXURE)
	public ResponseEntity<?> changeAllAuditorMain(@RequestParam("username") String username) {
		
       String uname = username;
		
		if(EncryptionUtil.decrypt(uname) != null)
			uname = EncryptionUtil.decrypt(uname);
		if (uname != null) {
			uname = uname.replace("\"", "").trim();
		}

		try {
			AnnexureMain amain = annexServ.getAnnexureForAuditorView(uname);
			if (amain == null) {
				return new ResponseEntity<>("No annexure record to deactivate.", HttpStatus.OK);
			}
			if (hasMissingAuditorRemarksForFilledSections(amain)) {
				return new ResponseEntity<>("Please provide Auditor Remarks", HttpStatus.BAD_REQUEST);
			}
			if ("Active".equalsIgnoreCase(amain.getStatus())) {
				amain.setStatus("Inactive");
				amain.setUpdated(new Date());
				annexServ.addAnnexureMain(amain);
			}
			return new ResponseEntity<>("Annexure Main was successful updated.", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Annexure deactivation was unsuccessful. Please try again.",
					HttpStatus.BAD_REQUEST);
		}
	}

	private boolean isBlankAuditorRemark(String value) {
		return value == null || value.trim().isEmpty();
	}

	private boolean hasTextValue(String value) {
		return value != null && !value.trim().isEmpty();
	}

	private boolean isMeaningfulDate(Date date) {
		if (date == null) {
			return false;
		}
		return date.getTime() > 0L;
	}

	private boolean hasNumberValue(Number value) {
		return value != null;
	}

	/**
	 * Final Annexure A2 submit is allowed only when every section the applicant filled
	 * with real content also has non-blank auditor remarks. Catalog-only / empty shells
	 * are skipped.
	 */
	private boolean hasMissingAuditorRemarksForFilledSections(AnnexureMain amain) {
		Long annexureId = amain.getAnnexureMainId();

		AnnualAuditPeriodMain periodMain = periodMainServ.getByAnnexureId(annexureId);
		if (periodMain != null) {
			List<AnnualAuditPeriodDetails> list = periodServ
					.getByAnnualAuditPeriodMainId(periodMain.getPeriodMainId());
			boolean filled = list != null && list.stream().anyMatch(row -> isMeaningfulDate(row.getFromDate())
					|| isMeaningfulDate(row.getToDate()) || hasTextValue(row.getObservations()));
			if (filled && isBlankAuditorRemark(periodMain.getAuditorVerification())) {
				return true;
			}
		}

		InternalAuditMain internalMain = internalMainServ.getByAnnexureId(annexureId);
		if (internalMain != null) {
			List<InternalAuditDetails> list = internalServ
					.getByInternalAuditMainId(internalMain.getInAuditMainId());
			boolean filled = list != null && list.stream()
					.anyMatch(row -> isMeaningfulDate(row.getFromDate()) || isMeaningfulDate(row.getToDate())
							|| hasTextValue(row.getObservations()) || hasTextValue(row.getAuditorDetails()));
			if (filled && isBlankAuditorRemark(internalMain.getAuditorVerification())) {
				return true;
			}
		}

		EKYCMonthMain ekycMonthMain = monthMainServ.getByAnnexureId(annexureId);
		if (ekycMonthMain != null) {
			List<EKYCAcMonthDetails> list = monthDetailsServ
					.getByEKYCAcMonthMainId(ekycMonthMain.geteKYCMonthMainId());
			boolean filled = list != null && list.stream()
					.anyMatch(row -> isMeaningfulDate(row.getFromDate()) || isMeaningfulDate(row.getToDate())
							|| hasTextValue(row.getObservations()) || hasTextValue(row.getAuditorDetails()));
			if (filled && isBlankAuditorRemark(ekycMonthMain.getAuditorVerification())) {
				return true;
			}
		}

		EKYCAccountBasedMain ekycAccountMain = accountBasedServ.getByAnnexureId(annexureId);
		if (ekycAccountMain != null && isEkycAccountFilled(ekycAccountMain)
				&& isBlankAuditorRemark(ekycAccountMain.getAuditorVerification())) {
			return true;
		}

		RaAuditMain raAuditMain = raAuditServ.getByAnnexureId(annexureId);
		if (raAuditMain != null && (hasNumberValue(raAuditMain.getTotalRA())
				|| hasNumberValue(raAuditMain.getActiveRA()) || hasTextValue(raAuditMain.getDatesOfRAAudit())
				|| hasTextValue(raAuditMain.getNcReportedByRA()) || hasTextValue(raAuditMain.getCaActionTaken()))
				&& isBlankAuditorRemark(raAuditMain.getAuditorVerification())) {
			return true;
		}

		CourtCasesMain courtCasesMain = courtCasesServ.getByAnnexureId(annexureId);
		if (courtCasesMain != null
				&& (hasTextValue(courtCasesMain.getActiveCourtCases())
						|| hasTextValue(courtCasesMain.getCourtCasesCount())
						|| hasTextValue(courtCasesMain.getPoliceComplaintsCount()))
				&& isBlankAuditorRemark(courtCasesMain.getAuditorVerification())) {
			return true;
		}

		RevocationMain revocationMain = revocationServ.getByAnnexureId(annexureId);
		if (revocationMain != null
				&& (hasNumberValue(revocationMain.getDscRevokedCount())
						|| hasTextValue(revocationMain.getRevocReqReason())
						|| hasTextValue(revocationMain.getDscRevokedReason()))
				&& isBlankAuditorRemark(revocationMain.getAuditorVerification())) {
			return true;
		}

		CryptoTokenMain cryptoTokenMain = cryptoTokenMainServ.getByAnnexureId(annexureId);
		if (cryptoTokenMain != null) {
			List<CryptoTokenDetails> list = cryptoTokenDetailsServ
					.getByCryptoTokenMainId(cryptoTokenMain.getCryptoTokMainId());
			boolean filled = list != null && list.stream()
					.anyMatch(row -> hasTextValue(row.getBrandName()) || hasTextValue(row.getOemDetails())
							|| hasNumberValue(row.getMakInPercentage()) || isMeaningfulDate(row.getFipCertUpTo())
							|| hasTextValue(row.getSecAuditDetails()));
			if (filled && isBlankAuditorRemark(cryptoTokenMain.getAuditorVerification())) {
				return true;
			}
		}

		CaSwWebMain caSwWebMain = caSwWebMainServ.getByAnnexureId(annexureId);
		if (caSwWebMain != null) {
			List<CaSwWebDetails> list = caSwWebDetailsServ.getByCaSwWebMainId(caSwWebMain.getCaWebMainId());
			boolean filled = list != null && list.stream()
					.anyMatch(row -> hasTextValue(row.getDevelopedBy()) || hasTextValue(row.getDatabaseUsed())
							|| hasTextValue(row.getCertification())
							|| isMeaningfulDate(row.getLastSecurityAudit()));
			if (filled && isBlankAuditorRemark(caSwWebMain.getAuditorVerification())) {
				return true;
			}
		}

		LocationMain locationMain = locationMainServ.getByAnnexureId(annexureId);
		if (locationMain != null) {
			List<LocationDetails> list = locationDetailsServ.getByLocationMainId(locationMain.getLocationMainId());
			boolean filled = list != null && list.stream()
					.anyMatch(row -> hasTextValue(row.getLocation()) || hasTextValue(row.getCaAdministratorCount())
							|| hasTextValue(row.getSysAdministratorCount())
							|| hasTextValue(row.getCaOperatorsCount())
							|| hasTextValue(row.getVerificationOfficersCount())
							|| hasTextValue(row.getCaManpowerCount()));
			if (filled && isBlankAuditorRemark(locationMain.getAuditorVerification())) {
				return true;
			}
		}

		CAServicesMain caServicesMain = caServicesMainServ.getByAnnexureId(annexureId);
		if (caServicesMain != null) {
			List<CAServicesDetails> list = caServicesDetailsServ
					.getByCAServicesMainId(caServicesMain.getCaServicesMainId());
			boolean filled = list != null && list.stream()
					.anyMatch(row -> hasTextValue(row.getInternalOnly()) || hasTextValue(row.getExternalOnly())
							|| hasNumberValue(row.getAspOrgCount()) || hasTextValue(row.getAspOrgCountFile()));
			if (filled && isBlankAuditorRemark(caServicesMain.getAuditorVerification())) {
				return true;
			}
		}

		ASPDetails aspDetails = aspDetailsServ.getByAnnexureId(annexureId);
		if (aspDetails != null
				&& (hasTextValue(aspDetails.getAspCount()) || hasTextValue(aspDetails.getAspsAuditOverdueCount()))
				&& isBlankAuditorRemark(aspDetails.getAuditorVerification())) {
			return true;
		}

		PublicInfoMain publicInfoMain = publicInfoMainServ.getByAnnexureId(annexureId);
		if (publicInfoMain != null) {
			List<PublicInfoDetails> list = publicInfoDetailsServ
					.getByPublicInfoMainId(publicInfoMain.getPublicInfoMainId());
			boolean filled = list != null && list.stream().anyMatch(row -> hasTextValue(row.getWebLink()));
			if (filled && isBlankAuditorRemark(publicInfoMain.getAuditorVerification())) {
				return true;
			}
		}

		CertificateCost certificateCost = certificateCostServ.getByAnnexureId(annexureId);
		if (certificateCost != null
				&& (hasNumberValue(certificateCost.getAvgDscIssuMaintenanceCost())
						|| hasNumberValue(certificateCost.getAvgFeeChargedByCA())
						|| hasTextValue(certificateCost.getExplanationForCostMismatch()))
				&& isBlankAuditorRemark(certificateCost.getAuditorVerification())) {
			return true;
		}

		SelfAssessmentMain selfAssessmentMain = selfServ.getByAnnexureId(annexureId);
		if (selfAssessmentMain != null && isSelfAssessmentFilled(selfAssessmentMain)
				&& isBlankAuditorRemark(selfAssessmentMain.getAuditorVerification())) {
			return true;
		}

		ConnectivityMain connectivityMain = connectivityMainServ.getByAnnexureId(annexureId);
		if (connectivityMain != null) {
			List<ConnectivityDetails> list = connectivityDetailsServ
					.getByConnectivityMainId(connectivityMain.getConnectivityMainId());
			boolean filled = list != null && list.stream().anyMatch(row -> hasTextValue(row.getDescription()));
			if (filled && isBlankAuditorRemark(connectivityMain.getAuditorVerification())) {
				return true;
			}
		}

		DownTime downTime = downTimeServ.getByAnnexureId(annexureId);
		if (downTime != null
				&& (hasTextValue(downTime.getReasonAndMeasuresTaken()) || hasNumberValue(downTime.getDownTimeHour())
						|| hasNumberValue(downTime.getDownTimeMinute())
						|| hasNumberValue(downTime.getDownTimeSecond()))
				&& isBlankAuditorRemark(downTime.getAuditorVerification())) {
			return true;
		}

		CATrustedPersonMain trustedPersonMain = personMainServ.getByAnnexureId(annexureId);
		if (trustedPersonMain != null) {
			List<CATrustedPerson> list = personServ
					.getByCATrustedPersonMainId(trustedPersonMain.getPersonMainId());
			boolean filled = list != null && list.stream()
					.anyMatch(row -> hasTextValue(row.getFullName()) || hasTextValue(row.getDesignation())
							|| hasTextValue(row.getLocationOfPosting()) || hasTextValue(row.getRole())
							|| hasTextValue(row.getIdCardNo()) || hasTextValue(row.getMobileNo())
							|| hasTextValue(row.getIdentificationDetails())
							|| isMeaningfulDate(row.getEmployedSince())
							|| hasTextValue(row.getTrainingDetails())
							|| isMeaningfulDate(row.getLastBackVerDate()));
			if (filled && isBlankAuditorRemark(trustedPersonMain.getAuditorVerification())) {
				return true;
			}
		}

		return false;
	}

	private boolean isEkycAccountFilled(EKYCAccountBasedMain main) {
		return isMeaningfulDate(main.getApprovalDateOfflineAadhaar())
				|| isMeaningfulDate(main.getApprovalDateOnlineAadhar())
				|| isMeaningfulDate(main.getApprovalDatePAN())
				|| isMeaningfulDate(main.getApprovalDateCA())
				|| isMeaningfulDate(main.getApprovalDateOrganisational())
				|| isMeaningfulDate(main.getApprovalDateBanking())
				|| hasNumberValue(main.getOfflineAadhaarDSCCount())
				|| hasNumberValue(main.getOnlineAadharDSCCount())
				|| hasNumberValue(main.getPanDSCCount())
				|| hasNumberValue(main.getCaDSCCount())
				|| hasNumberValue(main.getOrganisationalDSCCount())
				|| hasNumberValue(main.getBankingDSCCount())
				|| isMeaningfulDate(main.getKuaLicenseDate())
				|| isMeaningfulDate(main.getPanServDetails())
				|| isMeaningfulDate(main.getGstServiceDetails())
				|| hasTextValue(main.getNameOfBanks())
				|| hasTextValue(main.getExtServOffAadhar())
				|| hasTextValue(main.getExtServOnAadhar())
				|| hasTextValue(main.getExtServPAN())
				|| hasTextValue(main.getExtServCA())
				|| hasTextValue(main.getExtServOrg())
				|| hasTextValue(main.getExtServBanking());
	}

	private boolean isSelfAssessmentFilled(SelfAssessmentMain main) {
		return hasNumberValue(main.getDscIssuedCountWAppForm()) || hasTextValue(main.getNcReason())
				|| hasNumberValue(main.getDscIssuedWFee()) || hasTextValue(main.getDetailsDSCIssuedWFee())
				|| hasNumberValue(main.getDscIssuedWPhysicalVer()) || hasNumberValue(main.getDscIssuedWMatch())
				|| hasTextValue(main.getNcReasonWithMatch()) || hasTextValue(main.getIsOTPSent())
				|| hasTextValue(main.getOtpReasonNC()) || hasTextValue(main.getCaSystemAccessDetails())
				|| hasTextValue(main.getOwnNCDetails());
	}

	
	
	@PostMapping("/uploadCATrustedPersonExcel")
	public ResponseEntity<?> uploadCATrustedPersonExcel(@RequestParam("file") MultipartFile file, @RequestParam("userName") String userName) {
		
		
		
	    try (InputStream inputStream = file.getInputStream()) {
	        Workbook workbook = WorkbookFactory.create(inputStream);
	        Sheet sheet = workbook.getSheetAt(0);
	        List<CATrustedPersonDTO> objDetails = new ArrayList<>();

	        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
	            Row row = sheet.getRow(i);
	            if (row == null) continue;

	            CATrustedPersonDTO obj = new CATrustedPersonDTO();
	            obj.setFullName(getCellValue(row.getCell(1)));
	            obj.setDesignation(getCellValue(row.getCell(2)));
	            obj.setLocationOfPosting(getCellValue(row.getCell(3)));
	            obj.setRole(getCellValue(row.getCell(4)));
	            obj.setIdCardNo(getCellValue(row.getCell(5)));
	            obj.setMobileNo(getCellValue(row.getCell(6)));
	            obj.setIdentificationDetails(getCellValue(row.getCell(7)));
	            Cell cell = row.getCell(8);
	            Date employedSince = null;

	            if (cell != null) {
	                if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
	                    employedSince = cell.getDateCellValue();
	                } else if (cell.getCellType() == CellType.STRING) {
	                    String dateStr = cell.getStringCellValue().trim();
	                    try {
	                        // Adjust format as per your expected input (e.g., dd/MM/yyyy)
	                        employedSince = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
	                    } catch (ParseException e) {
	                        // log or handle parse failure
	                    }
	                }
	            }

	            obj.setEmployedSince(employedSince);

	            obj.setTrainingDetails(getCellValue(row.getCell(9)));
	            Cell dateCell = row.getCell(10); // or whichever index you are reading
	            Date parsedDate = getDateFromCell(dateCell);
	            obj.setLastBackVerDate(parsedDate);

	            objDetails.add(obj);
	        }

	        // Validate each object
	        for (CATrustedPersonDTO obj : objDetails) {
	            String error = validateTrustedPersonDTO(obj);
	            if (error != null) {
	                return ResponseEntity.badRequest().body(error);
	            }
	        }

	        if (objDetails.isEmpty()) {
	            return new ResponseEntity<>("Excel data is empty or invalid.", HttpStatus.BAD_REQUEST);
	        }

	        String decryptedUser = EncryptionUtil.decrypt(userName);
	        if (decryptedUser != null) {
	            decryptedUser = decryptedUser.replace("\"", "").trim();
	        }
	        if (decryptedUser == null || decryptedUser.isEmpty()) {
	            return new ResponseEntity<>("Invalid user. Please try again.", HttpStatus.BAD_REQUEST);
	        }

	        AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", decryptedUser);
	        if (amain == null) {
	            AnnexureMain sobj = new AnnexureMain();
	            sobj.setCompleted(18);
	            sobj.setCreatedBy(userName);
	            sobj.setStatus("Active");
	            sobj.setUpdatedBy(userName);
	            sobj.setUserName(decryptedUser);
	            amain = annexServ.addAnnexureMain(sobj);
	        } else {
	            if (amain.getCompleted() == null || amain.getCompleted() < 18) {
	                amain.setCompleted(18);
	            }
	            amain.setUpdated(new Date());
	            amain = annexServ.addAnnexureMain(amain);
	        }

	        CATrustedPersonMain mainObj = personMainServ.getByAnnexureId(amain.getAnnexureMainId());
	        if (mainObj == null) {
	            mainObj = new CATrustedPersonMain();
	            mainObj.setAnnexureMainId(amain);
	            mainObj = personMainServ.addCATrustedPersonMain(mainObj);
	        }

	        for (CATrustedPersonDTO obj : objDetails) {
	            CATrustedPerson trustedPerson = null;

	            try {
	                if (obj.getPersonId() != null && !obj.getPersonId().isEmpty()) {
	                    Long decryptedId = Long.parseLong(EncryptionUtil.decrypt(obj.getPersonId()));
	                    trustedPerson = personServ.getByCATrustedPersonId(decryptedId);
	                }
	            } catch (Exception ignored) {
	                trustedPerson = null; // If error, treat as new
	            }

	            if (trustedPerson == null) {
	                // Create new
	                trustedPerson = new CATrustedPerson();
	                trustedPerson.setPersonMainId(mainObj);
	                trustedPerson.setCreated(new Date());
	            }

	            // Set common fields (update or new)
	            trustedPerson.setFullName(obj.getFullName());
	            trustedPerson.setDesignation(obj.getDesignation());
	            trustedPerson.setLocationOfPosting(obj.getLocationOfPosting());
	            trustedPerson.setRole(obj.getRole());
	            trustedPerson.setIdCardNo(obj.getIdCardNo());
	            trustedPerson.setMobileNo(obj.getMobileNo());
	            trustedPerson.setIdentificationDetails(obj.getIdentificationDetails());
	            trustedPerson.setEmployedSince(obj.getEmployedSince());
	            trustedPerson.setTrainingDetails(obj.getTrainingDetails());
	            trustedPerson.setLastBackVerDate(obj.getLastBackVerDate());
	            trustedPerson.setUpdated(new Date());

	            personServ.addCATrustedPerson(trustedPerson);
	        }

	        return new ResponseEntity<>("Trusted persons uploaded successfully.", HttpStatus.OK);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>("Something went wrong while uploading the Excel: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	
	private Date getDateFromCell(Cell cell) {
	    if (cell == null) return null;

	    try {
	        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
	            return cell.getDateCellValue(); // Excel-stored date
	        } else if (cell.getCellType() == CellType.STRING) {
	            String value = cell.getStringCellValue().trim();
	            // Parse only if it looks like a date in yyyy-MM-dd format
	            if (value.matches("\\d{4}-\\d{2}-\\d{2}")) {
	                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	                return sdf.parse(value);
	            } else {
	                System.out.println("Not a valid date string: " + value);
	            }
	        }
	    } catch (Exception e) {
	        System.out.println("Error parsing date: " + e.getMessage());
	    }

	    return null;
	}
	
	
	
	private String getCellValue(Cell cell) {
	    if (cell == null) return "";

	    switch (cell.getCellType()) {
	        case BLANK:
	            return "";
	        case BOOLEAN:
	            return String.valueOf(cell.getBooleanCellValue());
	        case ERROR:
	            return "ERROR";
	        case NUMERIC:
	            if (DateUtil.isCellDateFormatted(cell)) {
	                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	                return sdf.format(cell.getDateCellValue());
	            } else {
	                DecimalFormat df = new DecimalFormat("0.##");
	                return df.format(cell.getNumericCellValue());
	            }
	        case STRING:
	            return cell.getStringCellValue().trim();
	        case FORMULA:
	            FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
	            CellValue evaluatedValue = evaluator.evaluate(cell);
	            switch (evaluatedValue.getCellType()) {
	                case BOOLEAN:
	                    return String.valueOf(evaluatedValue.getBooleanValue());
	                case NUMERIC:
	                    DecimalFormat df = new DecimalFormat("0.##");
	                    return df.format(evaluatedValue.getNumberValue());
	                case STRING:
	                    return evaluatedValue.getStringValue().trim();
	                case BLANK:
	                    return "";
	                case ERROR:
	                    return "ERROR";
	                
	                default:
	                    return "";
	            }
	        case _NONE:
	        default:
	            return "";
	    }
	}


	private String validateTrustedPersonDTO(CATrustedPersonDTO obj) {
	    // Validation constants
	    final String FULL_NAME_BLANK = "Full name cannot be empty.";
	    final String FULL_NAME_LENGTH = "Full name must be between 3 and 100 characters.";
	    final String FULL_NAME_FORMAT = "Full name can only contain alphabets and spaces.";
	    final String DESIGNATION_BLANK = "Designation cannot be empty.";
	    final String DESIGNATION_LENGTH = "Designation must be between 3 and 50 characters.";
	    final String DESIGNATION_FORMAT = "Designation can only contain alphabets and spaces.";
	    final String LOCATION_OF_POSTING_BLANK = "Location of posting cannot be empty.";
	    final String LOCATION_OF_POSTING_LENGTH = "Location of posting must be between 3 and 100 characters.";
	    final String ROLE_BLANK = "Role cannot be empty.";
	    final String ROLE_LENGTH = "Role must be between 3 and 50 characters.";
	    final String ROLE_FORMAT = "Role can only contain alphabets and spaces.";
	    final String ID_CARD_NO_BLANK = "ID card number cannot be empty.";
	    final String ID_CARD_NO_LENGTH = "ID card number must be between 3 and 20 characters.";
	    final String ID_CARD_NO_FORMAT = "ID card number can only contain alphanumeric characters.";
	    final String MOBILE_NO_BLANK = "Mobile number cannot be empty.";
	    final String MOBILE_NO_FORMAT = "Mobile number must start with 6, 7, 8, or 9 and be exactly 10 digits.";
	    final String IDENTIFICATION_DETAILS_BLANK = "Identification details cannot be empty.";
	    final String IDENTIFICATION_DETAILS_LENGTH = "Identification details must be between 3 and 250 characters.";
	    final String EMPLOYED_SINCE_BLANK = "Employed since date cannot be empty.";
	    final String TRAINING_DETAILS_BLANK = "Training details cannot be empty.";
	    final String TRAINING_DETAILS_LENGTH = "Training details must be between 3 and 250 characters.";
	    final String LAST_BACK_VER_DATE_BLANK = "Last background verification date cannot be empty.";
	    final String STRING_VALIDATION_MSG = "Only alphabets, numbers, characters ().,&- are allowed";

	    // Validation patterns
	    Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z ]+$");
	    Pattern ID_CARD_PATTERN = Pattern.compile("^[A-Za-z0-9]+$");
	    Pattern MOBILE_PATTERN = Pattern.compile("^[6-9]{1}[0-9]{9}$");
	    Pattern STRING_PATTERN = Pattern.compile("^[A-Za-z0-9().,&\\- ]+$");
	    Pattern ROLE_PATTERN = Pattern.compile("^[A-Za-z ]+$");

	    // Validations
	    if (obj.getFullName() == null || obj.getFullName().trim().isEmpty()) return FULL_NAME_BLANK;
	    if (obj.getFullName().length() < 3 || obj.getFullName().length() > 100) return FULL_NAME_LENGTH;
	    if (!NAME_PATTERN.matcher(obj.getFullName()).matches()) return FULL_NAME_FORMAT;

	    if (obj.getDesignation() == null || obj.getDesignation().trim().isEmpty()) return DESIGNATION_BLANK;
	    if (obj.getDesignation().length() < 3 || obj.getDesignation().length() > 50) return DESIGNATION_LENGTH;
	    if (!NAME_PATTERN.matcher(obj.getDesignation()).matches()) return DESIGNATION_FORMAT;

	    if (obj.getLocationOfPosting() == null || obj.getLocationOfPosting().trim().isEmpty()) return LOCATION_OF_POSTING_BLANK;
	    if (obj.getLocationOfPosting().length() < 3 || obj.getLocationOfPosting().length() > 100) return LOCATION_OF_POSTING_LENGTH;
	    if (!STRING_PATTERN.matcher(obj.getLocationOfPosting()).matches()) return STRING_VALIDATION_MSG;

	    if (obj.getRole() == null || obj.getRole().trim().isEmpty()) return ROLE_BLANK;
	    if (obj.getRole().length() < 3 || obj.getRole().length() > 50) return ROLE_LENGTH;
	    if (!ROLE_PATTERN.matcher(obj.getRole()).matches()) return ROLE_FORMAT;

	    if (obj.getIdCardNo() == null || obj.getIdCardNo().trim().isEmpty()) return ID_CARD_NO_BLANK;
	    if (obj.getIdCardNo().length() < 3 || obj.getIdCardNo().length() > 20) return ID_CARD_NO_LENGTH;
	    if (!ID_CARD_PATTERN.matcher(obj.getIdCardNo()).matches()) return ID_CARD_NO_FORMAT;

	    if (obj.getMobileNo() == null || obj.getMobileNo().trim().isEmpty()) return MOBILE_NO_BLANK;
	    if (!MOBILE_PATTERN.matcher(obj.getMobileNo()).matches()) return MOBILE_NO_FORMAT;

	    if (obj.getIdentificationDetails() == null || obj.getIdentificationDetails().trim().isEmpty()) return IDENTIFICATION_DETAILS_BLANK;
	    if (obj.getIdentificationDetails().length() < 3 || obj.getIdentificationDetails().length() > 250) return IDENTIFICATION_DETAILS_LENGTH;
	    if (!STRING_PATTERN.matcher(obj.getIdentificationDetails()).matches()) return STRING_VALIDATION_MSG;

	    if (obj.getEmployedSince() == null) return EMPLOYED_SINCE_BLANK;

	    if (obj.getTrainingDetails() == null || obj.getTrainingDetails().trim().isEmpty()) return TRAINING_DETAILS_BLANK;
	    if (obj.getTrainingDetails().length() < 3 || obj.getTrainingDetails().length() > 250) return TRAINING_DETAILS_LENGTH;
	    if (!STRING_PATTERN.matcher(obj.getTrainingDetails()).matches()) return STRING_VALIDATION_MSG;

	    if (obj.getLastBackVerDate() == null) return LAST_BACK_VER_DATE_BLANK;

	    return null; 
	}


	
	
}
