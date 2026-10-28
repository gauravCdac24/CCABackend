package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.GovtOrganizationApplication;

public interface GovernmentAgencyService {

	Optional<GovtOrganizationApplication> addData(GovtOrganizationApplication govtOrganizationApplication);

	GovtOrganizationApplication findIntentAppById(Long intentAppId, String status);

	Optional<GovtOrganizationApplication> UpdateData(GovtOrganizationApplication govtOrganizationApplication);

	List<String> getOrganizationNameByIntentAppId(Long intentAppId);

	GovtOrganizationApplication findWithoutIntentAppById(Long intentAppId);

}
