package in.lms.cca.service.impl;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.AuditAgencyEmail;
import in.lms.cca.entity.AuditAgencyMobile;
import in.lms.cca.entity.logs.AuditAgencyEmailLogs;
import in.lms.cca.entity.logs.AuditAgencyMobileLogs;
import in.lms.cca.repository.AuditAgencyMobileRepository;
import in.lms.cca.repository.logs.AuditAgencyMobileRepositoryLogs;
import in.lms.cca.service.IAuditAgencyMobileService;
import in.lms.cca.util.global.Constant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuditAgencyMobileServiceImpl implements IAuditAgencyMobileService{

	@Autowired
	private AuditAgencyMobileRepository mobileRepo;
	
	@Autowired
	private HttpServletRequest request;
	
	private final Set<String> clientHostnames = new HashSet<>();
	
	@Autowired
	private AuditAgencyMobileRepositoryLogs log;

	@Autowired
	private AuditChangeLogService auditChangeLog;
	
	@Override
	public Optional<AuditAgencyMobile> addAuditAgencyMobile(AuditAgencyMobile auditAgencyMobile) {
		
		if(auditAgencyMobile == null)
			return Optional.empty();
		
		try { 
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";
	        AuditAgencyMobile savedAuditAgencyMobile = mobileRepo.saveAndFlush(auditAgencyMobile);
	        if (usernameFromToken != null) {
	            try {
	                auditChangeLog.mobileChanged(savedAuditAgencyMobile, ipAddress, clientHostname, Constant.CREATED, usernameFromToken);
	            } catch (Exception logEx) {
	                logEx.printStackTrace();
	            }
	        }
	        System.out.println("Client Hostnames: " + clientHostname);
	        System.out.println("Client IP Address: " + ipAddress);
	        System.out.println("Token: " + bearerToken);
	        System.out.println("Username from Token: " + usernameFromToken);

	        return Optional.of(savedAuditAgencyMobile);
	    
	}catch(Exception e) {
			e.printStackTrace();
			if (e instanceof RuntimeException runtimeException) {
				throw runtimeException;
			}
			throw new IllegalStateException(e.getMessage(), e);
		}
		
	}

	@Override
	public Optional<AuditAgencyMobile> updateAuditAgencyMobile(AuditAgencyMobile auditAgencyMobile) {

		if(auditAgencyMobile == null)
			return Optional.empty();
		
		if(auditAgencyMobile.getAgencyMobileId() == null)
			return Optional.empty();
		
		try { 
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";
	        AuditAgencyMobile savedAuditAgencyMobile = mobileRepo.saveAndFlush(auditAgencyMobile);
	        if (usernameFromToken != null) {
	            try {
	                auditChangeLog.mobileChanged(savedAuditAgencyMobile, ipAddress, clientHostname, Constant.UPDATED, usernameFromToken);
	            } catch (Exception logEx) {
	                logEx.printStackTrace();
	            }
	        }
	        System.out.println("Client Hostnames: " + clientHostname);
	        System.out.println("Client IP Address: " + ipAddress);
	        System.out.println("Token: " + bearerToken);
	        System.out.println("Username from Token: " + usernameFromToken);

	        return Optional.of(savedAuditAgencyMobile);
	    
	}catch(Exception e) {
			e.printStackTrace();
			if (e instanceof RuntimeException runtimeException) {
				throw runtimeException;
			}
			throw new IllegalStateException(e.getMessage(), e);
		}
		
	}

	@Override
	public boolean deleteMobileByAuditAgencyId(Long auditAgencyId) {
		
		List<AuditAgencyMobile> auditAgencyMobile = mobileRepo.findAllAuditAgencyMobileByAuditAgencyId(auditAgencyId);
		
		if(auditAgencyMobile.size()==0)
			return false;
		
		try { 
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";
	      mobileRepo.deleteMobileByAuditAgencyId(auditAgencyId);
	        if (usernameFromToken != null) {
	            for (AuditAgencyMobile mobile : auditAgencyMobile) {
	                log.save(new AuditAgencyMobileLogs(mobile, ipAddress, Constant.DELETED, usernameFromToken));
	            }
	        }
	        System.out.println("Client Hostnames: " + clientHostname);
	        System.out.println("Client IP Address: " + ipAddress);
	        System.out.println("Token: " + bearerToken);
	        System.out.println("Username from Token: " + usernameFromToken);

	        return true;
	    
	}catch(Exception e) {
			return false;
		}
		
		
	}

	@Override
	public boolean deleteByAuditAgencyMobileId(Long auditAgencyMobileId) {

		AuditAgencyMobile auditAgencyMobile = mobileRepo.findAuditAgencyMobileById(auditAgencyMobileId);
		
		if(auditAgencyMobile == null)
			return false;
		try {
			mobileRepo.deleteByAuditAgencyMobileId(auditAgencyMobileId);
			return true;
		}catch(Exception e) {
			return false;
		}

	}

	@Override
	public List<AuditAgencyMobile> getAllActiveAuditAgencyMobile() {

		return mobileRepo.findAllActiveAuditAgencyMobile();
		
	}

	@Override
	public List<AuditAgencyMobile> getAllInActiveAuditAgencyMobile() {

		return mobileRepo.findAllInActiveAuditAgencyMobile();
		
	}

	@Override
	public AuditAgencyMobile getAuditAgencyMobileById(Long agencyMobileId) {

		return mobileRepo.findAuditAgencyMobileById(agencyMobileId);
		
	}

	@Override
	public List<AuditAgencyMobile> getAllAuditAgencyMobileByAuditAgencyId(Long auditAgencyId) {

		return mobileRepo.findAllAuditAgencyMobileByAuditAgencyId(auditAgencyId);
		
	}

	@Override
	public List<AuditAgencyMobile> getAllActiveAuditAgencyMobileByAuditAgencyId(Long auditAgencyId) {

		return mobileRepo.findAllActiveAuditAgencyMobileByAuditAgencyId(auditAgencyId);
		
	}

	@Override
	public List<AuditAgencyMobile> getAllInactiveAuditAgencyMobileByAuditAgencyId(Long auditAgencyId) {

		return mobileRepo.findAllInactiveAuditAgencyMobileByAuditAgencyId(auditAgencyId);
		
	}

	@Override
	public List<AuditAgencyMobile> getAllAuditAgencyMobile() {

		return mobileRepo.findAll(Sort.by(Sort.Direction.DESC, "creation"));
		
	}

	@Override
	public List<AuditAgencyMobile> findByAuditAgencyId(Long auditAgencyId) {
		// TODO Auto-generated method stub
		return mobileRepo.findByAuditAgencyId(auditAgencyId);
	}

	
	
}
