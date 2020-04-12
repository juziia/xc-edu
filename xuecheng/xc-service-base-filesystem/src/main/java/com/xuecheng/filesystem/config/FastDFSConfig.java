package com.xuecheng.filesystem.config;

import com.xuecheng.framework.domain.filesystem.response.FileSystemCode;
import com.xuecheng.framework.exception.CastException;
import com.xuecheng.framework.exception.CustomException;
import lombok.Data;
import lombok.ToString;
import net.bytebuddy.implementation.bytecode.Throw;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 *  FastDfs配置类:
 *      1.用于加载配置文件,完成FastDfs的环境初始化
 *      2.用于获取TrackerServer
 */
@Configuration
@Data
@ToString
@ConfigurationProperties(prefix = "xuecheng.fastdfs")
public class FastDFSConfig {

    private Integer connect_timeout_in_seconds;
    private Integer network_timeout_in_seconds;
    private String charset;
    private String tracker_servers;

    // 初始化FastDfs环境方法
    private void init() throws IOException, MyException {
        ClientGlobal.setG_connect_timeout(connect_timeout_in_seconds);
        ClientGlobal.setG_network_timeout(network_timeout_in_seconds);
        ClientGlobal.setG_charset(charset);
            ClientGlobal.initByTrackers(tracker_servers);
    }

    // 获取fastDfs客户端
    public StorageClient1 getStorageClient1(){
        try {
            init();
            // 定义TrackerClient第项
            TrackerClient trackerClient = new TrackerClient();
            // 获取TrackerServer
            TrackerServer trackerServer = trackerClient.getConnection();
            // 获取StorageServer
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            // 定义storageClient对昂
            StorageClient1 storageClient1 = new StorageClient1(trackerServer,storageServer);
            return storageClient1;
        } catch (Exception e) {
            throw new CustomException(FileSystemCode.FS_INIT_ERROR);
        }

    }


}
