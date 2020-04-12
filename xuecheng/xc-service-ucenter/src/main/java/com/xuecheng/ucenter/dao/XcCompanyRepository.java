package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcCompany;
import com.xuecheng.framework.domain.ucenter.XcCompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface XcCompanyRepository extends JpaRepository<XcCompanyUser,String> {

    XcCompanyUser findByUserId(String userId);
}
