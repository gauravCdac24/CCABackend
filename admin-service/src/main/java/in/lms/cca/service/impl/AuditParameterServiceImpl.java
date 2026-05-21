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
import in.lms.cca.entity.AuditParameter;
import in.lms.cca.entity.logs.AuditCriteriaLogs;
import in.lms.cca.entity.logs.AuditParameterLogs;
import in.lms.cca.repository.AuditParameterRepository;
import in.lms.cca.repository.logs.AuditCriteriaRepositoryLogs;
import in.lms.cca.repository.logs.AuditParameterRepositoryLogs;
import in.lms.cca.service.IAuditParameterService;
import in.lms.cca.util.global.Constant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuditParameterServiceImpl implements IAuditParameterService{

	@Autowired
	private AuditParameterRepository auditParameterRepo;
	
	@Autowired
	private HttpServletRequest request;
	
	private final Set<String> clientHostnames = new HashSet<>();
	
	@Autowired
	private AuditParameterRepositoryLogs log;

	
	@Override
	public Optional<AuditParameter> addAuditParameter(AuditParameter auditParameter) {
		
		if(auditParameter == null)
			return Optional.empty();
		
		try { 
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";
	        AuditParameter savedAuditParameter = auditParameterRepo.save(auditParameter);
	        if (usernameFromToken != null) {
	            log.save(new AuditParameterLogs(savedAuditParameter, ipAddress, Constant.CREATED, usernameFromToken));
	        }
	        System.out.println("Client Hostnames: " + clientHostname);
	        System.out.println("Client IP Address: " + ipAddress);
	        System.out.println("Token: " + bearerToken);
	        System.out.println("Username from Token: " + usernameFromToken);

	        return Optional.of(auditParameter);
	    
	}catch(Exception e) {
			return Optional.empty();
		}
		
	}

	@Override
	public Optional<AuditParameter> updateAuditParameter(AuditParameter auditParameter) {

		
		if(auditParameter == null)
			return Optional.empty();
		
		if(auditParameter.getAuditParameterId() == null)
			return Optional.empty();
		
		try { 
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";
	        AuditParameter savedAuditParameter = auditParameterRepo.save(auditParameter);
	        if (usernameFromToken != null) {
	            log.save(new AuditParameterLogs(savedAuditParameter, ipAddress, Constant.UPDATED, usernameFromToken));
	        }
	        System.out.println("Client Hostnames: " + clientHostname);
	        System.out.println("Client IP Address: " + ipAddress);
	        System.out.println("Token: " + bearerToken);
	        System.out.println("Username from Token: " + usernameFromToken);

	        return Optional.of(auditParameter);
	    
	}catch(Exception e) {
			return Optional.empty();
		}

	
	}

	@Override
	public boolean deleteByAuditParameterId(Long auditParameterId) {

		AuditParameter auditParameterObj = auditParameterRepo.findByAuditParameterId(auditParameterId);
		
		if(auditParameterObj == null)
			return false;
		
		try {
			auditParameterRepo.deleteByAuditParameterId(auditParameterId);
			return true;
		}catch(Exception e) {
			return false;
		}
		
	
	}

	@Override
	public boolean deleteByAuditSubCriteriaId(Long auditSubCriteriaId) {

		List<AuditParameter> auditParameterObj = auditParameterRepo.findAllByAuditSubCriteriaId(auditSubCriteriaId);
		
		if(auditParameterObj.size()==0)
			return false;
		try {
			auditParameterRepo.deleteByAuditSubCriteriaId(auditSubCriteriaId);
			return true;
		}catch(Exception e) {
			return false;
		}
		
		
	}

	@Override
	public AuditParameter getByAuditParameterId(Long auditParameterId) {
		
		return auditParameterRepo.findByAuditParameterId(auditParameterId);
		
	}

	@Override
	public List<AuditParameter> getAllByAuditSubCriteriaId(Long auditSubCriteriaId) {

		return auditParameterRepo.findAllByAuditSubCriteriaId(auditSubCriteriaId);
		
	}

	@Override
	public List<AuditParameter> getAllActiveByAuditSubCriteriaId(Long auditSubCriteriaId) {
		return auditParameterRepo.findAllActiveByAuditSubCriteriaId(auditSubCriteriaId);
	}

	@Override
	public List<AuditParameter> getAllInactiveByAuditSubCriteriaId(Long auditSubCriteriaId) {

		return auditParameterRepo.findAllInactiveByAuditSubCriteriaId(auditSubCriteriaId);
		
	}

	@Override
	public List<AuditParameter> getAllActiveAuditParameter() {

		return auditParameterRepo.findAllActiveAuditParameter();
		
	}

	@Override
	public List<AuditParameter> getAllInActiveAuditParameter() {

		return auditParameterRepo.findAllInActiveAuditParameter();
		
	}

	@Override
	public List<AuditParameter> getAllAuditParameter() {

		return auditParameterRepo.findAll(Sort.by(Sort.Direction.ASC, "created"));
	}

	@Override
	public AuditParameter getAuditParameterByTitle(String title) {
		return auditParameterRepo.findByAuditParameterTitle(title);
	}

	
}
