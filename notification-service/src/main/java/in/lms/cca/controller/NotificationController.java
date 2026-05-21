package in.lms.cca.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import in.lms.cca.config.EmailConfig;
import in.lms.cca.dto.NotificationTypeDTO;
import in.lms.cca.dto.NotificationsDTO;
import in.lms.cca.dto.NotificationsDetailsDTO;
import in.lms.cca.entity.NotificationType;
import in.lms.cca.entity.Notifications;
import in.lms.cca.payload.UserResponse;
import in.lms.cca.service.INotificationService;
import in.lms.cca.service.INotificationTypeService;
import in.lms.cca.service.IUserServiceClient;
import in.lms.cca.service.impl.SingleSignSMS;
import in.lms.cca.util.api.NotificationServiceAPIs;
import in.lms.cca.util.global.EncryptionUtil;
import in.lms.cca.util.global.SendMessage;

@RestController
@RequestMapping(NotificationServiceAPIs.NOTIFICATION_SERVICE_BASE_URL)
@CrossOrigin
public class NotificationController {

	
	@Autowired
	private INotificationService notificationServ;
	
	@Autowired
	private INotificationTypeService notificationTypeServ;
	
	@Autowired
	private IUserServiceClient userServClient;
	
	@Autowired
	private SendMessage sendMessage;
	
	@Autowired
	private MailSender mailSender;
	
	@Autowired
	private SingleSignSMS signSMS;
	
	private final RestTemplate restTemplate = new RestTemplate();
	
	@PostMapping(NotificationServiceAPIs.ADD_NOTIFICATION_TYPE)
	public ResponseEntity<?> addNotificationType(@RequestBody NotificationTypeDTO notificationTypeDTO){

		// Valid Notification Types
		
		List<String> validNotificationType = new ArrayList<>();
		validNotificationType.add("Email");
		validNotificationType.add("SMS");
		validNotificationType.add("Dashboard");
		validNotificationType.add("Email & SMS");
		validNotificationType.add("Email & Dashboard");
		validNotificationType.add("SMS & Dashboard");
		validNotificationType.add("All");
		
		//Server Side Validation

		//Blank
		if(notificationTypeDTO.getNotificationTypeName().equals("")) {
			return new ResponseEntity<>("The Notification Type cannot be blank.", HttpStatus.BAD_REQUEST);
		}
		
		//Invalid Characters
		if(!notificationTypeDTO.getNotificationTypeName().trim().matches("^[A-Za-z]{1}[A-Za-z& ]+")){
			return new ResponseEntity<>("The Notification Type should only include alphabets (A-Z or a-z), space, & and first character must be alphabet.", HttpStatus.BAD_REQUEST);
		}
		
		//Invalid Notification Type
		
		if(!validNotificationType.contains(notificationTypeDTO.getNotificationTypeName())) {
			return new ResponseEntity<>("The Notification Type can only be one of the following: Email, SMS, Dashboard, Email & SMS, Email & Dashboard, SMS & Dashboard, or All.", HttpStatus.BAD_REQUEST);			
		}
		
		//Duplicate Notification Type
		
		NotificationType availableNotificationType = notificationTypeServ.getNotificationTypeByName(notificationTypeDTO.getNotificationTypeName());
		
		if(availableNotificationType != null) {
			return new ResponseEntity<>("The Notification Type already exists.", HttpStatus.BAD_REQUEST);
		}
		
		//Save Notification Type
		
		
		NotificationType ntype = new NotificationType();
		ntype.setNotificationTypeName(notificationTypeDTO.getNotificationTypeName());
		ntype.setStatus("Active");
		notificationTypeServ.saveNotificationType(ntype);
		
		return new ResponseEntity<>("Notification Type saved successfully.", HttpStatus.OK);
	}
	
