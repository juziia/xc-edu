package com.xuecheng.ucenter.controller;

import com.xuecheng.api.ucenter.UserControllerApi;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import com.xuecheng.ucenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ucenter")
public class UserController implements UserControllerApi {

    @Autowired
    private UserService userService;


    @Override
    @GetMapping("getuserext")
    public XcUserExt getUserExt(@RequestParam("username") String username) {
        return this.userService.getXcUserExt(username);
    }
}
