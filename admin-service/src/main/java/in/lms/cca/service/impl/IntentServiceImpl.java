package in.lms.cca.service.impl;

import java.net.InetAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import in.lms.cca.dto.IntentDTO;
import in.lms.cca.dto.LoginDTO;
import in.lms.cca.dto.UserLoginDTO;
import in.lms.cca.entity.Address;
import in.lms.cca.entity.City;
import in.lms.cca.entity.Country;
import in.lms.cca.entity.Intent;
import in.lms.cca.entity.IntentUniqueCode;
import in.lms.cca.entity.State;
import in.lms.cca.entity.logs.IntentLogs;
import in.lms.cca.repository.IntentRepository;
import in.lms.cca.repository.logs.IntentRepositoryLogs;
import in.lms.cca.service.IAddressService;
import in.lms.cca.service.IAddressTypeService;
import in.lms.cca.service.ICityService;
import in.lms.cca.service.ICountryService;
import in.lms.cca.service.IIntentService;
import in.lms.cca.service.IIntentUniqueCodeService;
import in.lms.cca.service.IStateService;
import in.lms.cca.service.IUserLoginService;
import in.lms.cca.util.global.Constant;
import in.lms.cca.util.global.EncryptionUtil;
import jakarta.servlet.http.HttpServletRequest;

@Service
@Transactional
public class IntentServiceImpl implements IIntentService{

	@Autowired
	private IntentRepository intentRepo;
	
	@Value("${apigateway.service.url}")
	private String apigatewayServiceUrl;
	
	 @Autowired
	 private RestTemplate restTemplate;
	
	@Autowired
	private HttpServletRequest request;
	
	private final Set<String> clientHostnames = new HashSet<>();
	
	@Autowired
	private IntentRepositoryLogs log;

	@Autowired
	private IStateService stateServ;

	@Autowired
	private ICountryService countryServ;

	@Autowired
	private ICityService cityServ;

	@Autowired
	private IAddressTypeService addressTypeServ;

	@Autowired
	private IAddressService addressServ;

	@Autowired
	private IIntentUniqueCodeService uniqueCodeServ;

	@Autowired
	private IUserLoginService userLoginServ;
	
