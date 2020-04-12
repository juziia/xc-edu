package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

public class CustomException extends RuntimeException {

    // 状态码对象
    private ResultCode resultCode;

    public CustomException(ResultCode resultCode){
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode(){
        return this.resultCode;
    }

}
