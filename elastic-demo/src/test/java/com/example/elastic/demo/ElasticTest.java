package com.example.elastic.demo;

import com.example.elastic.demo.dao.UserRepository;
import com.example.elastic.demo.pojo.User;
import org.elasticsearch.index.query.QueryBuilders;
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

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class ElasticTest {

    @Autowired
    ElasticsearchRestTemplate template;

    @Autowired
    UserRepository userRepository;

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
        // 构建查询
//        queryBuilder.withQuery(QueryBuilders.matchQuery("name", "韩红"));
//        // 构建分页条件
//        queryBuilder.withPageable(PageRequest.of(0, 2));
//        // 构建排序条件
//        queryBuilder.withSort(SortBuilders.fieldSort("age").order(SortOrder.DESC));
//        queryBuilder.withHighlightBuilder(new HighlightBuilder().field("name").preTags("<em>").postTags("</em"));
//        // 执行查询
//        Page<User> userPage = this.userRepository.search(queryBuilder.build());
//        System.out.println("命中数：" + userPage.getTotalElements());
//        System.out.println("页数：" + userPage.getTotalPages());
//        userPage.getContent().forEach(System.out::println);
//        System.out.println(userPage.toString());

//        queryBuilder.withQuery(QueryBuilders.matchAllQuery());
        queryBuilder.withQuery(QueryBuilders.matchQuery("name","李四"));
        queryBuilder.withPageable(PageRequest.of(0,8));
        queryBuilder.withSort(SortBuilders.fieldSort("age").order(SortOrder.ASC));
        queryBuilder.withHighlightBuilder(new HighlightBuilder().field("name").preTags("<em>").postTags("</em>"));
        Page<User> userpage=userRepository.search(queryBuilder.build());

        userpage.getContent().forEach(System.out::println);
        System.out.println("总数:"+userpage.getTotalElements());
        System.out.println("总页数:"+userpage.getTotalPages());

    }

    //todo 获取高亮结果集


}
