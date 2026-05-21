package in.lms.cca.service.impl;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.CPSDocument;
import in.lms.cca.entity.logs.CPSDocumentLog;
import in.lms.cca.repository.CPSDocumentRepository;
import in.lms.cca.repository.logs.CPSDocumentRepositoryLogs;
import in.lms.cca.service.ICPSDocumentService;
import in.lms.cca.util.global.Constant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class CPSDocumentImpl implements ICPSDocumentService{

	@Autowired
	private CPSDocumentRepository cpsRepo;
	
	@Autowired
	private HttpServletRequest request;
	
	private final Set<String> clientHostnames = new HashSet<>();
	
	@Autowired
	private CPSDocumentRepositoryLogs log;

	
	@Override
	public Optional<CPSDocument> addCPSDocument(CPSDocument cpsDocumentObj) {
		
		if(cpsDocumentObj == null)
			return Optional.empty();
		
		
		try { 
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";
	        CPSDocument savedCPSDocument = cpsRepo.save(cpsDocumentObj);
	        if (usernameFromToken != null) {
	            log.save(new CPSDocumentLog(savedCPSDocument, ipAddress, Constant.CREATED, usernameFromToken));
	        }
	        System.out.println("Client Hostnames: " + clientHostname);
	        System.out.println("Client IP Address: " + ipAddress);
	        System.out.println("Token: " + bearerToken);
	        System.out.println("Username from Token: " + usernameFromToken);

	        return Optional.of(savedCPSDocument);
	    
	}catch(Exception e) {
			return Optional.empty();
		}
		
	}

	@Override
	public Optional<CPSDocument> updateCPSDocument(CPSDocument cpsDocumentObj) {
		System.out.println(cpsDocumentObj.getCpsDocId());
		if(cpsDocumentObj == null)
			return Optional.empty();
		
		if(cpsDocumentObj.getCpsDocId() == null)
			return Optional.empty();
		

		try { 
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";
	        CPSDocument savedCPSDocument = cpsRepo.save(cpsDocumentObj);
	        if (usernameFromToken != null) {
	            log.save(new CPSDocumentLog(savedCPSDocument, ipAddress, Constant.UPDATED, usernameFromToken));
	        }
	        System.out.println("Client Hostnames: " + clientHostname);
	        System.out.println("Client IP Address: " + ipAddress);
	        System.out.println("Token: " + bearerToken);
	        System.out.println("Username from Token: " + usernameFromToken);

	        return Optional.of(savedCPSDocument);
	    
	}catch(Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	@Override
	public List<CPSDocument> getAllCPSDocument() {
		return cpsRepo.findAll(Sort.by(Sort.Direction.DESC, "created"));
	}

	@Override
	public List<CPSDocument> getAllActiveCPSDocument() {
		return cpsRepo.findAllActiveCPSDocument();
	}

	@Override
	public List<CPSDocument> getAllInactiveCPSDocument() {
		return cpsRepo.findAllInActiveCPSDocument();
	}

	@Override
	public CPSDocument getByCPSDocId(Long cpsDocId) {
		return cpsRepo.findByCPSDocId(cpsDocId);
	}

	@Override
	public boolean deleteBycpsDocId(Long cpsDocId) {
		
		CPSDocument cpsdoc = getByCPSDocId(cpsDocId);
		
		if(cpsdoc == null) {
			return false;
		}
		try { 
	        String ipAddress = request.getRemoteAddr();
	        InetAddress clientAddress = InetAddress.getByName(ipAddress);
	        String clientHostname = clientAddress.getHostName();
	        String bearerToken = request.getHeader("Authorization");
	        String usernameFromToken = "1";
	        cpsRepo.deleteBycpsDocId(cpsDocId);
	        if (usernameFromToken != null) {
	                log.save(new CPSDocumentLog(cpsdoc, ipAddress, Constant.DELETED, usernameFromToken));
	        }
	        System.out.println("Client Hostnames: " + clientHostname);
	        System.out.println("Client IP Address: " + ipAddress);
	        System.out.println("Token: " + bearerToken);
	        System.out.println("Username from Token: " + usernameFromToken);

	        return true;
	    
	}catch(Exception e) {
			return false;
		}
		
		
	}

	@Override
	public CPSDocument getCPSDocById(long cpsDocuId) {
		// TODO Auto-generated method stub
		return cpsRepo.findByCPSDocId(cpsDocuId);
	}

	@Override
	public Optional<CPSDocument> downloadfile(long cpsDocuId) {
		// TODO Auto-generated method stub
		return  cpsRepo.findById(cpsDocuId);
	}

}
