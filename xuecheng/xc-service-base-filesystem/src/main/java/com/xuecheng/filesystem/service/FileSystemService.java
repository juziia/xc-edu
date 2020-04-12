package com.xuecheng.filesystem.service;

import com.xuecheng.framework.domain.filesystem.FileSystem;
import org.springframework.web.multipart.MultipartFile;

public interface FileSystemService {

    FileSystem uploadFile(MultipartFile multipartFile, String fileTag, String businessKey, String metadata);
}
