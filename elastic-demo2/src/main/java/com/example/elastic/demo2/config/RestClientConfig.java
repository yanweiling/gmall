package com.example.elastic.demo2.config;


import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

@Configuration
public class RestClientConfig {
//    @Override
//    public RestHighLevelClient elasticsearchClient() {
//
//        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
//                .connectedTo("localhost:9200")
//                .build();
//
//        return RestClients.create(clientConfiguration).rest();
//    }

//    @Autowired
//    private ElasticsearchMonitorProperties esProperties;
//
//    @Bean
//    public RestHighLevelClient restHighLevelClient() {
//        String[] urlArr = esProperties.getEsAddress().split(",");
//        HttpHost[] httpPostArr = new HttpHost[urlArr.length];
//        for (int i = 0; i < urlArr.length; i++) {
//            HttpHost httpHost = new HttpHost(urlArr[i].split(":")[0].trim(),
//                    Integer.parseInt(urlArr[i].split(":")[1].trim()), "http");
//            httpPostArr[i] = httpHost;
//        }
//        RestClientBuilder builder = RestClient.builder(httpPostArr);
//        // 异步httpclient连接延时配置
//        builder.setRequestConfigCallback(requestConfigBuilder -> {
//            requestConfigBuilder.setConnectTimeout(esProperties.getConnectTimeOut());
//            requestConfigBuilder.setSocketTimeout(esProperties.getSocketTimeOut());
//            requestConfigBuilder.setConnectionRequestTimeout(esProperties.getConnectionRequestTimeOut());
//            HttpHost proxy = new HttpHost("127.0.0.1", 22, "http");
//            requestConfigBuilder.setProxy(proxy);
//            return requestConfigBuilder;
//        });
//
//        // 异步httpclient配置
//        builder.setHttpClientConfigCallback(httpClientBuilder -> {
//            // httpclient连接数配置
//            httpClientBuilder.setMaxConnTotal(esProperties.getMaxConnectNum());
//            httpClientBuilder.setMaxConnPerRoute(esProperties.getMaxConnectPerRoute());
//            // httpclient保活策略
//            httpClientBuilder.setKeepAliveStrategy(
//                    CustomConnectionKeepAliveStrategy.getInstance(esProperties.getKeepAliveMinutes()));
//            return httpClientBuilder;
//        });
//        return new RestHighLevelClient(builder);
//    }
}
