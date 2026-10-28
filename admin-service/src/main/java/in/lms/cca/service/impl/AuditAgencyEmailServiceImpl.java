package in.lms.cca.service.impl;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.Address;
import in.lms.cca.entity.AuditAgencyEmail;
import in.lms.cca.entity.logs.AddressLogs;
import in.lms.cca.entity.logs.AuditAgencyEmailLogs;
import in.lms.cca.repository.AuditAgencyEmailRepository;
import in.lms.cca.repository.logs.AddressRepositoryLogs;
import in.lms.cca.repository.logs.AuditAgencyEmailRepositoryLogs;
import in.lms.cca.service.IAuditAgencyEmailService;
import in.lms.cca.util.global.Constant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuditAgencyEmailServiceImpl implements IAuditAgencyEmailService{

	@Autowired
	private AuditAgencyEmailRepository emailRepo;
	

	@Autowired
	private HttpServletRequest request;
	
	 private final Set<String> clientHostnames = new HashSet<>();
	 
	 @Autowired
	 private AuditAgencyEmailRepositoryLogs log;

	
	@Override
	public Optional<AuditAgencyEmail> addAuditAgencyEmail(AuditAgencyEmail auditAgencyEmail) {
		
		if(auditAgencyEmail == null)
			return Optional.empty();
		
		try { 
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";
	        AuditAgencyEmail savedAuditAgencyEmail = emailRepo.save(auditAgencyEmail);
	        if (usernameFromToken != null) {
	        	
	        	AuditAgencyEmailLogs emailLog = new AuditAgencyEmailLogs(savedAuditAgencyEmail, ipAddress, Constant.CREATED, usernameFromToken);
	            
	            // Explicitly set the hostname
	            emailLog.setClientHostName(clientHostname);
	           // log.save(new AuditAgencyEmailLogs(savedAuditAgencyEmail, ipAddress, Constant.CREATED, usernameFromToken));
	            log.save(emailLog);
	        }
	        System.out.println("Client Hostnames: " + clientHostname);
	        System.out.println("Client IP Address: " + ipAddress);
	        System.out.println("Token: " + bearerToken);
	        System.out.println("Username from Token: " + usernameFromToken);

	        return Optional.of(savedAuditAgencyEmail);
	    
	}catch(Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
		
	}

	@Override
	public Optional<AuditAgencyEmail> updateAuditAgencyEmail(AuditAgencyEmail auditAgencyEmail) {
		

		if(auditAgencyEmail == null)
			return Optional.empty();
		
		if(auditAgencyEmail.getAgencyEmailId() == null)
			return Optional.empty();
		
		try { 
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";
	        AuditAgencyEmail savedAuditAgencyEmail = emailRepo.save(auditAgencyEmail);
	        if (usernameFromToken != null) {
	            log.save(new AuditAgencyEmailLogs(savedAuditAgencyEmail, ipAddress, Constant.UPDATED, usernameFromToken));
	        }
	        System.out.println("Client Hostnames: " + clientHostname);
	        System.out.println("Client IP Address: " + ipAddress);
	        System.out.println("Token: " + bearerToken);
	        System.out.println("Username from Token: " + usernameFromToken);

	        return Optional.of(savedAuditAgencyEmail);
	    
	}catch(Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
		
	}

	@Override
	public boolean deleteEmailByAuditAgencyId(Long auditAgencyId) {
	    List<AuditAgencyEmail> auditAgencyEmailList = emailRepo.findAllAuditAgencyEmailByAuditAgencyId(auditAgencyId);

	    if (auditAgencyEmailList.isEmpty()) {
	        return false;
	    }

	    try {
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";
	        emailRepo.deleteEmailByAuditAgencyId(auditAgencyId);
	        if (usernameFromToken != null) {
	            for (AuditAgencyEmail email : auditAgencyEmailList) {
	                log.save(new AuditAgencyEmailLogs(email, ipAddress, "Deleted", usernameFromToken));
	            }
	        }

	        System.out.println("Client Hostname: " + clientHostname);
	        System.out.println("Client IP Address: " + ipAddress);
	        System.out.println("Token: " + bearerToken);
	        System.out.println("Username from Token: " + usernameFromToken);

	        return true;
	    } catch (Exception e) {
	        return false;
	    }
	}


	@Override
	public boolean deleteByAuditAgencyEmailId(Long auditAgencyEmailId) {
		
		AuditAgencyEmail auditAgencyEmail = emailRepo.findAuditAgencyEmailById(auditAgencyEmailId);
		
		if(auditAgencyEmail == null)
			return false;
		
		try {
			emailRepo.deleteByAuditAgencyEmailId(auditAgencyEmailId);
			return true;
		}catch(Exception e) {
			return false;
		}
		
	}

	@Override
	public List<AuditAgencyEmail> getAllActiveAuditAgencyEmail() {
		
		return emailRepo.findAllActiveAuditAgencyEmail();
		
	}

	@Override
	public List<AuditAgencyEmail> getAllInActiveAuditAgencyEmail() {
		
		return emailRepo.findAllInActiveAuditAgencyEmail();
		
	}

	@Override
	public AuditAgencyEmail getAuditAgencyEmailById(Long agencyEmailId) {

		return emailRepo.findAuditAgencyEmailById(agencyEmailId);
		
	}

	@Override
	public List<AuditAgencyEmail> getAllAuditAgencyEmailByAuditAgencyId(Long auditAgencyId) {

		return emailRepo.findAllAuditAgencyEmailByAuditAgencyId(auditAgencyId);
		
	}

	@Override
	public List<AuditAgencyEmail> getAllActiveAuditAgencyEmailByAuditAgencyId(Long auditAgencyId) {

		return emailRepo.findAllActiveAuditAgencyEmailByAuditAgencyId(auditAgencyId);
		
	}

	@Override
	public List<AuditAgencyEmail> getAllInactiveAuditAgencyEmailByAuditAgencyId(Long auditAgencyId) {

		return emailRepo.findAllInactiveAuditAgencyEmailByAuditAgencyId(auditAgencyId);
		
	}

	@Override
	public List<AuditAgencyEmail> getAllAuditAgencyEmail() {

		return emailRepo.findAll(Sort.by(Sort.Direction.DESC, "created"));
		
	}

	@Override
	public List<AuditAgencyEmail> findByAuditAgencyId(Long auditAgencyId) {
		// TODO Auto-generated method stub
		return emailRepo.findByAuditAgencyId(auditAgencyId);
	}

	
	
}
