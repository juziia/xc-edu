package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsSite;

import java.util.List;

public interface CmsSiteService {

    List<CmsSite> findAll();

    CmsSite findById(String siteId);
}
