package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.EsignAPIVersion;

public interface IEsignAPIVersionService{
	
	Optional<EsignAPIVersion> addEsignAPIVersion(EsignAPIVersion obj);

	List<EsignAPIVersion> getEsignAPIVersionByAppIdAndStatus(Long esignLicenseeAppId, String status);
	
	void inactiveAllEsignApiVerByAppId(Long id);
	
}
