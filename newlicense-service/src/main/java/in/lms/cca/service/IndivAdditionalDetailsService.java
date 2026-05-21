package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.IndivAdditionalDetails;

public interface IndivAdditionalDetailsService {

	
	Optional<IndivAdditionalDetails> addIndivAdditionalDetails(IndivAdditionalDetails indivAdditionalDetails);

	IndivAdditionalDetails findIntentAppById(Long intentAppId);

	Optional<IndivAdditionalDetails> updateIndivAdditionalDetails(IndivAdditionalDetails indivAdditionalDetails);

	List<IndivAdditionalDetails> findIntentWithoutStatusAppById(Long intentAppId);
}
