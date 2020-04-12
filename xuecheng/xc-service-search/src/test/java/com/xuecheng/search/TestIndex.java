package com.xuecheng.search;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.ElasticsearchClient;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.engine.Engine;
import org.elasticsearch.index.query.*;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.mongodb.core.mapping.TextScore;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestIndex {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Autowired
    RestClient restClient;

    @Test
    public void testDeleteIndex() throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("xc_course");
        //
        IndicesClient indices = restHighLevelClient.indices();
        DeleteIndexResponse deleteIndexResponse = indices.delete(deleteIndexRequest);

    }


    @Test
    public void createIndex() throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("xc_course");
        createIndexRequest.settings(Settings.builder().put("number_of_shards", "1").put("number_of_replicas", "0"));
        // 获取es客户端
        IndicesClient indices = restHighLevelClient.indices();
        // 创建索引库
        CreateIndexResponse createIndexResponse = indices.create(createIndexRequest);


    }

    // 创建映射关系
    @Test
    public void testIndexMapping() throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("xc_course");
        createIndexRequest.settings(Settings.builder().put("number_of_replicas", "0").put("number_of_shards", "1"));
        createIndexRequest.mapping("doc", "{\n" +
                "                \"properties\": {\n" +
                "                    \"description\": {\n" +
                "                        \"type\": \"text\",\n" +
                "                        \"analyzer\": \"ik_max_word\",\n" +
                "                        \"search_analyzer\": \"ik_smart\"\n" +
                "                    },\n" +
                "                    \"name\": {\n" +
                "                        \"type\": \"text\",\n" +
                "                        \"analyzer\": \"ik_max_word\",\n" +
                "                        \"search_analyzer\": \"ik_smart\"\n" +
                "                    },\n" +
                "                    \"pic\": {\n" +
                "                        \"type\": \"text\",\n" +
                "                        \"index\": false\n" +
                "                    },\n" +
                "                    \"price\": {\n" +
                "                        \"type\": \"float\"\n" +
                "                    },\n" +
                "                    \"studymodel\": {\n" +
                "                        \"type\": \"keyword\"\n" +
                "                    },\n" +
                "                    \"timestamp\": {\n" +
                "                        \"type\": \"date\",\n" +
                "                        \"format\": \"yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis\"\n" +
                "                    }\n" +
                "                }\n" +
                "            }", XContentType.JSON);
        IndicesClient indices = restHighLevelClient.indices();
        CreateIndexResponse createIndexResponse = indices.create(createIndexRequest);
        String index = createIndexResponse.index();
        System.out.println(index);

    }


    @Test
    public void testDelete() throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("xc_course");
        IndicesClient indices = restHighLevelClient.indices();
        DeleteIndexResponse delete = indices.delete(deleteIndexRequest);


    }

    @Test
    public void testIndex() throws IOException {
        IndexRequest indexRequest = new IndexRequest("xc_course", "doc");
        Map<String, Object> jsonMap = new HashMap<>();

        jsonMap.put("name", "spring cloud实战");
        jsonMap.put("description", "本课程主要从四个章节进行讲解： 1.微服务架构入门 2.spring cloud  基础入门 3.实战Spring Boot 4.注册中心eureka。");
        jsonMap.put("studymodel", "201001");
        SimpleDateFormat dateFormat = new
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //jsonMap.put("timestamp", dateFormat.format(new Date()));
        jsonMap.put("price", 5.6f);

        indexRequest.source(jsonMap);
        IndexResponse indexResponse = restHighLevelClient.index(indexRequest);
        RestStatus status = indexResponse.status();
        System.out.println(status.getStatus());


    }

    @Test
    public void find() throws IOException {
        GetRequest get = new GetRequest("xc_course","doc","yOebN3EBOYvIg3T3h2tS");
        GetResponse documentFields = restHighLevelClient.get(get);

        Map<String, Object> source = documentFields.getSource();
        System.out.println(source);

    }

    @Test
    public void testDeleteDocument() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest("xc_course","doc","yOebN3EBOYvIg3T3h2tS");
        DeleteResponse delete = restHighLevelClient.delete(deleteRequest);

    }


    @Test
    public void test0002() throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("xc_course");
        IndicesClient indices = restHighLevelClient.indices();
        DeleteIndexResponse delete = indices.delete(deleteIndexRequest);

        System.out.println(delete);

    }




    @Test
    public void testSerch() throws IOException {
        // 定义查询请求对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        // 定义查询构造器对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 查询方式
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        // 设置显示字段
        searchSourceBuilder.fetchSource(new String[]{"name","description","price"},new String[]{});
        // 进行查询
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        // 获取查询到的数据
        SearchHits searchHits = searchResponse.getHits();
        // 总记录数
        long totalHits = searchHits.totalHits;
        // 获取数据
        SearchHit[] searchHitsArr = searchHits.getHits();
        System.out.println(totalHits);
        for(SearchHit searchHit : searchHitsArr){
            String sourceAsMap = searchHit.getSourceAsString();
            System.out.println(sourceAsMap);


        }


    }



    @Test
    public void testSearchByPage() throws IOException {
        SearchRequest searchRequest = new SearchRequest("xc_course");
        // 定义查询构建器
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 分页查询
        int page = 2;
        int size = 1;
        int pageIndex = page >= 1 ?(page -1)*size : 0;
        searchSourceBuilder.from(pageIndex);
        searchSourceBuilder.size(size);

        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.fetchSource(new String[]{"name","description","price"},new String[]{});

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        SearchHits searchHits = searchResponse.getHits();
        long totalHits = searchHits.totalHits;
        SearchHit[] hits = searchHits.getHits();
        System.out.println("总记录数: "+totalHits);
        for (SearchHit hit : hits){
            String sourceAsString = hit.getSourceAsString();
            System.out.println(sourceAsString);
        }
    }



    @Test
    public void testTermQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest("xc_course");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        /*// 分页
        int page = 1;
        int size = 1;
        int index =  page -1 > 0 ? (page - 1) * size : 0;

        // 分页查询
        searchSourceBuilder.from(index);
        searchSourceBuilder.size(size);*/
        // 字段过滤
        searchSourceBuilder.fetchSource(new String[]{"name","price","description"},new String[]{});
        // 查询方式 TermQuery
        searchSourceBuilder.query(QueryBuilders.termQuery("name","开发"));
        // 查询
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        // 获取总记录数
        SearchHits searchHits = searchResponse.getHits();
        long totalHits = searchHits.totalHits;
        System.out.println("总记录数: "+totalHits);
        // 获取数据
        SearchHit[] searchHitsArr = searchHits.getHits();
        for (SearchHit searchHit : searchHitsArr) {
            System.out.println(searchHit.getSourceAsString());
        }

    }

    @Test
    public void testTermsIds() throws IOException {
        SearchRequest searchRequest = new SearchRequest("xc_course");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        String[] ids = new String[]{"1","2","100"};
        searchSourceBuilder.query(QueryBuilders.termsQuery("_id",ids));
//        searchSourceBuilder.fetchSource();
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        SearchHits searchHits = searchResponse.getHits();
        SearchHit[] searchHitsArr = searchHits.getHits();
        System.out.println("总记录数: "+searchHits.totalHits);
        for (SearchHit searchHit : searchHitsArr) {
            System.out.println(searchHit.getSourceAsString());
        }

    }


    @Test
    public void matchQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest("xc_course");
        SearchSourceBuilder searchRequestBuilder = new SearchSourceBuilder();
        searchRequestBuilder.query(QueryBuilders.matchQuery("description","java开发领域").minimumShouldMatch("80%"));

        searchRequest.source(searchRequestBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        SearchHits searchHits = searchResponse.getHits();
        SearchHit[] searchHits1 = searchHits.getHits();
        for (SearchHit searchHit : searchHits1) {
            System.out.println(searchHit.getSourceAsString());
        }

    }

    @Test // MultiMatchQuery : 多个字段匹配查询
    public void multiMatchQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest("xc_course");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 对多个字段进行匹配查询
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery("spring css","name","description").minimumShouldMatch("50%")
            .field("name",10));

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);

        SearchHits searchHits = searchResponse.getHits();
        for (SearchHit hit : searchHits.getHits()) {
            System.out.println(hit.getSourceAsString());
        }


    }


    @Test
    public void testMultiMatchQueryAndBoost() throws IOException {
        SearchRequest searchRequest = new SearchRequest("xc_course");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(QueryBuilders.multiMatchQuery("spring css","name","description")
                    .field("name",10));
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        SearchHits searchHits = searchResponse.getHits();
        SearchHit[] searchHitsArr = searchHits.getHits();
        for (SearchHit searchHit : searchHitsArr) {
            String source = searchHit.getSourceAsString();
            System.out.println(source);
        }
    }



    @Test
    public void testBoolQuery() throws IOException {
        SearchRequest searchRequest = new SearchRequest("xc_course");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // 构建MultiMatchQuery对象
        MultiMatchQueryBuilder multiMatchQuery = QueryBuilders.multiMatchQuery("spring css", "name", "description").field("name", 10);
        // 构建termQuery对象
        TermQueryBuilder termQuery = QueryBuilders.termQuery("studymodel", "201001");
         // 使用boolQuery将multiMatchQuery和termQuery组装
        /**
         * boolQuery: must mustNot should
         */
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().must(multiMatchQuery).must(termQuery);
        searchSourceBuilder.query(boolQuery);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        SearchHits searchHits = searchResponse.getHits();
        SearchHit[] searchHitsArr = searchHits.getHits();
        for (SearchHit searchHit : searchHitsArr){
            System.out.println(searchHit.getSourceAsString());
        }


    }


    @Test
    public void testFilter() throws IOException {
        SearchRequest searchRequest = new SearchRequest("xc_course");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        MultiMatchQueryBuilder multiMatchQuery = QueryBuilders.multiMatchQuery("spring css", "name", "description")
                .minimumShouldMatch("50%").field("name", 10);

        RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("price").gte(77).lte(100);
        TermQueryBuilder termQuery = QueryBuilders.termQuery("studymodel", "201001");

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.filter(rangeQuery).filter(termQuery).must(multiMatchQuery);

        searchSourceBuilder.query(boolQuery);

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);

        SearchHit[] searchHits = searchResponse.getHits().getHits();
        for (SearchHit searchHit : searchHits) {
            System.out.println(searchHit.getSourceAsString());
        }
    }

    @Test
    public void testSort() throws IOException {
        SearchRequest searchRequest = new SearchRequest("xc_course");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.filter(QueryBuilders.rangeQuery("price").gte(0).lte(100));
        // 排序
        searchSourceBuilder.sort("studymodel", SortOrder.DESC).
                sort("price",SortOrder.ASC);
        searchSourceBuilder.query(boolQuery);

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        for (SearchHit searchHit : searchHits) {
            System.out.println(searchHit.getSourceAsString());
        }

    }





    @Test
    public void testHighLight() throws IOException {
        SearchRequest searchRequest = new SearchRequest("xc_course");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        MultiMatchQueryBuilder multiMatch = QueryBuilders.multiMatchQuery("开发框架", "name", "description")
                .minimumShouldMatch("50%").field("name", 10).type(MultiMatchQueryBuilder.Type.BEST_FIELDS);
        boolQuery.filter(QueryBuilders.rangeQuery("price").gte(0).lte(100));
        boolQuery.must(multiMatch);

        HighlightBuilder highLightBuilder = new HighlightBuilder();
        highLightBuilder.preTags("<tag>").postTags("</tag>");
        highLightBuilder.field("name");
        highLightBuilder.field("description");

        searchSourceBuilder.sort("price",SortOrder.ASC);
        searchSourceBuilder.highlighter(highLightBuilder);
        searchSourceBuilder.query(boolQuery);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        StringBuilder stringBuilder = new StringBuilder();
        for (SearchHit searchHit : searchHits) {
            Map<String, HighlightField> fieldMap = searchHit.getHighlightFields();
            HighlightField name = fieldMap.get("name");
            HighlightField description = fieldMap.get("description");
            if(name != null){
                Text[] fragments = name.getFragments();
                for (Text fragment : fragments) {
                    stringBuilder.append(fragment.toString());
                }
            }
            if(description != null){
                Text[] fragments = description.getFragments();
                for (Text fragment : fragments) {
                    stringBuilder.append(fragment.string());
                }
            }
        }
        System.out.println(stringBuilder);

    }






}
