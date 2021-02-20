package com.example.elastic.demo.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {
    @Bean
    RestHighLevelClient client() {
        return new RestHighLevelClient(
                RestClient.builder(
                        HttpHost.create("http://localhost:9200")
                ));
    }
}
