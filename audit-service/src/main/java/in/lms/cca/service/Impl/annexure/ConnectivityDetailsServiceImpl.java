
package in.lms.cca.service.Impl.annexure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.annexure.ConnectivityDetails;
import in.lms.cca.repository.annexure.ConnectivityDetailsRepository;
import in.lms.cca.service.annexure.IConnectivityDetailsService;

@Service
@Transactional
public class ConnectivityDetailsServiceImpl implements IConnectivityDetailsService {

	@Autowired
	private ConnectivityDetailsRepository repo;
	
	@Override
	public ConnectivityDetails addConnectivityDetails(ConnectivityDetails obj) {
		
		if (obj == null) {
	        throw new IllegalArgumentException("Input object cannot be null");
	    }

	    try {
	        return repo.save(obj);
	    } catch (Exception e) {
	        throw new RuntimeException("Failed to save object", e);
	    }
	}

	@Override
	public ConnectivityDetails getByConnectivityDetailsId(Long id) {
		return repo.getByConnectivityDetailsId(id);
	}

	@Override
	public List<ConnectivityDetails> getByConnectivityMainId(Long id) {
		
		return repo.getByConnectivityMainId(id);
	}


	
}