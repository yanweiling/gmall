package com.example.elastic.demo2.pojo;

import lombok.Data;
import org.springframework.core.annotation.AliasFor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "info",type = "person")
public class Person {
    @Id
    private Long id;
    private String userName;
    private String password;
    @Transient
    private int age;

    public Person(){}
    public Person(String userName,String password){
        this.userName=userName;
        this.password=password;
    }
}
