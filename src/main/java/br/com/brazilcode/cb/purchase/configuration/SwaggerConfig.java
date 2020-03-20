package br.com.brazilcode.cb.purchase.configuration;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Classe responsável por configurar o uso do Swagger na aplicação.
 *
 * @author Brazil Code - Gabriel Guarido
 * @since 13 de mar de 2020 23:26:52
 * @version 1.0
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
          .select()
          .apis(RequestHandlerSelectors.basePackage("br.com.brazilcode.cb.purchase.controller"))
          .paths(PathSelectors.any())
          .build()
          .apiInfo(metaInfo());
    }

	@SuppressWarnings("rawtypes")
	private ApiInfo metaInfo() {
		return new ApiInfo(
				"Purchase Request REST API", 
				"REST API for the purchase module of the Clean Budget project", 
				"1.0", 
				"Terms of Service",
				new Contact("Gabriel Guarido", "https://www.linkedin.com/in/gabriel-guarido-oliveira/", 
						"gabrielguarido.oliveira@gmail.com"),
				"MIT License",
				"https://opensource.org/licenses/MIT", new ArrayList<VendorExtension>()
		);
	}

}
