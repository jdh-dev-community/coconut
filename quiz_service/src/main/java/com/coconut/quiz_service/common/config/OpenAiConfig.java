package com.coconut.quiz_service.common.config;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties(OpenAiProperties.class)
public class OpenAiConfig {
}
