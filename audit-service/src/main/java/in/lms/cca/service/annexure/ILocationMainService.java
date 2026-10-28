
package in.lms.cca.service.annexure;

import in.lms.cca.entity.annexure.LocationMain;

public interface ILocationMainService {

	LocationMain getByAnnexureId(Long annexureMainId);

	LocationMain addLocationMain(LocationMain obj);

	
}








