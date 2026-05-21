package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.AuditControlType;

public interface IAuditControlTypeService {

	Optional<AuditControlType> addAuditControlType(AuditControlType auditControlType);
	Optional<AuditControlType> updateAuditControlType(AuditControlType auditControlType);
	boolean deleteByAuditControlTypeId(Integer auditControlTypeId);
	AuditControlType getByAuditControlTypeId(Integer auditControlTypeId);
	List<AuditControlType> getAllActiveAuditControlType();
	List<AuditControlType> getAllInactiveAuditControlType();
	List<AuditControlType> getAllAuditControlType();
	AuditControlType getAuditControlTypeByDesc(String desc);
	
}
