package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.dto.AuditAgencySelectionDTO;
import in.lms.cca.dto.UserLoginDTO;
import in.lms.cca.entity.ApplicationTimeLine;

public interface IApplicationTimeLineService {

	List<ApplicationTimeLine> getAllApplicationTimeLine();
	List<ApplicationTimeLine> getByIntentApplicationId(Long intentAppId);
	Optional<ApplicationTimeLine> addApplicationTimeLine(ApplicationTimeLine obj);
	Optional<ApplicationTimeLine> updateApplicationTimeLine(ApplicationTimeLine obj);
	List<AuditAgencySelectionDTO> getAllData(String userId);
	UserLoginDTO getAllDetailsByUserName(String userName);
	
}
