package in.lms.cca.service.impl;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.lms.cca.dto.IndivAddressDTO;
import in.lms.cca.entity.Address;
import in.lms.cca.entity.City;
import in.lms.cca.entity.logs.AddressLogs;
import in.lms.cca.entity.logs.CityLogs;
import in.lms.cca.repository.AddressRepository;
import in.lms.cca.repository.logs.AddressRepositoryLogs;
import in.lms.cca.repository.logs.CityRepositoryLogs;
import in.lms.cca.service.IAddressService;
import in.lms.cca.util.global.Constant;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AddressServiceImpl implements IAddressService{

	@Autowired
	private AddressRepository addressRepo;
	
	@Autowired
	private HttpServletRequest request;
	
	 private final Set<String> clientHostnames = new HashSet<>();
	 
	 @Autowired
	 private AddressRepositoryLogs log;

	
	@Override
	public Optional<Address> addAddress(Address address) {

		if(address == null) {
		
			return Optional.empty();
		}
			
		try { 
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";
	        Address savedAddress = addressRepo.save(address);
	        if (usernameFromToken != null) {
	         //   log.save(new AddressLogs(address, ipAddress, Constant.CREATED, usernameFromToken));
	        }
	        System.out.println("Client Hostnames: " + clientHostname);
	        System.out.println("Client IP Address: " + ipAddress);
	        System.out.println("Token: " + bearerToken);
	        System.out.println("Username from Token: " + usernameFromToken);

	        return Optional.of(savedAddress);
	    
	}catch(Exception e) {
				e.printStackTrace();
				return Optional.empty();
			}		
		
	}

	@Override
	public Optional<Address> updateAddress(Address address) {
		
		if(address == null) {
			return Optional.empty();
		}
		
		if(address.getAddressId() == null) {
			return Optional.empty();
		}
		
		try { 
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";
	        Address savedAddress = addressRepo.save(address);
	        if (usernameFromToken != null) {
	            log.save(new AddressLogs(address, ipAddress, Constant.UPDATED, usernameFromToken));
	        }
	        System.out.println("Client Hostnames: " + clientHostname);
	        System.out.println("Client IP Address: " + ipAddress);
	        System.out.println("Token: " + bearerToken);
	        System.out.println("Username from Token: " + usernameFromToken);

	        return Optional.of(savedAddress);
	    
	}catch(Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
		
	}

	
	@Override
	public boolean deleteAddressById(Long id) {

		
		Address address = getAddressById(id);
		
		if(address == null) {
			return false;
		}
		try {
			
			addressRepo.deleteByAddressId(id);
			return true;
		}catch(Exception e) {
			return false;
		}
		
	}

	@Override
	public Address getAddressById(Long id) {
		
		return addressRepo.findByAddressId(id);
		
	}

	@Override
	public List<Address> getAllAddress() {

		return addressRepo.findAll(Sort.by(Sort.Direction.DESC, "created"));
		
	}

	@Override
	public Address getAddressByIdAndAddressTypeId(Long addressId, Integer addressTypeId) {

		return addressRepo.findByAddressIdAndAddressTypeId(addressId, addressTypeId);
		
	}

	@Override
	public Address getAddressByIdAndAddressTypeName(Long addressId, String addressTypeName) {

		return addressRepo.findByAddressIdAndAddressTypeName(addressId, addressTypeName);
	}

	@Override
	public List<Address> getAddressByCreatedBy(String createdBy) {
		
		return addressRepo.findByCreatedBy(createdBy);
		
	}

	@Override
	public Address getAddressByIds(Long addressId) {
	    Address addressObj = addressRepo.findByAddressId(addressId);

	    if (addressObj == null)
	        return null;

	    try {
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";

	        if (usernameFromToken != null) {
	            log.save(new AddressLogs(addressObj, ipAddress, Constant.DELETED, usernameFromToken));
	        }

	        System.out.println("Client Hostname: " + clientHostname);
	        System.out.println("Client IP Address: " + ipAddress);
	        System.out.println("Token: " + bearerToken);
	        System.out.println("Username from Token: " + usernameFromToken);

	        return addressObj;
	    } catch (Exception e) {
	        return null;
	    }
	}

	
	
	@Override
	public Optional<Address> addAddres(Address addressObj) {
		if(addressObj == null) {
			
			return Optional.empty();
		}
			
		try { 
	       
	        Address savedAddress = addressRepo.save(addressObj);

	        return Optional.of(savedAddress);
	    
	}catch(Exception e) {
				e.printStackTrace();
				return Optional.empty();
			}	
}

	@Override
	public Optional<Address> updateAddres(Address address) {
if(address == null) {
			
			return Optional.empty();
		}
if(address.getAddressId()==null) {
	return Optional.empty();
}
			
		try { 
	       
	        Address savedAddress = addressRepo.save(address);

	        return Optional.of(savedAddress);
	    
	}catch(Exception e) {
				e.printStackTrace();
				return Optional.empty();
			}	
	}
}