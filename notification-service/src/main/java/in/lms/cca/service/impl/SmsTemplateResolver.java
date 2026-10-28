package in.lms.cca.service.impl;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.lms.cca.config.SmsProperties;
import in.lms.cca.config.SmsProperties.Template;

@Service
public class SmsTemplateResolver {

	private static final Pattern PLACEHOLDER = Pattern.compile("\\{#(?:var|num)#\\}");

	@Autowired
	private SmsProperties smsProperties;

	public ResolvedSms resolve(String templateKey, List<String> vars) {
		if (templateKey == null || templateKey.isBlank()) {
			throw new IllegalArgumentException("SMS template key is required.");
		}

		Template template = smsProperties.getTemplates().get(templateKey);
		if (template == null || template.getId() == null || template.getId().isBlank()
				|| template.getBody() == null || template.getBody().isBlank()) {
			throw new IllegalArgumentException("Unknown or incomplete SMS template key: " + templateKey);
		}

		String body = fillPlaceholders(template.getBody(), vars);
		return new ResolvedSms(template.getId(), body);
	}

	/**
	 * Replaces {#var#} and {#num#} left-to-right with the provided values.
	 */
	public String fillPlaceholders(String body, List<String> vars) {
		if (body == null) {
			return "";
		}
		if (vars == null || vars.isEmpty()) {
			return body;
		}

		StringBuffer sb = new StringBuffer();
		Matcher matcher = PLACEHOLDER.matcher(body);
		int index = 0;
		while (matcher.find()) {
			String replacement = index < vars.size() && vars.get(index) != null ? vars.get(index) : "";
			matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
			index++;
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	public static class ResolvedSms {
		private final String templateId;
		private final String message;

		public ResolvedSms(String templateId, String message) {
			this.templateId = templateId;
			this.message = message;
		}

		public String getTemplateId() {
			return templateId;
		}

		public String getMessage() {
			return message;
		}
	}
}
