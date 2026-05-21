package in.lms.cca.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class SingleSignSMS {

	@Value("${sms.api.url}")
	private String smsApiUrl;

	private final RestTemplate restTemplate = new RestTemplate();

	public String sendSingleSMS(String username, String password, String message, String senderId, String mobileNumber,
			String secureKey) {
		String responseString = "";

		try {
			System.out.println("Sending message: " + message);
			System.out.println("mobileNumber: " + mobileNumber);
			System.out.println("username: " + username);
			System.out.println("password (plain): " + password);
			System.out.println("secureKey: " + secureKey);

// Encrypt password with SHA-1 (still called MD5 method, but uses SHA-1)
			String encryptedPassword = MD5(password);
			System.out.println("Encrypted password: " + encryptedPassword);

// URL encode the message content
			String encodedMessage = URLEncoder.encode(message.trim(), StandardCharsets.UTF_8.toString());

// ✅ Use encodedMessage in the hashGenerator
			String hashKey = hashGenerator(username, senderId, encodedMessage, secureKey);
			System.out.println("Generated hash key: " + hashKey);

			MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
			requestBody.add("mobileno", mobileNumber);
			requestBody.add("senderid", senderId);
			requestBody.add("content", encodedMessage);
			requestBody.add("smsservicetype", "singlemsg");
			requestBody.add("username", username);
			requestBody.add("password", encryptedPassword);
			requestBody.add("templateid", "1407174244996769199");
			requestBody.add("key", hashKey);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);

			ResponseEntity<String> response = restTemplate.postForEntity(smsApiUrl, request, String.class);

			System.out.println("========> " + response);
			responseString = response.getBody();
			System.out.println("SMS API response: " + responseString);

		} catch (HttpClientErrorException e) {
			System.out.println("Client error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
			responseString = "Client error: " + e.getMessage();
		} catch (HttpServerErrorException e) {
			System.out.println("Server error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
			responseString = "Server error: " + e.getMessage();
		} catch (Exception e) {
			System.out.println("Unexpected error: " + e.getMessage());
			responseString = "Unexpected error: " + e.getMessage();
		}

		return responseString;
	}

	private static String MD5(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md;
		md = MessageDigest.getInstance("SHA-1");
		byte[] md5 = new byte[64];
		md.update(text.getBytes("iso-8859-1"), 0, text.length());
		md5 = md.digest();
		return convertedToHex(md5);
	}

	private static String convertedToHex(byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			int halfOfByte = (data[i] >>> 4) & 0x0F;
			int twoHalfBytes = 0;
			do {
				if ((0 <= halfOfByte) && (halfOfByte <= 9)) {
					buf.append((char) ('0' + halfOfByte));
				} else {
					buf.append((char) ('a' + (halfOfByte - 10)));
				}
				halfOfByte = data[i] & 0x0F;
			} while (twoHalfBytes++ < 1);
		}
		return buf.toString();
	}

	protected String hashGenerator(String userName, String senderId, String content, String secureKey) {
		// TODO Auto-generated method stub
		StringBuffer finalString = new StringBuffer();
		finalString.append(userName.trim()).append(senderId.trim()).append(content.trim()).append(secureKey.trim());
		// logger.info("Parameters for SHA-512 : "+finalString);
		String hashGen = finalString.toString();
		StringBuffer sb = new StringBuffer();
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-512");
			md.update(hashGen.getBytes());
			byte byteData[] = md.digest();
			// convert the byte to hex format method 1 sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block e.printStackTrace();
		}
		return sb.toString();
	}
}
