package in.lms.cca.service;

import java.util.List;

import in.lms.cca.dto.EKYCModeClientDTO;

public interface IeKYCModeClientService {

	List<EKYCModeClientDTO> getAlleKYCModes();
	EKYCModeClientDTO getEKYCModeById(Long id);
	
}
