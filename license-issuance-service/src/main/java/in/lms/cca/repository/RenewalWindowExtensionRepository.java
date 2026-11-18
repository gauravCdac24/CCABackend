package in.lms.cca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.lms.cca.entity.RenewalWindowExtension;

public interface RenewalWindowExtensionRepository extends JpaRepository<RenewalWindowExtension, Long> {

	List<RenewalWindowExtension> findByUserNameAndStatusOrderByExtensionUntilDesc(String userName, String status);

	List<RenewalWindowExtension> findAllByOrderByCreatedDesc();
}
