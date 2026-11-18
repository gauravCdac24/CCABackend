package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.lms.cca.entity.LicenseReview;

public interface LicenseReviewRepository extends JpaRepository<LicenseReview, Long> {

	List<LicenseReview> findByIntentAppIdAndStatusNotOrderByCreatedDesc(String intentAppId, String status);
}
