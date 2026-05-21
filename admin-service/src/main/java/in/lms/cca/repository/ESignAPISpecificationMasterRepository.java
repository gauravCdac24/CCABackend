package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.EsignAPISpecificationMaster;

public interface ESignAPISpecificationMasterRepository extends JpaRepository<EsignAPISpecificationMaster, Long>{

	@Query("FROM EsignAPISpecificationMaster WHERE status = 'Active' ORDER BY created DESC")
	List<EsignAPISpecificationMaster> findAllActiveEsignAPISpecificationMaster();
	
	@Query("FROM EsignAPISpecificationMaster WHERE status = 'Inactive' ORDER BY created DESC")
	List<EsignAPISpecificationMaster> findAllInActiveEsignAPISpecificationMaster();
	
	@Query("FROM EsignAPISpecificationMaster a WHERE a.esignApiSpecId=:esignApiSpecId")
	EsignAPISpecificationMaster findByEsignAPISpecificationMasterId (@Param("esignApiSpecId")Long esignApiSpecId);
	
	@Query("FROM EsignAPISpecificationMaster a WHERE a.apiSpecification=:apiSpecification")
	EsignAPISpecificationMaster findByEsignAPISpecification (@Param("apiSpecification")String apiSpecification);
	

}
