package in.lms.cca.service.annexure;


import in.lms.cca.entity.annexure.AnnualAuditPeriodMain;

public interface IAnnualAuditPeriodMainService {

	AnnualAuditPeriodMain addAnnualAuditPeriodMain(AnnualAuditPeriodMain obj);

	AnnualAuditPeriodMain getByAnnexureId(Long annexureMainId);


}







