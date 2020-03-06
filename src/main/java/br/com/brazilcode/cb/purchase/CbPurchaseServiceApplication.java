package br.com.brazilcode.cb.purchase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.WebApplicationInitializer;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "br.com.brazilcode.cb.libs.repository")
@EntityScan(basePackages = "br.com.brazilcode.cb.libs.model")
public class CbPurchaseServiceApplication extends SpringBootServletInitializer implements WebApplicationInitializer {

	public static void main(String[] args) {
		SpringApplication.run(CbPurchaseServiceApplication.class, args);
	}

}
