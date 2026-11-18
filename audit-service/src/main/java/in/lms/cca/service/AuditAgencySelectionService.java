package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.ApplicationAuditEntity;
import in.lms.cca.entity.AuditAgencySelectionEntity;
import in.lms.cca.payload.AuditAgencySelectionDTO;

public interface AuditAgencySelectionService {

	Optional<AuditAgencySelectionEntity> addAuditAgencySelection(AuditAgencySelectionEntity agencySelectionEntity);

	void changedTheApplicantStatus(String intentAppId);

	List<AuditAgencySelectionEntity> getAllServiceMaster();

	AuditAgencySelectionEntity findbyAuditId(ApplicationAuditEntity applicationAuditEntity);

	List<AuditAgencySelectionEntity> getAllAuditId(Long appAuditId);

	Optional<AuditAgencySelectionEntity> upDateData(AuditAgencySelectionEntity selectionEntity);

	List<AuditAgencySelectionEntity> getAllDetailsByAuditAgencyId(String userId);

}
