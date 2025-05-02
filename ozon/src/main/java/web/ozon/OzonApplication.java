package web.ozon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "lib.entity.dto.repository")
@EntityScan(basePackages = "lib.entity.dto.entity")
@ComponentScan(basePackages = "lib")
public class OzonApplication {

	public static void main(String[] args) {
		SpringApplication.run(OzonApplication.class, args);
	}

}
