package ru.myblog.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("ru.myblog")
@PropertySource("classpath:application.properties")
public class AppConfig {
}
