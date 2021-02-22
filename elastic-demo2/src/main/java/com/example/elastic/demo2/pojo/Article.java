package com.example.elastic.demo2.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

@Document(indexName = "article",shards = 5, replicas = 1, indexStoreType = "fs", refreshInterval = "-1")
public class Article implements Serializable {
    private static final long serialVersionUID = 551589397625941750L;
    @Id
    private Long id;

    private String title;

    private String abstracts;

    private String content;

    @Field(format = DateFormat.date_time, index = true, store = true, type = FieldType.Object)
    private Date postTime;

    private Long clickCount;
}
