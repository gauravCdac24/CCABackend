package in.lms.cca.esign;

import java.util.Random;

public class TransactionId {

	public  String transactionId() {
		Random rand = new Random();
		int n = 0;
		String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String randStringemail = "";
		for (int i = 0; i < 40; i++) {
			n = rand.nextInt(chars.length());
			randStringemail += chars.substring(n, n + 1);
		}
		return randStringemail;
		
	}

	
}
