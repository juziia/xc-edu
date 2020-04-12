package com.xuecheng.manage_cms.service.impl;

import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.exception.CastException;
import com.xuecheng.manage_cms.dao.CmsSiteRepository;
import com.xuecheng.manage_cms.service.CmsSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CmsSiteServiceImpl implements CmsSiteService {

    @Autowired
    private CmsSiteRepository cmsSiteRepository;

    @Override
    public List<CmsSite> findAll() {
        return cmsSiteRepository.findAll();
    }

    @Override
    public CmsSite findById(String siteId) {
        Optional<CmsSite> optional = this.cmsSiteRepository.findById(siteId);
        if(optional.isPresent()){
            return optional.get();
        }
        CastException.cast(CmsCode.CMS_SITE_NOEXIST);
        return null;
    }
}
