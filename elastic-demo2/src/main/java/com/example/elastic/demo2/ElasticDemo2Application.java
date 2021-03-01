package com.example.elastic.demo2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableReactiveElasticsearchRepositories;

@SpringBootApplication

public class ElasticDemo2Application {

    public static void main(String[] args) {
        SpringApplication.run(ElasticDemo2Application.class, args);
    }

}
