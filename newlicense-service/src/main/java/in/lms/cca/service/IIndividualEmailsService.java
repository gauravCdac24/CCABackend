package in.lms.cca.service;

import java.util.List;
import java.util.Optional;

import in.lms.cca.entity.IndivEmails;

public interface IIndividualEmailsService {

	Optional<IndivEmails> addIndivEmails(IndivEmails indivEmail);

	List<IndivEmails> findByindivApplicationId(Long indivApplicationId);

	Optional<IndivEmails> updateIndivEmails(IndivEmails indivEmail);

}
