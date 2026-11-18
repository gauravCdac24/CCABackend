package in.lms.cca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.ServiceMaster;



public interface ServiceMasterRepository extends JpaRepository<ServiceMaster, Long>{

	@Query("FROM ServiceMaster a WHERE a.serviceId=:serviceId")
	ServiceMaster findByServiceMasterId(@Param("serviceId")Long serviceId);

	@Query("SELECT d FROM ServiceMaster d WHERE d.serviceTitle = :serviceName")
	ServiceMaster findServiceMasterByName(String serviceName);

}
