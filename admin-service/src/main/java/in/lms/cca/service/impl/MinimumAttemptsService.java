package in.lms.cca.service.impl;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.Intent;
import in.lms.cca.entity.MinimumAttempts;
import in.lms.cca.entity.logs.IntentLogs;
import in.lms.cca.entity.logs.MinimumAttemptLog;
import in.lms.cca.entity.logs.stateLogs;
import in.lms.cca.repository.MinimumAttemptsRepository;
import in.lms.cca.repository.logs.IntentRepositoryLogs;
import in.lms.cca.repository.logs.MinimumAttemptsRepositoryLogs;
import in.lms.cca.service.IMinimumAttemptsService;
import in.lms.cca.util.global.Constant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class MinimumAttemptsService implements IMinimumAttemptsService{

	@Autowired
	private MinimumAttemptsRepository minimumAttemptsRepo;
	
	@Autowired
	private HttpServletRequest request;
	
	private final Set<String> clientHostnames = new HashSet<>();
	
	@Autowired
	private MinimumAttemptsRepositoryLogs log;
	
	@Override
	public Optional<MinimumAttempts> addMinimumAttempts(MinimumAttempts minimumAttemptsObj) {
		if(minimumAttemptsObj == null)
			return Optional.empty();
		
		try { 
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";
	        MinimumAttempts savedMinimumAttempts = minimumAttemptsRepo.save(minimumAttemptsObj);
	        if (usernameFromToken != null) {
	            log.save(new MinimumAttemptLog(savedMinimumAttempts, ipAddress, Constant.CREATED, usernameFromToken));
	        }
	        System.out.println("Client Hostnames: " + clientHostname);
	        System.out.println("Client IP Address: " + ipAddress);
	        System.out.println("Token: " + bearerToken);
	        System.out.println("Username from Token: " + usernameFromToken);

	        return Optional.of(savedMinimumAttempts);
	    
	}catch(Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	@Override
	public Optional<MinimumAttempts> updateMinimumAttempts(MinimumAttempts minimumAttemptsObj) {
		if(minimumAttemptsObj == null)
			return Optional.empty();
		
		if(minimumAttemptsObj.getAttemptId() == null)
			return Optional.empty();
		
		try { 
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";
	        MinimumAttempts savedMinimumAttempts = minimumAttemptsRepo.save(minimumAttemptsObj);
	        if (usernameFromToken != null) {
	            log.save(new MinimumAttemptLog(savedMinimumAttempts, ipAddress, Constant.CREATED, usernameFromToken));
	        }
	        System.out.println("Client Hostnames: " + clientHostname);
	        System.out.println("Client IP Address: " + ipAddress);
	        System.out.println("Token: " + bearerToken);
	        System.out.println("Username from Token: " + usernameFromToken);

	        return Optional.of(savedMinimumAttempts);
	    
	}catch(Exception e) {
			return Optional.empty();
		}
	}

	@Override
	public List<MinimumAttempts> getAllMinimumAttempts() {
		return minimumAttemptsRepo.findAll(Sort.by(Sort.Direction.DESC, "created"));
	}
	
	@Override
	public MinimumAttempts getMinimumAttemptsById(Long id) {
		return minimumAttemptsRepo.findByMinimumAttemptsId(id);
	}

	@Override
	public boolean deleteByMinimumAttemptId(Long minimumId) {
		
		MinimumAttempts minimumAttempt = minimumAttemptsRepo.findById(minimumId).orElse(null);
		    
		    if (minimumAttempt == null) {
		        return false;  
		    }
	    try {
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";
	       
	        minimumAttemptsRepo.deleteMinimumAttempts(minimumId);
	        if (usernameFromToken != null) {
	            log.save(new MinimumAttemptLog(minimumAttempt, ipAddress, Constant.DELETED, usernameFromToken));
	        }
	        System.out.println("Client Hostname: " + clientHostname);
	        System.out.println("Client IP Address: " + ipAddress);
	        System.out.println("Token: " + bearerToken);
	        System.out.println("Username from Token: " + usernameFromToken);

	        return true;
	}catch (Exception e) {
		return false;
	}
	}

	@Override
	public List<MinimumAttempts> getAllActiveMinimumAttempts() {
		// TODO Auto-generated method stub
		return minimumAttemptsRepo.findAllActiveMinimumAttempt();
	}

}
