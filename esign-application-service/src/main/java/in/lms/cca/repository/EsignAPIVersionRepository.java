package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.EsignAPIVersion;
import jakarta.transaction.Transactional;

public interface EsignAPIVersionRepository extends JpaRepository<EsignAPIVersion, Long>{

	@Query("FROM EsignAPIVersion a WHERE a.esignLicenseeAppId.esignLicenseeAppId = :esignLicenseeAppId AND a.status = :status ORDER BY created DESC")
	List<EsignAPIVersion> findEsignAPIVersionByAppIdAndStatus(Long esignLicenseeAppId, String status);

	@Modifying
	@Transactional
	@Query("UPDATE EsignAPIVersion a SET a.status='Inactive' WHERE a.esignLicenseeAppId.esignLicenseeAppId = :id")
	void inactiveAllEsignApiVerByAppId(Long id);

}
