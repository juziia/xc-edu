package com.xuecheng.ucenter.service.impl;

import com.xuecheng.framework.domain.ucenter.XcCompanyUser;
import com.xuecheng.framework.domain.ucenter.XcMenu;
import com.xuecheng.framework.domain.ucenter.XcUser;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import com.xuecheng.ucenter.dao.MenuMapper;
import com.xuecheng.ucenter.dao.XcCompanyRepository;
import com.xuecheng.ucenter.dao.XcUserRepository;
import com.xuecheng.ucenter.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private XcUserRepository userRepository;
    @Autowired
    private XcCompanyRepository companyRepository;

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public XcUserExt getXcUserExt(String username) {
        XcUser xcUser = this.findUserByUsername(username);
        if(xcUser == null){
            return null;
        }
        // 根据用户id查询用户所属公司
        String userId = xcUser.getId();
        XcCompanyUser companyUser = this.findXcCompanyUserByUserId(userId);
        // 定义公司id
        String companyId = null;
        if(companyUser != null){
            companyId = companyUser.getCompanyId();
        }
        // 查询用户权限
        List<XcMenu> xcMenuList = menuMapper.getXcMenuListFromUserId(userId);
        XcUserExt xcUserExt = new XcUserExt();
        BeanUtils.copyProperties(xcUser,xcUserExt);
        xcUserExt.setCompanyId(companyId);
        xcUserExt.setPermissions(xcMenuList);
        return xcUserExt;
    }

    public XcCompanyUser findXcCompanyUserByUserId(String userId){
        return companyRepository.findByUserId(userId);
    }

    public XcUser findUserByUsername(String username){
        return this.userRepository.findByUsername(username);
    }
}
