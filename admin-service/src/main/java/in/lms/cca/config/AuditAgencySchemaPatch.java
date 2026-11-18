package in.lms.cca.config;

import javax.sql.DataSource;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.Statement;

/**
 * Hibernate schema update does not widen existing varchar columns.
 * The edit form stores "telephone" (9) and agency names up to 100 characters.
 * The log and agency columns were shorter, so the update insert failed.
 */
@Component
public class AuditAgencySchemaPatch implements ApplicationRunner {

	private final DataSource dataSource;

	public AuditAgencySchemaPatch(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void run(ApplicationArguments args) {
		String[] statements = {
				"ALTER TABLE cca_logs_management.address_logs ADD COLUMN IF NOT EXISTS client_host_name varchar(100)",
				"ALTER TABLE cca_logs_management.audit_agency_logs ADD COLUMN IF NOT EXISTS client_host_name varchar(100)",
				"ALTER TABLE cca_logs_management.audit_agency_mobile_logs ALTER COLUMN contact_type TYPE varchar(16)",
				"ALTER TABLE admin_user_management.audit_agency_mobile ALTER COLUMN contact_type TYPE varchar(16)",
				"ALTER TABLE admin_user_management.audit_agency ALTER COLUMN agency_name TYPE varchar(100)",
				"ALTER TABLE cca_logs_management.audit_agency_logs ALTER COLUMN agency_name TYPE varchar(100)"
		};
		try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
			for (String sql : statements) {
				try {
					statement.execute(sql);
				} catch (Exception ex) {
					System.out.println("Audit agency schema patch skipped: " + ex.getMessage());
				}
			}
		} catch (Exception ex) {
			System.out.println("Audit agency schema patch did not run: " + ex.getMessage());
		}
	}
}
