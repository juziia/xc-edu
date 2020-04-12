package com.xuecheng.filesystem.service.impl;

import com.alibaba.fastjson.JSON;
import com.xuecheng.filesystem.config.FastDFSConfig;
import com.xuecheng.filesystem.dao.FileSystemRepository;
import com.xuecheng.filesystem.service.FileSystemService;
import com.xuecheng.framework.domain.filesystem.FileSystem;
import com.xuecheng.framework.domain.filesystem.response.FileSystemCode;
import com.xuecheng.framework.exception.CastException;
import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.StorageClient1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class FileSystemServiceImpl implements FileSystemService {

    @Autowired
    private FastDFSConfig fastDfsConfig;

    @Autowired
    private FileSystemRepository fileSystemRepository;

    @Override
    public FileSystem uploadFile(MultipartFile multipartFile, String fileTag, String businessKey, String metadata) {
        // 上传至fastDfs文件服务器
        String fileId = this.upload(multipartFile);
        // 定义fileSystem对象,设置属性值,然后返回
        FileSystem fileSystem = new FileSystem();
        fileSystem.setBusinesskey(businessKey);
        fileSystem.setFileId(fileId);
        fileSystem.setFilePath(fileId);
        fileSystem.setFileSize(multipartFile.getSize());
        fileSystem.setFileType(multipartFile.getContentType());
        fileSystem.setFiletag(fileTag);
        fileSystem.setFileName(multipartFile.getOriginalFilename());
        if (StringUtils.isNotBlank(metadata)) {
            Map map = JSON.parseObject(metadata, Map.class);
            fileSystem.setMetadata(map);
        }
        // 存入mongodb数据库中
        fileSystemRepository.save(fileSystem);
        return fileSystem;
    }

    private String upload(MultipartFile multipartFile) {
        StorageClient1 storageClient1 = fastDfsConfig.getStorageClient1();
        // 获取文件名称
        String filename = multipartFile.getOriginalFilename();
        // 获取后缀
        String suffix = filename.substring(filename.lastIndexOf('.') + 1);
        // 获取文件的内容
        String fileId = null;
        try {
            fileId = storageClient1.upload_file1(multipartFile.getBytes(), suffix, null);
        } catch (Exception e) {
            // 抛出异常
            CastException.cast(FileSystemCode.FS_UPLOADFILE_SERVERFAIL);
        }
        return fileId;

    }
}
