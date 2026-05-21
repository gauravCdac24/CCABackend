package in.lms.cca.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import in.lms.cca.entity.EsignAPIVersion;
import in.lms.cca.repository.EsignAPIVersionRepository;
import in.lms.cca.service.IEsignAPIVersionService;

@Service
@Repository
public class EsignAPIVersionServiceImpl implements IEsignAPIVersionService{

	@Autowired
	private EsignAPIVersionRepository repo;
	
	@Override
	public Optional<EsignAPIVersion> addEsignAPIVersion(EsignAPIVersion obj) {
		if (obj == null)
	        return Optional.empty();

	    try {
	       
	    	EsignAPIVersion savedObj = repo.save(obj);
	       
	        return Optional.of(savedObj);
	    } catch (Exception e) {
	        return Optional.empty();
	    }
	}

	@Override
	public List<EsignAPIVersion> getEsignAPIVersionByAppIdAndStatus(Long esignLicenseeAppId, String status) {
		
		return repo.findEsignAPIVersionByAppIdAndStatus(esignLicenseeAppId, status);
	}

	@Override
	public void inactiveAllEsignApiVerByAppId(Long id) {
		
		repo.inactiveAllEsignApiVerByAppId(id);
		
	}
	
	
}
