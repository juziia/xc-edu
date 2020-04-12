package com.xuecheng.search.service.impl;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.exception.CastException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.search.service.EsCourseService;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.util.NumericUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.Column;
import java.io.IOException;
import java.util.*;

@Service
public class EsCourseServiceImpl implements EsCourseService {

    @Value("${xuecheng.elasticsearch.course.index}")
    private String index;
    @Value("${xuecheng.elasticsearch.course.type}")
    private String type;
    @Value("${xuecheng.elasticsearch.course.source_field}")
    private String source_field;


    @Value("${xuecheng.elasticsearch.media.index}")
    private String mediaIndex;
    @Value("${xuecheng.elasticsearch.media.type}")
    private String mediaType;
    @Value("${xuecheng.elasticsearch.media.source_field}")
    private String media_source_field;




    @Autowired
    private RestHighLevelClient restHighLevelClient;


    @Override
    public QueryResult<CoursePub> list(int page, int size, CourseSearchParam courseSearchParam) {
        String[] source_field_arr = source_field.split(",");
        // 判断是否为null
        if(courseSearchParam == null){
            courseSearchParam = new CourseSearchParam();
        }
        // 定义查询请求对象
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);
        // 定义查询源构造器对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 定义布尔查询
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        String filter = courseSearchParam.getFilter();
        String grade = courseSearchParam.getGrade();
        String keyword = courseSearchParam.getKeyword();
        String mt = courseSearchParam.getMt();
        String st = courseSearchParam.getSt();
        Double price_max = courseSearchParam.getPrice_max();
        price_max = price_max == null ? 0 :price_max;
        Double price_min = courseSearchParam.getPrice_min();
        price_min = price_min== null?  0 : price_min;
        String sort = courseSearchParam.getSort();

        // 根据关键字查询
        if(StringUtils.isNotBlank(keyword)){
            MultiMatchQueryBuilder multiMatchQuery = QueryBuilders.multiMatchQuery(keyword, "name", "description", "teachplan").
                    minimumShouldMatch("75%") // 最小匹配度
                    .field("name", 10);// 配置name的激励银子,提供name字段的权重
            // 模糊查询
            FuzzyQueryBuilder fuzzyQuery = QueryBuilders.fuzzyQuery("name", keyword)
                    .maxExpansions(3).fuzziness(Fuzziness.TWO);

            //multiMatchQuery.fuzziness(fuzzyQuery);
            // 高亮显示
            HighlightBuilder highLightBuilder = new HighlightBuilder();
            highLightBuilder.preTags("<font class='eslight'>").postTags("</font>");
            // 设置高亮字段
            highLightBuilder.fields().add(new HighlightBuilder.Field("name"));

            searchSourceBuilder.highlighter(highLightBuilder);

            boolQuery.must(multiMatchQuery).should(fuzzyQuery);
        }

        // 根据难度级别查询
        if(StringUtils.isNotBlank(grade)){
            boolQuery.filter(QueryBuilders.termQuery("grade",grade));
        }

        // 根据一级分类查询
        if(StringUtils.isNotBlank(mt)){
            boolQuery.filter(QueryBuilders.termQuery("mt",mt));
        }
        // 根据二级分类查询
        if(StringUtils.isNotBlank(st)){
            boolQuery.filter(QueryBuilders.termQuery("st",st));
        }
        // 根据价格范围查询
        price_min = price_min > 0 ? price_min : 0;
        RangeQueryBuilder rangeQuery = null;
        if(price_min != null){
            rangeQuery = QueryBuilders.rangeQuery("price").gte(price_min);
            if(price_max != null && price_max != 0){
                rangeQuery.lte(price_max);
            }
            boolQuery.filter(rangeQuery);
        }

        // 排序字段
        if(StringUtils.isNotBlank(sort)){
            searchSourceBuilder.sort(sort, SortOrder.ASC);
        }

        // 显示源字段
        String[] filter_arr = null;
        if(filter!= null&& filter.length() !=0) {
            filter_arr = filter.split(",");
        }
        searchSourceBuilder.fetchSource(source_field_arr,filter_arr);

        searchSourceBuilder.query(boolQuery);
        //
        size = size > 0 ? size : 20;
        int pageIndex = page > 0 ? (page - 1) * size : 0;
        searchSourceBuilder.from(pageIndex);
        searchSourceBuilder.size(size);

