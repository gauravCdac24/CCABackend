package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.EsignDocTypeMaster;

public interface IEsignDocTypeMasterService {

	Optional<EsignDocTypeMaster> addEsignDocTypeMaster(EsignDocTypeMaster Obj);
	Optional<EsignDocTypeMaster> updateEsignDocTypeMaster(EsignDocTypeMaster Obj);
	List<EsignDocTypeMaster> getAllEsignDocTypeMaster();
	List<EsignDocTypeMaster> getAllActiveEsignDocTypeMaster();
	List<EsignDocTypeMaster> getAllInactiveEsignDocTypeMaster();
	EsignDocTypeMaster getEsignDocTypeMasterById(Long id);
	EsignDocTypeMaster getByEsignDocType(String doctype);
	
}
