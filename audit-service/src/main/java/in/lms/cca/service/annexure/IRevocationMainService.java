
package in.lms.cca.service.annexure;

import in.lms.cca.entity.annexure.RevocationMain;

public interface IRevocationMainService {


	RevocationMain getByRevocationMainId(Long id);
	RevocationMain addRevocationMain(RevocationMain obj);
	RevocationMain getByAnnexureId(Long id);
}






