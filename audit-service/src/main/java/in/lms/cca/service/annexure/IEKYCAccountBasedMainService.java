
package in.lms.cca.service.annexure;

import in.lms.cca.entity.annexure.EKYCAccountBasedMain;

public interface IEKYCAccountBasedMainService {

	EKYCAccountBasedMain getByeKYCAcMainId(Long id);
	EKYCAccountBasedMain addeKYCAcMain(EKYCAccountBasedMain obj);
	EKYCAccountBasedMain getByAnnexureId(Long id);
	
}





