
package in.lms.cca.service.annexure;

import java.util.List;

import in.lms.cca.entity.annexure.CAServicesDetails;

public interface ICAServicesDetailsService {

	CAServicesDetails addCAServicesDetails(CAServicesDetails obj);

	CAServicesDetails getByCAServicesDetailsId(Long id);

	List<CAServicesDetails> getByCAServicesMainId(Long id);
	
}



