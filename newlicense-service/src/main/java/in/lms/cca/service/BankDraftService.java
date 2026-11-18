package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.BankDraft;

public interface BankDraftService {

	Optional<BankDraft> addBankDraft(BankDraft bankDraft);

	BankDraft findByindivApplicationId(Long intentAppId);

	Optional<BankDraft> updateBankDraft(BankDraft bankDraft);

	BankDraft findIntentAppWithStatusById(Long intentAppId, String status);

	BankDraft findByindivWithoutApplicationId(Long intentAppId);

	List<BankDraft> findIntentWithoutAppById(Long intentAppId);

}
