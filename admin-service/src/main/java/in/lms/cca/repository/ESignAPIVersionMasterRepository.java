package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.ESignAPIVersionMaster;


public interface ESignAPIVersionMasterRepository extends JpaRepository<ESignAPIVersionMaster, Long>{

	@Query("FROM ESignAPIVersionMaster WHERE status = 'Active' ORDER BY created DESC")
	List<ESignAPIVersionMaster> findAllActiveESignAPIVersionMaster();

	@Query("FROM ESignAPIVersionMaster WHERE status = 'Inactive' ORDER BY created DESC")
	List<ESignAPIVersionMaster> findAllInactiveESignAPIVersionMaster();

	@Query("FROM ESignAPIVersionMaster a WHERE a.esignApiSpecId.esignApiSpecId=:esignApiSpecId ORDER BY created DESC")
	List<ESignAPIVersionMaster> findAllESignAPIVersionMasterByApiSpecId(Long esignApiSpecId);

	@Query("FROM ESignAPIVersionMaster a WHERE a.esignApiVerId=:id")
	ESignAPIVersionMaster findESignAPIVersionMasterById(Long id);

	@Query("FROM ESignAPIVersionMaster a WHERE a.esignApiSpecId.esignApiSpecId=:esignApiSpecId AND status='Active' ORDER BY created DESC")
	List<ESignAPIVersionMaster> findAllActiveESignAPIVersionMasterByApiSpecId(Long esignApiSpecId);

	@Query("FROM ESignAPIVersionMaster a WHERE a.esignApiSpecId.esignApiSpecId=:esignApiSpecId AND status='Inactive' ORDER BY created DESC")
	List<ESignAPIVersionMaster> findAllInactiveESignAPIVersionMasterByApiSpecId(Long esignApiSpecId);

	@Query("FROM ESignAPIVersionMaster a WHERE a.apiVersion=:apivername")
	ESignAPIVersionMaster findEsignAPIVersionByAPIVersionName(String apivername);

}
