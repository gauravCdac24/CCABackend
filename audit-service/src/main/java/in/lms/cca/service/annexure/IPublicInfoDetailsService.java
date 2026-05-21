
package in.lms.cca.service.annexure;

import java.util.List;

import in.lms.cca.entity.annexure.PublicInfoDetails;

public interface IPublicInfoDetailsService {

	PublicInfoDetails addPublicInfoDetails(PublicInfoDetails obj);

	PublicInfoDetails getByPublicInfoDetailsId(Long id);

	List<PublicInfoDetails> getByPublicInfoMainId(Long id);

	
}







