package in.lms.cca.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.AppLocation;

public interface AppLocationRepository extends JpaRepository<AppLocation, Long>{

	@Query("SELECT d FROM AppLocation d WHERE d.intentAppId.intentAppId = :intentAppId AND d.status = :status AND d.created IN " +
		       "(SELECT MAX(d2.created) FROM AppLocation d2 WHERE d2.intentAppId.intentAppId = :intentAppId AND d2.status = :status GROUP BY d2.locationName) " +
		       "ORDER BY d.created DESC")
		List<AppLocation> findIntentAppById(@Param("intentAppId") Long intentAppId, @Param("status") String status);
	@Query("SELECT d FROM AppLocation d WHERE d.addressId = :addressId")
	Optional<AppLocation> findById(@Param("addressId")Long addressId);

	@Query("SELECT d FROM AppLocation d WHERE d.addressId = :offApp")
	Optional<AppLocation> findByEncryptedAddressId(@Param("offApp")String offApp);

	@Query("SELECT d FROM AppLocation d WHERE d.intentAppId.intentAppId = :intentAppId")
	List<AppLocation> findIntentWithoutStatusAppById(@Param("intentAppId")Long intentAppId);

}
