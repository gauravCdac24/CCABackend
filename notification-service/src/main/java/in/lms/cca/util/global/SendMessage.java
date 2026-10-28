package in.lms.cca.util.global;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import in.lms.cca.config.EmailConfig;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Component
public class SendMessage {

	
	private JavaMailSender mailSender;
	
	public SendMessage(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	
	public void sendSimpleEmailMessage(String title, String msg, String emailTo) {
		
		
		SimpleMailMessage messageS = new SimpleMailMessage();
		
		messageS.setFrom(EmailConfig.EMAIL_FROM);
		messageS.setTo(emailTo);
		messageS.setSubject(title);
		messageS.setText(msg);
		mailSender.send(messageS);	
	}
	
	public void sendMimeEmailMessage(String title, String msg, String emailTo) {
	    try {
	        MimeMessage messageS = mailSender.createMimeMessage();
	        
	        messageS.setFrom(new InternetAddress(EmailConfig.EMAIL_FROM));
	        messageS.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(emailTo));
	        messageS.setSubject(title);
	        messageS.setContent(msg, "text/html; charset=utf-8");
	        
	        mailSender.send(messageS);
	    } catch (MessagingException e) {
	        e.printStackTrace();
	    }
	}

	
	
	public void sendMessageOnMobile(String text, String mobileNumber) {
		
		
		
	}
	
}
