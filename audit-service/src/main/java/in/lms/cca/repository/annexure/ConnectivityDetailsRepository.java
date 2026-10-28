
package in.lms.cca.repository.annexure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.annexure.ConnectivityDetails;

public interface ConnectivityDetailsRepository extends JpaRepository<ConnectivityDetails, Long>{

	@Query("FROM ConnectivityDetails a WHERE a.connectivityDetailsId = :id")
	ConnectivityDetails getByConnectivityDetailsId(Long id);

	@Query("FROM ConnectivityDetails a WHERE a.connectivityMainId.connectivityMainId = :connectivityMainId ORDER BY connectivityDetailsId")
	List<ConnectivityDetails> getByConnectivityMainId(Long connectivityMainId);
	
}