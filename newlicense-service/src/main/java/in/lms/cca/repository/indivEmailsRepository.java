package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.IndivEmails;

public interface indivEmailsRepository extends JpaRepository<IndivEmails, Long> {

	@Query("SELECT d FROM IndivEmails d WHERE d.intentAppId.intentAppId = :intentAppId")
	List<IndivEmails> findByindivApplicationId(@Param("intentAppId")Long intentAppId);

}
