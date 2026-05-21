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

public class DocumentFileUtil {

	public static String saveFile(MultipartFile file, String documentName, String uniqueCode, String folderPath) throws IOException {

	    String originalFileName = file.getOriginalFilename();
	    String fileExtension = originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf(".")) : "";

	    Date cdate = new Date();
	    SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
	    String currentDate = sdf.format(cdate);

	    String uniqueFileName;
	    Path filePath;
	    Path directoryPath = Paths.get(RealPath.REAL_PATH, folderPath);

	    // Create the directory if it doesn't exist
	    Files.createDirectories(directoryPath);

	    do {
	        // Generate a unique file name
	        uniqueFileName = documentName + "_" + currentDate + "_" + UUID.randomUUID().toString() + uniqueCode + fileExtension;
	        filePath = directoryPath.resolve(uniqueFileName);
	    } while (Files.exists(filePath)); // Ensure the file name is unique
	    
	    try (OutputStream outputStream = Files.newOutputStream(filePath)) {
	        outputStream.write(file.getBytes());
	        return uniqueFileName;
	    }
	}

	public static String truncateFileName(String fileName, int maxLength) {
		if (fileName == null) {
			return null;
		}
		if (fileName.length() <= maxLength) {
			return fileName;
		}
		int dotIndex = fileName.lastIndexOf('.');
		if (dotIndex > 0) {
			String extension = fileName.substring(dotIndex);
			int baseMax = maxLength - extension.length();
			if (baseMax > 0) {
				return fileName.substring(0, baseMax) + extension;
			}
		}
		return fileName.substring(0, maxLength);
	}

	
	
}
