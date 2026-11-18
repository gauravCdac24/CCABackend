package in.lms.cca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.lms.cca.entity.ApplicantShortcomingsReportEntity;

public interface ApplicantShortcomingsReportRepository extends JpaRepository<ApplicantShortcomingsReportEntity, Long> {

	@Query("SELECT d FROM ApplicantShortcomingsReportEntity d WHERE d.shortcomingId.shortcomingId = :shortcomingId AND d.status='Active'")
	ApplicantShortcomingsReportEntity getByShortIdId(@Param("shortcomingId")Long shortcomingId);

}
