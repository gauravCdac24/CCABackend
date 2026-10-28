package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.FirmApplication2;

public interface FirmApplication2service {

	Optional<FirmApplication2> addData(FirmApplication2 firmApplication);

	FirmApplication2 findIntentAppById(Long intentAppId);

	Optional<FirmApplication2> updateData(FirmApplication2 firmApplication);

	List<FirmApplication2> findIntentWithoutStatusAppById(Long intentAppId);

}
