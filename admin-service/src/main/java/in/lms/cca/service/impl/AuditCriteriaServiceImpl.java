package in.lms.cca.service.impl;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.AuditCriteria;
import in.lms.cca.entity.logs.AuditCriteriaLogs;
import in.lms.cca.repository.AuditCriteriaRepository;
import in.lms.cca.repository.logs.AuditCriteriaRepositoryLogs;
import in.lms.cca.service.IAuditCriteriaService;
import in.lms.cca.util.global.Constant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuditCriteriaServiceImpl implements IAuditCriteriaService{

	@Autowired
	private AuditCriteriaRepository auditCriteriaRepo;
	
	@Autowired
	private HttpServletRequest request;
	
	private final Set<String> clientHostnames = new HashSet<>();
	
	@Autowired
	private AuditCriteriaRepositoryLogs log;

	@Override
	public Optional<AuditCriteria> addAuditCriteria(AuditCriteria auditCriteria) {
		
		if(auditCriteria == null)
			return Optional.empty();
		try { 
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";
	        AuditCriteria savedAuditCritetria = auditCriteriaRepo.save(auditCriteria);
	        if (usernameFromToken != null) {
	            log.save(new AuditCriteriaLogs(savedAuditCritetria, ipAddress, Constant.CREATED, usernameFromToken));
	        }
	        System.out.println("Client Hostnames: " + clientHostname);
	        System.out.println("Client IP Address: " + ipAddress);
	        System.out.println("Token: " + bearerToken);
	        System.out.println("Username from Token: " + usernameFromToken);

	        return Optional.of(auditCriteria);
	    
	}catch(Exception e) {
			return Optional.empty();
		}
		
		
	}

	@Override
	public Optional<AuditCriteria> updateAuditCriteria(AuditCriteria auditCriteria) {

		if(auditCriteria == null)
			return Optional.empty();
		
		if(auditCriteria.getAuditCriteriaId() == null)
			return Optional.empty();
		
		try { 
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";
	        AuditCriteria savedAuditCritetria = auditCriteriaRepo.save(auditCriteria);
	        if (usernameFromToken != null) {
	            log.save(new AuditCriteriaLogs(savedAuditCritetria, ipAddress, Constant.UPDATED, usernameFromToken));
	        }
	        System.out.println("Client Hostnames: " + clientHostname);
	        System.out.println("Client IP Address: " + ipAddress);
	        System.out.println("Token: " + bearerToken);
	        System.out.println("Username from Token: " + usernameFromToken);

	        return Optional.of(auditCriteria);
	    
	}catch(Exception e) {
		
			return Optional.empty();
		}

		
	}

	@Override
	public boolean deleteByAuditCriteriaId(Long auditCriteriaId) {

		AuditCriteria auditCriteria = auditCriteriaRepo.findByAuditCriteriaId(auditCriteriaId);
		
		if(auditCriteria == null)
			return false;
		
		try {
			auditCriteriaRepo.deleteByAuditCriteriaId(auditCriteriaId);
			return true;
		}catch(Exception e) {
			return false;
		}
		
	}

	@Override
	public AuditCriteria getByAuditCriteriaId(Long auditCriteriaId) {
		
		return auditCriteriaRepo.findByAuditCriteriaId(auditCriteriaId);
		
	}

	@Override
	public List<AuditCriteria> getAllAuditCriteria() {

		return auditCriteriaRepo.findAll(Sort.by(Sort.Direction.ASC, "created"));
		
	}

	@Override
	public List<AuditCriteria> getAllActiveAuditCriteria() {

		return auditCriteriaRepo.findAllActiveAuditCriteria();
		
	}

	@Override
	public List<AuditCriteria> getAllInActiveAuditCriteria() {

		return auditCriteriaRepo.findAllInActiveAuditCriteria();
		
	}

	@Override
	public AuditCriteria getAuditCriteriaByTitle(String title) {
		
		return auditCriteriaRepo.findAuditCriteriaByTitle(title);
	}
	
}
