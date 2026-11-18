
package in.lms.cca.service.annexure;

import in.lms.cca.entity.annexure.CAServicesMain;

public interface ICAServicesMainService {

	CAServicesMain addCAServicesMain(CAServicesMain obj);

	CAServicesMain getByAnnexureId(Long annexureMainId);
	
}


