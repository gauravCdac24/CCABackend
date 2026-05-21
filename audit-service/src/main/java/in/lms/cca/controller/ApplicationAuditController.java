package in.lms.cca.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.StackWalker.Option;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.multipart.MultipartFile;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.exceptions.PdfException;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.AffineTransform;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.ReaderProperties;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.kernel.utils.PdfMerger;

import in.lms.cca.dto.AuditReportCriteriaDTO;
import in.lms.cca.dto.AuditRequestDTO;
import in.lms.cca.entity.ApplicantActionTakenEntity;
import in.lms.cca.entity.ApplicantShortcomingsReportEntity;
import in.lms.cca.entity.ApplicationAuditEntity;
import in.lms.cca.entity.ApplicationAuditorsEntity;
import in.lms.cca.entity.AuditAgencySelectionEntity;
import in.lms.cca.entity.AuditControlEntity;
import in.lms.cca.entity.AuditNCSEntity;
import in.lms.cca.entity.AuditReportCriteriaEntity;
import in.lms.cca.entity.AuditReportDocumentEntity;
import in.lms.cca.entity.AuditReviewReportEntity;
import in.lms.cca.entity.AuditScheduleEntity;
import in.lms.cca.entity.AuditShortComingsEntity;
import in.lms.cca.entity.ESignedDocumentsEntity;
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
import in.lms.cca.payload.AuditAgencyFormDto;
import in.lms.cca.payload.AuditControlPayload;
import in.lms.cca.payload.AuditCriteriaPayload;
import in.lms.cca.payload.AuditParameterPayload;
import in.lms.cca.payload.AuditReportDocumentPayload;
import in.lms.cca.payload.AuditSubCriteriaPayload;
import in.lms.cca.payload.AuditorControlsNC;
import in.lms.cca.payload.NotificationsRequest;
import in.lms.cca.payload.UserLoginDTO;
import in.lms.cca.service.ApplicantShortcomingsReportService;
import in.lms.cca.service.ApplicationActionTakenService;
import in.lms.cca.service.ApplicationAuditService;
import in.lms.cca.service.ApplicationAuditorsService;
import in.lms.cca.service.AuditAgencySelectionService;
import in.lms.cca.service.AuditNCSService;
import in.lms.cca.service.AuditReportCriteriaService;
import in.lms.cca.service.AuditReportDocumentService;
import in.lms.cca.service.AuditReviewReportService;
import in.lms.cca.service.AuditScheduleService;
import in.lms.cca.service.AuditService;
import in.lms.cca.service.AuditShortComingsService;
import in.lms.cca.service.EsignedDocumentsService;
import in.lms.cca.service.Impl.NotificationClientServiceImpl;
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
import in.lms.cca.util.api.AuditServiceAPIs;
import in.lms.cca.util.global.Constant;
import in.lms.cca.util.global.EncryptionUtil;
import in.lms.cca.util.golbal.DocumentFileUtil;

@RestController
@CrossOrigin
@RequestMapping(AuditServiceAPIs.AUDIT_SERVICE_BASE_URL)
public class ApplicationAuditController {

	@Autowired
	private ApplicationAuditService applicationAuditServ;

	@Autowired
	private ApplicationAuditorsService applicationAuditorsService;

	@Autowired
	private AuditScheduleService auditScheduleService;

	@Autowired
	private AuditReportCriteriaService auditReportCriteriaService;

	@Autowired
	private NotificationClientServiceImpl notificationServ;

	@Autowired
	private AuditNCSService auditNCSService;

	@Autowired
	private AuditReportDocumentService auditReportDocumentService;

	@Autowired
	private AuditAgencySelectionService selectionServ;

	@Autowired
	private AuditShortComingsService auditShortComingsService;

	@Autowired
	private ApplicantShortcomingsReportService applicantShortcomingsReportService;

	@Autowired
	private AuditService auditService;

	@Autowired
	private ApplicationActionTakenService actionTakenService;

	@Autowired
	private EsignedDocumentsService documentsService;

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
	
	@Autowired
	private AuditReviewReportService auditReviewReportService;
	
	

