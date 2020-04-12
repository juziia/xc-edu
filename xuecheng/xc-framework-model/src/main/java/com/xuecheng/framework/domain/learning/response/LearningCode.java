package com.xuecheng.framework.domain.learning.response;

import com.xuecheng.framework.model.response.ResultCode;

public enum LearningCode implements ResultCode {

    LEARNING_MEDIA_ISBLANK(false,23001,"课程媒资文件url为空"),
    CHOOSECOURSE_TASKISNULL(false,23002,"添加选课任务信息为空");
    //操作代码
    boolean success;
    //操作代码
    int code;
    //提示信息
    String message;
    private LearningCode(boolean success, int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }

    @Override
    public boolean success() {
        return success;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
