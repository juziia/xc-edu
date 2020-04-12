package com.xuecheng.api.filesystem;

import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

@Api(value = "文件系统接口", description = "提供对文件系统的上传,下载,删除,查询等接口")
public interface FileSystemControllerApi {

    @ApiOperation("上传文件接口")
    public UploadFileResult uploadFile(MultipartFile multipartFile,
                                       String fileTag,
                                       String businessKey,
                                       String metadata );

}
