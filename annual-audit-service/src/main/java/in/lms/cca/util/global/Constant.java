package in.lms.cca.util.global;

import java.io.File;

public class Constant {
	
	//public static String REAL_PATH="/home/admin/microservices/annual-audit-service";
	
	//public static final String REAL_PATH = "C:\\Users\\hp\\Documents\\CCABackend\\annual-audit-service\\src\\main\\webapp";
	//public static final String REAL_PATH = "/home/dashboard/CCABackend/annual-audit-service/src/main/webapp";
	//public static final String REAL_PATH = "C:\\Users\\hp\\Documents\\CCAB\\annual-audit-service\\src\\main\\webapp";
	public static final String REAL_PATH = resolveDocumentRoot();

	private static String resolveDocumentRoot() {
		String configured = System.getProperty("annual.audit.document.root");
		if (configured == null || configured.isBlank()) {
			configured = System.getenv("ANNUAL_AUDIT_DOCUMENT_ROOT");
		}
		String[] candidates = {
				configured,
				"C:\\Users\\hp\\Documents\\CCABackend\\CCABackend\\annual-audit-service\\src\\main\\webapp",
				"/home/gaurav/Documents/CCABackend/annual-audit-service/src/main/webapp",
				"C:\\Users\\hp\\Documents\\CCABackend\\annual-audit-service\\src\\main\\webapp"
		};
		for (String candidate : candidates) {
			if (candidate == null || candidate.isBlank()) {
				continue;
			}
			File directory = new File(candidate);
			if (directory.isDirectory()) {
				return directory.getAbsolutePath();
			}
		}
		return "/home/gaurav/Documents/CCABackend/annual-audit-service/src/main/webapp";
	}

	

		public static final String CREATED="Created";
		public static final String UPDATED="Updated";
		public static final String DELETED="Deleted";
		public static final String OK="OK";

		// Annual audit schedule statuses (aligned with fresh application audit)
		public static final String STATUS_INITIATED = "Initiated";
		public static final String STATUS_AGENCY_SELECTION = "Agency Selection";
		public static final String STATUS_RECOMMENDED_FOR_AUDIT_AGENCY = "Recomanded For Audit Agency";
		public static final String STATUS_APPROVE_FROM_CCA = "Approve From CCA";
		public static final String STATUS_NC_ISSUED = "NC Issued By Auditor";
		public static final String STATUS_NC_ACTION_TAKEN = "NC Action taken By Applicant";
		public static final String STATUS_PENDING_LICENSEE_ESIGN = "Pending Licensee eSign";
		public static final String STATUS_REVIEW_NC_REPORT = "Review NC Report";
		public static final String STATUS_COMPLETED = "Completed";
		
	

}
