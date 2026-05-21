package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.EsignAPISpecificationMaster;

public interface IEsignAPISpecificationMasterService {

	Optional<EsignAPISpecificationMaster> addEsignAPISpecificationMaster(EsignAPISpecificationMaster Obj);
	Optional<EsignAPISpecificationMaster> updateEsignAPISpecificationMaster(EsignAPISpecificationMaster Obj);
	List<EsignAPISpecificationMaster> getAllEsignAPISpecificationMaster();
	List<EsignAPISpecificationMaster> getAllActiveEsignAPISpecificationMaster();
	List<EsignAPISpecificationMaster> getAllInactiveEsignAPISpecificationMaster();
	EsignAPISpecificationMaster getEsignAPISpecificationMasterById(Long id);
	EsignAPISpecificationMaster getByEsignAPISpecification(String apiSpecification);
	
}
