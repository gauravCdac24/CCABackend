
package in.lms.cca.service.annexure;

import in.lms.cca.entity.annexure.CaSwWebMain;

public interface ICaSwWebMainService {

	CaSwWebMain addCaSwWebMain(CaSwWebMain obj);

	CaSwWebMain getByAnnexureId(Long annexureMainId);
	
}



