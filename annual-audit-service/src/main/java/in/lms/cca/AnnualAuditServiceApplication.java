package in.lms.cca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AnnualAuditServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnnualAuditServiceApplication.class, args);
	}

}
