package in.lms.cca.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * An empty file input is posted as a text field. Spring cannot bind that
 * string onto a MultipartFile, which produced a userDetailsDTO validation error.
 * Treat any plain-text value as "no file" so the controller can reject it.
 */
@Component
public class StringToMultipartFileConverter implements Converter<String, MultipartFile> {

	@Override
	public MultipartFile convert(String source) {
		return null;
	}
}
