package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.LicenseCessationApplicationEntity;

public interface LicenseCessationApplicationService {

	List<LicenseCessationApplicationEntity> findByLicenseId(String licenseId);

	Optional<LicenseCessationApplicationEntity> saveAllData(
			LicenseCessationApplicationEntity licenseCessationApplicationEntity);

	Optional<LicenseCessationApplicationEntity> updateAllData(LicenseCessationApplicationEntity applicationEntity);

	LicenseCessationApplicationEntity getByLicenseById(String licenseId);

	Optional<LicenseCessationApplicationEntity> getByCessationById(String cessationAppId);

	List<LicenseCessationApplicationEntity> getAllData();

	LicenseCessationApplicationEntity getNoticeDocumentById(Long cessationAppId);

	List<LicenseCessationApplicationEntity> getAllDataForUndertaking();

	List<LicenseCessationApplicationEntity> getAllDataForUndertakings();

	String changeLicenseeRoleToExLicensee(String userName);

	List<LicenseCessationApplicationEntity> getAllDataForUndertakinges();

	LicenseCessationApplicationEntity getNoticeDocumentsById(Long cessationAppId);

	List<LicenseCessationApplicationEntity> getAllDataForUndertakingess();

}
