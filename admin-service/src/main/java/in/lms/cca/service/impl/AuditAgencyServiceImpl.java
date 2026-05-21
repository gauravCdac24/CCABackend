package in.lms.cca.service.impl;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.AuditAgency;
import in.lms.cca.entity.AuditAgencyMobile;
import in.lms.cca.entity.logs.AuditAgencyLogs;
import in.lms.cca.entity.logs.AuditAgencyMobileLogs;
import in.lms.cca.repository.AuditAgencyRepository;
import in.lms.cca.repository.logs.AuditAgencyMobileRepositoryLogs;
import in.lms.cca.repository.logs.AuditAgencyRepositoryLogs;
import in.lms.cca.service.IAuditAgencyService;
import in.lms.cca.util.global.Constant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuditAgencyServiceImpl implements IAuditAgencyService{

	@Autowired
	private AuditAgencyRepository auditAgencyRepo;
	
	@Autowired
	private HttpServletRequest request;
	
	private final Set<String> clientHostnames = new HashSet<>();
	
	@Autowired
	private AuditAgencyRepositoryLogs log;
	
	@Override
	public Optional<AuditAgency> addAuditAgency(AuditAgency auditAgency) {
		
		if(auditAgency == null)
			return Optional.empty();
		
		try { 
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";
	        AuditAgency savedAuditAgency = auditAgencyRepo.save(auditAgency);
	        if (usernameFromToken != null) {
	            log.save(new AuditAgencyLogs(savedAuditAgency, ipAddress, Constant.CREATED, usernameFromToken));
	        }
	        System.out.println("Client Hostnames: " + clientHostname);
	        System.out.println("Client IP Address: " + ipAddress);
	        System.out.println("Token: " + bearerToken);
	        System.out.println("Username from Token: " + usernameFromToken);

	        return Optional.of(savedAuditAgency);
	    
	}catch(Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
		
	}

	@Override
	public Optional<AuditAgency> updateAddress(AuditAgency auditAgency) {
		
		if(auditAgency == null)
			return Optional.empty();
		
		if(auditAgency.getAuditAgencyId() == null)
			return Optional.empty();
		try { 
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";
	        AuditAgency savedAuditAgency = auditAgencyRepo.save(auditAgency);
	        if (usernameFromToken != null) {
	        	
	            log.save(new AuditAgencyLogs(savedAuditAgency, ipAddress, Constant.UPDATED, usernameFromToken));
	            
	            
	        }
	        System.out.println("Client Hostnames: " + clientHostname);
	        System.out.println("Client IP Address: " + ipAddress);
	        System.out.println("Token: " + bearerToken);
	        System.out.println("Username from Token: " + usernameFromToken);

	        return Optional.of(savedAuditAgency);
	    
	}catch(Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
		
	}

	@Override
	public AuditAgency getAuditAgencyById(Long id) {
		
		return auditAgencyRepo.findAuditAgencyById(id);
		
	}

	@Override
	public List<AuditAgency> getAllAuditAgency(Long id) {

		return auditAgencyRepo.findAll(Sort.by(Sort.Direction.DESC, "created"));
		
	}

	@Override
	public List<AuditAgency> getAllActiveAuditAgency(Long id) {
		
		return auditAgencyRepo.findAllActiveAuditAgency();
		
	}

	@Override
	public List<AuditAgency> getAllInactiveAuditAgency(Long id) {
		
		return auditAgencyRepo.findAllInActiveAuditAgency();
		
	}

	@Override
	public boolean deleteAuditAgencyById(Long id) {

		AuditAgency auditAgency = auditAgencyRepo.findAuditAgencyById(id);
		
		if(auditAgency == null)
			return false;
		
		try { 
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";
	        auditAgencyRepo.deleteByAuditAgencyId(id);
	        if (usernameFromToken != null) {
	        
	                log.save(new AuditAgencyLogs(auditAgency, ipAddress, Constant.DELETED, usernameFromToken));
	            
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
	public List<AuditAgency> getAllAuditAgency() {
		// TODO Auto-generated method stub
		return auditAgencyRepo.findAll(Sort.by(Sort.Direction.DESC, "created"));
	}

	
	@Override
	public AuditAgency getAuditAgencyByCreatedBy(String createdBy) {
		return auditAgencyRepo.findByCreatedBy(createdBy);
	}
	
	@Override
	public AuditAgency getAuditAgencyByAgencyName(String agencyName) {
		return auditAgencyRepo.findByAgencyName(agencyName);
	}
}
