package in.lms.cca.service.impl;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import in.lms.cca.entity.Country;
import in.lms.cca.entity.State;
import in.lms.cca.entity.logs.stateLogs;
import in.lms.cca.repository.StateRepository;
import in.lms.cca.repository.logs.StateRepositoryLogs;
import in.lms.cca.service.IStateService;
import in.lms.cca.util.global.Constant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class StateServiceImpl implements IStateService{

	@Autowired
	private HttpServletRequest request;
	
	 private final Set<String> clientHostnames = new HashSet<>();
	
	@Autowired
	private StateRepository stateRepo;
	
	@Autowired
	private StateRepositoryLogs log;
	
//	@Autowired
//	private JwtTokenUtil jwtUtils;

	@Override
	public Optional<State> addState(State stateObj) {
	    if (stateObj == null)
	        return Optional.empty();

	    try {
	        String ipAddress = request.getRemoteAddr();
	        
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String token = bearerToken.substring(7);
	        System.out.println(token);
	       // String usernameFromToken = jwtUtils.getUsernameFromToken(token);
//	        System.out.println("usernameFromToken:"+usernameFromToken);
	        
	        String usernameFromToken ="1";
	        State savedState = stateRepo.save(stateObj);
	        if (usernameFromToken != null) {
	            //log.save(new stateLogs(savedState, ipAddress, Constant.CREATED, usernameFromToken));
	            log.save(new stateLogs(stateObj, ipAddress, Constant.CREATED, usernameFromToken, clientHostname));

	        }
	        System.out.println("Client Hostnames: " + clientHostname);
	        System.out.println("Client IP Address: " + ipAddress);
	        System.out.println("Token: " + bearerToken);
	        System.out.println("Username from Token: " + usernameFromToken);

	       
	        return Optional.of(savedState);
	    } catch (Exception e) {
	        return Optional.empty();
	    }
	}


	@Override
	public Optional<State> updateState(State stateObj) {
		if(stateObj == null)
			return Optional.empty();
		
		if(stateObj.getStateId() == null)
			return Optional.empty();
		
		 try {
		        String ipAddress = request.getRemoteAddr();
		        InetAddress clientAddress = InetAddress.getByName(ipAddress);
		        String clientHostname = clientAddress.getHostName();
		        String bearerToken = request.getHeader("Authorization");
		        String usernameFromToken = "1";
		        State savedState = stateRepo.save(stateObj);
		        if (usernameFromToken != null) {
		            //log.save(new stateLogs(savedState, ipAddress, Constant.UPDATED, usernameFromToken));
		            log.save(new stateLogs(stateObj, ipAddress, Constant.UPDATED, usernameFromToken, clientHostname));

		        }
		        System.out.println("Client Hostnames: " + clientHostname);
		        System.out.println("Client IP Address: " + ipAddress);
		        System.out.println("Token: " + bearerToken);
		        System.out.println("Username from Token: " + usernameFromToken);

		        return Optional.of(savedState);
		    }catch(Exception e) {
			return Optional.empty();
		}
	}

	@Override
	public List<State> getAllState() {
		return stateRepo.findAll(Sort.by(Sort.Direction.DESC, "created"));
	}

	@Override
	public List<State> getAllActiveState() {
		return stateRepo.findAllActiveState();
	}

	@Override
	public List<State> getAllInactiveState() {
		return stateRepo.findAllInActiveState();
	}

	@Override
	public List<State> getAllStateByCountryId(Long id) {
		return stateRepo.findStateByCountryId(id);
	}

	@Override
	public State getStateById(Long id) {
		return stateRepo.findByStateId(id);
	}

	@Override
	public State getStateName(String stateName) {
		// TODO Auto-generated method stub
		return stateRepo.findByStateName(stateName);
	}

	@Override
	public boolean deleteByStateId(Long stateId) {
	    State cobj = stateRepo.findByStateId(stateId);
	    if (cobj == null)
	        return false;
	    try {
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";
	       
	        stateRepo.deleteByStateId(stateId);
	        if (usernameFromToken != null) {
	            log.save(new stateLogs(cobj, ipAddress, Constant.DELETED, usernameFromToken , clientHostname));
	            //log.save(new stateLogs(stateObj, ipAddress, Constant.CREATED, usernameFromToken, clientHostname));

	        }
	        System.out.println("Client Hostname: " + clientHostname);
	        System.out.println("Client IP Address: " + ipAddress);
	        System.out.println("Token: " + bearerToken);
	        System.out.println("Username from Token: " + usernameFromToken);

	        return true;
	    } catch (Exception e) {
	        return false;
	    }
	}


	@Override
	public List<State> getAllStateByCountryName(String countryName) {
		
		return stateRepo.findAllStateByCountryName(countryName);
	}

	
	
	
}
