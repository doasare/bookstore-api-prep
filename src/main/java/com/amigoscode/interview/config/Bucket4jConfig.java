package com.amigoscode.interview.config;

import org.springframework.boot.cache.autoconfigure.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.configuration.MutableConfiguration;

@Configuration
@EnableCaching
public class Bucket4jConfig {

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            if (cm.getCache("caffeine") == null) {
                cm.createCache("caffeine", new MutableConfiguration<>());
            }
        };
    }
}