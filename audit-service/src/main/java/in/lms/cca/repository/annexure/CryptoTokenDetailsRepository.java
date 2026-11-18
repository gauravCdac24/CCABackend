
package in.lms.cca.repository.annexure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import in.lms.cca.entity.annexure.CryptoTokenDetails;

public interface CryptoTokenDetailsRepository extends JpaRepository<CryptoTokenDetails, Long>{

	@Query("FROM CryptoTokenDetails a WHERE a.cryptoTokDetailsId = :id")
	CryptoTokenDetails getByCryptoTokenDetailsId(Long id);

	@Query("FROM CryptoTokenDetails a WHERE a.cryptoTokMainId.cryptoTokMainId = :cryptoTokMainId ORDER BY cryptoTokDetailsId")
	List<CryptoTokenDetails> getByCryptoTokenMainId(Long cryptoTokMainId);
	
}