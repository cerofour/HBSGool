package pe.edu.utp.dwi.HBSGool.configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfiguration {

	@Bean
	public OpenAPI productServiceApi() {
		return new OpenAPI()
				.info(new Info()
						.title("HBS Gool API")
						.summary("Documentación de la API de HBS Gool")
						.version("0.0.1")
						.license(new License().name("No License")))
				.externalDocs(new ExternalDocumentation()
						.description("Refer to the Docs")
						.url("https://github.com/cerofour"));
	}
}
