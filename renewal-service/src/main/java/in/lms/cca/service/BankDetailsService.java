package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.BankDetails;

public interface BankDetailsService {

	Optional<BankDetails> addBankDetails(BankDetails bankDetails);

	BankDetails findIntentAppById(Long intentAppId);

	Optional<BankDetails> updateBankDetails(BankDetails bankDetails);

	BankDetails findIntentAppWithStatusById(Long intentAppId, String string);

	List<BankDetails> findIntentWithoutAppById(Long intentAppId);



}
