package com.xuecheng.manage_media.service.impl;

import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_media.dao.MediaFileRepository;
import com.xuecheng.manage_media.service.MediaFileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.management.Query;
import java.util.List;

@Service
public class MediaFileServiceImpl implements MediaFileService {

    @Autowired
    private MediaFileRepository mediaFileRepository;

    @Override
    public QueryResult<MediaFile> findList(int page, int size, QueryMediaFileRequest queryMediaFileRequest) {
        if(queryMediaFileRequest == null){
            queryMediaFileRequest = new QueryMediaFileRequest();
        }
        MediaFile mediaFile = new MediaFile();

        if(StringUtils.isNotBlank(queryMediaFileRequest.getFileOriginalName())){
            mediaFile.setFileOriginalName(queryMediaFileRequest.getFileOriginalName());
        }
        if(StringUtils.isNotBlank(queryMediaFileRequest.getProcessStatus())){
            mediaFile.setProcessStatus(queryMediaFileRequest.getProcessStatus());
        }
        if(StringUtils.isNotBlank(queryMediaFileRequest.getTag())){
            mediaFile.setTag(queryMediaFileRequest.getTag());
        }

        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                                .withMatcher("fileOriginalName", ExampleMatcher.GenericPropertyMatchers.contains())
                                .withMatcher("tag", ExampleMatcher.GenericPropertyMatchers.contains());
        Example<MediaFile> example = Example.of(mediaFile,exampleMatcher);

        page = page >= 0 ? page : 1;
        page = page - 1;
        size = size > 10 ? size : 10;

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<MediaFile> pageResult = mediaFileRepository.findAll(example, pageRequest);
        long totalElements = pageResult.getTotalElements();
        List<MediaFile> mediaFileList = pageResult.getContent();
        QueryResult<MediaFile> queryResult = new QueryResult<>();
        queryResult.setList(mediaFileList);
        queryResult.setTotal(totalElements);
        return queryResult;
    }
}
