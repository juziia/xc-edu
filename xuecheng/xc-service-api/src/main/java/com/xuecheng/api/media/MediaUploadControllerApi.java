package com.xuecheng.api.media;

import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

@Api(value = "媒资管理接口",description = "媒资管理接口,提供文件上传,文件处理等接口")
public interface MediaUploadControllerApi {

    // 上传文件前的处理
    @ApiOperation("向服务端请求注册上传文件")
    public ResponseResult register(
            String fileMd5,
            String fileName,
            Long fileSize,
            String mimetype,
            String fileExt

    );

    // 分块检查
    @ApiOperation("校验分块文件是否存在,有则不会上传,没有就上传,达到断点上传的目的")
    public CheckChunkResult checkChunk(
            String fileMd5,
            Integer chunk,
            Long chunkSize
    );

    @ApiOperation("上传分块")
    public ResponseResult uploadChunk(
            MultipartFile file,
            Integer chunk,
            String fileMd5
    );


    @ApiOperation("合并上传")
    public ResponseResult mergeChunks(
            String fileMd5,
            String fileName,
            Long fileSize,
            String mimetype,
            String fileExt
    );


}
