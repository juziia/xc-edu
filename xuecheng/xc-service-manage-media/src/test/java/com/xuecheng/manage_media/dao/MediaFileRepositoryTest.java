package com.xuecheng.manage_media.dao;

import com.mongodb.client.MongoClients;
import com.xuecheng.framework.domain.media.MediaFile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.TextScore;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MediaFileRepositoryTest {

    @Autowired
    private MediaFileRepository mediaFileRepository ;

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    public void testCreateDoucument(){
        mongoTemplate.createCollection(MediaFile.class);
    }

    @Test
    public void testAdd(){
        MediaFile mediaFile = new MediaFile();
        mediaFile.setFileId("454aaaaaaaaaaaaaaaasdasd");
        mediaFile.setFileName("测试");
        mediaFile.setFileOriginalName("cccc");
        mediaFile.setFilePath("test/aa");
        mediaFile.setFileStatus("22001");
        mediaFile.setFileUrl("fffffff");


        mediaFileRepository.save(mediaFile);

    }



}
