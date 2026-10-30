package in.lms.cca.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import in.lms.cca.util.global.AnnualAuditContext;
import in.lms.cca.util.global.EncryptionUtil;
import in.lms.cca.util.global.RenewalAnnexureContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AnnualAuditScheduleFilter extends OncePerRequestFilter {

	public static final String ANNUAL_AUDIT_SCHEDULE_HEADER = "X-Annual-Audit-Schedule-Id";
	public static final String RENEWAL_INTENT_APP_HEADER = "X-Renewal-Intent-App-Id";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String scheduleHeader = request.getHeader(ANNUAL_AUDIT_SCHEDULE_HEADER);
			if (scheduleHeader != null && !scheduleHeader.isBlank()) {
				String decrypted = EncryptionUtil.decrypt(scheduleHeader);
				if (decrypted != null && !decrypted.isBlank()) {
					AnnualAuditContext.setAuditScheduleId(Long.parseLong(decrypted));
				}
			}
			String intentHeader = request.getHeader(RENEWAL_INTENT_APP_HEADER);
			if (intentHeader != null && !intentHeader.isBlank()) {
				String decryptedIntent = EncryptionUtil.decrypt(intentHeader);
				if (decryptedIntent != null) {
					decryptedIntent = decryptedIntent.replace("\"", "").trim();
					if (!decryptedIntent.isBlank()) {
						try {
							RenewalAnnexureContext.setIntentAppId(Long.parseLong(decryptedIntent));
						} catch (NumberFormatException ignored) {
							RenewalAnnexureContext.clear();
						}
					}
				}
			}
			filterChain.doFilter(request, response);
		} finally {
			AnnualAuditContext.clear();
			RenewalAnnexureContext.clear();
		}
	}
}
