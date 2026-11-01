package in.lms.cca.util.golbal;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import in.lms.cca.util.global.Constant;

public class DocumentFileUtil {

	public static String saveFile(MultipartFile file, String documentName, String uniqueCode, String folderPath)
			throws IOException {

		if (file == null || file.isEmpty()) {
			return null;
		}

		String originalFileName = file.getOriginalFilename();
		String fileExtension = "";
		if (originalFileName != null && originalFileName.lastIndexOf('.') >= 0) {
			fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));
		}

		String currentDate = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
		Path directoryPath = Paths.get(Constant.REAL_PATH, folderPath);
		Files.createDirectories(directoryPath);

		// UUID prevents same-second collisions (multiple auditor files on one submit)
		// from spinning forever in Files.exists().
		String uniqueFileName;
		Path filePath;
		int attempts = 0;
		do {
			uniqueFileName = documentName + currentDate + (uniqueCode == null ? "" : uniqueCode)
					+ UUID.randomUUID().toString().replace("-", "") + fileExtension;
			filePath = directoryPath.resolve(uniqueFileName);
			attempts++;
		} while (Files.exists(filePath) && attempts < 5);

		if (Files.exists(filePath)) {
			throw new IOException("Unable to allocate a unique file name for " + documentName);
		}

		try (OutputStream outputStream = Files.newOutputStream(filePath)) {
			outputStream.write(file.getBytes());
			return uniqueFileName;
		}
	}

	public static void deleteFile(String fileName, String filepath) {
		Path filePath = Paths.get(Constant.REAL_PATH, filepath, fileName);
		try {
			Files.deleteIfExists(filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
