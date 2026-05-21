package in.lms.cca.service.impl;


import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.IntentUniqueCode;
import in.lms.cca.payload.NotificationsDetailsRequest;
import in.lms.cca.payload.NotificationsRequest;
import in.lms.cca.repository.IntentUniqueCodeRepository;
import in.lms.cca.service.IIntentUniqueCodeService;


@Service
public class IntnetUniqueCodeServiceImpl implements IIntentUniqueCodeService{

	@Autowired
	private IntentUniqueCodeRepository urepo;
	
	@Autowired
	private NotificationClientServiceImpl notificationServ;
	
	@Override
	public Long generateUniqueCode() {
		
		String DIGITS_WITHOUT_ZERO = "123456789";
		
		StringBuilder uniqueCode = new StringBuilder();
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(DIGITS_WITHOUT_ZERO.length());
            char digit = DIGITS_WITHOUT_ZERO.charAt(index);
            uniqueCode.append(digit);
        }

        return Long.parseLong(uniqueCode.toString());
		
	}

	@Override
	public Optional<IntentUniqueCode> addUniqueCode(IntentUniqueCode obj) {
		if (obj == null)
	        return Optional.empty();

	    try {
	    	
	    	//Notification
			
			String title = "Unique Code For Registration";
			String message = "Please do not share your code with anyone, your code is " + obj.getUniqueCode();
			
			NotificationsDetailsRequest notification = new NotificationsDetailsRequest();
			
			notification.setUsername(null);
			notification.setUserEmail(obj.getEmailId());
			notification.setMessage(message);
			notification.setSubject(title);
			notification.setNotificationType("Email & SMS");
			notification.setNotificationDescription("Intent Unique Code Sent.");
			
			//notificationServ.sendNotificationUsingDetails(notification);
	    	
	    		IntentUniqueCode iobj = urepo.save(obj);
	    	
	        return Optional.of(iobj);
	    } catch (Exception e) {
	        return Optional.empty();
	    }
	}

	@Override
	public Optional<IntentUniqueCode> updateUniqueCode(IntentUniqueCode obj) {
		if (obj == null)
	        return Optional.empty();

		if(obj.getUniqueCodeId() == null)
			return Optional.empty();
		
	    try {
	    	
	    		IntentUniqueCode iobj = urepo.save(obj);	
	    		return Optional.of(iobj);
	        
	    } catch (Exception e) {
	        return Optional.empty();
	    }
	}

	@Override
	public IntentUniqueCode getByActiveUniqueCode(Long id) {
		
			return urepo.findByActiveUniqueCode(id);
	}

	@Override
	public List<IntentUniqueCode> getAllUniqueCode() {
		
		return urepo.findAll(Sort.by(Sort.Direction.DESC, "created"));
	}

	@Override
	public IntentUniqueCode getUniqueCodeById(Long id) {
		
		return urepo.findUniqueCodeById(id);
	}

	@Override
	public IntentUniqueCode getUniqueCodeByEmailId(String emailId) {
		
		return urepo.findUniqueCodeByEmailId(emailId);
	}

	@Override
	public IntentUniqueCode getUniqueCodeByMobileNo(String mobileNo) {
		
		return urepo.findUniqueCodeByMobileNo(mobileNo);
	}

	@Override
	public IntentUniqueCode getUniqueCodeByOrganizationName(String organizationName) {
		
		return urepo.findUniqueCodeByOrganizationName(organizationName);
	}

	@Override
	public IntentUniqueCode getActiveUniqueCodeById(Long id) {
		
		return urepo.findActiveUniqueCodeById(id);
	}

	@Override
	public IntentUniqueCode getByUniqueCode(Long ucode) {
		
		return urepo.findByUniqueCode(ucode);
	}

}
