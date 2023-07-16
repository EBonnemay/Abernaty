package com.mediscreen.abernatyclient.configuration;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

/*    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("springshop-public")
                .pathsToMatch("/public/**")
                .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                 .group("springshop-admin")
                .pathsToMatch("/admin/**")
                .build();
    }
}
*/

    @Bean
    public OpenAPI api() {
        return new OpenAPI();
    }
}

