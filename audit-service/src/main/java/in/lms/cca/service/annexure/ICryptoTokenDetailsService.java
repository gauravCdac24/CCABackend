
package in.lms.cca.service.annexure;

import java.util.List;

import in.lms.cca.entity.annexure.CryptoTokenDetails;

public interface ICryptoTokenDetailsService {

	CryptoTokenDetails addCryptoTokenDetails(CryptoTokenDetails obj);

	CryptoTokenDetails getByCryptoTokenDetailsId(Long id);

	List<CryptoTokenDetails> getByCryptoTokenMainId(Long id);
	
}