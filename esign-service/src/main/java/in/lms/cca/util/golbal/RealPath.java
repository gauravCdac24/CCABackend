package in.lms.cca.util.golbal;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RealPath {

	// public static final String REAL_PATH = "/home/admin/microservices/esign-service";

	// public static final String REAL_PATH = "C:\\Users\\hp\\Documents\\CCABackend\\esign-service\\src\\main\\webapp";
	// public static final String REAL_PATH = "/home/dashboard/CCABackend/esign-service/src/main/webapp";
	// public static final String REAL_PATH = "C:\\Users\\hp\\Documents\\CCAB\\esign-service\\src\\main\\webapp";
	public static final String REAL_PATH = resolveDocumentRoot();

	private static String resolveDocumentRoot() {
		String configured = System.getProperty("esign.document.root");
		if (configured == null || configured.isBlank()) {
			configured = System.getenv("ESIGN_DOCUMENT_ROOT");
		}
		String[] candidates = {
				configured,
				"C:\\Users\\hp\\Documents\\CCABackend\\CCABackend\\esign-service\\src\\main\\webapp",
				"/home/gaurav/Documents/CCABackend/esign-service/src/main/webapp",
				"C:\\Users\\hp\\Documents\\CCABackend\\esign-service\\src\\main\\webapp"
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
		return "/home/gaurav/Documents/CCABackend/esign-service/src/main/webapp";
	}

	public static final String REQUEST_DOCUMENT_DIR = REAL_PATH + "/esignrequest/document";
	public static final String REQUEST_XML_DIR = REAL_PATH + "/esignrequest/xml";
	public static final String RESPONSE_DOCUMENT_DIR = REAL_PATH + "/esignresponse/document";
	public static final String RESPONSE_XML_DIR = REAL_PATH + "/esignresponse/xml";
	public static final String CERT_DIR = REAL_PATH + "/cert";

	/**
	 * Creates all required eSign storage folders if they do not exist.
	 */
	public static void ensureDirectories() throws IOException {
		Files.createDirectories(Paths.get(REQUEST_DOCUMENT_DIR));
		Files.createDirectories(Paths.get(REQUEST_XML_DIR));
		Files.createDirectories(Paths.get(RESPONSE_DOCUMENT_DIR));
		Files.createDirectories(Paths.get(RESPONSE_XML_DIR));
		Files.createDirectories(Paths.get(CERT_DIR));
	}

	/**
	 * Creates parent directories for a file path if missing.
	 */
	public static void ensureParentDirectory(String filePath) throws IOException {
		Path parent = Paths.get(filePath).getParent();
		if (parent != null) {
			Files.createDirectories(parent);
		}
	}
}
