
package in.lms.cca.service.annexure;

import in.lms.cca.entity.annexure.ConnectivityMain;

public interface IConnectivityMainService {

	ConnectivityMain addConnectivityMain(ConnectivityMain obj);

	ConnectivityMain getByAnnexureId(Long annexureMainId);
	
}