	@PostMapping(NotificationServiceAPIs.SEND_NOTIFICATION)
	public ResponseEntity<?> sendNotification(@RequestBody NotificationsDTO notification){
	

		//System generated Notification
		
		//validate
		NotificationType notificationType = notificationTypeServ.getNotificationTypeByName(notification.getNotificationType());
		
		
		
		if(notificationType == null) {
			return new ResponseEntity<>("Invalid Notification Type.", HttpStatus.BAD_REQUEST);
		}
		
		String username = notification.getUsername();
		
		
		if(username == null) {
			return new ResponseEntity<>("Invalid Username.", HttpStatus.BAD_REQUEST);
		}
		
		if(EncryptionUtil.decrypt(username)!=null)
			username = EncryptionUtil.decrypt(username);
		
		UserResponse userRes = userServClient.getUserDetailsByUsername(username);
		
		if(userRes == null) {
			return new ResponseEntity<>("No User Found", HttpStatus.BAD_REQUEST);
		}
		
		try {
			
			if(notificationType.getNotificationTypeName().equals("Email")) {
			
				//Email
				//sendMessage.sendEmailMessage(notification.getSubject(), notification.getMessage(), userRes.getEmailId());
				
				SimpleMailMessage messageS = new SimpleMailMessage();
				
				messageS.setFrom(EmailConfig.EMAIL_FROM);
				messageS.setTo(userRes.getEmailId());
				messageS.setSubject(notification.getSubject());
				messageS.setText(notification.getMessage());
				
				//mailSender.send(messageS);
				signSMS.sendSingleSMS("ccalmsdelhi", "cdac11@CCA", notification.getMessage(), "CCALMS", userRes.getMobile(), "f043c8ac-8646-442f-bc47-876263243a92");
				
			}else if(notificationType.getNotificationTypeName().equals("SMS")) {
				
			}else if(notificationType.getNotificationTypeName().equals("Dashboard")) {
				
			}else if(notificationType.getNotificationTypeName().equals("Email & SMS")) {
				
				//Email
				//sendMessage.sendEmailMessage(notification.getSubject(), notification.getMessage(), userRes.getEmailId());
				
				SimpleMailMessage messageS = new SimpleMailMessage();
				
				messageS.setFrom(EmailConfig.EMAIL_FROM);
				messageS.setTo(userRes.getEmailId());
				messageS.setSubject(notification.getSubject());
				messageS.setText(notification.getMessage());
				//signSMS.sendSingleSMS("ccalmsdelhi", "cdac11@CCA", "Dear {#var#}, your intent to become a Certifying Authority submitted successfully. You can login & submit the License Application using the login credentials sent to your registered email address. CCA-Meit", "CCALMS", userRes.getMobile(), "f043c8ac-8646-442f-bc47-876263243a92");
				//mailSender.send(messageS);
				
				
			}else if(notificationType.getNotificationTypeName().equals("Email & Dashboard")) {
				
				//Email
				//sendMessage.sendEmailMessage(notification.getSubject(), notification.getMessage(), userRes.getEmailId());
				
				SimpleMailMessage messageS = new SimpleMailMessage();
				
				messageS.setFrom(EmailConfig.EMAIL_FROM);
				messageS.setTo(userRes.getEmailId());
				messageS.setSubject(notification.getSubject());
				messageS.setText(notification.getMessage());
				
				//mailSender.send(messageS);
				signSMS.sendSingleSMS("ccalmsdelhi", "cdac11@CCA", notification.getMessage(), "CCALMS", userRes.getMobile(), "f043c8ac-8646-442f-bc47-876263243a92");
			}else if(notificationType.getNotificationTypeName().equals("SMS & Dashboard")) {
				
			}else if(notificationType.getNotificationTypeName().equals("All")) {
				
				//Email
				//sendMessage.sendEmailMessage(notification.getSubject(), notification.getMessage(), userRes.getEmailId());
				
				SimpleMailMessage messageS = new SimpleMailMessage();
				
				messageS.setFrom(EmailConfig.EMAIL_FROM);
				messageS.setTo(userRes.getEmailId());
				messageS.setSubject(notification.getSubject());
				messageS.setText(notification.getMessage());
				
				//mailSender.send(messageS);
				signSMS.sendSingleSMS("ccalmsdelhi", "cdac11@CCA", notification.getMessage(), "CCALMS", userRes.getMobile(), "f043c8ac-8646-442f-bc47-876263243a92");
			}
			
			
			
			Notifications newNotification = new Notifications();
			
			newNotification.setNotificationTypeId(notificationType);
			newNotification.setMessage(notification.getNotificationDescription());
			newNotification.setUsername(username);
			newNotification.setIsread(false);
			newNotification.setRole(notification.getRole());
			notificationServ.saveNotification(newNotification);
			
			return new ResponseEntity<>("Notification Sent.", HttpStatus.OK);
			
		}catch(Exception e) {
			return new ResponseEntity<>("Notification Failed."+e, HttpStatus.BAD_REQUEST);
		}
		
		
	}
	
	
	
