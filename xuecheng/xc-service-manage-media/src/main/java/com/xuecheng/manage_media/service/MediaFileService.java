package com.xuecheng.manage_media.service;

import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
import com.xuecheng.framework.model.response.QueryResult;

public interface MediaFileService {
    QueryResult<MediaFile> findList(int page, int size, QueryMediaFileRequest queryMediaFileRequest);
}
