package com.xuecheng.framework.domain.ucenter.request;

import com.xuecheng.framework.model.request.RequestData;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * Created by admin on 2018/3/5.
 */
@Data
@ToString
public class LoginRequest extends RequestData {

    @NotBlank(message = "用户名不能为空")
    String username;
    @NotBlank(message = "密码不能为空")
    String password;
    // @NotBlank(message = "请正确输入验证码")
    String verifycode;

}
