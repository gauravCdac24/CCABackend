
package in.lms.cca.service.annexure;

import java.util.List;

import in.lms.cca.entity.annexure.EKYCAcMonthDetails;


public interface IEKYCAcMonthDetailsService {

	EKYCAcMonthDetails getByeKYCAcMonthId(Long id);
	EKYCAcMonthDetails addEKYCAcMonthDetails(EKYCAcMonthDetails d);
	List<EKYCAcMonthDetails> getByEKYCAcMonthMainId(Long id);

	
}