package com.xuecheng.manage_cms.config;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongodbConfig {

    @Value("${spring.data.mongodb.database}")
    private String database;


    // GridBucket:  用于打开下载流对象
    @Bean
    public GridFSBucket getGridFSBucket(@NotNull MongoClient mongoClient){
        MongoDatabase database = mongoClient.getDatabase(this.database);
        GridFSBucket gridFSBucket = GridFSBuckets.create(database);
        return gridFSBucket;
    }
}