	@Override
	public Optional<Intent> addIntent(Intent intentObj) {
		
		if(intentObj == null)
			return Optional.empty();
		
		Intent savedIntent = intentRepo.save(intentObj);
		return Optional.of(savedIntent);
		
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Intent registerIntent(IntentDTO intentObj) throws Exception {

		// 1. Look up the unique code
		IntentUniqueCode u = uniqueCodeServ.getByActiveUniqueCode(Long.parseLong(intentObj.getUniqueCode()));
		if (u == null)
			throw new IllegalArgumentException("Please enter a valid code.");

		// 1b. Check if an intent already exists for this unique code (handles stale data from previous failed attempts)
		Intent existingIntent = intentRepo.findByUniqueCodeId(u.getUniqueCodeId());
		if (existingIntent != null)
			throw new IllegalArgumentException("An intent has already been registered with this unique code. Please contact support.");

		// 2. Build and save the Address
		Address newAddress = new Address();

		String countryId = EncryptionUtil.decrypt(intentObj.getCountry());
		Country country = countryServ.getCountryById(Long.parseLong(countryId));
		newAddress.setCountryId(country);

		String stateId = EncryptionUtil.decrypt(intentObj.getState());
		State state = stateServ.getStateById(Long.parseLong(stateId));
		newAddress.setStateId(state);

		String cityId = EncryptionUtil.decrypt(intentObj.getCity());
		City city = cityServ.getCityById(Long.parseLong(cityId));
		newAddress.setCityId(city);

		newAddress.setAddressTypeId(addressTypeServ.getAddressTypeByName("Permanent"));
		newAddress.setBlockNo(intentObj.getBlockNo());
		newAddress.setSubDivision(intentObj.getSubDivision());
		newAddress.setPostOffice(intentObj.getPostOffice());
		newAddress.setVillage(intentObj.getVillage());
		newAddress.setPincode(intentObj.getPin());
		newAddress.setStatus("Active");
		Optional<Address> addressObj = addressServ.addAddress(newAddress);

		if (addressObj.isEmpty())
			throw new RuntimeException("Failed to save address.");

		// 3. Build and save the Intent
		Intent newIntent = new Intent();
		newIntent.setAddressId(addressObj.get());
		newIntent.setSalutation(intentObj.getSalutation());
		newIntent.setFirstName(intentObj.getFirstName());
		newIntent.setMiddleName(intentObj.getMiddleName());
		newIntent.setLastName(intentObj.getLastName());
		newIntent.setUniqueCodeId(u);
		newIntent.setStatus("Active");

		Intent savedIntent = intentRepo.save(newIntent);

		// 4. Create login account via auth-service (if this fails, everything above rolls back)
		UserLoginDTO userLoginDTO = new UserLoginDTO();
		userLoginDTO.setEmailId(u.getEmailId());
		userLoginDTO.setMobile(u.getMobileNo());
		userLoginDTO.setSalutation(savedIntent.getSalutation());
		userLoginDTO.setFirstName(savedIntent.getFirstName());
		userLoginDTO.setMiddleName(savedIntent.getMiddleName());
		userLoginDTO.setLastName(savedIntent.getLastName());
		userLoginDTO.setStatus("Active");
		userLoginDTO.setUserId(EncryptionUtil.encrypt(savedIntent.getIntentId().toString()));

		userLoginServ.addUser(userLoginDTO);

		// 5. Deactivate the unique code
		u.setStatus("Inactive");
		uniqueCodeServ.updateUniqueCode(u);

		return savedIntent;
	}

	@Override
	public Optional<Intent> updateIntent(Intent intentObj) {
		if(intentObj == null)
			return Optional.empty();
		
		if(intentObj.getIntentId() == null)
			return Optional.empty();
		
		try { 
	       
	        Intent savedIntent = intentRepo.save(intentObj);
	       
	        return Optional.of(savedIntent);
	    
	}catch(Exception e) {
			return Optional.empty();
		}
	}

	@Override
	public List<Intent> getAllIntent() {
		return intentRepo.findAll(Sort.by(Sort.Direction.DESC, "created"));
	}

	@Override
	public List<Intent> getAllActiveIntent() {
	
		return intentRepo.findAllActiveIntent();
	}

	@Override
	public List<Intent> getAllInactiveIntent() {
		
		return intentRepo.findAllInActiveIntent();
	}

	@Override
	public Intent getIntentById(Long id) {
	
		return intentRepo.findByIntentId(id);
	}

	@Override
	public Intent getIntentByAddressId(Long id) {
		
		return intentRepo.findIntentByAddressId(id);
	}

	@Override
	public boolean deleteByIntentId(long intentId) {
	    Intent intent = intentRepo.findById(intentId).orElse(null);
	    
	    if (intent == null) {
	        return false;  
	    }

	    try {
	       
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";

	        intentRepo.deletebyIntentId(intentId);
	        if (usernameFromToken != null) {
	            log.save(new IntentLogs(intent, ipAddress, Constant.DELETED, usernameFromToken));
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
	public LoginDTO getIntentByUserName(String userName) {
		String uriTemplate = apigatewayServiceUrl + "/auth-service/get-by-username/{userName}";
  	  
   	 Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("userName", userName);
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uriTemplate);
		URI uri = builder.buildAndExpand(uriVariables).toUri();
		 
		return restTemplate.getForObject(uri, LoginDTO.class);
	}


	
	@Override
	public Intent getIntentByUserId(Long userId) {
		// TODO Auto-generated method stub
		return intentRepo.getByuserId(userId);
	}

	

}

