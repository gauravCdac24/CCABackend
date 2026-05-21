package in.lms.cca.util.global;

import java.security.SecureRandom;
import java.time.Year;
import java.util.Random;

public class Generator {
	
	 private static final String UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	 private static final String LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
	 private static final String DIGITS = "0123456789";
	 private static final String DIGITS_WITHOUT_ZERO = "123456789";
	 private static final String SPECIAL_CHARACTERS = "!@#$&%";
	 private static final int PASSWORD_LENGTH = 8;
	 private static final int OTP_LENGTH = 6;		
			
			
	 public static String generatePassword(String previousPassword) {
			
			
			/*
		 		Rules for password generation
		 	
				1. Password must be exactly 20 characters long.
				2. A combination of upper case letters(A-Z), lower case letters(a-z), digits(0-9), and symbols(!, @, #, $, &, and %).
				3. Different from previously generated password.
				4. Include at least one special character, digits, upper case and lower case.
		 */
			
			
				Random random = new Random();
			
			    String allCharacters = UPPER_CASE + LOWER_CASE + DIGITS + SPECIAL_CHARACTERS;
			

		        while (true) {
		            StringBuilder password = new StringBuilder();

		            password.append(UPPER_CASE.charAt(random.nextInt(UPPER_CASE.length())));
		            password.append(LOWER_CASE.charAt(random.nextInt(LOWER_CASE.length())));
		            password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
		            password.append(SPECIAL_CHARACTERS.charAt(random.nextInt(SPECIAL_CHARACTERS.length())));

	                
		            for (int i = 4; i < PASSWORD_LENGTH; i++) {
		                password.append(allCharacters.charAt(random.nextInt(allCharacters.length())));
		            }

		            // Shuffle the characters to ensure randomness
		            char[] passwordArray = password.toString().toCharArray();
		            for (int i = 0; i < passwordArray.length; i++) {
		                int randomIndex = random.nextInt(passwordArray.length);
		                char temp = passwordArray[i];
		                passwordArray[i] = passwordArray[randomIndex];
		                passwordArray[randomIndex] = temp;
		            }

		            String generatedPassword = new String(passwordArray);
		            
		            if (previousPassword == null || (!previousPassword.contains(generatedPassword))) {
		                return generatedPassword;
		            }
		        }
		    
			
			
			
			
		}
	 
	 
	 	public static String generateUsername(Integer count) {
	 		
	 		/*
	 		 * 
	         Rules for username generation:
	         1. Minimum Length 12 characters and maximum 20 characters.
	         2. Initially, a username will be generated with 12 characters. If the count exceeds this length, the username will be extended accordingly.
	         3. Format will be UCCA<Year><00 if required to make the length up to 12> <count>

			*/

	        String prefix = "UCCA";
	        String year = String.valueOf(Year.now().getValue());
	        String countStr = String.valueOf(count);

	        
	        int remainingLength = 12 - (prefix.length() + year.length() + countStr.length());
	        
	        
	        StringBuilder zeros = new StringBuilder();
	        for (int i = 0; i < remainingLength; i++) {
	            zeros.append("0");
	        }

	        
	        String newUsername = prefix + year + zeros + countStr;

	        
	        if (newUsername.length() > 20) {
	            newUsername = newUsername.substring(0, 20);
	        }

	        return newUsername;
	    }
	 	
	 	
	 public static String generateOTP() {
	         StringBuilder otps = new StringBuilder();
	         SecureRandom random = new SecureRandom();

	         for (int i = 0; i < OTP_LENGTH; i++) {
	             int index = random.nextInt(DIGITS_WITHOUT_ZERO.length());
	             char digit = DIGITS_WITHOUT_ZERO.charAt(index);
	             otps.append(digit);
	         }

	         return otps.toString();
	     }


}
