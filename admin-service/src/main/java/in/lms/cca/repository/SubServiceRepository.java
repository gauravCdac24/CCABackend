package in.lms.cca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.SubServiceMasterEntity;

public interface SubServiceRepository extends JpaRepository<SubServiceMasterEntity, Long>{
	@Query("FROM SubServiceMasterEntity a WHERE a.subServiceId=:id")
	SubServiceMasterEntity findBySubServiceMasterId(@Param("id")Long id);

	@Query("SELECT d FROM SubServiceMasterEntity d WHERE d.subServiceName = :subServiceName")
	SubServiceMasterEntity findSubServiceMasterByName(@Param("subServiceName")String subServiceName);


}
