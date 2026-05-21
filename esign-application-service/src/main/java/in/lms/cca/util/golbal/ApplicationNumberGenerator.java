package in.lms.cca.util.golbal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ApplicationNumberGenerator {

	public static String generateUniqueApplicationId(Long id) {
        
        String datePart = new SimpleDateFormat("yyyyMMdd").format(new Date());
        Random random = new Random();
        int randomDigits = 100 + random.nextInt(900); 
        return "A" + datePart + randomDigits + id;
    }
	
	
}