	@PostMapping(NotificationServiceAPIs.SEND_NOTIFICATION_BY_DETAILS)
	public ResponseEntity<?> sendNotificationByDetails(@RequestBody NotificationsDetailsDTO notification){
	

		//System generated Notification
		
		//validate
		NotificationType notificationType = notificationTypeServ.getNotificationTypeByName(notification.getNotificationType());
		
		
		
		if(notificationType == null) {
			return new ResponseEntity<>("Invalid Notification Type.", HttpStatus.BAD_REQUEST);
		}
		
		
		try {
			
			if(notificationType.getNotificationTypeName().equals("Email")) {
			
				
				
			}else if(notificationType.getNotificationTypeName().equals("SMS")) {
				
			}else if(notificationType.getNotificationTypeName().equals("Dashboard")) {
				
			}else if(notificationType.getNotificationTypeName().equals("Email & SMS")) {
				
				
				//Mobile
				signSMS.sendSingleSMS("ccalmsdelhi", "cdac11@CCA", "Dear {#var#}, your intent to become a Certifying Authority submitted successfully. You can login & submit the License Application using the login credentials sent to your registered email address. CCA-Meit", "CCALMS", notification.getUserMobile(), "f043c8ac-8646-442f-bc47-876263243a92");
				//Email
				//sendMessage.sendMimeEmailMessage(notification.getSubject(), notification.getMessage(), notification.getUserEmail());
				
				
			}else if(notificationType.getNotificationTypeName().equals("Email & Dashboard")) {
				
				
				
			}else if(notificationType.getNotificationTypeName().equals("SMS & Dashboard")) {
				
			}else if(notificationType.getNotificationTypeName().equals("All")) {
				
			
			}
			
			String username = notification.getUsername();
			
			if(EncryptionUtil.decrypt(username)!=null)
				username = EncryptionUtil.decrypt(username);
			
			Notifications newNotification = new Notifications();
			
			newNotification.setNotificationTypeId(notificationType);
			newNotification.setMessage(notification.getNotificationDescription());
			newNotification.setUsername(username);
			newNotification.setIsread(false);
			newNotification.setRole(notification.getRole());
			notificationServ.saveNotification(newNotification);
			
			return new ResponseEntity<>("Notification Sent.", HttpStatus.OK);
			
		}catch(Exception e) {
			return new ResponseEntity<>("Notification Failed."+e, HttpStatus.BAD_REQUEST);
		}
		
		
	}
	
	
	@GetMapping(NotificationServiceAPIs.GET_NOTIFICATION_BY_USERNAME_AND_ROLE)
	public ResponseEntity<?> getNotificationByUsername(@RequestParam("id") String username, @RequestParam("pid") String role)
	{
		String id = EncryptionUtil.decrypt(username);
		String rolename = EncryptionUtil.decrypt(role);
		
		
		
		try {
			List<Notifications> notificationList = notificationServ.getNotificationByUsernameAndRole(id, rolename);
			return new ResponseEntity<>(notificationList, HttpStatus.OK); 
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Error in getting notification.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	@GetMapping(NotificationServiceAPIs.READ_NOTIFICATION_BY_USERNAME_AND_ROLE)
	public ResponseEntity<?> readNotificationByUsernameAndRole(@RequestParam("id") String username, @RequestParam("pid") String roleName)
	{
		String id = EncryptionUtil.decrypt(username);
		String rolename = EncryptionUtil.decrypt(roleName);
		
		try {
			notificationServ.readNotificationByUsernameAndRole(id, rolename);
			return new ResponseEntity<>(HttpStatus.OK); 
		}catch(Exception e) {
			return new ResponseEntity<>("Error in updating notification.", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	@GetMapping(NotificationServiceAPIs.READ_NOTIFICATION_BY_ID)
	public ResponseEntity<?> readNotificationById(@RequestParam("id") String id)
	{
		String ids = EncryptionUtil.decrypt(id);
		
		try {
			Notifications notification = notificationServ.getNotificationById(Long.parseLong(ids));
			
			notification.setIsread(true);
			notificationServ.saveNotification(notification);
			
			return new ResponseEntity<>(HttpStatus.OK);
			
		}catch(Exception e) {
			return new ResponseEntity<>("Error in updating the status of read.", HttpStatus.BAD_REQUEST);
		}
		
	}
	

	

}
