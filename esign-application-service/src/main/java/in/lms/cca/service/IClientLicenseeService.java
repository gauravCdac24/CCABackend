package in.lms.cca.service;

import java.util.List;

import in.lms.cca.dto.ClientLicenseDetailsDTO;
import in.lms.cca.dto.ESignAPIVersionMasterDTO;



public interface IClientLicenseeService {

	List<ClientLicenseDetailsDTO> getAllLicenseDetailsByUsername(String username);
	List<ClientLicenseDetailsDTO> getAllActiveLicenseDetails();
	List<ESignAPIVersionMasterDTO> getAllAPIVersion();
	
}
