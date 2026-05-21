
package in.lms.cca.service.annexure;

import java.util.List;

import in.lms.cca.entity.annexure.InternalAuditDetails;

public interface IInternalAuditDetailsService {

	InternalAuditDetails getByInternalAuditDetailsId(Long id);

	InternalAuditDetails addInternalAuditDetails(InternalAuditDetails d);

	List<InternalAuditDetails> getByInternalAuditMainId(Long id);


	
}