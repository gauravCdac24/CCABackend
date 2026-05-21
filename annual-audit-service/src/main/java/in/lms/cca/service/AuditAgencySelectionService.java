package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.dto.StatusChangedOfLicense;
import in.lms.cca.entity.AuditAgencySelectionEntity;
import in.lms.cca.entity.LicenseeAuditEntity;
import in.lms.cca.payload.AuditAgencySelectionDTO;

public interface AuditAgencySelectionService {

	Optional<AuditAgencySelectionEntity> addAuditAgencySelection(AuditAgencySelectionEntity agencySelectionEntity);

	void changedTheApplicantStatus(StatusChangedOfLicense changedOfLicense);

	List<AuditAgencySelectionEntity> getAllServiceMaster();

	AuditAgencySelectionEntity findbyAuditId(LicenseeAuditEntity applicationAuditEntity);

	List<AuditAgencySelectionEntity> getAllAuditId(Long appAuditId);

	Optional<AuditAgencySelectionEntity> upDateData(AuditAgencySelectionEntity selectionEntity);

	List<AuditAgencySelectionEntity> getAllDetailsByAuditAgencyId(String userId);

}
