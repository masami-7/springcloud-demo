package com.yl.test.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {

    @Bean
    public RestHighLevelClient restClient() {
        RestHighLevelClient restClient = new RestHighLevelClient(RestClient.builder(new HttpHost("127.0.0.1", 9200)));
        return restClient;
    }

}
