package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

public class CastException {

    // 用于声明异常
    public static void cast(ResultCode resultCode){
        throw new CustomException(resultCode);
    }
}
