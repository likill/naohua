package com.jiaxin.aiweb.exception;



import com.jiaxin.aiweb.common.BaseResponse;
import com.jiaxin.aiweb.common.ResultUtils;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 全局异常处理器
 * 当出现无法控制的报错，则通过封装返回好看的异常
 */
@Hidden
@RestControllerAdvice//@ControllerAdvice只负责拦截异常；要想把你返回的 BaseResponse 序列化成 JSON 并写回给浏览器，还必须让 Spring MVC 认为“这是一个 @ResponseBody 的结果” 所以需要改为@RestControllerAdvice。
@RequestMapping(produces = "application/json;charset=UTF-8")
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException", e);
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> businessExceptionHandler(RuntimeException e) {
        log.error("RuntimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统错误");
    }

}
