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
import in.lms.cca.entity.Auditors;
import in.lms.cca.entity.logs.AuditAgencyLogs;
import in.lms.cca.entity.logs.AuditorsLog;
import in.lms.cca.repository.AuditorsRepository;
import in.lms.cca.repository.logs.AuditAgencyRepositoryLogs;
import in.lms.cca.repository.logs.AuditorsRepositoryLogs;
import in.lms.cca.service.IAuditorsService;
import in.lms.cca.util.global.Constant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuditorsServiceImpl implements IAuditorsService{

	@Autowired
	private AuditorsRepository auditorsRepo;

	@Autowired
	private HttpServletRequest request;
	
	private final Set<String> clientHostnames = new HashSet<>();
	
	@Autowired
	private AuditorsRepositoryLogs log;

	@Override
	public List<Auditors> getAllAuditors() {
		return auditorsRepo.findAll(Sort.by(Sort.Direction.DESC, "created"));
	}

	@Override
	public Optional<Auditors> addAuditAuditors(Auditors auditorObj) {
		if(auditorObj == null)
			return Optional.empty();
		
		try { 
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";
	        Auditors savedAuditors = auditorsRepo.save(auditorObj);
	        if (usernameFromToken != null) {
	        
	            AuditorsLog auditorsLog = new AuditorsLog(savedAuditors, ipAddress, Constant.CREATED, usernameFromToken);
	            
	            auditorsLog.setClientHostName(clientHostname); 
	            
	            log.save(auditorsLog);
	        // log.save(new AuditorsLog(savedAuditors, ipAddress, Constant.CREATED, usernameFromToken));

	        
	        }
	        System.out.println("Client Hostnames: " + clientHostname);
	        System.out.println("Client IP Address: " + ipAddress);
	        System.out.println("Token: " + bearerToken);
	        System.out.println("Username from Token: " + usernameFromToken);

	        return Optional.of(savedAuditors);
	    
	}catch(Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	@Override
	public Optional<Auditors> updateAuditors(Auditors auditorObj) {
		if(auditorObj == null)
			return Optional.empty();
		
		if(auditorObj.getAuditorsId() == null)
			return Optional.empty();
		try { 
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";
	        Auditors savedAuditors = auditorsRepo.save(auditorObj);
	        if (usernameFromToken != null) {
	            log.save(new AuditorsLog(savedAuditors, ipAddress, Constant.UPDATED, usernameFromToken));
	        }
	        System.out.println("Client Hostnames: " + clientHostname);
	        System.out.println("Client IP Address: " + ipAddress);
	        System.out.println("Token: " + bearerToken);
	        System.out.println("Username from Token: " + usernameFromToken);

	        return Optional.of(savedAuditors);
	    
	}catch(Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	@Override
	public Auditors getAuditorById(Long id) {
		return auditorsRepo.findByAuditorsId(id);
	}

	@Override
	public List<Auditors> getAllActiveAuditors() {
		return auditorsRepo.findAllActiveAuditors();
	}

	@Override
	public List<Auditors> getAllInActiveAuditors() {
		return auditorsRepo.findAllInActiveAuditors();
	}

	@Override
	public List<Auditors> findByAuditAgencyId(Long auditAgencyId) {
		// TODO Auto-generated method stub
		return auditorsRepo.findByAuditAgencyId(auditAgencyId);
	}
	
	
	
}
