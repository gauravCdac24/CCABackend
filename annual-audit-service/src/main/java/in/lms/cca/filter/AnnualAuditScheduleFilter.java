package in.lms.cca.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import in.lms.cca.util.global.AnnualAuditContext;
import in.lms.cca.util.global.EncryptionUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AnnualAuditScheduleFilter extends OncePerRequestFilter {

	public static final String ANNUAL_AUDIT_SCHEDULE_HEADER = "X-Annual-Audit-Schedule-Id";

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
			filterChain.doFilter(request, response);
		} finally {
			AnnualAuditContext.clear();
		}
	}
}
