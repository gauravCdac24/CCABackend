package in.lms.cca.util.golbal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RealPath {

	// public static final String REAL_PATH = "/home/admin/microservices/esign-service";

	// public static final String REAL_PATH = "C:\\Users\\hp\\Documents\\CCABackend\\esign-service\\src\\main\\webapp";
	// public static final String REAL_PATH = "/home/dashboard/CCABackend/esign-service/src/main/webapp";
	// public static final String REAL_PATH = "C:\\Users\\hp\\Documents\\CCAB\\esign-service\\src\\main\\webapp";
	public static final String REAL_PATH = "/home/gaurav/Documents/CCABackend/esign-service/src/main/webapp";

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
