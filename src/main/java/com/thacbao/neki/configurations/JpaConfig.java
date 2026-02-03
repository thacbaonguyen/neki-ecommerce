package com.thacbao.neki.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.thacbao.neki.repositories.jpa")
public class JpaConfig {
}
