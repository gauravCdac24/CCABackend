package in.lms.cca.service.Impl;


import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import in.lms.cca.entity.AnnualAuditScheduleEntity;
import in.lms.cca.payload.NotificationsRequest;
import in.lms.cca.repository.LicenseeAuditRepository;
import in.lms.cca.service.INotificationClientService;


@Service
public class LicenseSchedulerService {
	
	@Autowired
    private LicenseeAuditRepository licenseeAuditRepository;
	
	@Autowired
	private INotificationClientService clientService;

	   
	    @Scheduled(fixedRate = 86400000) 
	    public void notifyLicenseRenewal() {
	       Date currentDate = new Date(); 
	       Date currentDateNormalized = resetTime(currentDate);
	     
	        List<AnnualAuditScheduleEntity> licenses = licenseeAuditRepository.findAll();
	        
	        for (AnnualAuditScheduleEntity license : licenses) {
	        	
	        	 Date scheduleStartDateNormalized = resetTime(license.getScheduleStartDate());
	        	

	             if (scheduleStartDateNormalized.compareTo(currentDateNormalized) >= 0 ||scheduleStartDateNormalized.compareTo(currentDateNormalized) <= 0  && license.getActualStartDate() == null) {
	                 
	        		
	        		
	        		String title = "Annual Audit Reminder";
					String message = "Dear Licensee, \n\n" +
									 "This is a reminder that your annual audit is due. Please make sure to complete the necessary audit procedures as per the schedule. If you haven't started the audit yet, we recommend initiating it as soon as possible.\n\n" +
									 "For more details and the audit schedule, please check your email or log into your dashboard.\n\n" +
									 "If you have any questions or need help, feel free to reach out to our support team.\n\n" +
									 "Best regards,\n" +
									 "The Audit Team";

					NotificationsRequest notification = new NotificationsRequest();
					notification.setUsername(license.getUserName());  
					notification.setMessage(message);
					notification.setSubject(title);
					notification.setNotificationType("Email & Dashboard");
					notification.setNotificationDescription("License Renewal Reminder");
					notification.setRole("ROLE_LICENSEE");  
					clientService.sendNotification(notification);

	            }
	        }
	    }

	    private Date resetTime(Date date) {
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(date);
	        calendar.set(Calendar.HOUR_OF_DAY, 0);
	        calendar.set(Calendar.MINUTE, 0);
	        calendar.set(Calendar.SECOND, 0);
	        calendar.set(Calendar.MILLISECOND, 0);
	        return calendar.getTime();
	    }

}
