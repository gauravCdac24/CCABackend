package in.lms.cca.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.lms.cca.entity.ApplicationRejectionEntity;

public interface ApplicationRejectionRepository extends JpaRepository<ApplicationRejectionEntity, Long> {

}