        searchRequest.source(searchSourceBuilder);
        QueryResult<CoursePub> queryResult = new QueryResult<>();
        List<CoursePub> list = new ArrayList();
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
            SearchHits searchHits = searchResponse.getHits();
            long totalHits = searchHits.getTotalHits();
            queryResult.setTotal(totalHits);
            for (SearchHit searchHit : searchHits) {
                CoursePub coursePub = new CoursePub();
                Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
                String nameStr = (String) sourceAsMap.get("name");
                // 获取高亮显示的数据
                Map<String, HighlightField> highlightFieldMap = searchHit.getHighlightFields();
                if(highlightFieldMap != null && !highlightFieldMap.isEmpty()) {
                    HighlightField name = highlightFieldMap.get("name");
                    if (name != null) {
                        Text[] nameFragments = name.getFragments();
                        StringBuilder stringBuilder = new StringBuilder();
                        for (Text text : nameFragments) {
                            stringBuilder.append(text.string());
                        }
                        nameStr = stringBuilder.toString();
                    }
                }
                String id = (String) sourceAsMap.get("id");
                coursePub.setId(id);
                coursePub.setName(nameStr);
                String grade2 = (String) sourceAsMap.get("grade");
                coursePub.setGrade(grade2);
                String pic = (String) sourceAsMap.get("pic");
                coursePub.setPic(pic);
                Double price = (Double) sourceAsMap.get("price");
                coursePub.setPrice(price);
                Double price_old = (Double) sourceAsMap.get("price_old");
                coursePub.setPrice_old(price_old);
                String charge = (String) sourceAsMap.get("charge");
                coursePub.setCharge(charge);

                list.add(coursePub);
            }
            queryResult.setTotal(totalHits);
            queryResult.setList(list);
            return queryResult;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, CoursePub> getById(String id) {
        // 定义查询请求对象
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);
        // 定义查询源构造器对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 根据id进行词条查询
        TermQueryBuilder termQuery = QueryBuilders.termQuery("id", id);
        // 使用布尔查询过滤其他数据
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().filter(termQuery);
        // 设置到searchSourceBuilder中
        searchSourceBuilder.query(boolQuery);
        searchRequest.source(searchSourceBuilder);
        // 发起查询
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
            // 获取结构数据
            SearchHit searchHit = searchResponse.getHits().getHits()[0];
            Map<String, Object> sourceMap = searchHit.getSourceAsMap();
            // 从map中获取数据设置到CoursePub数据模型中
            String courseId = (String) sourceMap.get("id");
            String name = (String) sourceMap.get("name");
            String grade = (String) sourceMap.get("grade");
            String charge = (String) sourceMap.get("charge");
            String pic = (String) sourceMap.get("pic");
            String description = (String) sourceMap.get("description");
            String teachPlan = (String) sourceMap.get("teachplan");

            CoursePub coursePub = new CoursePub();
            coursePub.setId(courseId);
            coursePub.setName(name);
            coursePub.setGrade(grade);
            coursePub.setCharge(charge);
            coursePub.setPic(pic);
            coursePub.setDescription(description);
            coursePub.setTeachplan(teachPlan);
            Map<String,CoursePub> map = new HashMap<>();
            map.put(id,coursePub);
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public QueryResult<TeachplanMediaPub> getMedia(String[] teachplanId) {
        SearchRequest searchRequest = new SearchRequest(mediaIndex);
        searchRequest.types(mediaType);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 根据课程计划id数组进行查询
        searchSourceBuilder.query(QueryBuilders.termsQuery("teachplan_id",teachplanId));
        // 过滤源字段
        String[] media_source_field_arr = media_source_field.split(",");
        searchSourceBuilder.fetchSource(media_source_field_arr,null);
        searchRequest.source(searchSourceBuilder);
        // 使用客户端对象发起查询
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
            SearchHits searchHits = searchResponse.getHits();
            // 获取总记录数
            long totalHits = searchHits.getTotalHits();
            // 获取数据列表
            SearchHit[] searchHitsArr = searchHits.getHits();
            List<TeachplanMediaPub> list = new ArrayList<>();
            for (SearchHit searchHit : searchHitsArr){
                Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
                //courseid,media_id,media_url,teachplan_id,media_fileoriginalname
                String courseid = (String) sourceAsMap.get("courseid");
                String media_id = (String) sourceAsMap.get("media_id");
                String media_url = (String) sourceAsMap.get("media_url");
                String teachplan_id = (String) sourceAsMap.get("teachplan_id");
                String media_fileoriginalname = (String) sourceAsMap.get("media_fileoriginalname");
                TeachplanMediaPub teachplanMediaPub = new TeachplanMediaPub();

                teachplanMediaPub.setCourseId(courseid);
                teachplanMediaPub.setMediaFileOriginalName(media_fileoriginalname);
                teachplanMediaPub.setMediaUrl(media_url);
                teachplanMediaPub.setTeachplanId(teachplan_id);
                teachplanMediaPub.setMediaId(media_id);
                list.add(teachplanMediaPub);
            }
            QueryResult<TeachplanMediaPub> queryResult = new QueryResult<>();
            queryResult.setTotal(totalHits);
            queryResult.setList(list);
            return queryResult;
        } catch (IOException e) {
            CastException.cast(CommonCode.SERVER_ERROR);
            e.printStackTrace();
        }
        return null;
    }
}
