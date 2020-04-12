package com.xuecheng.manage_course.exception;

import com.xuecheng.framework.exception.CastExceptionHandler;
import com.xuecheng.framework.model.response.CommonCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Component
public class CustomExceptionCatch extends CastExceptionHandler {
    static {
        BUILDER.put(AccessDeniedException.class, CommonCode.UNAUTHORISE);
    }
}
