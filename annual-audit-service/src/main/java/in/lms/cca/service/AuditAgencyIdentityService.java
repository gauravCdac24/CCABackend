package in.lms.cca.service;

public interface AuditAgencyIdentityService {

	/**
	 * Resolves an audit-agency login username (or already-numeric id) to the
	 * admin_user_management.audit_agency.audit_agency_id used in selections.
	 */
	String resolveAuditAgencyId(String agencyLoginOrId);
}
