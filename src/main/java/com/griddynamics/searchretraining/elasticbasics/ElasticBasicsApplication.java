package com.griddynamics.searchretraining.elasticbasics;

import com.griddynamics.searchretraining.elasticbasics.config.IndexProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({IndexProperties.class})
public class ElasticBasicsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticBasicsApplication.class, args);
    }

}
