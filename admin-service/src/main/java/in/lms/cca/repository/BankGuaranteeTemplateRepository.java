package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.BankGuaranteeTemplate;
import jakarta.transaction.Transactional;

public interface BankGuaranteeTemplateRepository extends JpaRepository<BankGuaranteeTemplate, Long> {

	@Query("FROM BankGuaranteeTemplate a WHERE a.templateId=:templateId")
	BankGuaranteeTemplate findByTemplateId(@Param("templateId") Long templateId);

	@Query("FROM BankGuaranteeTemplate WHERE status = 'Active' ORDER BY created DESC")
	List<BankGuaranteeTemplate> findAllActiveTemplates();

	@Query("FROM BankGuaranteeTemplate WHERE status = 'Inactive' ORDER BY created DESC")
	List<BankGuaranteeTemplate> findAllInactiveTemplates();

	@Modifying
	@Transactional
	@Query("DELETE FROM BankGuaranteeTemplate c WHERE c.templateId=:templateId")
	void deleteByTemplateId(Long templateId);
}
