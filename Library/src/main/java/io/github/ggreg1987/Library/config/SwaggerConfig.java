package io.github.ggreg1987.Library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket docket() {

    }

    private ApiInfo apiInfo() {

    }

    private Contact contact() {
        return new Contact(
                "Gabriel Gregorio",
                "https://github.com/ggreg1987",
                "gr3g1987@gmail.com"
        );
    }
}
