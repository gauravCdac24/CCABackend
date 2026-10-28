package in.lms.cca.esign;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PdfChecksum {

	private String sha256Hash(File file) throws NoSuchAlgorithmException, IOException{
		
		MessageDigest digest = MessageDigest.getInstance("SHA-256");

        // Using DigestInputStream to hash the file
        try (DigestInputStream dis = new DigestInputStream(new FileInputStream(file), digest)) {
            byte[] buffer = new byte[8192];
            while (dis.read(buffer) != -1) {
                // Reading file in chunks and updating digest
            }
        }

        // Getting the hash bytes
        byte[] hash = digest.digest();

        // Converting hash bytes to hexadecimal format
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
	}
	
	public String getHashOfFile(File file) {
	      try {
	            String hash = sha256Hash(file);
	            return hash;
	        } catch (NoSuchAlgorithmException | IOException e) {
	            e.printStackTrace();
	            return null;
	        }
	}
	
	
}
