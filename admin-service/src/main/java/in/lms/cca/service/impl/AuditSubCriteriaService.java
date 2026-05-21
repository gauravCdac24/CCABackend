package in.lms.cca.service.impl;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.AuditParameter;
import in.lms.cca.entity.AuditSubCriteria;
import in.lms.cca.entity.logs.AuditParameterLogs;
import in.lms.cca.entity.logs.AuditSubCriteriaLogs;
import in.lms.cca.repository.AuditSubCriteriaRepository;
import in.lms.cca.repository.logs.AuditParameterRepositoryLogs;
import in.lms.cca.repository.logs.AuditSubCriteriaRepositoryLogs;
import in.lms.cca.service.IAuditSubCriteriaService;
import in.lms.cca.util.global.Constant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuditSubCriteriaService implements IAuditSubCriteriaService{

	@Autowired
	private AuditSubCriteriaRepository auditSubCriteriaRepo;
	
	@Autowired
	private HttpServletRequest request;
	
	private final Set<String> clientHostnames = new HashSet<>();
	
	@Autowired
	private AuditSubCriteriaRepositoryLogs log;

	
	@Override
	public Optional<AuditSubCriteria> addAuditSubCriteria(AuditSubCriteria auditSubCriteriaObj) {
		
		if(auditSubCriteriaObj == null)
			return Optional.empty();
		
		try { 
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";
	        AuditSubCriteria savedAuditSubCriteria = auditSubCriteriaRepo.save(auditSubCriteriaObj);
	        if (usernameFromToken != null) {
	            log.save(new AuditSubCriteriaLogs(savedAuditSubCriteria, ipAddress, Constant.CREATED, usernameFromToken));
	        }
	        System.out.println("Client Hostnames: " + clientHostname);
	        System.out.println("Client IP Address: " + ipAddress);
	        System.out.println("Token: " + bearerToken);
	        System.out.println("Username from Token: " + usernameFromToken);

	        return Optional.of(auditSubCriteriaObj);
	    
	}catch(Exception e) {
			return Optional.empty(); 
		}
		
	}

	@Override
	public Optional<AuditSubCriteria> updateAuditSubCriteria(AuditSubCriteria auditSubCriteriaObj) {
		
		if(auditSubCriteriaObj == null)
			return Optional.empty();
		
		if(auditSubCriteriaObj.getAuditSubCriteriaId() == null)
			return Optional.empty();
		
		try { 
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";
	        AuditSubCriteria savedAuditSubCriteria = auditSubCriteriaRepo.save(auditSubCriteriaObj);
	        if (usernameFromToken != null) {
	            log.save(new AuditSubCriteriaLogs(savedAuditSubCriteria, ipAddress, Constant.UPDATED, usernameFromToken));
	        }
	        System.out.println("Client Hostnames: " + clientHostname);
	        System.out.println("Client IP Address: " + ipAddress);
	        System.out.println("Token: " + bearerToken);
	        System.out.println("Username from Token: " + usernameFromToken);

	        return Optional.of(auditSubCriteriaObj);
	    
	}catch(Exception e) {
			return Optional.empty(); 
		}
		
	}

	@Override
	public boolean deleteByAuditSubCriteriaId(Long auditSubCriteriaId) {

		AuditSubCriteria auditSubCriteriaObj = auditSubCriteriaRepo.findByAuditSubCriteriaId(auditSubCriteriaId);
		
		if(auditSubCriteriaObj == null)
			return false;
		
		try {
			auditSubCriteriaRepo.deleteByAuditSubCriteriaId(auditSubCriteriaId);
			return true;
		}catch(Exception e) {
			return false;
		}
		
		

	}

	@Override
	public boolean deleteByAuditCriteriaId(Long auditCriteriaId) {
		
		
		List<AuditSubCriteria> auditSubCriteriaObj = auditSubCriteriaRepo.findAllByAuditCriteriaId(auditCriteriaId);
		
		if(auditSubCriteriaObj.size() == 0)
			return false;
		
		try {
			auditSubCriteriaRepo.deleteByAuditCriteriaId(auditCriteriaId);
			return true;
		}catch(Exception e) {
			return false;
		}
		
		
	}

	@Override
	public AuditSubCriteria getByAuditSubCriteriaId(Long auditSubCriteriaId) {
		return auditSubCriteriaRepo.findByAuditSubCriteriaId(auditSubCriteriaId);
	}

	@Override
	public List<AuditSubCriteria> getAllByAuditCriteriaId(Long auditCriteriaId) {

		return auditSubCriteriaRepo.findAllByAuditCriteriaId(auditCriteriaId);
	}

	@Override
	public List<AuditSubCriteria> getAllActiveByAuditCriteriaId(Long auditCriteriaId) {
	
		return auditSubCriteriaRepo.findAllActiveByAuditCriteriaId(auditCriteriaId);
	}

	@Override
	public List<AuditSubCriteria> getAllInactiveByAuditCriteriaId(Long auditCriteriaId) {

		return auditSubCriteriaRepo.findAllInactiveByAuditCriteriaId(auditCriteriaId);
	}

	@Override
	public List<AuditSubCriteria> getAllActiveAuditSubCriteria() {

		return auditSubCriteriaRepo.findAllActiveAuditSubCriteria();
	}

	@Override
	public List<AuditSubCriteria> getAllEnabledForAuditorView() {
		return auditSubCriteriaRepo.findAllEnabledForAuditorView();
	}

	@Override
	public List<AuditSubCriteria> getAllInActiveAuditSubCriteria() {

		return auditSubCriteriaRepo.findAllInActiveAuditSubCriteria();
	}

	@Override
	public List<AuditSubCriteria> getAllAuditSubCriteria() {

		return auditSubCriteriaRepo.findAll(Sort.by(Sort.Direction.ASC, "created"));
	}

	@Override
	public AuditSubCriteria getAuditSubCriteriaByTitle(String title) {
		return auditSubCriteriaRepo.findAuditSubCriteriaByTitle(title);
	}

}
