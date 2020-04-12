package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsTemplate;

import java.util.List;

public interface CmsTemplateService {

    List<CmsTemplate> findAll();

    CmsTemplate findById(String templateId);
}
