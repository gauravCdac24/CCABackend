package in.lms.cca.service.annexure;

import java.util.List;

import in.lms.cca.entity.annexure.AnnualAuditPeriodDetails;

public interface IAnnualAuditPeriodDetailsService {

	AnnualAuditPeriodDetails addAnnualAuditPeriodDetails(AnnualAuditPeriodDetails obj);

	AnnualAuditPeriodDetails getByAnnualAuditPeriodId(Long id);

	List<AnnualAuditPeriodDetails> getByAnnualAuditPeriodMainId(Long periodMainId);


	
}
