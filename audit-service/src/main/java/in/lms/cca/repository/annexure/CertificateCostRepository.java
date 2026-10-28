
package in.lms.cca.repository.annexure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.annexure.CertificateCost;

public interface CertificateCostRepository extends JpaRepository<CertificateCost, Long>{

	@Query("FROM CertificateCost a WHERE a.annexureMainId.annexureMainId = :id")
	CertificateCost findByAnnexureId(Long id);
	
	@Query("FROM CertificateCost a WHERE a.certCostId = :id")
	CertificateCost findByCertificateCostId(Long id);
	
	
}















