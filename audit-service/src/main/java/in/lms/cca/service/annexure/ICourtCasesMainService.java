
package in.lms.cca.service.annexure;

import in.lms.cca.entity.annexure.CourtCasesMain;

public interface ICourtCasesMainService {

	CourtCasesMain getByCourtCasesMainId(Long id);
	CourtCasesMain addCourtCasesMain(CourtCasesMain obj);
	CourtCasesMain getByAnnexureId(Long id);
	
}

