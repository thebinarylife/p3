package p3.config.Swagger;

import org.springframework.context.annotation.Bean;


import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2

public class Swagger {
	@Bean
	public Docket swaggerConfiguration() {
		return new Docket(DocumentationType.SWAGGER_2) 
				.apiInfo(apiInfo())
				.select()
				.paths(PathSelectors.ant("/rest/**"))
				.apis(RequestHandlerSelectors.basePackage("p3.rest"))
				.build();
	}
	
	/** @return ApiInfo that is used to display customized info on top of the swagger generated api documentations */
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("p3 REST api Reference")
				.version("1.0.0")
				.build();
	}

}
