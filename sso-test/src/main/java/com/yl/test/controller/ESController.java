package com.yl.test.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedMax;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/es")
@Slf4j
public class ESController {

    @Autowired
    private RestHighLevelClient restClient;

    /**
     * 模糊查询（matchQuery）
     */
    @GetMapping("/matchQuery/{indexName}/{field}/{value}")
    public <T> List<T> matchQuery(@PathVariable String indexName, @PathVariable String field, @PathVariable Object value) {
        // 查询的数据列表
        List<T> list = new ArrayList<>();
        try {
            // 构建查询条件（注意：termQuery 支持多种格式查询，如 boolean、int、double、string 等，这里使用的是 string 的查询）
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.matchQuery(field, value));
            // 执行查询es数据
            queryEsData(indexName, list, searchSourceBuilder);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    /**
     * 精确查询（termQuery）
     */
    @GetMapping("/termQuery/{indexName}/{field}/{value}")
    public <T> List<T> termQuery(@PathVariable String indexName, @PathVariable String field, @PathVariable Object value) {
        // 查询的数据列表
        List<T> list = new ArrayList<>();
        try {
            // 构建查询条件（注意：termQuery 支持多种格式查询，如 boolean、int、double、string 等，这里使用的是 string 的查询）
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.termQuery(field, value));
            // 执行查询es数据
            queryEsData(indexName, list, searchSourceBuilder);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    /**
     * 聚合查询 : 聚合查询一定是【先查出结果】，然后对【结果使用聚合函数】做处理.
     * <p>
     * Metric 指标聚合分析。常用的操作有：avg：求平均、max：最大值、min：最小值、sum：求和等
     * <p>
     */
    @GetMapping("/metricQuery/{indexName}")
    public void metricQuery(@PathVariable String indexName) {
        try {
            // 构建查询条件
            MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
            // 创建查询源构造器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(matchAllQueryBuilder);

            AggregationBuilder max = AggregationBuilders.max("max").field("time");
            searchSourceBuilder.aggregation(max);
            AggregationBuilder min = AggregationBuilders.min("min").field("time");
            searchSourceBuilder.aggregation(min);

            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest(indexName);
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restClient.search(searchRequest, RequestOptions.DEFAULT);
            // 打印请求体
            System.out.println(searchRequest.source().toString());
            System.out.println(JSONUtil.toJsonStr(searchResponse));
            Aggregations aggregations = searchResponse.getAggregations();
            System.out.println(JSONUtil.toJsonStr(aggregations));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 聚合查询： 聚合查询一定是【先查出结果】，然后对【结果使用聚合函数】做处理.
     * <p>
     * Bucket 分桶聚合分析 : 对查询出的数据进行分组group by，再在组上进行游标聚合
     * <p>
     */
    @GetMapping("/bucketQuery/{indexName}/{bucketField}/{bucketFieldAlias}")
    public void bucketQuery(@PathVariable String indexName,@PathVariable String bucketField,@PathVariable String bucketFieldAlias) {
        try {
            // 构建查询条件
            MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
            // 创建查询源构造器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(matchAllQueryBuilder);

            // 根据bucketField进行分组查询
            TermsAggregationBuilder aggBrandName = AggregationBuilders.terms(bucketFieldAlias).field(bucketField);
            searchSourceBuilder.aggregation(aggBrandName);

            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest(indexName);
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restClient.search(searchRequest, RequestOptions.DEFAULT);
            // 打印请求体
            System.out.println(searchRequest.source().toString());
            Aggregations aggregations = searchResponse.getAggregations();
            ParsedStringTerms aggBrandName1 = aggregations.get(bucketField); // 分组结果数据
            for (Terms.Bucket bucket : aggBrandName1.getBuckets()) {
                log.info(bucket.getKeyAsString() + "====" + bucket.getDocCount());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 子聚合聚合查询
     * Bucket 分桶聚合分析
     * <p>
     */
    @GetMapping("/subBucketQuery/{indexName}/{bucketField}/{bucketFieldAlias}/{maxField}/{maxFieldAlias}")
    public void subBucketQuery(@PathVariable String indexName, @PathVariable String bucketField, @PathVariable String bucketFieldAlias,
                               @PathVariable String maxField, @PathVariable String maxFieldAlias) {
        try {
            // 构建查询条件
            MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
            // 创建查询源构造器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(matchAllQueryBuilder);

            // 根据 bucketField进行分组查询,并且获取分类信息中 指定字段的平均值
            TermsAggregationBuilder subAggregation = AggregationBuilders.terms(bucketFieldAlias).field(bucketField)
                    .subAggregation(AggregationBuilders.max(maxFieldAlias).field(maxField));
            searchSourceBuilder.aggregation(subAggregation);

            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest(indexName);
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restClient.search(searchRequest, RequestOptions.DEFAULT);
            Aggregations aggregations = searchResponse.getAggregations();
            ParsedStringTerms aggBrandName1 = aggregations.get(bucketFieldAlias);
            for (Terms.Bucket bucket : aggBrandName1.getBuckets()) {
                // 获取聚合后的 组内字段平均值,注意返回值不是Aggregation对象,而是指定的ParsedAvg对象
                ParsedMax max = bucket.getAggregations().get(maxFieldAlias);
                log.info(bucket.getKeyAsString() + "====" + max.getValueAsString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 执行es查询
     *
     * @param indexName
     * @param list
     * @param searchSourceBuilder
     * @param <T>
     * @throws IOException
     */
    private <T> void queryEsData(String indexName, List<T> list, SearchSourceBuilder searchSourceBuilder) throws IOException {
        // 创建查询请求对象，将查询对象配置到其中
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(searchSourceBuilder);
        // 执行查询，然后处理响应结果
        SearchResponse searchResponse = restClient.search(searchRequest, RequestOptions.DEFAULT);
        // 打印请求体
        System.out.println(searchRequest.source().toString());
        // 根据状态和数据条数验证是否返回了数据
        if (RestStatus.OK.equals(searchResponse.status()) && searchResponse.getHits().getTotalHits().value > 0) {
            SearchHits hits = searchResponse.getHits();
            for (SearchHit hit : hits) {
                // 将 JSON 转换成对象
                JSONObject jsonObject = JSONUtil.parseObj(hit.getSourceAsString());
                list.add((T) jsonObject);
            }
        }
    }

}
