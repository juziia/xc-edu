package com.xuecheng.manage_cms.service.impl;

import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_cms.controller.SysDictionaryController;
import com.xuecheng.manage_cms.dao.SysDictionaryRepository;
import com.xuecheng.manage_cms.service.SysDictionaryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysDictionaryServiceImpl implements SysDictionaryService {

    public static final Logger LOGGER = LoggerFactory.getLogger(SysDictionaryController.class);


    @Autowired
    private SysDictionaryRepository sysDictionaryRepository;

    @Override
    public SysDictionary getByType(String type) {
        if(StringUtils.isBlank(type)){
            LOGGER.error("sysDictionary type is null or empty , type is {} ",type);
            return null;
        }
        // 根据数据字典类型获取数据字典
        SysDictionary sysDictionary = sysDictionaryRepository.findBydType(type);

        return sysDictionary;
    }
}
