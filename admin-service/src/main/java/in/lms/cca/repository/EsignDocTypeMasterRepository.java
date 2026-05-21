package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.EsignDocTypeMaster;

public interface EsignDocTypeMasterRepository extends JpaRepository<EsignDocTypeMaster, Long>{

	@Query("FROM EsignDocTypeMaster WHERE status = 'Active' ORDER BY created DESC")
	List<EsignDocTypeMaster> findAllActiveEsignDocTypeMaster();
	
	@Query("FROM EsignDocTypeMaster WHERE status = 'Inactive' ORDER BY created DESC")
	List<EsignDocTypeMaster> findAllInactiveEsignDocTypeMaster();

	@Query("FROM EsignDocTypeMaster a WHERE a.esignDocTypeId=:esignDocTypeId")
	EsignDocTypeMaster findEsignDocTypeMasterById(Long esignDocTypeId);

	@Query("FROM EsignDocTypeMaster a WHERE a.docType=:doctype")
	EsignDocTypeMaster findByEsignDocType(String doctype);

}