	@PostMapping(value = "/submit")
	public ResponseEntity<?> submitAuditForm(@ModelAttribute AuditAgencyFormDto auditAgencyFormDto) {
		try {
			String applicantUserName = auditAgencyFormDto.getApplicantUserName();
			if (applicantUserName == null || applicantUserName.trim().isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Applicant username is required.");
			}
			applicantUserName = applicantUserName.trim();

			if (auditAgencyFormDto.getUndertakingFile() == null
					|| auditAgencyFormDto.getUndertakingFile().isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload undertaking file.");
			}

			List<AuditAgencyFormDto.AuditorDescription> auditorDescriptions = auditAgencyFormDto
					.getAuditorDesscription();
			if (auditorDescriptions == null || auditorDescriptions.isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please add at least one auditor.");
			}

			for (int i = 0; i < auditorDescriptions.size(); i++) {
				AuditAgencyFormDto.AuditorDescription auditorDescription = auditorDescriptions.get(i);
				if (auditorDescription.getAuditorName() == null
						|| auditorDescription.getAuditorName().trim().isEmpty()) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body("Auditor name is required for row " + (i + 1) + ".");
				}
				if (auditorDescription.getCertificateType() == null
						|| auditorDescription.getCertificateType().trim().isEmpty()) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body("Certificate type is required for row " + (i + 1) + ".");
				}
				if (auditorDescription.getFile() == null || auditorDescription.getFile().isEmpty()) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body("Certificate file is required for row " + (i + 1) + ".");
				}
			}

			List<AuditAgencyFormDto.AuditScope> auditScopes = auditAgencyFormDto.getAuditScope();
			if (auditScopes == null || auditScopes.isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please add at least one audit schedule.");
			}

			for (int i = 0; i < auditScopes.size(); i++) {
				AuditAgencyFormDto.AuditScope auditScope = auditScopes.get(i);
				if (auditScope.getAuditTitle() == null || auditScope.getAuditTitle().trim().isEmpty()) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body("Audit title is required for schedule row " + (i + 1) + ".");
				}
				if (auditScope.getDescription() == null || auditScope.getDescription().trim().isEmpty()) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body("Description is required for schedule row " + (i + 1) + ".");
				}
				if (auditScope.getStartDate() == null || auditScope.getStartDate().trim().isEmpty()) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body("Start date is required for schedule row " + (i + 1) + ".");
				}
				if (auditScope.getEndDate() == null || auditScope.getEndDate().trim().isEmpty()) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body("End date is required for schedule row " + (i + 1) + ".");
				}
			}

			Random rand = new Random();
			Integer rnum = rand.nextInt(1000);

			ApplicationAuditEntity applicationAuditEntity = applicationAuditServ
					.getByApplicantUserName(applicantUserName);

			if (applicationAuditEntity == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Application not found for username: " + applicantUserName);
			}

			List<ApplicationAuditorsEntity> applicationAuditorsEntities = applicationAuditorsService
					.getAllDataByAuditId(applicationAuditEntity.getAppAuditId());

			for (ApplicationAuditorsEntity auditorsEntity : applicationAuditorsEntities) {
				auditorsEntity.setStatus("Inactive");
				auditorsEntity.setUpdated(new Date());
				applicationAuditorsService.updateData(auditorsEntity);
			}

			List<AuditScheduleEntity> existingSchedules = auditScheduleService
					.getAllDataByAuditId(applicationAuditEntity.getAppAuditId());
			for (AuditScheduleEntity scheduleEntity : existingSchedules) {
				scheduleEntity.setStatus("Inactive");
				scheduleEntity.setUpdated(new Date());
				auditScheduleService.updateData(scheduleEntity);
			}

			String undertakingFilename = DocumentFileUtil.saveFile(auditAgencyFormDto.getUndertakingFile(),
					"AuditorUndertaking", rnum.toString(), "AuditorUndertaking");

			for (AuditAgencyFormDto.AuditorDescription auditorDescription : auditorDescriptions) {
				String filename = DocumentFileUtil.saveFile(auditorDescription.getFile(), "AuditorDecription",
						rnum.toString(), "AuditorDecription");

				ApplicationAuditorsEntity auditorEntity = new ApplicationAuditorsEntity();
				auditorEntity.setFullName(auditorDescription.getAuditorName().trim());
				auditorEntity.setCertificationType(auditorDescription.getCertificateType().trim());
				auditorEntity.setCertificateDocument(filename);
				auditorEntity.setAppAuditId(applicationAuditEntity);
				auditorEntity.setUndertakingDocument(undertakingFilename);
				auditorEntity.setStatus("Active");

				applicationAuditorsService.processAuditForm(auditorEntity);
			}

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			for (AuditAgencyFormDto.AuditScope auditScope : auditScopes) {
				AuditScheduleEntity auditScheduleEntity = new AuditScheduleEntity();
				auditScheduleEntity.setAuditType(auditScope.getAuditTitle().trim());
				auditScheduleEntity.setDescription(auditScope.getDescription().trim());
				auditScheduleEntity.setAppAuditId(applicationAuditEntity);
				auditScheduleEntity.setStatus("Active");
				auditScheduleEntity.setStartDate(dateFormat.parse(auditScope.getStartDate().trim()));
				auditScheduleEntity.setEndDate(dateFormat.parse(auditScope.getEndDate().trim()));
				auditScheduleService.saveData(auditScheduleEntity);
			}

			try {
				applicationAuditServ.changeIntentStatusByUserName(applicantUserName);
			} catch (Exception statusEx) {
				statusEx.printStackTrace();
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
						"Auditor details saved but failed to update application status. Please contact support.");
			}

			return ResponseEntity.ok("Form submitted successfully");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error processing form: " + e.getMessage());
		}
	}

	@GetMapping("/getAuditForm")
	public ResponseEntity<AuditAgencyFormDto> getAuditForm(@RequestParam("userName") String applicantUserName) {
		// Retrieve ApplicationAuditEntity based on applicantUserName
		ApplicationAuditEntity applicationAuditEntity = applicationAuditServ.getByApplicantUserName(applicantUserName);

		// Create DTO to hold the response
		AuditAgencyFormDto auditAgencyFormDto = new AuditAgencyFormDto();
		// auditAgencyFormDto.setAuditAgencyName(applicationAuditEntity.getAuditAgencyName());
		auditAgencyFormDto.setApplicantUserName(applicationAuditEntity.getApplicantUserName());
		// auditAgencyFormDto.setIntentAppId(applicationAuditEntity.getIntentAppId());

		// Retrieve ApplicationAuditorsEntity and map to DTO
		List<ApplicationAuditorsEntity> applicationAuditorsEntities = applicationAuditorsService
				.getAllDataByAuditId(applicationAuditEntity.getAppAuditId());
		List<AuditAgencyFormDto.AuditorDescription> auditorDescriptions = applicationAuditorsEntities.stream()
				.map(entity -> {
					AuditAgencyFormDto.AuditorDescription description = new AuditAgencyFormDto.AuditorDescription();
					description.setAuditorDescriptionId(String.valueOf(entity.getAppAuditorId()));
					description.setAuditorName(entity.getFullName());
					description.setCertificateType(entity.getCertificationType());
					description.setCertificateName(entity.getCertificateDocument());
					auditAgencyFormDto.setUndertakingFileName(entity.getUndertakingDocument());
					Date certificateExpiryDate = entity.getCertificateExpiry(); // Get the Date from entity

					if (certificateExpiryDate != null) {
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Adjust format as needed
						String formattedDate = dateFormat.format(certificateExpiryDate);
						description.setCertificateExpiry(formattedDate);
					} else {

						description.setCertificateExpiry(null);
					}

					return description;
				}).toList();
		auditAgencyFormDto.setAuditorDesscription(auditorDescriptions);

		// Retrieve AuditScheduleEntity and map to DTO
		List<AuditScheduleEntity> auditScheduleEntities = auditScheduleService
				.getAllDataByAuditId(applicationAuditEntity.getAppAuditId());
		List<AuditAgencyFormDto.AuditScope> auditScopes = auditScheduleEntities.stream().map(entity -> {
			AuditAgencyFormDto.AuditScope scope = new AuditAgencyFormDto.AuditScope();
			scope.setAuditScopeId(String.valueOf(entity.getAuditScheduleId()));
			scope.setAuditTitle(entity.getAuditType());
			scope.setDescription(entity.getDescription());
			scope.setStartDate(entity.getStartDate().toString()); // Convert to String if needed
			scope.setEndDate(entity.getEndDate().toString()); // Convert to String if needed
			return scope;
		}).toList();
		auditAgencyFormDto.setAuditScope(auditScopes);

		return ResponseEntity.ok(auditAgencyFormDto);
	}

	@GetMapping("/downloadFile")
	public ResponseEntity<Resource> viewFile(@RequestParam("id") String cpsDocuId) {
		System.out.println(cpsDocuId);
		String id = EncryptionUtil.decrypt(cpsDocuId);

		System.out.println(id);
		try {
			Optional<ApplicationAuditorsEntity> cpsDocumentOpt = applicationAuditorsService
					.downloadfile(Long.parseLong(id));

			if (cpsDocumentOpt.isPresent()) {
				ApplicationAuditorsEntity c = cpsDocumentOpt.get();
				System.out.println(c.getCertificateDocument());
				Path filePath = Paths.get(Constant.REAL_PATH + "//AuditorDecription")
						.resolve(c.getCertificateDocument()).normalize();
				System.out.println(filePath);
				Resource resource = new UrlResource(filePath.toUri());
				System.out.println(resource);
				if (resource.exists()) {
					String contentType = Files.probeContentType(filePath);
					System.out.println(contentType);
					return ResponseEntity.ok()
							.contentType(MediaType
									.parseMediaType(contentType != null ? contentType : "application/octet-stream"))
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

	@GetMapping("/downloadFiles")
	public ResponseEntity<Resource> viewFiles(@RequestParam("id") String cpsDocuId) {
		System.out.println(cpsDocuId);
		String id = EncryptionUtil.decrypt(cpsDocuId);

		System.out.println(id);
		try {
			Optional<ApplicationAuditorsEntity> cpsDocumentOpt = applicationAuditorsService.downloadfiles(id);

			if (cpsDocumentOpt.isPresent()) {
				ApplicationAuditorsEntity c = cpsDocumentOpt.get();
				System.out.println(c.getUndertakingDocument());
				Path filePath = Paths.get(Constant.REAL_PATH + "//AuditorUndertaking")
						.resolve(c.getUndertakingDocument()).normalize();
				System.out.println(filePath);
				Resource resource = new UrlResource(filePath.toUri());
				System.out.println(resource);
				if (resource.exists()) {
					String contentType = Files.probeContentType(filePath);
					System.out.println(contentType);
					return ResponseEntity.ok()
							.contentType(MediaType
									.parseMediaType(contentType != null ? contentType : "application/octet-stream"))
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

	@PostMapping(value = "/approveSubmit")
	public ResponseEntity<?> approveAuditForm(@ModelAttribute AuditAgencyFormDto auditAgencyFormDto) {
		try {

			for (AuditAgencyFormDto.AuditorDescription auditorDescriptions : auditAgencyFormDto
					.getAuditorDesscription()) {
				Optional<ApplicationAuditorsEntity> auditorsOptional = applicationAuditorsService
						.downloadfile(Long.valueOf(auditorDescriptions.getAuditorDescriptionId()));
				ApplicationAuditorsEntity auditorDescriptiones = auditorsOptional.get();
				auditorDescriptiones.setRemarks(auditorDescriptions.getRemarks());

				applicationAuditorsService.aprovedAuditForm(auditorDescriptiones);
			}

			applicationAuditServ.approvedByUserName(auditAgencyFormDto.getApplicantUserName());

			// Return success response
			return ResponseEntity.ok("Form Aproved successfully");

		} catch (Exception e) {
			// Handle any errors
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error processing form: " + e.getMessage());
		}
	}

	@PostMapping(value = "/rejectSubmit")
	public ResponseEntity<?> rejectAuditForm(@ModelAttribute AuditAgencyFormDto auditAgencyFormDto) {
		try {

			for (AuditAgencyFormDto.AuditorDescription auditorDescriptions : auditAgencyFormDto
					.getAuditorDesscription()) {
				Optional<ApplicationAuditorsEntity> auditorsOptional = applicationAuditorsService
						.downloadfile(Long.valueOf(auditorDescriptions.getAuditorDescriptionId()));
				ApplicationAuditorsEntity auditorDescriptiones = auditorsOptional.get();
				auditorDescriptiones.setRemarks(auditorDescriptions.getRemarks());
				auditorDescriptiones.setStatus("Inactive");

				applicationAuditorsService.aprovedAuditForm(auditorDescriptiones);
			}

			// Iterate through audit scopes and save them individually
			for (AuditAgencyFormDto.AuditScope auditScope : auditAgencyFormDto.getAuditScope()) {
				Optional<AuditScheduleEntity> auditScheduleOptional = auditScheduleService
						.getAllData(Long.valueOf(auditScope.getAuditScopeId()));
				AuditScheduleEntity auditScheduleEntity = auditScheduleOptional.get();
				auditScheduleEntity.setStatus("Inactive");
				auditScheduleService.aprovedData(auditScheduleEntity);
			}

			// Save the auditor data

			applicationAuditServ.rejectedByUserName(auditAgencyFormDto.getApplicantUserName());

			// Return success response
			return ResponseEntity.ok("Form Rejected successfully");

		} catch (Exception e) {
			// Handle any errors
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error processing form: " + e.getMessage());
		}
	}

	@PostMapping(value = "/auditor-nc-form")
	public ResponseEntity<?> submitAuditNCForm(@ModelAttribute AuditorControlsNC auditorControlsNC) {
		Random rand = new Random();
		Integer rnum = rand.nextInt(1000);

		try {
			// Validate mandatory fields
			if (auditorControlsNC.getApplicantUserName() == null || auditorControlsNC.getFile() == null) {
				return ResponseEntity.badRequest().body("Applicant User Name and File are required.");
			}

			ApplicationAuditEntity applicationAuditEntity = applicationAuditServ
					.getByApplicantUserName(auditorControlsNC.getApplicantUserName());

			if (applicationAuditEntity == null) {
				return ResponseEntity.badRequest().body("Applicant not found.");
			}
			String controlId = auditorControlsNC.getControlId();
			AuditReportCriteriaEntity auditReportCriteriaEntity = auditReportCriteriaService
					.getAllDataByControlId(controlId, applicationAuditEntity);

			if (auditReportCriteriaEntity == null) {

				return saveNewAuditReportCriteria(auditorControlsNC, applicationAuditEntity, rnum);
			} else {

				return updateAuditReportCriteria(auditorControlsNC, auditReportCriteriaEntity, rnum);
			}
		} catch (IOException e) {
			// Log the error
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save file or data.");
		} catch (Exception e) {
			// Catch other exceptions and log them
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred.");
		}
	}

	private ResponseEntity<?> saveNewAuditReportCriteria(AuditorControlsNC auditorControlsNC,
			ApplicationAuditEntity applicationAuditEntity, Integer rnum) throws IOException {
		String filename = DocumentFileUtil.saveFile(auditorControlsNC.getFile(), "AuditorNCFile", rnum.toString(),
				"AuditorNCFile");

		AuditReportCriteriaEntity newEntity = new AuditReportCriteriaEntity();
		newEntity.setDocument(filename);
		newEntity.setAppAuditId(applicationAuditEntity);
		newEntity.setAuditControlId(auditorControlsNC.getControlId());
		newEntity.setRemarks(auditorControlsNC.getRemarks());
		newEntity.setCompliance(auditorControlsNC.getCompliance());
		newEntity.setStatus("Active");

		Optional<AuditReportCriteriaEntity> savedEntity = auditReportCriteriaService.saveData(newEntity);

		if (!savedEntity.isPresent()) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to save audit report criteria.");
		}

		return ResponseEntity.ok("Data submitted successfully.");
	}

	private ResponseEntity<?> updateAuditReportCriteria(AuditorControlsNC auditorControlsNC,
			AuditReportCriteriaEntity existingEntity, Integer rnum) throws IOException {
		// Delete the old file
		DocumentFileUtil.deleteFile(auditorControlsNC.getSelectedMultipartFile(), "AuditorNCFile");

		String newFilename = DocumentFileUtil.saveFile(auditorControlsNC.getFile(), "AuditorNCFile", rnum.toString(),
				"AuditorNCFile");

		existingEntity.setDocument(newFilename);
		existingEntity.setAuditControlId(auditorControlsNC.getControlId());
		existingEntity.setCompliance(auditorControlsNC.getCompliance());
		existingEntity.setRemarks(auditorControlsNC.getRemarks());
		existingEntity.setStatus("Active");
		existingEntity.setUpdated(new Date());

		Optional<AuditReportCriteriaEntity> updatedEntity = auditReportCriteriaService.updateData(existingEntity);

		if (!updatedEntity.isPresent()) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to update audit report criteria.");
		}

		return ResponseEntity.ok("Data updated successfully.");
	}

	@GetMapping("/criteriaId")
	public ResponseEntity<?> getByCriteriaId(@RequestParam("id") Long criteriaId) {
		try {
			// Fetch the entity based on the criteriaId
			Optional<AuditReportCriteriaEntity> entity = auditReportCriteriaService.findById(criteriaId);

			// Check if the entity exists
			if (entity.isPresent()) {
				return ResponseEntity.ok(entity.get());
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Criteria not found with ID: " + criteriaId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while retrieving the data.");
		}
	}

	@GetMapping("/get-all-criteria")
	public ResponseEntity<?> getAllCriteria() {
		try {
			// Fetch all criteria
			List<AuditReportCriteriaEntity> entities = auditReportCriteriaService.getAllData();

			// Check if the list is empty
			if (entities.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No criteria found.");
			} else {
				return ResponseEntity.ok(entities);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while retrieving the data.");
		}
	}

	@GetMapping("/document-name")
	public ResponseEntity<?> getByDocumentName(@RequestParam("documentName") String documentName) {
		try {
			Optional<AuditReportCriteriaEntity> cpsDocumentOpt = auditReportCriteriaService
					.downloadfileBydDocumentName(EncryptionUtil.decrypt(documentName));

			if (cpsDocumentOpt.isPresent()) {
				AuditReportCriteriaEntity c = cpsDocumentOpt.get();
				System.out.println(c.getDocument());
				Path filePath = Paths.get(Constant.REAL_PATH + "//AuditorNCFile").resolve(c.getDocument()).normalize();
				System.out.println(filePath);
				Resource resource = new UrlResource(filePath.toUri());
				System.out.println(resource);
				if (resource.exists()) {
					String contentType = Files.probeContentType(filePath);
					System.out.println(contentType);
					return ResponseEntity.ok()
							.contentType(MediaType
									.parseMediaType(contentType != null ? contentType : "application/octet-stream"))
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

	@PostMapping("/remarks-applicant-UserName")
	public ResponseEntity<?> getByApplicantUserName(@RequestParam("applicantUserName") String applicantUserName,
			@RequestParam("remarks") String remarks) {

		try {
			// Fetch the application audit entity
			ApplicationAuditEntity applicationAuditEntity = applicationAuditServ
					.getByApplicantUserName(applicantUserName);
			if (applicationAuditEntity == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Application audit entity not found for username: " + applicantUserName);
			}

			// Update remarks and timestamp
			applicationAuditEntity.setApplicantRemarks(remarks);
			applicationAuditEntity.setUpdated(new Date());
			applicationAuditServ.updateAuditor(applicationAuditEntity);

			// Retrieve audit report criteria
			List<AuditReportCriteriaEntity> auditReportCriteriaEntities = auditReportCriteriaService
					.findByAuditId(applicationAuditEntity.getAppAuditId());

			// Send notification to the applicant
			auditReportCriteriaService.sendTheApplicantUserName(applicantUserName);

			// Construct the notification
			String title = "NC Raised By Auditor";
			String message = "Dear Applicant, Some NC Generated Regarding Your Application. Please Check and Take Action: "
					+ remarks;

			NotificationsRequest notification = new NotificationsRequest();
			notification.setUsername(applicantUserName);
			notification.setMessage(message);
			notification.setSubject(title);
			notification.setNotificationType("Email & Dashboard");
			notification.setNotificationDescription("NC Generated By Auditor");
			notification.setRole("ROLE_APPLICANT");

			notificationServ.sendNotification(notification);

			return ResponseEntity.ok("Remarks updated and notification sent successfully.");

		} catch (Exception ex) {
			// Log and handle the error
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while processing the request: " + ex.getMessage());
		}
	}

	@GetMapping("/get-all-remarks")
	public ResponseEntity<?> getAllByApplicantUserName(@RequestParam("applicantUserName") String applicantUserName) {

		try {
			// Fetch the application audit entity
			ApplicationAuditEntity applicationAuditEntity = applicationAuditServ
					.getByApplicantUserName(applicantUserName);
			if (applicationAuditEntity == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Application audit entity not found for username: " + applicantUserName);
			}

			// Return the remarks list
			return ResponseEntity.ok(applicationAuditEntity);

		} catch (Exception ex) {
			// Log and return an error response
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while fetching remarks: " + ex.getMessage());
		}
	}

	@PostMapping("/applicant-remarks-by-applicant-UserName")
	public ResponseEntity<?> getApplicantRemarksByApplicantUserName(
			@RequestParam("applicantUserName") String applicantUserName, @RequestParam("remarks") String remarks,
			@RequestParam("file") MultipartFile file, @RequestParam("controlId") String auditControlId) {
		try {

			if (applicantUserName == null || applicantUserName.isEmpty() || auditControlId == null
					|| auditControlId.isEmpty()) {
				return ResponseEntity.badRequest().body("Invalid input parameters");
			}

			int randomNum = new Random().nextInt(1000);
			String filename = DocumentFileUtil.saveFile(file, "ApplicantActionTakenFile", String.valueOf(randomNum),
					"ApplicantActionTakenFile");

			AuditReportCriteriaEntity reportCriteriaEntity = resolveReportCriteriaForApplicant(applicantUserName,
					auditControlId);

			if (reportCriteriaEntity == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("No matching audit report found for control ID: " + auditControlId);
			}

			ApplicantActionTakenEntity existingActionEntity = actionTakenService
					.getAllByReportCriteria(reportCriteriaEntity);
			ApplicantActionTakenEntity actionTakenEntity = new ApplicantActionTakenEntity();

			if (existingActionEntity != null) {
				actionTakenEntity.setActionTakenId(existingActionEntity.getActionTakenId());
				actionTakenEntity.setCreated(existingActionEntity.getCreated());
				actionTakenEntity.setUpdated(new Date());
			} else {
				actionTakenEntity.setCreated(new Date());
			}

			actionTakenEntity.setActionReport(filename);
			actionTakenEntity.setRemarks(remarks);
			actionTakenEntity.setStatus("Active");
			actionTakenEntity.setUserName(applicantUserName);
			actionTakenEntity.setAuditReportCriteriaEntity(reportCriteriaEntity);

			if (existingActionEntity != null) {
				actionTakenService.updateData(actionTakenEntity);
			} else {
				actionTakenService.saveData(actionTakenEntity);
			}

			AuditAgencySelectionEntity agencySelectionEntity = selectionServ
					.findbyAuditId(reportCriteriaEntity.getAppAuditId());
			if (agencySelectionEntity == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Audit agency selection entity not found for audit ID: "
								+ reportCriteriaEntity.getAppAuditId());
			}

			UserLoginDTO userLoginDTO = auditReportCriteriaService
					.findbyAgencyId(agencySelectionEntity.getAuditAgencyId());
			if (userLoginDTO == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
						"User login details not found for agency ID: " + agencySelectionEntity.getAuditAgencyId());
			}

			NotificationsRequest notification = new NotificationsRequest();
			notification.setUsername(userLoginDTO.getUserName());
			notification.setMessage(
					"Dear Sir, We have attached the document and written our remarks. Please check and review it: "
							+ remarks);
			notification.setSubject("NC Action Taken By Applicant");
			notification.setNotificationType("Email & Dashboard");
			notification.setNotificationDescription("NC Action Taken By Applicant");
			notification.setRole("ROLE_AUDIT_AGENCY");

			// notificationServ.sendNotification(notification);

//	       if(isRejected==true)
//	       {
//	        auditReportCriteriaService.changedTheApplicantUserName(applicantUserName);
//	       }
			return ResponseEntity.ok("Remarks updated and notification sent successfully.");
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while processing the request: " + ex.getMessage());
		}
	}

	@PostMapping("/change-the-status-by-applicant-UserName")
	public ResponseEntity<?> getApplicantRemarksByApplicantUserName(
			@RequestParam("applicantUserName") String applicantUserName) {
		try {

			System.out.println(applicantUserName);

			auditReportCriteriaService.changedTheApplicantUserName(applicantUserName);

			return ResponseEntity.ok("Remarks updated and notification sent successfully.");
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while processing the request: " + ex.getMessage());
		}
	}

	@GetMapping("/get-All-Applicant-Action-Taken-details")
	public ResponseEntity<?> getByApplicationActionTaken(@RequestParam("applicantUserName") String applicantUserName) {

		try {

			List<ApplicantActionTakenEntity> actionTakenEntity = actionTakenService
					.getAllApplicationTaken(applicantUserName);

			if (actionTakenEntity == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Application audit entity not found for username: " + applicantUserName);
			}

			return ResponseEntity.ok(actionTakenEntity);

		} catch (Exception ex) {

			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("An error occurred while fetching remarks: " + ex.getMessage());
		}
	}

	@GetMapping("/get-All-AuditNC-details")
	public ResponseEntity<?> getByApplicantUserName(@RequestParam("applicantUserName") String applicantUserName) {

		System.out.println("applicantUserName====>" + applicantUserName);
		try {
			// Fetch the application audit entity
			ApplicationAuditEntity applicationAuditEntity = applicationAuditServ
					.getByApplicantUserName(applicantUserName);

			AuditNCSEntity auditNCSEntity = auditNCSService.findByAuditId(applicationAuditEntity.getAppAuditId());
			if (applicationAuditEntity == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Application audit entity not found for username: " + applicantUserName);
			}

			// Return the remarks list
			return ResponseEntity.ok(auditNCSEntity);

		} catch (Exception ex) {
			// Log and return an error response
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while fetching remarks: " + ex.getMessage());
		}
	}

	@GetMapping("/applicant-document-name")
	public ResponseEntity<?> getByApplicantDocumentName(@RequestParam("documentName") String documentName) {
		try {

			System.out.println("=-=-=-=-=-=->" + EncryptionUtil.decrypt(documentName));

			Optional<AuditNCSEntity> cpsDocumentOpt = auditNCSService
					.downloadfileBydDocumentName(EncryptionUtil.decrypt(documentName));

			if (cpsDocumentOpt.isPresent()) {
				AuditNCSEntity c = cpsDocumentOpt.get();
				System.out.println(c.getNcs());
				Path filePath = Paths.get(Constant.REAL_PATH + "//ApplicantNCActionFile").resolve(c.getNcs())
						.normalize();
				System.out.println(filePath);
				Resource resource = new UrlResource(filePath.toUri());
				System.out.println(resource);
				if (resource.exists()) {
					String contentType = Files.probeContentType(filePath);
					System.out.println(contentType);
					return ResponseEntity.ok()
							.contentType(MediaType
									.parseMediaType(contentType != null ? contentType : "application/octet-stream"))
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

	@PostMapping("/auditor-remarks-by-applicant-UserName")
	public ResponseEntity<?> getAuditorRemarksByApplicantUserName(
			@RequestParam("applicantUserName") String applicantUserName, @RequestParam("userName") String userName,
			@RequestParam("remarks") String remarks, @RequestParam("file") MultipartFile file) {

		System.out.println("=== auditor-remarks-by-applicant-UserName ===");
		System.out.println("applicantUserName: " + applicantUserName);
		System.out.println("userName: " + userName);
		System.out.println("remarks: " + remarks);
		System.out.println("file: " + (file != null ? file.getOriginalFilename() : "null"));

		try {
			Random rand = new Random();
			Integer rnum = rand.nextInt(1000);

			// Fetch the application audit entity
			System.out.println("Step 1: Fetching ApplicationAuditEntity...");
			ApplicationAuditEntity applicationAuditEntity = applicationAuditServ
					.getByApplicantUserName(applicantUserName);
			if (applicationAuditEntity == null) {
				System.out.println("ERROR: ApplicationAuditEntity not found for: " + applicantUserName);
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Application audit entity not found for username: " + applicantUserName);
			}
			System.out.println("Step 1 OK: Found app_audit_id = " + applicationAuditEntity.getAppAuditId());

			linkOrphanAuditReportDocuments(applicationAuditEntity);

			ApplicationAuditEntity auditReference = auditReportDocumentService
					.resolveAuditReference(applicationAuditEntity);
			if (auditReference == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Invalid application audit reference for applicant: " + applicantUserName);
			}

			// Check if document entity exists
			System.out.println("Step 2: Checking existing document...");
			AuditReportDocumentEntity documentEntity = auditReportDocumentService
					.getActiveByAppAuditId(applicationAuditEntity.getAppAuditId());
			if (documentEntity != null) {
				documentEntity.setStatus("Inactive");
				documentEntity.setUpdated(new Date());
				auditReportDocumentService.updateData(documentEntity);
				System.out.println("Step 2: Set existing document to Inactive");
			} else {
				System.out.println("Step 2: No existing document found");
			}

			// Create a new document entity
			System.out.println("Step 3: Saving new document...");
			AuditReportDocumentEntity auditReportDocumentEntity = new AuditReportDocumentEntity();
			String filename = DocumentFileUtil.saveFile(file, "AuditorNCReport", rnum.toString(), "AuditorNCReport");
			auditReportDocumentEntity.setDocument(filename);
			auditReportDocumentEntity.setRemarks(remarks);
			auditReportDocumentEntity.setStatus("Active");
			auditReportDocumentEntity.setAppAuditId(auditReference);
			Optional<AuditReportDocumentEntity> savedDocument = auditReportDocumentService
					.saveData(auditReportDocumentEntity);
			if (savedDocument.isEmpty() || savedDocument.get().getAppAuditId() == null) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("Failed to save auditor NC report document with application audit link.");
			}
			System.out.println("Step 3 OK: Document saved as " + filename + " for app_audit_id="
					+ applicationAuditEntity.getAppAuditId());

			// Handle agency selection and notification
			System.out.println("Step 4: Getting agency selection...");
			AuditAgencySelectionEntity agencySelectionEntity = selectionServ.findbyAuditId(applicationAuditEntity);
			if (agencySelectionEntity == null) {
				System.out.println("ERROR: AuditAgencySelectionEntity not found!");
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Audit agency selection not found for this application.");
			}
			
			System.out.println("Step 5: Getting user login DTO...");
			UserLoginDTO userLoginDTO = auditReportCriteriaService
					.findbyAgencyId(agencySelectionEntity.getAuditAgencyId());

			String title = "Review NC Report";
			String message = "Dear Sir, We have Attached the Document and write Our remarks in Our Application. Please Check and Review it: "
					+ remarks;

			NotificationsRequest notification = new NotificationsRequest();
			notification.setUsername(userName);
			notification.setMessage(message);
			notification.setSubject(title);
			notification.setNotificationType("Email & Dashboard");
			notification.setNotificationDescription("Review NC Report");
			notification.setRole("ROLE_APPLICATION_REVIEW_COMMITTEE");

			System.out.println("Step 6: Sending notification...");
			notificationServ.sendNotification(notification);
			
			System.out.println("Step 7: Changing status...");
			auditReportCriteriaService.changedTheStatus(applicantUserName);

			System.out.println("=== SUCCESS ===");
			return ResponseEntity.ok("Remarks updated and notification sent successfully.");

		} catch (Exception ex) {
			System.out.println("=== EXCEPTION ===");
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while processing the request: " + ex.getMessage());
		}
	}

	@GetMapping("/get-all-data")
	public ResponseEntity<?> getAllData() {
		try {

			List<ApplicationAuditEntity> applicationAuditEntities = applicationAuditServ.getAll();

			List<AuditReportDocumentPayload> allPayloads = new ArrayList<>();

			for (ApplicationAuditEntity auditEntity : applicationAuditEntities) {

				List<AuditReportDocumentEntity> auditReportDocumentEntities = auditReportDocumentService
						.findByAuditId(auditEntity.getAppAuditId());

				if (auditReportDocumentEntities != null && !auditReportDocumentEntities.isEmpty()) {

					for (AuditReportDocumentEntity reportDocumentEntity : auditReportDocumentEntities) {
						AuditReportDocumentPayload payload = new AuditReportDocumentPayload();

						payload.setCriteriaDocId(reportDocumentEntity.getCriteriaDocId());
						payload.setAppAuditId(auditEntity.getAppAuditId());
						payload.setDocument(reportDocumentEntity.getDocument());
						payload.setCreatedBy(reportDocumentEntity.getCreatedBy());
						payload.setUpdatedBy(reportDocumentEntity.getUpdatedBy());
						payload.setRemarks(reportDocumentEntity.getRemarks());
						payload.setStatus(reportDocumentEntity.getStatus());
						payload.setIntentId(auditEntity.getIntentAppId());
						payload.setApplicantUserName(auditEntity.getApplicantUserName());

						allPayloads.add(payload);
					}
				}
			}

			return ResponseEntity.ok(allPayloads);

		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while fetching the data: " + ex.getMessage());
		}
	}

	@GetMapping("/get-All-Audit-details")
	public ResponseEntity<?> getByAuditNCReportApplicantUserName(
			@RequestParam("applicantUserName") String applicantUserName) {

		System.out.println("applicantUserName====>" + applicantUserName);
		try {
			// Fetch the application audit entity
			ApplicationAuditEntity applicationAuditEntity = applicationAuditServ
					.getByApplicantUserName(applicantUserName);

			AuditReportDocumentEntity auditNCSEntity = auditReportDocumentService
					.findByAuditsId(applicationAuditEntity.getAppAuditId());
			if (applicationAuditEntity == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Application audit entity not found for username: " + applicantUserName);
			}

			// Return the remarks list
			return ResponseEntity.ok(auditNCSEntity);

		} catch (Exception ex) {
			// Log and return an error response
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while fetching remarks: " + ex.getMessage());
		}
	}

	@PostMapping("/review-by-committee")
	public ResponseEntity<?> getapplicationRejection(@RequestParam("applicantUserName") String applicantUserName,
			@RequestParam("remarks") String remarks, @RequestParam("isreject") boolean isreject) {

		try {

			ApplicationAuditEntity applicationAuditEntity = applicationAuditServ
					.getByApplicantUserName(applicantUserName);
			if (applicationAuditEntity == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Application audit entity not found for username: " + applicantUserName);
			}

			AuditShortComingsEntity documentEntity = auditShortComingsService.getByAuditId(applicationAuditEntity);

			if (documentEntity != null) {
				documentEntity.setStatus("Inactive");
				documentEntity.setUpdated(new Date());
				auditShortComingsService.updateData(documentEntity);
			}

			AuditShortComingsEntity auditShortComingsEntity = new AuditShortComingsEntity();
			auditShortComingsEntity.setRemarks(remarks);
			auditShortComingsEntity.setStatus("Active");
			auditShortComingsEntity.setAppAuditId(applicationAuditEntity);
			auditShortComingsService.saveData(auditShortComingsEntity);

			AuditAgencySelectionEntity agencySelectionEntity = selectionServ.findbyAuditId(applicationAuditEntity);
			UserLoginDTO userLoginDTO = auditReportCriteriaService
					.findbyAgencyId(agencySelectionEntity.getAuditAgencyId());

			String title = "NC Report Rejection";
			String message = "Dear Applicant, your application has been rejected. Please check your email for more details and review it: "
					+ remarks;

			NotificationsRequest notification = new NotificationsRequest();
			notification.setUsername(userLoginDTO.getUserName());
			notification.setMessage(message);
			notification.setSubject(title);
			notification.setNotificationType("Email & Dashboard");
			notification.setNotificationDescription("NC Report Rejection");
			notification.setRole("ROLE_APPLICANT");

			notificationServ.sendNotification(notification);

			if (isreject) {
				auditReportCriteriaService.changedTheAproveOfRejection(applicantUserName);
			} else {
				auditReportCriteriaService.changedTheStatus(applicantUserName);
			}
			return ResponseEntity.ok("Remarks updated and notification sent successfully.");

		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while processing the request: " + ex.getMessage());
		}
	}

	@PostMapping("/review-by-committees")
	public ResponseEntity<?> getapplicationApprove(@RequestParam("applicantUserName") String applicantUserName,
			@RequestParam("remarks") String remarks) {

		try {
			Random rand = new Random();
			Integer rnum = rand.nextInt(1000);

			// Fetch the application audit entity
			ApplicationAuditEntity applicationAuditEntity = applicationAuditServ
					.getByApplicantUserName(applicantUserName);
			if (applicationAuditEntity == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Application audit entity not found for username: " + applicantUserName);
			}

			// Check if document entity exists
			AuditShortComingsEntity documentEntity = auditShortComingsService.getByAuditId(applicationAuditEntity);

			if (documentEntity != null) {
				// Set status to Inactive for the existing record
				documentEntity.setStatus("Inactive");
				documentEntity.setUpdated(new Date());
				auditShortComingsService.updateData(documentEntity);
			}

			// Create a new document entity
			AuditShortComingsEntity auditShortComingsEntity = new AuditShortComingsEntity();
			auditShortComingsEntity.setRemarks(remarks);
			auditShortComingsEntity.setStatus("Active");
			auditShortComingsEntity.setAppAuditId(applicationAuditEntity);
			auditShortComingsService.saveData(auditShortComingsEntity);

			// Handle agency selection and notification
			AuditAgencySelectionEntity agencySelectionEntity = selectionServ.findbyAuditId(applicationAuditEntity);
			UserLoginDTO userLoginDTO = auditReportCriteriaService
					.findbyAgencyId(agencySelectionEntity.getAuditAgencyId());

			String title = "NC Report Approve";
			String message = "Dear Applicant, your application has been approved. Please check your email for more details and review it: "
					+ remarks;

			NotificationsRequest notification = new NotificationsRequest();
			notification.setUsername(userLoginDTO.getUserName());
			notification.setMessage(message);
			notification.setSubject(title);
			notification.setNotificationType("Email & Dashboard");
			notification.setNotificationDescription("NC Report Approve");
			notification.setRole("ROLE_APPLICANT");

			notificationServ.sendNotification(notification);
			auditReportCriteriaService.changedTheStatusApprove(applicantUserName);

			return ResponseEntity.ok("Remarks updated and notification sent successfully.");

		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while processing the request: " + ex.getMessage());
		}
	}

	@GetMapping("/get-All-ShortComming")
	public ResponseEntity<?> getByAllShortCommingApplicantUserName(
			@RequestParam("applicantUserName") String applicantUserName) {

		System.out.println("applicantUserName====>" + applicantUserName);
		try {
			// Fetch the application audit entity
			ApplicationAuditEntity applicationAuditEntity = applicationAuditServ
					.getByApplicantUserName(applicantUserName);

			List<AuditShortComingsEntity> auditShortComingsEntity = auditShortComingsService
					.findByAuditsId(applicationAuditEntity.getAppAuditId());
			if (applicationAuditEntity == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Application audit entity not found for username: " + applicantUserName);
			}

			// Return the remarks list
			return ResponseEntity.ok(auditShortComingsEntity);

		} catch (Exception ex) {
			// Log and return an error response
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while fetching remarks: " + ex.getMessage());
		}
	}

	@PostMapping("/reviewer-remarks-by-applicant")
	public ResponseEntity<?> getApplicantRemarksToReviewer(@RequestParam("applicantUserName") String applicantUserName,
			@RequestParam("remarks") String remarks, @RequestParam("file") MultipartFile file) {

		try {
			Random rand = new Random();
			Integer rnum = rand.nextInt(1000);

			// Fetch the application audit entity
			ApplicationAuditEntity applicationAuditEntity = applicationAuditServ
					.getByApplicantUserName(applicantUserName);
			if (applicationAuditEntity == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Application audit entity not found for username: " + applicantUserName);
			}

			linkOrphanAuditReportDocuments(applicationAuditEntity);
			AuditShortComingsEntity documentEntity = resolveActiveShortcoming(applicationAuditEntity);

			if (documentEntity == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
						"No shortcoming record found for this application. Ensure the audit agency has submitted the NC report "
								+ "or the Application Review Committee has recorded review remarks before the applicant responds.");
			}

			ApplicantShortcomingsReportEntity documentsEntity = applicantShortcomingsReportService
					.getByShortIdId(documentEntity.getShortcomingId());

			if (documentsEntity != null) {
				// Set status to Inactive for the existing record
				documentsEntity.setStatus("Inactive");
				documentsEntity.setUpdated(new Date());
				applicantShortcomingsReportService.updateData(documentsEntity);
			}

		
			
			
			// Create a new document entity
			ApplicantShortcomingsReportEntity applicantShortcomingsReportEntity = new ApplicantShortcomingsReportEntity();
			String filename = DocumentFileUtil.saveFile(file, "ShortCommingReport", rnum.toString(),
					"ShortCommingReport");
			applicantShortcomingsReportEntity.setShortcomingReport(filename);
			applicantShortcomingsReportEntity.setRemarks(remarks);
			applicantShortcomingsReportEntity.setStatus("Active");
			applicantShortcomingsReportEntity.setShortcomingId(documentEntity);
			applicantShortcomingsReportService.saveData(applicantShortcomingsReportEntity);

			
			
			// Handle agency selection and notification
			AuditAgencySelectionEntity agencySelectionEntity = selectionServ.findbyAuditId(applicationAuditEntity);
			UserLoginDTO userLoginDTO = auditReportCriteriaService
					.findbyAgencyId(agencySelectionEntity.getAuditAgencyId());

			String title = "Review NC Report";
			String message = "Dear Sir, We have Attached the Document and write Our remarks in Our Application. Please Check and Review it: "
					+ remarks;

			NotificationsRequest notification = new NotificationsRequest();
			notification.setUsername(userLoginDTO.getUserName());
			notification.setMessage(message);
			notification.setSubject(title);
			notification.setNotificationType("Email & Dashboard");
			notification.setNotificationDescription("Review NC Report");
			notification.setRole("ROLE_APPLICATION_REVIEW_COMMITTEE");

			notificationServ.sendNotification(notification);
			applicationAuditServ.changedApplicantStatus(applicantUserName);

			return ResponseEntity.ok("Remarks updated and notification sent successfully.");

		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while processing the request: " + ex.getMessage());
		}
	}

	
	@PostMapping("/auditor-remarks-by-auditors")
	public ResponseEntity<?> getAuditorRemarksToReviewer(@RequestParam("applicantUserName") String applicantUserName,
			@RequestParam("remarks") String remarks, @RequestParam("file") MultipartFile file) {

		try {
			Random rand = new Random();
			Integer rnum = rand.nextInt(1000);

			// Fetch the application audit entity
			ApplicationAuditEntity applicationAuditEntity = applicationAuditServ
					.getByApplicantUserName(applicantUserName);
			if (applicationAuditEntity == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Application audit entity not found for username: " + applicantUserName);
			}

			AuditShortComingsEntity documentEntity = auditShortComingsService.getByAuditId(applicationAuditEntity);

			// Check if document entity exists
			ApplicantShortcomingsReportEntity documentsEntity = applicantShortcomingsReportService
					.getByShortIdId(documentEntity.getShortcomingId());

			if (documentsEntity != null) {
				// Set status to Inactive for the existing record
				documentsEntity.setStatus("Inactive");
				documentsEntity.setUpdated(new Date());
				applicantShortcomingsReportService.updateData(documentsEntity);
			}

		
			
			
			// Create a new document entity
			AuditReviewReportEntity applicantShortcomingsReportEntity = new AuditReviewReportEntity();
			String filename = DocumentFileUtil.saveFile(file, "AuditReviewReport", rnum.toString(),
					"AuditReviewReport");
			applicantShortcomingsReportEntity.setAuditorReviewReport(filename);
			applicantShortcomingsReportEntity.setRemarks(remarks);
			applicantShortcomingsReportEntity.setStatus("Active");
			applicantShortcomingsReportEntity.setShortcomingId(documentEntity);
			auditReviewReportService.saveData(applicantShortcomingsReportEntity);

			
			applicationAuditServ.changedApplicantStatusByAuditor(applicantUserName);
			// Handle agency selection and notification
			AuditAgencySelectionEntity agencySelectionEntity = selectionServ.findbyAuditId(applicationAuditEntity);

			//			UserLoginDTO userLoginDTO = auditReportCriteriaService
//					.findbyAgencyId(agencySelectionEntity.getAuditAgencyId());
//
//			String title = "Review NC Report";
//			String message = "Dear Sir, We have Attached the Document and write Our remarks in Our Application. Please Check and Review it: "
//					+ remarks;
//
//			NotificationsRequest notification = new NotificationsRequest();
//			notification.setUsername(userLoginDTO.getUserName());
//			notification.setMessage(message);
//			notification.setSubject(title);
//			notification.setNotificationType("Email & Dashboard");
//			notification.setNotificationDescription("Review NC Report");
//			notification.setRole("ROLE_APPLICATION_REVIEW_COMMITTEE");
//
//			notificationServ.sendNotification(notification);
//			auditReportCriteriaService.changedTheStatus(applicantUserName);

			return ResponseEntity.ok("Remarks updated and notification sent successfully.");

		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while processing the request: " + ex.getMessage());
		}
	}

	// Accept NC report endpoint - breaks the loop and moves to In Principle Approval
	@PostMapping("/accept-auditor-remarks")
	public ResponseEntity<?> acceptAuditorRemarks(@RequestParam("applicantUserName") String applicantUserName,
			@RequestParam("remarks") String remarks, @RequestParam("file") MultipartFile file) {

		try {
			Random rand = new Random();
			Integer rnum = rand.nextInt(1000);

			// Fetch the application audit entity
			ApplicationAuditEntity applicationAuditEntity = applicationAuditServ
					.getByApplicantUserName(applicantUserName);
			
			// Try to save the review report if possible, but don't fail if shortcomings don't exist
			try {
				if (applicationAuditEntity != null) {
					AuditShortComingsEntity documentEntity = auditShortComingsService.getByAuditId(applicationAuditEntity);
					
					if (documentEntity != null) {
						// Update existing shortcoming report if exists
						ApplicantShortcomingsReportEntity documentsEntity = applicantShortcomingsReportService
								.getByShortIdId(documentEntity.getShortcomingId());

						if (documentsEntity != null) {
							documentsEntity.setStatus("Inactive");
							documentsEntity.setUpdated(new Date());
							applicantShortcomingsReportService.updateData(documentsEntity);
						}

						// Create a new review report entity
						AuditReviewReportEntity reviewReportEntity = new AuditReviewReportEntity();
						String filename = DocumentFileUtil.saveFile(file, "AuditReviewReport", rnum.toString(),
								"AuditReviewReport");
						reviewReportEntity.setAuditorReviewReport(filename);
						reviewReportEntity.setRemarks(remarks);
						reviewReportEntity.setStatus("Active");
						reviewReportEntity.setShortcomingId(documentEntity);
						auditReviewReportService.saveData(reviewReportEntity);
					}
				}
			} catch (Exception e) {
				// Log but continue - the main goal is to change the status
				System.out.println("Warning: Could not save review report but continuing with status change: " + e.getMessage());
			}

			// ALWAYS change the status to move to In Principle Approval - this is the main goal
			applicationAuditServ.acceptApplicantStatusByAuditor(applicantUserName);

			return ResponseEntity.ok("NC Report accepted. Application moved to In Principle Approval stage.");

		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while processing the request: " + ex.getMessage());
		}
	}


	
	@GetMapping("/get-All-ShortCommingReport")
	public ResponseEntity<?> getByAllShortCommingReportApplicantUserName(
			@RequestParam("applicantUserName") String applicantUserName) {
		System.out.println("applicantUserName====>" + applicantUserName);

		try {

			ApplicationAuditEntity applicationAuditEntity = applicationAuditServ
					.getByApplicantUserName(applicantUserName);

			if (applicationAuditEntity == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Application audit entity not found for username: " + applicantUserName);
			}

			List<AuditShortComingsEntity> auditShortComingsEntity = auditShortComingsService
					.findByAuditsId(applicationAuditEntity.getAppAuditId());

			List<ApplicantShortcomingsReportEntity> reportEntities = new ArrayList<>();

			for (AuditShortComingsEntity comingsEntity : auditShortComingsEntity) {
				List<ApplicantShortcomingsReportEntity> individualReportEntities = applicantShortcomingsReportService
						.findByAuditsId(comingsEntity.getShortcomingId());

				reportEntities.addAll(individualReportEntities);
			}

			return ResponseEntity.ok(reportEntities);

		} catch (Exception ex) {

			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while fetching remarks: " + ex.getMessage());
		}
	}

	
	@GetMapping("/get-All-Auditor-Review-Report")
	public ResponseEntity<?> getByAllAuditorReviewReportByApplicantUserName(
			@RequestParam("applicantUserName") String applicantUserName) {
		System.out.println("applicantUserName====>" + applicantUserName);

		try {

			ApplicationAuditEntity applicationAuditEntity = applicationAuditServ
					.getByApplicantUserName(applicantUserName);

			if (applicationAuditEntity == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Application audit entity not found for username: " + applicantUserName);
			}

			List<AuditShortComingsEntity> auditShortComingsEntity = auditShortComingsService
					.findByAuditsId(applicationAuditEntity.getAppAuditId());

			List<AuditReviewReportEntity> reportEntities = new ArrayList<>();

			for (AuditShortComingsEntity comingsEntity : auditShortComingsEntity) {
				List<AuditReviewReportEntity> individualReportEntities = auditReviewReportService
						.findByAuditsId(comingsEntity.getShortcomingId());

				reportEntities.addAll(individualReportEntities);
			}

			return ResponseEntity.ok(reportEntities);

		} catch (Exception ex) {

			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while fetching remarks: " + ex.getMessage());
		}
	}

	
	
	
	@PostMapping("/review-by-cca")
	public ResponseEntity<?> getapplicationRejection(@RequestParam("applicantUserName") String applicantUserName) {

		try {
			Random rand = new Random();
			Integer rnum = rand.nextInt(1000);

			// Fetch the application audit entity
			ApplicationAuditEntity applicationAuditEntity = applicationAuditServ
					.getByApplicantUserName(applicantUserName);
			if (applicationAuditEntity == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Application audit entity not found for username: " + applicantUserName);
			}

			// Check if document entity exists
			AuditShortComingsEntity documentEntity = auditShortComingsService.getByAuditId(applicationAuditEntity);

			if (documentEntity != null) {
				// Set status to Inactive for the existing record
				documentEntity.setStatus("Inactive");
				documentEntity.setUpdated(new Date());
				auditShortComingsService.updateData(documentEntity);
			}

			// Handle agency selection and notification
			AuditAgencySelectionEntity agencySelectionEntity = selectionServ.findbyAuditId(applicationAuditEntity);
			UserLoginDTO userLoginDTO = auditReportCriteriaService
					.findbyAgencyId(agencySelectionEntity.getAuditAgencyId());

			String title = "Application Rejected";
			String message = "Dear Applicant, your application has been rejected. Please check your email for more details and review it: ";

			NotificationsRequest notification = new NotificationsRequest();
			notification.setUsername(applicantUserName);
			notification.setMessage(message);
			notification.setSubject(title);
			notification.setNotificationType("Email & Dashboard");
			notification.setNotificationDescription("NC Report Approve");
			notification.setRole("ROLE_APPLICANT");

			notificationServ.sendNotification(notification);
			auditReportCriteriaService.changedTheApplicantRejection(applicantUserName);

			return ResponseEntity.ok("Remarks updated and notification sent successfully.");

		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while processing the request: " + ex.getMessage());
		}
	}

	@GetMapping("/get-all-active-auditors")
	public ResponseEntity<?> getAllActiveAuditors() {
		try {
			List<ApplicationAuditorsEntity> activeAuditors = applicationAuditorsService.getAllActiveActiveAuditor();

			if (activeAuditors == null || activeAuditors.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No active auditors found.");
			}

			return ResponseEntity.ok(activeAuditors);
		} catch (Exception e) {
			// Log the error for debugging
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while retrieving active auditors: " + e.getMessage());
		}
	}

	@PostMapping("/save-audit-control")
	public ResponseEntity<?> saveAuditControls(@RequestBody List<AuditRequestDTO> auditRequests) {
		try {

			List<AuditControlEntity> existingControls = auditService
					.getAuditControlsByUser(auditRequests.get(0).getUserName());

			Set<String> incomingControlIds = auditRequests.stream()
					.map(request -> String.valueOf(request.getControlId())).collect(Collectors.toSet());

			Set<String> existingControlIds = existingControls.stream()
					.map(control -> String.valueOf(control.getControlId())).collect(Collectors.toSet());

			Set<String> idsToAdd = new HashSet<>(incomingControlIds);
			idsToAdd.removeAll(existingControlIds);

			Set<String> idsToRemove = new HashSet<>(existingControlIds);
			idsToRemove.removeAll(incomingControlIds);

			for (String idToAdd : idsToAdd) {
				AuditRequestDTO requestToAdd = auditRequests.stream()
						.filter(req -> String.valueOf(req.getControlId()).equals(idToAdd)).findFirst().orElse(null);

				if (requestToAdd != null) {
					AuditControlEntity newControl = new AuditControlEntity();
					newControl.setControlId(Integer.parseInt(idToAdd));
					newControl.setUserName(requestToAdd.getUserName());
					newControl.setCreated(new Date());
					newControl.setStatus("Active");
					auditService.save(newControl);
				}
			}

			for (String idToRemove : idsToRemove) {
				auditService.deleteByControlId(Integer.parseInt(idToRemove));
			}

			return ResponseEntity.ok("Audit controls updated successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error occurred while updating audit controls: " + e.getMessage());
		}
	}

	@GetMapping("/audit-controls")
	public ResponseEntity<?> getAllAuditControls(@RequestParam String userName) {
		try {

			List<AuditControlEntity> auditControls = auditService.getAuditControlsByUser(userName);

			List<AuditRequestDTO> auditRequestDTOs = new ArrayList<>();

			for (AuditControlEntity auditControl : auditControls) {
				AuditRequestDTO dto = new AuditRequestDTO();
				dto.setControlId(auditControl.getControlId());
				dto.setUserName(auditControl.getUserName());
				dto.setStatus(auditControl.getStatus());
				dto.setCreated(auditControl.getCreated());
				dto.setUpdated(auditControl.getUpdated());
				auditRequestDTOs.add(dto);
			}

			if (auditRequestDTOs.isEmpty()) {
				return ResponseEntity.noContent().build();
			}

			return ResponseEntity.ok(auditRequestDTOs);

		} catch (Exception e) {

			return ResponseEntity.status(500)
					.body("An error occurred while fetching audit controls: " + e.getMessage());
		}
	}

	@GetMapping("/audit-controls/{userName}")
	public ResponseEntity<List<AuditRequestDTO>> getAllAuditControles(@PathVariable String userName) {
		try {
			List<AuditControlEntity> auditControls = auditService.getAuditControlsByUser(userName);

			List<AuditRequestDTO> auditRequestDTOs = new ArrayList<>();

			for (AuditControlEntity auditControl : auditControls) {
				AuditRequestDTO dto = new AuditRequestDTO();
				dto.setControlId(auditControl.getControlId());
				dto.setUserName(auditControl.getUserName());
				dto.setStatus(auditControl.getStatus());
				dto.setCreated(auditControl.getCreated());
				dto.setUpdated(auditControl.getUpdated());
				auditRequestDTOs.add(dto);
			}

			// Always return the list, even if empty - never return noContent()
			return ResponseEntity.ok(auditRequestDTOs);

		} catch (Exception e) {
			// Return empty list instead of 500 error to prevent NPE in caller
			return ResponseEntity.ok(new ArrayList<>());
		}
	}

	@GetMapping("/get-All-Audit-Report-Criteria-Details")
	public ResponseEntity<?> getByApplicantUserNames(@RequestParam("applicantUserName") String applicantUserName) {
		System.out.println("applicantUserName====>" + applicantUserName);
		try {

			ApplicationAuditEntity applicationAuditEntity = applicationAuditServ
					.getByApplicantUserName(applicantUserName);

			if (applicationAuditEntity == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Application audit entity not found for username: " + applicantUserName);
			}

			List<AuditReportCriteriaEntity> auditReportCriteriaEntity = auditReportCriteriaService
					.findByAuditId(applicationAuditEntity.getAppAuditId());

			return ResponseEntity.ok(auditReportCriteriaEntity);

		} catch (Exception ex) {

			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while fetching remarks: " + ex.getMessage());
		}
	}

	@GetMapping("/get-All-Audit-Report-Criteria-Details/{applicantUserName}")
	public ResponseEntity<?> getByApplicantsUserNames(@PathVariable String applicantUserName) {
		System.out.println("Fetching audit report criteria for username: {}" + applicantUserName);

		if (applicantUserName == null || applicantUserName.trim().isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username cannot be null or empty");
		}

		try {
			ApplicationAuditEntity applicationAuditEntity = applicationAuditServ
					.getByApplicantUserName(applicantUserName);

			if (applicationAuditEntity == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Application audit entity not found for username: " + applicantUserName);
			}

			List<AuditReportCriteriaEntity> auditReportCriteriaEntities = auditReportCriteriaService
					.findByAuditId(applicationAuditEntity.getAppAuditId());

			List<AuditReportCriteriaDTO> criteriaDTOs = new ArrayList<>();

			for (AuditReportCriteriaEntity entity : auditReportCriteriaEntities) {
				AuditReportCriteriaDTO dto = new AuditReportCriteriaDTO();
				dto.setCriteriaId(String.valueOf(entity.getCriteriaId()));
				dto.setAuditControlId(entity.getAuditControlId());
				dto.setAppAuditId(String.valueOf(entity.getAppAuditId()));
				dto.setCompliance(entity.getCompliance());
				dto.setDocument(entity.getDocument());
				dto.setStatus(entity.getStatus());
				dto.setCreated(entity.getCreated() != null ? entity.getCreated().toString() : null);
				dto.setUpdated(entity.getUpdated() != null ? entity.getUpdated().toString() : null);
				dto.setCreatedBy(entity.getCreatedBy());
				dto.setUpdatedBy(entity.getUpdatedBy());
				criteriaDTOs.add(dto);

			}
			System.out.println("criteriaDTOs=-=-->" + criteriaDTOs.get(0).toString());
			return ResponseEntity.ok(criteriaDTOs);

		} catch (Exception ex) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while fetching remarks: " + ex.getMessage());
		}
	}

	@GetMapping("/applicant-action-taken-document-name")
	public ResponseEntity<?> viewApplicantActionTaken(@RequestParam("documentName") String documentName) {
		try {
			Optional<ApplicantActionTakenEntity> optional = actionTakenService
					.downloadfileBydDocumentName(EncryptionUtil.decrypt(documentName));

			if (optional.isPresent()) {
				ApplicantActionTakenEntity c = optional.get();
				System.out.println(c.getActionReport());
				Path filePath = Paths.get(Constant.REAL_PATH + "//ApplicantActionTakenFile")
						.resolve(c.getActionReport()).normalize();
				System.out.println(filePath);
				Resource resource = new UrlResource(filePath.toUri());
				System.out.println(resource);
				if (resource.exists()) {
					String contentType = Files.probeContentType(filePath);
					System.out.println(contentType);
					return ResponseEntity.ok()
							.contentType(MediaType
									.parseMediaType(contentType != null ? contentType : "application/octet-stream"))
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

	@PostMapping("/auditor-accept-nc")
	public ResponseEntity<?> getAuditorApprovedData(@RequestParam("applicantUserName") String applicantUserName,
			@RequestParam("remarks") String remarks, @RequestParam("controlId") String auditControlId,
			@RequestParam("compliance") String compliance) {
		try {
			if (applicantUserName == null || applicantUserName.isEmpty() || auditControlId == null
					|| auditControlId.isEmpty()) {
				return ResponseEntity.badRequest().body("Invalid input parameters");
			}

			AuditReportCriteriaEntity reportCriteriaEntity = resolveReportCriteriaForApplicant(applicantUserName,
					auditControlId);

			if (reportCriteriaEntity == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("No matching audit report found for control ID: " + auditControlId);
			}

			reportCriteriaEntity.setCompliance(compliance);
			auditReportCriteriaService.updateData(reportCriteriaEntity);

			ApplicantActionTakenEntity existingActionEntity = actionTakenService
					.getAllByReportCriteria(reportCriteriaEntity);

			if (existingActionEntity != null) {
				existingActionEntity.setActionTakenId(existingActionEntity.getActionTakenId());
				existingActionEntity.setCreated(existingActionEntity.getCreated());
				existingActionEntity.setAuditorRemarks(remarks);
				existingActionEntity.setUpdated(new Date());
			}

			actionTakenService.updateData(existingActionEntity);

			return ResponseEntity.ok("Remarks updated and notification sent successfully.");
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while processing the request: " + ex.getMessage());
		}
	}

	@PostMapping("/auditor-reject-nc")
	public ResponseEntity<?> getAuditorRejectedData(@RequestParam("applicantUserName") String applicantUserName,
			@RequestParam("remarks") String remarks, @RequestParam("controlId") String auditControlId

	) {
		try {
			if (applicantUserName == null || applicantUserName.isEmpty() || auditControlId == null
					|| auditControlId.isEmpty()) {
				return ResponseEntity.badRequest().body("Invalid input parameters");
			}

			AuditReportCriteriaEntity reportCriteriaEntity = resolveReportCriteriaForApplicant(applicantUserName,
					auditControlId);

			if (reportCriteriaEntity == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("No matching audit report found for control ID: " + auditControlId);
			}

			ApplicantActionTakenEntity existingActionEntity = actionTakenService
					.getAllByReportCriteria(reportCriteriaEntity);

			if (existingActionEntity != null) {
				existingActionEntity.setActionTakenId(existingActionEntity.getActionTakenId());
				existingActionEntity.setCreated(existingActionEntity.getCreated());
				existingActionEntity.setAuditorRemarks(remarks);
				existingActionEntity.setUpdated(new Date());
				existingActionEntity.setStatus("Inactive");
			}

			actionTakenService.updateData(existingActionEntity);

			return ResponseEntity.ok("Remarks updated and notification sent successfully.");
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while processing the request: " + ex.getMessage());
		}
	}

	private AuditReportCriteriaEntity resolveReportCriteriaForApplicant(String applicantUserName,
			String auditControlId) {
		ApplicationAuditEntity applicationAuditEntity = applicationAuditServ.getByApplicantUserName(applicantUserName);
		if (applicationAuditEntity == null || applicationAuditEntity.getAppAuditId() == null) {
			return null;
		}
		Long appAuditId = applicationAuditEntity.getAppAuditId();

		AuditReportCriteriaEntity exact = auditReportCriteriaService.findActiveByAppAuditIdAndControlId(appAuditId,
				auditControlId);
		if (exact != null) {
			return exact;
		}

		List<AuditReportCriteriaEntity> scoped = auditReportCriteriaService.findActiveByAppAuditId(appAuditId);
		return findMatchingReportCriteria(scoped, auditControlId);
	}

	private AuditReportCriteriaEntity findMatchingReportCriteria(List<AuditReportCriteriaEntity> entities,
			String auditControlId) {
		if (auditControlId == null || entities == null || entities.isEmpty()) {
			return null;
		}

		for (AuditReportCriteriaEntity entity : entities) {
			if (auditControlId.equals(entity.getAuditControlId())) {
				return entity;
			}
		}

		Set<String> inputParts = Arrays.stream(auditControlId.split("-")).map(String::trim)
				.filter(part -> !part.isEmpty()).collect(Collectors.toSet());

		for (AuditReportCriteriaEntity entity : entities) {
			if (entity.getAuditControlId() == null) {
				continue;
			}
			Set<String> entityParts = Arrays.stream(entity.getAuditControlId().split("-")).map(String::trim)
					.filter(part -> !part.isEmpty()).collect(Collectors.toSet());

			if (entityParts.equals(inputParts) || entityParts.containsAll(inputParts)) {
				return entity;
			}
		}
		return null;
	}

	@PostMapping("/forword-to-applicant")
	public ResponseEntity<?> forwordToApplicant(@RequestParam("applicantUserName") String applicantUserName) {
		try {

			System.out.println(applicantUserName);

			auditReportCriteriaService.sendTheApplicantUserName(applicantUserName);

			String title = "NC Raised By Auditor";
			String message = "Dear Applicant, Some NC Generated Regarding Your Application. Please Check and Take Action";

			NotificationsRequest notification = new NotificationsRequest();
			notification.setUsername(applicantUserName);
			notification.setMessage(message);
			notification.setSubject(title);
			notification.setNotificationType("Email & Dashboard");
			notification.setNotificationDescription("NC Generated By Auditor");
			notification.setRole("ROLE_APPLICANT");

			notificationServ.sendNotification(notification);

			return ResponseEntity.ok("data to be forword applicant and notification sent successfully.");
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while processing the request: " + ex.getMessage());
		}
	}

	private String AnnualAuditPeriod(String username) {
		StringBuilder contentBuilder = new StringBuilder();

		AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", username);
		if (amain == null) {

			contentBuilder.append("<p><b>1. Annual Audit Period Details</b></p>");

			contentBuilder.append("<table>").append("<tr>").append("<th>S. No.</th>").append("<th>Description</th>")
					.append("<th>From</th>").append("<th>To</th>")
					.append("<th>Brief details of open observation (max 50 words)</th>").append("</tr>");

			for (int i = 0; i < 4; i++) {
				contentBuilder.append("<tr>").append("<td>").append(" ").append("</td>") // Serial Number
						.append("<td>").append(" ").append("</td>").append("<td>").append(" ").append("</td>")
						.append("<td>").append(" ").append("</td>").append("<td>").append(" ").append("</td>")
						.append("</tr>");
			}

			contentBuilder.append("<tr><td colspan = 5 style='text-align: left;'><b>Auditor Remarks: </b></td></tr>");

			contentBuilder.append("</table>");

			return contentBuilder.toString();

		} else {

			AnnualAuditPeriodMain mainObj = periodMainServ.getByAnnexureId(amain.getAnnexureMainId());
			if (mainObj == null) {
				return "<p>No audit period details available.</p>";
			}

			contentBuilder.append("<p><b>1. Annual Audit Period Details</b></p>");

			contentBuilder.append("<table>").append("<tr>").append("<th>S. No.</th>").append("<th>Description</th>")
					.append("<th>From</th>").append("<th>To</th>")
					.append("<th>Brief details of open observation (max 50 words)</th>").append("</tr>");

			List<AnnualAuditPeriodDetails> list = periodServ.getByAnnualAuditPeriodMainId(mainObj.getPeriodMainId());

			if (list.isEmpty()) {
				contentBuilder.append("<tr><td colspan='5'>No audit period records found.</td></tr>");
			} else {
				int serialNumber = 1;
				for (AnnualAuditPeriodDetails row : list) {
					contentBuilder.append("<tr>").append("<td>").append(serialNumber++).append("</td>") // Serial Number
							.append("<td>").append(row.getDescription() != null ? row.getDescription() : "N/A")
							.append("</td>").append("<td>")
							.append(row.getFromDate() != null ? row.getFromDate() : "N/A").append("</td>")
							.append("<td>").append(row.getToDate() != null ? row.getToDate() : "N/A").append("</td>")
							.append("<td>").append(row.getObservations() != null ? row.getObservations() : "N/A")
							.append("</td>").append("</tr>");
				}
			}

			contentBuilder.append("</table>");

			contentBuilder.append("<p><b>Auditor Remarks</b></p>");
			contentBuilder.append("<p>")
					.append(mainObj.getAuditorVerification() != null ? mainObj.getAuditorVerification() : "No remarks")
					.append("</p>");

		

			return contentBuilder.toString();

		}
	}

	private String InternalAuditDetailes(String username) {
		StringBuilder contentBuilder = new StringBuilder();

		AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", username);
		if (amain == null) {
			

			contentBuilder.append("<p><b>2. Internal Audit Details</b></p>");

			contentBuilder.append("<table>").append("<tr>").append("<th>S. No.</th>").append("<th>Description</th>")
					.append("<th>From</th>").append("<th>To</th>")
					.append("<th>Brief details of open observation (max 50 words)</th>")
					.append("<th>Details of Auditors</th>").append("</tr>");
			for (int i = 0; i < 4; i++) {
				contentBuilder.append("<tr>").append("<td>").append(" ").append("</td>") // Serial Number
						.append("<td>").append(" ").append("</td>").append("<td>").append(" ").append("</td>")
						.append("<td>").append(" ").append("</td>").append("<td>").append(" ").append("</td>")
						.append("<td>").append(" ").append("</td>").append("</tr>");
			}
			contentBuilder.append("<tr><td colspan = 6 style='text-align: left;'><b>Auditor Remarks: </b></td></tr>");

			contentBuilder.append("</table>");

		
			return contentBuilder.toString();

		} else {

			InternalAuditMain mainObj = internalMainServ.getByAnnexureId(amain.getAnnexureMainId());
			if (mainObj == null) {
				return "<p>No internal audit details available.</p>";
			}

			

			contentBuilder.append("<p><b>2. Internal Audit Details</b></p>");

			contentBuilder.append("<table>").append("<tr>").append("<th>S. No.</th>").append("<th>Description</th>")
					.append("<th>From</th>").append("<th>To</th>")
					.append("<th>Brief details of open observation (max 50 words)</th>")
					.append("<th>Details of Auditors</th>").append("</tr>");

			List<InternalAuditDetails> list = internalServ.getByInternalAuditMainId(mainObj.getInAuditMainId());

			if (list.isEmpty()) {
				contentBuilder.append("<tr><td colspan='6'>No internal audit records found.</td></tr>");
			} else {
				int serialNumber = 1;
				for (InternalAuditDetails row : list) {
					contentBuilder.append("<tr>").append("<td>").append(serialNumber++).append("</td>") // Serial Number
							.append("<td>").append(row.getDescription() != null ? row.getDescription() : "N/A")
							.append("</td>").append("<td>")
							.append(row.getFromDate() != null ? row.getFromDate() : "N/A").append("</td>")
							.append("<td>").append(row.getToDate() != null ? row.getToDate() : "N/A").append("</td>")
							.append("<td>").append(row.getObservations() != null ? row.getObservations() : "N/A")
							.append("</td>").append("<td>")
							.append(row.getAuditorDetails() != null ? row.getAuditorDetails() : "N/A").append("</td>")
							.append("</tr>");
				}
			}

			contentBuilder.append("</table>");

			contentBuilder.append("<p><b>Auditor Remarks</b></p>");
			contentBuilder.append("<p>")
					.append(mainObj.getAuditorVerification() != null ? mainObj.getAuditorVerification() : "No remarks")
					.append("</p>");

			

			return contentBuilder.toString();
		}
	}

	private String ekycAccountAuditDetailes(String username) {
		StringBuilder contentBuilder = new StringBuilder();

		// Fetch Annexure and eKYC Audit Details
		AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", username);
		if (amain == null) {
			

			contentBuilder.append("<p><b>3. eKYC Account Audit Details of last one year - Month-wise</b></p>");

			contentBuilder.append("<table>").append("<tr>").append("<th>S. No.</th>").append("<th>Month</th>")
					.append("<th>From</th>").append("<th>To</th>").append("<th>Summary of Observations</th>")
					.append("<th>Details of Auditors</th>").append("</tr>");
			for (int i = 0; i < 12; i++) {
				contentBuilder.append("<tr>").append("<td>").append(" ").append("</td>") // Serial Number
						.append("<td>").append(" ").append("</td>").append("<td>").append(" ").append("</td>")
						.append("<td>").append(" ").append("</td>").append("<td>").append(" ").append("</td>")
						.append("<td>").append(" ").append("</td>").append("</tr>");
			}
			contentBuilder.append("<tr><td colspan = 6 style='text-align: left;'><b>Auditor Remarks: </b></td></tr>");

			contentBuilder.append("</table>");

			
			return contentBuilder.toString();

		} else {

			EKYCMonthMain mainObj = monthMainServ.getByAnnexureId(amain.getAnnexureMainId());
			if (mainObj == null) {
				return "<p>No eKYC account audit details available.</p>";
			}

			

			// Title
			contentBuilder.append("<p><b>3. eKYC Account Audit Details of last one year - Month-wise</b></p>");

			// Table Header
			contentBuilder.append("<table>").append("<tr>").append("<th>S. No.</th>").append("<th>Month</th>")
					.append("<th>From</th>").append("<th>To</th>").append("<th>Summary of Observations</th>")
					.append("<th>Details of Auditors</th>").append("</tr>");

			// Fetch list of eKYC audit records
			List<EKYCAcMonthDetails> list = monthDetailsServ.getByEKYCAcMonthMainId(mainObj.geteKYCMonthMainId());

			if (list.isEmpty()) {
				contentBuilder.append("<tr><td colspan='6'>No eKYC account audit records found.</td></tr>");
			} else {
				int serialNumber = 1;
				for (EKYCAcMonthDetails row : list) {
					contentBuilder.append("<tr>").append("<td>").append(serialNumber++).append("</td>") // Serial Number
							.append("<td>").append(row.getMonth() != null ? row.getMonth() : "N/A").append("</td>")
							.append("<td>").append(row.getFromDate() != null ? row.getFromDate() : "N/A")
							.append("</td>").append("<td>").append(row.getToDate() != null ? row.getToDate() : "N/A")
							.append("</td>").append("<td>")
							.append(row.getObservations() != null ? row.getObservations() : "N/A").append("</td>")
							.append("<td>").append(row.getAuditorDetails() != null ? row.getAuditorDetails() : "N/A")
							.append("</td>").append("</tr>");
				}
			}

			contentBuilder.append("</table>");

			contentBuilder.append("<p><b>Auditor Remarks</b></p>");
			contentBuilder.append("<p>")
					.append(mainObj.getAuditorVerification() != null ? mainObj.getAuditorVerification() : "No remarks")
					.append("</p>");

			

			return contentBuilder.toString();
		}
	}

	private String ekycAccountBasedVerification(String username) {
		StringBuilder contentBuilder = new StringBuilder();

		// Fetch Annexure and eKYC Account-Based Verification Details
		AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", username);
		if (amain == null) {
			// Title
			

			contentBuilder.append(
					"<p><b>4. eKYC Account-based Verification Enabled by CA - Details of the Audit Period</b></p>");

			// Table Header
			contentBuilder.append("<table>").append("<tr>").append("<th>S. No.</th>").append("<th>Option</th>")
					.append("<th>Date of Approval by CCA</th>").append("<th>No DSCs Issued</th>")
					.append("<th>Associated External Service</th>").append("<th>Details of External Service</th>")
					.append("</tr>");
			for (int i = 0; i < 12; i++) {
				contentBuilder.append("<tr>").append("<td>").append(" ").append("</td>").append("<td>").append(" ")
						.append("</td>").append("<td>").append(" ").append("</td>").append("<td>").append(" ")
						.append("</td>").append("<td>").append(" ").append("</td>").append("<td>").append(" ")
						.append("</td>").append("</tr>");

			}
			contentBuilder.append("<tr><td colspan = 6 style='text-align: left;'><b>Auditor Remarks: </b></td></tr>");

			contentBuilder.append("</table>");

			return contentBuilder.toString();

		} else {

			EKYCAccountBasedMain mainObj = accountBasedServ.getByAnnexureId(amain.getAnnexureMainId());
			if (mainObj == null) {
				return "<p>No eKYC account-based verification details available.</p>";
			}

			// Title
		

			contentBuilder.append(
					"<p><b>4. eKYC Account-based Verification Enabled by CA - Details of the Audit Period</b></p>");

			// Table Header
			contentBuilder.append("<table>").append("<tr>").append("<th>S. No.</th>").append("<th>Option</th>")
					.append("<th>Date of Approval by CCA</th>").append("<th>No DSCs Issued</th>")
					.append("<th>Associated External Service</th>").append("<th>Details of External Service</th>")
					.append("</tr>");

			// Prepare data list
			List<Map<String, String>> dataList = new ArrayList<>();
			dataList.add(createRow("Offline Aadhaar eKYC",
					mainObj.getApprovalDateOfflineAadhaar() != null ? mainObj.getApprovalDateOfflineAadhaar().toString()
							: "-",
					mainObj.getOfflineAadhaarDSCCount() != null ? mainObj.getOfflineAadhaarDSCCount().toString() : "0",
					"", mainObj.getExtServOffAadhar()));

			dataList.add(createRow("Online Aadhaar eKYC",
					mainObj.getApprovalDateOnlineAadhar() != null ? mainObj.getApprovalDateOnlineAadhar().toString()
							: "-",
					mainObj.getOnlineAadharDSCCount() != null ? mainObj.getOnlineAadharDSCCount().toString() : "0",
					mainObj.getKuaLicenseDate() != null ? mainObj.getKuaLicenseDate().toString() : "-",
					mainObj.getExtServOnAadhar()));

			dataList.add(createRow("PAN eKYC",
					mainObj.getApprovalDatePAN() != null ? mainObj.getApprovalDatePAN().toString() : "-",
					mainObj.getPanDSCCount() != null ? mainObj.getPanDSCCount().toString() : "0",
					mainObj.getApprovalDatePAN() != null ? mainObj.getApprovalDatePAN().toString() : "-",
					mainObj.getPanServDetails() != null ? mainObj.getPanServDetails().toString() : "-"));

			dataList.add(createRow("CA eKYC",
					mainObj.getApprovalDateCA() != null ? mainObj.getApprovalDateCA().toString() : "-",
					mainObj.getCaDSCCount() != null ? mainObj.getCaDSCCount().toString() : "0", "",
					mainObj.getExtServCA()));

			dataList.add(createRow("Organisational eKYC",
					mainObj.getApprovalDateOrganisational() != null ? mainObj.getApprovalDateOrganisational().toString()
							: "-",
					mainObj.getOrganisationalDSCCount() != null ? mainObj.getOrganisationalDSCCount().toString() : "0",
					mainObj.getGstServiceDetails().toString(), mainObj.getExtServOrg()));

			dataList.add(createRow("Banking eKYC",
					mainObj.getApprovalDateBanking() != null ? mainObj.getApprovalDateBanking().toString() : "-",
					mainObj.getBankingDSCCount() != null ? mainObj.getBankingDSCCount().toString() : "0",
					mainObj.getNameOfBanks(), mainObj.getExtServBanking()));

			// Generate table rows dynamically
			int serialNo = 1;
			for (Map<String, String> row : dataList) {
				contentBuilder.append("<tr>").append("<td>").append(serialNo++).append("</td>").append("<td>")
						.append(row.get("option")).append("</td>").append("<td>").append(row.get("dateOfApproval"))
						.append("</td>").append("<td>").append(row.get("noDSCsIssued")).append("</td>").append("<td>")
						.append(row.get("associatedExternalService")).append("</td>").append("<td>")
						.append(row.get("detailsOfExternalService")).append("</td>").append("</tr>");
			}

			contentBuilder.append("</table>");

			contentBuilder.append("<p><b>Auditor Remarks</b></p>");
			contentBuilder.append("<p>")
					.append(mainObj.getAuditorVerification() != null ? mainObj.getAuditorVerification() : "No remarks")
					.append("</p>");

		

			return contentBuilder.toString();
		}
	}

	// Helper function to create a row
	private static Map<String, String> createRow(String option, String dateOfApproval, String noDSCsIssued,
			String associatedExternalService, String detailsOfExternalService) {
		Map<String, String> row = new HashMap<>();
		row.put("option", option);
		row.put("dateOfApproval", dateOfApproval != null ? dateOfApproval : "-");
		row.put("noDSCsIssued", noDSCsIssued != null ? noDSCsIssued : "0");
		row.put("associatedExternalService", associatedExternalService != null ? associatedExternalService : "-");
		row.put("detailsOfExternalService", detailsOfExternalService != null ? detailsOfExternalService : "-");
		return row;
	}

	private String raAuditDetails(String username) {
		StringBuilder contentBuilder = new StringBuilder();
		AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", username);
		if (amain == null) {
			

			contentBuilder.append("<h3>5. RA Audit Details – During the Annual Audit Period</h3>");

			contentBuilder.append("<table>").append("<tr>").append("<th>S. No.</th>").append("<th>Description</th>")
					.append("<th>Details</th>").append("</tr>");

			contentBuilder.append("<tr>").append("<td>1</td>").append("<td>Number of RAs</td>")
					.append("<td>Total RAs - ").append("  Active RAs - ").append("</td>").append("</tr>");

			contentBuilder.append("<tr>").append("<td>2</td>").append("<td>Dates of RA Audit</td>").append("<td>")
					.append(" ").append("</td>").append("</tr>");

			contentBuilder.append("<tr>").append("<td>3</td>")
					.append("<td>Details of Non-Compliance reported by RAs</td>").append("<td>)").append(" ")
					.append("</td>").append("</tr>");

			contentBuilder.append("<tr>").append("<td>4</td>").append("<td>Action Taken by CAs</td>").append("<td>")
					.append(" ").append("</td>").append("</tr>");

			contentBuilder.append("<tr><td colspan = 5 style='text-align: left;'><b>Auditor Remarks: </b></td></tr>");

			contentBuilder.append("</table>");

			
			return contentBuilder.toString();

		} else {

			RaAuditMain mainObj4 = raAuditServ.getByAnnexureId(amain.getAnnexureMainId());

		

			contentBuilder.append("<h3 style='color: red;'>5. RA Audit Details – During the Annual Audit Period</h3>");

			contentBuilder.append("<table>").append("<tr>").append("<th>S. No.</th>").append("<th>Description</th>")
					.append("<th>Details</th>").append("</tr>");

			contentBuilder.append("<tr>").append("<td>1</td>").append("<td>Number of RAs</td>")
					.append("<td>Total RAs <input type='text' value='"
							+ (mainObj4 != null ? mainObj4.getTotalRA() : "0")
							+ "'/> Active RAs <input type='text' value='"
							+ (mainObj4 != null ? mainObj4.getActiveRA() : "0") + "'/></td>")
					.append("</tr>");

			contentBuilder.append("<tr>").append("<td>2</td>").append("<td>Dates of RA Audit</td>")
					.append("<td><input type='text' value='" + (mainObj4 != null ? mainObj4.getDatesOfRAAudit() : "")
							+ "'/></td>")
					.append("</tr>");

			contentBuilder.append("<tr>").append("<td>3</td>")
					.append("<td>Details of Non-Compliance reported by RAs</td>")
					.append("<td><input type='text' value='" + (mainObj4 != null ? mainObj4.getNcReportedByRA() : "")
							+ "'/></td>")
					.append("</tr>");

			contentBuilder.append("<tr>").append("<td>4</td>").append("<td>Action Taken by CAs</td>")
					.append("<td><input type='text' value='" + (mainObj4 != null ? mainObj4.getCaActionTaken() : "")
							+ "'/></td>")
					.append("</tr>");

			contentBuilder.append("</table>");

			contentBuilder.append("<p><b>Auditor Remarks</b></p>");
			contentBuilder.append("<p>").append(
					mainObj4.getAuditorVerification() != null ? mainObj4.getAuditorVerification() : "No remarks")
					.append("</p>");

		
			return contentBuilder.toString();
		}
	}

	private String courtCases(String username) {
		StringBuilder contentBuilder = new StringBuilder();
		AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", username);
		if (amain == null) {
			

			contentBuilder.append("<h3>6. No of Court Cases / Police Complaints</h3>");

			contentBuilder.append("<table>").append("<tr>").append("<th>S. No.</th>").append("<th>Description</th>")
					.append("<th>Details</th>").append("</tr>");
			contentBuilder.append("<tr>").append("<td>1</td>").append(
					"<td>Number of active court cases related to verification prior to issuance of DSC (Exists before the audit period)</td>")
					.append("<td>").append(" ").append("</td>").append("</tr>");

			contentBuilder.append("<tr>").append("<td>2</td>").append(
					"<td>Number of court cases related to verification prior to issuance of DSC (Registered during the audit period)</td>")
					.append("<td>").append(" ").append("</td>").append("</tr>");

			contentBuilder.append("<tr>").append("<td>3</td>").append(
					"<td>Number of police complaints against CA on DSC issuance/verification related activities</td>")
					.append("<td>").append(" ").append("</td>").append("</tr>");

			contentBuilder.append("<tr><td colspan = 5 style='text-align: left;'><b>Auditor Remarks: </b></td></tr>");

			contentBuilder.append("</table>");

			
			return contentBuilder.toString();

		} else {

			CourtCasesMain mainObj5 = courtCasesServ.getByAnnexureId(amain.getAnnexureMainId());

			
			contentBuilder.append("<h3 style='color: red;'>6. No of Court Cases / Police Complaints</h3>");

			contentBuilder.append("<table>").append("<tr>").append("<th>S. No.</th>").append("<th>Description</th>")
					.append("<th>Details</th>").append("</tr>");

			contentBuilder.append("<tr>").append("<td>1</td>").append(
					"<td>Number of active court cases related to verification prior to issuance of DSC (Exists before the audit period)</td>")
					.append("<td><input type='text' value='" + (mainObj5 != null ? mainObj5.getActiveCourtCases() : "")
							+ "'/></td>")
					.append("</tr>");

			contentBuilder.append("<tr>").append("<td>2</td>").append(
					"<td>Number of court cases related to verification prior to issuance of DSC (Registered during the audit period)</td>")
					.append("<td><input type='text' value='" + (mainObj5 != null ? mainObj5.getCourtCasesCount() : "")
							+ "'/></td>")
					.append("</tr>");

			contentBuilder.append("<tr>").append("<td>3</td>").append(
					"<td>Number of police complaints against CA on DSC issuance/verification related activities</td>")
					.append("<td><input type='text' value='"
							+ (mainObj5 != null ? mainObj5.getPoliceComplaintsCount() : "") + "'/></td>")
					.append("</tr>");

			contentBuilder.append("</table>");

			contentBuilder.append("<p><b>Auditor Remarks</b></p>");
			contentBuilder.append("<p>").append(
					mainObj5.getAuditorVerification() != null ? mainObj5.getAuditorVerification() : "No remarks")
					.append("</p>");

			
			return contentBuilder.toString();
		}
	}

	private String revocationMain(String username) {
		StringBuilder contentBuilder = new StringBuilder();
		AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", username);
		if (amain == null) {
			
			contentBuilder.append("<h3>7. No of Revocation of DSC during the Audit Period</h3>");

			contentBuilder.append("<table>").append("<tr>").append("<th>S. No.</th>").append("<th>Description</th>")
					.append("<th>Details</th>").append("</tr>");

			contentBuilder.append("<tr>").append("<td>1</td>").append("<td>Number of DSCs revoked</td>").append("<td>")
					.append(" ").append("</td>").append("</tr>");

			contentBuilder.append("<tr>").append("<td>2</td>")
					.append("<td>Number of revocation requests received from subscriber/organizations & reasons</td>")
					.append("<td>").append(" ").append("</td>").append("</tr>");

			contentBuilder.append("<tr>").append("<td>3</td>")
					.append("<td>Number of DSCs revoked by CAs & reasons</td>").append("<td>)").append(" ")
					.append("</td>").append("</tr>");

			contentBuilder.append("<tr><td colspan = 5 style='text-align: left;'><b>Auditor Remarks: </b></td></tr>");

			contentBuilder.append("</table>");

			
			return contentBuilder.toString();

		} else {

			RevocationMain mainObj6 = revocationServ.getByAnnexureId(amain.getAnnexureMainId());

			

			contentBuilder.append("<h3 style='color: red;'>7. No of Revocation of DSC during the Audit Period</h3>");

			contentBuilder.append("<table>").append("<tr>").append("<th>S. No.</th>").append("<th>Description</th>")
					.append("<th>Details</th>").append("</tr>");

			contentBuilder.append("<tr>").append("<td>1</td>").append("<td>Number of DSCs revoked</td>")
					.append("<td><input type='text' value='" + (mainObj6 != null ? mainObj6.getDscRevokedCount() : "")
							+ "'/></td>")
					.append("</tr>");

			contentBuilder.append("<tr>").append("<td>2</td>")
					.append("<td>Number of revocation requests received from subscriber/organizations & reasons</td>")
					.append("<td><input type='text' value='" + (mainObj6 != null ? mainObj6.getRevocReqReason() : "")
							+ "'/></td>")
					.append("</tr>");

			contentBuilder.append("<tr>").append("<td>3</td>")
					.append("<td>Number of DSCs revoked by CAs & reasons</td>").append("<td><input type='text' value='"
							+ (mainObj6 != null ? mainObj6.getRevocReqReason() : "") + "'/></td>")
					.append("</tr>");

			contentBuilder.append("</table>");

			contentBuilder.append("<p><b>Auditor Remarks</b></p>");
			contentBuilder.append("<p>").append(
					mainObj6.getAuditorVerification() != null ? mainObj6.getAuditorVerification() : "No remarks")
					.append("</p>");

			
			return contentBuilder.toString();
		}
	}

	private String cryptoToken(String username) {
		StringBuilder contentBuilder = new StringBuilder();

		// Fetch Annexure and eKYC Audit Details
		AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", username);
		if (amain == null) {
			

			// Title
			contentBuilder.append("<p><b>8. Empanelment of Crypto Token</b></p>");

			// Table Header
			contentBuilder.append("<table>").append("<tr>").append("<th>S. No.</th>")
					.append("<th>Brand Name of Token</th>").append("<th>Details of OEM </th>")
					.append("<th>Make in India Percentage</th>").append("<th>FIPs Certification up to</th>")
					.append("<th>Details of Security Audit Details of Crypto token</th>").append("</tr>");
			for (int i = 0; i < 5; i++) {
				contentBuilder.append("<tr>").append("<td>").append(" ").append("</td>") // Serial Number
						.append("<td>").append(" ").append("</td>").append("<td>").append(" ").append("</td>")
						.append("<td>").append(" ").append("</td>").append("<td>").append(" ").append("</td>")
						.append("<td>").append(" ").append("</td>").append("</tr>");
			}
			contentBuilder.append("<tr><td colspan = 6 style='text-align: left;'><b>Auditor Remarks: </b></td></tr>");

			contentBuilder.append("</table>");

			
			return contentBuilder.toString();
		} else {

			CryptoTokenMain mainObj = cryptoTokenMainServ.getByAnnexureId(amain.getAnnexureMainId());
			if (mainObj == null) {
				return "<p>No eKYC account audit details available.</p>";
			}

			

			// Title
			contentBuilder.append("<p><b>8. Empanelment of Crypto Token</b></p>");

			// Table Header
			contentBuilder.append("<table>").append("<tr>").append("<th>S. No.</th>")
					.append("<th>Brand Name of Token</th>").append("<th>Details of OEM </th>")
					.append("<th>Make in India Percentage</th>").append("<th>FIPs Certification up to</th>")
					.append("<th>Details of Security Audit Details of Crypto token</th>").append("</tr>");

			// Fetch list of eKYC audit records
			List<CryptoTokenDetails> list = cryptoTokenDetailsServ.getByCryptoTokenMainId(mainObj.getCryptoTokMainId());

			if (list.isEmpty()) {
				contentBuilder.append("<tr><td colspan='6'>No eKYC account audit records found.</td></tr>");
			} else {
				int serialNumber = 1;
				for (CryptoTokenDetails row : list) {
					contentBuilder.append("<tr>").append("<td>").append(serialNumber++).append("</td>") // Serial Number
							.append("<td>").append(row.getBrandName() != null ? row.getBrandName() : "N/A")
							.append("</td>").append("<td>")
							.append(row.getOemDetails() != null ? row.getOemDetails() : "N/A").append("</td>")
							.append("<td>").append(row.getMakInPercentage() != null ? row.getMakInPercentage() : "N/A")
							.append("</td>").append("<td>")
							.append(row.getFipCertUpTo() != null ? row.getFipCertUpTo() : "N/A").append("</td>")
							.append("<td>").append(row.getSecAuditDetails() != null ? row.getSecAuditDetails() : "N/A")
							.append("</td>").append("</tr>");
				}
			}

			contentBuilder.append("</table>");

			contentBuilder.append("<p><b>Auditor Remarks</b></p>");
			contentBuilder.append("<p>")
					.append(mainObj.getAuditorVerification() != null ? mainObj.getAuditorVerification() : "No remarks")
					.append("</p>");

			

			return contentBuilder.toString();
		}
	}

	private String caSwWebMain(String username) {
		StringBuilder contentBuilder = new StringBuilder();

		// Fetch Annexure and eKYC Audit Details
		AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", username);
		if (amain == null) {
			

			// Title
			contentBuilder.append("<p><b>9. Details of CA Software/Website</b></p>");

			// Table Header
			contentBuilder.append("<table>").append("<tr>").append("<th>S. No.</th>").append("<th>Description</th>")
					.append("<th>Developed By</th>").append("<th>Database Used</th>").append("<th>Certification</th>")
					.append("<th>Last Security Audit</th>").append("</tr>");

			for (int i = 0; i < 6; i++) {
				contentBuilder.append("<tr>").append("<td>").append(" ").append("</td>") // Serial Number
						.append("<td>").append(" ").append("</td>").append("<td>").append(" ").append("</td>")
						.append("<td>").append(" ").append("</td>").append("<td>").append(" ").append("</td>")
						.append("<td>").append(" ").append("</td>").append("</tr>");
			}
			contentBuilder.append("<tr><td colspan = 6 style='text-align: left;'><b>Auditor Remarks: </b></td></tr>");

			contentBuilder.append("</table>");

		
			return contentBuilder.toString();

		} else {

			CaSwWebMain mainObj = caSwWebMainServ.getByAnnexureId(amain.getAnnexureMainId());
			if (mainObj == null) {
				return "<p>No eKYC account audit details available.</p>";
			}

		

			// Title
			contentBuilder.append("<p><b>9. Details of CA Software/Website</b></p>");

			// Table Header
			contentBuilder.append("<table>").append("<tr>").append("<th>S. No.</th>").append("<th>Description</th>")
					.append("<th>Developed By</th>").append("<th>Database Used</th>").append("<th>Certification</th>")
					.append("<th>Last Security Audit</th>").append("</tr>");

			// Fetch list of eKYC audit records
			List<CaSwWebDetails> list = caSwWebDetailsServ.getByCaSwWebMainId(mainObj.getCaWebMainId());

			if (list.isEmpty()) {
				contentBuilder.append("<tr><td colspan='6'>No eKYC account audit records found.</td></tr>");
			} else {
				int serialNumber = 1;
				for (CaSwWebDetails row : list) {
					contentBuilder.append("<tr>").append("<td>").append(serialNumber++).append("</td>") // Serial Number
							.append("<td>").append(row.getDescription() != null ? row.getDescription() : "N/A")
							.append("</td>").append("<td>")
							.append(row.getDevelopedBy() != null ? row.getDevelopedBy() : "N/A").append("</td>")
							.append("<td>").append(row.getDatabaseUsed() != null ? row.getDatabaseUsed() : "N/A")
							.append("</td>").append("<td>")
							.append(row.getCertification() != null ? row.getCertification() : "N/A").append("</td>")
							.append("<td>")
							.append(row.getLastSecurityAudit() != null ? row.getLastSecurityAudit() : "N/A")
							.append("</td>").append("</tr>");
				}
			}

			contentBuilder.append("</table>");

			contentBuilder.append("<p><b>Auditor Remarks</b></p>");
			contentBuilder.append("<p>")
					.append(mainObj.getAuditorVerification() != null ? mainObj.getAuditorVerification() : "No remarks")
					.append("</p>");

			

			return contentBuilder.toString();
		}
	}

	private String locationMain(String username) {
		StringBuilder contentBuilder = new StringBuilder();

		// Fetch Annexure and eKYC Audit Details
		AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", username);
		if (amain == null) {
			

			// Title
			contentBuilder.append("<p><b>10. Details of DC & DR Site</b></p>");

			// Table Header
			contentBuilder.append("<table>").append("<tr>").append("<th>S. No.</th>").append("<th>Description</th>")
					.append("<th>Location</th>").append("<thNo of CA Administrators</th>")
					.append("<th>No of System Administrators</th>").append("<th>No of CA Operators</th>")
					.append("<th>No of Verification Officers</th>").append("<th>Total CA Manpower</th>")
					.append("</tr>");
			for (int i = 0; i < 5; i++) {
				contentBuilder.append("<tr>").append("<td>").append(" ").append("</td>") // Serial Number
						.append("<td>").append(" ").append("</td>").append("<td>").append(" ").append("</td>")
						.append("<td>").append(" ").append("</td>").append("<td>").append(" ").append("</td>")
						.append("<td>").append(" ").append("</td>").append("<td>").append(" ").append("</td>").append("</tr>");
			}
			contentBuilder.append("<tr><td colspan = 7 style='text-align: left;'><b>Auditor Remarks: </b></td></tr>");

			contentBuilder.append("</table>");

		
			return contentBuilder.toString();
		} else {

			LocationMain mainObj = locationMainServ.getByAnnexureId(amain.getAnnexureMainId());
			if (mainObj == null) {
				return "<p>No Details of DC & DR Site available.</p>";
			}

			

			// Title
			contentBuilder.append("<p><b>10. Details of DC & DR Site</b></p>");

			// Table Header
			contentBuilder.append("<table>").append("<tr>").append("<th>S. No.</th>").append("<th>Description</th>")
					.append("<th>Location</th>").append("<thNo of CA Administrators</th>")
					.append("<th>No of System Administrators</th>").append("<th>No of CA Operators</th>")
					.append("<th>No of Verification Officers</th>").append("<th>Total CA Manpower</th>")
					.append("</tr>");

			// Fetch list of eKYC audit records
			List<LocationDetails> list = locationDetailsServ.getByLocationMainId(mainObj.getLocationMainId());

			if (list.isEmpty()) {
				contentBuilder.append("<tr><td colspan='6'>No eKYC account audit records found.</td></tr>");
			} else {
				int serialNumber = 1;
				for (LocationDetails row : list) {
					contentBuilder.append("<tr>").append("<td>").append(serialNumber++).append("</td>") // Serial Number
							.append("<td>").append(row.getDescription() != null ? row.getDescription() : "N/A")
							.append("</td>").append("<td>")
							.append(row.getLocation() != null ? row.getLocation() : "N/A").append("</td>")
							.append("<td>")
							.append(row.getCaAdministratorCount() != null ? row.getCaAdministratorCount() : "N/A")
							.append("</td>").append("<td>")
							.append(row.getSysAdministratorCount() != null ? row.getSysAdministratorCount() : "N/A")
							.append("</td>").append("<td>")
							.append(row.getCaOperatorsCount() != null ? row.getCaOperatorsCount() : "N/A")
							.append("</td>").append("<td>")
							.append(row.getVerificationOfficersCount() != null ? row.getVerificationOfficersCount()
									: "N/A")
							.append("</td>").append("<td>")
							.append(row.getCaManpowerCount() != null ? row.getCaManpowerCount() : "N/A").append("</td>")
							.append("</tr>");
				}
			}

			contentBuilder.append("</table>");

			contentBuilder.append("<p><b>Auditor Remarks</b></p>");
			contentBuilder.append("<p>")
					.append(mainObj.getAuditorVerification() != null ? mainObj.getAuditorVerification() : "No remarks")
					.append("</p>");

			

			return contentBuilder.toString();
		}
	}

	private String cAServiceMain(String username) {
		StringBuilder contentBuilder = new StringBuilder();

		// Fetch Annexure and eKYC Audit Details
		AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", username);
		if (amain == null) {
			
			// Title
			contentBuilder.append("<p><b>11. Details of CA Services</b></p>");

			// Table Header
			contentBuilder.append("<table>").append("<tr>").append("<th>S. No.</th>").append("<th>Description</th>")
					.append("<th>Internal Only</th>").append("<th>External Service</th>")
					.append("<th>No of ASPs/ Organizations</th>").append("</tr>");
			for (int i = 0; i < 5; i++) {
				contentBuilder.append("<tr>").append("<td>").append(" ").append("</td>") // Serial Number
						.append("<td>").append(" ").append("</td>").append("<td>").append(" ").append("</td>")
						.append("<td>").append(" ").append("</td>").append("<td>").append(" ").append("</td>")
						.append("</tr>");
			}
			contentBuilder.append("<tr><td colspan = 5 style='text-align: left;'><b>Auditor Remarks: </b></td></tr>");

			contentBuilder.append("</table>");

			
			return contentBuilder.toString();
		} else {

			CAServicesMain mainObj = caServicesMainServ.getByAnnexureId(amain.getAnnexureMainId());
			if (mainObj == null) {
				return "<p>No  Details of CA Services available.</p>";
			}

		

			// Title
			contentBuilder.append("<p><b>11. Details of CA Services</b></p>");

			// Table Header
			contentBuilder.append("<table>").append("<tr>").append("<th>S. No.</th>").append("<th>Description</th>")
					.append("<th>Internal Only</th>").append("<th>External Service</th>")
					.append("<th>No of ASPs/ Organizations</th>").append("</tr>");

			// Fetch list of eKYC audit records
			List<CAServicesDetails> list = caServicesDetailsServ.getByCAServicesMainId(mainObj.getCaServicesMainId());

			if (list.isEmpty()) {
				contentBuilder.append("<tr><td colspan='6'>No  Details of CA Services records found.</td></tr>");
			} else {
				int serialNumber = 1;
				for (CAServicesDetails row : list) {
					contentBuilder.append("<tr>").append("<td>").append(serialNumber++).append("</td>") // Serial Number
							.append("<td>").append(row.getDescription() != null ? row.getDescription() : "N/A")
							.append("</td>").append("<td>")
							.append(row.getInternalOnly() != null ? row.getInternalOnly() : "N/A").append("</td>")
							.append("<td>").append(row.getExternalOnly() != null ? row.getExternalOnly() : "N/A")
							.append("</td>").append("<td>")
							.append(row.getAspOrgCountFile() != null ? row.getAspOrgCountFile() : "N/A").append("</td>")
							.append("</tr>");
				}
			}

			contentBuilder.append("</table>");

			contentBuilder.append("<p><b>Auditor Remarks</b></p>");
			contentBuilder.append("<p>")
					.append(mainObj.getAuditorVerification() != null ? mainObj.getAuditorVerification() : "No remarks")
					.append("</p>");

			

			return contentBuilder.toString();
		}
	}

	// 12

	private String aSPDetails(String username) {
		StringBuilder contentBuilder = new StringBuilder();
		AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", username);
		if (amain == null) {
			
			contentBuilder.append("<h3>12. Details of ASP</h3>");

			contentBuilder.append("<table>").append("<tr>").append("<th>S. No.</th>").append("<th>Description</th>")
					.append("<th>Details</th>").append("</tr>");
			contentBuilder.append("<tr>").append("<td>1</td>").append("<td>No of ASPs</td>").append("<td>").append(" ")
					.append("</td>").append("</tr>");

			contentBuilder.append("<tr>").append("<td>2</td>")
					.append("<td>No of ASPs whose audit exceed more than One year</td>").append("<td>").append(" ")
					.append("</td>").append("</tr>");
			contentBuilder.append("<tr><td colspan = 3 style='text-align: left;'><b>Auditor Remarks: </b></td></tr>");

			contentBuilder.append("</table>");

			
			return contentBuilder.toString();

		}

		ASPDetails mainObj11 = aspDetailsServ.getByAnnexureId(amain.getAnnexureMainId());

		

		contentBuilder.append("<h3 style='color: red;'>12. Details of ASP</h3>");

		contentBuilder.append("<table>").append("<tr>").append("<th>S. No.</th>").append("<th>Description</th>")
				.append("<th>Details</th>").append("</tr>");

		contentBuilder.append("<tr>").append("<td>1</td>").append("<td>No of ASPs</td>").append(
				"<td><input type='text' value='" + (mainObj11 != null ? mainObj11.getAspCount() : "") + "'/></td>")
				.append("</tr>");

		contentBuilder.append("<tr>").append("<td>2</td>")
				.append("<td>No of ASPs whose audit exceed more than One year</td>")
				.append("<td><input type='text' value='"
						+ (mainObj11 != null ? mainObj11.getAspsAuditOverdueCount() : "") + "'/></td>")
				.append("</tr>");

		contentBuilder.append("</table>");

		contentBuilder.append("<p><b>Auditor Remarks</b></p>");
		contentBuilder.append("<p>")
				.append(mainObj11.getAuditorVerification() != null ? mainObj11.getAuditorVerification() : "No remarks")
				.append("</p>");

		
		return contentBuilder.toString();
	}

	// 13

	private String publicInfoMain(String username) {
		StringBuilder contentBuilder = new StringBuilder();

		// Fetch Annexure and eKYC Audit Details
		AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", username);
		if (amain == null) {
		

			// Title
			contentBuilder.append("<p><b>13. Public Information maintained at the website of CA</b></p>");

			// Table Header
			contentBuilder.append("<table>").append("<tr>").append("<th>S. No.</th>").append("<th>Description</th>")
					.append("<th>Website Link</th>").append("</tr>");
			for (int i = 0; i < 4; i++) {
				contentBuilder.append("<tr>").append("<td>").append(" ").append("</td>") // Serial Number
						.append("<td>").append(" ").append("</td>").append("<td>").append(" ").append("</td>")
						.append("</tr>");
			}
			contentBuilder.append("<tr><td colspan = 3 style='text-align: left;'><b>Auditor Remarks: </b></td></tr>");

			contentBuilder.append("</table>");

			
			return contentBuilder.toString();

		} else {

			PublicInfoMain mainObj = publicInfoMainServ.getByAnnexureId(amain.getAnnexureMainId());
			if (mainObj == null) {
				return "<p>No  Public Information maintained available.</p>";
			}

			

			// Title
			contentBuilder.append("<p><b>13. Public Information maintained at the website of CA</b></p>");

			// Table Header
			contentBuilder.append("<table>").append("<tr>").append("<th>S. No.</th>").append("<th>Description</th>")
					.append("<th>Website Link</th>").append("</tr>");

			// Fetch list of eKYC audit records
			List<PublicInfoDetails> list = publicInfoDetailsServ.getByPublicInfoMainId(mainObj.getPublicInfoMainId());

			if (list.isEmpty()) {
				contentBuilder.append("<tr><td colspan='6'>No  Public Information maintained records found.</td></tr>");
			} else {
				int serialNumber = 1;
				for (PublicInfoDetails row : list) {
					contentBuilder.append("<tr>").append("<td>").append(serialNumber++).append("</td>") // Serial Number
							.append("<td>").append(row.getDescription() != null ? row.getDescription() : "N/A")
							.append("</td>").append("<td>").append(row.getWebLink() != null ? row.getWebLink() : "N/A")
							.append("</td>").append("</tr>");
				}
			}

			contentBuilder.append("</table>");

			contentBuilder.append("<p><b>Auditor Remarks</b></p>");
			contentBuilder.append("<p>")
					.append(mainObj.getAuditorVerification() != null ? mainObj.getAuditorVerification() : "No remarks")
					.append("</p>");

			

			return contentBuilder.toString();
		}
	}

	// 14

	private String certificateCost(String username) {
		StringBuilder contentBuilder = new StringBuilder();
		AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", username);
		if (amain == null) {
		

			contentBuilder.append("<h3>14. Cost of Certificates issued during audit period</h3>");

			contentBuilder.append("<table>").append("<tr>").append("<th>S. No.</th>").append("<th>Description</th>")
					.append("<th>Details</th>").append("</tr>");

			contentBuilder.append("<tr>").append("<td>1</td>").append(
					"<td>Average expenditure for issuance of one DSC and maintenance of the details for a period of 7 years after expiry of DSC</td>")
					.append("<td>").append(" ").append("</td>").append("</tr>");

			contentBuilder.append("<tr>").append("<td>2</td>").append("<td>Average fee charged for one DSC by CA</td>")
					.append("<td>").append(" ").append("</td>").append("</tr>");

			contentBuilder.append("<tr>").append("<td>3</td>").append(
					"<td>Detailed explanation, sustainability plan if the average fee charged is less than average cost of certificates</td>")
					.append("<td>").append(" ").append("</td>").append("</tr>");
			contentBuilder.append("<tr><td colspan = 3 style='text-align: left;'><b>Auditor Remarks: </b></td></tr>");

			contentBuilder.append("</table>");

		
			return contentBuilder.toString();
		}

		CertificateCost mainObj13 = certificateCostServ.getByAnnexureId(amain.getAnnexureMainId());

		

		contentBuilder.append("<h3 style='color: red;'>14. Cost of Certificates issued during audit period</h3>");

		contentBuilder.append("<table>").append("<tr>").append("<th>S. No.</th>").append("<th>Description</th>")
				.append("<th>Details</th>").append("</tr>");

		contentBuilder.append("<tr>").append("<td>1</td>").append(
				"<td>Average expenditure for issuance of one DSC and maintenance of the details for a period of 7 years after expiry of DSC</td>")
				.append("<td><input type='text' value='"
						+ (mainObj13 != null ? mainObj13.getAvgDscIssuMaintenanceCost() : "") + "'/></td>")
				.append("</tr>");

		contentBuilder.append("<tr>").append("<td>2</td>").append("<td>Average fee charged for one DSC by CA</td>")
				.append("<td><input type='text' value='" + (mainObj13 != null ? mainObj13.getAvgFeeChargedByCA() : "")
						+ "'/></td>")
				.append("</tr>");

		contentBuilder.append("<tr>").append("<td>2</td>").append(
				"<td>Detailed explanation, sustainability plan if the average fee charged is less than average cost of certificates</td>")
				.append("<td><input type='text' value='" + (mainObj13 != null ? mainObj13.getAvgFeeChargedByCA() : "")
						+ "'/></td>")
				.append("</tr>");

		contentBuilder.append("</table>");

		contentBuilder.append("<p><b>Auditor Remarks</b></p>");
		contentBuilder.append("<p>")
				.append(mainObj13.getAuditorVerification() != null ? mainObj13.getAuditorVerification() : "No remarks")
				.append("</p>");

		
		return contentBuilder.toString();
	}

	// 15

	private String selfAssessmentMain(String username) {
		StringBuilder contentBuilder = new StringBuilder();
		AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", username);
		if (amain == null) {
			

			contentBuilder.append("<h3>15. CA Self-assessment for audit period</h3>");

			contentBuilder.append("<table>").append("<tr>").append("<th>S. No.</th>").append("<th>Name</th>")
					.append("<th>Details</th>").append("</tr>");

			contentBuilder.append("<tr>").append("<td>1</td>").append(
					"<td>No of DSC issued without any DSC application Forms, if any & reason for non-compliance.</td>")
					.append("<td>").append(" ").append("</td>")
					.append("</tr>");

			contentBuilder.append("<tr>").append("<td>2</td>")
					.append("<td>No of DSC issued without charging fee, if any & details.</td>").append("<td>")
					.append(" ").append("</td>").append("</tr>");

			contentBuilder.append("<tr>").append("<td>3</td>").append(
					"<td>No DSC issued without having physical verification(video/Biometric Aadhaar) if any</td>")
					.append("<td>").append(" ").append("</td>").append("</tr>");

			contentBuilder.append("<tr>").append("<td>4</td>").append(
					"<td>No of DSC issued(except for foreign nationals) whose name is not matching with as that of in Aadhaar or PAN, if any & reason for noncompliance</td>")
					.append("<td>").append(" ").append("</td>").append("</tr>");

			contentBuilder.append("<tr>").append("<td>5</td>").append(
					"<td>Whether CA system allows sending common OTP to many customers? If Yes, reason for such non-compliance</td>")
					.append("<td>").append(" ").append("</td>").append("</tr>");

			contentBuilder.append("<tr>").append("<td>6</td>").append(
					"<td>Whether access to CA system is based on single URL-point or allows Link based access. If link-based access allowed, provide details in respect of coverage of such access in the security audit, annual audit & internal audit. The details of vulnerabilities and non-compliance noted for each type of links.</td>")
					.append("<td>").append(" ").append("</td>").append("</tr>");

			contentBuilder.append("<tr>").append("<td>7</td>").append(
					"<td>The effort taken by CA to find out the own-noncompliance and action Taken during the audit period.</td>")
					.append("<td>").append(" ").append("</td>").append("</tr>");

			contentBuilder.append("<tr><td colspan = 3 style='text-align: left;'><b>Auditor Remarks: </b></td></tr>");

			contentBuilder.append("</table>");

			
			return contentBuilder.toString();
		} else {

			SelfAssessmentMain mainObj14 = selfServ.getByAnnexureId(amain.getAnnexureMainId());

			

			contentBuilder.append("<h3 style='color: red;'>15. CA Self-assessment for audit period</h3>");

			contentBuilder.append("<table>").append("<tr>").append("<th>S. No.</th>").append("<th>Name</th>")
					.append("<th>Details</th>").append("</tr>");

			contentBuilder.append("<tr>").append("<td>1</td>").append(
					"<td>No of DSC issued without any DSC application Forms, if any & reason for non-compliance.</td>")
					.append("<td><input type='text' value='"
							+ (mainObj14 != null ? mainObj14.getDscIssuedCountWAppForm() : "") + "'/></td>")
					.append("<td><input type='text' value='" + (mainObj14 != null ? mainObj14.getDscIssuedWMatch() : "")
							+ "'/></td>")
					.append("</tr>");

			contentBuilder.append("<tr>").append("<td>2</td>")
					.append("<td>No of DSC issued without charging fee, if any & details.</td>")
					.append("<td><input type='text' value='" + (mainObj14 != null ? mainObj14.getDscIssuedWFee() : "")
							+ "'/></td>")
					.append("<td><input type='text' value='"
							+ (mainObj14 != null ? mainObj14.getDetailsDSCIssuedWFee() : "") + "'/></td>")
					.append("</tr>");

			contentBuilder.append("<tr>").append("<td>3</td>").append(
					"<td>No DSC issued without having physical verification(video/Biometric Aadhaar) if any</td>")
					.append("<td><input type='text' value='"
							+ (mainObj14 != null ? mainObj14.getDscIssuedWPhysicalVer() : "") + "'/></td>")
					.append("</tr>");

			contentBuilder.append("<tr>").append("<td>4</td>").append(
					"<td>No of DSC issued(except for foreign nationals) whose name is not matching with as that of in Aadhaar or PAN, if any & reason for noncompliance</td>")
					.append("<td><input type='text' value='" + (mainObj14 != null ? mainObj14.getOwnNCDetails() : "")
							+ "'/></td>")
					.append("</tr>");

			contentBuilder.append("<tr>").append("<td>5</td>").append(
					"<td>Whether CA system allows sending common OTP to many customers? If Yes, reason for such non-compliance</td>")
					.append("<td><input type='text' value='"
							+ (mainObj14 != null ? mainObj14.getDscIssuedWPhysicalVer() : "") + "'/></td>")
					.append("</tr>");

			contentBuilder.append("<tr>").append("<td>6</td>").append(
					"<td>Whether access to CA system is based on single URL-point or allows Link based access. If link-based access allowed, provide details in respect of coverage of such access in the security audit, annual audit & internal audit. The details of vulnerabilities and non-compliance noted for each type of links.</td>")
					.append("<td><input type='text' value='"
							+ (mainObj14 != null ? mainObj14.getDscIssuedWPhysicalVer() : "") + "'/></td>")
					.append("</tr>");

			contentBuilder.append("<tr>").append("<td>7</td>").append(
					"<td>The effort taken by CA to find out the own-noncompliance and action Taken during the audit period.</td>")
					.append("<td><input type='text' value='"
							+ (mainObj14 != null ? mainObj14.getDscIssuedWPhysicalVer() : "") + "'/></td>")
					.append("</tr>");

			contentBuilder.append("</table>");

			contentBuilder.append("<p><b>Auditor Remarks</b></p>");
			contentBuilder.append("<p>").append(
					mainObj14.getAuditorVerification() != null ? mainObj14.getAuditorVerification() : "No remarks")
					.append("</p>");

			
			return contentBuilder.toString();
		}
	}

	// 16

	private String caSoftwareAndExternal(String username) {

		StringBuilder contentBuilder = new StringBuilder();

		// Fetch Annexure and eKYC Audit Details
		AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", username);
		if (amain == null) {
			
			// Title
			contentBuilder.append("<p><b>16. CA Software and external connectivity</b></p>");

			// Table Header
			contentBuilder.append("<table>").append("<tr>").append("<th>S. No.</th>").append("<th>Name</th>")
					.append("<th>Details</th>").append("</tr>");
			for (int i = 0; i < 12; i++) {
				contentBuilder.append("<tr>").append("<td>").append(" ").append("</td>") // Serial Number
						.append("<td>").append(" ").append("</td>").append("<td>").append(" ").append("</td>")
						.append("</tr>");
			}
			contentBuilder.append("<tr><td colspan = 3 style='text-align: left;'><b>Auditor Remarks: </b></td></tr>");

			contentBuilder.append("</table>");

			
			return contentBuilder.toString();
		} else {

			ConnectivityMain mainObj = connectivityMainServ.getByAnnexureId(amain.getAnnexureMainId());
			if (mainObj == null) {
				return "<p>No CA Software and external connectivity available.</p>";
			}

			
			// Title
			contentBuilder.append("<p><b>16. CA Software and external connectivity</b></p>");

			// Table Header
			contentBuilder.append("<table>").append("<tr>").append("<th>S. No.</th>").append("<th>Name</th>")
					.append("<th>Details</th>").append("</tr>");

			// Fetch list of eKYC audit records
			List<ConnectivityDetails> list = connectivityDetailsServ
					.getByConnectivityMainId(mainObj.getConnectivityMainId());

			if (list.isEmpty()) {
				contentBuilder.append(
						"<tr><td colspan='6'>No CA Software and external connectivity records found.</td></tr>");
			} else {
				int serialNumber = 1;
				for (ConnectivityDetails row : list) {
					contentBuilder.append("<tr>").append("<td>").append(serialNumber++).append("</td>") // Serial Number
							.append("<td>").append(row.getName() != null ? row.getName() : "N/A").append("</td>")
							.append("<td>").append(row.getDescription() != null ? row.getDescription() : "N/A")
							.append("</td>").append("</tr>");
				}
			}

			contentBuilder.append("</table>");

			contentBuilder.append("<p><b>Auditor Remarks</b></p>");
			contentBuilder.append("<p>")
					.append(mainObj.getAuditorVerification() != null ? mainObj.getAuditorVerification() : "No remarks")
					.append("</p>");

		

			return contentBuilder.toString();
		}
	}

	// 17

	private String downTimeDuringAudit(String username) {
		StringBuilder contentBuilder = new StringBuilder();
		AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", username);
		if (amain == null) {
			

			contentBuilder.append("<h3>15. CA Self-assessment for audit period</h3>");

			contentBuilder.append("<table>").append("<tr>").append("<th>S. No.</th>").append("<th>Name</th>")
					.append("<th>Details</th>").append("</tr>");

			contentBuilder.append("<tr>").append("<td>1</td>")
					.append("<td>Total service down time during the audit period, if any.</td>").append("<td>")
					.append(" ").append("</td>").append("</td>").append("</tr>");

			contentBuilder.append("<tr>").append("<td>2</td>")
					.append("<td>Reason for non-availability of service and remedial measures taken.</td>")
					.append("<td>").append(" ").append("</td>").append("</tr>");

			contentBuilder.append("<tr><td colspan = 3 style='text-align: left;'><b>Auditor Remarks: </b></td></tr>");

			contentBuilder.append("</table>");

		
			return contentBuilder.toString();
		} else {

			DownTime mainObj16 = downTimeServ.getByAnnexureId(amain.getAnnexureMainId());

		

			contentBuilder.append("<h3 style='color: red;'>15. CA Self-assessment for audit period</h3>");

			contentBuilder.append("<table>").append("<tr>").append("<th>S. No.</th>").append("<th>Name</th>")
					.append("<th>Details</th>").append("</tr>");

			contentBuilder.append("<tr>").append("<td>1</td>")
					.append("<td>Total service down time during the audit period, if any.</td>")
					.append("<td><input type='text' value='" + (mainObj16 != null ? mainObj16.getDownTimeHour() : "")
							+ "'/></td>")
					.append("<td><input type='text' value='" + (mainObj16 != null ? mainObj16.getDownTimeMinute() : "")
							+ "'/></td>")
					.append("<td><input type='text' value='" + (mainObj16 != null ? mainObj16.getDownTimeSecond() : "")
							+ "'/></td>")
					.append("</tr>");

			contentBuilder.append("<tr>").append("<td>2</td>")
					.append("<td>Reason for non-availability of service and remedial measures taken.</td>")
					.append("<td><input type='text' value='"
							+ (mainObj16 != null ? mainObj16.getReasonAndMeasuresTaken() : "") + "'/></td>")
					.append("</tr>");

			contentBuilder.append("</table>");

			contentBuilder.append("<p><b>Auditor Remarks</b></p>");
			contentBuilder.append("<p>").append(
					mainObj16.getAuditorVerification() != null ? mainObj16.getAuditorVerification() : "No remarks")
					.append("</p>");

			
			return contentBuilder.toString();
		}
	}

	// 18

	private String caTrustedPersons(String username) {

		StringBuilder contentBuilder = new StringBuilder();

		// Fetch Annexure and eKYC Audit Details
		AnnexureMain amain = annexServ.getAnnexureByStatusAndUsername("Active", username);
		if (amain == null) {
			

			// Title
			contentBuilder.append("<p><b>18. List of CA Trusted Persons</b></p>");

			contentBuilder.append("<table>").append("<tr>").append("<th>S. No.</th>").append("<th>Name</th>")
					.append("<th>Designation</th>").append("<th>Location of<br/>Posting<br/>DC/DR</th>")
					.append("<th>Role in CA</th>").append("<th>ID Card No<br/>& Mobile</th>")
					.append("<th>Identification<br/>Details in the CA<br/>Payroll</th>")
					.append("<th>Employed<br/>Since</th>").append("<th>Training details</th>")
					.append("<th>Date of last<br/>background<br/>verification</th>").append("</tr>");

			for (int i = 0; i < 12; i++) {
				contentBuilder.append("<tr>").append("<td>").append(" ").append("</td>").append("<td>").append(" ")
						.append("</td>").append("<td>").append(" ").append("</td>").append("<td>").append(" ")
						.append("</td>").append("<td>").append(" ").append("</td>").append("<td>").append(" ")
						.append("</td>").append("<td>").append(" ").append("</td>").append("<td>").append(" ")
						.append("</td>").append("<td>").append(" ").append("</td>").append("<td>").append(" ")
						.append("</td>").append("</tr>");
			}
			contentBuilder.append("<tr><td colspan = 10 style='text-align: left;'><b>Auditor Remarks: </b></td></tr>");

			contentBuilder.append("</table>");

			
			return contentBuilder.toString();
		} else {

			CATrustedPersonMain mainObj = personMainServ.getByAnnexureId(amain.getAnnexureMainId());
			if (mainObj == null) {
				return "<p>No CA Trusted Persons available.</p>";
			}

		
			// Title
			contentBuilder.append("<p><b>18. List of CA Trusted Persons</b></p>");

			contentBuilder.append("<table>").append("<tr>").append("<th>S. No.</th>").append("<th>Name</th>")
					.append("<th>Designation</th>").append("<th>Location of<br/>Posting<br/>DC/DR</th>")
					.append("<th>Role in CA</th>").append("<th>ID Card No<br/>& Mobile</th>")
					.append("<th>Identification<br/>Details in the CA<br/>Payroll</th>")
					.append("<th>Employed<br/>Since</th>").append("<th>Training details</th>")
					.append("<th>Date of last<br/>background<br/>verification</th>").append("</tr>");

			// Fetch list of eKYC audit records
			List<CATrustedPerson> list = personServ.getByCATrustedPersonMainId(mainObj.getPersonMainId());

			if (list.isEmpty()) {
				contentBuilder.append("<tr><td colspan='6'>No CA Trusted Persons records found.</td></tr>");
			} else {
				int serialNumber = 1;
				for (CATrustedPerson row : list) {
					contentBuilder.append("<tr>").append("<td>").append(serialNumber++).append("</td>").append("<td>")
							.append(row.getFullName() != null ? row.getFullName() : "N/A").append("</td>")
							.append("<td>").append(row.getDesignation() != null ? row.getDesignation() : "N/A")
							.append("</td>").append("<td>")
							.append(row.getLocationOfPosting() != null ? row.getLocationOfPosting() : "N/A")
							.append("</td>").append("<td>").append(row.getRole() != null ? row.getRole() : "N/A")
							.append("</td>").append("<td>")
							.append(row.getIdCardNo() != null ? row.getIdCardNo() : "N/A").append("</td>")
							.append("<td>").append(row.getMobileNo() != null ? row.getMobileNo() : "N/A")
							.append("</td>").append("<td>")
							.append(row.getIdentificationDetails() != null ? row.getIdentificationDetails() : "N/A")
							.append("</td>").append("<td>")
							.append(row.getEmployedSince() != null ? row.getEmployedSince() : "N/A").append("</td>")
							.append("<td>").append(row.getTrainingDetails() != null ? row.getTrainingDetails() : "N/A")
							.append("</td>").append("<td>")
							.append(row.getLastBackVerDate() != null ? row.getLastBackVerDate() : "N/A").append("</td>")
							.append("</tr>");
				}
			}

			contentBuilder.append("</table>");

			contentBuilder.append("<p><b>Auditor Remarks</b></p>");
			contentBuilder.append("<p>")
					.append(mainObj.getAuditorVerification() != null ? mainObj.getAuditorVerification() : "No remarks")
					.append("</p>");


			return contentBuilder.toString();
		}
	}

	@PostMapping(value = "/download-auditor-report")
	public ResponseEntity<?> downloadAuditorReport(
	        @RequestParam("applicantUserName") String userName,
	        @RequestParam(value = "auditCoverReport", required = false) MultipartFile auditCoverReport,
	        @RequestParam(value ="mainAuditReport" , required = false) MultipartFile mainAuditReport,
	        @RequestParam(value ="annexureA1" , required = false) MultipartFile annexureA1,
	        @RequestParam(value ="annexureA2" , required = false) MultipartFile annexureA2,
	        @RequestParam(value ="annexureA3" , required = false) MultipartFile annexureA3,
	        @RequestParam(value ="annexureA4" , required = false) MultipartFile annexureA4,
	        @RequestParam(value ="annexureA5" , required = false) MultipartFile annexureA5,
	        @RequestParam(value ="annexureA6" , required = false) MultipartFile annexureA6,
	        @RequestParam(value ="annexureVI" , required = false) MultipartFile annexureVI,
	        @RequestParam(value ="annexureVIII" , required = false) MultipartFile annexureVIII,
	        @RequestParam(value ="annexureVIIIRepeat" , required = false) MultipartFile annexureVIIIRepeat,
	        @RequestParam(value ="annexureVIIIUndertaking" , required = false) MultipartFile annexureVIIIUndertaking) {

	    if (userName == null || userName.trim().isEmpty()) {
	        return ResponseEntity.badRequest().body("Username is required");
	    }
	    String sanitizedUserName = userName.replaceAll("[^a-zA-Z0-9]", "");
	    if (sanitizedUserName.isEmpty()) {
	        return ResponseEntity.badRequest().body("Invalid username format");
	    }

	    try {
	        List<AuditCriteriaPayload> criteriaPayload = applicationAuditorsService.getAllDatas(sanitizedUserName);
	        ApplicationAuditEntity applicationAuditEntity = applicationAuditServ.getByApplicantUserName(sanitizedUserName);
	        if (applicationAuditEntity == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No audit data found for user: " + sanitizedUserName);
	        }
	        List<AuditReportCriteriaEntity> auditReportCriteriaEntities = auditReportCriteriaService.getAllDatas(applicationAuditEntity);

	        Optional<ESignedDocumentsEntity> optional = resolveOrCreatePendingEsignRecord(sanitizedUserName);
	        if (!optional.isPresent()) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save eSigned document");
	        }

	        // Generate HTML content
	        String htmlContent = generateHtmlContent(criteriaPayload, auditReportCriteriaEntities, sanitizedUserName);
	        if (htmlContent == null || htmlContent.trim().isEmpty()) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Generated HTML content is empty");
	        }

	        // Optional: Save HTML to disk for debugging
	        Path debugHtmlPath = Files.createTempFile("debug-html-", ".html");
	        Files.write(debugHtmlPath, htmlContent.getBytes(StandardCharsets.UTF_8));

	        // Convert HTML to PDF and save to disk
	        Path htmlPdfPath = Files.createTempFile("html-generated-", ".pdf");
	        try (OutputStream os = Files.newOutputStream(htmlPdfPath)) {
	            HtmlConverter.convertToPdf(htmlContent, os, new ConverterProperties());
	        }

	        // Begin PDF merging
	        ByteArrayOutputStream mergedOutput = new ByteArrayOutputStream();
	        try (PdfDocument finalPdfDoc = new PdfDocument(new PdfWriter(mergedOutput))) {
	            PdfMerger merger = new PdfMerger(finalPdfDoc);

	            // Merge HTML PDF
	            try (PdfDocument htmlPdfDoc = new PdfDocument(new PdfReader(htmlPdfPath.toFile()))) {
	                merger.merge(htmlPdfDoc, 1, htmlPdfDoc.getNumberOfPages());
	            }

	            // Merge uploaded files
	            List<MultipartFile> files = Arrays.asList(
	                    auditCoverReport, mainAuditReport, annexureA1, annexureA2, annexureA3,
	                    annexureA4, annexureA5, annexureA6, annexureVI,
	                    annexureVIII, annexureVIIIRepeat, annexureVIIIUndertaking
	            );

	            for (MultipartFile file : files) {
	                if (file != null && !file.isEmpty()) {
	                    try (PdfDocument pdfDoc = new PdfDocument(new PdfReader(file.getInputStream()))) {
	                        merger.merge(pdfDoc, 1, pdfDoc.getNumberOfPages());
	                    }
	                }
	            }

	            for (AuditReportCriteriaEntity entity : auditReportCriteriaEntities) {
	                if (entity != null && entity.getDocument() != null && !entity.getDocument().isEmpty()) {
	                    String documentData = entity.getDocument().trim();

	                    // Optional: skip if it's not likely base64 (doesn't contain only base64-safe chars)
	                    if (!documentData.matches("^[A-Za-z0-9+/=\\r\\n]+$")) {
	                        System.out.println("Skipping invalid base64 document for entity ID: " + entity.getAuditControlId());
	                        continue;
	                    }

	                    try {
	                        byte[] dbPdf = Base64.getDecoder().decode(documentData);
	                        try (PdfDocument pdfDoc = new PdfDocument(new PdfReader(new ByteArrayInputStream(dbPdf)))) {
	                            merger.merge(pdfDoc, 1, pdfDoc.getNumberOfPages());
	                        }
	                    } catch (IllegalArgumentException e) {
	                        System.out.println("Invalid base64 for entity ID: " + entity.getAuditControlId() + ", skipping: " + e.getMessage());
	                    }
	                }
	            }
	        }


	        // Add page numbers
	        ByteArrayOutputStream numberedPdf = addPageNumber(mergedOutput.toByteArray());

	        // Applicant status advances only after successful eSign (see download-final-esigned-report)

	        // Return final response
	        return ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + optional.get().geteSignedId() + " ")
	                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
	                .contentType(MediaType.APPLICATION_PDF)
	                .body(numberedPdf.toByteArray());

	    } catch (Exception e) {
	        e.printStackTrace(); // for dev logs
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error generating auditor report: " + e.getMessage());
	    }
	}

	private String generateHtmlContent(List<AuditCriteriaPayload> criteriaPayload,
			List<AuditReportCriteriaEntity> auditReportCriteriaEntities, String userName) {

		StringBuilder contentBuilder = new StringBuilder();

		contentBuilder.append("<html><head><style>").append("@page { margin: 50pt 20pt 50pt 20pt; }")
				.append("body { font-size: 12px; font-family: 'Times New Roman', serif; }")
				.append("table { border-collapse: collapse; width: 100%; }")
				.append("th, td { border: 1px solid black; padding: 5px; }")
				.append(".section-title { font-size: 22px; font-style: italic; font-weight: bold; color: #2f4f6f; }")
				.append(".summary-title { font-size: 15px; font-style: italic; font-weight: bold; color: #2f4f6f; }")
		        .append(".subsection-title { font-size: 20px; font-style: italic; font-weight: bold; color: #6f2020; margin-top: 10px; }")
		        .append(".minor-title { font-size: 18px; font-style: italic; font-weight: bold; color: #6f2020; margin-top: 10px; }")
				.append("th { background-color: #d9534f; color: white; }").append("</style></head><body>");
		
		
		contentBuilder.append("<p class='summary-title'>c. <i>Summary Of Observation</i></p>");

		List<AuditCriteriaPayload> criteriaPayloads = applicationAuditorsService.getAllData(userName);
		ApplicationAuditEntity applicationAuditEntity = applicationAuditServ.getByApplicantUserName(userName);

		List<AuditReportCriteriaEntity> auditReportCriteriaEntity = auditReportCriteriaService
		        .findByAuditId(applicationAuditEntity.getAppAuditId());

		int subCriteriaIndexes = 1;
		for (AuditCriteriaPayload criterias : criteriaPayloads) {

		    for (AuditSubCriteriaPayload subCriterias : criterias.getAuditSubCriteria()) {
		        contentBuilder.append("<table border='1'><tr>")
		            .append("<th>S.No.</th>")
		            .append("<th>Control No.</th>")
		            .append("<th>Reference(s)</th>")
		            .append("<th>Non-compliance observed</th>")
		            .append("<th>Action taken by CA, if any</th>")
		            .append("<th>Compliance</th>")
		            .append("</tr>");

		        int controlIndexes = 1;
		        for (AuditParameterPayload parameters : subCriterias.getAuditParameterPayload()) {
		            int auditControlIndexes = 1;

		            for (AuditControlPayload control : parameters.getAuditControlPayload()) {
		                String controlKey = "3." + subCriteriaIndexes + "." + controlIndexes + "." + auditControlIndexes;

		                // Match compliance from auditReportCriteriaEntity
		                String complianceValue = "";
		                for (AuditReportCriteriaEntity reportCriteria : auditReportCriteriaEntity) {
		                    if (reportCriteria.getAuditControlId() != null) {
		                        String[] parts = reportCriteria.getAuditControlId().split("-");
		                        if (parts.length > 0 && parts[0].equals(controlKey)) {
		                            complianceValue = reportCriteria.getCompliance() != null ? reportCriteria.getCompliance() : "";
		                            break;
		                        }
		                    }
		                }

		                contentBuilder.append("<tr>")
		                    .append("<td>").append(controlIndexes).append("</td>")
		                    .append("<td>").append(controlKey).append("</td>")
		                    .append("<td>").append(control.getReferences()).append("</td>")
		                    .append("<td style='max-width: 200px;'>").append(control.getAuditControl()).append("</td>")
		                    .append("<td>").append(control.getAuditCheck()).append("</td>")
		                    .append("<td>").append(complianceValue).append("</td>")
		                    .append("</tr>");

		                auditControlIndexes++;
		                controlIndexes++;
		            }
		        }

		        contentBuilder.append("</table><br/>");
		        subCriteriaIndexes++;
		    }
		}
		
		
		

		contentBuilder.append("<p class='section-title'>3. <i>Detailed Audit Controls</i></p>");

		int subCriteriaIndex = 1;
		for (AuditCriteriaPayload criteria : criteriaPayload) {
			contentBuilder.append("<p class='subsection-title'><i>3.").append(subCriteriaIndex).append(". ").append(criteria.getAuditCriteria())
					.append("</i></p>");

			int controlIndex = 1;
			for (AuditSubCriteriaPayload subCriteria : criteria.getAuditSubCriteria()) {
				contentBuilder.append("<p class='minor-title'><span style='color:#1a4fb2;'>3.").append(subCriteriaIndex).append(".").append(controlIndex).append(". ")
						.append("</span>").append("<i>").append(subCriteria.getAuditSubCriteria()).append("</i></p>");

				contentBuilder.append("<table><tr>").append("<th>Control No.</th><th>Control</th><th>Audit Checks</th>")
						.append("<th>Control Type</th><th>References</th>")
						.append("<th>Compliance</th><th>Document</th><th>Remarks</th></tr>");

				int auditControlIndex = 1;
				for (AuditParameterPayload parameter : subCriteria.getAuditParameterPayload()) {
					contentBuilder.append("<tr><td colspan='8' style='font-weight: bold;text-align: left;background-color:#d9d9d9;'>")
							.append(parameter.getAuditParameter()).append("</td></tr>");

					for (AuditControlPayload control : parameter.getAuditControlPayload()) {
						contentBuilder.append("<tr><td>").append("3.").append(subCriteriaIndex).append(".")
								.append(controlIndex).append(".").append(auditControlIndex).append("</td>")
								.append("<td style='max-width: 200px;'>").append(control.getAuditControl())
								.append("</td>").append("<td>").append(control.getAuditCheck()).append("</td>")
								.append("<td>").append(control.getControlType()).append("</td>").append("<td>")
								.append(control.getReferences()).append("</td>").append("<td>");

						String controlId = "3." + subCriteriaIndex + "." + controlIndex + "." + auditControlIndex;

						if (auditReportCriteriaEntities != null && !auditReportCriteriaEntities.isEmpty()) {
							AuditReportCriteriaEntity matchingEntity = auditReportCriteriaEntities.stream()
									.filter(entity -> {
										String[] parts = entity.getAuditControlId().split("-");
										return parts[0].equals(controlId);
									}).findFirst().orElse(null);

							if (matchingEntity != null) {
								contentBuilder.append(matchingEntity.getCompliance());
							} else {
								// contentBuilder.append("<td></td>");
								contentBuilder.append("");
							}
						}

						contentBuilder.append("</td>");
						contentBuilder.append("<td>");
						if (auditReportCriteriaEntities != null && !auditReportCriteriaEntities.isEmpty()) {
							AuditReportCriteriaEntity matchingEntity = auditReportCriteriaEntities.stream()
									.filter(entity -> {
										String[] parts = entity.getAuditControlId().split("-");
										return parts[0].equals(controlId);
									}).findFirst().orElse(null);

							if (matchingEntity != null) {
								contentBuilder.append(matchingEntity.getAuditControlId());
							} else {
								// contentBuilder.append("<td></td>");
								contentBuilder.append("");
							}
						}
						contentBuilder.append("</td>");
						contentBuilder.append("<td>");
						contentBuilder.append("</td>");
						contentBuilder.append("</tr>");
						auditControlIndex++;
					}
				}

				contentBuilder.append("</table><p></p>");
				controlIndex++;
			}
			subCriteriaIndex++;
		}

		// Append additional audit sections
		contentBuilder.append("<div>").append((AnnualAuditPeriod(userName))).append("</div>")
				.append("<div>").append((InternalAuditDetailes(userName))).append("</div>")
				.append("<div>").append((ekycAccountAuditDetailes(userName)))
				.append("</div>").append("<div>")
				.append((ekycAccountBasedVerification(userName))).append("</div>").append("<div>")
				.append((raAuditDetails(userName))).append("</div>").append("<div>")
				.append((courtCases(userName))).append("</div>").append("<div>")
				.append((revocationMain(userName))).append("</div>").append("<div>")
				.append((cryptoToken(userName))).append("</div>").append("<div>")
				.append((caSwWebMain(userName))).append("</div>").append("<div>")
				.append((locationMain(userName))).append("</div>").append("<div>")
				.append((cAServiceMain(userName))).append("</div>").append("<div>")
				.append((aSPDetails(userName))).append("</div>").append("<div>")
				.append((publicInfoMain(userName))).append("</div>").append("<div>")
				.append((certificateCost(userName))).append("</div>").append("<div>")
				.append((selfAssessmentMain(userName))).append("</div>").append("<div>")
				.append((caSoftwareAndExternal(userName))).append("</div>").append("<div>")
				.append((downTimeDuringAudit(userName))).append("</div>").append("<div>")
				.append((caTrustedPersons(userName))).append("</div>");

		contentBuilder.append("</body></html>");
		
		System.out.println(contentBuilder.toString());
		
		return contentBuilder.toString();
	}

	private Optional<AuditReportCriteriaEntity> findMatchingEntity(List<AuditReportCriteriaEntity> entities,
			String controlId) {
		if (entities == null || entities.isEmpty()) {
			return Optional.empty();
		}

		return entities.stream().filter(entity -> {
			if (entity == null || entity.getAuditControlId() == null) {
				return false;
			}
			String[] parts = entity.getAuditControlId().split("-");
			return parts.length > 0 && parts[0].equals(controlId);
		}).findFirst();
	}

	private String escapeHtml(String input) {
		if (input == null) {
			return "";
		}
		StringBuilder escaped = new StringBuilder();
		for (char c : input.toCharArray()) {
			switch (c) {
			case '&':
				escaped.append("&amp;");
				break;
			case '<':
				escaped.append("&lt;");
				break;
			case '>':
				escaped.append("&gt;");
				break;
			case '"':
				escaped.append("&quot;");
				break;
			case '\'':
				escaped.append("&#39;");
				break;
			default:
				escaped.append(c);
				break;
			}
		}
		return escaped.toString();
	}

	private ByteArrayOutputStream mergeSinglePdf(byte[] generatedPdfBytes, String title, String fileName) {
		try (ByteArrayOutputStream finalOutputStream = new ByteArrayOutputStream()) {

			PdfDocument finalPdfDocument = new PdfDocument(new PdfWriter(finalOutputStream));
			PdfDocument generatedPdf = new PdfDocument(new PdfReader(new ByteArrayInputStream(generatedPdfBytes)));
			PdfMerger pdfMerger = new PdfMerger(finalPdfDocument);
			pdfMerger.merge(generatedPdf, 1, generatedPdf.getNumberOfPages());
			Path filePath = Paths.get(Constant.REAL_PATH, "//AuditorNCFile").resolve(fileName).normalize();
			System.out.println("--------->" + filePath);
			if (Files.exists(filePath) && Files.isReadable(filePath)) {
				try (PdfReader existingPdfReader = new PdfReader(filePath.toString());
						ByteArrayOutputStream modifiedOutputStream = new ByteArrayOutputStream()) {
					PdfWriter modifiedWriter = new PdfWriter(modifiedOutputStream);
					PdfDocument existingPdf = new PdfDocument(existingPdfReader, modifiedWriter);
					PdfPage firstPage = existingPdf.getPage(1);
					PdfCanvas canvas = new PdfCanvas(firstPage);
					PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
					canvas.beginText().setFontAndSize(font, 12).moveText(20, firstPage.getPageSize().getTop() - 15) // Position
																													// the
																													// title
							.showText(title).endText();
					existingPdf.close();

					PdfDocument tempDoc = new PdfDocument(
							new PdfReader(new ByteArrayInputStream(modifiedOutputStream.toByteArray())));
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

	private ByteArrayOutputStream addPageNumber(byte[] generatedPdfBytes) throws IOException {
		try (ByteArrayOutputStream finalOutputStream = new ByteArrayOutputStream()) {
			if (generatedPdfBytes == null || generatedPdfBytes.length == 0) {
				throw new IOException("Generated PDF is empty or corrupted");
			}

			try (PdfReader reader = new PdfReader(new ByteArrayInputStream(generatedPdfBytes));
					PdfWriter writer = new PdfWriter(finalOutputStream);
					PdfDocument pdfDoc = new PdfDocument(reader, writer)) {

				int totalPages = pdfDoc.getNumberOfPages();
				PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
				float fontSize = 8f;

				for (int i = 1; i <= totalPages; i++) {
					PdfPage page = pdfDoc.getPage(i);
					PdfCanvas canvas = new PdfCanvas(page);

					// Add page number
					canvas.beginText().setFontAndSize(font, fontSize).moveText(page.getPageSize().getRight() - 100, 20)
							.showText("Page " + i + " of " + totalPages).endText();

					// Add watermark
					PdfCanvas watermarkCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(),
							pdfDoc);

					AffineTransform transform = AffineTransform.getRotateInstance(
					    Math.toRadians(45),
					    26,
					    150
					);
					watermarkCanvas.saveState().setFillColorGray(0.75f) // light gray color
							.setExtGState(new PdfExtGState().setFillOpacity(0.3f)) // opacity
							.beginText().setFontAndSize(font, 50).setTextMatrix(transform).moveText(0, 0)
							.showText("CCA - AUDIT REPORT").endText().restoreState();
				}

				return finalOutputStream;
			}
		}
	}

	@GetMapping("/download-final-esigned-report")
	public ResponseEntity<?> eSignCoverLetter(@RequestParam("id") String id, @RequestParam("did") String did) {

		try {

			String eSignDocId = id;
			String signedDocument = did;

			if (EncryptionUtil.decrypt(eSignDocId) != null)
				eSignDocId = EncryptionUtil.decrypt(eSignDocId);

			if (EncryptionUtil.decrypt(signedDocument) != null)
				signedDocument = EncryptionUtil.decrypt(signedDocument);

			ESignedDocumentsEntity edoc = documentsService.getEsignDocumentById(Long.parseLong(eSignDocId));

			if (edoc != null) {

				edoc.setSignedDocId(EncryptionUtil.encrypt(signedDocument));
				edoc.setStatus("Active");
				documentsService.addEsignDocument(edoc);

				if (edoc.getUserName() != null && !edoc.getUserName().isBlank()) {
					auditReportCriteriaService.changedTheStatus(edoc.getUserName());
				}

			}

			return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/get-esign-document-id")
	public ResponseEntity<?> eSignDocumentId(@RequestParam("userName") String userName) {

		try {

			String eSignDocId = userName;
			List<ESignedDocumentsEntity> edoc = documentsService.getEsignDocumentByUserName(eSignDocId);

			if (edoc == null || edoc.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("No eSign document found for username: " + userName);
			}

			ESignedDocumentsEntity latest = edoc.stream()
					.max(java.util.Comparator.comparing(ESignedDocumentsEntity::geteSignedId))
					.orElse(edoc.get(0));

			return new ResponseEntity<>(latest, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/repair-audit-report-document-links")
	public ResponseEntity<?> repairAuditReportDocumentLinksGet(
			@RequestParam("applicantUserName") String applicantUserName,
			@RequestParam(value = "criteriaDocIds", required = false) List<Long> criteriaDocIds) {
		return repairAuditReportDocumentLinks(applicantUserName, criteriaDocIds);
	}

	@PostMapping("/repair-audit-report-document-links")
	public ResponseEntity<?> repairAuditReportDocumentLinks(@RequestParam("applicantUserName") String applicantUserName,
			@RequestParam(value = "criteriaDocIds", required = false) List<Long> criteriaDocIds) {
		try {
			ApplicationAuditEntity applicationAuditEntity = applicationAuditServ
					.getByApplicantUserName(applicantUserName);
			if (applicationAuditEntity == null || applicationAuditEntity.getAppAuditId() == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Application audit entity not found for username: " + applicantUserName);
			}

			List<AuditReportDocumentEntity> orphans = auditReportDocumentService.findAllWithNullAppAuditId();
			if (criteriaDocIds != null && !criteriaDocIds.isEmpty()) {
				orphans = orphans.stream()
						.filter(doc -> criteriaDocIds.contains(doc.getCriteriaDocId()))
						.collect(Collectors.toList());
			}
			List<Long> idsToLink = orphans.stream().map(AuditReportDocumentEntity::getCriteriaDocId)
					.collect(Collectors.toList());

			int linked = auditReportDocumentService.linkOrphanDocumentsToApplicant(
					applicationAuditEntity.getAppAuditId(), idsToLink);

			Map<String, Object> response = new HashMap<>();
			response.put("applicantUserName", applicantUserName);
			response.put("appAuditId", applicationAuditEntity.getAppAuditId());
			response.put("linkedCount", linked);
			response.put("criteriaDocIds", idsToLink);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to repair audit report document links: " + e.getMessage());
		}
	}

	@GetMapping("/get-audit-esign-status")
	public ResponseEntity<?> getAuditEsignStatus(@RequestParam("userName") String userName) {
		try {
			if (userName == null || userName.trim().isEmpty()) {
				return ResponseEntity.badRequest().body("userName is required");
			}
			return ResponseEntity.ok(buildAuditEsignStatus(userName.trim()));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to fetch eSign status: " + e.getMessage());
		}
	}

	@PostMapping("/mark-audit-esign-failed")
	public ResponseEntity<?> markAuditEsignFailed(@RequestParam("userName") String userName) {
		try {
			if (userName == null || userName.trim().isEmpty()) {
				return ResponseEntity.badRequest().body("userName is required");
			}
			ESignedDocumentsEntity pending = findLatestPendingEsignRecord(userName.trim());
			if (pending == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("No pending eSign record found for this applicant.");
			}
			pending.setStatus("Failed");
			pending.setUpdated(new Date());
			documentsService.addEsignDocument(pending);
			return ResponseEntity.ok(buildAuditEsignStatus(userName.trim()));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to update eSign status: " + e.getMessage());
		}
	}

	@GetMapping("/short-comming-report")
	public ResponseEntity<?> getByApplicantShortCommingDocumentName(@RequestParam("documentName") String documentName) {
		try {
			Optional<ApplicantShortcomingsReportEntity> cpsDocumentOpt = applicantShortcomingsReportService
					.downloadfileBydDocumentName(EncryptionUtil.decrypt(documentName));

			if (cpsDocumentOpt.isPresent()) {
				ApplicantShortcomingsReportEntity c = cpsDocumentOpt.get();
				System.out.println(c.getShortcomingReport());
				Path filePath = Paths.get(Constant.REAL_PATH + "//ShortCommingReport").resolve(c.getShortcomingReport())
						.normalize();
				System.out.println(filePath);
				Resource resource = new UrlResource(filePath.toUri());
				System.out.println(resource);
				if (resource.exists()) {
					String contentType = Files.probeContentType(filePath);
					System.out.println(contentType);
					return ResponseEntity.ok()
							.contentType(MediaType
									.parseMediaType(contentType != null ? contentType : "application/octet-stream"))
							.header("Content-Disposition", "attachment; filename=ShortCommingReport.pdf")
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

	
	
	@GetMapping("/auditor-review-report")
	public ResponseEntity<?> getByAuditorReviewDocumentName(@RequestParam("documentName") String documentName) {
		try {
			Optional<AuditReviewReportEntity> cpsDocumentOpt = auditReviewReportService
					.downloadfileBydDocumentName(EncryptionUtil.decrypt(documentName));

			if (cpsDocumentOpt.isPresent()) {
				AuditReviewReportEntity c = cpsDocumentOpt.get();
				System.out.println(c.getAuditorReviewReport());
				Path filePath = Paths.get(Constant.REAL_PATH + "//AuditReviewReport").resolve(c.getAuditorReviewReport())
						.normalize();
				System.out.println(filePath);
				Resource resource = new UrlResource(filePath.toUri());
				System.out.println(resource);
				if (resource.exists()) {
					String contentType = Files.probeContentType(filePath);
					System.out.println(contentType);
					return ResponseEntity.ok()
							.contentType(MediaType
									.parseMediaType(contentType != null ? contentType : "application/octet-stream"))
							.header("Content-Disposition", "attachment; filename=AuditorReviewReport.pdf")
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

	private Optional<ESignedDocumentsEntity> resolveOrCreatePendingEsignRecord(String userName) {
		ESignedDocumentsEntity pending = findLatestPendingEsignRecord(userName);
		if (pending != null) {
			pending.setStatus("Active");
			pending.setUpdated(new Date());
			return documentsService.addEsignDocument(pending);
		}
		ESignedDocumentsEntity documentsEntity = new ESignedDocumentsEntity();
		documentsEntity.setUserName(userName);
		documentsEntity.setStatus("Active");
		return documentsService.addEsignDocument(documentsEntity);
	}

	private ESignedDocumentsEntity findLatestPendingEsignRecord(String userName) {
		List<ESignedDocumentsEntity> docs = documentsService.getEsignDocumentByUserName(userName);
		if (docs == null || docs.isEmpty()) {
			return null;
		}
		return docs.stream()
				.filter(doc -> doc.getSignedDocId() == null || doc.getSignedDocId().isBlank())
				.filter(doc -> !"Failed".equalsIgnoreCase(doc.getStatus()))
				.max(java.util.Comparator.comparing(ESignedDocumentsEntity::geteSignedId))
				.orElse(null);
	}

	private ESignedDocumentsEntity findLatestEsignRecord(String userName) {
		List<ESignedDocumentsEntity> docs = documentsService.getEsignDocumentByUserName(userName);
		if (docs == null || docs.isEmpty()) {
			return null;
		}
		return docs.stream()
				.max(java.util.Comparator.comparing(ESignedDocumentsEntity::geteSignedId))
				.orElse(docs.get(0));
	}

	private Map<String, Object> buildAuditEsignStatus(String userName) {
		Map<String, Object> status = new HashMap<>();
		ESignedDocumentsEntity latest = findLatestEsignRecord(userName);
		if (latest == null) {
			status.put("esignStatus", "NONE");
			status.put("canRetryEsign", false);
			status.put("workflowAdvanced", false);
			return status;
		}
		boolean hasSignedDoc = latest.getSignedDocId() != null && !latest.getSignedDocId().isBlank();
		String esignStatus;
		if (hasSignedDoc) {
			esignStatus = "COMPLETED";
		} else if ("Failed".equalsIgnoreCase(latest.getStatus())) {
			esignStatus = "FAILED";
		} else {
			esignStatus = "PENDING";
		}
		status.put("esignStatus", esignStatus);
		status.put("canRetryEsign", !hasSignedDoc);
		status.put("workflowAdvanced", hasSignedDoc);
		status.put("eSignedId", latest.geteSignedId());
		status.put("signedDocId", hasSignedDoc ? latest.getSignedDocId() : null);
		return status;
	}

	/**
	 * Applicant NC response requires an active audit_short_coming row. That row is normally
	 * created when the Review Committee reviews the NC ({@code review-by-committee*}).
	 * If the application is already at "Review NC Report" from the audit agency NC upload only,
	 * provision a shortcoming record from the active auditor NC document.
	 */
	private void linkOrphanAuditReportDocuments(ApplicationAuditEntity applicationAuditEntity) {
		if (applicationAuditEntity == null || applicationAuditEntity.getAppAuditId() == null) {
			return;
		}
		List<AuditReportDocumentEntity> orphans = auditReportDocumentService.findAllWithNullAppAuditId();
		if (orphans.isEmpty()) {
			return;
		}
		List<Long> idsToLink = orphans.stream().map(AuditReportDocumentEntity::getCriteriaDocId)
				.collect(Collectors.toList());
		int linked = auditReportDocumentService.linkOrphanDocumentsToApplicant(
				applicationAuditEntity.getAppAuditId(), idsToLink);
		if (linked > 0) {
			System.out.println("Linked " + linked + " orphan audit_report_document row(s) to app_audit_id="
					+ applicationAuditEntity.getAppAuditId());
		}
	}

	private AuditShortComingsEntity resolveActiveShortcoming(ApplicationAuditEntity applicationAuditEntity) {
		if (applicationAuditEntity == null || applicationAuditEntity.getAppAuditId() == null) {
			return null;
		}
		Long appAuditId = applicationAuditEntity.getAppAuditId();

		AuditShortComingsEntity active = auditShortComingsService.getActiveByAppAuditId(appAuditId);
		if (active != null) {
			return active;
		}

		List<AuditShortComingsEntity> existing = auditShortComingsService.findByAuditsId(appAuditId);
		if (existing != null && !existing.isEmpty()) {
			AuditShortComingsEntity latest = existing.get(0);
			latest.setStatus("Active");
			latest.setUpdated(new Date());
			auditShortComingsService.updateData(latest);
			return latest;
		}

		String seedRemarks = null;
		AuditReportDocumentEntity auditorNcReport = auditReportDocumentService.getActiveByAppAuditId(appAuditId);
		if (auditorNcReport != null && auditorNcReport.getRemarks() != null && !auditorNcReport.getRemarks().isBlank()) {
			seedRemarks = auditorNcReport.getRemarks();
		}
		if (seedRemarks == null || seedRemarks.isBlank()) {
			if (applicationAuditEntity.getRemarks() != null && !applicationAuditEntity.getRemarks().isBlank()) {
				seedRemarks = applicationAuditEntity.getRemarks();
			} else if (applicationAuditEntity.getApplicantRemarks() != null
					&& !applicationAuditEntity.getApplicantRemarks().isBlank()) {
				seedRemarks = applicationAuditEntity.getApplicantRemarks();
			} else {
				seedRemarks = "NC review in progress — applicant response to Review NC Report.";
			}
		}

		ApplicationAuditEntity auditReference = auditReportDocumentService
				.resolveAuditReference(applicationAuditEntity);
		if (auditReference == null) {
			return null;
		}

		AuditShortComingsEntity created = new AuditShortComingsEntity();
		created.setRemarks(seedRemarks);
		created.setStatus("Active");
		created.setAppAuditId(auditReference);
		return auditShortComingsService.saveData(created).orElse(null);
	}

	
}
