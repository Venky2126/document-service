package com.ama.app.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {

	
	/** OpenAPIConfig  to design documentation we will use */
	
	@Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .openapi("3.1.0") // Explicitly set OpenAPI 3.1.0 version
            .components(new Components()
                // You can add security schemes here if needed
                // .addSecuritySchemes("bearer-key", new SecurityScheme()
                //     .type(SecurityScheme.Type.HTTP)
                //     .scheme("bearer")
                //     .bearerFormat("JWT"))
            )
            .info(new Info()
                .title("PMS CARVE OUT API")
                .description("Card Service to perform Card operations")
                .version("1.0")
                .license(new License()
                    .name("Apache 2.0")
                    .url("https://www.apache.org/licenses/LICENSE-2.0.html"))
            );
    }
}
