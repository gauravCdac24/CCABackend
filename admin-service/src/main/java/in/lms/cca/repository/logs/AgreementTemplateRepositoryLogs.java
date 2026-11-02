package in.lms.cca.repository.logs;

import org.springframework.data.jpa.repository.JpaRepository;

import in.lms.cca.entity.logs.AgreementTemplateLog;

public interface AgreementTemplateRepositoryLogs extends JpaRepository<AgreementTemplateLog, Long> {

}
