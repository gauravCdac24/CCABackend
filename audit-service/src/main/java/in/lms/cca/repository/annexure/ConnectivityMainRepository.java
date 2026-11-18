
package in.lms.cca.repository.annexure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.annexure.ConnectivityMain;

public interface ConnectivityMainRepository extends JpaRepository<ConnectivityMain, Long>{

	@Query("FROM ConnectivityMain a WHERE a.annexureMainId.annexureMainId = :annexureMainId")
	ConnectivityMain findByAnnexureId(Long annexureMainId);
	
}