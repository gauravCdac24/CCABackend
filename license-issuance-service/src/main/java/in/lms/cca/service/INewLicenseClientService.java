package in.lms.cca.service;

public interface INewLicenseClientService {
	
	 String changeIntentApplicationStatus(String intentId, String status, String initiatedBy);

	String changeApplicantRoleToLicensee(String ausername);

	String changeAnnexureDetails(String ausername);
	
}
