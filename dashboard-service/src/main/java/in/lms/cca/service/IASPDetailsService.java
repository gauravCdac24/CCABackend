package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.ASPDetails;
import in.lms.cca.entity.AspAuditReport;

public interface IASPDetailsService {

	Optional<ASPDetails> addASPDetails(ASPDetails obj);
	Optional<ASPDetails> addASPDetails(ASPDetails obj, List<AspAuditReport> auditReports);
	List<ASPDetails> getAllASPDetails();
	List<ASPDetails> getASPDetailsByUsername(String username);
	ASPDetails getASPDetailsById(Long id);
	ASPDetails getASPDetailsByUsernameAndName(String username, String aspName);
	ASPDetails getASPDetailsByUsernameAndEmailId(String username, String emailId);
}
