package com.xuecheng.filesystem.controller;

import com.xuecheng.api.filesystem.FileSystemControllerApi;
import com.xuecheng.filesystem.service.FileSystemService;
import com.xuecheng.framework.domain.filesystem.FileSystem;
import com.xuecheng.framework.domain.filesystem.response.FileSystemCode;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import com.xuecheng.framework.model.response.CommonCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("filesystem")
public class FileSystemController implements FileSystemControllerApi {

    @Autowired
    private FileSystemService fileSystemService;

    @Override
    @PostMapping("upload")
    public UploadFileResult uploadFile(@RequestParam("multipartFile") MultipartFile multipartFile, String fileTag, String businessKey, String metadata) {
        FileSystem fileSystem = fileSystemService.uploadFile(multipartFile, fileTag, businessKey, metadata);

        return new UploadFileResult(CommonCode.SUCCESS,fileSystem);
    }
}
