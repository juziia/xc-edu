package com.xuecheng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import jdk.nashorn.internal.ir.annotations.Immutable;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.swing.plaf.multi.MultiTableUI;

/**
 * @ConrtollerAdvice:
 *          ControllerAdvice可以对所有的控制器定义全局性的配置
 *           常见的操作:
 *               ControllerAdvice与ExceptionHandler组合使用可以达到对控制器中抛出的异常进行全局处理的目的
 * @ExceptionHandler:
 *          ExceptionHandler:
 *                  声明一个方法为异常处理方法,在ExceptionHandler中可以指定多个异常类型,
 *                当抛出的异常类型是指定的异常类型时,则会进行调用当前方法
 *
 * @ControllerAdvice+@ExceptionHandler组合使用可以对控制器抛出的异常进行全局统一的处理
 *
 */
@ControllerAdvice // 增强控制器
public class CastExceptionHandler {

    /**
     *  ImmutableMap:
     *      谷歌提供的一个不可改变的线程安全的Map集合
     */
    private static ImmutableMap<Class<? extends Throwable>,ResultCode> EXCEPTION_MAP;
    protected static ImmutableMap.Builder<Class<? extends Throwable>,ResultCode> BUILDER = ImmutableMap.builder();

    static{
        // 参数非法异常
        BUILDER.put(HttpMessageNotReadableException.class, CommonCode.PARAMS_ILLEGAL);
    }


    /**
     *  @ExceptionHandler:
     *         作用:  用于捕捉Controller中抛出的不同类型的异常,从而达到异常全局处理的目的
     * @return
     */
    @ExceptionHandler(CustomException.class)
    @ResponseBody // 将错误提示信息对象转换为json返回
    public ResponseResult catchCastException(CustomException customException){
        // 获取异常中的状态信息
        ResultCode resultCode = customException.getResultCode();

        return new ResponseResult(resultCode);
    }


    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ResponseResult catchException(Throwable throwable){
        if(EXCEPTION_MAP == null){
            EXCEPTION_MAP = BUILDER.build();
        }
        ResultCode resultCode = EXCEPTION_MAP.get(throwable.getClass());
        if(resultCode != null){
            return new ResponseResult(resultCode);
        }
        return new ResponseResult(CommonCode.SERVER_ERROR);
    }


}
