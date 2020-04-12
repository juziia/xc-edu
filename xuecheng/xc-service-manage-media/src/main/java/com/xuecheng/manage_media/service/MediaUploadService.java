package com.xuecheng.manage_media.service;

import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.model.response.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

public interface MediaUploadService {
    ResponseResult register(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt);

    CheckChunkResult checkChunk(String fileMd5, Integer chunk, Long chunkSize);

    ResponseResult uploadChunk(MultipartFile file, Integer chunk, String fileMd5);

    ResponseResult mergeChunks(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt);

    public ResponseResult sendMediaProcessTaskMsg(String mediaId);
}
