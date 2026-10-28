package in.lms.cca.service;

import java.util.List;

import in.lms.cca.dto.AnnualAuditScheduleListDTO;

public interface AnnualAuditScheduleListService {

	List<AnnualAuditScheduleListDTO> getScheduleList(String id, String role);
}
