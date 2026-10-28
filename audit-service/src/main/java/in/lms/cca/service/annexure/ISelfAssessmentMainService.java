
package in.lms.cca.service.annexure;

import in.lms.cca.entity.annexure.SelfAssessmentMain;

public interface ISelfAssessmentMainService{

	SelfAssessmentMain getBySelfAssessmentMainId(Long id);
	SelfAssessmentMain addSelfAssessmentMain(SelfAssessmentMain obj);
	SelfAssessmentMain getByAnnexureId(Long id);
	
}







