package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import in.lms.cca.entity.ApplicationAuditorsEntity;
import in.lms.cca.payload.AuditCriteriaPayload;

public interface ApplicationAuditorsService {

	Optional<ApplicationAuditorsEntity> processAuditForm(ApplicationAuditorsEntity auditorDescriptiones);

	List<ApplicationAuditorsEntity> getAllDataByAuditId(Long appAuditId);

	Optional<ApplicationAuditorsEntity> downloadfile(Long id );

	Optional<ApplicationAuditorsEntity> aprovedAuditForm(ApplicationAuditorsEntity auditorDescriptiones);

	Optional<ApplicationAuditorsEntity> updateData(ApplicationAuditorsEntity auditorsEntity);

	List<ApplicationAuditorsEntity> getAllActiveActiveAuditor();

	//List<AuditCriteriaPayload> getAllData();

	List<AuditCriteriaPayload> getAllDatas(String userName);

	Optional<ApplicationAuditorsEntity> downloadfiles(String id);

	List<AuditCriteriaPayload> getAllData(String sanitizedUserName);


	

	

}
