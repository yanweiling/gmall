package com.example.elastic.demo;

import com.example.elastic.demo.dao.UserRepository;
import com.example.elastic.demo.pojo.User;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class ElasticTest {

    @Autowired
    ElasticsearchRestTemplate template;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Test
    public void test() {
        template.createIndex(User.class);
        template.putMapping(User.class);
    }

    @Test
    public void testSearchByAge(){
        int size=userRepository.findByAgeBetween(1,10).size();
        System.out.println(size);
        userRepository.findByNameLike("韩").forEach(System.out::println);
    }

    @Test
    public void testAdd(){
        userRepository.save(new User(1l,"韩红",3,"123456"));
        List<User> users= Arrays.asList(
                new User(7l,"张三",36,"123456"),
                new User(2l,"李四",34,"123456"),
                new User(3l,"李宇冲",33,"123456"),
                new User(4l,"张飞",33,"123456"),
                new User(5l,"肖战",23,"123456"),
                new User(6l,"赵丽颖",53,"123456"));
        userRepository.saveAll(users);
    }

    @Test
    public void find(){
        userRepository.findAll().forEach(System.out::println);
        User user=userRepository.findById(2l).get();
        System.out.println(user);
    }

    @Test
    public void testNativeQuery(){
        // 自定义查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        queryBuilder.withQuery(QueryBuilders.matchQuery("name","李四"));
        queryBuilder.withPageable(PageRequest.of(0,8));
        queryBuilder.withSort(SortBuilders.fieldSort("age").order(SortOrder.ASC));
        //这种方式没有实现高亮显示
        queryBuilder.withHighlightBuilder(new HighlightBuilder().field("name").preTags("<em>").postTags("</em>"));
        Page<User> userpage=userRepository.search(queryBuilder.build());

        userpage.getContent().forEach(System.out::println);
        System.out.println("总数:"+userpage.getTotalElements());
        System.out.println("总页数:"+userpage.getTotalPages());

    }

    //todo 获取高亮结果集
    @Test
    public void testHignLevelShow() throws IOException {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchQuery("name", "李四"));
        sourceBuilder.sort("age", SortOrder.DESC);
        sourceBuilder.from(0);
        sourceBuilder.size(2);
        sourceBuilder.highlighter(new HighlightBuilder().field("name").preTags("<em>").postTags("</em>"));
        SearchRequest searchRequest = new SearchRequest("user");
        searchRequest.types("info");
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = this.restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        System.out.println(hits.totalHits);
        System.out.println(hits.getMaxScore());
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit searchHit : searchHits) {
            System.out.println(searchHit.getHighlightFields());
            System.out.println(searchHit.getSourceAsString());
        }
    }

    //todo 名称精准查询


}
