package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcMenu;
import com.xuecheng.framework.domain.ucenter.ext.XcMenuExt;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper {

    List<XcMenu> getXcMenuListFromUserId(String userId);
}
