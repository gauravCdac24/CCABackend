
package in.lms.cca.service.annexure;

import in.lms.cca.entity.annexure.CryptoTokenMain;

public interface ICryptoTokenMainService {


	CryptoTokenMain addCryptoTokenMain(CryptoTokenMain obj);

	CryptoTokenMain getByAnnexureId(Long annexureMainId);
}


