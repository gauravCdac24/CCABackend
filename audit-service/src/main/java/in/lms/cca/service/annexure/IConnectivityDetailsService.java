
package in.lms.cca.service.annexure;

import java.util.List;

import in.lms.cca.entity.annexure.ConnectivityDetails;

public interface IConnectivityDetailsService {

	ConnectivityDetails addConnectivityDetails(ConnectivityDetails obj);

	ConnectivityDetails getByConnectivityDetailsId(Long id);

	List<ConnectivityDetails> getByConnectivityMainId(Long id);
	
}