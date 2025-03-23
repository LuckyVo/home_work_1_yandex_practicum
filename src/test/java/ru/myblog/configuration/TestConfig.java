package ru.myblog.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;

@Configuration
@ComponentScan("ru.myblog")
@TestPropertySource("classpath:application.properties")
public class TestConfig {

}