package com.example.elastic.demo2.dao;

import com.example.elastic.demo2.pojo.Person;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;

public interface ReactivePersonRepository extends ReactiveSortingRepository<Person, String> {
    Flux<Person> findByUserName(String userName);
}
