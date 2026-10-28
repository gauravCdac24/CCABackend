
package in.lms.cca.repository.annexure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.lms.cca.entity.annexure.CryptoTokenMain;

public interface CryptoTokenMainRepository extends JpaRepository<CryptoTokenMain, Long>{

	@Query("FROM CryptoTokenMain a WHERE a.annexureMainId.annexureMainId = :annexureMainId")
	CryptoTokenMain findByAnnexureId(Long annexureMainId);
	
}


