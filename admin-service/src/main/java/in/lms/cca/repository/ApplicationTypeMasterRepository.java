package in.lms.cca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.ApplicationTypeMaster;



public interface ApplicationTypeMasterRepository extends JpaRepository<ApplicationTypeMaster, Long>{

	@Query("FROM ApplicationTypeMaster a WHERE a.appTypeMasterId=:appTypeMasterId")
	ApplicationTypeMaster findByApplicationTypeId(@Param("appTypeMasterId")Long appTypeMasterId);

	@Query("SELECT d FROM ApplicationTypeMaster d WHERE d.appType = :appType")
	ApplicationTypeMaster findApplicationTypeMasterByName(@Param("appType")String appType);

	
}
