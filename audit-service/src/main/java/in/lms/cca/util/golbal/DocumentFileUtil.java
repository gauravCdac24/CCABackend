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

	public static String saveFile(MultipartFile file, String documentName, String uniqueCode, String folderPath) throws IOException {

	    String originalFileName = file.getOriginalFilename();
	    String fileExtension = originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf(".")) : "";

	    Date cdate = new Date();
	    SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
	    String currentDate = sdf.format(cdate);

	    String uniqueFileName;
	    Path filePath;
	    Path directoryPath = Paths.get(Constant.REAL_PATH, folderPath);

	    // Create the directory if it doesn't exist
	    Files.createDirectories(directoryPath);

	    // Include a UUID so multiple files saved in the same second (e.g. 2 auditor
	    // certificates on one submit) never collide and spin forever in exists().
	    do {
	        uniqueFileName = documentName + currentDate + uniqueCode
	                + UUID.randomUUID().toString().replace("-", "")
	                + fileExtension;
	        filePath = directoryPath.resolve(uniqueFileName);
	    } while (Files.exists(filePath));
	    
	    // Save the file 
	    try (OutputStream outputStream = Files.newOutputStream(filePath)) {
	        outputStream.write(file.getBytes());
	        return uniqueFileName;
	    }catch(Exception e) {
	    	return null;
	    }

	    
	}
	
	public static void deleteFile(String fileName, String filepath) {
		
		 Path filePath = Paths.get(Constant.REAL_PATH, filepath+"//"+fileName);
		
		try {
			Files.deleteIfExists(filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
		

	
}
