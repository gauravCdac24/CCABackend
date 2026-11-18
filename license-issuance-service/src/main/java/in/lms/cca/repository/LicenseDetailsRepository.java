package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.LicenseDetails;

public interface LicenseDetailsRepository extends JpaRepository<LicenseDetails, Long>{

	@Query("FROM LicenseDetails a WHERE a.licenseId=:id")
	LicenseDetails findByLicenseDetailsId(@Param("id")Long id);

	@Query("FROM LicenseDetails WHERE status='Active' order by created DESC")
	List<LicenseDetails> findAllActiveLicenseDetails();
	
	@Query("FROM LicenseDetails WHERE status='Inactive' order by created DESC")
	List<LicenseDetails> findAllInactiveLicenseDetails();

	@Query("FROM LicenseDetails WHERE status='Active' AND userName=:username order by created DESC")
	List<LicenseDetails> findActiveLicenseDetailsByUsername(String username);

	@Query("FROM LicenseDetails WHERE status='Inactive' AND userName=:username order by created DESC")
	List<LicenseDetails> findInactiveLicenseDetailsByUsername(String username);

	@Query("FROM LicenseDetails WHERE userName=:username order by created DESC")
	List<LicenseDetails> findAllLicenseDetailsByUsername(String username);
	
}
