
package in.lms.cca.service.annexure;

import in.lms.cca.entity.annexure.DownTime;

public interface IDownTimeService {

	DownTime getByDownTimeId(Long id);
	DownTime addDownTime(DownTime obj);
	DownTime getByAnnexureId(Long id);
	
	
}