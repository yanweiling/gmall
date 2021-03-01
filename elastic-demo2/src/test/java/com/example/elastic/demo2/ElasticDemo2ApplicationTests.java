package com.example.elastic.demo2;

import com.alibaba.fastjson.JSON;
import com.example.elastic.demo2.pojo.Article;
import com.example.elastic.demo2.pojo.Person;
import com.example.elastic.demo2.pojo.User;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.GetQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@SpringBootTest
class ElasticDemo2ApplicationTests {
    @Autowired
    RestHighLevelClient highLevelClient;

    @Autowired
    public ElasticsearchOperations elasticsearchOperations;

    @Test
    public void jsonIndex() throws IOException {
        User user=new User();
        user.setUserName("zhangsan");
        user.setPassword("zhangsan222");
        IndexRequest request = new IndexRequest("spring-data", "elasticsearch", "11111")
                .source(JSON.toJSONString(user), XContentType.JSON)
                .setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        IndexResponse response=highLevelClient.index(request);
    }


    @Test
    public void mapIndex(){
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("roleId", "1");
        jsonMap.put("create", new Date());
        jsonMap.put("role", "admin");
        IndexRequest indexRequest = new IndexRequest("spring-data", "role", "222")
                .source(jsonMap);
//https://www.cnblogs.com/cjsblog/p/10232581.html
//        IndexResponse response=highLevelClient.index(indexRequest,ReqeustOptions)
    }

//    @Test
//    public void initConfiguration(){
//// 关键字查询，查询性为“女"的所有记录
//        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("studentSex"/*字段名*/, "女"/*值*/);
//        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
//        nativeSearchQueryBuilder.withQuery(termQueryBuilder);
//        NativeSearchQuery nativeSearchQuery = nativeSearchQueryBuilder.build();
//        elasticsearchOperations
//        SearchHits<Map> search = this.elasticsearchOperations(nativeSearchQuery, Map.class, IndexCoordinates.of("xx"/*索引名*/));
//    }
    @Test
    public void save() {
        Person person=new Person();
        person.setUserName("阎维玲");
        person.setPassword("yanweiling");
        person.setId(2l);
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(person.getId().toString())
                .withObject(person)
                .build();
        String documentId = elasticsearchOperations.index(indexQuery);
        System.out.println(documentId);
    }

    @Test
    public void search(){
        Person person=
        elasticsearchOperations.queryForObject(GetQuery.getById("2"),Person.class);
        System.out.println(JSON.toJSONString(person));

    }



}
