package com.xuecheng.manage_cms;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.netflix.discovery.converters.Auto;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.GenerateHtmlUrlResult;
import com.xuecheng.manage_cms.service.CmsPageService;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RestTemplateTest {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;

    @Autowired
    CmsPageService cmsPageService;


    @Test
    public void testCMs(){
        String url = "http://xc-service-manage-cms/cms/page/publish";

        CmsPage cmsPage = cmsPageService.findById("5e8371d57ec26d4bbc555783");
        ResponseEntity<GenerateHtmlUrlResult> post = restTemplate.postForEntity(url, cmsPage, GenerateHtmlUrlResult.class);
        GenerateHtmlUrlResult body = post.getBody();
        String pageUrl = body.getPageUrl();
        System.out.println(pageUrl);

    }

    @Test
    public void test01(){
        ResponseEntity<Map> responseEntity = restTemplate.getForEntity("http://localhost:31001/cms/config/getmodel/5a791725dd573c3574ee333f", Map.class);
        // 获取响应的数据
        Map map = responseEntity.getBody();
        System.out.println(map);
    }


    @Test
    public void generatedTemplate() throws IOException {
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is("5e7cb1237ec26d4eb09c4943")));
        // 根据文件的id打开流对象
        GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        // 根据流对象获取文件源对象
        GridFsResource gridFsResource = new GridFsResource(gridFSFile,downloadStream);
        // 根据文件源对象获取数据
        String content = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
        System.out.println(content);
    }

    @Test
    public void store() throws FileNotFoundException {
        File file = new File("C:\\workdir\\java\\idea\\project\\xuecheng\\freemarker-test\\src\\main\\resources\\templates\\test1.ftl");

        ObjectId id = gridFsTemplate.store(new FileInputStream(file), "轮播图测试模板");
        System.out.println(id.toString());

    }


    @Test
    public void testStore() throws FileNotFoundException {
        File file = new File("C:\\workdir\\java\\idea\\project\\xuecheng\\freemarker-test\\src\\main\\resources\\templates\\index_banner.ftl");

        ObjectId id = gridFsTemplate.store(new FileInputStream(file), "index_banner.html");
        System.out.println(id);
    }


    @Test
    public void testCourseTemplate() throws FileNotFoundException {
        File file = new File("C:\\workdir\\java\\idea\\project\\xuecheng\\freemarker-test\\src\\main\\resources\\templates\\course.ftl");
        FileInputStream in = new FileInputStream(file);

        ObjectId id = gridFsTemplate.store(in, "course.ftl");
        System.out.println(id);
        //5e832c697ec26d22e0902c63
    }




}
