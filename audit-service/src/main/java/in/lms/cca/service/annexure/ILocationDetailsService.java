
package in.lms.cca.service.annexure;

import java.util.List;

import in.lms.cca.entity.annexure.LocationDetails;

public interface ILocationDetailsService{

	LocationDetails getByLocationDetailsId(Long id);

	LocationDetails addLocationDetails(LocationDetails d);

	List<LocationDetails> getByLocationMainId(Long id);
	
}






