package in.lms.cca.util.golbal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RealPath {

	/**
	 * Root folder for eSign PDF/XML storage and signing certs.
	 * Override with env ESIGN_REAL_PATH when needed.
	 * Windows local: esign-service working directory (absolute).
	 * Linux/server: /home/dashboard/CCABackend/esign-service
	 */
	public static final String REAL_PATH = resolveRealPath();

	public static final String REQUEST_DOCUMENT_DIR = join(REAL_PATH, "esignrequest", "document");
	public static final String REQUEST_XML_DIR = join(REAL_PATH, "esignrequest", "xml");
	public static final String RESPONSE_DOCUMENT_DIR = join(REAL_PATH, "esignresponse", "document");
	public static final String RESPONSE_XML_DIR = join(REAL_PATH, "esignresponse", "xml");
	public static final String CERT_DIR = join(REAL_PATH, "src", "main", "webapp", "cert");

	private static String resolveRealPath() {
		String override = System.getenv("ESIGN_REAL_PATH");
		if (override != null && !override.isBlank()) {
			return Paths.get(override.trim()).toAbsolutePath().normalize().toString();
		}

		String os = System.getProperty("os.name", "").toLowerCase();
		if (os.contains("win")) {
			Path cwd = Paths.get(System.getProperty("user.dir", ".")).toAbsolutePath().normalize();
			if (Files.isRegularFile(cwd.resolve("src/main/webapp/cert/ssdp.key"))
					|| "esign-service".equalsIgnoreCase(String.valueOf(cwd.getFileName()))) {
				return cwd.toString();
			}
			Path nested = cwd.resolve("esign-service");
			if (Files.isDirectory(nested)) {
				return nested.toAbsolutePath().normalize().toString();
			}
			return cwd.toString();
		}

		return "/home/dashboard/CCABackend/esign-service";
	}

	private static String join(String root, String... parts) {
		Path path = Paths.get(root);
		for (String part : parts) {
			path = path.resolve(part);
		}
		return path.toAbsolutePath().normalize().toString();
	}

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
