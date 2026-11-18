package in.lms.cca.repository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AuditAgencyDirectoryRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Optional<Long> findActiveIdByAgencyName(String agencyName) {
		if (agencyName == null || agencyName.isBlank()) {
			return Optional.empty();
		}
		try {
			Long id = jdbcTemplate.query(
					"SELECT audit_agency_id FROM admin_user_management.audit_agency "
							+ "WHERE LOWER(agency_name) = LOWER(?) AND status = 'Active' LIMIT 1",
					rs -> rs.next() ? rs.getLong(1) : null,
					agencyName.trim());
			return Optional.ofNullable(id);
		} catch (Exception ex) {
			return Optional.empty();
		}
	}

	public Optional<Long> findActiveIdByCreatedBy(String createdBy) {
		if (createdBy == null || createdBy.isBlank()) {
			return Optional.empty();
		}
		try {
			Long id = jdbcTemplate.query(
					"SELECT audit_agency_id FROM admin_user_management.audit_agency "
							+ "WHERE created_by = ? AND status = 'Active' LIMIT 1",
					rs -> rs.next() ? rs.getLong(1) : null,
					createdBy);
			return Optional.ofNullable(id);
		} catch (Exception ex) {
			return Optional.empty();
		}
	}
}
