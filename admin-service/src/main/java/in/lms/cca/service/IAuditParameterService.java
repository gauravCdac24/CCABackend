package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.AuditParameter;

public interface IAuditParameterService {

		Optional<AuditParameter> addAuditParameter(AuditParameter auditParameter);
		Optional<AuditParameter> updateAuditParameter(AuditParameter auditParameter);
		boolean deleteByAuditParameterId(Long auditParameterId);
		boolean deleteByAuditSubCriteriaId(Long auditSubCriteriaId);
		AuditParameter getByAuditParameterId(Long auditParameterId);
		List<AuditParameter> getAllByAuditSubCriteriaId(Long auditSubCriteriaId);
		List<AuditParameter> getAllActiveByAuditSubCriteriaId(Long auditSubCriteriaId);
		List<AuditParameter> getAllInactiveByAuditSubCriteriaId(Long auditSubCriteriaId);
		List<AuditParameter> getAllActiveAuditParameter();
		List<AuditParameter> getAllInActiveAuditParameter();
		List<AuditParameter> getAllAuditParameter();
		AuditParameter getAuditParameterByTitle(String title);

	
}
