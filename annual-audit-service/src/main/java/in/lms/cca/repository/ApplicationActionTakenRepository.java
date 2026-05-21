package in.lms.cca.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.lms.cca.entity.ApplicantActionTakenEntity;

public interface ApplicationActionTakenRepository extends JpaRepository<ApplicantActionTakenEntity, Long> {
	

}
