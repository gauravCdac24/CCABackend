package in.lms.cca.config;

import javax.sql.DataSource;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

/**
 * Menu Management stores submenu pages in the database, not in source.
 * These three admin pages were created from that screen, so a database on
 * another machine does not have them until this runs at startup.
 */
@Component
public class AdminTemplateMenuSeeder implements ApplicationRunner {

	private final DataSource dataSource;

	public AdminTemplateMenuSeeder(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void run(ApplicationArguments args) {
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(new ClassPathResource("db/admin_template_submenu.sql"));
		populator.setContinueOnError(false);
		populator.setSeparator(";");
		try {
			DatabasePopulatorUtils.execute(populator, dataSource);
			System.out.println("Admin template submenu pages are present in menu management.");
		} catch (Exception ex) {
			System.out.println("Could not restore admin template submenu pages: " + ex.getMessage());
		}
	}
}
