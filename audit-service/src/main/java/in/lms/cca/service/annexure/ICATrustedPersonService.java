
package in.lms.cca.service.annexure;


import java.util.List;

import in.lms.cca.entity.annexure.CATrustedPerson;

public interface ICATrustedPersonService {

	CATrustedPerson getByCATrustedPersonId(Long id);
	CATrustedPerson addCATrustedPerson(CATrustedPerson obj);
	List<CATrustedPerson> getByCATrustedPersonMainId(Long id);
	
}












