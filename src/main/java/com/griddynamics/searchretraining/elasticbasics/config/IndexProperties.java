package com.griddynamics.searchretraining.elasticbasics.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "index")
public record IndexProperties(
        String alias,
        String settings,
        String bulkData
) {

    public String aliasPattern() {
        return alias + "_*";
    }
}