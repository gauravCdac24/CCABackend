
package in.lms.cca.service.annexure;

import in.lms.cca.entity.annexure.InternalAuditMain;

public interface IInternalAuditMainService {

	InternalAuditMain getByAnnexureId(Long annexureMainId);

	InternalAuditMain addInternalAuditMain(InternalAuditMain obj);


	
}








