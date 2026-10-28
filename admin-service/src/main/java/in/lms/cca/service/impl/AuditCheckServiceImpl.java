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
import in.lms.cca.entity.AuditCheck;
import in.lms.cca.entity.logs.AuditAgencyLogs;
import in.lms.cca.entity.logs.AuditCheckLogs;
import in.lms.cca.repository.AuditCheckRepository;
import in.lms.cca.repository.logs.AuditAgencyRepositoryLogs;
import in.lms.cca.repository.logs.AuditCheckRepositoryLogs;
import in.lms.cca.service.IAuditCheckService;
import in.lms.cca.util.global.Constant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuditCheckServiceImpl implements IAuditCheckService{

	@Autowired
	private AuditCheckRepository auditCheckRepo;
	
	@Autowired
	private HttpServletRequest request;
	
	private final Set<String> clientHostnames = new HashSet<>();
	
	@Autowired
	private AuditCheckRepositoryLogs log;
	
	@Override
	public Optional<AuditCheck> addAuditCheck(AuditCheck auditCheck) {

		if(auditCheck == null)
			return Optional.empty();
		
		try { 
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";
	        AuditCheck savedAuditCheck = auditCheckRepo.save(auditCheck);
	        if (usernameFromToken != null) {
	            log.save(new AuditCheckLogs(savedAuditCheck, ipAddress, Constant.CREATED, usernameFromToken));
	        }
	        System.out.println("Client Hostnames: " + clientHostname);
	        System.out.println("Client IP Address: " + ipAddress);
	        System.out.println("Token: " + bearerToken);
	        System.out.println("Username from Token: " + usernameFromToken);

	        return Optional.of(auditCheck);
	    
	}catch(Exception e){
			e.printStackTrace();
			return Optional.empty();
		}
	}

	@Override
	public Optional<AuditCheck> updateAuditCheck(AuditCheck auditCheck) {
		
		if(auditCheck == null)
			return Optional.empty();
		
		if(auditCheck.getAuditCheckId() == null)
			return Optional.empty();
		try { 
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";
	        AuditCheck savedAuditCheck = auditCheckRepo.save(auditCheck);
	        if (usernameFromToken != null) {
	            log.save(new AuditCheckLogs(savedAuditCheck, ipAddress, Constant.CREATED, usernameFromToken));
	        }
	        System.out.println("Client Hostnames: " + clientHostname);
	        System.out.println("Client IP Address: " + ipAddress);
	        System.out.println("Token: " + bearerToken);
	        System.out.println("Username from Token: " + usernameFromToken);

	        return Optional.of(auditCheck);
	    
	}catch(Exception e){
			return Optional.empty();
		}
	}

	@Override
	public AuditCheck getAuditCheckById(Long id) {
		
		return auditCheckRepo.findByAuditCheckId(id);
		
	}

	@Override
	public List<AuditCheck> getAllActiveAuditCheck() {

		return auditCheckRepo.findAllActiveAuditCheck();
	}

	@Override
	public List<AuditCheck> getAllInActiveAuditCheck() {

		return auditCheckRepo.findAllInActiveAuditCheck();
		
	}

	@Override
	public List<AuditCheck> getAllAuditCheck() {
		
		return auditCheckRepo.findAll(Sort.by(Sort.Direction.ASC, "created"));
	}

	@Override
	public boolean deleteAuditCheckById(Long id) {

		AuditCheck auditCheck = auditCheckRepo.findByAuditCheckId(id);

		if(auditCheck == null)
			return false;
		try { 
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";
	        auditCheckRepo.deleteByAuditCheckId(id);
	        if (usernameFromToken != null) {
	        
	                log.save(new AuditCheckLogs(auditCheck, ipAddress, Constant.DELETED, usernameFromToken));
	            
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
	public AuditCheck getAuditCheckByDesc(String desc) {
		return auditCheckRepo.findAuditCheckByDesc(desc);
	}

}
