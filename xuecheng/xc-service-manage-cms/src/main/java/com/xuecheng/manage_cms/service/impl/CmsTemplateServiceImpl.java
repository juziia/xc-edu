package com.xuecheng.manage_cms.service.impl;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import com.xuecheng.manage_cms.service.CmsTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CmsTemplateServiceImpl implements CmsTemplateService {

    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;

    @Override
    public List<CmsTemplate> findAll() {
        return  cmsTemplateRepository.findAll();
    }

    @Override
    public CmsTemplate findById(String templateId) {
        Optional<CmsTemplate> optional = cmsTemplateRepository.findById(templateId);
        if(optional.isPresent()){
            return optional.get();
        }

        return null;
    }
}
