package in.lms.cca.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.lms.cca.entity.logs.AuthLogs;

public interface AuthLogsRepository extends JpaRepository<AuthLogs, Long>{

}
