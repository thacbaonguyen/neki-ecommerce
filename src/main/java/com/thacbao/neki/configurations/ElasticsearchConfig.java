package com.thacbao.neki.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.thacbao.neki.repositories.elasticsearch")
public class ElasticsearchConfig {
}
