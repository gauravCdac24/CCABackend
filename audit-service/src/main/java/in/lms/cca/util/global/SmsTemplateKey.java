package in.lms.cca.util.global;

/**
 * Keys must match sms.templates.&lt;key&gt; entries in application*.properties.
 */
public final class SmsTemplateKey {

	public static final String OTP = "otp";
	public static final String INTENT_REGISTRATION = "intent-registration";
	public static final String LICENSE_APPLICATION = "license-application";
	public static final String APPLICATION_REVIEW = "application-review";
	public static final String APPLICATION_ACCEPTANCE = "application-acceptance";
	public static final String RECOMMENDATION_FOR_AUDIT = "recommendation-for-audit";
	public static final String AUDIT_AGENCY_SELECTION = "audit-agency-selection";
	public static final String AUDIT_REPORT_REVIEW = "audit-report-review";
	public static final String APPLICATION_APPROVAL = "application-approval";
	public static final String LICENSE_ISSUANCE = "license-issuance";
	public static final String ESP_APPLICATION_REVIEW = "esp-application-review";
	public static final String ESP_GO_LIVE = "esp-go-live";
	public static final String APPLICATION_REJECTION = "application-rejection";
	public static final String SHOW_CAUSE = "show-cause";
	public static final String LICENSE_SUSPENSION = "license-suspension";
	public static final String CESSATION_APPLICATION = "cessation-application";
	public static final String CESSATION_APPROVAL = "cessation-approval";
	public static final String RENEWAL_APPLICATION = "renewal-application";
	public static final String RENEWAL_APPROVAL = "renewal-approval";
	public static final String LICENSE_RENEWAL = "license-renewal";

	private SmsTemplateKey() {
	}
}
