package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.ApplicationAuditorsEntity;
import in.lms.cca.payload.AuditCriteriaPayload;

public interface ApplicationAuditorsService {

	Optional<ApplicationAuditorsEntity> processAuditForm(ApplicationAuditorsEntity auditorDescriptiones);

	//List<ApplicationAuditorsEntity> getAllDataByAuditId(Long appAuditId);

	Optional<ApplicationAuditorsEntity> downloadfile(Long id );

	Optional<ApplicationAuditorsEntity> aprovedAuditForm(ApplicationAuditorsEntity auditorDescriptiones);

	Optional<ApplicationAuditorsEntity> updateData(ApplicationAuditorsEntity auditorsEntity);

	List<ApplicationAuditorsEntity> getAllDataByAuditId(Long licenseeAuditId);

	List<ApplicationAuditorsEntity> getAllActiveActiveAuditor();

	List<AuditCriteriaPayload> getAllData();

	

}
