package in.lms.cca;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class EurekaServerApplicationTests {

	@Autowired
    private ApplicationContext applicationContext;
	
	@Test
	void contextLoads() {
		
		 // Check that the application context is not null
        assertNotNull(applicationContext, "The application context should have loaded.");

		
	}

}
