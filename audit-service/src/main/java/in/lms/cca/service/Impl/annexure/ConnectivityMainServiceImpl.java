
package in.lms.cca.service.Impl.annexure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.lms.cca.entity.annexure.ConnectivityMain;
import in.lms.cca.repository.annexure.ConnectivityMainRepository;
import in.lms.cca.service.annexure.IConnectivityMainService;

@Service
@Transactional
public class ConnectivityMainServiceImpl implements IConnectivityMainService {

	@Autowired
	private ConnectivityMainRepository repo;
	
	@Override
	public ConnectivityMain addConnectivityMain(ConnectivityMain obj) {
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
	public ConnectivityMain getByAnnexureId(Long annexureMainId) {
		
		return repo.findByAnnexureId(annexureMainId);
	}


	
}