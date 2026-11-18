
package in.lms.cca.service.annexure;

import in.lms.cca.entity.annexure.RaAuditMain;

public interface IRaAuditMainService {

	RaAuditMain getByRaAuditMainId(Long id);
	RaAuditMain addRaAuditMain(RaAuditMain obj);
	RaAuditMain getByAnnexureId(Long id);
	
}








