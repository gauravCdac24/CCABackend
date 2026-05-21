package in.lms.cca.service.impl;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import in.lms.cca.dto.AuditReportCriteriaDTO;
import in.lms.cca.dto.AuditRequestDTO;
import in.lms.cca.entity.AuditCheck;
import in.lms.cca.entity.AuditControl;
import in.lms.cca.entity.logs.AuditCheckLogs;
import in.lms.cca.entity.logs.AuditControlLogs;
import in.lms.cca.repository.AuditControlRepository;
import in.lms.cca.repository.logs.AuditCheckRepositoryLogs;
import in.lms.cca.repository.logs.AuditControlRepositoryLogs;
import in.lms.cca.service.IAuditControlService;
import in.lms.cca.util.global.Constant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuditControlServiceImpl implements IAuditControlService{

	@Autowired
	private AuditControlRepository auditControlRepo;
	
	@Autowired
	private HttpServletRequest request;
	
	private final Set<String> clientHostnames = new HashSet<>();
	
	@Autowired
	private AuditControlRepositoryLogs log;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${apigateway.service.url}")
	private String apigatewayServiceUrl;
	
	@Override
	public Optional<AuditControl> addAuditControl(AuditControl auditControl) {
		
		if(auditControl == null)
			return Optional.empty();
		
		try { 
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";
	        AuditControl savedAuditControl = auditControlRepo.save(auditControl);
	        if (usernameFromToken != null) {
	            log.save(new AuditControlLogs(savedAuditControl, ipAddress, Constant.CREATED, usernameFromToken));
	        }
	        System.out.println("Client Hostnames: " + clientHostname);
	        System.out.println("Client IP Address: " + ipAddress);
	        System.out.println("Token: " + bearerToken);
	        System.out.println("Username from Token: " + usernameFromToken);

	        return Optional.of(auditControl);
	    
	}catch(Exception e) {
			return Optional.empty();
		}
		
	}

	@Override
	public Optional<AuditControl> updateAuditControl(AuditControl auditControl) {
		
		if(auditControl == null)
			return Optional.empty();
		
		if(auditControl.getAuditControlId() == null)
			return Optional.empty();
		
		try { 
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";
	        AuditControl savedAuditControl = auditControlRepo.save(auditControl);
	        if (usernameFromToken != null) {
	            log.save(new AuditControlLogs(savedAuditControl, ipAddress, Constant.UPDATED, usernameFromToken));
	        }
	        System.out.println("Client Hostnames: " + clientHostname);
	        System.out.println("Client IP Address: " + ipAddress);
	        System.out.println("Token: " + bearerToken);
	        System.out.println("Username from Token: " + usernameFromToken);

	        return Optional.of(auditControl);
	    
	}catch(Exception e) {
			return Optional.empty();
		}

		
	}

	@Override
	public boolean deleteByAuditControlId(Long auditControlId) {

		AuditControl auditControl = auditControlRepo.findByAuditControlId(auditControlId);
		
		if(auditControl == null)
			return false;
		
		try {
			auditControlRepo.deleteByAuditControlId(auditControlId);
			return true;
		}catch(Exception e) {
			return false;
		}
		
		
	}

	@Override
	public AuditControl getByAuditControlId(Long auditControlId) {
		
		return auditControlRepo.findByAuditControlId(auditControlId);
		
	}

	@Override
	public List<AuditControl> getAllAuditControl() {

		return auditControlRepo.findAll(Sort.by(Sort.Direction.ASC, "created"));
		
	}

	@Override
	public List<AuditControl> getAllActiveAuditControl() {
		return auditControlRepo.findAllActiveAuditControl();
	}

	@Override
	public List<AuditControl> getAllInActiveAuditControl() {
		return auditControlRepo.findAllInActiveAuditControl();
	}

	@Override
	public List<AuditControl> getAllAuditControlByParameterId(Long auditParameterId) {
		return auditControlRepo.findAllAuditControlByParameterId(auditParameterId);
	}

	@Override
	public List<AuditControl> getAllActiveAuditControlByParameterId(Long auditParameterId) {
		return auditControlRepo.findAllActiveAuditControlByParameterId(auditParameterId);
	}

	@Override
	public List<AuditControl> getAllInActiveAuditControlByParameterId(Long auditParameterId) {
		return auditControlRepo.findAllInActiveAuditControlByParameterId(auditParameterId);
	}

	@Override
	public List<AuditControl> getAllAuditControlByCheckId(Long auditCheckId) {
		return auditControlRepo.findAllAuditControlByCheckId(auditCheckId);
	}

	@Override
	public List<AuditControl> getAllActiveAuditControlByCheckId(Long auditCheckId) {
		return auditControlRepo.findAllActiveAuditControlByCheckId(auditCheckId);
	}

	@Override
	public List<AuditControl> getAllInActiveAuditControlByCheckId(Long auditCheckId) {
		return auditControlRepo.findAllInActiveAuditControlByCheckId(auditCheckId);
	}

	@Override
	public List<AuditControl> getAllAuditControlByControlType(Long auditControlTypeId) {
		return auditControlRepo.findAllAuditControlByControlType(auditControlTypeId);
	}

	@Override
	public List<AuditControl> getAllActiveAuditControlByControlType(Long auditControlTypeId) {
		return auditControlRepo.findAllActiveAuditControlByControlType(auditControlTypeId);
	}

	@Override
	public List<AuditControl> getAllInActiveAuditControlByControlType(Long auditControlTypeId) {
		return auditControlRepo.findAllInActiveAuditControlByControlType(auditControlTypeId);
	}

	@Override
	public AuditControl getAuditControlByDesc(String desc) {
		
		return auditControlRepo.findByAuditControlDesc(desc);
	}

	@Override
	public List<AuditRequestDTO> getAllAuditRequestByUserName(String userName) {
	    String uriTemplate = apigatewayServiceUrl + "/audit-service/audit-controls/{userName}";
	    
	    Map<String, String> uriVariables = new HashMap<>();
	    uriVariables.put("userName", userName);

	    try {
	        ResponseEntity<AuditRequestDTO[]> response = restTemplate.getForEntity(uriTemplate, AuditRequestDTO[].class, uriVariables);
	        
	        // Handle null body or empty response
	        if (response.getBody() == null) {
	            return new ArrayList<>();
	        }
	        return Arrays.asList(response.getBody());
	    } catch (Exception e) {
	        System.out.println("Error fetching audit requests for user " + userName + ": " + e.getMessage());
	        return new ArrayList<>();
	    }
	}

	@Override
	public List<AuditReportCriteriaDTO> getAllNCAuditReportCriteria(String applicantUserName) {
        String uriTemplate = apigatewayServiceUrl + "/audit-service/get-All-Audit-Report-Criteria-Details/{applicantUserName}";
	    
	    Map<String, String> uriVariables = new HashMap<>();
	    uriVariables.put("applicantUserName", applicantUserName);

	    try {
	        ResponseEntity<AuditReportCriteriaDTO[]> response = restTemplate.getForEntity(uriTemplate, AuditReportCriteriaDTO[].class, uriVariables);
	        
	        // Handle null body or empty response
	        if (response.getBody() == null) {
	            return new ArrayList<>();
	        }
	        return Arrays.asList(response.getBody());
	    } catch (Exception e) {
	        System.out.println("Error fetching NC audit report criteria for user " + applicantUserName + ": " + e.getMessage());
	        return new ArrayList<>();
	    }
	}

	
	
	
}
