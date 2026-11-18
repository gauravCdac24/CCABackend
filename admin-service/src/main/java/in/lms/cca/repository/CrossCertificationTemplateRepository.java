package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.CrossCertificationTemplate;
import jakarta.transaction.Transactional;

public interface CrossCertificationTemplateRepository extends JpaRepository<CrossCertificationTemplate, Long> {

	@Query("FROM CrossCertificationTemplate a WHERE a.templateId=:templateId")
	CrossCertificationTemplate findByTemplateId(@Param("templateId") Long templateId);

	@Query("FROM CrossCertificationTemplate WHERE status = 'Active' ORDER BY created DESC")
	List<CrossCertificationTemplate> findAllActiveTemplates();

	@Query("FROM CrossCertificationTemplate WHERE status = 'Inactive' ORDER BY created DESC")
	List<CrossCertificationTemplate> findAllInactiveTemplates();

	@Modifying
	@Transactional
	@Query("DELETE FROM CrossCertificationTemplate c WHERE c.templateId=:templateId")
	void deleteByTemplateId(Long templateId);
}
