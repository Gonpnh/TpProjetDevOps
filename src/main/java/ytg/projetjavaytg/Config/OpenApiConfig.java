package ytg.projetjavaytg.Config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi restApiGroup() {
        return GroupedOpenApi.builder()
                .group("REST API")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public GroupedOpenApi mvcGroup() {
        return GroupedOpenApi.builder()
                .group("MVC Controllers")
                .pathsToExclude("/api/**")
                // scanne le package des controllers de vues
                .packagesToScan("ytg.projetjavaytg.Controllers")
                .build();
    }
}

