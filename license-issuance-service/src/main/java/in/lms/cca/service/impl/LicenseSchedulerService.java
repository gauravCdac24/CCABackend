package in.lms.cca.service.impl;


import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import in.lms.cca.entity.LicenseDetails;
import in.lms.cca.payload.NotificationsRequest;
import in.lms.cca.repository.LicenseDetailsRepository;
import in.lms.cca.service.INotificationClientService;


@Service
public class LicenseSchedulerService {
	
	@Autowired
    private LicenseDetailsRepository licenseRepository;
	
	@Autowired
	private INotificationClientService clientService;

	@Value("${apigateway.service.url}")
	private String apigatewayServiceUrl;
	
	@Autowired
	private RestTemplate restTemplate;
	   
	
	    @Scheduled(fixedRate = 86400000) 
	    public void deactivateExpiredLicensesAndApplications() {
	        Date currentDate = new Date();

	        
	        List<LicenseDetails> allLicenses = licenseRepository.findAll();
	        

	        for (LicenseDetails license : allLicenses) {
	           
	            if (license.getExpiryDate().compareTo(currentDate) <= 0) {
	             
	                if ("Active".equals(license.getStatus())) {
	                    license.setStatus("Inactive");
	                    licenseRepository.save(license); 
	                }

	                String uriTemplate = apigatewayServiceUrl + "/esign-application-service/change-review-status/{applicantUsername}";
	        	    
	        	    restTemplate.getForObject(uriTemplate, String.class, license.getUserName());
	            }
	        }
	    }

	   
	    @Scheduled(fixedRate = 86400000) 
	    public void notifyLicenseRenewal() {
	       Date currentDate = new Date(); 
	        
	     
	        List<LicenseDetails> licenses = licenseRepository.findAll();
	        
	        for (LicenseDetails license : licenses) {
	           
	            long diffInMillies = license.getExpiryDate().getTime() - currentDate.getTime();
	            long diffDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

	          //  System.out.println("------>"+diffDays);
	            
	            
	         
	            if (diffDays <= 45 && diffDays >= 0) {
	            	
	            	 System.out.println("------>"+diffDays);
	            	 
	                String title = "License Renewal Reminder";
					String message = "Dear Licensee, \n\n" +
									 "This is a reminder that your license is due for renewal in the next 45 days. Please take the necessary steps to renew your license before it expires to avoid any service disruption.\n\n" +
									 "For renewal instructions and more details, please check your email or visit your account dashboard.\n\n" +
									 "If you have any questions or need assistance, feel free to contact our support team.\n\n" +
									 "Best regards,\n" +
									 "The Licensing Team";

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



}
