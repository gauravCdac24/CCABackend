package in.lms.cca.service.annexure;

import in.lms.cca.entity.annexure.ASPDetails;

public interface IASPDetailsService{

	ASPDetails getByASPDetailsId(Long id);
	ASPDetails addASPDetails(ASPDetails obj);
	ASPDetails getByAnnexureId(Long id);
	
}












