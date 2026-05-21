
package in.lms.cca.service.annexure;

import in.lms.cca.entity.annexure.PublicInfoMain;

public interface IPublicInfoMainService {

	PublicInfoMain addPublicInfoMain(PublicInfoMain obj);

	PublicInfoMain getByAnnexureId(Long annexureMainId);
	
}







