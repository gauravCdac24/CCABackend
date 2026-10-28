package in.lms.cca.service.annexure;

import java.util.List;

import in.lms.cca.entity.annexure.AnnexureMain;

public interface IAnnexureMainService {

	AnnexureMain getAnnexureByStatusAndUsername(String status, String username);

	AnnexureMain getAnnexureForAuditorView(String username);

	AnnexureMain addAnnexureMain(AnnexureMain sobj);

	List<AnnexureMain> getAllAnnexureMain();

}
