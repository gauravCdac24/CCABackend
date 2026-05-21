
package in.lms.cca.service.annexure;

import java.util.List;


import in.lms.cca.entity.annexure.CaSwWebDetails;

public interface ICaSwWebDetailsService {

	CaSwWebDetails addCaSwWebDetails(CaSwWebDetails obj);

	CaSwWebDetails getByCaSwWebDetailsId(Long id);

	List<CaSwWebDetails> getByCaSwWebMainId(Long id);
	
}
