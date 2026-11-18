package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.AgreementTemplate;
import jakarta.transaction.Transactional;

public interface AgreementTemplateRepository extends JpaRepository<AgreementTemplate, Long> {

	@Query("FROM AgreementTemplate a WHERE a.templateId=:templateId")
	AgreementTemplate findByTemplateId(@Param("templateId") Long templateId);

	@Query("FROM AgreementTemplate WHERE status = 'Active' ORDER BY created DESC")
	List<AgreementTemplate> findAllActiveTemplates();

	@Query("FROM AgreementTemplate WHERE status = 'Inactive' ORDER BY created DESC")
	List<AgreementTemplate> findAllInactiveTemplates();

	@Modifying
	@Transactional
	@Query("DELETE FROM AgreementTemplate c WHERE c.templateId=:templateId")
	void deleteByTemplateId(Long templateId);
}
