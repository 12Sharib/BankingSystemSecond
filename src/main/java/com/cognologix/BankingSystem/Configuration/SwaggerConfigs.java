package com.cognologix.BankingSystem.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
@Configuration
@EnableSwagger2
public class SwaggerConfigs {
        @Bean
        public Docket postsApi() {
            return new Docket(DocumentationType.SWAGGER_2)
                    .groupName("com.cognologix.BankingSystem")
                    .apiInfo(apiInfo())
                    .select()
                    .paths(PathSelectors.any())
                    .build();
        }
        private ApiInfo apiInfo() {
            return new ApiInfoBuilder().title("Banking System Application")
                    .description("Banking system with services")
                    .termsOfServiceUrl("http://cognologix.com")
                    .license("cognologix License")
                    .licenseUrl("cognologix@gmail.com")
                    .version("1.0").build();
        }

    }


