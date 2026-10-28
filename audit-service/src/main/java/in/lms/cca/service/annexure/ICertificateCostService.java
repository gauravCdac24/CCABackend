
package in.lms.cca.service.annexure;

import in.lms.cca.entity.annexure.CertificateCost;

public interface ICertificateCostService {

	CertificateCost getByCertificateCostId(Long id);
	CertificateCost addCertificateCost(CertificateCost obj);
	CertificateCost getByAnnexureId(Long id);
	
	
}















