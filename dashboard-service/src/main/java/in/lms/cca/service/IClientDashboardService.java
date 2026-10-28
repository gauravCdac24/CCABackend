package in.lms.cca.service;

import java.util.List;

import in.lms.cca.dto.AddressDTO;
import in.lms.cca.dto.AnnualAuditScheduleDTO;
import in.lms.cca.dto.AppLocationDTO;
import in.lms.cca.dto.ApplicationAuditorsDTO;
import in.lms.cca.dto.ApplicationTimeLineDTO;
import in.lms.cca.dto.AuditAgencyDTO;
import in.lms.cca.dto.AuditAgencySelectionDTO;
import in.lms.cca.dto.ClientLicenseDetailsDTO;
import in.lms.cca.dto.ESPDTO;
import in.lms.cca.dto.ESPWithEkycModeDTO;
import in.lms.cca.dto.FirmApplicationMainDTO;
import in.lms.cca.dto.GovtOrganizationApplicationMainDTO;
import in.lms.cca.dto.IndivAddressMainDTO;
import in.lms.cca.dto.IndivApplicationMainDTO;
import in.lms.cca.dto.IntentUniqueCodeDTO;
import in.lms.cca.dto.LicenseeAuditorsDTO;
import in.lms.cca.dto.StateDTO;


public interface IClientDashboardService {

	List<ClientLicenseDetailsDTO> getAllLicenseDetailsByUsername(String username);
	List<ClientLicenseDetailsDTO> getAllActiveLicenseDetails();
	List<ESPDTO> getAllApprovedESP();
	String isESP(String username);
	List<StateDTO> getAllStates();
	List<ClientLicenseDetailsDTO> getAllLicenseDetails();
	List<ESPWithEkycModeDTO> getAllESPWithEkycModeApproved();
	List<StateDTO> getAllStateByCountryName(String countryName);
	List<ClientLicenseDetailsDTO> getAllInactiveLicenseDetails();
	List<AnnualAuditScheduleDTO> getAllAnnualAuditSchedule();
	List<ApplicationTimeLineDTO> getAllIntentApplicationTimeLine();
	List<AuditAgencySelectionDTO> getAllSelectedAuditAgency();
	List<ApplicationAuditorsDTO> getAllActiveApplicationAuditors();
	List<AuditAgencyDTO> getAllAuditAgencyList();
	FirmApplicationMainDTO getFirmApplicationByUsername(String username);
	GovtOrganizationApplicationMainDTO getGovtOrgAppByUsername(String username);
	IndivApplicationMainDTO getIndivAppByUsername(String username);
	List<LicenseeAuditorsDTO> getAllActiveAnnualApplicationAuditors();
	List<IntentUniqueCodeDTO> getAllIntentUniqueCode();
	List<AddressDTO> getAllAddress();
	IndivAddressMainDTO getIndivAddressByUsername(String userName);
	List<AppLocationDTO> getAllActiveAppLocations();
}
