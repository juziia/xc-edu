package com.xuecheng.manage_cms_client.config;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseUtils;
import org.springframework.data.mongodb.MongoDbFactory;

@Configuration
public class MongodbConfig {

    @Value("${spring.data.mongodb.database}")   // 数据库名称
    private String db;

    @Bean
    public GridFSBucket createGridFSBucket(MongoClient mongoClient){
        MongoDatabase mongoDatabase = mongoClient.getDatabase(db);
        GridFSBucket gridFSBucket = GridFSBuckets.create(mongoDatabase);
        return gridFSBucket;
    }

}
