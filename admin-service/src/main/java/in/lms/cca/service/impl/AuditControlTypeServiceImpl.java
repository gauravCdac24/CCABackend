package in.lms.cca.service.impl;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.AuditControlType;
import in.lms.cca.entity.logs.AuditControlTypeLogs;
import in.lms.cca.repository.AuditControlTypeRepository;
import in.lms.cca.repository.logs.AuditControlTypeRepositoryLogs;
import in.lms.cca.service.IAuditControlTypeService;
import in.lms.cca.util.global.Constant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuditControlTypeServiceImpl implements IAuditControlTypeService{

	@Autowired
	private AuditControlTypeRepository auditControlTypeRepo;
	
	@Autowired
	private HttpServletRequest request;
	
	private final Set<String> clientHostnames = new HashSet<>();
	
	@Autowired
	private AuditControlTypeRepositoryLogs log;
	

	@Override
	public Optional<AuditControlType> addAuditControlType(AuditControlType auditControlType) {
		
		if(auditControlType == null)
			return Optional.empty();
		
		try { 
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";
	        AuditControlType savedAuditControlType = auditControlTypeRepo.save(auditControlType);
	        if (usernameFromToken != null) {
	            log.save(new AuditControlTypeLogs(savedAuditControlType, ipAddress, Constant.CREATED, usernameFromToken));
	        }
	        System.out.println("Client Hostnames: " + clientHostname);
	        System.out.println("Client IP Address: " + ipAddress);
	        System.out.println("Token: " + bearerToken);
	        System.out.println("Username from Token: " + usernameFromToken);

	        return Optional.of(auditControlType);
	    
	}catch(Exception e) {
			return Optional.empty();
		}
		
	}

	@Override
	public Optional<AuditControlType> updateAuditControlType(AuditControlType auditControlType) {
		
		if(auditControlType == null)
			return Optional.empty();
		
		if(auditControlType.getAuditControlTypeId() == null)
			return Optional.empty();
		
		try { 
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";
	        AuditControlType savedAuditControlType = auditControlTypeRepo.save(auditControlType);
	        if (usernameFromToken != null) {
	            log.save(new AuditControlTypeLogs(savedAuditControlType, ipAddress, Constant.CREATED, usernameFromToken));
	        }
	        System.out.println("Client Hostnames: " + clientHostname);
	        System.out.println("Client IP Address: " + ipAddress);
	        System.out.println("Token: " + bearerToken);
	        System.out.println("Username from Token: " + usernameFromToken);

	        return Optional.of(auditControlType);
	    
	}catch(Exception e) {
			return Optional.empty();
		}
		
	}

	@Override
	public boolean deleteByAuditControlTypeId(Integer auditControlTypeId) {

		AuditControlType auditControlType = auditControlTypeRepo.findByAuditControlTypeId(auditControlTypeId);
		
		if(auditControlType == null)
			return false;
		
		try {
			auditControlTypeRepo.deleteByAuditControlTypeId(auditControlTypeId);
			return true;
		}catch(Exception e) {
			return false;
		}
		
	}

	@Override
	public AuditControlType getByAuditControlTypeId(Integer auditControlTypeId) {

		return auditControlTypeRepo.findByAuditControlTypeId(auditControlTypeId);
		
	}

	@Override
	public List<AuditControlType> getAllActiveAuditControlType() {

		return auditControlTypeRepo.findAllActiveAuditControlType();
		
	}

	@Override
	public List<AuditControlType> getAllInactiveAuditControlType() {

		return auditControlTypeRepo.findAllInactiveAuditControlType();
		
	}

	@Override
	public List<AuditControlType> getAllAuditControlType() {

		return auditControlTypeRepo.findAll(Sort.by(Sort.Direction.ASC, "created"));
		
	}

	@Override
	public AuditControlType getAuditControlTypeByDesc(String desc) {
		return auditControlTypeRepo.findAuditControlTypeByDesc(desc);
	}
	
}
