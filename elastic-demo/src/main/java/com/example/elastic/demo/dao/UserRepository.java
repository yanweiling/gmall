package com.example.elastic.demo.dao;

import com.example.elastic.demo.pojo.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface UserRepository extends ElasticsearchRepository<User,Long> {

    List<User> findByAgeBetween(Integer age1, Integer age2);
    List<User> findByNameLike(String name);

}
